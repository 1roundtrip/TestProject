import request from '@/config/api'

export interface MaintenanceTeam {
  teamId?: number
  teamCode?: string
  teamName?: string
  teamType?: string
  leaderId?: number
  leaderName?: string
  memberCount?: number
  specialty?: string
  status?: string
}

export function getTeamPage(params: {
  current: number
  size: number
  teamName?: string
  status?: string
}) {
  return request.get<{ records: MaintenanceTeam[]; total: number }>('/maintenance/team/page', { params })
}

export function createTeam(data: { team: MaintenanceTeam; memberIds?: number[] }) {
  return request.post('/maintenance/team', data)
}

export function updateTeam(team: MaintenanceTeam) {
  return request.put('/maintenance/team', team)
}

export function deleteTeam(teamId: number) {
  return request.delete(`/maintenance/team/${teamId}`)
}

export function addMember(teamId: number, userId: number) {
  return request.post(`/maintenance/team/${teamId}/members`, { userId })
}

export function removeMember(teamId: number, userId: number) {
  return request.delete(`/maintenance/team/${teamId}/members/${userId}`)
}

