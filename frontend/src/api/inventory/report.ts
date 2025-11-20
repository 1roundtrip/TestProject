import request from '@/config/api'

export interface InventoryStatistics {
  statId?: number
  statDate?: string
  statType?: string
  warehouseId?: number
  warehouseCode?: string
  warehouseName?: string
  totalMaterials?: number
  totalQuantity?: number
  totalValue?: number
  inboundCount?: number
  inboundQuantity?: number
  inboundAmount?: number
  outboundCount?: number
  outboundQuantity?: number
  outboundAmount?: number
  transferCount?: number
  adjustmentCount?: number
  stocktakingCount?: number
  warningCount?: number
  turnoverRate?: number
  createTime?: string
  updateTime?: string
}

export function getReportPage(params: {
  current: number
  size: number
  startDate?: string
  endDate?: string
  warehouseId?: number
  statType?: string
}) {
  return request.get('/inventory/report/page', { params })
}

export function getReportStatistics(params: {
  startDate?: string
  endDate?: string
  warehouseId?: number
}) {
  return request.get('/inventory/report/statistics', { params })
}

export function getMaterialTurnoverReport(params: {
  startDate?: string
  endDate?: string
  materialId?: number
}) {
  return request.get('/inventory/report/material-turnover', { params })
}

export function getWarehouseStockReport(params: {
  warehouseId?: number
  date?: string
}) {
  return request.get('/inventory/report/warehouse-stock', { params })
}

