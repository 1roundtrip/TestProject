import request from '@/config/api'

export interface AssetStorage {
  storageId?: number
  storageNo: string
  storageType: string
  storageDate: string
  supplierId?: number
  supplierName?: string
  totalAmount?: number
  warehouse?: string
  location?: string
  status?: string
  createUserId?: number
  createUserName?: string
  auditUserId?: number
  auditUserName?: string
  auditTime?: string
  remark?: string
  createTime?: string
  updateTime?: string
}

export interface AssetStorageDetail {
  detailId?: number
  storageId?: number
  assetId?: number
  assetCode?: string
  assetName: string
  assetType?: string
  category?: string
  manufacturer?: string
  model?: string
  serialNumber?: string
  quantity?: number
  unitPrice?: number
  totalPrice?: number
  purchaseDate?: string
  warrantyPeriod?: number
  remark?: string
}

export interface PageParams {
  current: number
  size: number
  storageNo?: string
  status?: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}

/**
 * 分页查询入库单
 */
export function getStoragePage(params: PageParams) {
  return request.get<PageResult<AssetStorage>>('/asset/storage/page', { params })
}

/**
 * 创建入库单
 */
export function createStorage(data: { storage: AssetStorage; details: AssetStorageDetail[] }) {
  return request.post('/asset/storage', data)
}

/**
 * 确认入库
 */
export function confirmStorage(id: number) {
  return request.post(`/asset/storage/${id}/confirm`)
}

/**
 * 取消入库
 */
export function cancelStorage(id: number) {
  return request.post(`/asset/storage/${id}/cancel`)
}

/**
 * 获取入库单详情
 */
export function getStorageById(id: number) {
  return request.get<AssetStorage>(`/asset/storage/${id}`)
}

/**
 * 获取入库明细
 */
export function getStorageDetails(id: number) {
  return request.get<AssetStorageDetail[]>(`/asset/storage/${id}/details`)
}

/**
 * 更新入库单
 */
export function updateStorage(data: AssetStorage) {
  return request.put('/asset/storage', data)
}

/**
 * 删除入库单
 */
export function deleteStorage(id: number) {
  return request.delete(`/asset/storage/${id}`)
}

