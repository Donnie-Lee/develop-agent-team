<template>
  <div class="page-container">
    <div class="card">
      <div class="card-header">
        <h3>简历管理</h3>
        <el-button type="primary" @click="handleExport">导出数据</el-button>
      </div>
      <el-table :data="resumes" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="简历标题" width="150" />
        <el-table-column prop="userName" label="用户" width="120" />
        <el-table-column prop="fileName" label="文件名" />
        <el-table-column prop="parseStatus" label="解析状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getParseStatusType(row.parseStatus)">
              {{ getParseStatusText(row.parseStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isDefault" label="默认" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.isDefault" type="success">是</el-tag>
            <span v-else>否</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="上传时间" width="180" />
        <el-table-column label="操作" fixed="right" width="150">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
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

const resumes = ref([
  { id: 1, title: '前端工程师简历', userName: '张同学', fileName: 'frontend_resume.pdf', parseStatus: 'success', isDefault: true, createTime: '2026-04-03 15:30' },
  { id: 2, title: '全栈简历', userName: '李同学', fileName: 'fullstack_resume.docx', parseStatus: 'pending', isDefault: false, createTime: '2026-04-03 14:22' },
  { id: 3, title: '后端简历', userName: '王同学', fileName: 'backend_resume.pdf', parseStatus: 'failed', isDefault: false, createTime: '2026-04-03 13:15' }
])

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)

const getParseStatusType = (status) => {
  const map = { success: 'success', pending: 'warning', failed: 'danger', parsing: 'info' }
  return map[status] || 'info'
}

const getParseStatusText = (status) => {
  const map = { success: '已解析', pending: '待解析', failed: '解析失败', parsing: '解析中' }
  return map[status] || status
}

const handleExport = () => {
  ElMessage.success('导出功能开发中')
}

const handleView = (row) => {
  ElMessage.info(`查看简历: ${row.title}`)
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除简历「${row.title}」吗？`, '提示', {
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
  }
}
</style>
