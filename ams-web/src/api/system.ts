import request from '@/utils/request';

export function getSystemList(params: any) {
  return request.get('/v1/systems', { params });
}
export function createSystem(data: any) {
  return request.post('/v1/systems', data);
}
export function updateSystem(id: number, data: any) {
  return request.put(`/v1/systems/${id}`, data);
}
export function deleteSystem(id: number) {
  return request.delete(`/v1/systems/${id}`);
}
export function getAllSystems() {
  return request.get('/v1/systems/all');
}
export function importSystemExcel(file: File) {
  const formData = new FormData();
  formData.append('file', file);
  return request.post('/v1/systems/import', formData, { headers: { 'Content-Type': 'multipart/form-data' } });
}
export function exportSystemExcel(params: any) {
  return request.get('/v1/systems/export', { params, responseType: 'blob' });
}
export function downloadSystemTemplate() {
  return request.get('/v1/systems/template', { responseType: 'blob' });
}

export function getSubSystemList(params: any) {
  return request.get('/v1/sub-systems', { params });
}
export function createSubSystem(data: any) {
  return request.post('/v1/sub-systems', data);
}
export function updateSubSystem(id: number, data: any) {
  return request.put(`/v1/sub-systems/${id}`, data);
}
export function deleteSubSystem(id: number) {
  return request.delete(`/v1/sub-systems/${id}`);
}
export function importSubSystemExcel(file: File) {
  const formData = new FormData();
  formData.append('file', file);
  return request.post('/v1/sub-systems/import', formData, { headers: { 'Content-Type': 'multipart/form-data' } });
}
export function exportSubSystemExcel(params: any) {
  return request.get('/v1/sub-systems/export', { params, responseType: 'blob' });
}
export function downloadSubSystemTemplate() {
  return request.get('/v1/sub-systems/template', { responseType: 'blob' });
}

export function getDeploySubSystemList(params: any) {
  return request.get('/v1/deploy-sub-systems', { params });
}
export function createDeploySubSystem(data: any) {
  return request.post('/v1/deploy-sub-systems', data);
}
export function updateDeploySubSystem(id: number, data: any) {
  return request.put(`/v1/deploy-sub-systems/${id}`, data);
}
export function deleteDeploySubSystem(id: number) {
  return request.delete(`/v1/deploy-sub-systems/${id}`);
}
export function importDeploySubSystemExcel(file: File) {
  const formData = new FormData();
  formData.append('file', file);
  return request.post('/v1/deploy-sub-systems/import', formData, { headers: { 'Content-Type': 'multipart/form-data' } });
}
export function exportDeploySubSystemExcel(params: any) {
  return request.get('/v1/deploy-sub-systems/export', { params, responseType: 'blob' });
}
export function downloadDeploySubSystemTemplate() {
  return request.get('/v1/deploy-sub-systems/template', { responseType: 'blob' });
}

export function getReleaseUnitList(params: any) {
  return request.get('/v1/release-units', { params });
}
export function createReleaseUnit(data: any) {
  return request.post('/v1/release-units', data);
}
export function updateReleaseUnit(id: number, data: any) {
  return request.put(`/v1/release-units/${id}`, data);
}
export function deleteReleaseUnit(id: number) {
  return request.delete(`/v1/release-units/${id}`);
}
export function importReleaseUnitExcel(file: File) {
  const formData = new FormData();
  formData.append('file', file);
  return request.post('/v1/release-units/import', formData, { headers: { 'Content-Type': 'multipart/form-data' } });
}
export function exportReleaseUnitExcel(params: any) {
  return request.get('/v1/release-units/export', { params, responseType: 'blob' });
}
export function downloadReleaseUnitTemplate() {
  return request.get('/v1/release-units/template', { responseType: 'blob' });
}
