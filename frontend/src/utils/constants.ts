/**
 * 统一常量定义
 */

// 业务中心枚举
export enum BusinessCenter {
  ASSET = 'asset',
  PURCHASE = 'purchase',
  MAINTENANCE = 'maintenance',
  INVENTORY = 'inventory',
  WARNING = 'warning',
  FINANCE = 'finance',
}

// 标准操作枚举
export enum StandardAction {
  LIST = 'list',
  ADD = 'add',
  EDIT = 'edit',
  REMOVE = 'remove',
  VIEW = 'view',
}

// 标准状态枚举
export enum StandardStatus {
  DRAFT = 'DRAFT',
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  PROCESSING = 'PROCESSING',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
  REJECTED = 'REJECTED',
}

// 分页默认值
export const DEFAULT_PAGE_SIZE = 10
export const DEFAULT_CURRENT = 1

// API路径前缀
export const API_PREFIX = '/api'

// 权限前缀
export const PERMISSION_PREFIX = {
  [BusinessCenter.ASSET]: 'asset',
  [BusinessCenter.PURCHASE]: 'purchase',
  [BusinessCenter.MAINTENANCE]: 'maintenance',
  [BusinessCenter.INVENTORY]: 'inventory',
  [BusinessCenter.WARNING]: 'warning',
  [BusinessCenter.FINANCE]: 'finance',
}

/**
 * 生成权限字符串
 */
export function generatePermission(
  center: BusinessCenter,
  module: string,
  action: StandardAction | string
): string {
  return `${center}:${module}:${action}`
}

/**
 * 生成API路径
 */
export function generateApiPath(center: BusinessCenter, module: string): string {
  return `${API_PREFIX}/${center}/${module}`
}

