<template>
  <div class="job-select-page">
    <div class="page-header">
      <van-icon name="arrow-left" size="20" @click="handleBack" />
      <span>选择岗位</span>
      <span></span>
    </div>

    <div class="page-content">
      <div class="section-title">
        <h2>选择您要面试的岗位</h2>
        <p>AI将根据您的选择生成个性化面试问题</p>
      </div>

      <div class="category-list">
        <div
          v-for="category in jobCategories"
          :key="category.id"
          class="category-card"
          :class="{ selected: selectedCategory === category.id }"
          @click="handleSelectCategory(category)"
        >
          <div class="category-icon">
            <van-icon :name="category.icon" size="32" :color="category.color" />
          </div>
          <div class="category-info">
            <h3>{{ category.name }}</h3>
            <p>{{ category.description }}</p>
          </div>
          <van-icon
            v-if="selectedCategory === category.id"
            name="success"
            size="20"
            color="#1A73E8"
            class="check-icon"
          />
        </div>
      </div>

      <div class="section-title sub">
        <h3>职位分类</h3>
      </div>

      <div class="job-list">
        <div
          v-for="job in filteredJobs"
          :key="job.id"
          class="job-card"
          :class="{ selected: selectedJob?.id === job.id }"
          @click="handleSelectJob(job)"
        >
          <div class="job-header">
            <span class="job-title">{{ job.title }}</span>
            <van-tag :type="getTagType(job.level)" size="small">
              {{ job.level }}
            </van-tag>
          </div>
          <div class="job-tags">
            <span v-for="tag in job.tags" :key="tag" class="job-tag">
              {{ tag }}
            </span>
          </div>
          <div class="job-footer">
            <span class="interview-count">
              <van-icon name="play-circle-o" size="14" />
              {{ job.interviewCount }}人面试过
            </span>
            <span class="salary">{{ job.salary }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="page-footer">
      <van-button
        type="primary"
        size="large"
        :disabled="!canProceed"
        @click="handleNext"
      >
        下一步
      </van-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'

const router = useRouter()

// 岗位分类
const jobCategories = [
  {
    id: 'frontend',
    name: '前端工程师',
    icon: 'desktop-o',
    color: '#1A73E8',
    description: 'HTML/CSS/JavaScript, Vue, React'
  },
  {
    id: 'backend',
    name: '后端工程师',
    icon: 'cluster-o',
    color: '#34A853',
    description: 'Java, Python, Go, 数据库'
  },
  {
    id: 'fullstack',
    name: '全栈工程师',
    icon: 'apps-o',
    color: '#FB8C00',
    description: '前后端均需掌握'
  }
]

// 职位列表
const jobs = [
  {
    id: 'fe-web',
    title: 'Web前端工程师',
    category: 'frontend',
    level: '初级',
    salary: '15K-25K',
    tags: ['Vue', 'React', 'JavaScript'],
    interviewCount: 1234
  },
  {
    id: 'fe-mobile',
    title: '移动端前端工程师',
    category: 'frontend',
    level: '中级',
    salary: '20K-35K',
    tags: ['React Native', 'Flutter'],
    interviewCount: 856
  },
  {
    id: 'fe-big',
    title: '前端架构师',
    category: 'frontend',
    level: '高级',
    salary: '35K-60K',
    tags: ['架构设计', '性能优化'],
    interviewCount: 234
  },
  {
    id: 'be-java',
    title: 'Java开发工程师',
    category: 'backend',
    level: '初级',
    salary: '15K-25K',
    tags: ['Spring Boot', 'MySQL'],
    interviewCount: 2156
  },
  {
    id: 'be-python',
    title: 'Python开发工程师',
    category: 'backend',
    level: '初级',
    salary: '15K-28K',
    tags: ['Django', 'Flask'],
    interviewCount: 1567
  },
  {
    id: 'be-go',
    title: 'Go开发工程师',
    category: 'backend',
    level: '中级',
    salary: '25K-40K',
    tags: ['Gin', 'gRPC'],
    interviewCount: 678
  },
  {
    id: 'fs-node',
    title: 'Node.js全栈',
    category: 'fullstack',
    level: '中级',
    salary: '20K-35K',
    tags: ['Express', 'MongoDB'],
    interviewCount: 945
  }
]

const selectedCategory = ref('')
const selectedJob = ref(null)

const filteredJobs = computed(() => {
  if (!selectedCategory.value) return jobs
  return jobs.filter(job => job.category === selectedCategory.value)
})

const canProceed = computed(() => {
  return !!selectedJob.value
})

const getTagType = (level) => {
  const map = {
    '初级': 'success',
    '中级': 'warning',
    '高级': 'danger'
  }
  return map[level] || 'default'
}

const handleSelectCategory = (category) => {
  selectedCategory.value = category.id
  selectedJob.value = null
}

const handleSelectJob = (job) => {
  selectedJob.value = job
  selectedCategory.value = job.category
}

const handleBack = () => {
  router.back()
}

const handleNext = () => {
  if (!selectedJob.value) {
    showToast('请选择职位')
    return
  }
  // 保存选择的职位到localStorage，传递给下一步
  localStorage.setItem('selectedJob', JSON.stringify(selectedJob.value))
  router.push('/interview/config')
}
</script>

<style lang="scss" scoped>
.job-select-page {
  min-height: 100vh;
  background: #F8F9FA;
  padding-bottom: 80px;
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
  padding: 20px 16px;
}

.section-title {
  margin-bottom: 20px;

  h2 {
    font-size: 22px;
    color: #202124;
    margin: 0 0 8px;
  }

  p {
    font-size: 14px;
    color: #5F6368;
    margin: 0;
  }

  &.sub {
    margin-top: 32px;
    margin-bottom: 16px;

    h3 {
      font-size: 16px;
      color: #202124;
      margin: 0;
    }
  }
}

.category-list {
  .category-card {
    display: flex;
    align-items: center;
    background: white;
    border-radius: 12px;
    padding: 16px;
    margin-bottom: 12px;
    border: 2px solid transparent;
    transition: all 0.2s;

    &:active {
      transform: scale(0.98);
    }

    &.selected {
      border-color: #1A73E8;
      background: #E8F0FE;
    }

    .category-icon {
      width: 56px;
      height: 56px;
      background: #F8F9FA;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;
    }

    .category-info {
      flex: 1;

      h3 {
        font-size: 16px;
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
      margin-left: 12px;
    }
  }
}

.job-list {
  .job-card {
    background: white;
    border-radius: 12px;
    padding: 16px;
    margin-bottom: 12px;
    border: 2px solid transparent;
    transition: all 0.2s;

    &:active {
      transform: scale(0.98);
    }

    &.selected {
      border-color: #1A73E8;
    }

    .job-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 8px;

      .job-title {
        font-size: 16px;
        font-weight: 600;
        color: #202124;
      }
    }

    .job-tags {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      margin-bottom: 12px;

      .job-tag {
        font-size: 12px;
        color: #5F6368;
        background: #F1F3F4;
        padding: 2px 8px;
        border-radius: 4px;
      }
    }

    .job-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .interview-count {
        font-size: 12px;
        color: #9AA0A6;
        display: flex;
        align-items: center;
        gap: 4px;
      }

      .salary {
        font-size: 14px;
        color: #EA4335;
        font-weight: 600;
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
