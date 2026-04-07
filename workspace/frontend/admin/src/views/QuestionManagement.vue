<template>
  <div class="page-container">
    <div class="card">
      <div class="card-header">
        <h3>题库管理</h3>
        <div class="header-actions">
          <el-button type="primary" @click="handleAdd">添加题目</el-button>
          <el-button @click="handleExport">导出数据</el-button>
        </div>
      </div>
      <el-table :data="questions" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="difficulty" label="难度" width="100">
          <template #default="{ row }">
            <el-tag :type="getDifficultyType(row.difficulty)">{{ row.difficulty }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="question" label="题目内容" />
        <el-table-column prop="answer" label="参考答案" show-overflow-tooltip />
        <el-table-column prop="usageCount" label="使用次数" width="100" />
        <el-table-column label="操作" fixed="right" width="150">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
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
import { ElMessage, ElMessageBox } from 'element-plus'

const questions = ref([
  { id: 1, category: 'JavaScript', difficulty: '中等', question: '请解释 JavaScript 中的闭包是什么？', answer: '闭包是指...', usageCount: 156 },
  { id: 2, category: 'Vue', difficulty: '简单', question: 'Vue 中如何实现组件通信？', answer: '可以通过 props、emit、provide/inject...', usageCount: 234 },
  { id: 3, category: 'React', difficulty: '困难', question: 'React 的 Fiber 架构是什么？', answer: 'Fiber 是 React 16...', usageCount: 89 }
])

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)

const getDifficultyType = (difficulty) => {
  const map = { 简单: 'success', 中等: 'warning', 困难: 'danger' }
  return map[difficulty] || 'info'
}

const handleAdd = () => {
  ElMessage.info('添加题目功能开发中')
}

const handleExport = () => {
  ElMessage.success('导出功能开发中')
}

const handleEdit = (row) => {
  ElMessage.info(`编辑题目: ${row.id}`)
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除题目「${row.question.slice(0, 20)}...」吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('删除成功')
  }).catch(() => {})
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

    .header-actions {
      display: flex;
      gap: 12px;
    }
  }
}
</style>
