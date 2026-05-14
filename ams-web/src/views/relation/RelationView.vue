<template>
  <div class="page-title">发布单元调用关系管理</div>
  <el-card>
    <app-search @search="loadData" @reset="handleReset">
      <el-input v-model="searchForm.deploySysNo" placeholder="部署子系统编号" style="width: 180px" />
      <el-input v-model="searchForm.callerUnitNo" placeholder="调用方编号" style="width: 180px" />
      <el-input v-model="searchForm.calleeUnitNo" placeholder="被调用方编号" style="width: 180px" />
      <el-input v-model="searchForm.protocol" placeholder="协议" style="width: 180px" />
    </app-search>
    <div class="toolbar"><el-button type="primary" @click="handleAdd">+ 新增</el-button></div>
    <app-table :data="tableData" :loading="loading">
      <el-table-column prop="deploySysNo" label="部署子系统" />
      <el-table-column prop="callerUnitNo" label="调用发布单元" />
      <el-table-column prop="calleeUnitNo" label="被调用发布单元" />
      <el-table-column prop="protocol" label="协议" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </app-table>
    <app-pagination v-model:page="page" v-model:pageSize="pageSize" :total="total" @change="loadData" />
    <app-dialog v-model="dialogVisible" :title="isEdit ? '编辑调用关系' : '新增调用关系'" @confirm="handleSubmit">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="部署子系统" prop="deploySysNo"><el-input v-model="form.deploySysNo" /></el-form-item>
        <el-form-item label="调用发布单元" prop="callerUnitNo"><el-input v-model="form.callerUnitNo" /></el-form-item>
        <el-form-item label="被调用发布单元" prop="calleeUnitNo"><el-input v-model="form.calleeUnitNo" /></el-form-item>
        <el-form-item label="协议" prop="protocol"><el-input v-model="form.protocol" placeholder="如 HTTP、HTTPS、TCP" /></el-form-item>
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
import { getRelationList, createRelation, updateRelation, deleteRelation } from '@/api/relation';

const loading = ref(false);
const tableData = ref([]);
const page = ref(1);
const pageSize = ref(20);
const total = ref(0);
const searchForm = reactive({ deploySysNo: '', callerUnitNo: '', calleeUnitNo: '', protocol: '' });
const dialogVisible = ref(false);
const isEdit = ref(false);
const formRef = ref();
const form = reactive({ id: null, deploySysNo: '', callerUnitNo: '', calleeUnitNo: '', protocol: '' });
const rules = { deploySysNo: [{ required: true, message: '请输入部署子系统编号', trigger: 'blur' }], callerUnitNo: [{ required: true, message: '请输入调用方', trigger: 'blur' }], calleeUnitNo: [{ required: true, message: '请输入被调用方', trigger: 'blur' }], protocol: [{ required: true, message: '请输入协议', trigger: 'blur' }] };

const loadData = async () => {
  loading.value = true;
  try { const res: any = await getRelationList({ page: page.value, pageSize: pageSize.value, ...searchForm }); tableData.value = res.data.records || res.data.list || []; total.value = res.data.total; }
  finally { loading.value = false; }
};
const handleReset = () => { Object.assign(searchForm, { deploySysNo: '', callerUnitNo: '', calleeUnitNo: '', protocol: '' }); page.value = 1; loadData(); };
const handleAdd = () => { isEdit.value = false; Object.assign(form, { id: null, deploySysNo: '', callerUnitNo: '', calleeUnitNo: '', protocol: '' }); dialogVisible.value = true; };
const handleEdit = (row: any) => { isEdit.value = true; Object.assign(form, row); dialogVisible.value = true; };
const handleSubmit = async () => {
  try { await formRef.value.validate(); if (isEdit.value) { await updateRelation(form.id!, { ...form }); ElMessage.success('更新成功'); } else { await createRelation({ ...form }); ElMessage.success('创建成功'); } dialogVisible.value = false; loadData(); }
  catch (e) {}
};
const handleDelete = (row: any) => { ElMessageBox.confirm('确定删除吗？', '提示', { type: 'warning' }).then(async () => { await deleteRelation(row.id); ElMessage.success('删除成功'); loadData(); }).catch(() => {}); };
onMounted(loadData);
</script>

<style scoped>
.page-title { font-size: 20px; font-weight: 600; color: #303133; margin-bottom: 16px; }
.toolbar { margin-bottom: 16px; }
</style>
