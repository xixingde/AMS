<template>
  <el-table :data="data" v-loading="loading" stripe border style="width: 100%">
    <slot />
  </el-table>
</template>

<script setup lang="ts">
defineProps<{
  data: any[];
  loading?: boolean;
}>();
</script>
