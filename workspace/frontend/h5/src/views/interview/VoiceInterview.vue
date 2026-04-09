<template>
  <div class="voice-interview-page">
    <div class="page-header">
      <van-icon name="cross" size="20" @click="handleClose" />
      <div class="header-center">
        <span class="title">语音面试</span>
        <span class="timer">{{ formatTime(remainingTime) }}</span>
      </div>
      <van-icon name="question-o" size="20" @click="showGuide = true" />
    </div>

    <div class="interview-status" v-if="currentQuestion">
      <div class="progress">
        <span>问题 {{ currentIndex + 1 }} / {{ questions.length }}</span>
        <div class="progress-bar">
          <div class="progress-fill" :style="{ width: `${progress}%` }"></div>
        </div>
      </div>
      <div class="status-indicator">
        <div class="indicator-dot" :class="{ recording: isRecording }"></div>
        <span>{{ isRecording ? '录音中...' : '等待回答' }}</span>
      </div>
    </div>

    <div class="chat-container" ref="chatContainer">
      <!-- AI开场 -->
      <div class="message ai-message" v-if="showIntro">
        <div class="avatar ai-avatar">
          <van-icon name="manager-o" size="24" />
        </div>
        <div class="message-content">
          <p>您好，我是您的AI面试官。请问您是{{ userName }}吗？今天我将为您进行一次{{ jobTitle }}的语音面试。</p>
          <p>面试将包括技术问题、项目经验和行为面试三个环节。听到问题后，请点击下方录音按钮作答。</p>
          <p class="time">{{ formatTimeStr(currentTime) }}</p>
        </div>
      </div>

      <!-- 消息列表 -->
      <div v-for="(msg, index) in messages" :key="index">
        <!-- AI消息 -->
        <div class="message ai-message" v-if="msg.role === 'ai'">
          <div class="avatar ai-avatar">
            <van-icon name="manager-o" size="24" />
          </div>
          <div class="message-content">
            <p>{{ msg.content }}</p>
            <p class="time">{{ msg.time }}</p>
          </div>
        </div>

        <!-- 用户语音消息 -->
        <div class="message user-message" v-else>
          <div class="message-content voice-message">
            <div class="voice-wave" @click="playAudio(msg.audioUrl, index)">
              <van-icon :name="msg.playing ? 'pause-circle-o' : 'play-circle-o'" size="24" />
              <div class="wave-bars">
                <span v-for="i in 5" :key="i" :class="{ active: msg.playing }"></span>
              </div>
              <span class="duration">{{ msg.duration }}"</span>
            </div>
            <p class="time">{{ msg.time }}</p>
          </div>
          <div class="avatar user-avatar">
            <van-icon name="user-o" size="24" />
          </div>
        </div>
      </div>

      <!-- 当前问题 -->
      <div class="message ai-message" v-if="currentQuestion">
        <div class="avatar ai-avatar">
          <van-icon name="manager-o" size="24" />
        </div>
        <div class="message-content">
          <p class="question">{{ currentQuestion.content }}</p>
          <div class="message-tips" v-if="currentQuestion.tips">
            <van-icon name="info-o" />
            <span>{{ currentQuestion.tips }}</span>
          </div>
          <p class="time">{{ formatTimeStr(currentTime) }}</p>
        </div>
      </div>

      <!-- AI思考中 -->
      <div class="message ai-message" v-if="aiThinking">
        <div class="avatar ai-avatar">
          <van-icon name="manager-o" size="24" />
        </div>
        <div class="message-content loading">
          <van-loading type="spinner" size="20px" />
          <span>面试官正在思考...</span>
        </div>
      </div>
    </div>

    <!-- 语音录制组件 -->
    <VoiceRecorder
      v-if="showRecorder"
      :tips="isRecording ? '点击停止录音' : '点击开始录音'"
      :max-duration="60"
      @start="handleRecordingStart"
      @stop="handleRecordingStop"
      @upload="handleAudioUpload"
    />

    <!-- 底部操作 -->
    <div class="bottom-actions" v-if="!showRecorder && currentQuestion">
      <van-button
        type="primary"
        size="large"
        :icon="isRecording ? 'stop-circle-o' : 'volume-o'"
        :class="{ recording: isRecording }"
        @click="toggleRecording"
      >
        {{ isRecording ? '停止录音' : '开始回答' }}
      </van-button>
      <div class="skip-btn">
        <span @click="handleSkip">跳过此题</span>
      </div>
    </div>

    <!-- 面试结束 -->
    <van-popup
      v-model:show="showEndConfirm"
      position="bottom"
      round
      :close-on-click-overlay="false"
    >
      <div class="end-popup">
        <div class="end-icon">
          <van-icon name="checked" size="48" color="#34A853" />
        </div>
        <h3>面试完成</h3>
        <p>感谢您的参与，面试报告生成中...</p>
        <van-button type="primary" block @click="goToReport">
          查看报告
        </van-button>
      </div>
    </van-popup>

    <!-- 面试指导弹窗 -->
    <van-popup v-model:show="showGuide" position="bottom" round>
      <div class="guide-popup">
        <h3>语音面试须知</h3>
        <div class="guide-content">
          <div class="guide-item">
            <van-icon name="volume-o" size="20" color="#1A73E8" />
            <div>
              <h4>录音权限</h4>
              <p>请允许使用麦克风权限</p>
            </div>
          </div>
          <div class="guide-item">
            <van-icon name="clock-o" size="20" color="#34A853" />
            <div>
              <h4>录音时长</h4>
              <p>建议每次回答10-60秒</p>
            </div>
          </div>
          <div class="guide-item">
            <van-icon name="success" size="20" color="#FB8C00" />
            <div>
              <h4>网络环境</h4>
              <p>建议在网络稳定的环境下面试</p>
            </div>
          </div>
        </div>
        <van-button type="primary" block @click="showGuide = false">
          我知道了
        </van-button>
      </div>
    </van-popup>

    <!-- 结束面试确认 -->
    <van-dialog
      v-model:show="showExitConfirm"
      title="确认结束面试？"
      message="面试尚未完成，确认要退出吗？"
      show-cancel-button
      @confirm="confirmEnd"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showToast } from 'vant'
