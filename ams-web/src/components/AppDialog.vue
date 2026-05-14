<template>
  <el-dialog
    v-model="visible"
    :title="title"
    width="560px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <slot />
    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps<{
  modelValue: boolean;
  title: string;
}>();

const emit = defineEmits(['update:modelValue', 'confirm', 'close']);

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val),
});

const handleClose = () => {
  emit('update:modelValue', false);
  emit('close');
};

const handleConfirm = () => {
  emit('confirm');
};
</script>
