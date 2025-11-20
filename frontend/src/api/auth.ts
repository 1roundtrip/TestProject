import request from '@/config/api'

export interface LoginParams {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  userInfo?: {
    userId: number
    username: string
    nickName?: string
    avatar?: string
  }
  permissions?: string[]
}

/**
 * 登录
 */
export function login(params: LoginParams) {
  return request.post<LoginResponse>('/auth/login', params)
}

/**
 * 登出
 */
export function logout() {
  return request.post('/auth/logout')
}

export interface ChangePasswordParams {
  userId: number
  oldPassword: string
  newPassword: string
}

/**
 * 修改密码
 */
export function changePassword(params: ChangePasswordParams) {
  return request.post('/system/user/change-password', params)
}