import VoiceRecorder from '@/components/VoiceRecorder.vue'

const router = useRouter()
const route = useRoute()

// 状态
const interviewId = ref('')
const questions = ref([])
const currentIndex = ref(0)
const messages = ref([])
const isRecording = ref(false)
const aiThinking = ref(false)
const showGuide = ref(false)
const showEndConfirm = ref(false)
const showExitConfirm = ref(false)
const showRecorder = ref(true)
const remainingTime = ref(0)
const currentTime = ref(Date.now())
const duration = ref(30)
const chatContainer = ref(null)
const timer = ref(null)
const audioPlayingIndex = ref(-1)

// 模拟数据
const userName = ref('求职者')
const jobTitle = ref('前端工程师')

// 计算属性
const currentQuestion = computed(() => {
  if (currentIndex.value < questions.value.length) {
    return questions.value[currentIndex.value]
  }
  return null
})

const progress = computed(() => {
  if (questions.value.length === 0) return 0
  return Math.round((currentIndex.value / questions.value.length) * 100)
})

const showIntro = computed(() => {
  return messages.value.length === 0 && !aiThinking.value
})

// 初始化
onMounted(async () => {
  interviewId.value = route.query.id || 'demo'

  // 获取用户信息
  const userData = localStorage.getItem('userInfo')
  if (userData) {
    userName.value = JSON.parse(userData).name || '求职者'
  }

  // 加载问题
  await loadQuestions()

  // 开始计时
  remainingTime.value = duration.value * 60
  startTimer()
})

// 加载问题
const loadQuestions = async () => {
  aiThinking.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 1500))

    questions.value = [
      {
        id: 1,
        type: 'technical',
        content: '请介绍一下你在前端开发中使用Vue框架的经验，以及Vue3相比Vue2有哪些重要的改进？',
        tips: '可以从响应式原理、Composition API、性能优化等方面回答'
      },
      {
        id: 2,
        type: 'project',
        content: '请描述一个你最引以为豪的项目经历，你在其中承担了什么角色？遇到了哪些挑战？',
        tips: '建议使用STAR法则描述'
      },
      {
        id: 3,
        type: 'behavioral',
        content: '如果你发现同事的代码存在严重的安全隐患，你会怎么处理？',
        tips: '考察团队协作和问题解决能力'
      }
    ]

    aiThinking.value = false
  } catch (error) {
    console.error('Load questions failed:', error)
    showToast('加载问题失败')
    aiThinking.value = false
  }
}

// 切换录音状态
const toggleRecording = () => {
  if (isRecording.value) {
    stopRecording()
  } else {
    startRecording()
  }
}

// 开始录音
const startRecording = () => {
  isRecording.value = true
  showRecorder.value = true
}

