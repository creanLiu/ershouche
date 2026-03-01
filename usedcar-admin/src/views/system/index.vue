<template>
  <div class="system-page">
    <el-tabs tab-position="left" class="system-tabs">
      <!-- 用户管理 -->
      <el-tab-pane label="用户管理">
        <div class="tab-header">
          <span class="tab-title">用户管理</span>
          <el-button type="primary" size="small">新增用户</el-button>
        </div>
        <el-table :data="users" border stripe>
          <el-table-column prop="username" label="用户名" width="120" />
          <el-table-column prop="realName" label="姓名" width="100" />
          <el-table-column prop="role" label="角色" width="120" />
          <el-table-column label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status ? 'success' : 'danger'">{{ row.status ? '启用' : '禁用' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="140">
            <template #default>
              <el-button size="small">编辑</el-button>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 角色权限 -->
      <el-tab-pane label="角色权限">
        <div class="tab-header">
          <span class="tab-title">角色权限</span>
          <el-button type="primary" size="small">新增角色</el-button>
        </div>
        <el-table :data="roles" border stripe>
          <el-table-column prop="name" label="角色名" width="140" />
          <el-table-column prop="desc" label="描述" min-width="200" />
          <el-table-column label="操作" width="160">
            <template #default>
              <el-button size="small" type="primary" @click="permDialogVisible = true">权限配置</el-button>
              <el-button size="small">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-dialog v-model="permDialogVisible" title="权限配置" width="400px">
          <el-tree
            :data="permTree"
            show-checkbox
            node-key="id"
            :default-checked-keys="[1, 2, 3, 5]"
            :props="{ label: 'label', children: 'children' }"
          />
          <template #footer>
            <el-button @click="permDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="permDialogVisible = false">保存</el-button>
          </template>
        </el-dialog>
      </el-tab-pane>

      <!-- 品牌型号 -->
      <el-tab-pane label="品牌型号">
        <div class="tab-header">
          <span class="tab-title">品牌型号</span>
          <el-button type="primary" size="small">新增品牌</el-button>
        </div>
        <el-table :data="brands" border stripe>
          <el-table-column prop="name" label="品牌名" width="140" />
          <el-table-column prop="modelCount" label="车型数量" width="100" align="center" />
          <el-table-column label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status ? 'success' : 'info'">{{ row.status ? '启用' : '停用' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="140">
            <template #default>
              <el-button size="small">编辑</el-button>
              <el-button size="small">管理车型</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 操作日志 -->
      <el-tab-pane label="操作日志">
        <div class="tab-header">
          <span class="tab-title">操作日志</span>
        </div>
        <el-table :data="logs" border stripe>
          <el-table-column prop="operator" label="操作人" width="100" />
          <el-table-column prop="action" label="操作内容" min-width="200" />
          <el-table-column prop="ip" label="IP地址" width="140" />
          <el-table-column prop="time" label="时间" width="160" />
        </el-table>
      </el-tab-pane>

      <!-- 系统参数 -->
      <el-tab-pane label="系统参数">
        <div class="tab-header">
          <span class="tab-title">系统参数</span>
        </div>
        <el-form :model="sysConfig" label-width="140px" style="max-width:500px">
          <el-form-item label="系统名称">
            <el-input v-model="sysConfig.siteName" />
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="sysConfig.phone" />
          </el-form-item>
          <el-form-item label="每页显示条数">
            <el-input-number v-model="sysConfig.pageSize" :min="5" :max="100" />
          </el-form-item>
          <el-form-item label="开启水印">
            <el-switch v-model="sysConfig.watermark" />
          </el-form-item>
          <el-form-item label="图片存储路径">
            <el-input v-model="sysConfig.uploadPath" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="saveConfig">保存配置</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const users = [
  { username: 'admin', realName: '超级管理员', role: '超级管理员', status: true },
  { username: 'manager01', realName: '张经理', role: '销售经理', status: true },
  { username: 'sales01', realName: '李销售', role: '销售顾问', status: true },
  { username: 'sales02', realName: '王销售', role: '销售顾问', status: false },
  { username: 'finance01', realName: '陈财务', role: '财务专员', status: true },
]

const roles = [
  { name: '超级管理员', desc: '拥有所有权限，可管理系统所有功能' },
  { name: '销售经理', desc: '可查看所有销售数据，管理销售顾问' },
  { name: '销售顾问', desc: '可管理自己的客户和订单' },
  { name: '财务专员', desc: '可查看订单和财务数据' },
]

const permDialogVisible = ref(false)
const permTree = [
  { id: 1, label: '车辆管理', children: [
    { id: 2, label: '车辆列表' },
    { id: 3, label: '新增车辆' },
    { id: 4, label: '删除车辆' },
  ]},
  { id: 5, label: '客户管理', children: [
    { id: 6, label: '客户列表' },
    { id: 7, label: '新增跟进' },
  ]},
  { id: 8, label: '订单管理', children: [
    { id: 9, label: '订单列表' },
    { id: 10, label: '导出订单' },
  ]},
  { id: 11, label: '系统管理', children: [
    { id: 12, label: '用户管理' },
    { id: 13, label: '角色权限' },
  ]},
]

const brands = [
  { name: '大众', modelCount: 12, status: true },
  { name: '丰田', modelCount: 10, status: true },
  { name: '本田', modelCount: 8, status: true },
  { name: '宝马', modelCount: 9, status: true },
  { name: '奔驰', modelCount: 11, status: true },
  { name: '奥迪', modelCount: 7, status: false },
]

const logs = [
  { operator: 'admin', action: '新增车辆：大众途观L 2023款', ip: '192.168.1.100', time: '2026-02-24 16:30:22' },
  { operator: '李销售', action: '新增跟进记录：客户张伟', ip: '192.168.1.105', time: '2026-02-24 15:12:08' },
  { operator: '张经理', action: '导出订单数据', ip: '192.168.1.102', time: '2026-02-24 14:05:33' },
  { operator: 'admin', action: '修改系统参数', ip: '192.168.1.100', time: '2026-02-24 10:22:11' },
  { operator: '王销售', action: '更新订单状态：ORD20260201008', ip: '192.168.1.108', time: '2026-02-23 17:45:00' },
]

const sysConfig = ref({
  siteName: '二手车管理系统',
  phone: '400-888-8888',
  pageSize: 10,
  watermark: false,
  uploadPath: '/uploads/images',
})

function saveConfig() {
  ElMessage.success('配置已保存')
}
</script>

<style scoped>
.system-page { height: 100%; }
.system-tabs { min-height: 500px; }
.tab-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.tab-title { font-size: 16px; font-weight: 600; }
</style>
