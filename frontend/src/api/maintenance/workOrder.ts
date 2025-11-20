import request from '@/config/api'

export interface MaintenanceWorkOrder {
  workOrderId?: number
  workOrderNo?: string
  workOrderType?: string
  priority?: string
  assetId?: number
  assetCode?: string
  assetName?: string
  faultType?: string
  faultDescription?: string
  reportedBy?: number
  reportedByName?: string
  reportedTime?: string
  assignedTeamId?: number
  assignedTeamName?: string
  assignedTechnicianId?: number
  assignedTechnicianName?: string
  scheduledStartTime?: string
  scheduledEndTime?: string
  actualStartTime?: string
  actualEndTime?: string
  status?: string
  completionRate?: number
  laborCost?: number
  materialCost?: number
  totalCost?: number
  qualityScore?: number
  qualityComment?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export interface MaintenanceWorkOrderDetail {
  detailId?: number
  workOrderId?: number
  stepNo?: number
  stepName?: string
  stepDescription?: string
  technicianId?: number
  technicianName?: string
  startTime?: string
  endTime?: string
  duration?: number
  status?: string
  remark?: string
}

/**
 * 分页查询维修工单
 */
export function getWorkOrderPage(params: {
  current: number
  size: number
  workOrderNo?: string
  status?: string
  assetId?: number
}) {
  return request.get<{
    records: MaintenanceWorkOrder[]
    total: number
  }>('/maintenance/work-order/page', { params })
}

/**
 * 创建维修工单
 */
export function createWorkOrder(data: {
  workOrder: MaintenanceWorkOrder
  details?: MaintenanceWorkOrderDetail[]
}) {
  return request.post('/maintenance/work-order', data)
}

/**
 * 更新维修工单
 */
export function updateWorkOrder(workOrder: MaintenanceWorkOrder) {
  return request.put('/maintenance/work-order', workOrder)
}

/**
 * 删除维修工单
 */
export function deleteWorkOrder(workOrderId: number) {
  return request.delete(`/maintenance/work-order/${workOrderId}`)
}

/**
 * 获取工单详情
 */
export function getWorkOrderById(workOrderId: number) {
  return request.get<MaintenanceWorkOrder>(`/maintenance/work-order/${workOrderId}`)
}

/**
 * 获取工单明细
 */
export function getWorkOrderDetails(workOrderId: number) {
  return request.get<MaintenanceWorkOrderDetail[]>(`/maintenance/work-order/${workOrderId}/details`)
}

/**
 * 分配工单
 */
export function assignWorkOrder(workOrderId: number, data: {
  teamId?: number
  technicianId?: number
}) {
  return request.post(`/maintenance/work-order/${workOrderId}/assign`, data)
}

/**
 * 开始维修
 */
export function startWorkOrder(workOrderId: number) {
  return request.post(`/maintenance/work-order/${workOrderId}/start`)
}

/**
 * 完成工单
 */
export function completeWorkOrder(workOrderId: number, data: {
  qualityComment?: string
  qualityScore?: number
}) {
  return request.post(`/maintenance/work-order/${workOrderId}/complete`, data)
}

