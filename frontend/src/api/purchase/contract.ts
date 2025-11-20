import request from '@/config/api'

export interface PurchaseContract {
  contractId?: number
  contractNo?: string
  contractName: string
  orderId?: number
  orderNo?: string
  supplierId?: number
  supplierName?: string
  contractType?: string
  contractDate?: string
  startDate?: string
  endDate?: string
  totalAmount?: number
  currency?: string
  paymentMethod?: string
  paymentSchedule?: string
  deliveryTerms?: string
  qualityTerms?: string
  warrantyTerms?: string
  penaltyTerms?: string
  contractFile?: string
  status?: string
  approveUserId?: number
  approveUserName?: string
  approveTime?: string
  signUserId?: number
  signUserName?: string
  signTime?: string
  createUserId?: number
  createUserName?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface PurchaseContractDetail {
  detailId?: number
  contractId?: number
  itemName: string
  itemCode?: string
  specification?: string
  unit?: string
  quantity?: number
  unitPrice?: number
  totalAmount?: number
  deliveryDate?: string
  remark?: string
}

export interface PageParams {
  current: number
  size: number
  contractNo?: string
  status?: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}

/**
 * 分页查询采购合同
 */
export function getContractPage(params: PageParams) {
  return request.get<PageResult<PurchaseContract>>('/purchase/contract/page', { params })
}

/**
 * 创建采购合同
 */
export function createContract(data: { contract: PurchaseContract; details: PurchaseContractDetail[] }) {
  return request.post('/purchase/contract', data)
}

/**
 * 从采购订单创建合同
 */
export function createContractFromOrder(orderId: number) {
  return request.post(`/purchase/contract/from-order/${orderId}`)
}

/**
 * 提交审批
 */
export function submitContract(id: number) {
  return request.post(`/purchase/contract/${id}/submit`)
}

/**
 * 审批通过
 */
export function approveContract(id: number, approveRemark?: string) {
  return request.post(`/purchase/contract/${id}/approve`, null, { params: { approveRemark } })
}

/**
 * 签订合同
 */
export function signContract(id: number) {
  return request.post(`/purchase/contract/${id}/sign`)
}

/**
 * 获取合同明细
 */
export function getContractDetails(id: number) {
  return request.get<PurchaseContractDetail[]>(`/purchase/contract/${id}/details`)
}

/**
 * 更新合同
 */
export function updateContract(data: PurchaseContract) {
  return request.put('/purchase/contract', data)
}

/**
 * 删除合同
 */
export function deleteContract(id: number) {
  return request.delete(`/purchase/contract/${id}`)
}

