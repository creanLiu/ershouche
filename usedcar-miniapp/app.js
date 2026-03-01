const { getToken, clearToken } = require('./utils/storage')

App({
  globalData: {
    userInfo: null,
    token: null,
  },

  onLaunch() {
    this._checkToken()
  },

  _checkToken() {
    const token = getToken()
    if (token) {
      this.globalData.token = token
    }
  },

  setToken(token) {
    this.globalData.token = token
  },

  clearToken() {
    clearToken()
    this.globalData.token = null
    this.globalData.userInfo = null
  },
})
