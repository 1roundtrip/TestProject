-- ============================================
-- 确保系统管理权限完整并分配给超级管理员
-- ============================================
-- 说明：确保所有 system:* 权限都存在，并分配给超级管理员角色

USE coal_erp;

-- 1. 获取超级管理员角色ID
SET @admin_role_id = (
    SELECT role_id FROM sys_role 
    WHERE role_key = 'admin' OR role_name = '超级管理员'
    ORDER BY role_id ASC
    LIMIT 1
);

SELECT CONCAT('超级管理员角色ID: ', @admin_role_id) AS info;

-- 2. 获取系统管理父菜单ID（如果存在）
SET @system_parent_id = (
    SELECT menu_id FROM sys_menu 
    WHERE menu_name = '系统管理' OR path = '/system'
    ORDER BY menu_id ASC
    LIMIT 1
);

-- 如果不存在系统管理父菜单，创建一个
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '系统管理', 0, 99, '/system', NULL, 'M', 'system', 'SettingOutlined', '0', NOW(), NOW(), '系统管理主菜单'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE menu_name = '系统管理' OR path = '/system'
);

SET @system_parent_id = (
    SELECT menu_id FROM sys_menu 
    WHERE menu_name = '系统管理' OR path = '/system'
    ORDER BY menu_id ASC
    LIMIT 1
);

SELECT CONCAT('系统管理父菜单ID: ', @system_parent_id) AS info;

-- 3. 确保用户管理菜单存在
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '用户管理', @system_parent_id, 1, '/system/user', 'System/User', 'C', 'system:user', 'UserOutlined', '0', NOW(), NOW(), '用户管理菜单'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE menu_name = '用户管理' AND parent_id = @system_parent_id
);

SET @user_menu_id = (
    SELECT menu_id FROM sys_menu 
    WHERE menu_name = '用户管理' AND parent_id = @system_parent_id
    LIMIT 1
);

-- 4. 创建用户管理相关的按钮权限（如果不存在）
-- 用户管理-查询
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '用户管理-查询', @user_menu_id, 1, '', NULL, 'F', 'system:user:list', NULL, '0', NOW(), NOW(), '用户查询权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'system:user:list'
);

-- 用户管理-新增
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '用户管理-新增', @user_menu_id, 2, '', NULL, 'F', 'system:user:add', NULL, '0', NOW(), NOW(), '用户新增权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'system:user:add'
);

-- 用户管理-编辑
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '用户管理-编辑', @user_menu_id, 3, '', NULL, 'F', 'system:user:edit', NULL, '0', NOW(), NOW(), '用户编辑权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'system:user:edit'
);

-- 用户管理-删除
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '用户管理-删除', @user_menu_id, 4, '', NULL, 'F', 'system:user:remove', NULL, '0', NOW(), NOW(), '用户删除权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'system:user:remove'
);

-- 5. 确保角色管理菜单存在
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '角色管理', @system_parent_id, 2, '/system/role', 'System/Role', 'C', 'system:role', 'TeamOutlined', '0', NOW(), NOW(), '角色管理菜单'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE menu_name = '角色管理' AND parent_id = @system_parent_id
);

SET @role_menu_id = (
    SELECT menu_id FROM sys_menu 
    WHERE menu_name = '角色管理' AND parent_id = @system_parent_id
    LIMIT 1
);

-- 6. 创建角色管理相关的按钮权限（如果不存在）
-- 角色管理-查询
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '角色管理-查询', @role_menu_id, 1, '', NULL, 'F', 'system:role:list', NULL, '0', NOW(), NOW(), '角色查询权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'system:role:list'
);

-- 角色管理-新增
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '角色管理-新增', @role_menu_id, 2, '', NULL, 'F', 'system:role:add', NULL, '0', NOW(), NOW(), '角色新增权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'system:role:add'
);

-- 角色管理-编辑
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '角色管理-编辑', @role_menu_id, 3, '', NULL, 'F', 'system:role:edit', NULL, '0', NOW(), NOW(), '角色编辑权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'system:role:edit'
);

-- 角色管理-删除
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '角色管理-删除', @role_menu_id, 4, '', NULL, 'F', 'system:role:remove', NULL, '0', NOW(), NOW(), '角色删除权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'system:role:remove'
);

-- 7. 确保菜单管理菜单存在
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '菜单管理', @system_parent_id, 3, '/system/menu', 'System/Menu', 'C', 'system:menu', 'MenuOutlined', '0', NOW(), NOW(), '菜单管理菜单'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE menu_name = '菜单管理' AND parent_id = @system_parent_id
);

SET @menu_menu_id = (
    SELECT menu_id FROM sys_menu 
    WHERE menu_name = '菜单管理' AND parent_id = @system_parent_id
    LIMIT 1
);

-- 8. 创建菜单管理相关的按钮权限（如果不存在）
-- 菜单管理-查询
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '菜单管理-查询', @menu_menu_id, 1, '', NULL, 'F', 'system:menu:list', NULL, '0', NOW(), NOW(), '菜单查询权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'system:menu:list'
);

-- 菜单管理-新增
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '菜单管理-新增', @menu_menu_id, 2, '', NULL, 'F', 'system:menu:add', NULL, '0', NOW(), NOW(), '菜单新增权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'system:menu:add'
);

-- 菜单管理-编辑
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '菜单管理-编辑', @menu_menu_id, 3, '', NULL, 'F', 'system:menu:edit', NULL, '0', NOW(), NOW(), '菜单编辑权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'system:menu:edit'
);

-- 菜单管理-删除
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '菜单管理-删除', @menu_menu_id, 4, '', NULL, 'F', 'system:menu:remove', NULL, '0', NOW(), NOW(), '菜单删除权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'system:menu:remove'
);

-- 9. 为超级管理员角色分配所有 system:* 权限
-- 获取所有 system:* 权限的菜单ID
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT DISTINCT @admin_role_id, m.menu_id
FROM sys_menu m
WHERE m.perms LIKE 'system:%'
AND NOT EXISTS (
    SELECT 1 FROM sys_role_menu rm 
    WHERE rm.role_id = @admin_role_id 
    AND rm.menu_id = m.menu_id
);

-- 10. 验证超级管理员角色拥有的 system:* 权限
SELECT 
    m.menu_name,
    m.perms,
    m.menu_type
FROM sys_menu m
INNER JOIN sys_role_menu rm ON m.menu_id = rm.menu_id
WHERE rm.role_id = @admin_role_id
AND m.perms LIKE 'system:%'
ORDER BY m.menu_id;

-- 11. 验证权限数量
SELECT 
    COUNT(*) AS total_system_permissions,
    COUNT(DISTINCT CASE WHEN m.perms LIKE 'system:user:%' THEN m.menu_id END) AS user_permissions,
    COUNT(DISTINCT CASE WHEN m.perms LIKE 'system:role:%' THEN m.menu_id END) AS role_permissions,
    COUNT(DISTINCT CASE WHEN m.perms LIKE 'system:menu:%' THEN m.menu_id END) AS menu_permissions
FROM sys_menu m
INNER JOIN sys_role_menu rm ON m.menu_id = rm.menu_id
WHERE rm.role_id = @admin_role_id
AND m.perms LIKE 'system:%';

