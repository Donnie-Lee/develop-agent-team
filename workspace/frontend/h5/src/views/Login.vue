<template>
  <div class="login-page">
    <div class="login-header">
      <div class="logo">
        <img src="@/assets/logo.png" alt="InterviewAI" />
        <span>InterviewAI</span>
      </div>
      <p class="slogan">让每一次练习，都离offer更近一步</p>
    </div>

    <div class="login-form">
      <div class="form-item">
        <div class="phone-input">
          <span class="prefix">+86</span>
          <input
            v-model="phone"
            type="tel"
            placeholder="请输入手机号"
            maxlength="11"
            class="input-field"
          />
        </div>
      </div>

      <div class="form-item">
        <div class="code-input">
          <input
            v-model="code"
            type="text"
            placeholder="请输入验证码"
            maxlength="6"
            class="input-field"
          />
          <button
            class="code-btn"
            :disabled="countdown > 0"
            @click="handleSendCode"
          >
            {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
          </button>
        </div>
      </div>

      <button class="btn-primary login-btn" :disabled="loading" @click="handleLogin">
        {{ loading ? '登录中...' : '登录/注册' }}
      </button>

      <div class="third-party">
        <div class="divider">
          <span>或其他登录方式</span>
        </div>
        <div class="third-icons">
          <button class="third-btn">
            <img src="@/assets/wechat.png" alt="微信" />
          </button>
          <button class="third-btn">
            <img src="@/assets/google.png" alt="Google" />
          </button>
        </div>
      </div>
    </div>

    <div class="agreement">
      <van-checkbox v-model="agreed" shape="round" icon-size="16" />
      <span>
        登录即表示同意《<a href="#">用户协议</a>》和《<a href="#">隐私政策</a>》
      </span>
    </div>
  </div>
</template>

<script setup>
import { ref, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { showToast } from 'vant'

const router = useRouter()
const userStore = useUserStore()

const phone = ref('')
const code = ref('')
const agreed = ref(false)
const loading = ref(false)
const countdown = ref(0)
let countdownTimer = null

onUnmounted(() => {
  if (countdownTimer) {
    clearInterval(countdownTimer)
  }
})

const validatePhone = () => {
  if (!phone.value) {
    showToast('请输入手机号')
    return false
  }
  if (!/^1[3-9]\d{9}$/.test(phone.value)) {
    showToast('手机号格式不正确')
    return false
  }
  return true
}

const handleSendCode = async () => {
  if (!validatePhone()) return

  const success = await userStore.sendCode(phone.value)
  if (success) {
    countdown.value = 60
    countdownTimer = setInterval(() => {
      countdown.value--
      if (countdown.value <= 0) {
        clearInterval(countdownTimer)
      }
    }, 1000)
  }
}

const handleLogin = async () => {
  if (!validatePhone()) return

  if (!code.value) {
    showToast('请输入验证码')
    return
  }

  if (!agreed.value) {
    showToast('请先同意用户协议')
    return
  }

  loading.value = true
  try {
    const success = await userStore.loginWithCode(phone.value, code.value)
    if (success) {
      router.replace('/')
    }
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #E8F0FE 0%, #FFFFFF 50%);
  padding: 48px 24px 24px;
}

.login-header {
  text-align: center;
  margin-bottom: 48px;

  .logo {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    margin-bottom: 16px;

    img {
      width: 48px;
      height: 48px;
    }

    span {
      font-size: 24px;
      font-weight: 600;
      color: #1A73E8;
    }
  }

  .slogan {
    font-size: 16px;
    color: #5F6368;
  }
}

.login-form {
  .form-item {
    margin-bottom: 16px;
  }

  .phone-input {
    display: flex;
    align-items: center;
    background: white;
    border: 1px solid #E8EAED;
    border-radius: 8px;
    overflow: hidden;

    .prefix {
      padding: 0 16px;
      height: 48px;
      line-height: 48px;
      background: #F8F9FA;
      color: #5F6368;
      font-size: 14px;
      border-right: 1px solid #E8EAED;
    }

    .input-field {
      flex: 1;
      border: none;
      height: 48px;
      padding: 0 16px;
      font-size: 14px;

      &:focus {
        outline: none;
        box-shadow: none;
      }
    }
  }

  .code-input {
    display: flex;
    gap: 12px;

    .input-field {
      flex: 1;
    }

    .code-btn {
      width: 38%;
      height: 48px;
      background: white;
      border: 1px solid #1A73E8;
      border-radius: 8px;
      color: #1A73E8;
      font-size: 14px;
      cursor: pointer;

      &:disabled {
        background: #F8F9FA;
        color: #9AA0A6;
        border-color: #E8EAED;
        cursor: not-allowed;
      }
    }
  }

  .login-btn {
    margin-top: 24px;
  }
}

.third-party {
  margin-top: 48px;

  .divider {
    text-align: center;
    position: relative;

    &::before,
    &::after {
      content: '';
      position: absolute;
      top: 50%;
      width: 30%;
      height: 1px;
      background: #E8EAED;
    }

    &::before {
      left: 0;
    }

    &::after {
      right: 0;
    }

    span {
      color: #9AA0A6;
      font-size: 12px;
      background: #FFFFFF;
      padding: 0 16px;
    }
  }

  .third-icons {
    display: flex;
    justify-content: center;
    gap: 32px;
    margin-top: 24px;

    .third-btn {
      width: 48px;
      height: 48px;
      border-radius: 50%;
      border: none;
      background: #F8F9FA;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;

      img {
        width: 28px;
        height: 28px;
      }
    }
  }
}

.agreement {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  margin-top: 24px;
  padding: 0 8px;

  span {
    font-size: 12px;
    color: #9AA0A6;
    line-height: 1.5;

    a {
      color: #1A73E8;
    }
  }
}
</style>
