<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h1>InterviewAI</h1>
        <p>智能面试练兵场 - 管理后台</p>
      </div>
      <el-form ref="loginFormRef" :model="loginForm" :rules="rules" class="login-form">
        <el-form-item prop="phone">
          <el-input
            v-model="loginForm.phone"
            placeholder="请输入手机号"
            prefix-icon="Phone"
            size="large"
          />
        </el-form-item>
        <el-form-item prop="code">
          <el-input
            v-model="loginForm.code"
            placeholder="请输入验证码"
            prefix-icon="Lock"
            size="large"
            @keyup.enter="handleLogin"
          >
            <template #append>
              <el-button :disabled="countdown > 0" @click="handleSendCode">
                {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
              </el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-button"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import api from '@/api'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref(null)
const loading = ref(false)
const countdown = ref(0)

const loginForm = reactive({
  phone: '',
  code: ''
})

const rules = {
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

let countdownTimer = null

const handleSendCode = async () => {
  if (!loginForm.phone || !/^1[3-9]\d{9}$/.test(loginForm.phone)) {
    ElMessage.warning('请输入正确的手机号')
    return
  }
  try {
    await api.post('/user/send-code', { phone: loginForm.phone, type: 'login' })
    ElMessage.success('验证码已发送')
    countdown.value = 60
    countdownTimer = setInterval(() => {
      countdown.value--
      if (countdown.value <= 0) {
        clearInterval(countdownTimer)
      }
    }, 1000)
  } catch (error) {
    ElMessage.error('发送验证码失败')
  }
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const success = await userStore.login(loginForm.phone, loginForm.code)
        if (success) {
          ElMessage.success('登录成功')
          router.push('/')
        }
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style lang="scss" scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #E8F0FE 0%, #F8F9FA 100%);
}

.login-card {
  width: 400px;
  background: white;
  border-radius: 16px;
  padding: 48px 40px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.login-header {
  text-align: center;
  margin-bottom: 40px;

  h1 {
    font-size: 28px;
    color: #1A73E8;
    margin-bottom: 8px;
  }

  p {
    color: #5F6368;
    font-size: 14px;
  }
}

.login-form {
  :deep(.el-form-item) {
    margin-bottom: 24px;
  }

  :deep(.el-input__wrapper) {
    padding: 12px 16px;
  }
}

.login-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
}
</style>
