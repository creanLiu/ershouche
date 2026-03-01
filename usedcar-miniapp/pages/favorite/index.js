const mockFavorites = [
  {
    id: 1,
    title: '2021款 丰田凯美瑞 2.5L 豪华版',
    price: 18.8,
    mileage: '3.2万公里',
    year: '2021年',
    location: '北京',
    image: 'https://picsum.photos/200/150?random=1'
  },
  {
    id: 2,
    title: '2020款 本田雅阁 1.5T 精英版',
    price: 15.5,
    mileage: '4.8万公里',
    year: '2020年',
    location: '上海',
    image: 'https://picsum.photos/200/150?random=2'
  },
  {
    id: 3,
    title: '2022款 宝马3系 320Li 豪华套装',
    price: 28.9,
    mileage: '1.5万公里',
    year: '2022年',
    location: '深圳',
    image: 'https://picsum.photos/200/150?random=3'
  },
  {
    id: 4,
    title: '2019款 奔驰C级 C200L 运动版',
    price: 22.0,
    mileage: '6.1万公里',
    year: '2019年',
    location: '广州',
    image: 'https://picsum.photos/200/150?random=4'
  },
  {
    id: 5,
    title: '2021款 大众帕萨特 330TSI 商务版',
    price: 16.8,
    mileage: '2.9万公里',
    year: '2021年',
    location: '成都',
    image: 'https://picsum.photos/200/150?random=5'
  }
];

Page({
  data: {
    list: []
  },

  onLoad() {
    this.setData({ list: mockFavorites });
  },

  onDeleteTap(e) {
    const id = e.currentTarget.dataset.id;
    wx.showModal({
      title: '取消收藏',
      content: '确定要取消收藏该车辆吗？',
      confirmColor: '#ff4d4f',
      success: (res) => {
        if (res.confirm) {
          const list = this.data.list.filter(item => item.id !== id);
          this.setData({ list });
          wx.showToast({ title: '已取消收藏', icon: 'none' });
        }
      }
    });
  },

  onCardTap(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/vehicle-detail/index?id=${id}` });
  },

  onGoBrowse() {
    wx.navigateTo({ url: '/pages/vehicle-list/index' });
  },
});
