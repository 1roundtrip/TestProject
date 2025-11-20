import request from '@/config/api'

export interface DashboardStats {
  totalAssets: number
  repairingAssets: number
  inventoryAlerts: number
  explosionProofWarnings: number
}

export interface AssetStatusDistribution {
  name: string
  value: number
}

export interface RepairTrend {
  date: string
  count: number
}

/**
 * 获取仪表盘统计数据
 */
export function getDashboardStats() {
  return request.get<DashboardStats>('/dashboard/stats')
}

/**
 * 获取防爆预警统计（从预警中心获取）
 */
export function getExplosionProofWarningStats() {
  return request.get('/warning/stats')
}

/**
 * 获取设备状态分布
 */
export function getAssetStatusDistribution() {
  return request.get<AssetStatusDistribution[]>('/dashboard/asset-status')
}

/**
 * 获取维修趋势
 */
export function getRepairTrend() {
  return request.get<RepairTrend[]>('/dashboard/repair-trend')
}

