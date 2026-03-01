const MOCK_ORDERS = [
  {
    id: 'ORD001',
    status: 'pending',
    statusLabel: '待确认',
    title: '2021款 丰田凯美瑞 2.5L 豪华版',
    price: 18.8,
    image: 'https://picsum.photos/200/150?random=1',
    year: '2021年',
    mileage: '3.2万公里',
    location: '北京',
    createTime: '2024-01-15 10:30'
  },
  {
    id: 'ORD002',
    status: 'ongoing',
    statusLabel: '进行中',
    title: '2020款 本田雅阁 1.5T 精英版',
    price: 15.5,
    image: 'https://picsum.photos/200/150?random=2',
    year: '2020年',
    mileage: '4.8万公里',
    location: '上海',
    createTime: '2024-01-10 14:20'
  },
  {
    id: 'ORD003',
    status: 'done',
    statusLabel: '已完成',
    title: '2019款 奔驰C级 C200L 运动版',
    price: 22.0,
    image: 'https://picsum.photos/200/150?random=4',
    year: '2019年',
    mileage: '6.1万公里',
    location: '广州',
    createTime: '2023-12-20 09:15'
  }
];

const TABS = [
  { key: 'all', label: '全部' },
  { key: 'pending', label: '待确认' },
  { key: 'ongoing', label: '进行中' },
  { key: 'done', label: '已完成' }
];

Page({
  data: {
    tabs: TABS,
    activeTab: 'all',
    list: MOCK_ORDERS
  },

  onTabTap(e) {
    const key = e.currentTarget.dataset.key;
    const list = key === 'all'
      ? MOCK_ORDERS
      : MOCK_ORDERS.filter(o => o.status === key);
    this.setData({ activeTab: key, list });
  },

  onOrderTap(e) {
    const id = e.currentTarget.dataset.id;
    wx.showToast({ title: `订单 ${id}`, icon: 'none' });
  },

  onContactSeller() {
    wx.showToast({ title: '联系卖家', icon: 'none' });
  }
});
