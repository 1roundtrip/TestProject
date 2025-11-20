import request from '@/config/api'

export interface PurchaseRequisition {
  requisitionId?: number
  requisitionNo?: string
  planId?: number
  planNo?: string
  requisitionName: string
  deptId?: number
  deptName?: string
  applicantId?: number
  applicantName?: string
  totalAmount?: number
  urgentLevel?: string
  purpose?: string
  status?: string
  approveUserId?: number
  approveUserName?: string
  approveTime?: string
  approveRemark?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface PurchaseRequisitionDetail {
  detailId?: number
  requisitionId?: number
  itemName: string
  itemCode?: string
  specification?: string
  brand?: string
  unit?: string
  quantity?: number
  estimatedPrice?: number
  estimatedAmount?: number
  requiredDate?: string
  purpose?: string
  remark?: string
}

export interface PageParams {
  current: number
  size: number
  requisitionNo?: string
  status?: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}

/**
 * 分页查询采购申请
 */
export function getRequisitionPage(params: PageParams) {
  return request.get<PageResult<PurchaseRequisition>>('/purchase/requisition/page', { params })
}

/**
 * 创建采购申请
 */
export function createRequisition(data: { requisition: PurchaseRequisition; details: PurchaseRequisitionDetail[] }) {
  return request.post('/purchase/requisition', data)
}

/**
 * 提交审批
 */
export function submitRequisition(id: number) {
  return request.post(`/purchase/requisition/${id}/submit`)
}

/**
 * 审批通过
 */
export function approveRequisition(id: number, approveRemark?: string) {
  return request.post(`/purchase/requisition/${id}/approve`, null, { params: { approveRemark } })
}

/**
 * 审批驳回
 */
export function rejectRequisition(id: number, approveRemark?: string) {
  return request.post(`/purchase/requisition/${id}/reject`, null, { params: { approveRemark } })
}

/**
 * 获取申请明细
 */
export function getRequisitionDetails(id: number) {
  return request.get<PurchaseRequisitionDetail[]>(`/purchase/requisition/${id}/details`)
}

/**
 * 更新申请
 */
export function updateRequisition(data: PurchaseRequisition) {
  return request.put('/purchase/requisition', data)
}

/**
 * 删除申请
 */
export function deleteRequisition(id: number) {
  return request.delete(`/purchase/requisition/${id}`)
}

