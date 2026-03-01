<template>
  <el-drawer v-model="visible" title="车辆详情" size="620px" direction="rtl">
    <template v-if="vehicle">
      <el-tabs>
        <el-tab-pane label="基本信息">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="品牌">{{ vehicle.brand }}</el-descriptions-item>
            <el-descriptions-item label="型号">{{ vehicle.model }}</el-descriptions-item>
            <el-descriptions-item label="年份">{{ vehicle.year }}</el-descriptions-item>
            <el-descriptions-item label="上牌城市">{{ vehicle.city }}</el-descriptions-item>
            <el-descriptions-item label="价格">{{ vehicle.price.toLocaleString() }} 元</el-descriptions-item>
            <el-descriptions-item label="里程">{{ vehicle.mileage.toLocaleString() }} km</el-descriptions-item>
            <el-descriptions-item label="颜色">{{ vehicle.color }}</el-descriptions-item>
            <el-descriptions-item label="变速箱">{{ vehicle.transmission }}</el-descriptions-item>
            <el-descriptions-item label="排量">{{ vehicle.displacement }}</el-descriptions-item>
            <el-descriptions-item label="上牌日期">{{ vehicle.registrationDate }}</el-descriptions-item>
            <el-descriptions-item label="VIN码" :span="2">{{ vehicle.vin }}</el-descriptions-item>
            <el-descriptions-item label="特价出售">
              <el-tag :type="vehicle.isSpecial ? 'danger' : 'info'" size="small">
                {{ vehicle.isSpecial ? '特价' : '普通' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="vehicle.status === 1 ? 'success' : 'info'">
                {{ vehicle.status === 1 ? '在售' : '已下架' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="描述" :span="2">{{ vehicle.description }}</el-descriptions-item>
          </el-descriptions>

          <div style="margin-top: 20px">
            <div style="font-weight: 600; margin-bottom: 12px; color: #303133">车辆图片</div>
            <div style="display: flex; gap: 10px; flex-wrap: wrap">
              <div
                v-for="(img, idx) in vehicle.images"
                :key="idx"
                style="position:relative"
              >
                <el-image
                  :src="img.url"
                  style="width: 160px; height: 120px; border-radius: 4px; border: 2px solid transparent"
                  :style="img.isMain ? 'border-color:#409eff' : ''"
                  fit="cover"
                />
                <span v-if="img.isMain" style="position:absolute;top:6px;left:6px;background:#409eff;color:#fff;font-size:11px;padding:2px 6px;border-radius:4px">主图</span>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="保险信息">
          <el-descriptions :column="1" border>
            <el-descriptions-item label="保险到期时间">
              <div style="display:flex;align-items:center;gap:12px">
                <span :style="isInsuranceExpiringSoon ? 'color:#e6a23c;font-weight:600' : ''">
                  {{ vehicle.insuranceExpiry || '未录入' }}
                </span>
                <el-tag v-if="isInsuranceExpiringSoon" type="warning" size="small">即将到期</el-tag>
                <el-tag v-else-if="isInsuranceExpired" type="danger" size="small">已过期</el-tag>
              </div>
            </el-descriptions-item>
          </el-descriptions>
          <div style="margin-top:20px">
            <div style="font-weight:600;margin-bottom:12px;color:#303133">更新保险到期时间</div>
            <div style="display:flex;gap:12px;align-items:center">
              <el-date-picker
                v-model="newInsuranceExpiry"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="选择新的到期日期"
                style="width:220px"
              />
              <el-button type="primary" @click="updateInsurance">保存</el-button>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </template>

    <template #footer>
      <el-button type="primary" @click="emit('edit', vehicle!)">编辑</el-button>
      <el-button type="success" @click="shareVisible = true">
        <el-icon style="margin-right:4px"><Share /></el-icon>转发朋友圈
      </el-button>
      <el-button @click="visible = false">关闭</el-button>
    </template>
  </el-drawer>

  <!-- 分享弹窗 -->
  <el-dialog v-model="shareVisible" title="转发朋友圈" width="420px">
    <div class="share-content" v-if="vehicle">
      <div class="share-preview">
        <el-image :src="vehicle.coverImageUrl" style="width:100%;height:180px" fit="cover" />
        <div class="share-info">
          <div class="share-title">{{ vehicle.brand }} {{ vehicle.model }} · {{ vehicle.year }}年</div>
          <div class="share-price">¥ {{ (vehicle.price / 10000).toFixed(1) }} 万</div>
          <div class="share-meta">{{ vehicle.mileage.toLocaleString() }}km · {{ vehicle.city }}</div>
        </div>
      </div>
      <el-divider />
      <div style="margin-bottom:8px;font-size:13px;color:#606266">分享链接（含当前销售信息）</div>
      <el-input :value="shareLink" readonly>
        <template #append>
          <el-button @click="copyLink">复制</el-button>
        </template>
      </el-input>
      <div style="margin-top:12px;font-size:12px;color:#909399">
        客户点击链接后可直接联系您，链接有效期7天
      </div>
    </div>
  </el-dialog>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { Vehicle } from '@/mock/vehicle'
import { useUserStore } from '@/stores/user'

const props = defineProps<{ modelValue: boolean; vehicle: Vehicle | null }>()
const emit = defineEmits<{
  (e: 'update:modelValue', v: boolean): void
  (e: 'edit', v: Vehicle): void
}>()

const visible = computed({
  get: () => props.modelValue,
  set: (v) => emit('update:modelValue', v),
})

const userStore = useUserStore()
const shareVisible = ref(false)
const newInsuranceExpiry = ref('')

const today = new Date().toISOString().slice(0, 10)
const in30Days = new Date(Date.now() + 30 * 86400000).toISOString().slice(0, 10)

const isInsuranceExpired = computed(() =>
  !!props.vehicle?.insuranceExpiry && props.vehicle.insuranceExpiry < today
)
const isInsuranceExpiringSoon = computed(() =>
  !!props.vehicle?.insuranceExpiry &&
  props.vehicle.insuranceExpiry >= today &&
  props.vehicle.insuranceExpiry <= in30Days
)

const shareLink = computed(() => {
  if (!props.vehicle) return ''
  const salesId = userStore.userInfo?.username || 'unknown'
  return `https://usedcar.example.com/vehicle/${props.vehicle.id}?sales=${salesId}`
})

function updateInsurance() {
  if (!newInsuranceExpiry.value) return ElMessage.warning('请选择日期')
  ElMessage.success(`保险到期时间已更新为 ${newInsuranceExpiry.value}`)
  newInsuranceExpiry.value = ''
}

function copyLink() {
  navigator.clipboard.writeText(shareLink.value).then(() => {
    ElMessage.success('链接已复制')
  })
}
</script>
