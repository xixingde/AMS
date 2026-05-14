import { defineStore } from 'pinia';
import { ref } from 'vue';

export interface UserInfo {
  id: number;
  username: string;
  role: string;
}

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem('ams_token') || '');
  const userInfo = ref<UserInfo | null>(null);

  const setToken = (t: string) => {
    token.value = t;
    localStorage.setItem('ams_token', t);
  };

  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info;
  };

  const logout = () => {
    token.value = '';
    userInfo.value = null;
    localStorage.removeItem('ams_token');
  };

  return { token, userInfo, setToken, setUserInfo, logout };
});
