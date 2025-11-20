import request from '@/config/api'

export interface WarningHandleRecord {
  handleId?: number
  recordId: number
  handleType?: string
  handlerId: number
  handlerName: string
  handleAction?: string
  handleContent?: string
  handleAttachment?: string
  nextHandlerId?: number
  nextHandlerName?: string
  handleTime?: string
}

export function getHandleRecordPage(params: {
  current: number
  size: number
  recordId?: number
  handlerId?: number
}) {
  return request.get('/warning/tracking/page', { params })
}

export function getHandleRecordById(id: number) {
  return request.get(`/warning/tracking/${id}`)
}

export function createHandleRecord(data: WarningHandleRecord) {
  return request.post('/warning/tracking', data)
}

