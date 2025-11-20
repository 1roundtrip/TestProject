import request from '@/config/api'

export interface WarningAlert {
  alertId?: number
  alertType?: string
  alertLevel?: 'YELLOW' | 'ORANGE' | 'RED'
  assetId?: number
  assetCode?: string
  assetName?: string
  alertTitle?: string
  alertContent?: string
  expireDate?: string
  daysRemaining?: number
  status?: string
  createTime?: string
}

export interface WarningStats {
  totalUnhandled: number
  yellowCount: number
  orangeCount: number
  redCount: number
}

export interface PageParams {
  current: number
  size: number
  alertLevel?: string
  status?: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}

/**
 * 分页查询预警记录
 */
export function getWarningPage(params: PageParams) {
  return request.get<PageResult<WarningAlert>>('/warning/page', { params })
}

/**
 * 根据级别查询预警
 */
export function getWarningsByLevel(level: string) {
  return request.get<WarningAlert[]>(`/warning/level/${level}`)
}

/**
 * 获取预警统计
 */
export function getWarningStats() {
  return request.get<WarningStats>('/warning/stats')
}

/**
 * 标记预警为已处理
 */
export function handleWarning(id: number) {
  return request.put(`/warning/${id}/handle`)
}

/**
 * 批量处理预警
 */
export function batchHandleWarnings(ids: number[]) {
  return request.put('/warning/batch-handle', ids)
}