// 停止录音
const stopRecording = () => {
  isRecording.value = false
}

// 录音开始回调
const handleRecordingStart = () => {
  console.log('Recording started')
}

// 录音停止回调
const handleRecordingStop = async ({ duration: dur }) => {
  console.log('Recording stopped:', dur)
}

// 音频上传
const handleAudioUpload = async (formData) => {
  try {
    // 模拟上传
    await new Promise(resolve => setTimeout(resolve, 1000))

    // 添加用户消息
    messages.value.push({
      role: 'user',
      content: '[语音回答]',
      audioUrl: 'demo',
      duration: 15,
      playing: false,
      time: formatTimeStr(Date.now())
    })

    showRecorder.value = false
    scrollToBottom()

    // AI思考
    aiThinking.value = true
    await new Promise(resolve => setTimeout(resolve, 2000))

    // AI回复
    const response = generateAiResponse(currentQuestion.value)
    messages.value.push({
      role: 'ai',
      content: response,
      time: formatTimeStr(Date.now())
    })

    aiThinking.value = false

    // 下一题
    if (currentIndex.value < questions.value.length - 1) {
      currentIndex.value++
      showRecorder.value = true
    } else {
      // 面试结束
      setTimeout(() => {
        showEndConfirm.value = true
      }, 2000)
    }

    scrollToBottom()
  } catch (error) {
    console.error('Upload failed:', error)
    showToast('上传失败，请重试')
  }
}

// 播放音频
const playAudio = (url, index) => {
  if (audioPlayingIndex.value === index) {
    // 暂停
    messages.value[index].playing = false
    audioPlayingIndex.value = -1
  } else {
    // 播放
    if (audioPlayingIndex.value !== -1) {
      messages.value[audioPlayingIndex.value].playing = false
    }
    messages.value[index].playing = true
    audioPlayingIndex.value = index

    // 模拟播放完成
    setTimeout(() => {
      messages.value[index].playing = false
      audioPlayingIndex.value = -1
    }, 3000)
  }
}

// 生成AI回复
const generateAiResponse = (question) => {
  const responses = {
    technical: '您的回答很详细，特别是对Vue3新特性的理解比较深入。我想追问一下，您在项目中有使用过Vue3的Composition API吗？',
    project: '感谢您的分享，项目经验很丰富。接下来我想了解一下，您在这个项目中的技术难点是什么？是如何解决的？',
    behavioral: '我理解您的顾虑。请问您当时具体是怎么处理这个情况的？最终的结果如何？'
  }
  return responses[question.type] || responses.technical
}

// 跳过
const handleSkip = async () => {
  if (currentIndex.value < questions.value.length - 1) {
    currentIndex.value++
    showRecorder.value = true
    scrollToBottom()
  } else {
    showEndConfirm.value = true
  }
}

