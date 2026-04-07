<template>
  <div class="resume-edit-page">
    <div class="page-header">
      <div class="header-left" @click="handleBack">
        <van-icon name="arrow-left" size="20" />
        <span>{{ isEdit ? '编辑简历' : '新建简历' }}</span>
      </div>
      <div class="header-right" @click="handleSave">
        保存
      </div>
    </div>

    <div class="form-content">
      <!-- 基本信息 -->
      <div class="form-section">
        <h3 class="section-title">基本信息</h3>
        <div class="form-group">
          <van-cell-group inset>
            <van-field
              v-model="form.name"
              label="姓名"
              placeholder="请输入姓名"
            />
            <van-field
              v-model="form.phone"
              label="手机"
              placeholder="请输入手机号"
              type="tel"
            />
            <van-field
              v-model="form.email"
              label="邮箱"
              placeholder="请输入邮箱"
              type="email"
            />
            <van-field
              v-model="form.city"
              label="城市"
              placeholder="请输入所在城市"
            />
          </van-cell-group>
        </div>
      </div>

      <!-- 教育经历 -->
      <div class="form-section">
        <div class="section-header">
          <h3 class="section-title">教育经历</h3>
          <van-button size="small" type="primary" @click="handleAddItem('education')">
            + 添加
          </van-button>
        </div>
        <div v-for="(item, index) in form.education" :key="index" class="item-card">
          <van-cell-group inset>
            <van-field
              v-model="item.school"
              label="学校"
              placeholder="请输入学校名称"
            />
            <van-field
              v-model="item.degree"
              label="学历"
              placeholder="本科/硕士/博士"
            />
            <van-field
              v-model="item.major"
              label="专业"
              placeholder="请输入专业"
            />
            <van-field
              v-model="item.duration"
              label="时间"
              placeholder="如：2019-2023"
            />
          </van-cell-group>
          <van-button size="small" type="danger" @click="handleRemoveItem('education', index)">
            删除
          </van-button>
        </div>
      </div>

      <!-- 工作经历 -->
      <div class="form-section">
        <div class="section-header">
          <h3 class="section-title">工作经历</h3>
          <van-button size="small" type="primary" @click="handleAddItem('work')">
            + 添加
          </van-button>
        </div>
        <div v-for="(item, index) in form.work" :key="index" class="item-card">
          <van-cell-group inset>
            <van-field
              v-model="item.company"
              label="公司"
              placeholder="请输入公司名称"
            />
            <van-field
              v-model="item.position"
              label="职位"
              placeholder="请输入职位"
            />
            <van-field
              v-model="item.duration"
              label="时间"
              placeholder="如：2023-至今"
            />
            <van-field
              v-model="item.description"
              label="描述"
              type="textarea"
              rows="3"
              placeholder="请输入工作描述"
            />
          </van-cell-group>
          <van-button size="small" type="danger" @click="handleRemoveItem('work', index)">
            删除
          </van-button>
        </div>
      </div>

      <!-- 项目经历 -->
      <div class="form-section">
        <div class="section-header">
          <h3 class="section-title">项目经历</h3>
          <van-button size="small" type="primary" @click="handleAddItem('project')">
            + 添加
          </van-button>
        </div>
        <div v-for="(item, index) in form.project" :key="index" class="item-card">
          <van-cell-group inset>
            <van-field
              v-model="item.name"
              label="项目名称"
              placeholder="请输入项目名称"
            />
            <van-field
              v-model="item.role"
              label="角色"
              placeholder="请输入项目角色"
            />
            <van-field
              v-model="item.duration"
              label="时间"
              placeholder="如：2023.06-2023.12"
            />
            <van-field
              v-model="item.description"
              label="描述"
              type="textarea"
              rows="3"
              placeholder="请输入项目描述"
            />
          </van-cell-group>
          <van-button size="small" type="danger" @click="handleRemoveItem('project', index)">
            删除
          </van-button>
        </div>
      </div>

      <!-- 技能证书 -->
      <div class="form-section">
        <div class="section-header">
          <h3 class="section-title">技能证书</h3>
          <van-button size="small" type="primary" @click="handleAddItem('skill')">
            + 添加
          </van-button>
        </div>
        <div v-for="(item, index) in form.skill" :key="index" class="item-card">
          <van-cell-group inset>
            <van-field
              v-model="item.name"
              label="技能/证书"
              placeholder="请输入技能或证书名称"
            />
            <van-field
              v-model="item.level"
              label="熟练度"
              placeholder="如：精通/熟悉/了解"
            />
          </van-cell-group>
          <van-button size="small" type="danger" @click="handleRemoveItem('skill', index)">
            删除
          </van-button>
        </div>
      </div>
    </div>

    <!-- 文件上传区域 -->
    <div class="upload-section">
      <h3 class="section-title">简历附件</h3>
      <div class="upload-area" @click="handleFileUpload">
        <van-uploader :after-read="handleFileSelect" accept=".pdf,.doc,.docx" :max-count="1">
          <div class="upload-content">
            <van-icon name="upgrade" size="48" color="#9AA0A6" />
            <span>点击上传简历文件</span>
            <p>支持 PDF、Word 格式</p>
          </div>
        </van-uploader>
      </div>
      <div v-if="uploadedFile" class="uploaded-file">
        <van-icon name="description" size="24" color="#5F6368" />
        <span>{{ uploadedFile.name }}</span>
        <van-icon name="cross" size="16" @click="handleRemoveFile" />
      </div>
    </div>

    <van-button type="primary" size="large" class="save-btn" @click="handleSave">
      保存简历
    </van-button>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useResumeStore } from '@/stores/resume'
