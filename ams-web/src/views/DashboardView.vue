<template>
  <div class="page-title">首页</div>
  <div class="stat-grid">
    <el-card shadow="hover">
      <div class="stat-value">{{ stats.systemCount }}</div>
      <div class="stat-label">系统总数</div>
    </el-card>
    <el-card shadow="hover">
      <div class="stat-value">{{ stats.subSystemCount }}</div>
      <div class="stat-label">子系统总数</div>
    </el-card>
    <el-card shadow="hover">
      <div class="stat-value">{{ stats.releaseUnitCount }}</div>
      <div class="stat-label">发布单元总数</div>
    </el-card>
    <el-card shadow="hover">
      <div class="stat-value">{{ stats.techStackCount }}</div>
      <div class="stat-label">技术栈总数</div>
    </el-card>
  </div>
  <el-card>
    <template #header>
      <span>快捷入口</span>
    </template>
    <el-space>
      <el-button type="primary" @click="$router.push('/system')">进入系统清单</el-button>
      <el-button type="primary" @click="$router.push('/techstack')">进入技术栈管理</el-button>
      <el-button type="primary" @click="$router.push('/diagram')">查看架构图</el-button>
    </el-space>
  </el-card>
</template>

<script setup lang="ts">
import { reactive } from 'vue';

const stats = reactive({
  systemCount: 0,
  subSystemCount: 0,
  releaseUnitCount: 0,
  techStackCount: 0,
});
</script>

<style scoped>
.page-title {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 16px;
}
.stat-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}
.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
}
.stat-label {
  color: #909399;
  margin-top: 4px;
}
</style>
