import request from '@/config/api'

export interface MaintenancePerformance {
  performanceId?: number
  evaluationPeriod?: string
  evaluationDate?: string
  evaluatedUserId?: number
  evaluatedUserName?: string
  workOrderCount?: number
  completedCount?: number
  completionRate?: number
  qualityScore?: number
  totalScore?: number
  performanceLevel?: string
}

export function getPerformancePage(params: {
  current: number
  size: number
  userId?: number
  evaluationPeriod?: string
}) {
  return request.get<{ records: MaintenancePerformance[]; total: number }>('/maintenance/performance/page', { params })
}

export function createPerformance(performance: MaintenancePerformance) {
  return request.post('/maintenance/performance', performance)
}

export function updatePerformance(performance: MaintenancePerformance) {
  return request.put('/maintenance/performance', performance)
}

export function deletePerformance(performanceId: number) {
  return request.delete(`/maintenance/performance/${performanceId}`)
}

