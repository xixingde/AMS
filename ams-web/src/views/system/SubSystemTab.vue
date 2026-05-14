<template>
  <div>
    <app-search @search="loadData" @reset="handleReset">
      <el-input v-model="searchForm.subSysNo" placeholder="子系统编号" style="width: 180px" />
      <el-input v-model="searchForm.subSysNameCn" placeholder="名称" style="width: 180px" />
      <el-select v-model="searchForm.subSysLevel" placeholder="级别" style="width: 180px" clearable>
        <el-option label="核心" value="CORE" /><el-option label="重要" value="IMPORTANT" /><el-option label="一般" value="GENERAL" />
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
      <el-table-column prop="subSysNo" label="编号" />
      <el-table-column prop="sysNo" label="所属系统" />
      <el-table-column prop="subSysNameCn" label="名称" />
      <el-table-column prop="subSysLevel" label="级别" />
      <el-table-column prop="subSysStatus" label="状态" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </app-table>
    <app-pagination v-model:page="page" v-model:pageSize="pageSize" :total="total" @change="loadData" />
    <app-dialog v-model="dialogVisible" :title="isEdit ? '编辑子系统' : '新增子系统'" @confirm="handleSubmit">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="所属系统" prop="sysNo"><el-input v-model="form.sysNo" /></el-form-item>
        <el-form-item label="子系统编号" prop="subSysNo"><el-input v-model="form.subSysNo" /></el-form-item>
        <el-form-item label="中文名称" prop="subSysNameCn"><el-input v-model="form.subSysNameCn" /></el-form-item>
        <el-form-item label="级别" prop="subSysLevel">
          <el-select v-model="form.subSysLevel" style="width: 100%"><el-option label="核心" value="CORE" /><el-option label="重要" value="IMPORTANT" /><el-option label="一般" value="GENERAL" /></el-select>
        </el-form-item>
        <el-form-item label="状态" prop="subSysStatus">
          <el-select v-model="form.subSysStatus" style="width: 100%"><el-option label="已上线" value="ONLINE" /><el-option label="研发中" value="DEVELOPING" /><el-option label="规划中" value="PLANNING" /><el-option label="已下线" value="OFFLINE" /></el-select>
        </el-form-item>
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
import { getSubSystemList, createSubSystem, updateSubSystem, deleteSubSystem, importSubSystemExcel, exportSubSystemExcel, downloadSubSystemTemplate } from '@/api/system';

const loading = ref(false);
const tableData = ref([]);
const page = ref(1);
const pageSize = ref(20);
const total = ref(0);
const searchForm = reactive({ subSysNo: '', subSysNameCn: '', subSysLevel: '', sysNo: '' });
const dialogVisible = ref(false);
const isEdit = ref(false);
const formRef = ref();
const form = reactive({ id: null, sysNo: '', subSysNo: '', subSysNameCn: '', subSysNameEn: '', subSysLevel: '', subSysStatus: '', devMode: '', birthCertNo: '' });
const rules = { subSysNo: [{ required: true, message: '请输入编号', trigger: 'blur' }], subSysNameCn: [{ required: true, message: '请输入名称', trigger: 'blur' }], sysNo: [{ required: true, message: '请输入所属系统编号', trigger: 'blur' }] };
const fileInput = ref<HTMLInputElement>();

const loadData = async () => {
  loading.value = true;
  try { const res: any = await getSubSystemList({ page: page.value, pageSize: pageSize.value, ...searchForm }); tableData.value = res.data.records || res.data.list || []; total.value = res.data.total; }
  finally { loading.value = false; }
};
const handleReset = () => { Object.assign(searchForm, { subSysNo: '', subSysNameCn: '', subSysLevel: '', sysNo: '' }); page.value = 1; loadData(); };
const handleAdd = () => { isEdit.value = false; Object.assign(form, { id: null, sysNo: '', subSysNo: '', subSysNameCn: '', subSysNameEn: '', subSysLevel: '', subSysStatus: '', devMode: '', birthCertNo: '' }); dialogVisible.value = true; };
const handleEdit = (row: any) => { isEdit.value = true; Object.assign(form, row); dialogVisible.value = true; };
const handleSubmit = async () => {
  try { await formRef.value.validate(); if (isEdit.value) { await updateSubSystem(form.id as unknown as number, { ...form }); ElMessage.success('更新成功'); } else { await createSubSystem({ ...form }); ElMessage.success('创建成功'); } dialogVisible.value = false; loadData(); }
  catch (e) {}
};
const handleDelete = (row: any) => { ElMessageBox.confirm('确定删除吗？', '提示', { type: 'warning' }).then(async () => { await deleteSubSystem(row.id); ElMessage.success('删除成功'); loadData(); }).catch(() => {}); };

const handleImport = () => { fileInput.value?.click(); };
const onFileChange = async (e: Event) => {
  const target = e.target as HTMLInputElement;
  const file = target.files?.[0];
  if (!file) return;
  try {
    const res: any = await importSubSystemExcel(file);
    ElMessage.success(`导入完成，成功 ${res.data.successCount} 条，失败 ${res.data.failCount} 条`);
    loadData();
  } catch (err) {}
  target.value = '';
};
const handleExport = async () => {
  try {
    const res: any = await exportSubSystemExcel({ ...searchForm });
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = '子系统清单.xlsx';
    a.click();
    URL.revokeObjectURL(url);
  } catch (err) {}
};
const handleTemplate = async () => {
  try {
    const res: any = await downloadSubSystemTemplate();
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = '子系统清单_template.xlsx';
    a.click();
    URL.revokeObjectURL(url);
  } catch (err) {}
};

onMounted(loadData);
</script>
