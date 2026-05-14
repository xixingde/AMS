import request from '@/utils/request';

export function getRelationList(params: any) {
  return request.get('/v1/release-unit-relations', { params });
}
export function createRelation(data: any) {
  return request.post('/v1/release-unit-relations', data);
}
export function updateRelation(id: number, data: any) {
  return request.put(`/v1/release-unit-relations/${id}`, data);
}
export function deleteRelation(id: number) {
  return request.delete(`/v1/release-unit-relations/${id}`);
}
