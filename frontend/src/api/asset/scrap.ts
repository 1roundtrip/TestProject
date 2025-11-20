import request from '@/config/api'

export interface AssetScrap {
  scrapId?: number
  scrapNo: string
  scrapDate: string
  assetId: number
  assetCode?: string
  assetName?: string
  scrapReason: string
  scrapType?: string
  originalValue?: number
  netValue?: number
  scrapValue?: number
  status?: string
  applyUserId?: number
  applyUserName?: string
  applyTime?: string
  approveUserId?: number
  approveUserName?: string
  approveTime?: string
  approveRemark?: string
  handleUserId?: number
  handleUserName?: string
  handleTime?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface PageParams {
  current: number
  size: number
  scrapNo?: string
  status?: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}

/**
 * 分页查询报废单
 */
export function getScrapPage(params: PageParams) {
  return request.get<PageResult<AssetScrap>>('/asset/scrap/page', { params })
}

/**
 * 创建报废申请
 */
export function createScrap(data: AssetScrap) {
  return request.post('/asset/scrap', data)
}

/**
 * 审批报废申请
 */
export function approveScrap(id: number, approveRemark?: string) {
  return request.post(`/asset/scrap/${id}/approve`, { approveRemark })
}

/**
 * 驳回报废申请
 */
export function rejectScrap(id: number, rejectRemark?: string) {
  return request.post(`/asset/scrap/${id}/reject`, { rejectRemark })
}

/**
 * 完成报废
 */
export function completeScrap(id: number) {
  return request.post(`/asset/scrap/${id}/complete`)
}

/**
 * 获取报废单详情
 */
export function getScrapById(id: number) {
  return request.get<AssetScrap>(`/asset/scrap/${id}`)
}

/**
 * 更新报废单
 */
export function updateScrap(data: AssetScrap) {
  return request.put('/asset/scrap', data)
}

/**
 * 删除报废单
 */
export function deleteScrap(id: number) {
  return request.delete(`/asset/scrap/${id}`)
}

