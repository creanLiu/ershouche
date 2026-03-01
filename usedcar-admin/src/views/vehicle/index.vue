<template>
  <div class="vehicle-page">
    <!-- 筛选栏 -->
    <el-card class="filter-card">
      <el-form :model="filter" inline>
        <el-form-item label="品牌">
          <el-select v-model="filter.brand" placeholder="全部品牌" clearable style="width:130px">
            <el-option v-for="b in brands" :key="b" :label="b" :value="b" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="filter.status" placeholder="全部状态" clearable style="width:120px">
            <el-option label="在售" :value="1" />
            <el-option label="已下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格区间">
          <el-input v-model.number="filter.priceMin" placeholder="最低价" style="width:100px" />
          <span style="margin:0 6px">-</span>
          <el-input v-model.number="filter.priceMax" placeholder="最高价" style="width:100px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作栏 -->
    <el-card class="table-card">
      <div class="toolbar">
        <div>
          <el-button type="success" :disabled="!selectedIds.length" @click="batchToggle(1)">批量上架</el-button>
          <el-button type="warning" :disabled="!selectedIds.length" @click="batchToggle(0)">批量下架</el-button>
        </div>
        <el-button type="primary" @click="openForm(null)">
          <el-icon><Plus /></el-icon> 新增车辆
        </el-button>
      </div>

      <!-- 表格 -->
      <el-table
        :data="pagedData"
        @selection-change="handleSelectionChange"
        style="width:100%"
        stripe
      >
        <el-table-column type="selection" width="50" />
        <el-table-column prop="vehicleNo" label="编号" width="80" align="center" />
        <el-table-column label="封面" width="80">
          <template #default="{ row }">
            <el-image :src="row.coverImageUrl" style="width:60px;height:45px" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column label="品牌/型号" width="130">
          <template #default="{ row }">{{ row.brand }} {{ row.model }}</template>
        </el-table-column>
        <el-table-column prop="year" label="年份" width="80" />
        <el-table-column label="价格(元)" width="120">
          <template #default="{ row }">{{ row.price.toLocaleString() }}</template>
        </el-table-column>
        <el-table-column label="里程(km)" width="110">
          <template #default="{ row }">{{ row.mileage.toLocaleString() }}</template>
        </el-table-column>
        <el-table-column prop="color" label="颜色" width="100" />
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '在售' : '已下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="保险到期" width="130">
          <template #default="{ row }">
            <span v-if="row.insuranceExpiry" :style="insuranceStyle(row.insuranceExpiry)">
              {{ row.insuranceExpiry }}
            </span>
            <el-tag v-if="isExpired(row.insuranceExpiry)" type="danger" size="small" style="margin-left:4px">已过期</el-tag>
            <el-tag v-else-if="isExpiringSoon(row.insuranceExpiry)" type="warning" size="small" style="margin-left:4px">即将到期</el-tag>
            <span v-if="!row.insuranceExpiry" style="color:#c0c4cc">未录入</span>
          </template>
        </el-table-column>
        <el-table-column label="特价" width="70" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.isSpecial" type="danger" size="small">特价</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="230" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDetail(row)">查看</el-button>
            <el-button link type="primary" @click="openForm(row)">编辑</el-button>
            <el-button link type="success" @click="openShare(row)">转发</el-button>
            <el-button link :type="row.status === 1 ? 'warning' : 'success'" @click="toggleStatus(row)">
              {{ row.status === 1 ? '下架' : '上架' }}
            </el-button>
            <el-popconfirm title="确认删除该车辆？" @confirm="deleteRow(row)">
              <template #reference>
                <el-button link type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="filteredData.length"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top:16px;justify-content:flex-end"
      />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <VehicleFormDialog
      v-model="formVisible"
      :edit-data="editData"
      @success="handleFormSuccess"
    />

    <!-- 详情抽屉 -->
    <VehicleDetailDrawer
      v-model="drawerVisible"
      :vehicle="detailVehicle"
      @edit="handleEditFromDrawer"
    />

    <!-- 快速分享弹窗 -->
    <el-dialog v-model="shareDialogVisible" title="转发朋友圈" width="420px">
      <div v-if="shareVehicle" class="share-content">
        <el-image :src="shareVehicle.coverImageUrl" style="width:100%;height:160px" fit="cover" />
        <div style="padding:12px 0 8px;font-weight:600">{{ shareVehicle.brand }} {{ shareVehicle.model }} · {{ shareVehicle.year }}年</div>
        <div style="color:#f56c6c;font-size:18px;font-weight:700;margin-bottom:4px">¥ {{ (shareVehicle.price / 10000).toFixed(1) }} 万</div>
        <el-divider />
        <div style="margin-bottom:8px;font-size:13px;color:#606266">分享链接（含当前销售信息）</div>
        <el-input :value="quickShareLink" readonly>
          <template #append>
            <el-button @click="copyQuickLink">复制</el-button>
          </template>
        </el-input>
        <div style="margin-top:10px;font-size:12px;color:#909399">客户点击链接后可直接联系您，链接有效期7天</div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { mockVehicles } from '@/mock/vehicle'
