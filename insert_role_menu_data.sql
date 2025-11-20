-- 智慧煤矿ERP系统 - 用户角色和菜单测试数据脚本
-- 注意：此脚本会先清理现有数据，然后重新插入

-- ============================================
-- 1. 清理现有数据（可选，谨慎使用）
-- ============================================
-- 删除关联数据
DELETE FROM sys_user_role;
DELETE FROM sys_role_menu;
-- 删除菜单数据
DELETE FROM sys_menu;
-- 删除角色数据
DELETE FROM sys_role;

-- ============================================
-- 2. 插入角色数据
-- ============================================
INSERT INTO `sys_role` (`role_name`, `role_key`, `role_sort`, `status`, `create_time`, `remark`) VALUES
('超级管理员', 'admin', 1, '0', NOW(), '拥有所有权限'),
('系统管理员', 'system', 2, '0', NOW(), '系统管理权限'),
('资产管理员', 'asset', 3, '0', NOW(), '资产管理权限'),
('普通用户', 'user', 4, '0', NOW(), '普通用户权限');

-- ============================================
-- 3. 插入一级菜单
-- ============================================
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `remark`) VALUES
('仪表盘', 0, 1, '/dashboard', 'Dashboard', 'C', 'dashboard:view', 'DashboardOutlined', '0', NOW(), '仪表盘'),
('资产中心', 0, 2, '/asset', 'Asset', 'C', 'asset:list', 'DatabaseOutlined', '0', NOW(), '资产中心'),
('采购中心', 0, 3, '/purchase', 'Purchase', 'C', 'purchase:list', 'ShoppingCartOutlined', '0', NOW(), '采购中心'),
('维修中心', 0, 4, '/repair', 'Repair', 'C', 'repair:list', 'ToolOutlined', '0', NOW(), '维修中心'),
('库存中心', 0, 5, '/inventory', 'Inventory', 'C', 'inventory:list', 'InboxOutlined', '0', NOW(), '库存中心'),
('预警中心', 0, 6, '/warning', 'Warning', 'C', 'warning:list', 'WarningOutlined', '0', NOW(), '预警中心'),
('系统管理', 0, 7, '/system', NULL, 'M', 'system:view', 'SettingOutlined', '0', NOW(), '系统管理');

-- ============================================
-- 4. 插入二级菜单（系统管理下的菜单）
-- ============================================
SET @system_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '系统管理' LIMIT 1);

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `remark`) VALUES
('用户管理', @system_menu_id, 1, '/system/user', 'System/User', 'C', 'system:user:list', 'UserOutlined', '0', NOW(), '用户管理'),
('角色管理', @system_menu_id, 2, '/system/role', 'System/Role', 'C', 'system:role:list', 'TeamOutlined', '0', NOW(), '角色管理'),
('菜单管理', @system_menu_id, 3, '/system/menu', 'System/Menu', 'C', 'system:menu:list', 'MenuOutlined', '0', NOW(), '菜单管理');

-- ============================================
-- 5. 插入按钮权限
-- ============================================
-- 资产中心按钮权限
SET @asset_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '资产中心' LIMIT 1);
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `remark`) VALUES
('资产新增', @asset_menu_id, 1, NULL, NULL, 'F', 'asset:add', NULL, '0', NOW(), '资产新增'),
('资产编辑', @asset_menu_id, 2, NULL, NULL, 'F', 'asset:edit', NULL, '0', NOW(), '资产编辑'),
('资产删除', @asset_menu_id, 3, NULL, NULL, 'F', 'asset:remove', NULL, '0', NOW(), '资产删除'),
('资产查询', @asset_menu_id, 4, NULL, NULL, 'F', 'asset:query', NULL, '0', NOW(), '资产查询');

-- 预警中心按钮权限
SET @warning_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '预警中心' LIMIT 1);
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `remark`) VALUES
('预警处理', @warning_menu_id, 1, NULL, NULL, 'F', 'warning:handle', NULL, '0', NOW(), '预警处理');

