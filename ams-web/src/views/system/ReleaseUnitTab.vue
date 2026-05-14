<template>
  <div>
    <app-search @search="loadData" @reset="handleReset">
      <el-input v-model="searchForm.unitNo" placeholder="发布单元编号" style="width: 180px" />
      <el-input v-model="searchForm.unitNameCn" placeholder="名称" style="width: 180px" />
      <el-input v-model="searchForm.deploySysNo" placeholder="部署子系统编号" style="width: 180px" />
    </app-search>
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">+ 新增</el-button>
      <el-button @click="handleImport">导入</el-button>
      <el-button @click="handleExport">导出</el-button>
      <el-button @click="handleTemplate">模板下载</el-button>
      <input type="file" ref="fileInput" style="display:none" accept=".xlsx,.xls" @change="onFileChange" />
    </div>
    <app-table :data="tableData" :loading="loading">
      <el-table-column prop="unitNo" label="编号" />
      <el-table-column prop="deploySysNo" label="所属部署子系统" />
      <el-table-column prop="unitNameCn" label="名称" />
      <el-table-column prop="unitType" label="类型" />
      <el-table-column prop="networkZone" label="网络区域" />
      <el-table-column prop="deployMode" label="部署方式" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </app-table>
    <app-pagination v-model:page="page" v-model:pageSize="pageSize" :total="total" @change="loadData" />
    <app-dialog v-model="dialogVisible" :title="isEdit ? '编辑发布单元' : '新增发布单元'" @confirm="handleSubmit">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="所属部署子系统" prop="deploySysNo"><el-input v-model="form.deploySysNo" /></el-form-item>
        <el-form-item label="发布单元编号" prop="unitNo"><el-input v-model="form.unitNo" /></el-form-item>
        <el-form-item label="中文名称" prop="unitNameCn"><el-input v-model="form.unitNameCn" /></el-form-item>
        <el-form-item label="类型" prop="unitType"><el-input v-model="form.unitType" /></el-form-item>
        <el-form-item label="网络区域" prop="networkZone">
          <el-select v-model="form.networkZone" style="width: 100%"><el-option label="互联网区" value="INTERNET" /><el-option label="DMZ区" value="DMZ" /><el-option label="办公网区" value="OFFICE_NETWORK" /><el-option label="核心区" value="CORE" /><el-option label="管理区" value="MANAGEMENT" /></el-select>
        </el-form-item>
        <el-form-item label="部署方式" prop="deployMode"><el-input v-model="form.deployMode" /></el-form-item>
        <el-form-item label="技术栈" prop="stackNos">
          <el-select v-model="form.stackNos" multiple filterable style="width: 100%" placeholder="请选择技术栈">
            <el-option v-for="ts in techStackOptions" :key="ts.stackNo" :label="ts.name" :value="ts.stackNo" />
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
import { getReleaseUnitList, createReleaseUnit, updateReleaseUnit, deleteReleaseUnit, importReleaseUnitExcel, exportReleaseUnitExcel, downloadReleaseUnitTemplate } from '@/api/system';
import { getTechStackList } from '@/api/techstack';

const loading = ref(false);
const tableData = ref([]);
const page = ref(1);
const pageSize = ref(20);
const total = ref(0);
const searchForm = reactive({ unitNo: '', unitNameCn: '', deploySysNo: '', unitType: '', deployMode: '' });
const dialogVisible = ref(false);
const isEdit = ref(false);
const formRef = ref();
const form = reactive({ id: null, deploySysNo: '', unitNo: '', unitNameCn: '', unitType: '', networkZone: '', deployMode: '', stackNos: [] as string[] });
const rules = { unitNo: [{ required: true, message: '请输入编号', trigger: 'blur' }], unitNameCn: [{ required: true, message: '请输入名称', trigger: 'blur' }], deploySysNo: [{ required: true, message: '请输入所属部署子系统编号', trigger: 'blur' }] };
const fileInput = ref<HTMLInputElement>();
const techStackOptions = ref<any[]>([]);

const loadData = async () => {
  loading.value = true;
  try { const res: any = await getReleaseUnitList({ page: page.value, pageSize: pageSize.value, ...searchForm }); tableData.value = res.data.records || res.data.list || []; total.value = res.data.total; }
  finally { loading.value = false; }
};
const handleReset = () => { Object.assign(searchForm, { unitNo: '', unitNameCn: '', deploySysNo: '', unitType: '', deployMode: '' }); page.value = 1; loadData(); };
const handleAdd = () => { isEdit.value = false; Object.assign(form, { id: null, deploySysNo: '', unitNo: '', unitNameCn: '', unitType: '', networkZone: '', deployMode: '', stackNos: [] }); dialogVisible.value = true; };
const handleEdit = (row: any) => { isEdit.value = true; Object.assign(form, row); dialogVisible.value = true; };
const handleSubmit = async () => {
  try { await formRef.value.validate(); if (isEdit.value) { await updateReleaseUnit(form.id as unknown as number, { ...form }); ElMessage.success('更新成功'); } else { await createReleaseUnit({ ...form }); ElMessage.success('创建成功'); } dialogVisible.value = false; loadData(); }
  catch (e) {}
};
const handleDelete = (row: any) => { ElMessageBox.confirm('确定删除吗？', '提示', { type: 'warning' }).then(async () => { await deleteReleaseUnit(row.id); ElMessage.success('删除成功'); loadData(); }).catch(() => {}); };

const handleImport = () => { fileInput.value?.click(); };
const onFileChange = async (e: Event) => {
  const target = e.target as HTMLInputElement;
  const file = target.files?.[0];
  if (!file) return;
  try {
    const res: any = await importReleaseUnitExcel(file);
    ElMessage.success(`导入完成，成功 ${res.data.successCount} 条，失败 ${res.data.failCount} 条`);
    loadData();
  } catch (err) {}
  target.value = '';
};
const handleExport = async () => {
  try {
    const res: any = await exportReleaseUnitExcel({ ...searchForm });
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = '发布单元清单.xlsx';
    a.click();
    URL.revokeObjectURL(url);
  } catch (err) {}
};
const handleTemplate = async () => {
  try {
    const res: any = await downloadReleaseUnitTemplate();
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = '发布单元清单_template.xlsx';
    a.click();
    URL.revokeObjectURL(url);
  } catch (err) {}
};

const loadTechStacks = async () => {
  try {
    const res: any = await getTechStackList({ page: 1, pageSize: 9999 });
    techStackOptions.value = res.data.records || res.data.list || [];
  } catch (e) {}
};

onMounted(() => { loadData(); loadTechStacks(); });
</script>