// 格式化时间
const formatTime = (seconds) => {
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${mins}:${secs.toString().padStart(2, '0')}`
}

const formatTimeStr = (timestamp) => {
  const date = new Date(timestamp)
  return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (chatContainer.value) {
      chatContainer.value.scrollTop = chatContainer.value.scrollHeight
    }
  })
}

// 开始计时器
const startTimer = () => {
  timer.value = setInterval(() => {
    if (remainingTime.value > 0) {
      remainingTime.value--
    } else {
      clearInterval(timer.value)
      showToast('面试时间到')
      showEndConfirm.value = true
    }
  }, 1000)
}

// 关闭
const handleClose = () => {
  showExitConfirm.value = true
}

// 确认结束
const confirmEnd = () => {
  clearInterval(timer.value)
  showEndConfirm.value = true
}

// 跳转报告
const goToReport = () => {
  clearInterval(timer.value)
  router.replace(`/interview/report/${interviewId.value}`)
}

onUnmounted(() => {
  if (timer.value) {
    clearInterval(timer.value)
  }
})
</script>

<style lang="scss" scoped>
.voice-interview-page {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #F8F9FA;
  padding-bottom: 180px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: white;
  border-bottom: 1px solid #E8EAED;

  .header-center {
    display: flex;
    flex-direction: column;
    align-items: center;

    .title {
      font-size: 16px;
      font-weight: 600;
      color: #202124;
    }

    .timer {
      font-size: 12px;
      color: #EA4335;
      font-weight: 500;
    }
  }

  .van-icon {
    color: #5F6368;
  }
}

.interview-status {
  background: white;
  padding: 12px 16px;
  border-bottom: 1px solid #E8EAED;

  .progress {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 8px;

    span {
      font-size: 13px;
      color: #5F6368;
      white-space: nowrap;
    }

    .progress-bar {
      flex: 1;
      height: 4px;
      background: #E8EAED;
      border-radius: 2px;

      .progress-fill {
        height: 100%;
        background: #1A73E8;
        border-radius: 2px;
        transition: width 0.3s;
      }
    }
  }

  .status-indicator {
    display: flex;
    align-items: center;
    gap: 8px;

    .indicator-dot {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      background: #9AA0A6;

      &.recording {
        background: #EA4335;
        animation: pulse 1s infinite;
      }
    }

    span {
      font-size: 13px;
      color: #5F6368;
    }
  }
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.chat-container {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
}

.message {
  display: flex;
  margin-bottom: 16px;

  .avatar {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
  }

  .ai-avatar {
    background: #F1F3F4;
  }

  .user-avatar {
    background: #E8F0FE;
  }

  .message-content {
    max-width: 75%;
    padding: 12px 16px;
    border-radius: 12px;

    p {
      font-size: 14px;
      line-height: 1.6;
      color: #202124;
      margin: 0;
    }

    .time {
      font-size: 11px;
      color: #9AA0A6;
      margin-top: 8px !important;
    }

    .question {
      font-weight: 500;
    }

    .message-tips {
      display: flex;
      align-items: flex-start;
      gap: 6px;
      margin-top: 10px;
      padding: 10px;
      background: #FFF8E1;
      border-radius: 8px;
      font-size: 12px;
      color: #F57C00;

      .van-icon {
        flex-shrink: 0;
        margin-top: 2px;
      }
    }

    &.loading {
      display: flex;
      align-items: center;
      gap: 8px;
      background: #F1F3F4;

      span {
        font-size: 13px;
        color: #5F6368;
      }
    }

    &.voice-message {
      background: #1A73E8;
      padding: 8px 12px;

      .voice-wave {
        display: flex;
        align-items: center;
        gap: 8px;
        color: white;

        .van-icon {
          color: white;
        }

        .wave-bars {
          display: flex;
          align-items: center;
          gap: 3px;
          height: 20px;

          span {
            width: 3px;
            height: 8px;
            background: rgba(255, 255, 255, 0.5);
            border-radius: 2px;
            transition: height 0.2s;

            &.active {
              height: 16px;
              background: white;
            }
          }
        }

        .duration {
          font-size: 12px;
          opacity: 0.8;
        }
      }

      .time {
        color: rgba(255, 255, 255, 0.7);
      }
    }
  }

  &.ai-message {
    .avatar {
      margin-right: 10px;
    }

    .message-content {
      background: white;
      border-bottom-left-radius: 4px;
    }
  }

  &.user-message {
    flex-direction: row-reverse;

    .message-content {
      border-bottom-right-radius: 4px;
    }
  }
}

.bottom-actions {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px;
  background: white;
  border-top: 1px solid #E8EAED;
  max-width: 600px;
  margin: 0 auto;
  left: 50%;
  transform: translateX(-50%);

  .van-button {
    border-radius: 24px;

    &.recording {
      background: #EA4335;
      border-color: #EA4335;
    }
  }

  .skip-btn {
    text-align: center;
    margin-top: 12px;

    span {
      font-size: 13px;
      color: #5F6368;
    }
  }
}

.end-popup {
  padding: 32px 24px;
  text-align: center;

  .end-icon {
    width: 80px;
    height: 80px;
    background: #E8F5E9;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 20px;
  }

  h3 {
    font-size: 20px;
    font-weight: 600;
    color: #202124;
    margin: 0 0 12px;
  }

  p {
    font-size: 14px;
    color: #5F6368;
    margin: 0 0 24px;
  }
}

.guide-popup {
  padding: 20px;

  h3 {
    font-size: 18px;
    font-weight: 600;
    color: #202124;
    margin: 0 0 20px;
    text-align: center;
  }

  .guide-content {
    margin-bottom: 24px;

    .guide-item {
      display: flex;
      align-items: flex-start;
      gap: 12px;
      padding: 12px 0;
      border-bottom: 1px solid #E8EAED;

      &:last-child {
        border-bottom: none;
      }

      > div {
        flex: 1;

        h4 {
          font-size: 14px;
          font-weight: 600;
          color: #202124;
          margin: 0 0 4px;
        }

        p {
          font-size: 13px;
          color: #5F6368;
          margin: 0;
        }
      }
    }
  }
}
</style>
