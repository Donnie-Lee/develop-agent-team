import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '@/api'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('admin_token') || '')
  const userInfo = ref(null)

  const login = async (username, password) => {
    try {
      const res = await api.post('/auth/login', { username, password })
      if (res.data.code === 0) {
        token.value = res.data.data.token
        localStorage.setItem('admin_token', res.data.data.token)
        await fetchUserInfo()
        return true
      }
      return false
    } catch (error) {
      console.error('Login failed:', error)
      return false
    }
  }

  const logout = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('admin_token')
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

  return {
    token,
    userInfo,
    login,
    logout,
    fetchUserInfo
  }
})
