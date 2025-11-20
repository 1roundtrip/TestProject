import request from '@/config/api'

export interface PurchaseOrder {
  orderId?: number
  orderNo?: string
  requisitionId?: number
  requisitionNo?: string
  supplierId?: number
  supplierName?: string
  supplierCode?: string
  orderType?: string
  orderDate?: string
  deliveryDate?: string
  deliveryAddress?: string
  deliveryMethod?: string
  paymentTerms?: string
  currency?: string
  totalAmount?: number
  taxAmount?: number
  totalAmountWithTax?: number
  status?: string
  approveUserId?: number
  approveUserName?: string
  approveTime?: string
  buyerId?: number
  buyerName?: string
  createUserId?: number
  createUserName?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface PurchaseOrderDetail {
  detailId?: number
  orderId?: number
  itemName: string
  itemCode?: string
  specification?: string
  brand?: string
  unit?: string
  quantity?: number
  unitPrice?: number
  taxRate?: number
  amount?: number
  taxAmount?: number
  amountWithTax?: number
  receivedQuantity?: number
  requiredDate?: string
  remark?: string
}

export interface PageParams {
  current: number
  size: number
  orderNo?: string
  status?: string
  supplierId?: number
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}

/**
 * 分页查询采购订单
 */
export function getOrderPage(params: PageParams) {
  return request.get<PageResult<PurchaseOrder>>('/purchase/order/page', { params })
}

/**
 * 创建采购订单
 */
export function createOrder(data: { order: PurchaseOrder; details: PurchaseOrderDetail[] }) {
  return request.post('/purchase/order', data)
}

/**
 * 从采购申请创建订单
 */
export function createOrderFromRequisition(requisitionId: number, supplierId: number) {
  return request.post(`/purchase/order/from-requisition/${requisitionId}`, null, { params: { supplierId } })
}

/**
 * 提交审批
 */
export function submitOrder(id: number) {
  return request.post(`/purchase/order/${id}/submit`)
}

/**
 * 审批通过
 */
export function approveOrder(id: number, approveRemark?: string) {
  return request.post(`/purchase/order/${id}/approve`, null, { params: { approveRemark } })
}

/**
 * 确认订单
 */
export function confirmOrder(id: number) {
  return request.post(`/purchase/order/${id}/confirm`)
}

/**
 * 获取订单明细
 */
export function getOrderDetails(id: number) {
  return request.get<PurchaseOrderDetail[]>(`/purchase/order/${id}/details`)
}

/**
 * 更新订单
 */
export function updateOrder(data: PurchaseOrder) {
  return request.put('/purchase/order', data)
}

/**
 * 删除订单
 */
export function deleteOrder(id: number) {
  return request.delete(`/purchase/order/${id}`)
}

