<template>
  <div>
    <app-search @search="loadData" @reset="handleReset">
      <el-input v-model="searchForm.deploySysNo" placeholder="部署子系统编号" style="width: 180px" />
      <el-input v-model="searchForm.deploySysNameCn" placeholder="名称" style="width: 180px" />
      <el-input v-model="searchForm.subSysNo" placeholder="子系统编号" style="width: 180px" />
    </app-search>
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">+ 新增</el-button>
      <el-button @click="handleImport">导入</el-button>
      <el-button @click="handleExport">导出</el-button>
      <el-button @click="handleTemplate">模板下载</el-button>
      <input type="file" ref="fileInput" style="display:none" accept=".xlsx,.xls" @change="onFileChange" />
    </div>
    <app-table :data="tableData" :loading="loading">
      <el-table-column prop="deploySysNo" label="编号" />
      <el-table-column prop="subSysNo" label="所属子系统" />
      <el-table-column prop="deploySysNameCn" label="名称" />
      <el-table-column prop="deploySysDesc" label="描述" show-overflow-tooltip />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </app-table>
    <app-pagination v-model:page="page" v-model:pageSize="pageSize" :total="total" @change="loadData" />
    <app-dialog v-model="dialogVisible" :title="isEdit ? '编辑部署子系统' : '新增部署子系统'" @confirm="handleSubmit">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="所属子系统" prop="subSysNo"><el-input v-model="form.subSysNo" /></el-form-item>
        <el-form-item label="部署子系统编号" prop="deploySysNo"><el-input v-model="form.deploySysNo" /></el-form-item>
        <el-form-item label="中文名称" prop="deploySysNameCn"><el-input v-model="form.deploySysNameCn" /></el-form-item>
        <el-form-item label="描述" prop="deploySysDesc"><el-input v-model="form.deploySysDesc" type="textarea" /></el-form-item>
      </el-form>
    </app-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import AppTable from '@/components/AppTable.vue';
import AppPagination from '@/components/AppPagination.vue';
import AppSearch from '@/components/AppSearch.vue';
import AppDialog from '@/components/AppDialog.vue';
import { getDeploySubSystemList, createDeploySubSystem, updateDeploySubSystem, deleteDeploySubSystem, importDeploySubSystemExcel, exportDeploySubSystemExcel, downloadDeploySubSystemTemplate } from '@/api/system';

const loading = ref(false);
const tableData = ref([]);
const page = ref(1);
const pageSize = ref(20);
const total = ref(0);
const searchForm = reactive({ deploySysNo: '', deploySysNameCn: '', subSysNo: '' });
const dialogVisible = ref(false);
const isEdit = ref(false);
const formRef = ref();
const form = reactive({ id: null, subSysNo: '', deploySysNo: '', deploySysNameCn: '', deploySysDesc: '' });
const rules = { deploySysNo: [{ required: true, message: '请输入编号', trigger: 'blur' }], deploySysNameCn: [{ required: true, message: '请输入名称', trigger: 'blur' }], subSysNo: [{ required: true, message: '请输入所属子系统编号', trigger: 'blur' }] };
const fileInput = ref<HTMLInputElement>();

const loadData = async () => {
  loading.value = true;
  try { const res: any = await getDeploySubSystemList({ page: page.value, pageSize: pageSize.value, ...searchForm }); tableData.value = res.data.records || res.data.list || []; total.value = res.data.total; }
  finally { loading.value = false; }
};
const handleReset = () => { Object.assign(searchForm, { deploySysNo: '', deploySysNameCn: '', subSysNo: '' }); page.value = 1; loadData(); };
const handleAdd = () => { isEdit.value = false; Object.assign(form, { id: null, subSysNo: '', deploySysNo: '', deploySysNameCn: '', deploySysDesc: '' }); dialogVisible.value = true; };
const handleEdit = (row: any) => { isEdit.value = true; Object.assign(form, row); dialogVisible.value = true; };
const handleSubmit = async () => {
  try { await formRef.value.validate(); if (isEdit.value) { await updateDeploySubSystem(form.id as unknown as number, { ...form }); ElMessage.success('更新成功'); } else { await createDeploySubSystem({ ...form }); ElMessage.success('创建成功'); } dialogVisible.value = false; loadData(); }
  catch (e) {}
};
const handleDelete = (row: any) => { ElMessageBox.confirm('确定删除吗？', '提示', { type: 'warning' }).then(async () => { await deleteDeploySubSystem(row.id); ElMessage.success('删除成功'); loadData(); }).catch(() => {}); };

const handleImport = () => { fileInput.value?.click(); };
const onFileChange = async (e: Event) => {
  const target = e.target as HTMLInputElement;
  const file = target.files?.[0];
  if (!file) return;
  try {
    const res: any = await importDeploySubSystemExcel(file);
    ElMessage.success(`导入完成，成功 ${res.data.successCount} 条，失败 ${res.data.failCount} 条`);
    loadData();
  } catch (err) {}
  target.value = '';
};
const handleExport = async () => {
  try {
    const res: any = await exportDeploySubSystemExcel({ ...searchForm });
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = '部署子系统清单.xlsx';
    a.click();
    URL.revokeObjectURL(url);
  } catch (err) {}
};
const handleTemplate = async () => {
  try {
    const res: any = await downloadDeploySubSystemTemplate();
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = '部署子系统清单_template.xlsx';
    a.click();
    URL.revokeObjectURL(url);
  } catch (err) {}
};

onMounted(loadData);
</script>
