import request from '@/config/api'

export interface PurchaseQualityCheck {
  checkId?: number
  checkNo?: string
  receivingId?: number
  receivingNo?: string
  orderId?: number
  orderNo?: string
  supplierId?: number
  supplierName?: string
  checkDate?: string
  checkType?: string
  checkMethod?: string
  checkStandard?: string
  totalQuantity?: number
  qualifiedQuantity?: number
  unqualifiedQuantity?: number
  qualifiedRate?: number
  checkResult?: string
  checkerId?: number
  checkerName?: string
  approveUserId?: number
  approveUserName?: string
  approveTime?: string
  status?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface PurchaseQualityCheckDetail {
  detailId?: number
  checkId?: number
  receivingDetailId?: number
  itemName: string
  itemCode?: string
  specification?: string
  checkQuantity?: number
  qualifiedQuantity?: number
  unqualifiedQuantity?: number
  checkItem?: string
  checkResult?: string
  defectDescription?: string
  disposalMethod?: string
  remark?: string
}

export interface PageParams {
  current: number
  size: number
  checkNo?: string
  status?: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}

/**
 * 分页查询采购质检
 */
export function getQualityCheckPage(params: PageParams) {
  return request.get<PageResult<PurchaseQualityCheck>>('/purchase/quality/page', { params })
}

/**
 * 创建质检单
 */
export function createQualityCheck(data: { qualityCheck: PurchaseQualityCheck; details: PurchaseQualityCheckDetail[] }) {
  return request.post('/purchase/quality', data)
}

/**
 * 从收货单创建质检单
 */
export function createQualityCheckFromReceiving(receivingId: number) {
  return request.post(`/purchase/quality/from-receiving/${receivingId}`)
}

/**
 * 完成质检
 */
export function completeQualityCheck(id: number) {
  return request.post(`/purchase/quality/${id}/complete`)
}

/**
 * 获取质检明细
 */
export function getQualityCheckDetails(id: number) {
  return request.get<PurchaseQualityCheckDetail[]>(`/purchase/quality/${id}/details`)
}

/**
 * 更新质检单
 */
export function updateQualityCheck(data: PurchaseQualityCheck) {
  return request.put('/purchase/quality', data)
}

