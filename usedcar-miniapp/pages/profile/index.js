Page({
  data: {
    userInfo: null,
    isLoggedIn: false,
    browseCount: 7,
    favoriteCount: 5,
    orderCount: 2,
    menuList: [
      { id: 'favorite', iconPath: '/assets/icons/menu-favorite.png', title: '我的收藏', url: '/pages/favorite/index' },
      { id: 'history',  iconPath: '/assets/icons/menu-history.png',  title: '浏览历史', url: '/pages/browse-history/index' },
      { id: 'order',    iconPath: '/assets/icons/menu-order.png',    title: '我的订单', url: '/pages/order/index' },
      { id: 'message',  iconPath: '/assets/icons/menu-message.png',  title: '消息通知', url: '/pages/message/index' },
      { id: 'setting',  iconPath: '/assets/icons/menu-setting.png',  title: '设置',     url: '/pages/setting/index' }
    ]
  },

  onShow() {
    const userInfo = wx.getStorageSync('userInfo');
    this.setData({
      userInfo: userInfo || null,
      isLoggedIn: !!userInfo
    });
  },

  onMenuTap(e) {
    const { url, tab } = e.currentTarget.dataset;
    if (!this.data.isLoggedIn) {
      wx.navigateTo({ url: '/pages/login/index' });
      return;
    }
    if (tab) {
      wx.switchTab({ url });
    } else {
      wx.navigateTo({ url });
    }
  },

  onGoBrowseHistory() {
    wx.navigateTo({ url: '/pages/browse-history/index' });
  },

  onGoFavorite() {
    wx.switchTab({ url: '/pages/favorite/index' });
  },

  onGoOrder() {
    wx.navigateTo({ url: '/pages/order/index' });
  },

  onDevLogin() {
    wx.setStorageSync('userInfo', {
      nickName: '测试用户',
      phone: '138****8888',
      avatarUrl: 'https://picsum.photos/100/100?random=1'
    });
    this.setData({
      userInfo: { nickName: '测试用户', phone: '138****8888', avatarUrl: 'https://picsum.photos/100/100?random=1' },
      isLoggedIn: true
    });
    wx.showToast({ title: '模拟登录成功', icon: 'success' });
  },

  onGoLogin() {
    wx.navigateTo({ url: '/pages/login/index' });
  },

  onLogout() {
    wx.showModal({
      title: '退出登录',
      content: '确定要退出登录吗？',
      confirmColor: '#ff4d4f',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('userInfo');
          this.setData({ userInfo: null, isLoggedIn: false });
          wx.showToast({ title: '已退出登录', icon: 'none' });
        }
      }
    });
  }
});
