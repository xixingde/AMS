import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from '@/stores/user';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/user/LoginView.vue'),
    },
    {
      path: '/',
      component: () => import('@/layouts/MainLayout.vue'),
      redirect: '/dashboard',
      children: [
        { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/DashboardView.vue') },
        { path: 'system', name: 'System', component: () => import('@/views/system/SystemView.vue') },
        { path: 'techstack', name: 'TechStack', component: () => import('@/views/techstack/TechStackView.vue') },
        { path: 'diagram', name: 'Diagram', component: () => import('@/views/diagram/DiagramView.vue') },
        { path: 'relation', name: 'Relation', component: () => import('@/views/relation/RelationView.vue') },
        { path: 'user', name: 'User', component: () => import('@/views/user/UserView.vue') },
      ],
    },
  ],
});

router.beforeEach((to, from, next) => {
  const userStore = useUserStore();
  if (to.path !== '/login' && !userStore.token) {
    next('/login');
  } else {
    next();
  }
});

export default router;
