<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <div class="logo">
        <img src="@/assets/logo.svg" alt="InterviewAI" />
        <span>管理后台</span>
      </div>
      <el-menu
        :default-active="route.path"
        router
        class="sidebar-menu"
      >
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <span>控制台</span>
        </el-menu-item>
        <el-menu-item index="/users">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/resumes">
          <el-icon><Document /></el-icon>
          <span>简历管理</span>
        </el-menu-item>
        <el-menu-item index="/interviews">
          <el-icon><Mic /></el-icon>
          <span>面试管理</span>
        </el-menu-item>
        <el-menu-item index="/questions">
          <el-icon><Collection /></el-icon>
          <span>题库管理</span>
        </el-menu-item>
        <el-menu-item index="/settings">
          <el-icon><Setting /></el-icon>
          <span>系统设置</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container class="main-container">
      <el-header>
        <div class="header-left">
          <h2>{{ route.meta.title }}</h2>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" icon="UserFilled" />
              <span class="username">管理员</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const handleCommand = (command) => {
  if (command === 'logout') {
    userStore.logout()
    router.push('/login')
  } else if (command === 'profile') {
    // 个人中心
  }
}
</script>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;
}

.el-aside {
  background: #001529;
  color: white;

  .logo {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 20px 16px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);

    img {
      width: 32px;
      height: 32px;
    }

    span {
      font-size: 16px;
      font-weight: 600;
    }
  }

  .sidebar-menu {
    border-right: none;
    background: transparent;

    :deep(.el-menu-item) {
      color: rgba(255, 255, 255, 0.7);

      &:hover {
        background: rgba(255, 255, 255, 0.1);
        color: white;
      }

      &.is-active {
        background: #1A73E8;
        color: white;
      }
    }
  }
}

.el-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-bottom: 1px solid #E8EAED;
  padding: 0 24px;

  .header-left h2 {
    font-size: 18px;
    color: #202124;
    margin: 0;
  }

  .user-info {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;

    .username {
      color: #5F6368;
    }
  }
}

.el-main {
  background: #F8F9FA;
  padding: 24px;
  flex: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
}

.main-container {
  flex: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}
</style>
