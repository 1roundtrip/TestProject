import request from '@/config/api'

export interface AssetInventory {
  inventoryId?: number
  inventoryNo: string
  inventoryType: string
  inventoryDate: string
  warehouse?: string
  deptId?: number
  deptName?: string
  status?: string
  totalCount?: number
  actualCount?: number
  surplusCount?: number
  shortageCount?: number
  createUserId?: number
  createUserName?: string
  inventoryUserId?: number
  inventoryUserName?: string
  confirmUserId?: number
  confirmUserName?: string
  confirmTime?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface AssetInventoryDetail {
  detailId?: number
  inventoryId?: number
  assetId: number
  assetCode?: string
  assetName?: string
  bookQuantity?: number
  actualQuantity?: number
  differenceQuantity?: number
  differenceType?: string
  differenceReason?: string
  handleStatus?: string
  handleRemark?: string
  createTime?: string
}

export interface PageParams {
  current: number
  size: number
  inventoryNo?: string
  status?: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}

/**
 * 分页查询盘点单
 */
export function getInventoryPage(params: PageParams) {
  return request.get<PageResult<AssetInventory>>('/asset/inventory/page', { params })
}

/**
 * 创建盘点单
 */
export function createInventory(data: AssetInventory) {
  return request.post('/asset/inventory', data)
}

/**
 * 添加盘点明细
 */
export function addInventoryDetails(id: number, details: AssetInventoryDetail[]) {
  return request.post(`/asset/inventory/${id}/details`, details)
}

/**
 * 开始盘点
 */
export function startInventory(id: number) {
  return request.post(`/asset/inventory/${id}/start`)
}

/**
 * 完成盘点
 */
export function completeInventory(id: number) {
  return request.post(`/asset/inventory/${id}/complete`)
}

/**
 * 确认盘点
 */
export function confirmInventory(id: number) {
  return request.post(`/asset/inventory/${id}/confirm`)
}

/**
 * 处理盘点差异
 */
export function handleDifference(detailId: number, handleRemark: string) {
  return request.post(`/asset/inventory/detail/${detailId}/handle`, { handleRemark })
}

/**
 * 获取盘点单详情
 */
export function getInventoryById(id: number) {
  return request.get<AssetInventory>(`/asset/inventory/${id}`)
}

/**
 * 获取盘点明细
 */
export function getInventoryDetails(id: number) {
  return request.get<AssetInventoryDetail[]>(`/asset/inventory/${id}/details`)
}

/**
 * 更新盘点单
 */
export function updateInventory(data: AssetInventory) {
  return request.put('/asset/inventory', data)
}

/**
 * 删除盘点单
 */
export function deleteInventory(id: number) {
  return request.delete(`/asset/inventory/${id}`)
}

