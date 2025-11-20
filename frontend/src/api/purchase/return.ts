import request from '@/config/api'

export interface PurchaseReturn {
  returnId?: number
  returnNo?: string
  receivingId?: number
  receivingNo?: string
  orderId?: number
  orderNo?: string
  supplierId?: number
  supplierName?: string
  returnDate?: string
  returnType?: string
  returnReason?: string
  totalAmount?: number
  logisticsCompany?: string
  logisticsNo?: string
  status?: string
  approveUserId?: number
  approveUserName?: string
  approveTime?: string
  returnUserId?: number
  returnUserName?: string
  createUserId?: number
  createUserName?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface PurchaseReturnDetail {
  detailId?: number
  returnId?: number
  receivingDetailId?: number
  itemName: string
  itemCode?: string
  specification?: string
  unit?: string
  returnQuantity?: number
  unitPrice?: number
  totalAmount?: number
  returnReason?: string
  batchNo?: string
  remark?: string
}

export interface PageParams {
  current: number
  size: number
  returnNo?: string
  status?: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}

/**
 * 分页查询采购退货
 */
export function getReturnPage(params: PageParams) {
  return request.get<PageResult<PurchaseReturn>>('/purchase/return/page', { params })
}

/**
 * 创建退货单
 */
export function createReturn(data: { return: PurchaseReturn; details: PurchaseReturnDetail[] }) {
  return request.post('/purchase/return', data)
}

/**
 * 提交审批
 */
export function submitReturn(id: number) {
  return request.post(`/purchase/return/${id}/submit`)
}

/**
 * 审批通过
 */
export function approveReturn(id: number, approveRemark?: string) {
  return request.post(`/purchase/return/${id}/approve`, null, { params: { approveRemark } })
}

/**
 * 确认退货
 */
export function confirmReturn(id: number) {
  return request.post(`/purchase/return/${id}/confirm`)
}

/**
 * 获取退货明细
 */
export function getReturnDetails(id: number) {
  return request.get<PurchaseReturnDetail[]>(`/purchase/return/${id}/details`)
}

/**
 * 更新退货单
 */
export function updateReturn(data: PurchaseReturn) {
  return request.put('/purchase/return', data)
}

/**
 * 删除退货单
 */
export function deleteReturn(id: number) {
  return request.delete(`/purchase/return/${id}`)
}

