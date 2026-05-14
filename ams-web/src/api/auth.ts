import request from '@/utils/request';

export function login(data: { username: string; password: string }) {
  return request.post('/v1/auth/login', data);
}

export function logout() {
  return request.post('/v1/auth/logout');
}

export function getMe() {
  return request.get('/v1/auth/me');
}
