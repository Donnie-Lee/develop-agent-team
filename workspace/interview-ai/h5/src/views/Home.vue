<template>
  <div class="home-page">
    <div class="banner">
      <div class="banner-content">
        <h1>智能面试练兵场</h1>
        <p>AI模拟面试，助力斩获心仪offer</p>
      </div>
    </div>

    <div class="quick-actions">
      <div class="action-card" @click="handleStartInterview">
        <div class="action-icon">
          <van-icon name="play-circle-o" size="32" color="#1A73E8" />
        </div>
        <span>开始面试</span>
      </div>
      <div class="action-card" @click="handlePickQuestions">
        <div class="action-icon">
          <van-icon name="records" size="32" color="#34A853" />
        </div>
        <span>选题练习</span>
      </div>
      <div class="action-card" @click="handleViewRanking">
        <div class="action-icon">
          <van-icon name="chart-trending-o" size="32" color="#FB8C00" />
        </div>
        <span>排行榜</span>
      </div>
    </div>

    <div class="section">
      <div class="section-header">
        <h3>推荐职位</h3>
        <span class="more">更多</span>
      </div>
      <div class="job-list">
        <div v-for="job in jobs" :key="job.id" class="job-card">
          <div class="job-info">
            <h4>{{ job.title }}</h4>
            <p class="company">{{ job.company }}</p>
            <p class="salary">{{ job.salary }}</p>
          </div>
          <van-button size="small" type="primary" @click="handleApply(job)">
            申请
          </van-button>
        </div>
      </div>
    </div>

    <div class="section">
      <div class="section-header">
        <h3>热门面试题</h3>
        <span class="more">更多</span>
      </div>
      <div class="question-list">
        <div v-for="q in questions" :key="q.id" class="question-card">
          <van-tag :type="getTagType(q.difficulty)" size="medium">
            {{ q.difficulty }}
          </van-tag>
          <p class="question-text">{{ q.title }}</p>
          <div class="question-meta">
            <span>{{ q.category }}</span>
            <span>{{ q.interviewCount }}次面试</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'

const router = useRouter()

const jobs = ref([
  { id: 1, title: '前端工程师', company: '字节跳动', salary: '25K-40K' },
  { id: 2, title: '后端工程师', company: '腾讯', salary: '20K-35K' },
  { id: 3, title: '产品经理', company: '阿里巴巴', salary: '22K-38K' }
])

const questions = ref([
  { id: 1, title: '请解释什么是闭包？', difficulty: '中等', category: 'JavaScript', interviewCount: 156 },
  { id: 2, title: 'Vue中的响应式原理是什么？', difficulty: '简单', category: 'Vue', interviewCount: 234 },
  { id: 3, title: '如何实现一个防抖函数？', difficulty: '困难', category: 'JavaScript', interviewCount: 89 }
])

const getTagType = (difficulty) => {
  const map = { '简单': 'success', '中等': 'warning', '困难': 'danger' }
  return map[difficulty] || 'default'
}

const handleStartInterview = () => {
  showToast('功能开发中')
}

const handlePickQuestions = () => {
  showToast('功能开发中')
}

const handleViewRanking = () => {
  showToast('功能开发中')
}

const handleApply = (job) => {
  showToast(`申请 ${job.title} 功能开发中`)
}
</script>

<style lang="scss" scoped>
.home-page {
  background: #F8F9FA;
}

.banner {
  background: linear-gradient(135deg, #1A73E8 0%, #4285F4 100%);
  padding: 48px 24px;
  color: white;

  .banner-content {
    h1 {
      font-size: 24px;
      margin: 0 0 8px;
    }

    p {
      font-size: 14px;
      opacity: 0.9;
      margin: 0;
    }
  }
}

.quick-actions {
  display: flex;
  justify-content: space-around;
  padding: 24px 16px;
  margin-top: -24px;
  background: white;
  border-radius: 12px;
  margin-left: 16px;
  margin-right: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);

  .action-card {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;

    .action-icon {
      width: 56px;
      height: 56px;
      background: #F8F9FA;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    span {
      font-size: 14px;
      color: #5F6368;
    }
  }
}

.section {
  margin-top: 24px;
  padding: 0 16px;

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;

    h3 {
      font-size: 18px;
      color: #202124;
      margin: 0;
    }

    .more {
      font-size: 14px;
      color: #1A73E8;
    }
  }
}

.job-list {
  .job-card {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: white;
    padding: 16px;
    border-radius: 12px;
    margin-bottom: 12px;

    .job-info {
      h4 {
        font-size: 16px;
        color: #202124;
        margin: 0 0 4px;
      }

      .company {
        font-size: 14px;
        color: #5F6368;
        margin: 0 0 4px;
      }

      .salary {
        font-size: 14px;
        color: #EA4335;
        font-weight: 600;
        margin: 0;
      }
    }
  }
}

.question-list {
  .question-card {
    background: white;
    padding: 16px;
    border-radius: 12px;
    margin-bottom: 12px;

    .question-text {
      font-size: 14px;
      color: #202124;
      margin: 12px 0 8px;
    }

    .question-meta {
      display: flex;
      gap: 16px;
      font-size: 12px;
      color: #9AA0A6;
    }
  }
}
</style>
