import request from '@/config/api'

export interface SysRole {
  roleId?: number
  roleName: string
  roleKey?: string
  roleSort?: number
  status?: string
  remark?: string
}

export interface SysMenu {
  menuId?: number
  menuName: string
  parentId?: number
  orderNum?: number
  path?: string
  component?: string
  menuType?: string
  perms?: string
  icon?: string
  status?: string
  remark?: string
}

export interface SysUser {
  userId?: number
  username: string
  password?: string
  nickName?: string
  email?: string
  phone?: string
  status?: string
}

export interface PageParams {
  current?: number
  size?: number
  username?: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  current: number
  size: number
}

/**
 * 获取角色列表（分页）
 */
export function getRolePage(params: PageParams) {
  return request.get<PageResult<SysRole>>('/system/role/page', { params })
}

/**
 * 获取菜单列表
 */
export function getMenuList() {
  return request.get<SysMenu[]>('/system/menu/list')
}

/**
 * 获取用户列表（分页）
 */
export function getUserPage(params: PageParams) {
  return request.get<PageResult<SysUser>>('/system/user/page', { params })
}

/**
 * 新增角色
 */
export function addRole(role: SysRole) {
  return request.post('/system/role', role)
}

/**
 * 更新角色
 */
export function updateRole(role: SysRole) {
  return request.put('/system/role', role)
}

/**
 * 删除角色
 */
export function deleteRole(roleId: number) {
  return request.delete(`/system/role/${roleId}`)
}

/**
 * 新增用户
 */
export function addUser(user: SysUser) {
  return request.post('/system/user', user)
}

/**
 * 更新用户
 */
export function updateUser(user: SysUser) {
  return request.put('/system/user', user)
}

/**
 * 删除用户
 */
export function deleteUser(userId: number) {
  return request.delete(`/system/user/${userId}`)
}

/**
 * 获取用户角色列表
 */
export function getUserRoles(userId: number) {
  return request.get<number[]>(`/system/user/${userId}/roles`)
}

/**
 * 分配用户角色
 */
export function assignUserRoles(userId: number, roleIds: number[]) {
  return request.post(`/system/user/${userId}/roles`, { roleIds })
}

/**
 * 新增菜单
 */
export function addMenu(menu: SysMenu) {
  return request.post('/system/menu', menu)
}

/**
 * 更新菜单
 */
export function updateMenu(menu: SysMenu) {
  return request.put('/system/menu', menu)
}

/**
 * 删除菜单
 */
export function deleteMenu(menuId: number) {
  return request.delete(`/system/menu/${menuId}`)
}

