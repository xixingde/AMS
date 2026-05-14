import type { Router } from 'vue-router';
import { useUserStore } from '@/stores/user';

export function setupRouterGuard(router: Router) {
  router.beforeEach((to, from, next) => {
    const userStore = useUserStore();
    if (to.path !== '/login' && !userStore.token) {
      next('/login');
    } else {
      next();
    }
  });
}
