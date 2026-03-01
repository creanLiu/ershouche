import { defineStore } from 'pinia'
import { ref } from 'vue'
import router from '@/router'
import { mockLogin } from '@/mock/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const userInfo = ref<Record<string, any>>(JSON.parse(localStorage.getItem('userInfo') || '{}'))
  const permissions = ref<string[]>(JSON.parse(localStorage.getItem('permissions') || '[]'))
  const role = ref<string>(localStorage.getItem('role') || '')
  const menuPermissions = ref<string[]>(JSON.parse(localStorage.getItem('menuPermissions') || '[]'))

  function setToken(val: string) {
    token.value = val
    localStorage.setItem('token', val)
  }

  function clearToken() {
    token.value = ''
    localStorage.removeItem('token')
  }

  async function login(username: string, password: string) {
    const result = await mockLogin(username, password)
    setToken(result.token)
    userInfo.value = result.userInfo
    role.value = result.userInfo.role
    menuPermissions.value = result.permissions
    permissions.value = result.permissions
    localStorage.setItem('userInfo', JSON.stringify(result.userInfo))
    localStorage.setItem('role', result.userInfo.role)
    localStorage.setItem('permissions', JSON.stringify(result.permissions))
    localStorage.setItem('menuPermissions', JSON.stringify(result.permissions))
  }

  function logout() {
    clearToken()
    userInfo.value = {}
    permissions.value = []
    role.value = ''
    menuPermissions.value = []
    localStorage.removeItem('userInfo')
    localStorage.removeItem('role')
    localStorage.removeItem('permissions')
    localStorage.removeItem('menuPermissions')
    router.push('/login')
  }

  return { token, userInfo, permissions, role, menuPermissions, setToken, clearToken, login, logout }
})
