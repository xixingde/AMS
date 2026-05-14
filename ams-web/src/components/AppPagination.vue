<template>
  <el-pagination
    v-model:current-page="currentPage"
    v-model:page-size="pageSize"
    :total="total"
    :page-sizes="[10, 20, 50, 100]"
    layout="total, sizes, prev, pager, next, jumper"
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
  />
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps<{
  page: number;
  pageSize: number;
  total: number;
}>();

const emit = defineEmits(['update:page', 'update:pageSize', 'change']);

const currentPage = computed({
  get: () => props.page,
  set: (val) => {
    emit('update:page', val);
  },
});

const pageSize = computed({
  get: () => props.pageSize,
  set: (val) => {
    emit('update:pageSize', val);
  },
});

const handleSizeChange = () => {
  emit('change');
};

const handleCurrentChange = () => {
  emit('change');
};
</script>
