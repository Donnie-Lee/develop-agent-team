<template>
  <div class="resume-list-page">
    <div class="page-header">
      <div class="header-left" @click="handleBack">
        <van-icon name="arrow-left" size="20" />
        <span>我的简历</span>
      </div>
      <div class="header-right" @click="handleAdd">
        <van-icon name="plus" size="20" />
        <span>新建</span>
      </div>
    </div>

    <div class="resume-list">
      <div v-for="resume in resumes" :key="resume.id" class="resume-card">
        <div class="resume-header">
          <van-icon name="description" size="24" color="#5F6368" />
          <div class="resume-info">
            <h4>{{ resume.title }}</h4>
            <div class="resume-meta">
              <van-tag v-if="resume.isDefault" type="success" size="small">默认简历</van-tag>
              <span class="file-name">{{ resume.fileName }}</span>
            </div>
          </div>
        </div>
        <div class="resume-actions">
          <van-button size="small" @click="handleSetDefault(resume)" v-if="!resume.isDefault">
            设为默认
          </van-button>
          <van-button size="small" type="primary" @click="handleEdit(resume)">
            编辑
          </van-button>
          <van-button size="small" type="danger" @click="handleDelete(resume)">
            删除
          </van-button>
        </div>
      </div>

      <!-- 添加简历按钮 -->
      <div class="add-resume-card" @click="handleAdd">
        <div class="add-content">
          <van-icon name="plus" size="32" color="#9AA0A6" />
          <span>添加简历</span>
          <p class="add-options">在线创建 / 上传文件</p>
        </div>
      </div>
    </div>

    <!-- 上传文件弹窗 -->
    <van-popup v-model:show="showUpload" position="bottom" round>
      <div class="upload-popup">
        <h4>上传简历</h4>
        <van-uploader :after-read="handleFileUpload" accept=".pdf,.doc,.docx,.jpg,.png">
          <van-button type="primary" size="large" icon="plus">
            点击上传简历
          </van-button>
        </van-uploader>
        <p class="upload-tip">支持 PDF、Word、图片格式</p>
      </div>
    </van-popup>

    <!-- 添加方式选择 -->
    <van-action-sheet
      v-model:show="showAddOptions"
      :actions="addActions"
      cancel-text="取消"
      @select="handleSelectAddOption"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useResumeStore } from '@/stores/resume'
import { showToast, showConfirmDialog } from 'vant'

const router = useRouter()
const resumeStore = useResumeStore()

const resumes = ref([])
const showUpload = ref(false)
const showAddOptions = ref(false)

const addActions = [
  { name: '在线创建', action: 'create' },
  { name: '上传文件', action: 'upload' }
]

onMounted(async () => {
  await loadResumes()
})

const loadResumes = async () => {
  await resumeStore.fetchResumes()
  resumes.value = resumeStore.resumes
}

const handleBack = () => {
  router.back()
}

const handleAdd = () => {
  showAddOptions.value = true
}

const handleSelectAddOption = (action) => {
  showAddOptions.value = false
  if (action.action === 'create') {
    router.push('/resume/edit')
  } else if (action.action === 'upload') {
    showUpload.value = true
  }
}

const handleFileUpload = async (file) => {
  const result = await resumeStore.uploadFile(file.file)
  if (result) {
    showUpload.value = false
    await loadResumes()
  }
}

const handleSetDefault = async (resume) => {
  await resumeStore.setDefaultResume(resume.id)
  await loadResumes()
}

const handleEdit = (resume) => {
  router.push(`/resume/edit?id=${resume.id}`)
}

const handleDelete = async (resume) => {
  try {
    await showConfirmDialog({
      title: '确认删除',
      message: `确定删除简历「${resume.title}」吗？`
    })
    await resumeStore.deleteResume(resume.id)
    await loadResumes()
  } catch (e) {
    // 用户取消
  }
}
</script>

<style lang="scss" scoped>
.resume-list-page {
  min-height: 100vh;
  background: #F8F9FA;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: white;
  border-bottom: 1px solid #E8EAED;

  .header-left {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 18px;
    font-weight: 600;
    color: #202124;
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 14px;
    color: #1A73E8;
  }
}

.resume-list {
  padding: 16px;
}

.resume-card {
  background: white;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);

  .resume-header {
    display: flex;
    align-items: flex-start;
    gap: 12px;

    .resume-info {
      flex: 1;

      h4 {
        font-size: 16px;
        font-weight: 600;
        color: #202124;
        margin: 0 0 8px;
      }

      .resume-meta {
        display: flex;
        align-items: center;
        gap: 8px;

        .file-name {
          font-size: 14px;
          color: #5F6368;
        }
      }
    }
  }

  .resume-actions {
    display: flex;
    gap: 8px;
    margin-top: 12px;
    padding-top: 12px;
    border-top: 1px solid #E8EAED;
  }
}

.add-resume-card {
  background: white;
  border: 2px dashed #E8EAED;
  border-radius: 12px;
  padding: 32px;
  text-align: center;
  cursor: pointer;

  .add-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;

    span {
      font-size: 16px;
      color: #5F6368;
    }

    .add-options {
      font-size: 12px;
      color: #9AA0A6;
      margin: 4px 0 0;
    }
  }
}

.upload-popup {
  padding: 24px;
  text-align: center;

  h4 {
    font-size: 18px;
    color: #202124;
    margin: 0 0 24px;
  }

  .upload-tip {
    font-size: 12px;
    color: #9AA0A6;
    margin: 16px 0 0;
  }
}
</style>
