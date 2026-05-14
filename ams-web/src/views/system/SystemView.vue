<template>
  <div class="page-title">系统清单管理</div>
  <el-card>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="系统" name="system">
        <SystemTab />
      </el-tab-pane>
      <el-tab-pane label="子系统" name="subsystem">
        <SubSystemTab />
      </el-tab-pane>
      <el-tab-pane label="部署子系统" name="deploy">
        <DeploySubSystemTab />
      </el-tab-pane>
      <el-tab-pane label="发布单元" name="release">
        <ReleaseUnitTab />
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import SystemTab from './SystemTab.vue';
import SubSystemTab from './SubSystemTab.vue';
import DeploySubSystemTab from './DeploySubSystemTab.vue';
import ReleaseUnitTab from './ReleaseUnitTab.vue';

const activeTab = ref('system');
</script>

<style scoped>
.page-title {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 16px;
}
</style>
