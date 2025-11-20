import request from '@/config/api'

export interface AssetDepreciation {
  depreciationId?: number
  assetId: number
  assetCode?: string
  assetName?: string
  depreciationMethod: string
  originalValue: number
  residualValue?: number
  usefulLife: number
  depreciationRate?: number
  monthlyDepreciation?: number
  accumulatedDepreciation?: number
  netValue?: number
  startDate: string
  lastDepreciationDate?: string
  status?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface AssetDepreciationDetail {
  detailId?: number
  depreciationId: number
  assetId: number
  depreciationMonth: string
  depreciationAmount: number
  accumulatedAmount: number
  netValue: number
  status?: string
  createTime?: string
  confirmTime?: string
  confirmUserId?: number
}

export interface PageParams {
  current: number
  size: number
  assetCode?: string
  status?: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}

/**
 * 分页查询折旧配置
 */
export function getDepreciationPage(params: PageParams) {
  return request.get<PageResult<AssetDepreciation>>('/asset/depreciation/page', { params })
}

/**
 * 配置折旧
 */
export function configDepreciation(data: AssetDepreciation) {
  return request.post('/asset/depreciation/config', data)
}

/**
 * 计算折旧
 */
export function calculateDepreciation(month: string) {
  return request.post(`/asset/depreciation/calculate/${month}`)
}

/**
 * 确认折旧
 */
export function confirmDepreciation(detailId: number) {
  return request.post(`/asset/depreciation/confirm/${detailId}`)
}

/**
 * 获取折旧配置详情
 */
export function getDepreciationById(id: number) {
  return request.get<AssetDepreciation>(`/asset/depreciation/${id}`)
}

/**
 * 获取折旧明细
 */
export function getDepreciationDetails(id: number) {
  return request.get<AssetDepreciationDetail[]>(`/asset/depreciation/${id}/details`)
}

/**
 * 获取月度折旧明细
 */
export function getMonthDepreciationDetails(month: string) {
  return request.get<AssetDepreciationDetail[]>(`/asset/depreciation/month/${month}`)
}

/**
 * 更新折旧配置
 */
export function updateDepreciation(data: AssetDepreciation) {
  return request.put('/asset/depreciation', data)
}

/**
 * 删除折旧配置
 */
export function deleteDepreciation(id: number) {
  return request.delete(`/asset/depreciation/${id}`)
}

