import request from '@/config/api'

export interface InventoryLocation {
  locationId?: number
  warehouseId?: number
  warehouseCode?: string
  warehouseName?: string
  locationCode?: string
  locationName?: string
  locationType?: string
  zone?: string
  aisle?: string
  shelf?: string
  level?: string
  position?: string
  capacity?: number
  capacityUnit?: string
  status?: string
  createTime?: string
  updateTime?: string
  remark?: string
}

export function getLocationPage(params: {
  current: number
  size: number
  warehouseId?: number
  locationCode?: string
  status?: string
}) {
  return request.get('/inventory/location/page', { params })
}

export function createLocation(data: InventoryLocation) {
  return request.post('/inventory/location', data)
}

export function updateLocation(data: InventoryLocation) {
  return request.put('/inventory/location', data)
}

export function deleteLocation(id: number) {
  return request.delete(`/inventory/location/${id}`)
}

export function getLocationById(id: number) {
  return request.get(`/inventory/location/${id}`)
}

