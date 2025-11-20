import request from '@/config/api'

export function getReportStatistics(params: {
  startDate?: string
  endDate?: string
}) {
  return request.get('/warning/report/statistics', { params })
}

export function getReportPage(params: {
  current: number
  size: number
  warningType?: string
  warningLevelCode?: string
  status?: string
}) {
  return request.get('/warning/report/page', { params })
}

