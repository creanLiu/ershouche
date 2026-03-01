const ALL_MESSAGES = [
  {
    id: 1,
    type: 'price',
    icon: '📉',
    title: '价格变动提醒',
    content: '您关注的「2021款 宝马3系 320Li」降价了，当前价格 26.8万',
    time: '10分钟前',
    unread: true
  },
  {
    id: 2,
    type: 'recommend',
    icon: '🚗',
    title: '新车推荐',
    content: '根据您的浏览记录，为您推荐：2022款 奔驰C级 C200L，仅需 28.5万',
    time: '1小时前',
    unread: true
  },
  {
    id: 3,
    type: 'system',
    icon: '🔔',
    title: '系统通知',
    content: '您的账号已完成实名认证，现可享受更多平台服务',
    time: '昨天 14:30',
    unread: false
  },
  {
    id: 4,
    type: 'price',
    icon: '📉',
    title: '价格变动提醒',
    content: '您关注的「2020款 本田雅阁 1.5T」降价了，当前价格 14.9万',
    time: '昨天 09:15',
    unread: false
  },
  {
    id: 5,
    type: 'recommend',
    icon: '🚗',
    title: '新车推荐',
    content: '热门车型上新：2023款 丰田汉兰达 四驱版，口碑好价格优',
    time: '2天前',
    unread: false
  },
  {
    id: 6,
    type: 'system',
    icon: '🔔',
    title: '系统通知',
    content: '平台升级公告：新增车辆对比功能，欢迎体验',
    time: '3天前',
    unread: false
  }
];

const TABS = [
  { key: 'all', label: '全部' },
  { key: 'price', label: '价格变动' },
  { key: 'recommend', label: '新车推荐' },
  { key: 'system', label: '系统通知' }
];

Page({
  data: {
    tabs: TABS,
    activeTab: 'all',
    list: ALL_MESSAGES
  },

  onTabTap(e) {
    const key = e.currentTarget.dataset.key;
    const list = key === 'all'
      ? ALL_MESSAGES
      : ALL_MESSAGES.filter(m => m.type === key);
    this.setData({ activeTab: key, list });
  },

  onMsgTap(e) {
    const id = e.currentTarget.dataset.id;
    // 标记已读
    const list = this.data.list.map(m =>
      m.id === id ? { ...m, unread: false } : m
    );
    this.setData({ list });
  }
});
