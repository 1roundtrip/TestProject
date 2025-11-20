import request from '@/config/api'

export function getStatistics(startDate?: string, endDate?: string) {
  return request.get<Record<string, any>>('/maintenance/report/statistics', {
    params: { startDate, endDate },
  })
}

export function getWorkOrderStatistics(startDate?: string, endDate?: string) {
  return request.get<Record<string, any>>('/maintenance/report/work-order', {
    params: { startDate, endDate },
  })
}

export function getCostStatistics(startDate?: string, endDate?: string) {
  return request.get<Record<string, any>>('/maintenance/report/cost', {
    params: { startDate, endDate },
  })
}

export function getFaultStatistics(startDate?: string, endDate?: string) {
  return request.get<Record<string, any>>('/maintenance/report/fault', {
    params: { startDate, endDate },
  })
}

