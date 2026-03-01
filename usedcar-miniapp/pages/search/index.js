const HOT_KEYWORDS = [
  '丰田凯美瑞', '宝马3系', '本田雅阁', '奔驰C级', '大众帕萨特',
  '特斯拉Model3', '比亚迪汉', '奥迪A4L',
];

const SUGGEST_MAP = {
  '丰田': ['丰田凯美瑞', '丰田汉兰达', '丰田RAV4'],
  '本田': ['本田雅阁', '本田CR-V', '本田思域'],
  '宝马': ['宝马3系', '宝马5系', '宝马X5'],
  '奔驰': ['奔驰C级', '奔驰E级', '奔驰GLC'],
  '大众': ['大众帕萨特', '大众途观', '大众迈腾'],
  '奥迪': ['奥迪A4L', '奥迪A6L', '奥迪Q5L'],
  '特斯拉': ['特斯拉Model3', '特斯拉ModelY', '特斯拉ModelS'],
  '比亚迪': ['比亚迪汉', '比亚迪宋', '比亚迪唐'],
};

Page({
  data: {
    keyword: '',
    historyList: [],
    hotList: HOT_KEYWORDS,
    suggestList: [],
    showSuggest: false,
  },

  onShow() {
    const history = wx.getStorageSync('searchHistory') || [];
    this.setData({ historyList: history });
  },

  onInputChange(e) {
    const keyword = e.detail.value;
    this.setData({ keyword });
    if (!keyword.trim()) {
      this.setData({ showSuggest: false, suggestList: [] });
      return;
    }
    let suggests = [];
    for (const key of Object.keys(SUGGEST_MAP)) {
      if (key.includes(keyword) || keyword.includes(key)) {
        suggests = SUGGEST_MAP[key];
        break;
      }
    }
    if (suggests.length === 0) {
      suggests = [`${keyword}轿车`, `${keyword}SUV`, `${keyword}新款`];
    }
    this.setData({ suggestList: suggests.slice(0, 4), showSuggest: true });
  },

  onSuggestTap(e) {
    const word = e.currentTarget.dataset.word;
    this.setData({ keyword: word, showSuggest: false });
    this.doSearch(word);
  },

  onTagTap(e) {
    const word = e.currentTarget.dataset.word;
    this.setData({ keyword: word, showSuggest: false });
    this.doSearch(word);
  },

  onSearch() {
    const kw = this.data.keyword.trim();
    if (!kw) {
      wx.showToast({ title: '请输入搜索关键词', icon: 'none' });
      return;
    }
    this.doSearch(kw);
  },

  doSearch(kw) {
    let history = wx.getStorageSync('searchHistory') || [];
    history = [kw, ...history.filter(h => h !== kw)].slice(0, 10);
    wx.setStorageSync('searchHistory', history);
    this.setData({ historyList: history, showSuggest: false });
    wx.navigateTo({ url: `/pages/vehicle-list/index?keyword=${encodeURIComponent(kw)}` });
  },

  onDeleteHistory(e) {
    const word = e.currentTarget.dataset.word;
    const history = this.data.historyList.filter(h => h !== word);
    wx.setStorageSync('searchHistory', history);
    this.setData({ historyList: history });
  },

  onClearHistory() {
    wx.showModal({
      title: '清空历史',
      content: '确定清空所有搜索历史？',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('searchHistory');
          this.setData({ historyList: [] });
        }
      }
    });
  },

  onClearInput() {
    this.setData({ keyword: '', suggestList: [], showSuggest: false });
  },

  onInputFocus() {
    if (this.data.keyword.trim()) {
      this.setData({ showSuggest: true });
    }
  },

  onInputBlur() {
    setTimeout(() => this.setData({ showSuggest: false }), 200);
  },
});
