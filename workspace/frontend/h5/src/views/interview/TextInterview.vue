<template>
  <div class="text-interview-page">
    <div class="page-header">
      <van-icon name="cross" size="20" @click="handleClose" />
      <div class="header-center">
        <span class="title">文字面试</span>
        <span class="timer">{{ formatTime(remainingTime) }}</span>
      </div>
      <van-icon name="question-o" size="20" @click="showGuide = true" />
    </div>

    <div class="interview-info" v-if="currentQuestion">
      <div class="progress">
        <span>问题 {{ currentIndex + 1 }} / {{ questions.length }}</span>
        <div class="progress-bar">
          <div class="progress-fill" :style="{ width: `${progress}%` }"></div>
        </div>
      </div>
      <div class="question-type">
        <van-tag :type="getTagType(currentQuestion.type)" size="small">
          {{ getTypeLabel(currentQuestion.type) }}
        </van-tag>
      </div>
    </div>

    <div class="chat-container" ref="chatContainer">
      <!-- AI开场 -->
      <div class="message ai-message" v-if="showIntro">
        <div class="avatar">
          <van-icon name="manager-o" size="24" />
        </div>
        <div class="message-content">
          <p>您好，我是您的AI面试官。请问您是{{ userName }}吗？今天我将为您进行一次{{ jobTitle }}的面试。</p>
          <p>面试将包括技术问题、项目经验和行为面试三个环节。请您认真思考后作答。</p>
          <p class="time">{{ formatTime(currentTime) }}</p>
        </div>
      </div>

      <!-- 加载状态 -->
      <div class="message ai-message" v-if="loading && messages.length === 0">
        <div class="avatar">
          <van-icon name="manager-o" size="24" />
        </div>
        <div class="message-content loading">
          <van-loading type="spinner" size="20px" />
          <span>面试官正在思考...</span>
        </div>
      </div>

      <!-- 消息列表 -->
      <div v-for="(msg, index) in messages" :key="index">
        <!-- AI消息 -->
        <div class="message ai-message" v-if="msg.role === 'ai'">
          <div class="avatar">
            <van-icon name="manager-o" size="24" />
          </div>
          <div class="message-content">
            <p>{{ msg.content }}</p>
            <div class="message-actions" v-if="msg.suggestions">
              <span
                v-for="suggestion in msg.suggestions"
                :key="suggestion"
                class="suggestion"
                @click="handleSuggestion(suggestion)"
              >
                {{ suggestion }}
              </span>
            </div>
            <p class="time">{{ msg.time }}</p>
          </div>
        </div>

        <!-- 用户消息 -->
        <div class="message user-message" v-else>
          <div class="message-content">
            <p>{{ msg.content }}</p>
            <p class="time">{{ msg.time }}</p>
          </div>
          <div class="avatar">
            <van-icon name="user-o" size="24" />
          </div>
        </div>
      </div>

      <!-- 当前问题 -->
      <div class="message ai-message" v-if="currentQuestion">
        <div class="avatar">
          <van-icon name="manager-o" size="24" />
        </div>
        <div class="message-content">
          <p class="question">{{ currentQuestion.content }}</p>
          <div class="message-tips" v-if="currentQuestion.tips">
            <van-icon name="info-o" />
            <span>{{ currentQuestion.tips }}</span>
          </div>
          <p class="time">{{ formatTime(currentTime) }}</p>
        </div>
      </div>
    </div>

    <div class="input-area">
      <van-field
        v-model="answerText"
        type="textarea"
        placeholder="请输入您的回答..."
        rows="2"
        autosize
        maxlength="2000"
        show-word-limit
        @keydown.enter.exact.prevent="handleSend"
      />
      <div class="input-actions">
        <div class="char-count">{{ answerText.length }} / 2000</div>
        <van-button
          type="primary"
          size="small"
          :disabled="!canSend"
          :loading="submitting"
          @click="handleSend"
        >
          发送
        </van-button>
      </div>
      <div class="quick-actions">
        <span class="quick-btn" @click="handleFollowUp">
          <van-icon name="chat-o" />
          追问
        </span>
        <span class="quick-btn" @click="handleSkip">
          <van-icon name="skip" />
          跳过
        </span>
      </div>
    </div>

    <!-- 面试指导弹窗 -->
    <van-popup v-model:show="showGuide" position="bottom" round>
      <div class="guide-popup">
        <h3>面试须知</h3>
        <div class="guide-content">
          <div class="guide-item">
            <van-icon name="clock-o" size="20" color="#1A73E8" />
            <div>
              <h4>面试时长</h4>
              <p>本次面试约{{ duration }}分钟</p>
            </div>
          </div>
          <div class="guide-item">
            <van-icon name="description" size="20" color="#34A853" />
            <div>
              <h4>问题数量</h4>
              <p>共{{ questions.length }}道问题</p>
            </div>
          </div>
          <div class="guide-item">
            <van-icon name="bulb-o" size="20" color="#FB8C00" />
            <div>
              <h4>答题建议</h4>
              <p>建议回答完整、逻辑清晰</p>
            </div>
          </div>
          <div class="guide-item">
            <van-icon name="warning-o" size="20" color="#EA4335" />
            <div>
              <h4>注意事项</h4>
              <p>退出后面试将自动结束</p>
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
      v-model:show="showEndConfirm"
      title="确认结束面试？"
      message="面试尚未完成，确认要退出吗？"
      show-cancel-button
      @confirm="confirmEnd"
      @cancel="showEndConfirm = false"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showToast, showConfirmDialog } from 'vant'
