<template>
  <div class="profit-page">
    <!-- 汇总卡片 -->
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6" v-for="card in summaryCards" :key="card.label">
        <el-card shadow="never" class="stat-card">
          <div class="stat-inner">
            <div class="stat-label">{{ card.label }}</div>
            <div class="stat-value" :style="{ color: card.color }">
              ¥ {{ card.value.toLocaleString() }}
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 利润图表 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="16">
        <el-card shadow="never">
          <template #header><span class="card-title">各车辆利润对比</span></template>
          <div ref="barChartEl" style="height:320px" />
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="never">
          <template #header><span class="card-title">利润率分布</span></template>
          <div ref="pieChartEl" style="height:320px" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 明细表格 -->
    <el-card shadow="never">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span class="card-title">车辆成本利润明细</span>
          <el-input v-model="search" placeholder="搜索品牌/型号" clearable style="width:200px" />
        </div>
      </template>
      <el-table :data="filteredVehicles" border stripe>
        <el-table-column prop="vehicleNo" label="编号" width="80" align="center" />
        <el-table-column prop="brand" label="品牌" width="90" />
        <el-table-column prop="model" label="型号" width="110" />
        <el-table-column prop="year" label="年份" width="70" />
        <el-table-column label="售价" width="120" align="right">
          <template #default="{ row }">¥ {{ row.price.toLocaleString() }}</template>
        </el-table-column>
        <el-table-column label="收购价" width="120" align="right">
          <template #default="{ row }">¥ {{ row.purchasePrice.toLocaleString() }}</template>
        </el-table-column>
        <el-table-column label="整备费" width="110" align="right">
          <template #default="{ row }">¥ {{ row.refurbishCost.toLocaleString() }}</template>
        </el-table-column>
        <el-table-column label="最终成本" width="120" align="right">
          <template #default="{ row }">¥ {{ row.finalCost.toLocaleString() }}</template>
        </el-table-column>
        <el-table-column label="利润" width="120" align="right">
          <template #default="{ row }">
            <span :style="profit(row) >= 0 ? 'color:#67c23a;font-weight:600' : 'color:#f56c6c;font-weight:600'">
              ¥ {{ profit(row).toLocaleString() }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="利润率" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="profitRate(row) >= 10 ? 'success' : profitRate(row) >= 5 ? 'warning' : 'danger'" size="small">
              {{ profitRate(row).toFixed(1) }}%
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
              {{ row.status === 1 ? '在售' : '已下架' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import * as echarts from 'echarts'
import { mockVehicles, type Vehicle } from '@/mock/vehicle'

const search = ref('')

const filteredVehicles = computed(() =>
  mockVehicles.filter(v =>
    !search.value || v.brand.includes(search.value) || v.model.includes(search.value)
  )
)

function profit(v: Vehicle) { return v.price - v.finalCost }
function profitRate(v: Vehicle) { return v.finalCost > 0 ? (profit(v) / v.finalCost) * 100 : 0 }

const totalRevenue = mockVehicles.reduce((s, v) => s + v.price, 0)
const totalCost = mockVehicles.reduce((s, v) => s + v.finalCost, 0)
const totalProfit = totalRevenue - totalCost
const avgProfitRate = totalCost > 0 ? (totalProfit / totalCost) * 100 : 0

const summaryCards = [
  { label: '总售价', value: totalRevenue, color: '#409eff' },
  { label: '总成本', value: totalCost, color: '#e6a23c' },
  { label: '总利润', value: totalProfit, color: '#67c23a' },
  { label: '平均利润率', value: Math.round(avgProfitRate * 10) / 10, color: '#f56c6c' },
]

const barChartEl = ref<HTMLElement>()
const pieChartEl = ref<HTMLElement>()

onMounted(() => {
  const top10 = [...mockVehicles]
    .sort((a, b) => profit(b) - profit(a))
    .slice(0, 10)

  if (barChartEl.value) {
    const chart = echarts.init(barChartEl.value)
    chart.setOption({
      tooltip: { trigger: 'axis', formatter: (p: any) => `${p[0].name}<br/>利润: ¥${p[0].value.toLocaleString()}` },
      grid: { left: 60, right: 20, top: 20, bottom: 60 },
      xAxis: {
        type: 'category',
        data: top10.map(v => `${v.brand}${v.model}(${v.vehicleNo})`),
        axisLabel: { rotate: 30, fontSize: 11 },
      },
      yAxis: { type: 'value', name: '利润(元)', axisLabel: { formatter: (v: number) => `¥${(v/1000).toFixed(0)}k` } },
      series: [{
        type: 'bar',
        data: top10.map(v => profit(v)),
        itemStyle: {
          color: (p: any) => p.value >= 20000 ? '#67c23a' : p.value >= 10000 ? '#e6a23c' : '#f56c6c',
        },
        label: { show: true, position: 'top', formatter: (p: any) => `¥${(p.value/1000).toFixed(1)}k`, fontSize: 11 },
      }],
    })
  }

  if (pieChartEl.value) {
    const high = mockVehicles.filter(v => profitRate(v) >= 10).length
    const mid = mockVehicles.filter(v => profitRate(v) >= 5 && profitRate(v) < 10).length
    const low = mockVehicles.filter(v => profitRate(v) < 5).length
    const chart = echarts.init(pieChartEl.value)
    chart.setOption({
      tooltip: { trigger: 'item' },
      legend: { bottom: 0 },
      series: [{
        type: 'pie', radius: ['38%', '62%'], center: ['50%', '44%'],
        data: [
          { value: high, name: '高利润率(≥10%)', itemStyle: { color: '#67c23a' } },
          { value: mid, name: '中利润率(5-10%)', itemStyle: { color: '#e6a23c' } },
          { value: low, name: '低利润率(<5%)', itemStyle: { color: '#f56c6c' } },
        ],
        label: { formatter: '{b}\n{c}台' },
      }],
    })
  }
})
</script>

<style scoped>
.profit-page { padding: 0; }
.stat-row { margin-bottom: 16px; }
.stat-card { border-radius: 8px; }
.stat-inner { text-align: center; padding: 8px 0; }
.stat-label { font-size: 13px; color: #909399; margin-bottom: 8px; }
.stat-value { font-size: 24px; font-weight: 700; }
.chart-row { margin-bottom: 16px; }
.card-title { font-weight: 600; font-size: 14px; }
</style>
