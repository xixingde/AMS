<template>
  <div class="page-title">逻辑部署架构图</div>
  <el-card>
    <div class="toolbar">
      <el-select v-model="selectedDeploySys" placeholder="请选择部署子系统" style="width: 280px" @change="loadDiagram">
        <el-option v-for="d in deployList" :key="d.deploySysNo" :label="d.deploySysNameCn" :value="d.deploySysNo" />
      </el-select>
      <el-button @click="loadDiagram">刷新</el-button>
      <el-button :type="editMode ? 'primary' : 'default'" @click="toggleEditMode">{{ editMode ? '退出编辑' : '编辑模式' }}</el-button>
      <el-button v-if="editMode" type="success" @click="saveLayout">保存调整</el-button>
    </div>
    <div v-if="empty" class="empty-box">
      <el-empty :description="emptyMsg" />
    </div>
    <div v-else ref="mermaidRef" class="mermaid-container" :class="{ editable: editMode }"></div>
    <div class="legend" v-if="!empty">
      <span style="display:inline-block;width:12px;height:12px;background:#90EE90;border:1px solid #333;vertical-align:middle;margin:0 4px"></span> 当前系统发布单元
      <span style="display:inline-block;width:12px;height:12px;background:#E6F7E6;border:1px solid #333;vertical-align:middle;margin:0 4px;margin-left:12px"></span> 其它系统发布单元
    </div>
    <div v-if="remark" class="remark-box">
      <el-divider />
      <div style="color:#606266;font-size:14px"><strong>备注：</strong>{{ remark }}</div>
    </div>
  </el-card>

  <el-dialog v-model="editDialogVisible" title="架构图调整" width="500px">
    <el-form label-width="80px">
      <el-form-item label="备注说明">
        <el-input v-model="editRemark" type="textarea" rows="4" placeholder="请输入架构图备注说明" />
      </el-form-item>
      <el-form-item label="节点调整">
        <div style="color:#909399;font-size:13px">提示：在编辑模式下，可拖拽节点调整位置（自动保存偏移量）</div>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="editDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="confirmEdit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue';
import { ElMessage } from 'element-plus';
import mermaid from 'mermaid';
import { getDiagram, saveLayout as apiSaveLayout, getLayout } from '@/api/diagram';
import { getDeploySubSystemList } from '@/api/system';

const selectedDeploySys = ref('');
const deployList = ref<any[]>([]);
const mermaidRef = ref<HTMLElement>();
const empty = ref(false);
const emptyMsg = ref('');
const editMode = ref(false);
const editDialogVisible = ref(false);
const editRemark = ref('');
const remark = ref('');
const nodeOffsets = ref<Record<string, { x: number; y: number }>>({});

mermaid.initialize({ startOnLoad: false, theme: 'default' });

const loadDeployList = async () => {
  const res: any = await getDeploySubSystemList({ page: 1, pageSize: 9999 });
  deployList.value = res.data.records || res.data.list || [];
  if (deployList.value.length > 0) {
    selectedDeploySys.value = deployList.value[0].deploySysNo;
    loadDiagram();
  }
};

const loadDiagram = async () => {
  if (!selectedDeploySys.value) return;
  const res: any = await getDiagram(selectedDeploySys.value);
  if (res.data.empty) {
    empty.value = true;
    emptyMsg.value = res.data.message;
    return;
  }
  empty.value = false;
  await nextTick();
  if (mermaidRef.value) {
    mermaidRef.value.innerHTML = res.data.mermaid;
    await mermaid.run({ nodes: [mermaidRef.value] });
    await loadSavedLayout();
    if (editMode.value) {
      enableDrag();
    }
  }
};

const loadSavedLayout = async () => {
  if (!selectedDeploySys.value) return;
  try {
    const res: any = await getLayout(selectedDeploySys.value);
    if (res.data.layoutData) {
      const layout = JSON.parse(res.data.layoutData);
      nodeOffsets.value = layout.nodeOffsets || {};
      applyOffsets();
    }
    remark.value = res.data.remark || '';
  } catch (e) {}
};

const applyOffsets = () => {
  if (!mermaidRef.value) return;
  const svg = mermaidRef.value.querySelector('svg');
  if (!svg) return;
  const nodes = svg.querySelectorAll<SVGGElement>('.node');
  nodes.forEach((node) => {
    const id = node.id;
    const offset = nodeOffsets.value[id];
    if (offset) {
      const currentTransform = node.getAttribute('transform') || '';
      const translateMatch = currentTransform.match(/translate\(([^,]+),\s*([^)]+)\)/);
      if (translateMatch) {
        const baseX = parseFloat(translateMatch[1]);
        const baseY = parseFloat(translateMatch[2]);
        node.setAttribute('transform', `translate(${baseX + offset.x}, ${baseY + offset.y})`);
      }
    }
  });
};

