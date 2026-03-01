<template>
  <div class="sidebar-container">
    <div class="logo">
      <img v-if="!collapsed" src="@/assets/logo.png" class="logo-img" alt="logo" />
      <el-icon v-else size="26" color="#a5b4fc"><Van /></el-icon>
    </div>
    <el-menu
      :default-active="activeMenu"
      :collapse="collapsed"
      background-color="transparent"
      text-color="rgba(255,255,255,0.6)"
      active-text-color="#fff"
      router
    >
      <el-menu-item
        v-for="item in filteredMenuItems"
        :key="item.path"
        :index="item.path"
      >
        <el-icon><component :is="item.icon" /></el-icon>
        <template #title>{{ item.title }}</template>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'

defineProps<{ collapsed: boolean }>()

const route = useRoute()
const userStore = useUserStore()
const activeMenu = computed(() => '/' + route.path.split('/')[1])

const allMenuItems = [
  { path: '/dashboard', title: '仪表盘', icon: 'Odometer', roles: [] as string[] },
  { path: '/vehicle', title: '车辆管理', icon: 'Van', roles: ['super_admin', 'manager'] },
  { path: '/customer', title: '客户管理', icon: 'User', roles: [] as string[] },
  { path: '/order', title: '订单管理', icon: 'Document', roles: [] as string[] },
  { path: '/statistics', title: '数据统计', icon: 'TrendCharts', roles: [] as string[] },
  { path: '/system', title: '系统管理', icon: 'Setting', roles: [] as string[] },
  { path: '/profit', title: '成本利润', icon: 'Money', roles: ['super_admin'] },
]

const filteredMenuItems = computed(() => {
  const perms = userStore.menuPermissions
  const role = userStore.role
  return allMenuItems.filter(item => {
    if (item.roles.length > 0 && !item.roles.includes(role)) return false
    if (!perms || perms.length === 0) return true
    return perms.includes(item.path)
  })
})
</script>

<style scoped>
.sidebar-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.logo {
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
  overflow: hidden;
}

.logo-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.el-menu {
  border-right: none;
  flex: 1;
  padding: 8px 0;
}

/* 菜单项基础样式 */
:deep(.el-menu-item) {
  position: relative;
  margin: 2px 10px;
  border-radius: 8px;
  height: 44px;
  line-height: 44px;
  transition: background 0.2s ease, color 0.2s ease, transform 0.2s ease;
  overflow: visible;
}

/* 左侧指示条 */
:deep(.el-menu-item::after) {
  content: '';
  position: absolute;
  left: -10px;
  top: 50%;
  transform: translateY(-50%) scaleY(0);
  width: 3px;
  height: 60%;
  background: #818cf8;
  border-radius: 0 3px 3px 0;
  transition: transform 0.2s ease;
}

/* 悬停效果 */
:deep(.el-menu-item:hover) {
  background: rgba(99, 102, 241, 0.14) !important;
  color: #fff !important;
  transform: translateX(3px);
}

:deep(.el-menu-item:hover::after) {
  transform: translateY(-50%) scaleY(1);
}

/* 激活状态 */
:deep(.el-menu-item.is-active) {
  background: rgba(99, 102, 241, 0.22) !important;
  color: #fff !important;
  font-weight: 600;
}

:deep(.el-menu-item.is-active::after) {
  transform: translateY(-50%) scaleY(1);
  background: #818cf8;
  box-shadow: 0 0 8px rgba(129, 140, 248, 0.6);
}

/* 图标颜色过渡 */
:deep(.el-menu-item .el-icon) {
  transition: color 0.2s ease;
}

:deep(.el-menu-item:hover .el-icon),
:deep(.el-menu-item.is-active .el-icon) {
  color: #a5b4fc !important;
}

/* 折叠状态下的 tooltip */
:deep(.el-menu--collapse .el-menu-item) {
  margin: 2px 6px;
}
</style>
