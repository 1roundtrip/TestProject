import request from '@/config/api'

export interface InventoryTransfer {
  transferId?: number
  transferNo?: string
  transferType?: string
  fromWarehouseId?: number
  fromWarehouseCode?: string
  fromWarehouseName?: string
  fromLocationId?: number
  fromLocationCode?: string
  toWarehouseId?: number
  toWarehouseCode?: string
  toWarehouseName?: string
  toLocationId?: number
  toLocationCode?: string
  transferDate?: string
  totalQuantity?: number
  totalAmount?: number
  handlerId?: number
  handlerName?: string
  status?: string
  approveUserId?: number
  approveUserName?: string
  approveTime?: string
  outboundTime?: string
  inboundTime?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface InventoryTransferDetail {
  detailId?: number
  transferId?: number
  materialId?: number
  materialCode?: string
  materialName?: string
  specification?: string
  unit?: string
  quantity?: number
  outboundQuantity?: number
  inboundQuantity?: number
  unitPrice?: number
  amount?: number
  batchNo?: string
  fromStockId?: number
  toStockId?: number
  remark?: string
}

export function getTransferPage(params: {
  current: number
  size: number
  transferNo?: string
  status?: string
  fromWarehouseId?: number
  toWarehouseId?: number
}) {
  return request.get('/inventory/transfer/page', { params })
}

export function createTransfer(data: { transfer: InventoryTransfer; details: InventoryTransferDetail[] }) {
  return request.post('/inventory/transfer', data)
}

export function submitTransfer(id: number) {
  return request.post(`/inventory/transfer/${id}/submit`)
}

export function approveTransfer(id: number) {
  return request.post(`/inventory/transfer/${id}/approve`)
}

export function outboundTransfer(id: number) {
  return request.post(`/inventory/transfer/${id}/outbound`)
}

export function inboundTransfer(id: number) {
  return request.post(`/inventory/transfer/${id}/inbound`)
}

