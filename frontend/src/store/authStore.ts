import { create } from 'zustand'
import { getToken, getUserInfo, getPermissions, setToken, setUserInfo, setPermissions as savePermissions, clearAuth } from '@/utils/auth'

interface UserInfo {
  userId: number
  username: string
  nickName?: string
  avatar?: string
}

interface AuthState {
  token: string | null
  userInfo: UserInfo | null
  permissions: string[]
  setToken: (token: string) => void
  setUserInfo: (userInfo: UserInfo) => void
  setPermissions: (permissions: string[]) => void
  logout: () => void
  hasPermission: (permission: string) => boolean
}

export const useAuthStore = create<AuthState>((set, get) => ({
  token: getToken(),
  userInfo: getUserInfo(),
  permissions: getPermissions(),
  
  setToken: (token: string) => {
    setToken(token)
    set({ token })
  },
  
  setUserInfo: (userInfo: UserInfo) => {
    setUserInfo(userInfo)
    set({ userInfo })
  },
  
  setPermissions: (permissions: string[]) => {
    savePermissions(permissions)
    set({ permissions })
  },
  
  logout: () => {
    clearAuth()
    set({ token: null, userInfo: null, permissions: [] })
  },
  
  hasPermission: (permission: string) => {
    const { permissions } = get()
    return permissions.includes(permission) || permissions.includes('*:*:*')
  },
}))












