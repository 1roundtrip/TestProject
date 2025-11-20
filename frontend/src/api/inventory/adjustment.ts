import request from '@/config/api'

export interface InventoryAdjustment {
  adjustmentId?: number
  adjustmentNo?: string
  adjustmentType?: string
  adjustmentReason?: string
  warehouseId?: number
  warehouseCode?: string
  warehouseName?: string
  adjustmentDate?: string
  totalItems?: number
  handlerId?: number
  handlerName?: string
  status?: string
  approveUserId?: number
  approveUserName?: string
  approveTime?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface InventoryAdjustmentDetail {
  detailId?: number
  adjustmentId?: number
  stockId?: number
  materialId?: number
  materialCode?: string
  materialName?: string
  locationId?: number
  locationCode?: string
  batchNo?: string
  beforeQuantity?: number
  afterQuantity?: number
  adjustmentQuantity?: number
  beforeUnitPrice?: number
  afterUnitPrice?: number
  beforeTotalValue?: number
  afterTotalValue?: number
  adjustmentValue?: number
  reason?: string
  remark?: string
}

export function getAdjustmentPage(params: {
  current: number
  size: number
  adjustmentNo?: string
  adjustmentType?: string
  status?: string
  warehouseId?: number
}) {
  return request.get('/inventory/adjustment/page', { params })
}

export function createAdjustment(data: { adjustment: InventoryAdjustment; details: InventoryAdjustmentDetail[] }) {
  return request.post('/inventory/adjustment', data)
}

export function submitAdjustment(id: number) {
  return request.post(`/inventory/adjustment/${id}/submit`)
}

export function approveAdjustment(id: number) {
  return request.post(`/inventory/adjustment/${id}/approve`)
}

