import request from '@/config/api'

export interface WarningRule {
  ruleId?: number
  ruleCode: string
  ruleName: string
  ruleType: string
  warningCategory: string
  warningLevelId?: number
  ruleCondition?: string
  ruleExpression?: string
  checkFrequency?: string
  isEnabled?: number
  priority?: number
  remark?: string
}

export function getRulePage(params: {
  current: number
  size: number
  ruleCode?: string
  ruleName?: string
  ruleType?: string
  isEnabled?: number
}) {
  return request.get('/warning/rule/page', { params })
}

export function getRuleById(id: number) {
  return request.get(`/warning/rule/${id}`)
}

export function createRule(data: WarningRule) {
  return request.post('/warning/rule', data)
}

export function updateRule(data: WarningRule) {
  return request.put('/warning/rule', data)
}

export function deleteRule(id: number) {
  return request.delete(`/warning/rule/${id}`)
}

export function enableRule(id: number, isEnabled: number) {
  return request.put(`/warning/rule/${id}/enable`, null, { params: { isEnabled } })
}

