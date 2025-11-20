import { ReactNode } from 'react'
import { useAuthStore } from '@/store/authStore'

interface PermissionProps {
  permission: string
  children: ReactNode
  fallback?: ReactNode
}

/**
 * 权限控制组件
 */
export function Permission({ permission, children, fallback = null }: PermissionProps) {
  const hasPermission = useAuthStore((state) => state.hasPermission(permission))
  const permissions = useAuthStore((state) => state.permissions)
  
  // 临时解决方案：如果权限列表为空，显示按钮（开发模式）
  // 生产环境应该移除这个逻辑，确保权限正确配置
  const shouldShow = hasPermission || permissions.length === 0
  
  return shouldShow ? <>{children}</> : <>{fallback}</>
}

/**
 * 权限指令（用于按钮等）
 */
export function vPermission(permission: string): boolean {
  const { hasPermission } = useAuthStore.getState()
  return hasPermission(permission)
}












