import request from '@/config/api'

export interface MaintenanceQualityCheck {
  checkId?: number
  checkNo?: string
  workOrderId?: number
  checkType?: string
  checkDate?: string
  checkResult?: string
  qualityScore?: number
}

export function getQualityPage(params: {
  current: number
  size: number
  checkNo?: string
  workOrderId?: number
}) {
  return request.get<{ records: MaintenanceQualityCheck[]; total: number }>('/maintenance/quality/page', { params })
}

export function createQualityCheck(check: MaintenanceQualityCheck) {
  return request.post('/maintenance/quality', check)
}

export function updateQualityCheck(check: MaintenanceQualityCheck) {
  return request.put('/maintenance/quality', check)
}

export function deleteQualityCheck(checkId: number) {
  return request.delete(`/maintenance/quality/${checkId}`)
}

