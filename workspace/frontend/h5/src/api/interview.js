import api from './index'

/**
 * 创建面试
 * @param {Object} data - 面试配置数据
 * @returns {Promise}
 */
export const createInterview = (data) => {
  return api.post('/interview', data)
}

/**
 * 获取面试详情
 * @param {string} interviewId - 面试ID
 * @returns {Promise}
 */
export const getInterview = (interviewId) => {
  return api.get(`/interview/${interviewId}`)
}

/**
 * 获取面试问题列表
 * @param {string} sessionId - 面试会话ID
 * @returns {Promise}
 */
export const fetchQuestions = (sessionId) => {
  return api.get(`/interview/session/${sessionId}/questions`)
}

/**
 * 提交回答
 * @param {Object} answerData - 回答数据 (包含 sessionId, questionId 等)
 * @returns {Promise}
 */
export const submitAnswer = (answerData) => {
  return api.post('/interview/answer', answerData)
}

/**
 * 获取面试报告
 * @param {string} interviewId - 面试ID
 * @returns {Promise}
 */
export const fetchReport = (interviewId) => {
  return api.get(`/interview/${interviewId}/report`)
}

/**
 * 获取面试历史记录
 * @returns {Promise}
 */
export const fetchInterviewHistory = () => {
  return api.get('/interview/list')
}

/**
 * 提交语音回答
 * @param {string} interviewId - 面试ID
 * @param {FormData} audioData - 语音数据
 * @returns {Promise}
 */
export const submitVoiceAnswer = (interviewId, audioData) => {
  return api.post(`/interview/${interviewId}/voice`, audioData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/**
 * 获取AI回复(流式)
 * @param {string} interviewId - 面试ID
 * @param {Object} data - 请求数据
 * @returns {Promise}
 */
export const getAiStreamResponse = (interviewId, data) => {
  return api.post(`/interview/${interviewId}/stream`, data, {
    responseType: 'blob'
  })
}

export default {
  createInterview,
  getInterview,
  fetchQuestions,
  submitAnswer,
  fetchReport,
  fetchInterviewHistory,
  submitVoiceAnswer,
  getAiStreamResponse
}
