<template>
  <el-container class="layout-container">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="sidebar">
      <div class="logo">{{ isCollapse ? 'AMS' : '架构管理系统 AMS' }}</div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
        background-color="#001529"
        text-color="#fff"
        active-text-color="#fff"
      >
        <el-menu-item index="/dashboard">
          <el-icon><Compass /></el-icon>
          <template #title>首页</template>
        </el-menu-item>
        <el-menu-item index="/system">
          <el-icon><Grid /></el-icon>
          <template #title>系统清单</template>
        </el-menu-item>
        <el-menu-item index="/techstack">
          <el-icon><SetUp /></el-icon>
          <template #title>技术栈管理</template>
        </el-menu-item>
        <el-menu-item index="/diagram">
          <el-icon><Picture /></el-icon>
          <template #title>逻辑部署架构图</template>
        </el-menu-item>
        <el-menu-item index="/relation">
          <el-icon><Switch /></el-icon>
          <template #title>调用关系管理</template>
        </el-menu-item>
        <el-menu-item index="/user">
          <el-icon><User /></el-icon>
          <template #title>用户权限</template>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <el-icon class="toggle-icon" @click="toggleCollapse"><Fold v-if="!isCollapse" /><Expand v-else /></el-icon>
          <breadcrumb />
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              {{ userStore.userInfo?.username || '管理员' }}
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import Breadcrumb from '@/components/Breadcrumb.vue';

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();

const isCollapse = ref(false);
const activeMenu = computed(() => route.path);

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value;
};

const handleCommand = (cmd: string) => {
  if (cmd === 'logout') {
    userStore.logout();
    router.push('/login');
  }
};
</script>

<style scoped>
.layout-container {
  height: 100vh;
}
.sidebar {
  background: #001529;
  color: #fff;
  transition: width 0.3s;
}
.logo {
  height: 56px;
  display: flex;
  align-items: center;
  padding: 0 16px;
  font-size: 16px;
  font-weight: 600;
  white-space: nowrap;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
}
.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}
.toggle-icon {
  font-size: 18px;
  cursor: pointer;
  color: #303133;
}
.user-info {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
}
.main-content {
  background: #f0f2f5;
  padding: 16px;
}
</style>
