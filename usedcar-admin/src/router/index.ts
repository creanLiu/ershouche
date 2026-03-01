import { createRouter, createWebHistory } from 'vue-router'
import AdminLayout from '@/layouts/AdminLayout.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      component: () => import('@/views/login/index.vue'),
    },
    {
      path: '/',
      component: AdminLayout,
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'Dashboard',
          component: () => import('@/views/dashboard/index.vue'),
          meta: { title: '仪表盘', icon: 'Odometer' },
        },
        {
          path: 'vehicle',
          name: 'Vehicle',
          component: () => import('@/views/vehicle/index.vue'),
          meta: { title: '车辆管理', icon: 'Van' },
        },
        {
          path: 'customer',
          name: 'Customer',
          component: () => import('@/views/customer/index.vue'),
          meta: { title: '客户管理', icon: 'User' },
        },
        {
          path: 'order',
          name: 'Order',
          component: () => import('@/views/order/index.vue'),
          meta: { title: '订单管理', icon: 'Document' },
        },
        {
          path: 'statistics',
          name: 'Statistics',
          component: () => import('@/views/statistics/index.vue'),
          meta: { title: '数据统计', icon: 'TrendCharts' },
        },
        {
          path: 'system',
          name: 'System',
          component: () => import('@/views/system/index.vue'),
          meta: { title: '系统管理', icon: 'Setting' },
        },
        {
          path: 'profit',
          name: 'Profit',
          component: () => import('@/views/profit/index.vue'),
          meta: { title: '成本利润', icon: 'Money' },
        },
      ],
    },
  ],
})

router.beforeEach((to) => {
  const token = localStorage.getItem('token')
  if (!token && to.path !== '/login') return '/login'
  if (token && to.path === '/login') return '/dashboard'
})

export default router
