const mockHistory = [
  {
    date: '今天',
    items: [
      { id: 101, title: '2022款 宝马5系 530Li 豪华套装', price: 38.5, mileage: '1.2万公里', year: '2022年', location: '北京', image: 'https://picsum.photos/200/150?random=11' },
      { id: 102, title: '2021款 奔驰E级 E300L 运动版', price: 32.8, mileage: '2.6万公里', year: '2021年', location: '上海', image: 'https://picsum.photos/200/150?random=12' },
      { id: 103, title: '2023款 丰田汉兰达 2.5L 四驱豪华', price: 26.9, mileage: '0.8万公里', year: '2023年', location: '深圳', image: 'https://picsum.photos/200/150?random=13' },
    ]
  },
  {
    date: '昨天',
    items: [
      { id: 104, title: '2020款 本田CR-V 1.5T 四驱豪华版', price: 17.5, mileage: '5.3万公里', year: '2020年', location: '广州', image: 'https://picsum.photos/200/150?random=14' },
      { id: 105, title: '2021款 大众途观L 330TSI 豪华版', price: 19.8, mileage: '3.1万公里', year: '2021年', location: '成都', image: 'https://picsum.photos/200/150?random=15' },
    ]
  },
  {
    date: '更早',
    items: [
      { id: 106, title: '2019款 奥迪A6L 45TFSI 豪华型', price: 24.5, mileage: '7.8万公里', year: '2019年', location: '杭州', image: 'https://picsum.photos/200/150?random=16' },
      { id: 107, title: '2020款 凯迪拉克CT5 28T 豪华型', price: 21.0, mileage: '4.5万公里', year: '2020年', location: '武汉', image: 'https://picsum.photos/200/150?random=17' },
    ]
  }
];

Page({
  data: {
    groups: mockHistory,
    totalCount: mockHistory.reduce((sum, g) => sum + g.items.length, 0),
  },

  onCardTap(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/vehicle-detail/index?id=${id}` });
  },

  onClearAll() {
    wx.showModal({
      title: '清空记录',
      content: '确定清空所有浏览记录？',
      confirmColor: '#EF4444',
      success: (res) => {
        if (res.confirm) {
          this.setData({ groups: [], totalCount: 0 });
          wx.showToast({ title: '已清空', icon: 'none' });
        }
      }
    });
  },
});
