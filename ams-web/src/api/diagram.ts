import request from '@/utils/request';

export function getDiagram(deploySysNo: string) {
  return request.get(`/v1/diagrams/${deploySysNo}`);
}
export function saveLayout(deploySysNo: string, data: any) {
  return request.post(`/v1/diagrams/${deploySysNo}/layout`, data);
}
export function getLayout(deploySysNo: string) {
  return request.get(`/v1/diagrams/${deploySysNo}/layout`);
}
