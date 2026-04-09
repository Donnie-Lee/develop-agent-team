import { defineStore } from 'pinia'
import { ref } from 'vue'
import {
  createInterview,
  fetchQuestions,
  submitAnswer,
  fetchReport,
  getInterview,
  fetchInterviewHistory
} from '@/api/interview'
import { showToast } from 'vant'

export const useInterviewStore = defineStore('interview', () => {
  // 当前面试信息
  const currentInterview = ref(null)
  // 面试问题列表
  const questions = ref([])
  // 面试历史记录
  const history = ref([])
  // 面试报告
  const report = ref(null)
  // 加载状态
  const loading = ref(false)

  /**
   * 创建面试
   * @param {Object} data - 面试配置数据
   * @returns {Promise<Object|null>} 返回创建的面试ID和信息
   */
  const createNewInterview = async (data) => {
    loading.value = true
    try {
      const res = await createInterview(data)
      if (res.data.code === 0) {
        currentInterview.value = res.data.data
        showToast('面试创建成功')
        return currentInterview.value
      }
      showToast(res.data.message || '创建失败')
      return null
    } catch (error) {
      console.error('Create interview failed:', error)
      showToast('创建面试失败，请重试')
      return null
    } finally {
      loading.value = false
    }
  }

  /**
   * 获取面试问题列表
   * @param {string} interviewId - 面试ID
   * @returns {Promise<Array>} 返回问题列表
   */
  const getQuestions = async (interviewId) => {
    loading.value = true
    try {
      const res = await fetchQuestions(interviewId)
      if (res.data.code === 0) {
        questions.value = res.data.data || []
        return questions.value
      }
      showToast(res.data.message || '获取问题失败')
      return []
    } catch (error) {
      console.error('Fetch questions failed:', error)
      showToast('获取问题失败，请重试')
      return []
    } finally {
      loading.value = false
    }
  }

  /**
   * 提交回答
   * @param {string} interviewId - 面试ID
   * @param {Object} answerData - 回答数据 {questionId, content, type}
   * @returns {Promise<boolean>}
   */
  const submitUserAnswer = async (interviewId, answerData) => {
    loading.value = true
    try {
      const res = await submitAnswer(interviewId, answerData)
      if (res.data.code === 0) {
        showToast('回答已提交')
        return true
      }
      showToast(res.data.message || '提交失败')
      return false
    } catch (error) {
      console.error('Submit answer failed:', error)
      showToast('提交失败，请重试')
      return false
    } finally {
      loading.value = false
    }
  }

  /**
   * 获取面试报告
   * @param {string} interviewId - 面试ID
   * @returns {Promise<Object|null>}
   */
  const getReport = async (interviewId) => {
    loading.value = true
    try {
      const res = await fetchReport(interviewId)
      if (res.data.code === 0) {
        report.value = res.data.data
        return report.value
      }
      showToast(res.data.message || '获取报告失败')
      return null
    } catch (error) {
      console.error('Fetch report failed:', error)
      showToast('获取报告失败，请重试')
      return null
    } finally {
      loading.value = false
    }
  }

  /**
   * 获取面试详情
   * @param {string} interviewId - 面试ID
   * @returns {Promise<Object|null>}
   */
  const getInterviewDetail = async (interviewId) => {
    loading.value = true
    try {
      const res = await getInterview(interviewId)
      if (res.data.code === 0) {
        currentInterview.value = res.data.data
        return currentInterview.value
      }
      return null
    } catch (error) {
      console.error('Get interview failed:', error)
      return null
    } finally {
      loading.value = false
    }
  }

  /**
   * 获取面试历史记录
   * @returns {Promise<Array>}
   */
  const getHistory = async () => {
    loading.value = true
    try {
      const res = await fetchInterviewHistory()
      if (res.data.code === 0) {
        history.value = res.data.data || []
        return history.value
      }
      return []
    } catch (error) {
      console.error('Fetch history failed:', error)
      return []
    } finally {
      loading.value = false
    }
  }

  /**
   * 重置状态
   */
  const reset = () => {
    currentInterview.value = null
    questions.value = []
    report.value = null
    loading.value = false
  }

  return {
    currentInterview,
    questions,
    history,
    report,
    loading,
    createNewInterview,
    getQuestions,
    submitUserAnswer,
    getReport,
    getInterviewDetail,
    getHistory,
    reset
  }
})
