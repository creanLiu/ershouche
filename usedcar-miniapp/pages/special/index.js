const { vehicleList } = require('../../mock/vehicle')

function toWan(num) {
  return (num / 10000).toFixed(1)
}

Page({
  data: {
    list: [],
  },

  onLoad() {
    const list = vehicleList
      .filter(v => v.isSpecial)
      .map(v => ({
        ...v,
        priceWan: toWan(v.price),
        mileageW: toWan(v.mileage),
      }))
    this.setData({ list })
  },

  onVehicleTap(e) {
    const { id } = e.currentTarget.dataset
    wx.navigateTo({ url: `/pages/vehicle-detail/index?id=${id}` })
  },
})
