import request from '@/config/api'

export interface WarningTemplate {
  templateId?: number
  templateCode: string
  templateName: string
  templateType: string
  warningType?: string
  templateSubject?: string
  templateContent: string
  templateVariables?: string
  isDefault?: number
  isEnabled?: number
  remark?: string
}

export function getTemplatePage(params: {
  current: number
  size: number
  templateCode?: string
  templateName?: string
  templateType?: string
}) {
  return request.get('/warning/template/page', { params })
}

export function getTemplateById(id: number) {
  return request.get(`/warning/template/${id}`)
}

export function createTemplate(data: WarningTemplate) {
  return request.post('/warning/template', data)
}

export function updateTemplate(data: WarningTemplate) {
  return request.put('/warning/template', data)
}

export function deleteTemplate(id: number) {
  return request.delete(`/warning/template/${id}`)
}

