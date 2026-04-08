import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '@/api'
import { showToast } from 'vant'

export const useResumeStore = defineStore('resume', () => {
  const resumes = ref([])
  const currentResume = ref(null)

  const fetchResumes = async () => {
    try {
      const res = await api.get('/resumes')
      if (res.data.code === 0) {
        resumes.value = res.data.data || []
      }
    } catch (error) {
      console.error('Fetch resumes failed:', error)
    }
  }

  const fetchResumeDetail = async (id) => {
    try {
      const res = await api.get(`/resumes/${id}`)
      if (res.data.code === 0) {
        currentResume.value = res.data.data
        return currentResume.value
      }
      return null
    } catch (error) {
      console.error('Fetch resume detail failed:', error)
      return null
    }
  }

  const createResume = async (data) => {
    try {
      const res = await api.post('/resumes', data)
      if (res.data.code === 0) {
        showToast('创建成功')
        await fetchResumes()
        return res.data.data
      }
      return null
    } catch (error) {
      console.error('Create resume failed:', error)
      return null
    }
  }

  const updateResume = async (id, data) => {
    try {
      const res = await api.put(`/resumes/${id}`, data)
      if (res.data.code === 0) {
        showToast('保存成功')
        return true
      }
      return false
    } catch (error) {
      console.error('Update resume failed:', error)
      return false
    }
  }

  const deleteResume = async (id) => {
    try {
      const res = await api.delete(`/resumes/${id}`)
      if (res.data.code === 0) {
        showToast('删除成功')
        await fetchResumes()
        return true
      }
      return false
    } catch (error) {
      console.error('Delete resume failed:', error)
      return false
    }
  }

  const parseResume = async (id) => {
    try {
      const res = await api.post(`/resumes/${id}/parse`)
      if (res.data.code === 0) {
        showToast('简历解析中...')
        return true
      }
      return false
    } catch (error) {
      console.error('Parse resume failed:', error)
      return false
    }
  }

  const setDefaultResume = async (id) => {
    try {
      const res = await api.put(`/resumes/${id}`, { isDefault: true })
      if (res.data.code === 0) {
        showToast('设置成功')
        await fetchResumes()
        return true
      }
      return false
    } catch (error) {
      console.error('Set default resume failed:', error)
      return false
    }
  }

  const uploadFile = async (file) => {
    try {
      const formData = new FormData()
      formData.append('file', file)
      const res = await api.post('/resumes/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
      if (res.data.code === 0) {
        showToast('上传成功')
        return res.data.data
      }
      return null
    } catch (error) {
      console.error('Upload file failed:', error)
      return null
    }
  }

  return {
    resumes,
    currentResume,
    fetchResumes,
    fetchResumeDetail,
    createResume,
    updateResume,
    deleteResume,
    parseResume,
    uploadFile,
    setDefaultResume
  }
})
