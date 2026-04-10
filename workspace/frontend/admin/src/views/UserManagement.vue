<template>
  <div class="page-container">
    <div class="card">
      <div class="card-header">
        <h3>用户管理</h3>
        <div class="header-actions">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索用户..."
            style="width: 200px; margin-right: 12px;"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button icon="Search" @click="handleSearch" />
            </template>
          </el-input>
          <el-button type="primary" @click="handleExport">导出数据</el-button>
        </div>
      </div>
      <el-table :data="filteredUsers" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="phone" label="手机号"  />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="vipStatus" label="VIP状态" >
          <template #default="{ row }">
            <el-tag :type="row.vipStatus === 'pro' ? 'danger' : 'info'">
              {{ row.vipStatus === 'pro' ? 'Pro' : '普通' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" />
        <el-table-column label="操作" fixed="right" >
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button link type="danger" @click="handleDisable(row)">禁用</el-button>
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
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'

const users = ref([
  { id: 1, nickname: '张同学', phone: '138****8888', email: 'zhang@example.com', vipStatus: 'pro', createTime: '2026-04-01 10:00' },
  { id: 2, nickname: '李同学', phone: '139****6666', email: 'li@example.com', vipStatus: 'normal', createTime: '2026-03-28 15:30' },
  { id: 3, nickname: '王同学', phone: '137****5555', email: 'wang@example.com', vipStatus: 'pro', createTime: '2026-03-25 09:15' }
])

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)
const searchKeyword = ref('')

const filteredUsers = computed(() => {
  if (!searchKeyword.value) return users.value
  const keyword = searchKeyword.value.toLowerCase()
  return users.value.filter(user =>
    user.nickname.toLowerCase().includes(keyword) ||
    user.phone.includes(keyword) ||
    user.email.toLowerCase().includes(keyword)
  )
})

const handleExport = () => {
  ElMessage.success('导出功能开发中')
}

const handleView = (row) => {
  ElMessage.info(`查看用户: ${row.nickname}`)
}

const handleDisable = (row) => {
  ElMessage.warning(`禁用用户: ${row.nickname}`)
}

const handleSearch = () => {
  ElMessage.success(`搜索: ${searchKeyword.value}`)
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
