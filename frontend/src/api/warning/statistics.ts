import request from '@/config/api'

export interface WarningStatistics {
  statId?: number
  statDate?: string
  statType?: string
  warningType?: string
  warningLevelId?: number
  totalCount?: number
  pendingCount?: number
  processingCount?: number
  resolvedCount?: number
  ignoredCount?: number
  avgResolveTime?: number
}

export function getStatisticsPage(params: {
  current: number
  size: number
  startDate?: string
  endDate?: string
  warningType?: string
  warningLevelId?: number
}) {
  return request.get('/warning/statistics/page', { params })
}

export function getStatisticsSummary(params: {
  startDate?: string
  endDate?: string
  warningType?: string
}) {
  return request.get('/warning/statistics/summary', { params })
}

