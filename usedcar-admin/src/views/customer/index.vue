<template>
  <div class="customer-page">
    <!-- 筛选区 -->
    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm">
        <el-form-item label="姓名/手机号">
          <el-input v-model="filterForm.keyword" placeholder="请输入姓名或手机号" clearable style="width:200px" />
        </el-form-item>
        <el-form-item label="来源">
          <el-select v-model="filterForm.source" placeholder="全部" clearable style="width:120px">
            <el-option v-for="s in sourceOptions" :key="s" :label="s" :value="s" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="filterForm.status" placeholder="全部" clearable style="width:120px">
            <el-option label="高意向" :value="1" />
            <el-option label="已他购" :value="2" />
            <el-option label="已成交" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="canViewAll" label="负责销售">
          <el-select v-model="filterForm.salesId" placeholder="全部" clearable style="width:120px">
            <el-option v-for="s in salesOptions" :key="s.id" :label="s.name" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card class="table-card">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span>
            共 <b>{{ filteredList.length }}</b> 位客户
            <el-tag v-if="!canViewAll" type="info" size="small" style="margin-left:8px">仅显示我的客户</el-tag>
          </span>
          <el-button type="primary" size="small" @click="openAddCustomer">新增客户</el-button>
        </div>
      </template>
      <el-table :data="pagedList" border stripe>
        <el-table-column prop="name" label="姓名" width="90" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="source" label="来源" width="90" />
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="statusType(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="salesName" label="负责销售" width="100" />
        <el-table-column prop="followupCount" label="跟进次数" width="90" align="center" />
        <el-table-column prop="lastFollowupAt" label="最后跟进" width="120" />
        <el-table-column label="下次跟进" width="120">
          <template #default="{ row }">
            <span v-if="row.nextFollowupAt" :style="isOverdue(row.nextFollowupAt) ? 'color:#f56c6c;font-weight:600' : ''">
              {{ row.nextFollowupAt }}
            </span>
            <span v-else style="color:#c0c4cc">未设置</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="160">
          <template #default="{ row }">
            <el-button size="small" @click="openDetail(row)">查看详情</el-button>
            <el-button size="small" type="primary" @click="openFollowup(row)">新增跟进</el-button>
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
    <el-drawer v-model="drawerVisible" title="客户详情" :size="520">
      <template v-if="currentCustomer">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="姓名">{{ currentCustomer.name }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ currentCustomer.phone }}</el-descriptions-item>
          <el-descriptions-item label="微信">{{ currentCustomer.wechat }}</el-descriptions-item>
          <el-descriptions-item label="来源">{{ currentCustomer.source }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="statusType(currentCustomer.status)" size="small">
              {{ statusLabel(currentCustomer.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="负责销售">{{ currentCustomer.salesName }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentCustomer.createdAt }}</el-descriptions-item>
          <el-descriptions-item label="下次跟进">
            <span v-if="currentCustomer.nextFollowupAt" :style="isOverdue(currentCustomer.nextFollowupAt) ? 'color:#f56c6c' : ''">
              {{ currentCustomer.nextFollowupAt }}
            </span>
            <span v-else style="color:#c0c4cc">未设置</span>
          </el-descriptions-item>
          <el-descriptions-item label="提醒备注" :span="2">{{ currentCustomer.reminderNote || '—' }}</el-descriptions-item>
        </el-descriptions>

        <div class="section-title">跟进记录</div>
        <el-timeline>
          <el-timeline-item timestamp="2026-02-20 14:30" placement="top">
            <el-card shadow="never">电话沟通，客户对途观L感兴趣，约下周到店看车。</el-card>
          </el-timeline-item>
          <el-timeline-item timestamp="2026-02-10 10:00" placement="top">
            <el-card shadow="never">微信发送了车辆详情图片，客户表示需要考虑。</el-card>
          </el-timeline-item>
          <el-timeline-item timestamp="2026-01-28 16:00" placement="top">
            <el-card shadow="never">首次联系，客户有购车意向，预算20万以内。</el-card>
          </el-timeline-item>
        </el-timeline>
      </template>
    </el-drawer>

    <!-- 新增客户 Dialog -->
    <el-dialog v-model="addCustomerVisible" title="新增客户" width="480px">
      <el-form :model="addForm" label-width="100px">
        <el-form-item label="姓名" required>
          <el-input v-model="addForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号" required>
          <el-input v-model="addForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="微信号">
          <el-input v-model="addForm.wechat" placeholder="请输入微信号" />
        </el-form-item>
        <el-form-item label="来源">
          <el-select v-model="addForm.source" style="width:100%">
            <el-option v-for="s in sourceOptions" :key="s" :label="s" :value="s" />
          </el-select>
        </el-form-item>
        <el-form-item label="客户状态">
          <el-radio-group v-model="addForm.status">
            <el-radio :value="1">高意向</el-radio>
            <el-radio :value="2">已他购</el-radio>
            <el-radio :value="3">已成交</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="负责销售">
          <el-select v-model="addForm.salesId" style="width:100%" @change="onSalesChange">
            <el-option v-for="s in salesOptions" :key="s.id" :label="s.name" :value="s.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addCustomerVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAddCustomer">提交</el-button>
      </template>
    </el-dialog>

    <!-- 新增跟进 Dialog -->
    <el-dialog v-model="followupVisible" title="新增跟进记录" width="500px">
      <el-form :model="followupForm" label-width="100px">
        <el-form-item label="客户状态">
          <el-radio-group v-model="followupForm.status">
            <el-radio :value="1">高意向</el-radio>
            <el-radio :value="2">已他购</el-radio>
            <el-radio :value="3">已成交</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="跟进方式">
          <el-radio-group v-model="followupForm.method">
            <el-radio value="电话">电话</el-radio>
            <el-radio value="微信">微信</el-radio>
            <el-radio value="上门">上门</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="跟进内容">
          <el-input v-model="followupForm.content" type="textarea" :rows="4" placeholder="请输入跟进内容" />
        </el-form-item>
        <el-form-item label="下次跟进">
          <el-date-picker v-model="followupForm.nextDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width:100%" />
        </el-form-item>
        <el-form-item label="提醒备注">
          <el-input v-model="followupForm.reminderNote" placeholder="如：约好下周到店看车" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="followupVisible = false">取消</el-button>
        <el-button type="primary" @click="submitFollowup">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { customers, type Customer } from '@/mock/customer'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const currentUsername = computed(() => userStore.userInfo?.username || '')
const currentRole = computed(() => userStore.role)
// super_admin 和 manager 可以看全部客户
const canViewAll = computed(() => ['super_admin', 'manager'].includes(currentRole.value))

const sourceOptions = ['线上', '转介绍', '门店', '电话']
const salesOptions = [
  { id: 'admin', name: '超级管理员' },
  { id: 'manager', name: '经理' },
  { id: 'sales', name: '销售' },
]
const today = new Date().toISOString().slice(0, 10)

function statusLabel(s: number) {
  return s === 1 ? '高意向' : s === 2 ? '已他购' : '已成交'
}
function statusType(s: number) {
  return s === 1 ? 'warning' : s === 2 ? 'info' : 'success'
}
function isOverdue(date: string) {
  return date < today
}

const filterForm = ref({ keyword: '', source: '', status: null as number | null, salesId: '' })
const currentPage = ref(1)
const pageSize = ref(10)

// 先按角色过滤，再按筛选条件过滤
const filteredList = computed(() => {
  return customers.filter(c => {
    // 销售只能看自己的客户
    if (!canViewAll.value && c.salesId !== currentUsername.value) return false
    const kw = filterForm.value.keyword
    if (kw && !c.name.includes(kw) && !c.phone.includes(kw)) return false
    if (filterForm.value.source && c.source !== filterForm.value.source) return false
    if (filterForm.value.status !== null && c.status !== filterForm.value.status) return false
    if (filterForm.value.salesId && c.salesId !== filterForm.value.salesId) return false
    return true
  })
})

const pagedList = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return filteredList.value.slice(start, start + pageSize.value)
})

function handleSearch() { currentPage.value = 1 }
function handleReset() {
  filterForm.value = { keyword: '', source: '', status: null, salesId: '' }
  currentPage.value = 1
}

const drawerVisible = ref(false)
const currentCustomer = ref<Customer | null>(null)
function openDetail(row: Customer) {
  currentCustomer.value = row
  drawerVisible.value = true
}

// 新增客户
const addCustomerVisible = ref(false)
const addForm = ref({ name: '', phone: '', wechat: '', source: '线上', status: 1 as 1|2|3, salesId: '', salesName: '' })

function openAddCustomer() {
  addForm.value = {
    name: '', phone: '', wechat: '', source: '线上', status: 1,
    salesId: currentUsername.value,
    salesName: salesOptions.find(s => s.id === currentUsername.value)?.name || currentUsername.value,
  }
  addCustomerVisible.value = true
}
function onSalesChange(val: string) {
  addForm.value.salesName = salesOptions.find(s => s.id === val)?.name || val
}
function submitAddCustomer() {
  if (!addForm.value.name || !addForm.value.phone) return ElMessage.warning('请填写姓名和手机号')
  ElMessage.success('客户已添加')
  addCustomerVisible.value = false
}

// 新增跟进
const followupVisible = ref(false)
const followupForm = ref({ status: 1 as 1|2|3, method: '电话', content: '', nextDate: '', reminderNote: '' })
function openFollowup(row: Customer) {
  currentCustomer.value = row
  followupForm.value = { status: row.status, method: '电话', content: '', nextDate: row.nextFollowupAt || '', reminderNote: row.reminderNote || '' }
  followupVisible.value = true
}
function submitFollowup() {
  ElMessage.success('跟进记录已添加')
  followupVisible.value = false
}
</script>

<style scoped>
.customer-page { padding: 0; }
.filter-card { margin-bottom: 16px; }
.pagination { margin-top: 16px; justify-content: flex-end; }
.section-title { font-weight: 600; margin: 20px 0 12px; font-size: 15px; }
</style>
