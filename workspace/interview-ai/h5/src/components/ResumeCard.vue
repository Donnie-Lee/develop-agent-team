<template>
  <div class="resume-card" @click="$emit('click')">
    <div class="card-left">
      <van-icon name="description" size="32" color="#5F6368" />
    </div>
    <div class="card-content">
      <h4>{{ resume.title }}</h4>
      <div class="card-meta">
        <van-tag v-if="resume.isDefault" type="success" size="small">默认</van-tag>
        <span class="file-name">{{ resume.fileName || '在线简历' }}</span>
      </div>
      <div class="card-status">
        <van-tag :type="getParseStatusType(resume.parseStatus)" size="small">
          {{ getParseStatusText(resume.parseStatus) }}
        </van-tag>
      </div>
    </div>
    <div class="card-actions">
      <van-button size="small" type="primary" @click.stop="$emit('edit')">编辑</van-button>
      <van-button size="small" type="danger" @click.stop="$emit('delete')">删除</van-button>
    </div>
  </div>
</template>

<script setup>
defineProps({
  resume: {
    type: Object,
    required: true
  }
})

defineEmits(['click', 'edit', 'delete'])

const getParseStatusType = (status) => {
  const map = {
    success: 'success',
    pending: 'warning',
    failed: 'danger',
    parsing: 'primary'
  }
  return map[status] || 'default'
}

const getParseStatusText = (status) => {
  const map = {
    success: '已解析',
    pending: '待解析',
    failed: '解析失败',
    parsing: '解析中'
  }
  return map[status] || status
}
</script>

<style lang="scss" scoped>
.resume-card {
  display: flex;
  align-items: center;
  gap: 12px;
  background: white;
  padding: 16px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  margin-bottom: 12px;

  .card-content {
    flex: 1;

    h4 {
      font-size: 16px;
      font-weight: 600;
      color: #202124;
      margin: 0 0 8px;
    }

    .card-meta {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 4px;

      .file-name {
        font-size: 14px;
        color: #5F6368;
      }
    }

    .card-status {
      margin-top: 4px;
    }
  }

  .card-actions {
    display: flex;
    gap: 8px;
  }
}
</style>
