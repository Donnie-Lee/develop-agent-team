<template>
  <div class="interview-config-page">
    <div class="page-header">
      <van-icon name="arrow-left" size="20" @click="handleBack" />
      <span>面试配置</span>
      <span></span>
    </div>

    <div class="page-content">
      <!-- 选择的职位 -->
      <div class="selected-job" v-if="selectedJob">
        <div class="job-info">
          <span class="job-title">{{ selectedJob.title }}</span>
          <van-tag type="primary" size="small">{{ selectedJob.level }}</van-tag>
        </div>
        <div class="job-tags">
          <span v-for="tag in selectedJob.tags" :key="tag" class="tag">{{ tag }}</span>
        </div>
      </div>

      <!-- 面试风格 -->
      <div class="config-section">
        <h3>面试风格</h3>
        <div class="style-list">
          <div
            v-for="style in interviewStyles"
            :key="style.id"
            class="style-card"
            :class="{ selected: selectedStyle === style.id }"
            @click="selectedStyle = style.id"
          >
            <div class="style-icon">
              <van-icon :name="style.icon" size="28" />
            </div>
            <div class="style-info">
              <h4>{{ style.name }}</h4>
              <p>{{ style.description }}</p>
            </div>
            <van-icon
              v-if="selectedStyle === style.id"
              name="success-circle"
              size="20"
              color="#1A73E8"
              class="check-icon"
            />
          </div>
        </div>
      </div>

      <!-- 面试时长 -->
      <div class="config-section">
        <h3>面试时长</h3>
        <div class="duration-options">
          <div
            v-for="duration in durationOptions"
            :key="duration.value"
            class="duration-chip"
            :class="{ selected: selectedDuration === duration.value }"
            @click="selectedDuration = duration.value"
          >
            {{ duration.label }}
          </div>
        </div>
      </div>

      <!-- 选择简历 -->
      <div class="config-section">
        <div class="section-header">
          <h3>选择简历</h3>
          <span class="link" @click="goToResumeEdit">新建简历</span>
        </div>
        <div class="resume-list" v-if="resumes.length > 0">
          <div
            v-for="resume in resumes"
            :key="resume.id"
            class="resume-card"
            :class="{ selected: selectedResume?.id === resume.id }"
            @click="selectedResume = resume"
          >
            <div class="resume-icon">
              <van-icon name="description" size="24" color="#5F6368" />
            </div>
            <div class="resume-info">
              <span class="resume-title">{{ resume.title }}</span>
              <span class="resume-date">{{ formatDate(resume.updatedAt) }}</span>
            </div>
            <van-icon
              v-if="selectedResume?.id === resume.id"
              name="success"
              size="18"
              color="#1A73E8"
            />
          </div>
        </div>
        <div class="no-resume" v-else>
          <p>暂无简历，请先创建简历</p>
          <van-button type="primary" size="small" @click="goToResumeEdit">
            创建简历
          </van-button>
        </div>
      </div>

      <!-- 面试模式 -->
      <div class="config-section">
        <h3>面试模式</h3>
        <div class="mode-list">
          <div
            v-for="mode in interviewModes"
            :key="mode.id"
            class="mode-card"
            :class="{ selected: selectedMode === mode.id }"
            @click="selectedMode = mode.id"
          >
            <div class="mode-icon">
              <van-icon :name="mode.icon" size="28" :color="mode.color" />
            </div>
            <div class="mode-info">
              <h4>{{ mode.name }}</h4>
              <p>{{ mode.description }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="page-footer">
      <van-button type="primary" size="large" :loading="loading" @click="handleStart">
        开始面试
      </van-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import { useInterviewStore } from '@/stores/interview'
import { useResumeStore } from '@/stores/resume'

const router = useRouter()
const interviewStore = useInterviewStore()
const resumeStore = useResumeStore()

const loading = ref(false)

// 面试风格选项
const interviewStyles = [
  {
    id: 'technical',
    name: '技术面',
    icon: 'code-o',
    description: '侧重考察技术能力和项目经验'
  },
  {
    id: 'pressure',
    name: '压力面',
    icon: 'fire-o',
    description: '模拟高压面试环境，考察应变能力'
  },
  {
    id: 'behavioral',
    name: '行为面',
    icon: 'user-o',
    description: '考察过往经历和行为模式'
  }
]

// 时长选项
const durationOptions = [
  { label: '15分钟', value: 15 },
  { label: '30分钟', value: 30 },
  { label: '45分钟', value: 45 },
  { label: '60分钟', value: 60 }
]

// 面试模式
const interviewModes = [
  {
    id: 'text',
    name: '文字面试',
    icon: 'chat-o',
    color: '#1A73E8',
    description: '通过文字进行面试对答'
  },
  {
    id: 'voice',
    name: '语音面试',
    icon: 'volume-o',
    color: '#34A853',
    description: '语音实时对话，模拟真实面试'
  }
]

// 状态
const selectedJob = ref(null)
const selectedStyle = ref('technical')
const selectedDuration = ref(30)
const selectedResume = ref(null)
const selectedMode = ref('text')
const resumes = ref([])

onMounted(async () => {
  // 获取保存的职位信息
  const jobData = localStorage.getItem('selectedJob')
  if (jobData) {
    selectedJob.value = JSON.parse(jobData)
  } else {
    showToast('请先选择职位')
    router.replace('/interview/select')
    return
  }

  // 获取简历列表
  await resumeStore.fetchResumes()
  resumes.value = resumeStore.resumes

  // 设置默认简历
  if (resumes.value.length > 0) {
    const defaultResume = resumes.value.find(r => r.isDefault) || resumes.value[0]
    selectedResume.value = defaultResume
  }
})

const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

const handleBack = () => {
  router.back()
}

const goToResumeEdit = () => {
  router.push('/resume/edit')
}

const handleStart = async () => {
  if (!selectedJob.value) {
    showToast('请选择职位')
    return
  }

  loading.value = true

  try {
    // 创建面试
    const interviewData = {
      jobId: selectedJob.value.id,
      jobTitle: selectedJob.value.title,
      style: selectedStyle.value,
      duration: selectedDuration.value,
      resumeId: selectedResume.value?.id,
      mode: selectedMode.value
    }

    const result = await interviewStore.createNewInterview(interviewData)

    if (result) {
      // 跳转到面试页面
      if (selectedMode.value === 'text') {
        router.replace(`/interview/text?id=${result.id}`)
      } else {
        router.replace(`/interview/voice?id=${result.id}`)
      }
    }
  } catch (error) {
    console.error('Start interview failed:', error)
    showToast('创建面试失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.interview-config-page {
  min-height: 100vh;
  background: #F8F9FA;
  padding-bottom: 100px;
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

.selected-job {
  background: linear-gradient(135deg, #1A73E8 0%, #4285F4 100%);
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 24px;
  color: white;

  .job-info {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 8px;

    .job-title {
      font-size: 18px;
      font-weight: 600;
    }

    :deep(.van-tag) {
      background: rgba(255, 255, 255, 0.2);
      border: none;
    }
  }

  .job-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;

    .tag {
      font-size: 12px;
      opacity: 0.9;
    }
  }
}

.config-section {
  margin-bottom: 24px;

  h3 {
    font-size: 16px;
    font-weight: 600;
    color: #202124;
    margin: 0 0 12px;
  }

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;

    h3 {
      margin: 0;
    }

    .link {
      font-size: 14px;
      color: #1A73E8;
    }
  }
}

.style-list {
  .style-card {
    display: flex;
    align-items: center;
    background: white;
    border-radius: 12px;
    padding: 16px;
    margin-bottom: 12px;
    border: 2px solid transparent;
    transition: all 0.2s;

    &.selected {
      border-color: #1A73E8;
    }

    .style-icon {
      width: 48px;
      height: 48px;
      background: #F8F9FA;
      border-radius: 10px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 12px;
      color: #5F6368;
    }

    .style-info {
      flex: 1;

      h4 {
        font-size: 15px;
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

    .check-icon {
      margin-left: 8px;
    }
  }
}

.duration-options {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;

  .duration-chip {
    padding: 10px 20px;
    background: white;
    border-radius: 20px;
    font-size: 14px;
    color: #5F6368;
    border: 2px solid transparent;
    transition: all 0.2s;

    &.selected {
      background: #E8F0FE;
      border-color: #1A73E8;
      color: #1A73E8;
    }
  }
}

.resume-list {
  .resume-card {
    display: flex;
    align-items: center;
    background: white;
    border-radius: 12px;
    padding: 14px 16px;
    margin-bottom: 10px;
    border: 2px solid transparent;
    transition: all 0.2s;

    &.selected {
      border-color: #1A73E8;
    }

    .resume-icon {
      width: 40px;
      height: 40px;
      background: #F8F9FA;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 12px;
    }

    .resume-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 4px;

      .resume-title {
        font-size: 14px;
        font-weight: 500;
        color: #202124;
      }

      .resume-date {
        font-size: 12px;
        color: #9AA0A6;
      }
    }
  }
}

.no-resume {
  background: white;
  border-radius: 12px;
  padding: 24px;
  text-align: center;

  p {
    font-size: 14px;
    color: #5F6368;
    margin: 0 0 16px;
  }
}

.mode-list {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;

  .mode-card {
    background: white;
    border-radius: 12px;
    padding: 16px;
    text-align: center;
    border: 2px solid transparent;
    transition: all 0.2s;

    &.selected {
      border-color: #1A73E8;
    }

    .mode-icon {
      width: 56px;
      height: 56px;
      background: #F8F9FA;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto 12px;
    }

    .mode-info {
      h4 {
        font-size: 14px;
        font-weight: 600;
        color: #202124;
        margin: 0 0 4px;
      }

      p {
        font-size: 12px;
        color: #5F6368;
        margin: 0;
      }
    }
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

  .van-button {
    border-radius: 8px;
  }
}
</style>
