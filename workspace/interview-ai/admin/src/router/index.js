import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/views/Layout.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '控制台' }
      },
      {
        path: 'users',
        name: 'UserManagement',
        component: () => import('@/views/UserManagement.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'resumes',
        name: 'ResumeManagement',
        component: () => import('@/views/ResumeManagement.vue'),
        meta: { title: '简历管理' }
      },
      {
        path: 'interviews',
        name: 'InterviewManagement',
        component: () => import('@/views/InterviewManagement.vue'),
        meta: { title: '面试管理' }
      },
      {
        path: 'questions',
        name: 'QuestionManagement',
        component: () => import('@/views/QuestionManagement.vue'),
        meta: { title: '题库管理' }
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('@/views/Settings.vue'),
        meta: { title: '系统设置' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - InterviewAI` : 'InterviewAI'
  next()
})

export default router
