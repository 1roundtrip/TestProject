import request from '@/config/api'

export interface InventoryWarning {
  warningId?: number
  warningNo?: string
  warningType?: string
  warningLevel?: string
  warehouseId?: number
  warehouseCode?: string
  warehouseName?: string
  materialId?: number
  materialCode?: string
  materialName?: string
  currentQuantity?: number
  minStock?: number
  maxStock?: number
  safetyStock?: number
  expiryDate?: string
  daysToExpiry?: number
  warningMessage?: string
  status?: string
  handlerId?: number
  handlerName?: string
  handleTime?: string
  handleResult?: string
  warningTime?: string
  createTime?: string
  updateTime?: string
}

export function getWarningPage(params: {
  current: number
  size: number
  warningType?: string
  warningLevel?: string
  status?: string
}) {
  return request.get('/inventory/warning/page', { params })
}

export function handleWarning(id: number, handleResult: string) {
  return request.post(`/inventory/warning/${id}/handle`, null, { params: { handleResult } })
}

export function ignoreWarning(id: number) {
  return request.post(`/inventory/warning/${id}/ignore`)
}

export function generateWarnings() {
  return request.post('/inventory/warning/generate')
}

export function getWarningStatistics() {
  return request.get('/inventory/warning/statistics')
}

