import request from '@/config/api'

export interface PurchasePlan {
  planId?: number
  planNo?: string
  planName: string
  planYear: number
  planQuarter?: number
  planMonth?: number
  deptId?: number
  deptName?: string
  budgetAmount?: number
  totalAmount?: number
  status?: string
  approveUserId?: number
  approveUserName?: string
  approveTime?: string
  approveRemark?: string
  createUserId?: number
  createUserName?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface PurchasePlanDetail {
  detailId?: number
  planId?: number
  itemName: string
  itemCode?: string
  specification?: string
  unit?: string
  quantity?: number
  estimatedPrice?: number
  estimatedAmount?: number
  purpose?: string
  requiredDate?: string
  priority?: string
  remark?: string
}

export interface PageParams {
  current: number
  size: number
  planNo?: string
  status?: string
  planYear?: number
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}

/**
 * 分页查询采购计划
 */
export function getPlanPage(params: PageParams) {
  return request.get<PageResult<PurchasePlan>>('/purchase/plan/page', { params })
}

/**
 * 创建采购计划
 */
export function createPlan(data: { plan: PurchasePlan; details: PurchasePlanDetail[] }) {
  return request.post('/purchase/plan', data)
}

/**
 * 提交审批
 */
export function submitPlan(id: number) {
  return request.post(`/purchase/plan/${id}/submit`)
}

/**
 * 审批通过
 */
export function approvePlan(id: number, approveRemark?: string) {
  return request.post(`/purchase/plan/${id}/approve`, null, { params: { approveRemark } })
}

/**
 * 审批驳回
 */
export function rejectPlan(id: number, approveRemark?: string) {
  return request.post(`/purchase/plan/${id}/reject`, null, { params: { approveRemark } })
}

/**
 * 获取计划明细
 */
export function getPlanDetails(id: number) {
  return request.get<PurchasePlanDetail[]>(`/purchase/plan/${id}/details`)
}

/**
 * 更新计划
 */
export function updatePlan(data: PurchasePlan) {
  return request.put('/purchase/plan', data)
}

/**
 * 删除计划
 */
export function deletePlan(id: number) {
  return request.delete(`/purchase/plan/${id}`)
}

