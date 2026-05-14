import axios, { AxiosError } from 'axios';
import { ElMessage } from 'element-plus';
import { useUserStore } from '@/stores/user';

const request = axios.create({
  baseURL: '/api',
  timeout: 30000,
});

request.interceptors.request.use(
  (config) => {
    const userStore = useUserStore();
    if (userStore.token) {
      config.headers.Authorization = `Bearer ${userStore.token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

request.interceptors.response.use(
  (response) => {
    const data = response.data;
    if (data.code !== 0) {
      ElMessage.error(data.message || '请求失败');
      return Promise.reject(new Error(data.message));
    }
    return data;
  },
  (error: AxiosError) => {
    if (error.response?.status === 401) {
      const userStore = useUserStore();
      userStore.logout();
      window.location.href = '/login';
    }
    ElMessage.error((error.response?.data as any)?.message || '网络错误');
    return Promise.reject(error);
  }
);

export default request;
