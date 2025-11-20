import request from '@/config/api'

export interface WarningChannel {
  channelId?: number
  channelCode: string
  channelName: string
  channelType: string
  channelConfig?: string
  isEnabled?: number
  priority?: number
  dailyLimit?: number
  currentCount?: number
  remark?: string
}

export function getChannelPage(params: {
  current: number
  size: number
  channelCode?: string
  channelName?: string
  channelType?: string
}) {
  return request.get('/warning/channel/page', { params })
}

export function getChannelById(id: number) {
  return request.get(`/warning/channel/${id}`)
}

export function updateChannel(data: WarningChannel) {
  return request.put('/warning/channel', data)
}

export function enableChannel(id: number, isEnabled: number) {
  return request.put(`/warning/channel/${id}/enable`, null, { params: { isEnabled } })
}

