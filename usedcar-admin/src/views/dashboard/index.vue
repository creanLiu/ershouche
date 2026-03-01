<template>
  <div class="dashboard-page">
    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6" v-for="card in statCards" :key="card.label">
        <el-card class="stat-card" shadow="never">
          <div class="stat-inner">
            <div class="stat-icon" :style="{ background: card.bg }">
              <el-icon size="24" :color="card.color"><component :is="card.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ card.value }}</div>
              <div class="stat-label">{{ card.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="16">
        <el-card shadow="never">
          <template #header><span class="card-title">近30天成交趋势</span></template>
          <div ref="trendChartEl" style="height:280px" />
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="never">
          <template #header><span class="card-title">车辆状态分布</span></template>
          <div ref="pieChartEl" style="height:280px" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 最新跟进提醒 -->
    <el-card shadow="never" class="reminder-card">
      <template #header><span class="card-title">今日跟进提醒</span></template>
      <el-table :data="reminders" size="small">
        <el-table-column prop="name" label="客户" width="100" />
        <el-table-column prop="phone" label="手机号" width="140" />
        <el-table-column prop="nextFollowupAt" label="提醒时间" width="130" />
        <el-table-column prop="reminderNote" label="备注" />
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="statusType(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { customers } from '@/mock/customer'
import { mockVehicles } from '@/mock/vehicle'

const statCards = [
  { label: '在售车辆', value: mockVehicles.filter(v => v.status === 1).length, icon: 'Van', bg: '#e8f4ff', color: '#409eff' },
  { label: '本月新增客户', value: customers.filter(c => c.createdAt >= '2026-02-01').length, icon: 'User', bg: '#f0f9eb', color: '#67c23a' },
  { label: '高意向客户', value: customers.filter(c => c.status === 1).length, icon: 'Star', bg: '#fdf6ec', color: '#e6a23c' },
  { label: '本月成交', value: customers.filter(c => c.status === 3).length, icon: 'Trophy', bg: '#fef0f0', color: '#f56c6c' },
]

const reminders = customers.filter(c => c.nextFollowupAt && c.nextFollowupAt <= '2026-03-05').slice(0, 5)

function statusLabel(s: number) {
  return s === 1 ? '高意向' : s === 2 ? '已他购' : '已成交'
}
function statusType(s: number) {
  return s === 1 ? 'warning' : s === 2 ? 'info' : 'success'
}

const trendChartEl = ref<HTMLElement>()
const pieChartEl = ref<HTMLElement>()

onMounted(() => {
  if (trendChartEl.value) {
    const chart = echarts.init(trendChartEl.value)
    chart.setOption({
      tooltip: { trigger: 'axis' },
      grid: { left: 40, right: 20, top: 20, bottom: 30 },
      xAxis: { type: 'category', data: ['2/1','2/5','2/10','2/15','2/20','2/25','3/1'] },
      yAxis: { type: 'value', name: '台' },
      series: [{ name: '成交量', type: 'line', smooth: true, data: [2,3,1,4,3,5,2], areaStyle: { opacity: 0.15 }, itemStyle: { color: '#409eff' } }],
    })
  }
  if (pieChartEl.value) {
    const chart = echarts.init(pieChartEl.value)
    const onSale = mockVehicles.filter(v => v.status === 1).length
    const offSale = mockVehicles.filter(v => v.status === 0).length
    chart.setOption({
      tooltip: { trigger: 'item' },
      legend: { bottom: 0 },
      series: [{
        type: 'pie', radius: ['40%', '65%'], center: ['50%', '45%'],
        data: [
          { value: onSale, name: '在售', itemStyle: { color: '#67c23a' } },
          { value: offSale, name: '已下架', itemStyle: { color: '#c0c4cc' } },
        ],
        label: { formatter: '{b}: {c}台' },
      }],
    })
  }
})
</script>

<style scoped>
.dashboard-page { padding: 0; }
.stat-row { margin-bottom: 16px; }
.stat-card { border-radius: 8px; }
.stat-inner { display: flex; align-items: center; gap: 16px; }
.stat-icon { width: 52px; height: 52px; border-radius: 12px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.stat-value { font-size: 28px; font-weight: 700; color: #303133; line-height: 1.2; }
.stat-label { font-size: 13px; color: #909399; margin-top: 4px; }
.chart-row { margin-bottom: 16px; }
.card-title { font-weight: 600; font-size: 14px; }
.reminder-card {}
</style>
