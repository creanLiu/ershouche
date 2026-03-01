type Role = 'super_admin' | 'manager' | 'sales'

interface LoginResult {
  token: string
  userInfo: { username: string; name: string; role: Role }
  permissions: string[]
}

const roleMap: Record<string, { name: string; role: Role; permissions: string[] }> = {
  admin: {
    name: '超级管理员',
    role: 'super_admin',
    permissions: ['/dashboard', '/vehicle', '/customer', '/order', '/statistics', '/system', '/profit'],
  },
  manager: {
    name: '经理',
    role: 'manager',
    permissions: ['/dashboard', '/vehicle', '/customer', '/order', '/statistics'],
  },
  sales: {
    name: '销售',
    role: 'sales',
    permissions: ['/dashboard', '/vehicle', '/customer'],
  },
}

export function mockLogin(username: string, password: string): Promise<LoginResult> {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      const user = roleMap[username]
      if (user && password === '123456') {
        resolve({
          token: `mock-token-${username}-${Date.now()}`,
          userInfo: { username, name: user.name, role: user.role },
          permissions: user.permissions,
        })
      } else {
        reject(new Error('用户名或密码错误'))
      }
    }, 500)
  })
}
