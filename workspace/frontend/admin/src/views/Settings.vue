<template>
  <div class="page-container">
    <el-row :gutter="24">
      <el-col :span="16">
        <div class="card">
          <h3>系统设置</h3>
          <el-form label-width="120px" style="margin-top: 20px;">
            <el-form-item label="网站名称">
              <el-input v-model="settings.siteName" />
            </el-form-item>
            <el-form-item label="网站描述">
              <el-input v-model="settings.siteDescription" type="textarea" :rows="3" />
            </el-form-item>
            <el-form-item label="联系电话">
              <el-input v-model="settings.contactPhone" />
            </el-form-item>
            <el-form-item label="联系邮箱">
              <el-input v-model="settings.contactEmail" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSave">保存设置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="card">
          <h3>系统信息</h3>
          <div class="system-info">
            <div class="info-item">
              <span class="label">版本号：</span>
              <span class="value">1.0.0</span>
            </div>
            <div class="info-item">
              <span class="label">环境：</span>
              <el-tag size="small">生产环境</el-tag>
            </div>
            <div class="info-item">
              <span class="label">最后更新：</span>
              <span class="value">2026-04-03</span>
            </div>
          </div>
        </div>
        <div class="card" style="margin-top: 24px;">
          <h3>快捷操作</h3>
          <div class="quick-actions">
            <el-button style="width: 100%; margin-bottom: 12px;">清除缓存</el-button>
            <el-button style="width: 100%; margin-bottom: 12px;">重新加载配置</el-button>
            <el-button type="danger" style="width: 100%;">系统重置</el-button>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'

const settings = reactive({
  siteName: 'InterviewAI 智能面试练兵场',
  siteDescription: '面向求职者的AI模拟面试平台，帮助用户提升面试技能',
  contactPhone: '400-123-4567',
  contactEmail: 'support@interviewai.com'
})

const handleSave = () => {
  ElMessage.success('设置保存成功')
}
</script>

<style lang="scss" scoped>
.card {
  h3 {
    font-size: 18px;
    color: #202124;
    margin: 0 0 20px;
    padding-bottom: 16px;
    border-bottom: 1px solid #E8EAED;
  }
}

.system-info {
  .info-item {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 12px 0;
    border-bottom: 1px solid #E8EAED;

    &:last-child {
      border-bottom: none;
    }

    .label {
      color: #5F6368;
    }

    .value {
      color: #202124;
    }
  }
}

.quick-actions {
  margin-top: 16px;
}
</style>
