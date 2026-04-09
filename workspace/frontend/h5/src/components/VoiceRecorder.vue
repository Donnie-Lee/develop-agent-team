<template>
  <div class="voice-recorder">
    <div class="recorder-status">
      <span v-if="status === 'idle'" class="status-text">点击开始录音</span>
      <span v-else-if="status === 'recording'" class="status-text recording">
        <span class="dot"></span>
        录音中 {{ formatDuration(duration) }}
      </span>
      <span v-else-if="status === 'playing'" class="status-text playing">
        播放中 {{ formatDuration(playbackTime) }} / {{ formatDuration(duration) }}
      </span>
      <span v-else-if="status === 'uploading'" class="status-text">
        上传中...
      </span>
      <span v-else-if="status === 'done'" class="status-text done">
        录音完成
      </span>
    </div>

    <div class="waveform" v-if="status !== 'idle'">
      <div
        v-for="(level, index) in waveformLevels"
        :key="index"
        class="wave-bar"
        :style="{ height: `${level}%` }"
        :class="{ active: status === 'recording' }"
      ></div>
    </div>

    <div class="controls">
      <!-- 录音按钮 -->
      <div class="main-control">
        <div
          class="record-btn"
          :class="{ recording: status === 'recording' }"
          @click="handleRecordClick"
        >
          <van-icon :name="status === 'recording' ? 'stop-circle-o' : 'record'" size="48" />
        </div>
      </div>

      <!-- 播放控制 -->
      <div class="playback-control" v-if="audioUrl && status !== 'recording'">
        <van-button
          size="small"
          :icon="status === 'playing' ? 'pause-circle-o' : 'play-circle-o'"
          @click="togglePlayback"
        >
          {{ status === 'playing' ? '暂停' : '播放' }}
        </van-button>
      </div>

      <!-- 重新录制 -->
      <div class="re-record" v-if="audioUrl && status !== 'recording'">
        <van-button size="small" @click="reRecord">
          重新录制
        </van-button>
      </div>

      <!-- 上传按钮 -->
      <div class="upload-control" v-if="audioUrl && status !== 'uploading'">
        <van-button
          type="primary"
          size="small"
          :loading="status === 'uploading'"
          :disabled="!canUpload"
          @click="handleUpload"
        >
          上传
        </van-button>
      </div>
    </div>

    <div class="tips" v-if="tips">
      <van-icon name="info-o" />
      <span>{{ tips }}</span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { showToast, showFailToast } from 'vant'
import { uploadFile } from '@/api/interview'

const props = defineProps({
  tips: {
    type: String,
    default: '建议录音时长10-60秒'
  },
  maxDuration: {
    type: Number,
    default: 60
  }
})

const emit = defineEmits(['upload', 'start', 'stop'])

// 状态: idle, recording, playing, uploading, done
const status = ref('idle')
const duration = ref(0)
const playbackTime = ref(0)
const audioUrl = ref('')
const audioBlob = ref(null)
const waveformLevels = ref([])

// 音频相关
let mediaRecorder = null
let audioContext = null
let analyser = null
let audioElement = null
let animationId = null
let timerInterval = null

const canUpload = computed(() => {
  return audioBlob.value && status.value !== 'uploading'
})

