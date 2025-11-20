import request from '@/config/api'

export interface InventoryMaterial {
  materialId?: number
  materialCode?: string
  materialName?: string
  materialType?: string
  category?: string
  specification?: string
  brand?: string
  manufacturer?: string
  unit?: string
  unitPrice?: number
  currency?: string
  minStock?: number
  maxStock?: number
  safetyStock?: number
  reorderPoint?: number
  reorderQuantity?: number
  shelfLife?: number
  storageCondition?: string
  status?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export function getMaterialPage(params: {
  current: number
  size: number
  materialCode?: string
  materialName?: string
  materialType?: string
  status?: string
}) {
  return request.get('/inventory/material/page', { params })
}

export function createMaterial(data: InventoryMaterial) {
  return request.post('/inventory/material', data)
}

export function updateMaterial(data: InventoryMaterial) {
  return request.put('/inventory/material', data)
}

export function deleteMaterial(id: number) {
  return request.delete(`/inventory/material/${id}`)
}

export function getMaterialById(id: number) {
  return request.get(`/inventory/material/${id}`)
}