import type { Vehicle } from '@/mock/vehicle'
import VehicleFormDialog from './components/VehicleFormDialog.vue'
import VehicleDetailDrawer from './components/VehicleDetailDrawer.vue'

const brands = ['丰田', '本田', '大众', '宝马', '奔驰', '奥迪']

const today = new Date().toISOString().slice(0, 10)
const in30Days = new Date(Date.now() + 30 * 86400000).toISOString().slice(0, 10)

function isExpired(date: string) { return !!date && date < today }
function isExpiringSoon(date: string) { return !!date && date >= today && date <= in30Days }
function insuranceStyle(date: string) {
  if (isExpired(date)) return 'color:#f56c6c;font-weight:600'
  if (isExpiringSoon(date)) return 'color:#e6a23c;font-weight:600'
  return ''
}

// 数据源（本地可变副本）
const vehicles = ref<Vehicle[]>([...mockVehicles])

// 筛选
const filter = reactive({ brand: '', status: null as 0 | 1 | null, priceMin: '', priceMax: '' })
const activeFilter = reactive({ brand: '', status: null as 0 | 1 | null, priceMin: '', priceMax: '' })

function handleSearch() {
  Object.assign(activeFilter, filter)
  currentPage.value = 1
}
function handleReset() {
  Object.assign(filter, { brand: '', status: null, priceMin: '', priceMax: '' })
  Object.assign(activeFilter, { brand: '', status: null, priceMin: '', priceMax: '' })
  currentPage.value = 1
}

const filteredData = computed(() => {
  return vehicles.value.filter((v) => {
    if (activeFilter.brand && v.brand !== activeFilter.brand) return false
    if (activeFilter.status !== null && v.status !== activeFilter.status) return false
    if (activeFilter.priceMin !== '' && v.price < Number(activeFilter.priceMin)) return false
    if (activeFilter.priceMax !== '' && v.price > Number(activeFilter.priceMax)) return false
    return true
  })
})

// 分页
const currentPage = ref(1)
const pageSize = ref(10)
const pagedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return filteredData.value.slice(start, start + pageSize.value)
})

// 多选
const selectedIds = ref<number[]>([])
function handleSelectionChange(rows: Vehicle[]) {
  selectedIds.value = rows.map((r) => r.id)
}

function batchToggle(status: 0 | 1) {
  vehicles.value.forEach((v) => {
    if (selectedIds.value.includes(v.id)) v.status = status
  })
  ElMessage.success(`批量${status === 1 ? '上架' : '下架'}成功`)
}

function toggleStatus(row: Vehicle) {
  row.status = row.status === 1 ? 0 : 1
  ElMessage.success(`${row.status === 1 ? '上架' : '下架'}成功`)
}

function deleteRow(row: Vehicle) {
  const idx = vehicles.value.findIndex((v) => v.id === row.id)
  if (idx !== -1) vehicles.value.splice(idx, 1)
  ElMessage.success('删除成功')
}

// 表单弹窗
const formVisible = ref(false)
const editData = ref<Record<string, unknown> | null>(null)

function openForm(row: Vehicle | null) {
  editData.value = row ? { ...row } : null
  formVisible.value = true
}

function handleFormSuccess() {
  ElMessage.success('操作成功')
}

// 详情抽屉
const drawerVisible = ref(false)
const detailVehicle = ref<Vehicle | null>(null)

function openDetail(row: Vehicle) {
  detailVehicle.value = row
  drawerVisible.value = true
}

function handleEditFromDrawer(v: Vehicle) {
  drawerVisible.value = false
  openForm(v)
}

// 分享
const shareDialogVisible = ref(false)
const shareVehicle = ref<Vehicle | null>(null)
const quickShareLink = computed(() => {
  if (!shareVehicle.value) return ''
  return `https://usedcar.example.com/vehicle/${shareVehicle.value.id}?sales=admin`
})

function openShare(row: Vehicle) {
  shareVehicle.value = row
  shareDialogVisible.value = true
}

function copyQuickLink() {
  navigator.clipboard.writeText(quickShareLink.value).then(() => {
    ElMessage.success('链接已复制')
  })
}
</script>

<style scoped>
.vehicle-page { padding: 0; }
.filter-card { margin-bottom: 16px; }
.table-card {}
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}
</style>
