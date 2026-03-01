Page({
  data: {
    agreed: false,
    loading: false
  },

  onAgreeChange(e) {
    this.setData({ agreed: e.detail.value.length > 0 });
  },

  onGetPhoneNumber(e) {
    if (!this.data.agreed) {
      wx.showToast({ title: '请先同意用户协议', icon: 'none' });
      return;
    }
    if (e.detail.errMsg !== 'getPhoneNumber:ok') {
      wx.showToast({ title: '获取手机号失败', icon: 'none' });
      return;
    }
    this.setData({ loading: true });
    // 模拟登录成功
    setTimeout(() => {
      wx.setStorageSync('userInfo', {
        nickName: '微信用户',
        phone: '138****8888',
        avatarUrl: 'https://picsum.photos/100/100?random=1'
      });
      this.setData({ loading: false });
      wx.switchTab({ url: '/pages/profile/index' });
    }, 800);
  },

  onDevLogin() {
    wx.setStorageSync('userInfo', {
      nickName: '测试用户',
      phone: '138****8888',
      avatarUrl: 'https://picsum.photos/100/100?random=1'
    });
    wx.showToast({ title: '模拟登录成功', icon: 'success' });
    setTimeout(() => {
      const pages = getCurrentPages();
      if (pages.length > 1) {
        wx.navigateBack();
      } else {
        wx.switchTab({ url: '/pages/profile/index' });
      }
    }, 800);
  },

  onSkipLogin() {
    const pages = getCurrentPages();
    if (pages.length > 1) {
      wx.navigateBack();
    } else {
      wx.switchTab({ url: '/pages/index/index' });
    }
  }
});