import { showToast, showConfirmDialog } from 'vant'

const router = useRouter()
const route = useRoute()
const resumeStore = useResumeStore()

const resumeId = computed(() => route.query.id)
const isEdit = computed(() => !!resumeId.value)

const form = reactive({
  name: '',
  phone: '',
  email: '',
  city: '',
  title: '我的简历',
  education: [],
  work: [],
  project: [],
  skill: []
})

const uploadedFile = ref(null)

onMounted(async () => {
  if (resumeId.value) {
    const resume = await resumeStore.fetchResumeDetail(resumeId.value)
    if (resume) {
      Object.assign(form, resume)
    }
  }
})

const handleBack = async () => {
  if (hasChanges()) {
    try {
      await showConfirmDialog({
        title: '确认返回',
        message: '有未保存的修改，确定要返回吗？'
      })
      router.back()
    } catch (e) {
      // 用户取消
    }
  } else {
    router.back()
  }
}

const hasChanges = () => {
  return form.name || form.phone || form.education.length > 0
}

const handleAddItem = (type) => {
  const templates = {
    education: { school: '', degree: '', major: '', duration: '' },
    work: { company: '', position: '', duration: '', description: '' },
    project: { name: '', role: '', duration: '', description: '' },
    skill: { name: '', level: '' }
  }
  form[type].push({ ...templates[type] })
}

const handleRemoveItem = (type, index) => {
  form[type].splice(index, 1)
}

const handleFileUpload = () => {
  // 触发文件选择
}

const handleFileSelect = async (file) => {
  uploadedFile.value = file.file
  const result = await resumeStore.uploadFile(file.file)
  if (result) {
    form.title = result.fileName || '我的简历'
  }
}

const handleRemoveFile = () => {
  uploadedFile.value = null
}

const handleSave = async () => {
  if (!form.name) {
    showToast('请填写姓名')
    return
  }

  let success
  if (isEdit.value) {
    success = await resumeStore.updateResume(resumeId.value, form)
  } else {
    const newResume = await resumeStore.createResume(form)
    success = !!newResume
  }

  if (success) {
    router.back()
  }
}
</script>

<style lang="scss" scoped>
.resume-edit-page {
  min-height: 100vh;
  background: #F8F9FA;
  padding-bottom: 80px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: white;
  border-bottom: 1px solid #E8EAED;
  position: sticky;
  top: 0;
  z-index: 10;

  .header-left {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 18px;
    font-weight: 600;
    color: #202124;
  }

  .header-right {
    font-size: 14px;
    color: #1A73E8;
    font-weight: 500;
  }
}

.form-content {
  padding: 16px;
}

.form-section {
  margin-bottom: 24px;

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
  }

  .section-title {
    font-size: 16px;
    font-weight: 600;
    color: #202124;
    margin: 0 0 12px;

    .section-header & {
      margin: 0;
    }
  }

  .item-card {
    background: white;
    border-radius: 12px;
    padding: 12px;
    margin-bottom: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);

    .van-button {
      margin-top: 12px;
      width: 100%;
    }
  }
}

.upload-section {
  padding: 0 16px;

  .section-title {
    font-size: 16px;
    font-weight: 600;
    color: #202124;
    margin: 0 0 12px;
  }

  .upload-area {
    background: white;
    border: 2px dashed #E8EAED;
    border-radius: 12px;
    padding: 32px;
    text-align: center;

    .upload-content {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 8px;

      span {
        font-size: 14px;
        color: #5F6368;
      }

      p {
        font-size: 12px;
        color: #9AA0A6;
        margin: 4px 0 0;
      }
    }
  }

  .uploaded-file {
    display: flex;
    align-items: center;
    gap: 8px;
    background: white;
    padding: 12px 16px;
    border-radius: 8px;
    margin-top: 12px;

    span {
      flex: 1;
      font-size: 14px;
      color: #5F6368;
    }
  }
}

.save-btn {
  position: fixed;
  bottom: 24px;
  left: 16px;
  right: 16px;
  max-width: calc(600px - 32px);
  margin: 0 auto;
}
</style>
