<template>
  <div class="page-container">
    <div class="card">
      <div class="card-header">
        <h3>题库管理</h3>
        <div class="header-actions">
          <el-button type="primary" @click="handleAdd">添加题目</el-button>
        </div>
      </div>
      <el-table :data="questions" stripe style="width: 100%;" max-height="calc(100vh - 280px)" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="题目标题" show-overflow-tooltip />
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="difficulty" label="难度" width="100">
          <template #default="{ row }">
            <el-tag :type="getDifficultyType(row.difficulty)">{{ row.difficulty }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="类型" width="100" />
        <el-table-column prop="viewCount" label="浏览次数" width="100" />
        <el-table-column label="操作" fixed="right" width="150">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top: 20px; justify-content: flex-end;"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- Add/Edit Dialog -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px">
        <el-form-item label="题目标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入题目标题" />
        </el-form-item>
        <el-form-item label="题目内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="3" placeholder="请输入题目内容" />
        </el-form-item>
        <el-form-item label="题目类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择题目类型" style="width: 100%">
            <el-option label="技术面试" value="technical" />
            <el-option label="行为面试" value="behavioral" />
            <el-option label="场景面试" value="scenario" />
            <el-option label="压力面试" value="stress" />
          </el-select>
        </el-form-item>
        <el-form-item label="难度" prop="difficulty">
          <el-select v-model="form.difficulty" placeholder="请选择难度" style="width: 100%">
            <el-option label="简单" value="简单" />
            <el-option label="中等" value="中等" />
            <el-option label="困难" value="困难" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-input v-model="form.category" placeholder="请输入分类" />
        </el-form-item>
        <el-form-item label="标签" prop="tags">
          <el-input v-model="form.tags" placeholder="请输入标签，多个用逗号分隔" />
        </el-form-item>
        <el-form-item label="参考答案" prop="answer">
          <el-input v-model="form.answer" type="textarea" :rows="4" placeholder="请输入参考答案" />
        </el-form-item>
        <el-form-item label="解析" prop="explanation">
          <el-input v-model="form.explanation" type="textarea" :rows="3" placeholder="请输入题目解析" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getQuestionList, createQuestion, updateQuestion, deleteQuestion } from '@/api/question'

const questions = ref([])

const loading = ref(false)
const submitLoading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const dialogVisible = ref(false)
const dialogTitle = ref('添加题目')
const isEdit = ref(false)
const editingId = ref(null)
const formRef = ref(null)

const form = reactive({
  title: '',
  content: '',
  type: '',
  difficulty: '',
  category: '',
  tags: '',
  answer: '',
  explanation: ''
})

const formRules = {
  title: [{ required: true, message: '请输入题目标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入题目内容', trigger: 'blur' }],
  type: [{ required: true, message: '请选择题目类型', trigger: 'change' }],
  difficulty: [{ required: true, message: '请选择难度', trigger: 'change' }]
}

const getDifficultyType = (difficulty) => {
  const map = { '简单': 'success', '中等': 'warning', '困难': 'danger' }
  return map[difficulty] || 'info'
}

const fetchQuestions = async () => {
  loading.value = true
  try {
    const res = await getQuestionList(currentPage.value, pageSize.value)
    const pageData = res.data.data
    if (pageData) {
      questions.value = pageData.records || []
      total.value = pageData.total || 0
    }
  } catch (error) {
    console.error('Failed to fetch questions:', error)
  } finally {
    loading.value = false
  }
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  fetchQuestions()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchQuestions()
}

const resetForm = () => {
  form.title = ''
  form.content = ''
  form.type = ''
  form.difficulty = ''
  form.category = ''
  form.tags = ''
  form.answer = ''
  form.explanation = ''
}

const handleAdd = () => {
  resetForm()
  dialogTitle.value = '添加题目'
  isEdit.value = false
  editingId.value = null
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑题目'
  isEdit.value = true
  editingId.value = row.id
  form.title = row.title || ''
  form.content = row.content || ''
  form.type = row.type || ''
  form.difficulty = row.difficulty || ''
  form.category = row.category || ''
  form.tags = row.tags || ''
  form.answer = row.answer || ''
  form.explanation = row.explanation || ''
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除题目「${row.title}」吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteQuestion(row.id)
      ElMessage.success('删除成功')
      fetchQuestions()
    } catch (error) {
      console.error('Failed to delete question:', error)
    }
  }).catch(() => {})
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    submitLoading.value = true
    try {
      const data = {
        title: form.title,
        content: form.content,
        type: form.type,
        difficulty: form.difficulty,
        category: form.category,
        tags: form.tags,
        answer: form.answer,
        explanation: form.explanation
      }

      if (isEdit.value) {
        await updateQuestion(editingId.value, data)
        ElMessage.success('更新成功')
      } else {
        await createQuestion(data)
        ElMessage.success('添加成功')
      }

      dialogVisible.value = false
      fetchQuestions()
    } catch (error) {
      console.error('Failed to submit question:', error)
    } finally {
      submitLoading.value = false
    }
  })
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
}

onMounted(() => {
  fetchQuestions()
})
</script>

<style lang="scss" scoped>
.card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h3 {
      margin: 0;
      font-size: 18px;
      color: #202124;
    }

    .header-actions {
      display: flex;
      gap: 12px;
    }
  }
}
</style>
