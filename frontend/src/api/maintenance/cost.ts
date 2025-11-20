import request from '@/config/api'

export interface MaintenanceCost {
  costId?: number
  workOrderId?: number
  costType?: string
  costItem?: string
  quantity?: number
  unitPrice?: number
  amount?: number
  costDate?: string
}

export function getCostPage(params: {
  current: number
  size: number
  workOrderId?: number
  costType?: string
}) {
  return request.get<{ records: MaintenanceCost[]; total: number }>('/maintenance/cost/page', { params })
}

export function createCost(cost: MaintenanceCost) {
  return request.post('/maintenance/cost', cost)
}

export function updateCost(cost: MaintenanceCost) {
  return request.put('/maintenance/cost', cost)
}

export function deleteCost(costId: number) {
  return request.delete(`/maintenance/cost/${costId}`)
}

