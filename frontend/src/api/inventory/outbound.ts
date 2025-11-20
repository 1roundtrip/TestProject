import request from '@/config/api'

export interface InventoryOutbound {
  outboundId?: number
  outboundNo?: string
  outboundType?: string
  warehouseId?: number
  warehouseCode?: string
  warehouseName?: string
  destinationType?: string
  destinationNo?: string
  destinationId?: number
  outboundDate?: string
  customerId?: number
  customerName?: string
  deptId?: number
  deptName?: string
  recipientId?: number
  recipientName?: string
  totalQuantity?: number
  totalAmount?: number
  handlerId?: number
  handlerName?: string
  status?: string
  approveUserId?: number
  approveUserName?: string
  approveTime?: string
  issueUserId?: number
  issueUserName?: string
  issueTime?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface InventoryOutboundDetail {
  detailId?: number
  outboundId?: number
  materialId?: number
  materialCode?: string
  materialName?: string
  specification?: string
  unit?: string
  quantity?: number
  issuedQuantity?: number
  unitPrice?: number
  amount?: number
  batchNo?: string
  locationId?: number
  locationCode?: string
  stockId?: number
  remark?: string
}

export function getOutboundPage(params: {
  current: number
  size: number
  outboundNo?: string
  outboundType?: string
  status?: string
  warehouseId?: number
}) {
  return request.get('/inventory/outbound/page', { params })
}

export function createOutbound(data: { outbound: InventoryOutbound; details: InventoryOutboundDetail[] }) {
  return request.post('/inventory/outbound', data)
}

export function getOutboundById(id: number) {
  return request.get(`/inventory/outbound/${id}`)
}

export function getOutboundDetails(id: number) {
  return request.get(`/inventory/outbound/${id}/details`)
}

export function submitOutbound(id: number) {
  return request.post(`/inventory/outbound/${id}/submit`)
}

export function approveOutbound(id: number) {
  return request.post(`/inventory/outbound/${id}/approve`)
}

export function issueOutbound(id: number) {
  return request.post(`/inventory/outbound/${id}/issue`)
}

