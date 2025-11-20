import request from '@/config/api'

export interface InventoryStocktaking {
  stocktakingId?: number
  stocktakingNo?: string
  stocktakingType?: string
  warehouseId?: number
  warehouseCode?: string
  warehouseName?: string
  stocktakingDate?: string
  startTime?: string
  endTime?: string
  totalItems?: number
  countedItems?: number
  surplusItems?: number
  shortageItems?: number
  surplusAmount?: number
  shortageAmount?: number
  status?: string
  inventoryUserId?: number
  inventoryUserName?: string
  confirmUserId?: number
  confirmUserName?: string
  confirmTime?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface InventoryStocktakingDetail {
  detailId?: number
  stocktakingId?: number
  stockId?: number
  materialId?: number
  materialCode?: string
  materialName?: string
  locationId?: number
  locationCode?: string
  batchNo?: string
  bookQuantity?: number
  actualQuantity?: number
  differenceQuantity?: number
  unitPrice?: number
  differenceAmount?: number
  differenceType?: string
  reason?: string
  remark?: string
}

export function getStocktakingPage(params: {
  current: number
  size: number
  stocktakingNo?: string
  status?: string
  warehouseId?: number
}) {
  return request.get('/inventory/stocktaking/page', { params })
}

export function createStocktaking(data: { stocktaking: InventoryStocktaking; details: InventoryStocktakingDetail[] }) {
  return request.post('/inventory/stocktaking', data)
}

export function startStocktaking(id: number) {
  return request.post(`/inventory/stocktaking/${id}/start`)
}

export function completeStocktaking(id: number) {
  return request.post(`/inventory/stocktaking/${id}/complete`)
}

export function confirmStocktaking(id: number) {
  return request.post(`/inventory/stocktaking/${id}/confirm`)
}

