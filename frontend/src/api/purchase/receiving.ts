import request from '@/config/api'

export interface PurchaseReceiving {
  receivingId?: number
  receivingNo?: string
  orderId?: number
  orderNo?: string
  contractId?: number
  contractNo?: string
  supplierId?: number
  supplierName?: string
  receivingDate?: string
  warehouse?: string
  location?: string
  deliveryNo?: string
  logisticsCompany?: string
  logisticsNo?: string
  totalAmount?: number
  status?: string
  receiverId?: number
  receiverName?: string
  warehouseKeeperId?: number
  warehouseKeeperName?: string
  createUserId?: number
  createUserName?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface PurchaseReceivingDetail {
  detailId?: number
  receivingId?: number
  orderDetailId?: number
  itemName: string
  itemCode?: string
  specification?: string
  unit?: string
  orderQuantity?: number
  receivedQuantity?: number
  qualifiedQuantity?: number
  unqualifiedQuantity?: number
  unitPrice?: number
  totalAmount?: number
  batchNo?: string
  productionDate?: string
  expiryDate?: string
  qualityStatus?: string
  storageStatus?: string
  remark?: string
}

export interface PageParams {
  current: number
  size: number
  receivingNo?: string
  status?: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}

/**
 * 分页查询采购收货
 */
export function getReceivingPage(params: PageParams) {
  return request.get<PageResult<PurchaseReceiving>>('/purchase/receiving/page', { params })
}

/**
 * 创建收货单
 */
export function createReceiving(data: { receiving: PurchaseReceiving; details: PurchaseReceivingDetail[] }) {
  return request.post('/purchase/receiving', data)
}

/**
 * 从采购订单创建收货单
 */
export function createReceivingFromOrder(orderId: number) {
  return request.post(`/purchase/receiving/from-order/${orderId}`)
}

/**
 * 确认收货
 */
export function confirmReceiving(id: number) {
  return request.post(`/purchase/receiving/${id}/confirm`)
}

/**
 * 获取收货明细
 */
export function getReceivingDetails(id: number) {
  return request.get<PurchaseReceivingDetail[]>(`/purchase/receiving/${id}/details`)
}

/**
 * 更新收货单
 */
export function updateReceiving(data: PurchaseReceiving) {
  return request.put('/purchase/receiving', data)
}

/**
 * 删除收货单
 */
export function deleteReceiving(id: number) {
  return request.delete(`/purchase/receiving/${id}`)
}

