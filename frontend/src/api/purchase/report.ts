import request from '@/config/api'

export interface PurchaseStatistics {
  totalOrders?: number
  totalAmount?: number
  totalReceived?: number
  totalPaid?: number
  qualityPassRate?: number
  onTimeDeliveryRate?: number
}

export interface SupplierEvaluation {
  supplierId?: number
  supplierName?: string
  totalScore?: number
  qualityScore?: number
  serviceScore?: number
  priceScore?: number
  orderCount?: number
}

/**
 * 采购统计报表
 */
export function getStatistics(params?: { startDate?: string; endDate?: string; supplierId?: number }) {
  return request.get<PurchaseStatistics>('/purchase/report/statistics', { params })
}

/**
 * 供应商评价报表
 */
export function getSupplierEvaluation(params?: { supplierId?: number }) {
  return request.get<SupplierEvaluation>('/purchase/report/supplier-evaluation', { params })
}

