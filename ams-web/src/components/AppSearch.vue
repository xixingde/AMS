<template>
  <div class="search-area">
    <slot />
    <el-button type="primary" :icon="Search" @click="emit('search')">查询</el-button>
    <el-button :icon="Refresh" @click="emit('reset')">重置</el-button>
  </div>
</template>

<script setup lang="ts">
import { Search, Refresh } from '@element-plus/icons-vue';

const emit = defineEmits(['search', 'reset']);
</script>

<style scoped>
.search-area {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 16px;
}
</style>
