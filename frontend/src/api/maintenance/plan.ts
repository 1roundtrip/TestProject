import request from '@/config/api'

export interface MaintenancePlan {
  planId?: number
  planNo?: string
  planName?: string
  planType?: string
  assetId?: number
  assetCode?: string
  assetName?: string
  maintenanceType?: string
  cycleType?: string
  cycleValue?: number
  cycleUnit?: string
  nextMaintenanceDate?: string
  lastMaintenanceDate?: string
  maintenanceContent?: string
  status?: string
}

export function getPlanPage(params: {
  current: number
  size: number
  planNo?: string
  status?: string
  assetId?: number
}) {
  return request.get<{ records: MaintenancePlan[]; total: number }>('/maintenance/plan/page', { params })
}

export function createPlan(plan: MaintenancePlan) {
  return request.post('/maintenance/plan', plan)
}

export function updatePlan(plan: MaintenancePlan) {
  return request.put('/maintenance/plan', plan)
}

export function deletePlan(planId: number) {
  return request.delete(`/maintenance/plan/${planId}`)
}

export function executePlan(planId: number) {
  return request.post(`/maintenance/plan/${planId}/execute`)
}

