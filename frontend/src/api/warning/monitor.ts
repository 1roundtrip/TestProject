import request from '@/config/api'

export interface WarningRecord {
  recordId?: number
  ruleId?: number
  ruleCode?: string
  ruleName?: string
  warningType: string
  warningCategory?: string
  warningLevelId?: number
  warningLevelCode?: string
  warningLevelName?: string
  warningTitle: string
  warningContent?: string
  warningData?: string
  sourceType?: string
  sourceId?: number
  sourceCode?: string
  sourceName?: string
  triggerTime?: string
  status?: string
  handlerId?: number
  handlerName?: string
  handleTime?: string
  handleResult?: string
  resolveTime?: string
  remark?: string
}

export function getRecordPage(params: {
  current: number
  size: number
  warningType?: string
  warningLevelCode?: string
  status?: string
  sourceType?: string
}) {
  return request.get('/warning/monitor/page', { params })
}

export function getRecordById(id: number) {
  return request.get(`/warning/monitor/${id}`)
}

export function createRecord(data: WarningRecord) {
  return request.post('/warning/monitor', data)
}

export function handleRecord(id: number, data: { handleResult: string; handlerId?: number; handlerName?: string }) {
  return request.post(`/warning/monitor/${id}/handle`, data)
}

export function ignoreRecord(id: number) {
  return request.post(`/warning/monitor/${id}/ignore`)
}

export function closeRecord(id: number) {
  return request.post(`/warning/monitor/${id}/close`)
}

