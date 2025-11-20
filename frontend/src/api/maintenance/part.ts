import request from '@/config/api'

export interface MaintenancePartRequisition {
  requisitionId?: number
  requisitionNo?: string
  workOrderId?: number
  requisitionType?: string
  requisitionDate?: string
  totalAmount?: number
  status?: string
}

export interface MaintenancePartRequisitionDetail {
  detailId?: number
  requisitionId?: number
  materialId?: number
  materialName?: string
  quantity?: number
  unitPrice?: number
  amount?: number
}

export function getPartPage(params: {
  current: number
  size: number
  requisitionNo?: string
  status?: string
}) {
  return request.get<{ records: MaintenancePartRequisition[]; total: number }>('/maintenance/part/page', { params })
}

export function createPartRequisition(data: {
  requisition: MaintenancePartRequisition
  details?: MaintenancePartRequisitionDetail[]
}) {
  return request.post('/maintenance/part', data)
}

export function approveRequisition(requisitionId: number) {
  return request.post(`/maintenance/part/${requisitionId}/approve`)
}

export function issueRequisition(requisitionId: number) {
  return request.post(`/maintenance/part/${requisitionId}/issue`)
}

