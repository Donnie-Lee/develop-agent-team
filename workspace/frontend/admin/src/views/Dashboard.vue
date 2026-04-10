<template>
  <div class="dashboard">
    <el-row :gutter="24" class="stats-row">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #E8F0FE;">
            <el-icon :size="32" color="#1A73E8"><User /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-value">{{ loading ? '-' : stats.totalUsers }}</p>
            <p class="stat-label">用户总数</p>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #E8F5E9;">
            <el-icon :size="32" color="#34A853"><Document /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-value">{{ loading ? '-' : stats.totalResumes }}</p>
            <p class="stat-label">简历总数</p>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #FFF3E0;">
            <el-icon :size="32" color="#FB8C00"><Mic /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-value">{{ loading ? '-' : stats.totalInterviews }}</p>
            <p class="stat-label">面试次数</p>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #FCE4EC;">
            <el-icon :size="32" color="#EA4335"><Collection /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-value">{{ loading ? '-' : stats.totalQuestions }}</p>
            <p class="stat-label">题库数量</p>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="24" class="charts-row">
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-header">
            <h3>用户增长趋势</h3>
            <el-button size="small" @click="fetchDashboardStats">刷新</el-button>
          </div>
          <div class="chart-placeholder">
            <p>用户增长图表</p>
          </div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-header">
            <h3>面试完成情况</h3>
            <el-button size="small" @click="fetchDashboardStats">刷新</el-button>
          </div>
          <div class="chart-placeholder">
            <p>面试统计图表</p>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="24" class="table-row">
      <el-col :span="24">
        <div class="table-card">
          <h3>最近用户活动</h3>
          <el-table :data="recentActivities" style="width: 100%" v-loading="loading">
            <el-table-column prop="user" label="用户" width="180" />
            <el-table-column prop="action" label="操作" />
            <el-table-column prop="time" label="时间" width="180" />
          </el-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getDashboardStats } from '@/api/dashboard'

const loading = ref(true)
const stats = reactive({
  totalUsers: 0,
  totalResumes: 0,
  totalInterviews: 0,
  totalQuestions: 0
})
const recentActivities = ref([])

const fetchDashboardStats = async () => {
  loading.value = true
  try {
    const res = await getDashboardStats()
    const data = res.data.data
    if (data) {
      stats.totalUsers = data.totalUsers || 0
      stats.totalResumes = data.totalResumes || 0
      stats.totalInterviews = data.totalInterviews || 0
      stats.totalQuestions = data.totalQuestions || 0
      recentActivities.value = data.recentActivities || []
    }
  } catch (error) {
    console.error('Failed to fetch dashboard stats:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchDashboardStats()
})
</script>

<style lang="scss" scoped>
.dashboard {
  .stats-row {
    margin-bottom: 24px;
  }

  .stat-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    display: flex;
    align-items: center;
    gap: 16px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);

    .stat-icon {
      width: 64px;
      height: 64px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .stat-info {
      .stat-value {
        font-size: 28px;
        font-weight: 600;
        color: #202124;
        margin: 0;
      }

      .stat-label {
        font-size: 14px;
        color: #9AA0A6;
        margin: 4px 0 0;
      }
    }
  }

  .charts-row {
    margin-bottom: 24px;
  }

  .chart-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);

    .chart-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      h3 {
        font-size: 16px;
        color: #202124;
        margin: 0;
      }
    }

    .chart-placeholder {
      height: 200px;
      background: #F8F9FA;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #9AA0A6;
    }
  }

  .table-row {
    .table-card {
      background: white;
      border-radius: 12px;
      padding: 24px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);

      h3 {
        font-size: 16px;
        color: #202124;
        margin: 0 0 16px;
      }
    }
  }
}
</style>
