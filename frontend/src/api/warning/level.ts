import request from '@/config/api'

export interface WarningLevel {
  levelId?: number
  levelCode: string
  levelName: string
  levelColor?: string
  levelOrder?: number
  notificationChannels?: string
  escalationRule?: string
  isEnabled?: number
  remark?: string
}

export function getLevelPage(params: {
  current: number
  size: number
  levelCode?: string
  levelName?: string
}) {
  return request.get('/warning/level/page', { params })
}

export function getLevelById(id: number) {
  return request.get(`/warning/level/${id}`)
}

export function createLevel(data: WarningLevel) {
  return request.post('/warning/level', data)
}

export function updateLevel(data: WarningLevel) {
  return request.put('/warning/level', data)
}

export function deleteLevel(id: number) {
  return request.delete(`/warning/level/${id}`)
}

