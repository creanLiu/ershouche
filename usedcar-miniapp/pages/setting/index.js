Page({
  data: {},

  onClearCache() {
    wx.clearStorageSync()
    wx.showToast({ title: '缓存已清除', icon: 'success' })
  }
})