import { useInterviewStore } from '@/stores/interview'

const router = useRouter()
const route = useRoute()
const interviewStore = useInterviewStore()

// 状态
const interviewId = ref('')
const questions = ref([])
const currentIndex = ref(0)
const messages = ref([])
const answerText = ref('')
const loading = ref(true)
const submitting = ref(false)
const showGuide = ref(false)
const showEndConfirm = ref(false)
const remainingTime = ref(0)
const currentTime = ref(Date.now())
const duration = ref(30)
const chatContainer = ref(null)
const timer = ref(null)

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

const canSend = computed(() => {
  return answerText.value.trim().length > 0 && !submitting.value
})

const showIntro = computed(() => {
  return messages.value.length === 0 && !loading.value
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
  loading.value = true
  try {
    // 模拟获取问题列表
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

    loading.value = false
  } catch (error) {
    console.error('Load questions failed:', error)
    showToast('加载问题失败')
    loading.value = false
  }
}

// 发送回答
const handleSend = async () => {
  if (!canSend.value) return

  const content = answerText.value.trim()
  answerText.value = ''

  // 添加用户消息
  messages.value.push({
    role: 'user',
    content,
    time: formatTime(Date.now())
  })

  scrollToBottom()

  submitting.value = true

  try {
    // 模拟提交回答
    await new Promise(resolve => setTimeout(resolve, 1000))

    // 添加AI回复
    const aiResponse = generateAiResponse(currentQuestion.value)
    messages.value.push({
      role: 'ai',
      content: aiResponse.text,
      suggestions: aiResponse.suggestions,
      time: formatTime(Date.now())
    })

    // 下一题
    if (currentIndex.value < questions.value.length - 1) {
      currentIndex.value++
    } else {
      // 面试结束
      setTimeout(() => {
        goToReport()
      }, 2000)
    }
  } catch (error) {
    console.error('Submit answer failed:', error)
    showToast('提交失败，请重试')
  } finally {
    submitting.value = false
    scrollToBottom()
  }
}

// 生成AI回复
const generateAiResponse = (question) => {
  const responses = {
    technical: {
      text: '您的回答很详细，特别是对Vue3新特性的理解比较深入。我想追问一下，您在项目中有使用过Vue3的Composition API吗？能举个例子说明其优势吗？',
      suggestions: ['使用过', '没有使用过']
    },
    project: {
      text: '感谢您的分享，项目经验很丰富。接下来我想了解一下，您在这个项目中的技术难点是什么？是如何解决的？',
      suggestions: ['性能优化', '架构设计', '团队协作']
    },
    behavioral: {
      text: '我理解您的顾虑。请问您当时具体是怎么处理这个情况的？最终的结果如何？',
      suggestions: ['直接沟通', '上报Leader', '记录问题']
    }
  }
  return responses[question.type] || responses.technical
}

// 追问
const handleFollowUp = () => {
  answerText.value = '追问：'
}

// 跳过
const handleSkip = async () => {
  if (currentIndex.value < questions.value.length - 1) {
    try {
      await showConfirmDialog({
        title: '确认跳过',
        message: '跳过此题将不计入面试评分，确定要跳过吗？'
      })
      currentIndex.value++
    } catch {
      // 用户取消
    }
  } else {
    goToReport()
  }
}

// 建议点击
const handleSuggestion = (suggestion) => {
  answerText.value = suggestion
}

// 格式化时间
const formatTime = (timestamp) => {
  const date = new Date(timestamp)
  return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
}

// 计时器格式化
const formatTime2 = (seconds) => {
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${mins}:${secs.toString().padStart(2, '0')}`
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
      goToReport()
    }
  }, 1000)
}

// 关闭
const handleClose = () => {
  showEndConfirm.value = true
}

// 确认结束
const confirmEnd = async () => {
  clearInterval(timer.value)
  goToReport()
}

// 跳转报告
const goToReport = () => {
  clearInterval(timer.value)
  router.replace(`/interview/report/${interviewId.value}`)
}

// 类型标签
const getTagType = (type) => {
  const map = {
    technical: 'primary',
    project: 'success',
    behavioral: 'warning'
  }
  return map[type] || 'default'
}

const getTypeLabel = (type) => {
  const map = {
    technical: '技术问题',
    project: '项目经历',
    behavioral: '行为面试'
  }
  return map[type] || '其他'
}

onUnmounted(() => {
  if (timer.value) {
    clearInterval(timer.value)
  }
})
</script>

<style lang="scss" scoped>
.text-interview-page {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #F8F9FA;
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

.interview-info {
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

  .question-type {
    :deep(.van-tag) {
      font-size: 12px;
    }
  }
}

.chat-container {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  padding-bottom: 200px;
}

.message {
  display: flex;
  margin-bottom: 16px;

  .avatar {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    background: #F1F3F4;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
  }

  .message-content {
    max-width: 75%;
    padding: 12px 16px;
    border-radius: 12px;
    position: relative;

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

    .message-actions {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      margin-top: 12px;

      .suggestion {
        font-size: 13px;
        color: #1A73E8;
        background: #E8F0FE;
        padding: 6px 12px;
        border-radius: 16px;
        cursor: pointer;

        &:active {
          background: #D2E3FC;
        }
      }
    }

    &.loading {
      display: flex;
      align-items: center;
      gap: 8px;
      background: #F1F3F4;

      .van-loading {
        flex-shrink: 0;
      }

      span {
        font-size: 13px;
        color: #5F6368;
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

    .avatar {
      margin-left: 10px;
      background: #E8F0FE;
    }

    .message-content {
      background: #1A73E8;
      color: white;
      border-bottom-right-radius: 4px;

      p {
        color: white;
      }

      .time {
        color: rgba(255, 255, 255, 0.7);
      }
    }
  }
}

.input-area {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: white;
  border-top: 1px solid #E8EAED;
  padding: 12px 16px;
  max-width: 600px;
  margin: 0 auto;
  left: 50%;
  transform: translateX(-50%);

  :deep(.van-field) {
    background: #F8F9FA;
    border-radius: 8px;
    padding: 10px 12px;

    .van-field__control {
      font-size: 14px;
    }
  }

  .input-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 10px;

    .char-count {
      font-size: 12px;
      color: #9AA0A6;
    }

    .van-button {
      min-width: 70px;
    }
  }

  .quick-actions {
    display: flex;
    gap: 16px;
    margin-top: 10px;
    padding-top: 10px;
    border-top: 1px solid #E8EAED;

    .quick-btn {
      display: flex;
      align-items: center;
      gap: 4px;
      font-size: 13px;
      color: #5F6368;

      &:active {
        color: #1A73E8;
      }
    }
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
