import request from '@/config/api'

export interface AssetBorrow {
  borrowId?: number
  borrowNo: string
  borrowType: string
  borrowDate: string
  assetId: number
  assetCode?: string
  assetName?: string
  borrowerId: number
  borrowerName: string
  borrowerDeptId?: number
  borrowerDeptName?: string
  expectedReturnDate?: string
  actualReturnDate?: string
  borrowReason?: string
  status?: string
  createUserId?: number
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface PageParams {
  current: number
  size: number
  borrowNo?: string
  status?: string
  borrowType?: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}

/**
 * 分页查询领用单
 */
export function getBorrowPage(params: PageParams) {
  return request.get<PageResult<AssetBorrow>>('/asset/borrow/page', { params })
}

/**
 * 创建领用单
 */
export function createBorrow(data: AssetBorrow) {
  return request.post('/asset/borrow', data)
}

/**
 * 退库
 */
export function returnAsset(id: number) {
  return request.post(`/asset/borrow/${id}/return`)
}

/**
 * 获取领用单详情
 */
export function getBorrowById(id: number) {
  return request.get<AssetBorrow>(`/asset/borrow/${id}`)
}

/**
 * 更新领用单
 */
export function updateBorrow(data: AssetBorrow) {
  return request.put('/asset/borrow', data)
}

/**
 * 删除领用单
 */
export function deleteBorrow(id: number) {
  return request.delete(`/asset/borrow/${id}`)
}

/**
 * 获取逾期领用列表
 */
export function getOverdueBorrows() {
  return request.get<AssetBorrow[]>('/asset/borrow/overdue')
}

