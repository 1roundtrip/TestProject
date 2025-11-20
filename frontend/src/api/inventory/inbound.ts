import request from '@/config/api'

export interface InventoryInbound {
  inboundId?: number
  inboundNo?: string
  inboundType?: string
  warehouseId?: number
  warehouseCode?: string
  warehouseName?: string
  sourceType?: string
  sourceNo?: string
  sourceId?: number
  inboundDate?: string
  supplierId?: number
  supplierName?: string
  totalQuantity?: number
  totalAmount?: number
  handlerId?: number
  handlerName?: string
  receiverId?: number
  receiverName?: string
  status?: string
  approveUserId?: number
  approveUserName?: string
  approveTime?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface InventoryInboundDetail {
  detailId?: number
  inboundId?: number
  materialId?: number
  materialCode?: string
  materialName?: string
  specification?: string
  unit?: string
  quantity?: number
  receivedQuantity?: number
  unitPrice?: number
  amount?: number
  batchNo?: string
  productionDate?: string
  expiryDate?: string
  locationId?: number
  locationCode?: string
  remark?: string
}

export function getInboundPage(params: {
  current: number
  size: number
  inboundNo?: string
  inboundType?: string
  status?: string
  warehouseId?: number
}) {
  return request.get('/inventory/inbound/page', { params })
}

export function createInbound(data: { inbound: InventoryInbound; details: InventoryInboundDetail[] }) {
  return request.post('/inventory/inbound', data)
}

export function getInboundById(id: number) {
  return request.get(`/inventory/inbound/${id}`)
}

export function getInboundDetails(id: number) {
  return request.get(`/inventory/inbound/${id}/details`)
}

export function submitInbound(id: number) {
  return request.post(`/inventory/inbound/${id}/submit`)
}

export function approveInbound(id: number) {
  return request.post(`/inventory/inbound/${id}/approve`)
}

export function receiveInbound(id: number) {
  return request.post(`/inventory/inbound/${id}/receive`)
}

