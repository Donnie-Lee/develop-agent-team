import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/api'
import { showToast } from 'vant'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('h5_token') || '')
  const userInfo = ref(null)

  const isLoggedIn = computed(() => !!token.value)

  const sendCode = async (phone) => {
    try {
      const res = await api.post('/auth/sendCode', { phone })
      if (res.data.code === 0) {
        showToast('验证码已发送')
        return true
      }
      return false
    } catch (error) {
      console.error('Send code failed:', error)
      return false
    }
  }

  const loginWithCode = async (phone, code) => {
    try {
      const res = await api.post('/auth/phoneLogin', { phone, code })
      if (res.data.code === 0) {
        token.value = res.data.data.token
        localStorage.setItem('h5_token', res.data.data.token)
        await fetchUserInfo()
        return true
      }
      showToast(res.data.message || '登录失败')
      return false
    } catch (error) {
      console.error('Login failed:', error)
      showToast('登录失败，请重试')
      return false
    }
  }

  const logout = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('h5_token')
  }

  const fetchUserInfo = async () => {
    try {
      const res = await api.get('/user/info')
      if (res.data.code === 0) {
        userInfo.value = res.data.data
      }
    } catch (error) {
      console.error('Fetch user info failed:', error)
    }
  }

  const updateUserInfo = async (data) => {
    try {
      const res = await api.put('/user/info', data)
      if (res.data.code === 0) {
        showToast('更新成功')
        await fetchUserInfo()
        return true
      }
      return false
    } catch (error) {
      console.error('Update user info failed:', error)
      return false
    }
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    sendCode,
    loginWithCode,
    logout,
    fetchUserInfo,
    updateUserInfo
  }
})
