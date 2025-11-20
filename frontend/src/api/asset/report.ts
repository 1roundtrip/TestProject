import request from '@/config/api'

/**
 * 获取资产统计报表
 */
export function getStatistics() {
  return request.get('/asset/report/statistics')
}

/**
 * 获取资产价值分析
 */
export function getValueAnalysis(startDate?: string, endDate?: string) {
  return request.get('/asset/report/value', {
    params: { startDate, endDate }
  })
}

/**
 * 获取资产使用率分析
 */
export function getUsageAnalysis() {
  return request.get('/asset/report/usage')
}

/**
 * 获取资产折旧报表
 */
export function getDepreciationReport(month?: string) {
  return request.get('/asset/report/depreciation', {
    params: { month }
  })
}

/**
 * 获取资产盘点差异分析
 */
export function getInventoryDifference(inventoryId?: string) {
  return request.get('/asset/report/inventory/difference', {
    params: { inventoryId }
  })
}

/**
 * 获取资产报废统计
 */
export function getScrapStatistics(startDate?: string, endDate?: string) {
  return request.get('/asset/report/scrap', {
    params: { startDate, endDate }
  })
}

