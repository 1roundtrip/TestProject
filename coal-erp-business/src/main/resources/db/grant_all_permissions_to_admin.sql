-- ============================================
-- 为管理员角色分配所有权限
-- ============================================
-- 说明：此脚本为角色ID为1的管理员角色分配所有资产和采购权限
-- 执行前请确保：
-- 1. 已执行 asset_permission_menu.sql
-- 2. 已执行 purchase_permission_menu.sql
-- 3. 管理员角色ID为1（如果不是，请修改下面的role_id）

USE coal_erp;

-- 为管理员角色分配所有资产权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id 
FROM sys_menu 
WHERE perms LIKE 'asset:%' 
  AND menu_id NOT IN (SELECT menu_id FROM sys_role_menu WHERE role_id = 1)
ON DUPLICATE KEY UPDATE role_id = role_id;

-- 为管理员角色分配所有采购权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id 
FROM sys_menu 
WHERE perms LIKE 'purchase:%' 
  AND menu_id NOT IN (SELECT menu_id FROM sys_role_menu WHERE role_id = 1)
ON DUPLICATE KEY UPDATE role_id = role_id;

-- 为管理员角色分配其他业务权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id 
FROM sys_menu 
WHERE (perms LIKE 'inventory:%' OR perms LIKE 'repair:%' OR perms LIKE 'warning:%')
  AND menu_id NOT IN (SELECT menu_id FROM sys_role_menu WHERE role_id = 1)
ON DUPLICATE KEY UPDATE role_id = role_id;

-- 显示分配结果
SELECT 
    r.role_name,
    COUNT(rm.menu_id) AS permission_count
FROM sys_role r
LEFT JOIN sys_role_menu rm ON r.role_id = rm.role_id
WHERE r.role_id = 1
GROUP BY r.role_id, r.role_name;

-- 显示管理员拥有的所有权限标识
SELECT 
    m.perms AS permission,
    m.menu_name AS menu_name
FROM sys_menu m
INNER JOIN sys_role_menu rm ON m.menu_id = rm.menu_id
WHERE rm.role_id = 1
  AND m.perms IS NOT NULL
  AND m.perms != ''
ORDER BY m.perms;

SELECT '权限分配完成！' AS message;

