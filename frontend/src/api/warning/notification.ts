import request from '@/config/api'

export interface WarningNotification {
  notificationId?: number
  recordId: number
  channelType: string
  recipientId?: number
  recipientName?: string
  recipientEmail?: string
  recipientPhone?: string
  notificationTitle: string
  notificationContent?: string
  templateId?: number
  sendStatus?: string
  sendTime?: string
  sendResult?: string
  retryCount?: number
}

export function getNotificationPage(params: {
  current: number
  size: number
  recordId?: number
  channelType?: string
  sendStatus?: string
}) {
  return request.get('/warning/notification/page', { params })
}

export function createNotification(data: WarningNotification) {
  return request.post('/warning/notification', data)
}

export function resendNotification(id: number) {
  return request.post(`/warning/notification/${id}/resend`)
}

