import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/Home.vue')
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/Profile.vue')
      },
      {
        path: 'resume',
        name: 'ResumeList',
        component: () => import('@/views/ResumeList.vue')
      },
      {
        path: 'resume/edit',
        name: 'ResumeEdit',
        component: () => import('@/views/ResumeEdit.vue')
      }
    ]
  },
  {
    path: '/interview',
    component: () => import('@/views/Layout.vue'),
    redirect: '/interview/select',
    children: [
      {
        path: 'select',
        name: 'JobSelect',
        component: () => import('@/views/interview/JobSelect.vue')
      },
      {
        path: 'config',
        name: 'InterviewConfig',
        component: () => import('@/views/interview/InterviewConfig.vue')
      },
      {
        path: 'text',
        name: 'TextInterview',
        component: () => import('@/views/interview/TextInterview.vue')
      },
      {
        path: 'report/:id',
        name: 'InterviewReport',
        component: () => import('@/views/interview/InterviewReport.vue')
      },
      {
        path: 'voice',
        name: 'VoiceInterview',
        component: () => import('@/views/interview/VoiceInterview.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
