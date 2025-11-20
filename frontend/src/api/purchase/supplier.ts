import request from '@/config/api'

export interface PurchaseSupplier {
  supplierId?: number
  supplierCode?: string
  supplierName: string
  supplierType?: string
  creditLevel?: string
  cooperationYears?: number
  businessLicense?: string
  taxNumber?: string
  legalPerson?: string
  registeredCapital?: number
  contactPerson?: string
  contactPhone?: string
  contactEmail?: string
  address?: string
  bankName?: string
  bankAccount?: string
  accountName?: string
  paymentTerms?: string
  deliveryTerms?: string
  qualityRating?: number
  serviceRating?: number
  priceRating?: number
  totalRating?: number
  status?: string
  blacklistReason?: string
  createUserId?: number
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface PurchaseSupplierEvaluation {
  evaluationId?: number
  supplierId?: number
  orderId?: number
  orderNo?: string
  evaluationDate?: string
  qualityScore?: number
  deliveryScore?: number
  serviceScore?: number
  priceScore?: number
  totalScore?: number
  evaluationContent?: string
  evaluatorId?: number
  evaluatorName?: string
  createTime?: string
}

export interface PageParams {
  current: number
  size: number
  supplierName?: string
  status?: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}

/**
 * 分页查询供应商
 */
export function getSupplierPage(params: PageParams) {
  return request.get<PageResult<PurchaseSupplier>>('/purchase/supplier/page', { params })
}

/**
 * 新增供应商
 */
export function addSupplier(data: PurchaseSupplier) {
  return request.post('/purchase/supplier', data)
}

/**
 * 更新供应商
 */
export function updateSupplier(data: PurchaseSupplier) {
  return request.put('/purchase/supplier', data)
}

/**
 * 删除供应商
 */
export function deleteSupplier(id: number) {
  return request.delete(`/purchase/supplier/${id}`)
}

/**
 * 评价供应商
 */
export function evaluateSupplier(id: number, data: PurchaseSupplierEvaluation) {
  return request.post(`/purchase/supplier/${id}/evaluate`, data)
}

/**
 * 获取供应商评价记录
 */
export function getSupplierEvaluations(id: number) {
  return request.get<PurchaseSupplierEvaluation[]>(`/purchase/supplier/${id}/evaluations`)
}

/**
 * 更新供应商评分
 */
export function updateSupplierRating(id: number) {
  return request.post(`/purchase/supplier/${id}/update-rating`)
}

