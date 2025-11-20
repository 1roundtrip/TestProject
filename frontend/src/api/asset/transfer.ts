import request from '@/config/api'

export interface AssetTransfer {
  transferId?: number
  transferNo: string
  transferDate: string
  assetId: number
  assetCode?: string
  assetName?: string
  fromDeptId?: number
  fromDeptName?: string
  fromLocation?: string
  toDeptId: number
  toDeptName: string
  toLocation?: string
  transferReason?: string
  status?: string
  createUserId?: number
  createUserName?: string
  approveUserId?: number
  approveUserName?: string
  approveTime?: string
  transferUserId?: number
  transferUserName?: string
  transferTime?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface PageParams {
  current: number
  size: number
  transferNo?: string
  status?: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}

/**
 * 分页查询转移单
 */
export function getTransferPage(params: PageParams) {
  return request.get<PageResult<AssetTransfer>>('/asset/transfer/page', { params })
}

/**
 * 创建转移单
 */
export function createTransfer(data: AssetTransfer) {
  return request.post('/asset/transfer', data)
}

/**
 * 审批转移单
 */
export function approveTransfer(id: number, approveRemark?: string) {
  return request.post(`/asset/transfer/${id}/approve`, null, { params: { approveRemark } })
}

/**
 * 驳回转移单
 */
export function rejectTransfer(id: number, rejectRemark?: string) {
  return request.post(`/asset/transfer/${id}/reject`, null, { params: { rejectRemark } })
}

/**
 * 执行转移
 */
export function executeTransfer(id: number) {
  return request.post(`/asset/transfer/${id}/execute`)
}

/**
 * 获取转移单详情
 */
export function getTransferById(id: number) {
  return request.get<AssetTransfer>(`/asset/transfer/${id}`)
}

/**
 * 更新转移单
 */
export function updateTransfer(data: AssetTransfer) {
  return request.put('/asset/transfer', data)
}

/**
 * 删除转移单
 */
export function deleteTransfer(id: number) {
  return request.delete(`/asset/transfer/${id}`)
}