// 格式化时间
const formatDuration = (seconds) => {
  const mins = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${mins}:${secs.toString().padStart(2, '0')}`
}

// 初始化录音
const initRecorder = async () => {
  try {
    const stream = await navigator.mediaDevices.getUserMedia({ audio: true })
    mediaRecorder = new MediaRecorder(stream)
    audioContext = new AudioContext()
    analyser = audioContext.createAnalyser()
    const source = audioContext.createMediaStreamSource(stream)
    source.connect(analyser)
    analyser.fftSize = 64

    // 监听数据
    const chunks = []
    mediaRecorder.ondataavailable = (e) => {
      chunks.push(e.data)
    }

    mediaRecorder.onstop = () => {
      audioBlob.value = new Blob(chunks, { type: 'audio/webm' })
      audioUrl.value = URL.createObjectURL(audioBlob.value)
      stream.getTracks().forEach(track => track.stop())
      status.value = 'done'
    }

    // 更新波形
    updateWaveform()
  } catch (error) {
    console.error('Init recorder failed:', error)
    showFailToast('无法访问麦克风，请检查权限设置')
  }
}

// 更新波形
const updateWaveform = () => {
  if (!analyser) return

  const dataArray = new Uint8Array(analyser.frequencyBinCount)
  analyser.getByteFrequencyData(dataArray)

  const levels = []
  const step = Math.floor(dataArray.length / 20)
  for (let i = 0; i < 20; i++) {
    levels.push(Math.max(10, dataArray[i * step] / 2))
  }
  waveformLevels.value = levels

  if (status.value === 'recording') {
    animationId = requestAnimationFrame(updateWaveform)
  }
}

// 开始录音
const startRecording = async () => {
  if (!mediaRecorder) {
    await initRecorder()
  }

  if (mediaRecorder && mediaRecorder.state === 'inactive') {
    duration.value = 0
    waveformLevels.value = []
    mediaRecorder.start()
    status.value = 'recording'
    emit('start')

    // 计时
    timerInterval = setInterval(() => {
      duration.value++
      if (duration.value >= props.maxDuration) {
        stopRecording()
      }
    }, 1000)
  }
}

// 停止录音
const stopRecording = () => {
  if (mediaRecorder && mediaRecorder.state === 'recording') {
    mediaRecorder.stop()
    status.value = 'done'
    emit('stop', { duration: duration.value, blob: audioBlob.value })

    if (timerInterval) {
      clearInterval(timerInterval)
      timerInterval = null
    }
    if (animationId) {
      cancelAnimationFrame(animationId)
      animationId = null
    }
  }
}

// 处理录音按钮点击
const handleRecordClick = () => {
  if (status.value === 'recording') {
    stopRecording()
  } else {
    startRecording()
  }
}

// 重新录制
const reRecord = () => {
  audioUrl.value = ''
  audioBlob.value = null
  duration.value = 0
  playbackTime.value = 0
  waveformLevels.value = []
  status.value = 'idle'
}

// 播放/暂停
const togglePlayback = () => {
  if (!audioElement) {
    audioElement = new Audio(audioUrl.value)
    audioElement.onended = () => {
      status.value = 'done'
      playbackTime.value = 0
    }
    audioElement.ontimeupdate = () => {
      playbackTime.value = Math.floor(audioElement.currentTime)
    }
  }

  if (status.value === 'playing') {
    audioElement.pause()
    status.value = 'done'
  } else {
    audioElement.play()
    status.value = 'playing'
  }
}

// 上传
const handleUpload = async () => {
  if (!audioBlob.value) return

  status.value = 'uploading'

  try {
    const formData = new FormData()
    formData.append('audio', audioBlob.value, `recording_${Date.now()}.webm`)
    formData.append('duration', duration.value)

    emit('upload', formData)
    showToast('上传成功')
  } catch (error) {
    console.error('Upload failed:', error)
    showToast('上传失败，请重试')
    status.value = 'done'
  }
}

// 清理
onUnmounted(() => {
  if (timerInterval) clearInterval(timerInterval)
  if (animationId) cancelAnimationFrame(animationId)
  if (audioElement) {
    audioElement.pause()
    audioElement = null
  }
  if (audioContext) audioContext.close()
})
</script>

<style lang="scss" scoped>
.voice-recorder {
  background: white;
  border-radius: 12px;
  padding: 20px;
  margin: 16px 0;
}

.recorder-status {
  text-align: center;
  margin-bottom: 16px;

  .status-text {
    font-size: 14px;
    color: #5F6368;

    &.recording {
      color: #EA4335;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
    }

    &.playing {
      color: #1A73E8;
    }

    &.done {
      color: #34A853;
    }
  }

  .dot {
    width: 8px;
    height: 8px;
    background: #EA4335;
    border-radius: 50%;
    animation: pulse 1s infinite;
  }
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.waveform {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  height: 60px;
  margin-bottom: 20px;

  .wave-bar {
    width: 4px;
    background: #E8EAED;
    border-radius: 2px;
    transition: height 0.1s ease;

    &.active {
      background: #1A73E8;
    }
  }
}

.controls {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;

  .main-control {
    .record-btn {
      width: 72px;
      height: 72px;
      border-radius: 50%;
      background: #F8F9FA;
      display: flex;
      align-items: center;
      justify-content: center;
      border: 3px solid #E8EAED;
      transition: all 0.2s;

      &:active {
        transform: scale(0.95);
      }

      &.recording {
        background: #FFEBEE;
        border-color: #EA4335;

        .van-icon {
          color: #EA4335;
        }
      }
    }
  }

  .playback-control,
  .re-record,
  .upload-control {
    .van-button {
      min-width: 80px;
    }
  }
}

.tips {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  margin-top: 16px;
  font-size: 12px;
  color: #9AA0A6;

  .van-icon {
    font-size: 14px;
  }
}
</style>
