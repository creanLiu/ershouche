<template>
  <div class="header-container">
    <el-button
      text
      :icon="collapsed ? 'Expand' : 'Fold'"
      size="large"
      @click="$emit('toggle')"
      class="toggle-btn"
    />
    <div class="right-area">
      <el-dropdown>
        <span class="user-info">
          <el-avatar size="small" :icon="UserFilled" />
          <span class="username">管理员</span>
          <el-icon><ArrowDown /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup lang="ts">
import { UserFilled } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

defineProps<{ collapsed: boolean }>()
defineEmits(['toggle'])

const userStore = useUserStore()
function handleLogout() {
  userStore.logout()
}
</script>

<style scoped>
.header-container {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.toggle-btn {
  font-size: 20px;
  color: #64748b;
  transition: color 0.2s ease, transform 0.2s ease;
}

.toggle-btn:hover {
  color: #6366f1;
  transform: scale(1.1);
}

.right-area {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #374151;
  padding: 6px 12px;
  border-radius: 8px;
  transition: background 0.2s ease, color 0.2s ease;
}

.user-info:hover {
  background: #f1f5f9;
  color: #6366f1;
}

.username {
  font-size: 14px;
  font-weight: 500;
}
</style>
