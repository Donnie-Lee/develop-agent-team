import api from './index'

/**
 * 获取题库列表
 * @param {Object} params - 查询参数 { category, page, pageSize }
 * @returns {Promise}
 */
export const fetchQuestionList = (params) => {
  return api.get('/questions', { params })
}

/**
 * 搜索题库
 * @param {Object} params - 搜索参数
 * @returns {Promise}
 */
export const searchQuestions = (params) => {
  return api.get('/questions/search', { params })
}

export default {
  fetchQuestionList,
  searchQuestions
}