import request from '@/config/api'

export interface MaintenanceFaultRecord {
  faultId?: number
  faultNo?: string
  assetId?: number
  assetName?: string
  faultType?: string
  faultSeverity?: string
  faultDescription?: string
  occurredTime?: string
}

export function getFaultPage(params: {
  current: number
  size: number
  faultNo?: string
  assetId?: number
  faultType?: string
}) {
  return request.get<{ records: MaintenanceFaultRecord[]; total: number }>('/maintenance/fault/page', { params })
}

export function createFaultRecord(record: MaintenanceFaultRecord) {
  return request.post('/maintenance/fault', record)
}

export function updateFaultRecord(record: MaintenanceFaultRecord) {
  return request.put('/maintenance/fault', record)
}

export function deleteFaultRecord(faultId: number) {
  return request.delete(`/maintenance/fault/${faultId}`)
}

