import request from '@/config/api'

export interface PurchasePayment {
  paymentId?: number
  paymentNo?: string
  orderId?: number
  orderNo?: string
  contractId?: number
  contractNo?: string
  supplierId?: number
  supplierName?: string
  paymentType?: string
  paymentDate?: string
  paymentMethod?: string
  currency?: string
  paymentAmount?: number
  orderAmount?: number
  paidAmount?: number
  balanceAmount?: number
  bankName?: string
  bankAccount?: string
  accountName?: string
  voucherNo?: string
  status?: string
  approveUserId?: number
  approveUserName?: string
  approveTime?: string
  payUserId?: number
  payUserName?: string
  payTime?: string
  createUserId?: number
  createUserName?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface PurchasePaymentDetail {
  detailId?: number
  paymentId?: number
  orderId?: number
  orderNo?: string
  receivingId?: number
  receivingNo?: string
  itemName?: string
  paymentAmount?: number
  remark?: string
}

export interface PageParams {
  current: number
  size: number
  paymentNo?: string
  status?: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}

/**
 * 分页查询采购付款
 */
export function getPaymentPage(params: PageParams) {
  return request.get<PageResult<PurchasePayment>>('/purchase/payment/page', { params })
}

/**
 * 创建付款单
 */
export function createPayment(data: { payment: PurchasePayment; details: PurchasePaymentDetail[] }) {
  return request.post('/purchase/payment', data)
}

/**
 * 从采购订单创建付款单
 */
export function createPaymentFromOrder(orderId: number, paymentType: string) {
  return request.post(`/purchase/payment/from-order/${orderId}`, null, { params: { paymentType } })
}

/**
 * 提交审批
 */
export function submitPayment(id: number) {
  return request.post(`/purchase/payment/${id}/submit`)
}

/**
 * 审批通过
 */
export function approvePayment(id: number, approveRemark?: string) {
  return request.post(`/purchase/payment/${id}/approve`, null, { params: { approveRemark } })
}

/**
 * 确认付款
 */
export function confirmPayment(id: number) {
  return request.post(`/purchase/payment/${id}/confirm`)
}

/**
 * 获取付款明细
 */
export function getPaymentDetails(id: number) {
  return request.get<PurchasePaymentDetail[]>(`/purchase/payment/${id}/details`)
}

/**
 * 更新付款单
 */
export function updatePayment(data: PurchasePayment) {
  return request.put('/purchase/payment', data)
}

/**
 * 删除付款单
 */
export function deletePayment(id: number) {
  return request.delete(`/purchase/payment/${id}`)
}

