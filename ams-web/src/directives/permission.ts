import type { Directive } from 'vue';
import { useUserStore } from '@/stores/user';

export const permission: Directive = {
  mounted(el, binding) {
    const userStore = useUserStore();
    const required = binding.value;
    const role = userStore.userInfo?.role;
    if (!role || !required.includes(role)) {
      el.parentNode?.removeChild(el);
    }
  },
};
