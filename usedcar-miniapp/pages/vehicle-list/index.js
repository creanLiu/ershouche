const { vehicleList } = require('../../mock/vehicle')

const PAGE_SIZE = 8

function toWan(num) {
  return (num / 10000).toFixed(1)
}

Page({
  data: {
    // 筛选面板
    filterOpen: false,
    selectedBrands: [],
    brandOptions: ['大众', '丰田', '本田', '宝马', '奔驰', '奥迪', '比亚迪', '特斯拉'],
    priceRange: '',
    priceRangeOptions: [
      { label: '不限', value: '' },
      { label: '10万以下', value: '0-10' },
      { label: '10-20万', value: '10-20' },
      { label: '20-30万', value: '20-30' },
      { label: '30万以上', value: '30-999' },
    ],
    ageRange: '',
    ageRangeOptions: [
      { label: '不限', value: '' },
      { label: '1年以内', value: '0-1' },
      { label: '1-3年', value: '1-3' },
      { label: '3-5年', value: '3-5' },
      { label: '5年以上', value: '5-99' },
    ],
    mileageRange: '',
    mileageRangeOptions: [
      { label: '不限', value: '' },
      { label: '3万以内', value: '0-3' },
      { label: '3-6万', value: '3-6' },
      { label: '6-10万', value: '6-10' },
      { label: '10万以上', value: '10-999' },
    ],
    // 排序
    sortType: 'latest',
    sortOptions: [
      { label: '最新', value: 'latest' },
      { label: '价格↑', value: 'price_asc' },
      { label: '价格↓', value: 'price_desc' },
    ],
    // 列表
    list: [],
    page: 1,
    hasMore: true,
    loading: false,
    // 外部传入的价格筛选
    minPrice: null,
    maxPrice: null,
    keyword: '',
  },

  onLoad(options) {
    const update = {}
    if (options.keyword) update.keyword = decodeURIComponent(options.keyword)
    if (options.brand) update.selectedBrands = [decodeURIComponent(options.brand)]
    if (options.sort) update.sortType = options.sort
    if (options.minPrice) update.minPrice = Number(options.minPrice)
    if (options.maxPrice) update.maxPrice = Number(options.maxPrice)
    if (options.minPrice || options.maxPrice) {
      const min = options.minPrice ? Number(options.minPrice) / 10000 : 0
      const max = options.maxPrice ? Number(options.maxPrice) / 10000 : 999
      if (max <= 10) update.priceRange = '0-10'
      else if (min >= 10 && max <= 20) update.priceRange = '10-20'
      else if (min >= 20 && max <= 30) update.priceRange = '20-30'
      else if (min >= 30) update.priceRange = '30-999'
    }
    if (Object.keys(update).length > 0) {
      this.setData(update, () => this.loadList(true))
    } else {
      this.loadList(true)
    }
  },

  onPullDownRefresh() {
    this.loadList(true, () => wx.stopPullDownRefresh())
  },

  onReachBottom() {
    if (this.data.hasMore && !this.data.loading) {
      this.loadList(false)
    }
  },

  loadList(reset, cb) {
    if (this.data.loading) return
    this.setData({ loading: true })

    const { selectedBrands, priceRange, ageRange, mileageRange, sortType, minPrice, maxPrice, keyword } = this.data
    const currentYear = new Date().getFullYear()

    let filtered = vehicleList.filter(v => {
      // 关键词搜索
      if (keyword) {
        const kw = keyword.toLowerCase()
        const match = (v.brand && v.brand.includes(kw)) ||
          (v.model && v.model.includes(kw)) ||
          (v.brand && v.model && (v.brand + v.model).includes(kw))
        if (!match) return false
      }
      // 品牌筛选
      if (selectedBrands.length > 0 && !selectedBrands.includes(v.brand)) return false
      // 价格区间（外部传入优先）
      if (minPrice !== null && v.price < minPrice) return false
      if (maxPrice !== null && v.price > maxPrice) return false
      // 内部价格区间
      if (!minPrice && !maxPrice && priceRange) {
        const [pMin, pMax] = priceRange.split('-').map(Number)
        const priceW = v.price / 10000
        if (priceW < pMin || priceW > pMax) return false
      }
      // 车龄
      if (ageRange) {
        const [aMin, aMax] = ageRange.split('-').map(Number)
        const age = currentYear - v.year
        if (age < aMin || age > aMax) return false
      }
      // 里程
      if (mileageRange) {
        const [mMin, mMax] = mileageRange.split('-').map(Number)
        const mileageW = v.mileage / 10000
        if (mileageW < mMin || mileageW > mMax) return false
      }
      return true
    })

    // 排序
    if (sortType === 'price_asc') filtered.sort((a, b) => a.price - b.price)
    else if (sortType === 'price_desc') filtered.sort((a, b) => b.price - a.price)
    else filtered.sort((a, b) => Number(b.id) - Number(a.id))

    const page = reset ? 1 : this.data.page
    const start = (page - 1) * PAGE_SIZE
    const pageData = filtered.slice(start, start + PAGE_SIZE).map(v => ({
      ...v,
      priceWan: toWan(v.price),
      mileageW: toWan(v.mileage),
    }))
    const hasMore = start + PAGE_SIZE < filtered.length

    const list = reset ? pageData : [...this.data.list, ...pageData]
    this.setData({ list, page: page + 1, hasMore, loading: false })
    cb && cb()
  },

  toggleFilter() {
    this.setData({ filterOpen: !this.data.filterOpen })
  },

  onBrandTap(e) {
    const brand = e.currentTarget.dataset.brand
    let brands = [...this.data.selectedBrands]
    const idx = brands.indexOf(brand)
    if (idx >= 0) brands.splice(idx, 1)
    else brands.push(brand)
    this.setData({ selectedBrands: brands })
  },

  onPriceRangeTap(e) {
    this.setData({ priceRange: e.currentTarget.dataset.value, minPrice: null, maxPrice: null })
  },

  onAgeRangeTap(e) {
    this.setData({ ageRange: e.currentTarget.dataset.value })
  },

  onMileageRangeTap(e) {
    this.setData({ mileageRange: e.currentTarget.dataset.value })
  },

  onFilterReset() {
    this.setData({ selectedBrands: [], priceRange: '', ageRange: '', mileageRange: '', minPrice: null, maxPrice: null })
  },

  onFilterConfirm() {
    this.setData({ filterOpen: false })
    this.loadList(true)
  },

  onSortTap(e) {
    const sortType = e.currentTarget.dataset.sort
    this.setData({ sortType })
    this.loadList(true)
  },

  onVehicleTap(e) {
    const { id } = e.currentTarget.dataset
    wx.navigateTo({ url: `/pages/vehicle-detail/index?id=${id}` })
  },
})
