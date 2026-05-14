<template>
  <div>
    <app-search @search="loadData" @reset="handleReset">
      <el-input v-model="searchForm.sysNo" placeholder="编号" style="width: 180px" />
      <el-input v-model="searchForm.sysNameCn" placeholder="名称" style="width: 180px" />
      <el-select v-model="searchForm.sysType" placeholder="全部类型" style="width: 180px" clearable>
        <el-option label="综合办公" value="COMPREHENSIVE_OFFICE" />
        <el-option label="经营管理" value="BUSINESS_MANAGEMENT" />
        <el-option label="生产运营" value="PRODUCTION_OPERATION" />
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
      <el-table-column prop="sysNo" label="编号" />
      <el-table-column prop="sysNameCn" label="名称" />
      <el-table-column prop="sysType" label="类型">
        <template #default="{ row }">
          <el-tag>{{ typeMap[row.sysType] || row.sysType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="sysOwner" label="属主" />
      <el-table-column prop="sysLevel" label="级别" />
      <el-table-column prop="protectionLevel" label="等保级别" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </app-table>
    <app-pagination v-model:page="page" v-model:pageSize="pageSize" :total="total" @change="loadData" />

    <app-dialog v-model="dialogVisible" :title="isEdit ? '编辑系统' : '新增系统'" @confirm="handleSubmit">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="系统编号" prop="sysNo"><el-input v-model="form.sysNo" /></el-form-item>
        <el-form-item label="中文名称" prop="sysNameCn"><el-input v-model="form.sysNameCn" /></el-form-item>
        <el-form-item label="英文名称" prop="sysNameEn"><el-input v-model="form.sysNameEn" /></el-form-item>
        <el-form-item label="系统类型" prop="sysType">
          <el-select v-model="form.sysType" style="width: 100%">
            <el-option label="综合办公" value="COMPREHENSIVE_OFFICE" />
            <el-option label="经营管理" value="BUSINESS_MANAGEMENT" />
            <el-option label="生产运营" value="PRODUCTION_OPERATION" />
          </el-select>
        </el-form-item>
        <el-form-item label="系统属主" prop="sysOwner"><el-input v-model="form.sysOwner" /></el-form-item>
        <el-form-item label="系统级别" prop="sysLevel">
          <el-select v-model="form.sysLevel" style="width: 100%">
            <el-option label="核心" value="CORE" />
            <el-option label="重要" value="IMPORTANT" />
            <el-option label="一般" value="GENERAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="等保级别" prop="protectionLevel">
          <el-select v-model="form.protectionLevel" style="width: 100%">
            <el-option label="一级" value="LEVEL_1" />
            <el-option label="二级" value="LEVEL_2" />
            <el-option label="三级" value="LEVEL_3" />
            <el-option label="四级" value="LEVEL_4" />
            <el-option label="五级" value="LEVEL_5" />
          </el-select>
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
import { getSystemList, createSystem, updateSystem, deleteSystem, importSystemExcel, exportSystemExcel, downloadSystemTemplate } from '@/api/system';

const loading = ref(false);
const tableData = ref([]);
const page = ref(1);
const pageSize = ref(20);
const total = ref(0);
const searchForm = reactive({ sysNo: '', sysNameCn: '', sysType: '' });
const dialogVisible = ref(false);
const isEdit = ref(false);
const formRef = ref();
const form = reactive({ id: null, sysNo: '', sysNameCn: '', sysNameEn: '', sysType: '', sysOwner: '', sysLevel: '', protectionLevel: '' });
const rules = { sysNo: [{ required: true, message: '请输入编号', trigger: 'blur' }], sysNameCn: [{ required: true, message: '请输入名称', trigger: 'blur' }] };
const typeMap: Record<string, string> = { COMPREHENSIVE_OFFICE: '综合办公', BUSINESS_MANAGEMENT: '经营管理', PRODUCTION_OPERATION: '生产运营' };
const fileInput = ref<HTMLInputElement>();

const loadData = async () => {
  loading.value = true;
  try {
    const res: any = await getSystemList({ page: page.value, pageSize: pageSize.value, ...searchForm });
    tableData.value = res.data.records || res.data.list || [];
    total.value = res.data.total;
  } finally { loading.value = false; }
};
const handleReset = () => { Object.assign(searchForm, { sysNo: '', sysNameCn: '', sysType: '' }); page.value = 1; loadData(); };
const handleAdd = () => { isEdit.value = false; Object.assign(form, { id: null, sysNo: '', sysNameCn: '', sysNameEn: '', sysType: '', sysOwner: '', sysLevel: '', protectionLevel: '' }); dialogVisible.value = true; };
const handleEdit = (row: any) => { isEdit.value = true; Object.assign(form, row); dialogVisible.value = true; };
const handleSubmit = async () => {
  try {
    await formRef.value.validate();
    if (isEdit.value) { await updateSystem(form.id as unknown as number, { ...form }); ElMessage.success('更新成功'); }
    else { await createSystem({ ...form }); ElMessage.success('创建成功'); }
    dialogVisible.value = false; loadData();
  } catch (e) {}
};
const handleDelete = (row: any) => { ElMessageBox.confirm('确定删除吗？', '提示', { type: 'warning' }).then(async () => { await deleteSystem(row.id); ElMessage.success('删除成功'); loadData(); }).catch(() => {}); };

const handleImport = () => { fileInput.value?.click(); };
const onFileChange = async (e: Event) => {
  const target = e.target as HTMLInputElement;
  const file = target.files?.[0];
  if (!file) return;
  try {
    const res: any = await importSystemExcel(file);
    ElMessage.success(`导入完成，成功 ${res.data.successCount} 条，失败 ${res.data.failCount} 条`);
    loadData();
  } catch (err) {}
  target.value = '';
};
const handleExport = async () => {
  try {
    const res: any = await exportSystemExcel({ ...searchForm });
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = '系统清单.xlsx';
    a.click();
    URL.revokeObjectURL(url);
  } catch (err) {}
};
const handleTemplate = async () => {
  try {
    const res: any = await downloadSystemTemplate();
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = '系统清单_template.xlsx';
    a.click();
    URL.revokeObjectURL(url);
  } catch (err) {}
};

onMounted(loadData);
</script>
