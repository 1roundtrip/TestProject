import request from '@/config/api'

export interface InventoryStock {
  stockId?: number
  warehouseId?: number
  warehouseCode?: string
  warehouseName?: string
  locationId?: number
  locationCode?: string
  materialId?: number
  materialCode?: string
  materialName?: string
  batchNo?: string
  productionDate?: string
  expiryDate?: string
  quantity?: number
  availableQuantity?: number
  frozenQuantity?: number
  unitPrice?: number
  totalValue?: number
  lastInDate?: string
  lastOutDate?: string
  createTime?: string
  updateTime?: string
}

export function getStockPage(params: {
  current: number
  size: number
  warehouseId?: number
  locationId?: number
  materialCode?: string
  materialName?: string
}) {
  return request.get('/inventory/stock/page', { params })
}

export function getMaterialStockSummary(materialId: number) {
  return request.get(`/inventory/stock/material/${materialId}/summary`)
}

export function getWarehouseStockSummary(warehouseId: number) {
  return request.get(`/inventory/stock/warehouse/${warehouseId}/summary`)
}