-- 维修中心按钮权限
SET @repair_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '维修中心' LIMIT 1);
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `remark`) VALUES
('维修新增', @repair_menu_id, 1, NULL, NULL, 'F', 'repair:add', NULL, '0', NOW(), '维修新增');

-- 采购中心按钮权限
SET @purchase_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '采购中心' LIMIT 1);
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `remark`) VALUES
('采购新增', @purchase_menu_id, 1, NULL, NULL, 'F', 'purchase:add', NULL, '0', NOW(), '采购新增');

-- 库存中心按钮权限
SET @inventory_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '库存中心' LIMIT 1);
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `remark`) VALUES
('库存新增', @inventory_menu_id, 1, NULL, NULL, 'F', 'inventory:add', NULL, '0', NOW(), '库存新增');

-- 用户管理按钮权限
SET @user_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '用户管理' LIMIT 1);
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `remark`) VALUES
('用户新增', @user_menu_id, 1, NULL, NULL, 'F', 'system:user:add', NULL, '0', NOW(), '用户新增'),
('用户编辑', @user_menu_id, 2, NULL, NULL, 'F', 'system:user:edit', NULL, '0', NOW(), '用户编辑'),
('用户删除', @user_menu_id, 3, NULL, NULL, 'F', 'system:user:remove', NULL, '0', NOW(), '用户删除');

-- 角色管理按钮权限
SET @role_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '角色管理' LIMIT 1);
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `remark`) VALUES
('角色新增', @role_menu_id, 1, NULL, NULL, 'F', 'system:role:add', NULL, '0', NOW(), '角色新增');

-- 菜单管理按钮权限
SET @menu_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '菜单管理' LIMIT 1);
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `remark`) VALUES
('菜单新增', @menu_menu_id, 1, NULL, NULL, 'F', 'system:menu:add', NULL, '0', NOW(), '菜单新增');

-- ============================================
-- 6. 为超级管理员角色分配所有菜单权限
-- ============================================
SET @admin_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'admin' LIMIT 1);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT @admin_role_id, menu_id
FROM sys_menu
WHERE status = '0';

-- ============================================
-- 7. 为admin用户分配超级管理员角色
-- ============================================
SET @admin_user_id = (SELECT user_id FROM sys_user WHERE username = 'admin' LIMIT 1);
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (@admin_user_id, @admin_role_id);

-- ============================================
-- 8. 验证数据
-- ============================================
SELECT '=== 角色数据统计 ===' AS info;
SELECT role_id, role_name, role_key, status FROM sys_role ORDER BY role_sort;

SELECT '=== 菜单数据统计 ===' AS info;
SELECT menu_id, menu_name, parent_id, menu_type, perms, status FROM sys_menu ORDER BY parent_id, order_num;

SELECT '=== 角色菜单关联统计 ===' AS info;
SELECT r.role_name, COUNT(rm.menu_id) AS menu_count
FROM sys_role r
LEFT JOIN sys_role_menu rm ON r.role_id = rm.role_id
GROUP BY r.role_id, r.role_name
ORDER BY r.role_sort;

SELECT '=== 用户角色关联统计 ===' AS info;
SELECT u.username, r.role_name, r.role_key
FROM sys_user u
LEFT JOIN sys_user_role ur ON u.user_id = ur.user_id
LEFT JOIN sys_role r ON ur.role_id = r.role_id
WHERE u.username = 'admin';

SELECT '=== 数据插入完成 ===' AS info;
SELECT 
    (SELECT COUNT(*) FROM sys_role) AS role_count,
    (SELECT COUNT(*) FROM sys_menu) AS menu_count,
    (SELECT COUNT(*) FROM sys_role_menu) AS role_menu_count,
    (SELECT COUNT(*) FROM sys_user_role) AS user_role_count;
