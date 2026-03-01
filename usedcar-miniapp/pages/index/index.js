const { vehicleList } = require('../../mock/vehicle')

function toWan(num) {
  return (num / 10000).toFixed(1)
}

Page({
  data: {
    banners: [
      { id: 1, bg: '#1E90FF', text: '精选二手车 品质有保障', url: '' },
      { id: 2, bg: '#4169E1', text: '万辆好车 等你来选', url: '' },
      { id: 3, bg: '#0080CC', text: '透明定价 放心购车', url: '' },
    ],
    brands: [
      { name: '大众',  logo: 'VW',   color: '#1565C0', bg: '#E3F0FF' },
      { name: '丰田',  logo: 'T',    color: '#EB0A1E', bg: '#FFEBEB' },
      { name: '本田',  logo: 'H',    color: '#CC0000', bg: '#FFEBEB' },
      { name: '宝马',  logo: 'BMW',  color: '#1C69D4', bg: '#E3EEFF' },
      { name: '奔驰',  logo: '✦',   color: '#1A1A1A', bg: '#F0F0F0' },
      { name: '奥迪',  logo: 'AUDI', color: '#BB0000', bg: '#FFEBEB' },
      { name: '特斯拉', logo: '⚡',  color: '#CC0000', bg: '#FFF0F0' },
      { name: '比亚迪', logo: 'BYD', color: '#1E5EAE', bg: '#E3EEFF' },
    ],
    recommendList: [],
    statusBarHeight: 0,
  },

  onLoad() {
    const { statusBarHeight } = wx.getSystemInfoSync()
    this.setData({ statusBarHeight })
    const list = vehicleList.slice(0, 6).map(v => ({
      ...v,
      priceWan: toWan(v.price),
      mileageW: toWan(v.mileage),
    }))
    this.setData({ recommendList: list })
  },

  onSearchTap() {
    wx.switchTab({ url: '/pages/search/index' })
  },

  onQuickFuncTap(e) {
    const { id } = e.currentTarget.dataset
    if (id === 'sell') {
      wx.navigateTo({ url: '/pages/sell-car/index' })
      return
    }
    let url = '/pages/vehicle-list/index'
    if (id === 'price') url += '?sort=price_asc'
    wx.navigateTo({ url })
  },

  onBannerTap(e) {
    const { url } = e.currentTarget.dataset
    if (url) wx.navigateTo({ url })
  },

  onBrandTap(e) {
    const { brand } = e.currentTarget.dataset
    wx.navigateTo({ url: `/pages/vehicle-list/index?brand=${brand}` })
  },

  onVehicleTap(e) {
    const { id } = e.currentTarget.dataset
    wx.navigateTo({ url: `/pages/vehicle-detail/index?id=${id}` })
  },

  onViewAllTap() {
    wx.navigateTo({ url: '/pages/vehicle-list/index' })
  },
})