const toggleEditMode = () => {
  editMode.value = !editMode.value;
  if (editMode.value) {
    editRemark.value = remark.value;
    editDialogVisible.value = true;
    nextTick(() => enableDrag());
  } else {
    disableDrag();
    loadDiagram();
  }
};

const confirmEdit = () => {
  editDialogVisible.value = false;
  nextTick(() => enableDrag());
};

let dragNode: SVGGElement | null = null;
let dragStartX = 0;
let dragStartY = 0;
let dragOffsetX = 0;
let dragOffsetY = 0;
let dragNodeId = '';

const enableDrag = () => {
  if (!mermaidRef.value) return;
  const svg = mermaidRef.value.querySelector('svg');
  if (!svg) return;
  const nodes = svg.querySelectorAll<SVGGElement>('.node');
  nodes.forEach((node) => {
    node.style.cursor = 'move';
    node.addEventListener('mousedown', onMouseDown);
  });
  svg.addEventListener('mousemove', onMouseMove);
  svg.addEventListener('mouseup', onMouseUp);
  svg.addEventListener('mouseleave', onMouseUp);
};

const disableDrag = () => {
  if (!mermaidRef.value) return;
  const svg = mermaidRef.value.querySelector('svg');
  if (!svg) return;
  const nodes = svg.querySelectorAll<SVGGElement>('.node');
  nodes.forEach((node) => {
    node.style.cursor = '';
    node.removeEventListener('mousedown', onMouseDown);
  });
  svg.removeEventListener('mousemove', onMouseMove);
  svg.removeEventListener('mouseup', onMouseUp);
  svg.removeEventListener('mouseleave', onMouseUp);
};

const onMouseDown = (e: MouseEvent) => {
  const target = e.currentTarget as SVGGElement;
  dragNode = target;
  dragNodeId = target.id;
  dragStartX = e.clientX;
  dragStartY = e.clientY;
  const transform = target.getAttribute('transform') || '';
  const translateMatch = transform.match(/translate\(([^,]+),\s*([^)]+)\)/);
  if (translateMatch) {
    dragOffsetX = parseFloat(translateMatch[1]);
    dragOffsetY = parseFloat(translateMatch[2]);
  }
  e.preventDefault();
};

const onMouseMove = (e: MouseEvent) => {
  if (!dragNode) return;
  const dx = e.clientX - dragStartX;
  const dy = e.clientY - dragStartY;
  dragNode.setAttribute('transform', `translate(${dragOffsetX + dx}, ${dragOffsetY + dy})`);
};

const onMouseUp = () => {
  if (!dragNode || !dragNodeId) return;
  const transform = dragNode.getAttribute('transform') || '';
  const translateMatch = transform.match(/translate\(([^,]+),\s*([^)]+)\)/);
  if (translateMatch) {
    const finalX = parseFloat(translateMatch[1]);
    const finalY = parseFloat(translateMatch[2]);
    const originalOffset = nodeOffsets.value[dragNodeId] || { x: 0, y: 0 };
    nodeOffsets.value[dragNodeId] = {
      x: originalOffset.x + (finalX - dragOffsetX),
      y: originalOffset.y + (finalY - dragOffsetY),
    };
  }
  dragNode = null;
  dragNodeId = '';
};

const saveLayout = async () => {
  if (!selectedDeploySys.value) return;
  try {
    await apiSaveLayout(selectedDeploySys.value, {
      layoutData: JSON.stringify({ nodeOffsets: nodeOffsets.value }),
      remark: editRemark.value,
    });
    remark.value = editRemark.value;
    ElMessage.success('保存成功');
    editMode.value = false;
    disableDrag();
    loadDiagram();
  } catch (e) {}
};

onMounted(() => {
  loadDeployList();
});
</script>

<style scoped>
.page-title { font-size: 20px; font-weight: 600; color: #303133; margin-bottom: 16px; }
.toolbar { margin-bottom: 16px; display: flex; gap: 8px; }
.mermaid-container { background: #fafafa; border: 1px dashed #dcdfe6; border-radius: 4px; padding: 24px; min-height: 320px; }
.mermaid-container.editable { border-color: #409eff; background: #f0f9ff; }
.empty-box { background: #fafafa; border: 1px dashed #dcdfe6; border-radius: 4px; padding: 40px; }
.legend { margin-top: 16px; color: #909399; font-size: 13px; }
.remark-box { margin-top: 16px; }
</style>
