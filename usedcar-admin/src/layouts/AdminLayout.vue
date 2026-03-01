<template>
  <el-container class="admin-layout">
    <el-aside :width="isCollapsed ? '64px' : '220px'" class="sidebar">
      <Sidebar :collapsed="isCollapsed" />
    </el-aside>
    <el-container>
      <el-header class="header">
        <Header @toggle="isCollapsed = !isCollapsed" :collapsed="isCollapsed" />
      </el-header>
      <el-main class="main">
        <RouterView />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Sidebar from './components/Sidebar.vue'
import Header from './components/Header.vue'

const isCollapsed = ref(false)
</script>

<style scoped>
.admin-layout {
  height: 100vh;
}
.sidebar {
  background: linear-gradient(180deg, #0f172a 0%, #1e293b 100%);
  transition: width 0.28s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.18);
}
.header {
  background: #fff;
  border-bottom: 1px solid #f0f0f0;
  padding: 0;
  height: 56px;
  display: flex;
  align-items: center;
  box-shadow: 0 1px 8px rgba(0, 0, 0, 0.06);
  z-index: 10;
}
.main {
  background: #f1f5f9;
  padding: 20px;
  overflow-y: auto;
}
</style>
