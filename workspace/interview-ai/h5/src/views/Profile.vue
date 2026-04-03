<template>
  <div class="profile-page">
    <!-- 用户信息卡片 -->
    <div class="user-card">
      <div class="user-info">
        <van-image round width="64" height="64" :src="userInfo.avatar || defaultAvatar" />
        <div class="user-detail">
          <div class="nickname-row">
            <span class="nickname">{{ userInfo.nickname || '用户' }}</span>
            <van-tag type="danger" size="medium">VIP</van-tag>
          </div>
          <span class="phone">{{ userInfo.phone || '未登录' }}</span>
        </div>
      </div>
    </div>

    <!-- 会员权益提示条 -->
    <div class="vip-tip" @click="handleUpgrade">
      <span>升级Pro会员，解锁无限面试</span>
      <van-icon name="arrow" color="#FB8C00" />
    </div>

    <!-- 面试统计卡片 -->
    <div class="stats-card">
      <div class="stat-item">
        <span class="stat-value">{{ stats.interviewCount }}</span>
        <span class="stat-label">面试次数</span>
      </div>
      <div class="stat-item">
        <span class="stat-value">{{ stats.totalDuration }}</span>
        <span class="stat-label">总时长</span>
      </div>
      <div class="stat-item">
        <span class="stat-value">{{ stats.avgScore }}</span>
        <span class="stat-label">平均分</span>
      </div>
    </div>

    <!-- 功能入口列表 -->
    <div class="menu-list">
      <div class="menu-item" @click="handleMenuClick('resume')">
        <van-icon name="description" size="24" color="#5F6368" />
        <span>我的简历</span>
        <van-icon name="arrow" size="16" color="#9AA0A6" />
      </div>
      <div class="menu-item" @click="handleMenuClick('interview')">
        <van-icon name="todo-list-o" size="24" color="#5F6368" />
        <span>面试记录</span>
        <van-icon name="arrow" size="16" color="#9AA0A6" />
      </div>
      <div class="menu-item" @click="handleMenuClick('wrong')">
        <van-icon name="close-circle-o" size="24" color="#5F6368" />
        <span>错题本</span>
        <van-icon name="arrow" size="16" color="#9AA0A6" />
      </div>
      <div class="menu-item" @click="handleMenuClick('favorite')">
        <van-icon name="star-o" size="24" color="#5F6368" />
        <span>收藏题目</span>
        <van-icon name="arrow" size="16" color="#9AA0A6" />
      </div>
      <div class="menu-item" @click="handleMenuClick('vip')">
        <van-icon name="gem-o" size="24" color="#5F6368" />
        <span>会员中心</span>
        <van-badge dot>
          <van-icon name="arrow" size="16" color="#9AA0A6" />
        </van-badge>
      </div>
      <div class="menu-item" @click="handleMenuClick('notification')">
        <van-icon name="bell-o" size="24" color="#5F6368" />
        <span>消息通知</span>
        <van-icon name="arrow" size="16" color="#9AA0A6" />
      </div>
      <div class="menu-item" @click="handleMenuClick('settings')">
        <van-icon name="setting-o" size="24" color="#5F6368" />
        <span>设置</span>
        <van-icon name="arrow" size="16" color="#9AA0A6" />
      </div>
      <div class="menu-item" @click="handleMenuClick('help')">
        <van-icon name="question-o" size="24" color="#5F6368" />
        <span>帮助与反馈</span>
        <van-icon name="arrow" size="16" color="#9AA0A6" />
      </div>
    </div>

    <van-button v-if="isLoggedIn" class="logout-btn" @click="handleLogout">
      退出登录
    </van-button>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { showToast } from 'vant'

const router = useRouter()
const userStore = useUserStore()

const defaultAvatar = 'https://fastly.jsdelivr.net/npm/@vant/assets/cat.jpeg'

const userInfo = reactive({
  nickname: '张同学',
  phone: '138****8888',
  avatar: ''
})

const stats = reactive({
  interviewCount: 12,
  totalDuration: '3.5h',
  avgScore: 85
})

const isLoggedIn = computed(() => userStore.isLoggedIn)

onMounted(() => {
  // 如果已登录，获取用户信息
  if (userStore.isLoggedIn) {
    userStore.fetchUserInfo()
    if (userStore.userInfo) {
      Object.assign(userInfo, userStore.userInfo)
    }
  }
})

const handleUpgrade = () => {
  showToast('会员功能开发中')
}

const handleMenuClick = (type) => {
  switch (type) {
    case 'resume':
      router.push('/resume')
      break
    case 'interview':
      showToast('面试记录功能开发中')
      break
    case 'wrong':
      showToast('错题本功能开发中')
      break
    case 'favorite':
      showToast('收藏功能开发中')
      break
    case 'vip':
      showToast('会员中心功能开发中')
      break
    case 'notification':
      showToast('消息通知功能开发中')
      break
    case 'settings':
      showToast('设置功能开发中')
      break
    case 'help':
      showToast('帮助与反馈功能开发中')
      break
  }
}

const handleLogout = () => {
  userStore.logout()
  router.replace('/login')
}
</script>

<style lang="scss" scoped>
.profile-page {
  min-height: 100vh;
  background: #F8F9FA;
  padding-bottom: 24px;
}

.user-card {
  background: linear-gradient(135deg, #1A73E8 0%, #4285F4 100%);
  padding: 20px;
  height: 160px;
  display: flex;
  align-items: center;

  .user-info {
    display: flex;
    align-items: center;
    gap: 16px;

    .user-detail {
      .nickname-row {
        display: flex;
        align-items: center;
        gap: 8px;

        .nickname {
          font-size: 18px;
          font-weight: 600;
          color: white;
        }
      }

      .phone {
        font-size: 14px;
        color: rgba(255, 255, 255, 0.8);
      }
    }
  }
}

.vip-tip {
  background: #FFF8E1;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  margin: 0 16px;
  border-radius: 8px;
  transform: translateY(-24px);

  span {
    font-size: 14px;
    color: #5F6368;
  }
}

.stats-card {
  display: flex;
  background: white;
  border-radius: 12px;
  margin: 0 16px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transform: translateY(-12px);

  .stat-item {
    flex: 1;
    text-align: center;

    .stat-value {
      display: block;
      font-size: 24px;
      font-weight: 600;
      color: #202124;
    }

    .stat-label {
      font-size: 12px;
      color: #9AA0A6;
    }
  }
}

.menu-list {
  background: white;
  margin: 0 16px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);

  .menu-item {
    display: flex;
    align-items: center;
    padding: 16px;
    border-bottom: 1px solid #E8EAED;

    &:last-child {
      border-bottom: none;
    }

    span {
      flex: 1;
      font-size: 16px;
      color: #202124;
      margin-left: 12px;
    }
  }
}

.logout-btn {
  display: block;
  width: calc(100% - 32px);
  margin: 24px 16px 0;
  height: 44px;
  background: white;
  border: 1px solid #E8EAED;
  color: #EA4335;
  border-radius: 8px;
}
</style>
