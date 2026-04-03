<template>
  <div class="layout">
    <div class="page-content">
      <router-view />
    </div>
    <van-tabbar v-model="active" route class="tabbar">
      <van-tabbar-item to="/home" icon="home-o">首页</van-tabbar-item>
      <van-tabbar-item to="/resume" icon="notes-o">简历</van-tabbar-item>
      <van-tabbar-item to="/profile" icon="user-o">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const active = ref(0)
</script>

<style lang="scss" scoped>
.layout {
  min-height: 100vh;
  background: #F8F9FA;
}

.page-content {
  padding-bottom: 60px;
}

.tabbar {
  max-width: 600px;
  margin: 0 auto;
}
</style>
