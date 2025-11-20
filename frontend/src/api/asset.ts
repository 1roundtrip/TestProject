import request from '@/config/api'

export interface Asset {
  assetId?: number
  assetCode: string
  assetName: string
  assetType?: string
  category?: string
  manufacturer?: string
  model?: string
  serialNumber?: string
  purchaseDate?: string
  purchasePrice?: number
  status?: string
  location?: string
  deptId?: number
  isExplosionProof?: string
  explosionProofExpireDate?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface PageParams {
  current: number
  size: number
  assetName?: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}

/**
 * 分页查询资产
 */
export function getAssetPage(params: PageParams) {
  return request.get<PageResult<Asset>>('/asset/page', { params })
}

/**
 * 新增资产
 */
export function addAsset(data: Asset) {
  return request.post('/asset', data)
}

/**
 * 修改资产
 */
export function updateAsset(data: Asset) {
  return request.put('/asset', data)
}

/**
 * 删除资产
 */
export function deleteAsset(id: number) {
  return request.delete(`/asset/${id}`)
}

/**
 * 查询资产详情
 */
export function getAssetById(id: number) {
  return request.get<Asset>(`/asset/${id}`)
}

/**
 * 获取所有资产类型
 */
export function getAssetTypes() {
  return request.get<string[]>('/asset/types')
}

/**
 * 获取所有制造商
 */
export function getManufacturers() {
  return request.get<string[]>('/asset/manufacturers')
}





