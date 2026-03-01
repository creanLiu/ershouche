const brands = ['大众', '丰田', '本田', '宝马', '奔驰', '奥迪', '特斯拉', '比亚迪', '吉利', '长安', '哈弗', '日产', '马自达', '福特', '其他']
const years = Array.from({ length: 11 }, (_, i) => String(2025 - i))

Page({
  data: {
    brands,
    years,
    brandIndex: 0,
    yearIndex: 0,
    form: {
      phone: '',
      brand: brands[0],
      year: years[0],
      mileage: '',
    },
  },

  onPhoneInput(e) {
    this.setData({ 'form.phone': e.detail.value })
  },

  onMileageInput(e) {
    this.setData({ 'form.mileage': e.detail.value })
  },

  onBrandChange(e) {
    const idx = e.detail.value
    this.setData({ brandIndex: idx, 'form.brand': brands[idx] })
  },

  onYearChange(e) {
    const idx = e.detail.value
    this.setData({ yearIndex: idx, 'form.year': years[idx] })
  },

  onSubmit() {
    const { phone, mileage } = this.data.form
    if (!phone || phone.length !== 11) {
      wx.showToast({ title: '请输入正确的手机号', icon: 'none' })
      return
    }
    if (!mileage) {
      wx.showToast({ title: '请输入行驶里程', icon: 'none' })
      return
    }
    wx.showToast({ title: '提交成功，稍后联系您', icon: 'success', duration: 2000 })
    setTimeout(() => wx.navigateBack(), 2000)
  },
})
