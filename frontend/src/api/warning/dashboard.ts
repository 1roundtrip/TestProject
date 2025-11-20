import request from '@/config/api'

export function getDashboardSummary() {
  return request.get('/warning/dashboard/summary')
}

export function getRecentWarnings(limit: number = 10) {
  return request.get('/warning/dashboard/recent', { params: { limit } })
}

