<template>
  <div id="app">
    <router-view />
    <van-tabbar
      v-if="showTabBar"
      v-model="active"
      route
      class="tabbar"
    >
      <van-tabbar-item to="/home" icon="home-o">首页</van-tabbar-item>
      <van-tabbar-item to="/resume" icon="notes-o">简历</van-tabbar-item>
      <van-tabbar-item to="/profile" icon="user-o">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const active = ref(0)

// 需要显示 TabBar 的路由
const tabBarRoutes = ['/home', '/resume', '/profile']

// 隐藏 TabBar 的路由（面试相关页面）
const hideTabBarRoutes = [
  '/interview/select',
  '/interview/config',
  '/interview/text',
  '/interview/report',
  '/interview/voice'
]

// 根据当前路由判断是否显示 TabBar
const showTabBar = computed(() => {
  // 首先检查是否在隐藏列表中
  if (hideTabBarRoutes.some(r => route.path.includes(r))) {
    return false
  }
  return tabBarRoutes.some(r => route.path.includes(r))
})

// 路由变化时更新 active
watch(
  () => route.path,
  (path) => {
    if (path.includes('/home')) active.value = 0
    else if (path.includes('/resume')) active.value = 1
    else if (path.includes('/profile')) active.value = 2
  },
  { immediate: true }
)
</script>

<style lang="scss">
#app {
  height: 100%;
}

.tabbar {
  max-width: 600px;
  left: 50% !important;
  transform: translateX(-50%) !important;
  border-top: none !important;

  // 移除 Vant 默认的上边框
  &::before {
    display: none;
  }
}

// 确保 TabBar 背景是纯白色，没有顶部线条
:deep(.van-tabbar) {
  border-top: none !important;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.08);
}
</style>
