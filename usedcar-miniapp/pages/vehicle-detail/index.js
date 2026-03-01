const { getVehicleById } = require('../../mock/vehicle')

function toWan(num) {
  return (num / 10000).toFixed(1)
}

Page({
  data: {
    vehicle: null,
    isFavorite: false,
    currentImageIndex: 0,
    descExpanded: false,
  },

  onLoad(options) {
    const { id } = options
    const v = getVehicleById(id)
    if (v) {
      const vehicle = {
        ...v,
        priceWan: toWan(v.price),
        mileageW: toWan(v.mileage),
        conditionTags: _buildTags(v),
      }
      this.setData({ vehicle })
      wx.setNavigationBarTitle({ title: `${v.brand} ${v.model}` })
    } else {
      wx.showToast({ title: '车辆不存在', icon: 'none' })
      setTimeout(() => wx.navigateBack(), 1500)
    }
  },

  onSwiperChange(e) {
    this.setData({ currentImageIndex: e.detail.current })
  },

  onImageTap(e) {
    const { index } = e.currentTarget.dataset
    const { vehicle } = this.data
    wx.previewImage({ current: vehicle.images[index], urls: vehicle.images })
  },

  onToggleDesc() {
    this.setData({ descExpanded: !this.data.descExpanded })
  },

  onToggleFavorite() {
    const isFavorite = !this.data.isFavorite
    this.setData({ isFavorite })
    wx.showToast({ title: isFavorite ? '已收藏' : '已取消收藏', icon: 'none', duration: 1200 })
  },

  onConsult() {
    const { vehicle } = this.data
    if (!vehicle || !vehicle.salesPhone) {
      wx.showToast({ title: '暂无联系方式', icon: 'none' })
      return
    }
    wx.makePhoneCall({ phoneNumber: vehicle.salesPhone })
  },

  onBookVisit() {
    wx.showToast({ title: '预约成功，稍后联系您', icon: 'success' })
  },

  onShareAppMessage() {
    const { vehicle } = this.data
    return {
      title: `${vehicle.brand} ${vehicle.model} · ${vehicle.priceWan}万元`,
      path: `/pages/vehicle-detail/index?id=${vehicle.id}`,
      imageUrl: vehicle.coverImageUrl,
    }
  },
})

function _buildTags(v) {
  const tags = []
  const mileageW = v.mileage / 10000
  if (mileageW < 3) tags.push({ label: '准新车', type: 'primary' })
  else if (mileageW < 6) tags.push({ label: '低里程', type: 'primary' })
  if (v.description && v.description.includes('无事故')) tags.push({ label: '无事故', type: 'success' })
  if (v.description && v.description.includes('一手')) tags.push({ label: '一手车', type: 'success' })
  if (v.description && v.description.includes('4S')) tags.push({ label: '4S保养', type: 'success' })
  return tags
}
