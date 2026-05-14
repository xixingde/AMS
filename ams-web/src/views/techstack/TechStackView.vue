<template>
  <div class="page-title">技术栈管理</div>
  <el-card>
    <app-search @search="loadData" @reset="handleReset">
      <el-input v-model="searchForm.stackNo" placeholder="编号" style="width: 180px" />
      <el-input v-model="searchForm.name" placeholder="名称" style="width: 180px" />
      <el-select v-model="searchForm.category" placeholder="全部分类" style="width: 180px" clearable>
        <el-option label="前端" value="前端" /><el-option label="后端" value="后端" /><el-option label="数据库" value="数据库" /><el-option label="人工智能" value="人工智能" />
      </el-select>
    </app-search>
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">+ 新增</el-button>
      <el-button @click="handleImport">导入</el-button>
      <el-button @click="handleExport">导出</el-button>
      <el-button @click="handleTemplate">模板下载</el-button>
      <input type="file" ref="fileInput" style="display:none" accept=".xlsx,.xls" @change="onFileChange" />
    </div>
    <app-table :data="tableData" :loading="loading">
      <el-table-column prop="stackNo" label="编号" />
      <el-table-column prop="category" label="分类" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="selectionAdvice" label="选型建议">
        <template #default="{ row }">
          <el-tag :type="row.selectionAdvice === 'RECOMMENDED' ? 'success' : row.selectionAdvice === 'OBSOLETE' ? 'danger' : 'info'">
            {{ adviceMap[row.selectionAdvice] || row.selectionAdvice }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="responsibleLine" label="负责条线" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </app-table>
    <app-pagination v-model:page="page" v-model:pageSize="pageSize" :total="total" @change="loadData" />
    <app-dialog v-model="dialogVisible" :title="isEdit ? '编辑技术栈' : '新增技术栈'" @confirm="handleSubmit">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="编号" prop="stackNo"><el-input v-model="form.stackNo" /></el-form-item>
        <el-form-item label="分类" prop="category"><el-input v-model="form.category" /></el-form-item>
        <el-form-item label="名称" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="选型建议" prop="selectionAdvice">
          <el-select v-model="form.selectionAdvice" style="width: 100%"><el-option label="推荐" value="RECOMMENDED" /><el-option label="有条件推荐" value="CONDITIONAL" /><el-option label="淘汰" value="OBSOLETE" /><el-option label="评估中" value="EVALUATING" /></el-select>
        </el-form-item>
        <el-form-item label="负责条线" prop="responsibleLine">
          <el-select v-model="form.responsibleLine" style="width: 100%"><el-option label="应用线" value="APPLICATION" /><el-option label="技术平台线" value="TECH_PLATFORM" /><el-option label="数据线" value="DATA" /><el-option label="基础设施线" value="INFRASTRUCTURE" /></el-select>
        </el-form-item>
      </el-form>
    </app-dialog>
  </el-card>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import AppTable from '@/components/AppTable.vue';
import AppPagination from '@/components/AppPagination.vue';
import AppSearch from '@/components/AppSearch.vue';
import AppDialog from '@/components/AppDialog.vue';
import { getTechStackList, createTechStack, updateTechStack, deleteTechStack, importTechStackExcel, exportTechStackExcel, downloadTechStackTemplate } from '@/api/techstack';

const loading = ref(false);
const tableData = ref([]);
const page = ref(1);
const pageSize = ref(20);
const total = ref(0);
const searchForm = reactive({ stackNo: '', name: '', category: '' });
const dialogVisible = ref(false);
const isEdit = ref(false);
const formRef = ref();
const form = reactive({ id: null, stackNo: '', category: '', name: '', selectionAdvice: '', responsibleLine: '' });
const rules = { stackNo: [{ required: true, message: '请输入编号', trigger: 'blur' }], name: [{ required: true, message: '请输入名称', trigger: 'blur' }] };
const adviceMap: Record<string, string> = { RECOMMENDED: '推荐', CONDITIONAL: '有条件推荐', OBSOLETE: '淘汰', EVALUATING: '评估中' };
const fileInput = ref<HTMLInputElement>();

const loadData = async () => {
  loading.value = true;
  try { const res: any = await getTechStackList({ page: page.value, pageSize: pageSize.value, ...searchForm }); tableData.value = res.data.records || res.data.list || []; total.value = res.data.total; }
  finally { loading.value = false; }
};
const handleReset = () => { Object.assign(searchForm, { stackNo: '', name: '', category: '' }); page.value = 1; loadData(); };
const handleAdd = () => { isEdit.value = false; Object.assign(form, { id: null, stackNo: '', category: '', name: '', selectionAdvice: '', responsibleLine: '' }); dialogVisible.value = true; };
const handleEdit = (row: any) => { isEdit.value = true; Object.assign(form, row); dialogVisible.value = true; };
const handleSubmit = async () => {
  try { await formRef.value.validate(); if (isEdit.value) { await updateTechStack(form.id!, { ...form }); ElMessage.success('更新成功'); } else { await createTechStack({ ...form }); ElMessage.success('创建成功'); } dialogVisible.value = false; loadData(); }
  catch (e) {}
};
const handleDelete = (row: any) => { ElMessageBox.confirm('确定删除吗？', '提示', { type: 'warning' }).then(async () => { await deleteTechStack(row.id); ElMessage.success('删除成功'); loadData(); }).catch(() => {}); };

const handleImport = () => { fileInput.value?.click(); };
const onFileChange = async (e: Event) => {
  const target = e.target as HTMLInputElement;
  const file = target.files?.[0];
  if (!file) return;
  try {
    const res: any = await importTechStackExcel(file);
    ElMessage.success(`导入完成，成功 ${res.data.successCount} 条，失败 ${res.data.failCount} 条`);
    loadData();
  } catch (err) {}
  target.value = '';
};
const handleExport = async () => {
  try {
    const res: any = await exportTechStackExcel({ ...searchForm });
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = '技术栈清单.xlsx';
    a.click();
    URL.revokeObjectURL(url);
  } catch (err) {}
};
const handleTemplate = async () => {
  try {
    const res: any = await downloadTechStackTemplate();
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = '技术栈清单_template.xlsx';
    a.click();
    URL.revokeObjectURL(url);
  } catch (err) {}
};

onMounted(loadData);
</script>

<style scoped>
.page-title { font-size: 20px; font-weight: 600; color: #303133; margin-bottom: 16px; }
.toolbar { margin-bottom: 16px; }
</style>
