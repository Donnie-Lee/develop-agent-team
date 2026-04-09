<template>
  <div class="interview-report-page">
    <div class="page-header">
      <van-icon name="arrow-left" size="20" @click="handleBack" />
      <span>面试报告</span>
      <span></span>
    </div>

    <div class="page-content" v-if="report">
      <!-- 总体评分 -->
      <div class="overall-score">
        <div class="score-circle">
          <svg viewBox="0 0 100 100">
            <circle
              class="score-bg"
              cx="50"
              cy="50"
              r="45"
              stroke-width="8"
              fill="none"
            />
            <circle
              class="score-fill"
              cx="50"
              cy="50"
              r="45"
              stroke-width="8"
              fill="none"
              :stroke-dasharray="circumference"
              :stroke-dashoffset="scoreOffset"
            />
          </svg>
          <div class="score-text">
            <span class="score-value">{{ report.totalScore }}</span>
            <span class="score-label">综合得分</span>
          </div>
        </div>
        <div class="score-grade">
          <van-tag :type="getGradeTagType(report.grade)" size="large">
            {{ report.grade }}
          </van-tag>
          <p class="grade-desc">{{ getGradeDesc(report.grade) }}</p>
        </div>
      </div>

      <!-- 能力雷达图 -->
      <div class="ability-section">
        <h3>能力分析</h3>
        <div class="ability-radar">
          <div
            v-for="ability in report.abilities"
            :key="ability.name"
            class="ability-item"
          >
            <div class="ability-header">
              <span class="ability-name">{{ ability.name }}</span>
              <span class="ability-score">{{ ability.score }}分</span>
            </div>
            <div class="ability-bar">
              <div
                class="ability-fill"
                :style="{ width: `${ability.score}%`, background: getAbilityColor(ability.score) }"
              ></div>
            </div>
          </div>
        </div>
      </div>

      <!-- 问题回答详情 -->
      <div class="questions-section">
        <h3>回答详情</h3>
        <div
          v-for="(qa, index) in report.answers"
          :key="index"
          class="qa-card"
        >
          <div class="qa-header">
            <span class="q-num">Q{{ index + 1 }}</span>
            <van-tag :type="getTagType(qa.type)" size="small">
              {{ getTypeLabel(qa.type) }}
            </van-tag>
          </div>
          <div class="qa-content">
            <div class="question-part">
              <h4>面试官提问</h4>
              <p>{{ qa.question }}</p>
            </div>
            <div class="answer-part">
              <h4>您的回答</h4>
              <p>{{ qa.answer }}</p>
            </div>
            <div class="evaluation-part">
              <h4>评价</h4>
              <div class="eval-tags">
                <van-tag
                  v-for="tag in qa.evaluation"
                  :key="tag"
                  :type="getEvalTagType(tag)"
                  size="small"
                >
                  {{ tag }}
                </van-tag>
              </div>
              <p class="eval-comment">{{ qa.comment }}</p>
            </div>
            <div class="suggestion-part" v-if="qa.suggestion">
              <h4>改进建议</h4>
              <p>{{ qa.suggestion }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 改进建议 -->
      <div class="improvement-section">
        <h3>整体改进建议</h3>
        <div class="improvement-list">
          <div
            v-for="(item, index) in report.improvements"
            :key="index"
            class="improvement-item"
          >
            <div class="improvement-icon">
              <van-icon :name="item.icon" size="20" :color="item.color" />
            </div>
            <div class="improvement-content">
              <h4>{{ item.title }}</h4>
              <p>{{ item.description }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 历史记录 -->
      <div class="history-section">
        <h3>面试记录</h3>
        <div class="history-card">
          <div class="history-info">
            <span class="history-title">{{ report.jobTitle }}</span>
            <span class="history-date">{{ formatDate(report.interviewTime) }}</span>
          </div>
          <div class="history-score">
            <span class="score">{{ report.totalScore }}</span>
            <span class="label">分</span>
          </div>
        </div>
      </div>
    </div>

    <div class="loading-state" v-else-if="loading">
      <van-loading type="spinner" size="40px" />
      <span>正在生成报告...</span>
    </div>

    <div class="empty-state" v-else>
      <van-empty description="暂无面试报告" />
    </div>

    <div class="page-footer" v-if="report">
      <van-button type="primary" size="large" @click="handleShare">
        分享报告
      </van-button>
      <van-button plain size="large" @click="handleRetry">
        再来一次
      </van-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showToast } from 'vant'

const router = useRouter()
const route = useRoute()

const loading = ref(true)
const report = ref(null)
const interviewId = ref('')

// 计算属性
const circumference = 2 * Math.PI * 45
const scoreOffset = computed(() => {
  if (!report.value) return circumference
  const percent = report.value.totalScore / 100
  return circumference * (1 - percent)
})

onMounted(async () => {
  interviewId.value = route.params.id || 'demo'

  // 模拟加载报告
  await new Promise(resolve => setTimeout(resolve, 2000))

  // 模拟报告数据
  report.value = {
    totalScore: 78,
    grade: 'B',
    jobTitle: '前端工程师',
    interviewTime: Date.now() - 3600000,
    abilities: [
      { name: '技术能力', score: 82, level: 'good' },
      { name: '项目经验', score: 75, level: 'good' },
      { name: '沟通表达', score: 80, level: 'good' },
      { name: '问题解决', score: 72, level: 'medium' },
      { name: '学习能力', score: 85, level: 'excellent' }
    ],
    answers: [
      {
        type: 'technical',
        question: '请介绍一下你在前端开发中使用Vue框架的经验，以及Vue3相比Vue2有哪些重要的改进？',
        answer: '我在上一份工作中主要使用Vue2开发公司内部管理系统...',
        evaluation: ['思路清晰', '有深度'],
        comment: '回答较为完整，但对Vue3的新特性理解不够深入，缺少对实际项目的应用经验。',
        suggestion: '建议深入学习Vue3的Composition API和响应式原理，并在项目中实践。'
      },
      {
        type: 'project',
        question: '请描述一个你最引以为豪的项目经历，你在其中承担了什么角色？遇到了哪些挑战？',
        answer: '我最引以为豪的是一个电商平台的性能优化项目...',
        evaluation: ['有亮点', '表达清晰'],
        comment: '项目描述清晰，但重点不够突出，建议使用STAR法则更规范地描述。',
        suggestion: '建议在描述项目时，突出个人贡献和技术创新点。'
      },
      {
        type: 'behavioral',
        question: '如果你发现同事的代码存在严重的安全隐患，你会怎么处理？',
        answer: '我会先私下和同事沟通，说明问题的影响和解决方案...',
        evaluation: ['有情商', '合理'],
        comment: '处理方式得当，既顾及了同事感受，又解决了问题。',
        suggestion: '可以考虑建立代码review机制来预防类似问题。'
      }
    ],
    improvements: [
      {
        icon: 'book-marked',
        color: '#1A73E8',
        title: '深入学习Vue3',
        description: '建议系统学习Vue3的新特性和最佳实践，特别是Composition API的使用场景。'
      },
      {
        icon: 'chat-o',
        color: '#34A853',
        title: '提升表达逻辑',
        description: '使用STAR法则组织回答，让面试官更容易理解您的经历和成就。'
      },
      {
        icon: 'bulb-o',
        color: '#FB8C00',
        title: '增加项目亮点',
        description: '在描述项目时，突出个人独特贡献和技术难点解决方案，展示个人价值。'
      }
    ]
  }

  loading.value = false
})

const getGradeTagType = (grade) => {
  const map = {
    'A': 'success',
    'B': 'primary',
    'C': 'warning',
    'D': 'danger'
  }
  return map[grade] || 'default'
}

const getGradeDesc = (grade) => {
  const map = {
    'A': '优秀，超出预期',
    'B': '良好，符合预期',
    'C': '一般，还需提升',
    'D': '较差，需要加强'
  }
  return map[grade] || ''
}

const getAbilityColor = (score) => {
  if (score >= 80) return '#34A853'
  if (score >= 60) return '#FB8C00'
  return '#EA4335'
}

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

const getEvalTagType = (tag) => {
  const goodTags = ['思路清晰', '有深度', '有亮点', '表达清晰', '有情商', '合理']
  return goodTags.includes(tag) ? 'success' : 'primary'
}

const formatDate = (timestamp) => {
  const date = new Date(timestamp)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

const handleBack = () => {
  router.replace('/home')
}

const handleShare = () => {
  showToast('分享功能开发中')
}

const handleRetry = () => {
  router.replace('/interview/select')
}
</script>

<style lang="scss" scoped>
.interview-report-page {
  min-height: 100vh;
  background: #F8F9FA;
  padding-bottom: 120px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: white;
  border-bottom: 1px solid #E8EAED;
  position: sticky;
  top: 0;
  z-index: 10;

  span {
    font-size: 18px;
    font-weight: 600;
    color: #202124;
  }

  .van-icon {
    color: #202124;
  }
}

.page-content {
  padding: 16px;
}

.overall-score {
  background: linear-gradient(135deg, #1A73E8 0%, #4285F4 100%);
  border-radius: 16px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 24px;
  margin-bottom: 24px;

  .score-circle {
    position: relative;
    width: 120px;
    height: 120px;

    svg {
      transform: rotate(-90deg);

      .score-bg {
        stroke: rgba(255, 255, 255, 0.2);
      }

      .score-fill {
        stroke: white;
        stroke-linecap: round;
        transition: stroke-dashoffset 1s ease;
      }
    }

    .score-text {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      text-align: center;

      .score-value {
        display: block;
        font-size: 36px;
        font-weight: 700;
        color: white;
      }

      .score-label {
        font-size: 12px;
        color: rgba(255, 255, 255, 0.8);
      }
    }
  }

  .score-grade {
    flex: 1;
    color: white;

    :deep(.van-tag) {
      background: rgba(255, 255, 255, 0.2);
      border: none;
      font-size: 16px;
      padding: 6px 16px;
    }

    .grade-desc {
      font-size: 14px;
      margin: 12px 0 0;
      opacity: 0.9;
    }
  }
}

.ability-section,
.questions-section,
.improvement-section,
.history-section {
  background: white;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 16px;

  h3 {
    font-size: 16px;
    font-weight: 600;
    color: #202124;
    margin: 0 0 16px;
  }
}

.ability-radar {
  .ability-item {
    margin-bottom: 14px;

    &:last-child {
      margin-bottom: 0;
    }

    .ability-header {
      display: flex;
      justify-content: space-between;
      margin-bottom: 6px;

      .ability-name {
        font-size: 14px;
        color: #202124;
      }

      .ability-score {
        font-size: 14px;
        font-weight: 600;
        color: #5F6368;
      }
    }

    .ability-bar {
      height: 8px;
      background: #E8EAED;
      border-radius: 4px;
      overflow: hidden;

      .ability-fill {
        height: 100%;
        border-radius: 4px;
        transition: width 0.5s ease;
      }
    }
  }
}

.qa-card {
  background: #F8F9FA;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 12px;

  &:last-child {
    margin-bottom: 0;
  }

  .qa-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 12px;

    .q-num {
      font-size: 14px;
      font-weight: 600;
      color: #1A73E8;
    }
  }

  .qa-content {
    > div {
      margin-bottom: 12px;

      &:last-child {
        margin-bottom: 0;
      }

      h4 {
        font-size: 12px;
        font-weight: 600;
        color: #9AA0A6;
        margin: 0 0 6px;
      }

      p {
        font-size: 14px;
        color: #202124;
        margin: 0;
        line-height: 1.6;
      }
    }

    .eval-tags {
      display: flex;
      flex-wrap: wrap;
      gap: 6px;
      margin-bottom: 8px;
    }

    .eval-comment {
      color: #5F6368 !important;
      font-size: 13px !important;
    }

    .suggestion-part {
      background: #FFF8E1;
      padding: 12px;
      border-radius: 8px;

      h4 {
        color: #F57C00 !important;
      }

      p {
        color: #E65100 !important;
      }
    }
  }
}

.improvement-list {
  .improvement-item {
    display: flex;
    gap: 12px;
    padding: 12px 0;
    border-bottom: 1px solid #E8EAED;

    &:last-child {
      border-bottom: none;
    }

    .improvement-icon {
      width: 40px;
      height: 40px;
      background: #F8F9FA;
      border-radius: 10px;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
    }

    .improvement-content {
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
        line-height: 1.5;
      }
    }
  }
}

.history-card {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .history-info {
    display: flex;
    flex-direction: column;
    gap: 4px;

    .history-title {
      font-size: 14px;
      font-weight: 600;
      color: #202124;
    }

    .history-date {
      font-size: 12px;
      color: #9AA0A6;
    }
  }

  .history-score {
    display: flex;
    align-items: baseline;

    .score {
      font-size: 28px;
      font-weight: 700;
      color: #1A73E8;
    }

    .label {
      font-size: 14px;
      color: #5F6368;
      margin-left: 2px;
    }
  }
}

.loading-state,
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 0;

  span {
    margin-top: 16px;
    font-size: 14px;
    color: #5F6368;
  }
}

.page-footer {
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
  display: flex;
  flex-direction: column;
  gap: 12px;

  .van-button {
    border-radius: 8px;
  }
}
</style>
