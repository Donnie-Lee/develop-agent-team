<template>
  <div class="page-container">
    <div class="card">
      <div class="card-header">
        <h3>面试管理</h3>
        <el-button type="primary" @click="handleExport">导出数据</el-button>
      </div>
      <el-table :data="interviews" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" / />
        <el-table-column prop="userName" label="用户"  />
        <el-table-column prop="position" label="应聘职位"  />
        <el-table-column prop="interviewType" label="面试类型"  />
        <el-table-column prop="score" label="得分" >
          <template #default="{ row }">
            <span :style="{ color: getScoreColor(row.score) }">{{ row.score }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="duration" label="时长"  />
        <el-table-column prop="questionCount" label="题目数"  />
        <el-table-column prop="status" label="状态" >
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.statusText }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="面试时间" />
        <el-table-column label="操作" fixed="right" width="120">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        style="margin-top: 20px; justify-content: flex-end;"
      />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const interviews = ref([
  { id: 1, userName: '张同学', position: '前端工程师', interviewType: '技术面试', score: 85, duration: '12分钟', questionCount: 5, status: 'completed', statusText: '已完成', createTime: '2026-04-03 15:30' },
  { id: 2, userName: '李同学', position: '后端工程师', interviewType: '压力面试', score: 78, duration: '18分钟', questionCount: 8, status: 'completed', statusText: '已完成', createTime: '2026-04-02 10:00' },
  { id: 3, userName: '王同学', position: '产品经理', interviewType: 'HR面试', score: 0, duration: '-', questionCount: 0, status: 'in_progress', statusText: '进行中', createTime: '2026-04-03 16:00' }
])

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)

const getScoreColor = (score) => {
  if (score >= 80) return '#34A853'
  if (score >= 60) return '#FB8C00'
  return '#EA4335'
}

const getStatusType = (status) => {
  const map = { completed: 'success', in_progress: 'warning', pending: 'info' }
  return map[status] || 'info'
}

const handleExport = () => {
  ElMessage.success('导出功能开发中')
}

const handleView = (row) => {
  ElMessage.info(`查看面试报告: ${row.position}`)
}
</script>

<style lang="scss" scoped>
.card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h3 {
      margin: 0;
      font-size: 18px;
      color: #202124;
    }
  }
}
</style>
