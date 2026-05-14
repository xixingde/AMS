import request from '@/utils/request';

export function getTechStackList(params: any) {
  return request.get('/v1/tech-stacks', { params });
}
export function createTechStack(data: any) {
  return request.post('/v1/tech-stacks', data);
}
export function updateTechStack(id: number, data: any) {
  return request.put(`/v1/tech-stacks/${id}`, data);
}
export function deleteTechStack(id: number) {
  return request.delete(`/v1/tech-stacks/${id}`);
}
export function importTechStackExcel(file: File) {
  const formData = new FormData();
  formData.append('file', file);
  return request.post('/v1/tech-stacks/import', formData, { headers: { 'Content-Type': 'multipart/form-data' } });
}
export function exportTechStackExcel(params: any) {
  return request.get('/v1/tech-stacks/export', { params, responseType: 'blob' });
}
export function downloadTechStackTemplate() {
  return request.get('/v1/tech-stacks/template', { responseType: 'blob' });
}
