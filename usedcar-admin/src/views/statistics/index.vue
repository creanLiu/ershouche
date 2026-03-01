<template>
  <div class="statistics-page">
    <!-- 数据卡片 -->
    <el-row :gutter="16" class="stat-cards">
      <el-col :span="6" v-for="card in statCards" :key="card.label">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-value">{{ card.value }}</div>
          <div class="stat-label">{{ card.label }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="24">
        <el-card header="销售趋势（近7天）">
          <div ref="lineChartRef" style="height:300px" />
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <el-card header="品牌分布">
          <div ref="pieChartRef" style="height:300px" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card header="价格区间分布">
          <div ref="barChartRef" style="height:300px" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'

const statCards = [
  { label: '今日新增车辆', value: '12' },
  { label: '今日新增客户', value: '8' },
  { label: '本月成交订单', value: '47' },
  { label: '本月成交金额', value: '¥892万' },
]

const lineChartRef = ref<HTMLDivElement>()
const pieChartRef = ref<HTMLDivElement>()
const barChartRef = ref<HTMLDivElement>()

onMounted(() => {
  // 折线图
  const lineChart = echarts.init(lineChartRef.value!)
  lineChart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: ['02-18', '02-19', '02-20', '02-21', '02-22', '02-23', '02-24'] },
    yAxis: { type: 'value', name: '成交量（辆）' },
    series: [{
      name: '成交量',
      type: 'line',
      smooth: true,
      data: [5, 8, 6, 12, 9, 15, 11],
      areaStyle: { opacity: 0.2 },
      itemStyle: { color: '#409eff' },
    }],
  })

  // 饼图
  const pieChart = echarts.init(pieChartRef.value!)
  pieChart.setOption({
    tooltip: { trigger: 'item' },
    legend: { bottom: 0 },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      data: [
        { name: '大众', value: 28 },
        { name: '丰田', value: 22 },
        { name: '本田', value: 18 },
        { name: '宝马', value: 12 },
        { name: '奔驰', value: 10 },
        { name: '其他', value: 10 },
      ],
    }],
  })

  // 柱状图
  const barChart = echarts.init(barChartRef.value!)
  barChart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: ['10万以下', '10-15万', '15-20万', '20-30万', '30万以上'] },
    yAxis: { type: 'value', name: '数量（辆）' },
    series: [{
      type: 'bar',
      data: [15, 32, 28, 18, 7],
      itemStyle: { color: '#67c23a' },
    }],
  })

  window.addEventListener('resize', () => {
    lineChart.resize()
    pieChart.resize()
    barChart.resize()
  })
})
</script>

<style scoped>
.statistics-page {}
.stat-cards { margin-bottom: 16px; }
.stat-card { text-align: center; }
.stat-value { font-size: 28px; font-weight: 700; color: #409eff; }
.stat-label { margin-top: 8px; color: #909399; font-size: 14px; }
.chart-row { margin-bottom: 16px; }
</style>
