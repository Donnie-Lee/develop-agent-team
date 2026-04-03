import axios from 'axios'
import { showToast } from 'vant'

const api = axios.create({
  baseURL: '/api/v1',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('h5_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    if (error.response) {
      if (error.response.status === 401) {
        showToast('登录已过期，请重新登录')
        localStorage.removeItem('h5_token')
        window.location.href = '/login'
      } else {
        showToast(error.response.data?.message || '请求失败')
      }
    } else {
      showToast('网络错误')
    }
    return Promise.reject(error)
  }
)

export default api
