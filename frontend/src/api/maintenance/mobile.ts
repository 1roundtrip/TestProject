import request from '@/config/api'

export interface MaintenanceMobileCheckin {
  checkinId?: number
  workOrderId?: number
  technicianId?: number
  checkinType?: string
  checkinTime?: string
  location?: string
  latitude?: number
  longitude?: number
}

export function checkin(data: {
  workOrderId: number
  technicianId: number
  checkinType: string
  location?: string
  latitude?: number
  longitude?: number
}) {
  return request.post('/maintenance/mobile/checkin', data)
}

export function getCheckins(workOrderId: number) {
  return request.get<MaintenanceMobileCheckin[]>(`/maintenance/mobile/${workOrderId}/checkins`)
}

