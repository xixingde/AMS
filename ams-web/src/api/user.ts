import request from '@/utils/request';

export function getUserList(params: any) {
  return request.get('/v1/users', { params });
}

export function createUser(data: any) {
  return request.post('/v1/users', data);
}

export function updateUser(id: number, data: any) {
  return request.put(`/v1/users/${id}`, data);
}

export function deleteUser(id: number) {
  return request.delete(`/v1/users/${id}`);
}

export function getRoleList() {
  return request.get('/v1/roles');
}
