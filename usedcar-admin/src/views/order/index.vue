<template>
  <div class="order-page">
    <!-- 筛选区 -->
    <el-card class="filter-card">
      <div class="filter-row">
        <el-form :inline="true" :model="filterForm">
          <el-form-item label="订单号">
            <el-input v-model="filterForm.orderNo" placeholder="请输入订单号" clearable style="width:200px" />
          </el-form-item>
          <el-form-item label="状态">
            <el-radio-group v-model="filterForm.status">
              <el-radio-button :value="0">全部</el-radio-button>
              <el-radio-button :value="1">待确认</el-radio-button>
              <el-radio-button :value="2">已确认</el-radio-button>
              <el-radio-button :value="3">已完成</el-radio-button>
              <el-radio-button :value="4">已取消</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">查询</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
        <el-button type="success" :icon="Download">导出 Excel</el-button>
      </div>
    </el-card>

    <!-- 表格 -->
    <el-card>
      <el-table :data="pagedList" border stripe>
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="customerName" label="客户" width="100" />
        <el-table-column prop="vehicleName" label="车辆" min-width="180" />
        <el-table-column label="金额" width="120" align="right">
          <template #default="{ row }">
            <span class="price">¥{{ row.price.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="salesName" label="负责销售" width="100" />
        <el-table-column prop="createdAt" label="创建时间" width="120" />
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button size="small" @click="openDetail(row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        class="pagination"
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="filteredList.length"
        layout="total, prev, pager, next"
        background
      />
    </el-card>

    <!-- 详情 Drawer -->
    <el-drawer v-model="drawerVisible" title="订单详情" :size="500">
      <template v-if="currentOrder">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号" :span="2">{{ currentOrder.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="客户">{{ currentOrder.customerName }}</el-descriptions-item>
          <el-descriptions-item label="车辆">{{ currentOrder.vehicleName }}</el-descriptions-item>
          <el-descriptions-item label="金额">¥{{ currentOrder.price.toLocaleString() }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="statusTagType(currentOrder.status)">{{ statusLabel(currentOrder.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="负责销售">{{ currentOrder.salesName }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentOrder.createdAt }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ currentOrder.updatedAt }}</el-descriptions-item>
        </el-descriptions>

        <div class="section-title">状态流转</div>
        <el-timeline>
          <el-timeline-item timestamp="2026-02-15 09:00" placement="top" type="success">
            <el-card shadow="never">订单创建，等待确认</el-card>
          </el-timeline-item>
          <el-timeline-item timestamp="2026-02-16 14:30" placement="top" type="primary">
            <el-card shadow="never">销售顾问已确认订单，安排看车</el-card>
          </el-timeline-item>
          <el-timeline-item v-if="currentOrder.status === 3" timestamp="2026-02-20 11:00" placement="top" type="warning">
            <el-card shadow="never">交易完成，客户已提车</el-card>
          </el-timeline-item>
          <el-timeline-item v-if="currentOrder.status === 4" timestamp="2026-02-18 16:00" placement="top" type="danger">
            <el-card shadow="never">订单已取消</el-card>
          </el-timeline-item>
        </el-timeline>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { Download } from '@element-plus/icons-vue'
import { orders, type Order } from '@/mock/order'

const filterForm = ref({ orderNo: '', status: 0 })
const currentPage = ref(1)
const pageSize = ref(10)

const filteredList = computed(() => {
  return orders.filter(o => {
    const matchNo = !filterForm.value.orderNo || o.orderNo.includes(filterForm.value.orderNo)
    const matchStatus = filterForm.value.status === 0 || o.status === filterForm.value.status
    return matchNo && matchStatus
  })
})

const pagedList = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return filteredList.value.slice(start, start + pageSize.value)
})

function handleSearch() { currentPage.value = 1 }
function handleReset() {
  filterForm.value = { orderNo: '', status: 0 }
  currentPage.value = 1
}

function statusLabel(status: number) {
  return { 1: '待确认', 2: '已确认', 3: '已完成', 4: '已取消' }[status] ?? '未知'
}
function statusTagType(status: number): '' | 'success' | 'warning' | 'danger' | 'info' {
  return ({ 1: 'warning', 2: '', 3: 'success', 4: 'danger' } as Record<number, '' | 'success' | 'warning' | 'danger' | 'info'>)[status] ?? 'info'
}

const drawerVisible = ref(false)
const currentOrder = ref<Order | null>(null)
function openDetail(row: Order) {
  currentOrder.value = row
  drawerVisible.value = true
}
</script>

<style scoped>
.order-page {}
.filter-card { margin-bottom: 16px; }
.filter-row { display: flex; justify-content: space-between; align-items: flex-start; }
.pagination { margin-top: 16px; justify-content: flex-end; }
.price { color: #e6a23c; font-weight: 600; }
.section-title { font-weight: 600; margin: 20px 0 12px; font-size: 15px; }
</style>
