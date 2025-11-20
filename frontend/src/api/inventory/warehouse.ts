import request from '@/config/api'

export interface InventoryWarehouse {
  warehouseId?: number
  warehouseCode?: string
  warehouseName?: string
  warehouseType?: string
  location?: string
  managerId?: number
  managerName?: string
  contactPhone?: string
  area?: number
  capacity?: number
  capacityUnit?: string
  status?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export function getWarehousePage(params: {
  current: number
  size: number
  warehouseCode?: string
  warehouseName?: string
  status?: string
}) {
  return request.get('/inventory/warehouse/page', { params })
}

export function createWarehouse(data: InventoryWarehouse) {
  return request.post('/inventory/warehouse', data)
}

export function updateWarehouse(data: InventoryWarehouse) {
  return request.put('/inventory/warehouse', data)
}

export function deleteWarehouse(id: number) {
  return request.delete(`/inventory/warehouse/${id}`)
}

export function getWarehouseById(id: number) {
  return request.get(`/inventory/warehouse/${id}`)
}

