<template>
  <div class="login-container">
    <el-card class="login-box">
      <h2 class="login-title">架构管理系统 AMS</h2>
      <el-form :model="form" :rules="rules" ref="formRef" @keyup.enter="handleLogin">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" :prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" :prefix-icon="Lock" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100%" :loading="loading" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { User, Lock } from '@element-plus/icons-vue';
import { login } from '@/api/auth';
import { useUserStore } from '@/stores/user';

const router = useRouter();
const userStore = useUserStore();
const loading = ref(false);
const formRef = ref();

const form = reactive({
  username: 'admin',
  password: 'admin123',
});

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
};

const handleLogin = async () => {
  try {
    await formRef.value.validate();
    loading.value = true;
    const res: any = await login(form);
    userStore.setToken(res.data.token);
    userStore.setUserInfo(res.data.user);
    ElMessage.success('登录成功');
    router.push('/dashboard');
  } catch (e: any) {
    // error handled by interceptor
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
}
.login-box {
  width: 420px;
}
.login-title {
  text-align: center;
  font-size: 24px;
  margin-bottom: 24px;
  color: #303133;
}
</style>
