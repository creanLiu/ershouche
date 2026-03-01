<template>
  <el-dialog
    v-model="visible"
    :title="isEdit ? '编辑车辆' : '新增车辆'"
    width="700px"
    :close-on-click-modal="false"
    @closed="handleClosed"
  >
    <el-steps :active="step" finish-status="success" style="margin-bottom: 24px">
      <el-step title="基础信息" />
      <el-step title="详细参数" />
      <el-step title="图片上传" />
    </el-steps>

    <!-- Step 1 -->
    <el-form v-show="step === 0" ref="form1Ref" :model="form" :rules="rules1" label-width="90px">
      <el-form-item label="车辆编号" prop="vehicleNo">
        <el-input v-model="form.vehicleNo" placeholder="如 C001，留空则自动生成" />
      </el-form-item>
      <el-form-item label="品牌" prop="brand">
        <el-select v-model="form.brand" placeholder="请选择品牌" @change="form.model = ''" style="width:100%">
          <el-option v-for="b in brands" :key="b" :label="b" :value="b" />
        </el-select>
      </el-form-item>
      <el-form-item label="型号" prop="model">
        <el-select v-model="form.model" placeholder="请先选择品牌" style="width:100%">
          <el-option v-for="m in modelOptions" :key="m" :label="m" :value="m" />
        </el-select>
      </el-form-item>
      <el-form-item label="年份" prop="year">
        <el-select v-model="form.year" placeholder="请选择年份" style="width:100%">
          <el-option v-for="y in years" :key="y" :label="String(y)" :value="y" />
        </el-select>
      </el-form-item>
      <el-form-item label="上牌城市" prop="city">
        <el-input v-model="form.city" placeholder="请输入城市" />
      </el-form-item>
      <el-form-item label="VIN码" prop="vin">
        <el-input v-model="form.vin" placeholder="17位车辆识别码" maxlength="17" />
      </el-form-item>
      <el-form-item label="上牌日期" prop="registrationDate">
        <el-date-picker v-model="form.registrationDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width:100%" />
      </el-form-item>
    </el-form>

    <!-- Step 2 -->
    <el-form v-show="step === 1" ref="form2Ref" :model="form" :rules="rules2" label-width="90px">
      <el-form-item label="价格(元)" prop="price">
        <el-input-number v-model="form.price" :min="0" :step="1000" style="width:100%" />
      </el-form-item>
      <el-form-item label="里程(km)" prop="mileage">
        <el-input-number v-model="form.mileage" :min="0" :step="1000" style="width:100%" />
      </el-form-item>
      <el-form-item label="排量" prop="displacement">
        <el-input v-model="form.displacement" placeholder="如 2.0T / 1.5L" />
      </el-form-item>
      <el-form-item label="变速箱" prop="transmission">
        <el-radio-group v-model="form.transmission">
          <el-radio value="自动">自动</el-radio>
          <el-radio value="手动">手动</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="颜色" prop="color">
        <el-input v-model="form.color" placeholder="如 珍珠白" />
      </el-form-item>
      <el-form-item label="保险到期" prop="insuranceExpiry">
        <el-date-picker v-model="form.insuranceExpiry" type="date" value-format="YYYY-MM-DD" placeholder="选择保险到期日期" style="width:100%" />
      </el-form-item>
      <el-form-item label="特价出售">
        <el-switch v-model="form.isSpecial" active-text="是" inactive-text="否" />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input v-model="form.description" type="textarea" :rows="3" placeholder="车辆描述" />
      </el-form-item>
      <!-- 超管专属成本字段 -->
      <template v-if="isSuperAdmin">
        <el-divider content-position="left" style="margin:16px 0 12px">成本信息（仅超管可见）</el-divider>
        <el-form-item label="收购价(元)">
          <el-input-number v-model="form.purchasePrice" :min="0" :step="1000" style="width:100%" />
        </el-form-item>
        <el-form-item label="整备费(元)">
          <el-input-number v-model="form.refurbishCost" :min="0" :step="100" style="width:100%" />
        </el-form-item>
        <el-form-item label="最终成本">
          <span style="font-weight:600;color:#303133">¥ {{ finalCostAuto.toLocaleString() }}</span>
          <span style="font-size:12px;color:#909399;margin-left:8px">（收购价 + 整备费，自动计算）</span>
        </el-form-item>
      </template>
    </el-form>

    <!-- Step 3 -->
    <div v-show="step === 2">
      <el-alert type="info" :closable="false" style="margin-bottom:16px">
        点击图片下方"设为主图"可指定封面图，主图将在小程序首页展示
      </el-alert>
      <div class="image-upload-area">
        <div
          v-for="(img, idx) in uploadImages"
          :key="idx"
          class="image-item"
          :class="{ 'is-main': img.isMain }"
        >
          <el-image :src="img.url" fit="cover" class="image-preview" />
          <div class="image-main-badge" v-if="img.isMain">主图</div>
          <div class="image-actions">
            <el-button size="small" :type="img.isMain ? 'success' : 'default'" @click="setMainImage(idx)">
              {{ img.isMain ? '已是主图' : '设为主图' }}
            </el-button>
            <el-button size="small" type="danger" @click="removeImage(idx)">删除</el-button>
          </div>
        </div>
        <el-upload
          v-if="uploadImages.length < 6"
          action="#"
          :auto-upload="false"
          :show-file-list="false"
          accept="image/*"
          :on-change="handleImageAdd"
        >
          <div class="upload-placeholder">
            <el-icon size="28" color="#c0c4cc"><Plus /></el-icon>
            <span>添加图片</span>
          </div>
        </el-upload>
      </div>
      <div style="color:#909399;font-size:12px;margin-top:8px">支持 jpg/png，单张不超过 2MB，最多6张</div>
    </div>

    <template #footer>
      <el-button v-if="step > 0" @click="step--">上一步</el-button>
      <el-button v-if="step < 2" type="primary" @click="nextStep">下一步</el-button>
      <el-button v-if="step === 2" type="primary" @click="handleSubmit">提交</el-button>
      <el-button @click="visible = false">取消</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import type { FormInstance, UploadFile } from 'element-plus'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const isSuperAdmin = computed(() => userStore.role === 'super_admin')

