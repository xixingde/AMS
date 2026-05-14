<template>
  <div class="page-title">用户权限管理</div>
  <el-card>
    <app-search @search="loadData" @reset="handleReset">
      <el-input v-model="searchForm.username" placeholder="用户名" style="width: 180px" />
      <el-select v-model="searchForm.role" placeholder="全部角色" style="width: 180px" clearable>
        <el-option v-for="r in roleList" :key="r.roleCode" :label="r.roleName" :value="r.roleCode" />
      </el-select>
    </app-search>

    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">+ 新增用户</el-button>
    </div>

    <app-table :data="tableData" :loading="loading">
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="roleName" label="角色">
        <template #default="{ row }">
          <el-tag :type="row.roleCode === 'SUPER_ADMIN' ? 'danger' : row.roleCode === 'ADMIN' ? 'primary' : 'info'">
            {{ row.roleName || row.roleCode }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </app-table>

    <app-pagination v-model:page="page" v-model:pageSize="pageSize" :total="total" @change="loadData" />
  </el-card>

  <app-dialog v-model="dialogVisible" :title="isEdit ? '编辑用户' : '新增用户'" @confirm="handleSubmit">
    <el-form :model="form" :rules="formRules" ref="formRef" label-width="80px">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" :disabled="isEdit" />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="form.password" type="password" show-password :placeholder="isEdit ? '不修改请留空' : ''" />
      </el-form-item>
      <el-form-item label="角色" prop="roleId">
        <el-select v-model="form.roleId" placeholder="请选择角色" style="width: 100%">
          <el-option v-for="r in roleList" :key="r.id" :label="r.roleName" :value="r.id" />
        </el-select>
      </el-form-item>
    </el-form>
  </app-dialog>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import AppTable from '@/components/AppTable.vue';
import AppPagination from '@/components/AppPagination.vue';
import AppSearch from '@/components/AppSearch.vue';
import AppDialog from '@/components/AppDialog.vue';
import { getUserList, createUser, updateUser, deleteUser, getRoleList } from '@/api/user';

const loading = ref(false);
const tableData = ref([]);
const page = ref(1);
const pageSize = ref(20);
const total = ref(0);
const searchForm = reactive({ username: '', role: '' });
const roleList = ref<any[]>([]);

const dialogVisible = ref(false);
const isEdit = ref(false);
const formRef = ref();
const form = reactive({ id: null, username: '', password: '', roleId: null });

const formRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  roleId: [{ required: true, message: '请选择角色', trigger: 'change' }],
};

const loadData = async () => {
  loading.value = true;
  try {
    const res: any = await getUserList({ page: page.value, pageSize: pageSize.value, username: searchForm.username });
    tableData.value = res.data.records || res.data.list || [];
    total.value = res.data.total;
  } finally {
    loading.value = false;
  }
};

const handleReset = () => {
  searchForm.username = '';
  searchForm.role = '';
  page.value = 1;
  loadData();
};

const handleAdd = () => {
  isEdit.value = false;
  form.id = null;
  form.username = '';
  form.password = '';
  form.roleId = null;
  dialogVisible.value = true;
};

const handleEdit = (row: any) => {
  isEdit.value = true;
  form.id = row.id;
  form.username = row.username;
  form.password = '';
  form.roleId = row.roleId;
  dialogVisible.value = true;
};

const handleSubmit = async () => {
  try {
    await formRef.value.validate();
    if (isEdit.value) {
      await updateUser(form.id!, { roleId: form.roleId, password: form.password });
      ElMessage.success('更新成功');
    } else {
      await createUser({ username: form.username, password: form.password, roleId: form.roleId });
      ElMessage.success('创建成功');
    }
    dialogVisible.value = false;
    loadData();
  } catch (e) {}
};

const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定删除该用户吗？', '提示', { type: 'warning' }).then(async () => {
    await deleteUser(row.id);
    ElMessage.success('删除成功');
    loadData();
  }).catch(() => {});
};

onMounted(() => {
  loadData();
  getRoleList().then((res: any) => {
    roleList.value = res.data || [];
  });
});
</script>

<style scoped>
.page-title {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 16px;
}
.toolbar {
  margin-bottom: 16px;
}
</style>