const props = defineProps<{ modelValue: boolean; editData?: Record<string, unknown> | null }>()
const emit = defineEmits<{ (e: 'update:modelValue', v: boolean): void; (e: 'success'): void }>()

const visible = computed({
  get: () => props.modelValue,
  set: (v) => emit('update:modelValue', v),
})
const isEdit = computed(() => !!props.editData)

const step = ref(0)
const form1Ref = ref<FormInstance>()
const form2Ref = ref<FormInstance>()

interface UploadImage { url: string; isMain: boolean }
const uploadImages = ref<UploadImage[]>([])

const defaultForm = () => ({
  vehicleNo: '',
  brand: '',
  model: '',
  year: new Date().getFullYear(),
  city: '',
  vin: '',
  registrationDate: '',
  price: 0,
  mileage: 0,
  displacement: '',
  transmission: '自动' as '自动' | '手动',
  color: '',
  description: '',
  insuranceExpiry: '',
  isSpecial: false,
  purchasePrice: 0,
  refurbishCost: 0,
  finalCost: 0,
})

const finalCostAuto = computed(() => form.value.purchasePrice + form.value.refurbishCost)

const form = ref(defaultForm())

watch(() => props.editData, (data) => {
  if (data) Object.assign(form.value, data)
}, { immediate: true })

const brands = ['丰田', '本田', '大众', '宝马', '奔驰', '奥迪']
const brandModels: Record<string, string[]> = {
  丰田: ['凯美瑞', '卡罗拉', 'RAV4', '汉兰达'],
  本田: ['雅阁', '思域', 'CR-V', '奥德赛'],
  大众: ['帕萨特', '高尔夫', '途观L', '迈腾'],
  宝马: ['3系', '5系', 'X3', 'X5'],
  奔驰: ['C级', 'E级', 'GLC', 'GLE'],
  奥迪: ['A4L', 'A6L', 'Q5L', 'Q7'],
}
const modelOptions = computed(() => brandModels[form.value.brand] ?? [])
const years = Array.from({ length: 10 }, (_, i) => new Date().getFullYear() - i)

const rules1 = {
  brand: [{ required: true, message: '请选择品牌', trigger: 'change' }],
  model: [{ required: true, message: '请选择型号', trigger: 'change' }],
  year: [{ required: true, message: '请选择年份', trigger: 'change' }],
  city: [{ required: true, message: '请输入城市', trigger: 'blur' }],
  vin: [{ required: true, message: '请输入VIN码', trigger: 'blur' }, { len: 17, message: 'VIN码为17位', trigger: 'blur' }],
}
const rules2 = {
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
  mileage: [{ required: true, message: '请输入里程', trigger: 'blur' }],
  displacement: [{ required: true, message: '请输入排量', trigger: 'blur' }],
  color: [{ required: true, message: '请输入颜色', trigger: 'blur' }],
}

function setMainImage(idx: number) {
  uploadImages.value.forEach((img, i) => { img.isMain = i === idx })
}

function removeImage(idx: number) {
  const wasMain = uploadImages.value[idx].isMain
  uploadImages.value.splice(idx, 1)
  if (wasMain && uploadImages.value.length > 0) {
    uploadImages.value[0].isMain = true
  }
}

function handleImageAdd(file: UploadFile) {
  const url = URL.createObjectURL(file.raw as File)
  uploadImages.value.push({ url, isMain: uploadImages.value.length === 0 })
}

async function nextStep() {
  if (step.value === 0) {
    await form1Ref.value?.validate()
  } else if (step.value === 1) {
    await form2Ref.value?.validate()
  }
  step.value++
}

function handleSubmit() {
  emit('success')
  visible.value = false
}

function handleClosed() {
  step.value = 0
  form.value = defaultForm()
  uploadImages.value = []
  form1Ref.value?.clearValidate()
  form2Ref.value?.clearValidate()
}
</script>

<style scoped>
.image-upload-area {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}
.image-item {
  position: relative;
  width: 148px;
  border: 2px solid #e4e7ed;
  border-radius: 6px;
  overflow: hidden;
}
.image-item.is-main {
  border-color: #409eff;
}
.image-preview {
  width: 148px;
  height: 110px;
  display: block;
}
.image-main-badge {
  position: absolute;
  top: 6px;
  left: 6px;
  background: #409eff;
  color: #fff;
  font-size: 11px;
  padding: 2px 6px;
  border-radius: 4px;
}
.image-actions {
  display: flex;
  gap: 4px;
  padding: 6px;
  background: #f5f7fa;
}
.upload-placeholder {
  width: 148px;
  height: 148px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  cursor: pointer;
  color: #909399;
  font-size: 13px;
}
.upload-placeholder:hover {
  border-color: #409eff;
  color: #409eff;
}
</style>
