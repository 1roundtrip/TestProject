-- ============================================
-- 简单直接的权限修复脚本
-- ============================================
-- 此脚本将强制修复用户角色关联和权限分配
-- ============================================

USE coal_erp;

-- ============================================
-- 步骤1: 确保管理员角色存在（如果不存在则创建）
-- ============================================
INSERT INTO sys_role (role_name, role_key, role_sort, status, create_time, update_time, remark)
VALUES ('Super Admin', 'admin', 1, '0', NOW(), NOW(), '超级管理员，拥有所有权限')
ON DUPLICATE KEY UPDATE role_name = 'Super Admin', role_key = 'admin';

-- 获取管理员角色ID
SET @admin_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'admin' LIMIT 1);

SELECT CONCAT('管理员角色ID: ', @admin_role_id) AS step1_result;

-- ============================================
-- 步骤2: 强制关联用户和角色（先删除旧关联，再创建新关联）
-- ============================================
-- 删除admin用户的所有旧角色关联
DELETE FROM sys_user_role WHERE user_id = (SELECT user_id FROM sys_user WHERE username = 'admin' LIMIT 1);

-- 删除ceshi用户的所有旧角色关联
DELETE FROM sys_user_role WHERE user_id = (SELECT user_id FROM sys_user WHERE username = 'ceshi' LIMIT 1);

-- 关联admin用户到管理员角色
INSERT INTO sys_user_role (user_id, role_id)
SELECT u.user_id, @admin_role_id
FROM sys_user u
WHERE u.username = 'admin'
  AND @admin_role_id IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM sys_user_role ur 
    WHERE ur.user_id = u.user_id AND ur.role_id = @admin_role_id
  );

-- 关联ceshi用户到管理员角色
INSERT INTO sys_user_role (user_id, role_id)
SELECT u.user_id, @admin_role_id
FROM sys_user u
WHERE u.username = 'ceshi'
  AND @admin_role_id IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM sys_user_role ur 
    WHERE ur.user_id = u.user_id AND ur.role_id = @admin_role_id
  );

SELECT '用户角色关联完成' AS step2_result;

-- ============================================
-- 步骤3: 为管理员角色分配所有菜单权限
-- ============================================
-- 先删除该角色的所有旧权限（可选，如果想重新分配）
-- DELETE FROM sys_role_menu WHERE role_id = @admin_role_id;

-- 分配所有资产权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @admin_role_id, menu_id 
FROM sys_menu 
WHERE perms LIKE 'asset:%'
  AND menu_id NOT IN (SELECT menu_id FROM sys_role_menu WHERE role_id = @admin_role_id);

-- 分配所有采购权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @admin_role_id, menu_id 
FROM sys_menu 
WHERE perms LIKE 'purchase:%'
  AND menu_id NOT IN (SELECT menu_id FROM sys_role_menu WHERE role_id = @admin_role_id);

-- 分配其他业务权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @admin_role_id, menu_id 
FROM sys_menu 
WHERE (perms LIKE 'inventory:%' OR perms LIKE 'repair:%' OR perms LIKE 'warning:%' OR perms LIKE 'system:%' OR perms LIKE 'dashboard:%')
  AND menu_id NOT IN (SELECT menu_id FROM sys_role_menu WHERE role_id = @admin_role_id);

-- 如果上面的权限分配后仍然为空，则分配所有状态为0的菜单（备用方案）
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @admin_role_id, menu_id 
FROM sys_menu 
WHERE status = '0'
  AND menu_id NOT IN (SELECT menu_id FROM sys_role_menu WHERE role_id = @admin_role_id);

SELECT '权限分配完成' AS step3_result;

-- ============================================
-- 步骤4: 验证结果
-- ============================================
SELECT '=== 验证：用户角色关联 ===' AS verification;
SELECT 
    u.username,
    r.role_name,
    r.role_key
FROM sys_user u
LEFT JOIN sys_user_role ur ON u.user_id = ur.user_id
LEFT JOIN sys_role r ON ur.role_id = r.role_id
WHERE u.username IN ('admin', 'ceshi');

SELECT '=== 验证：角色权限数量 ===' AS verification;
SELECT 
    r.role_name,
    COUNT(rm.menu_id) AS permission_count
FROM sys_role r
LEFT JOIN sys_role_menu rm ON r.role_id = rm.role_id
WHERE r.role_id = @admin_role_id
GROUP BY r.role_id, r.role_name;

SELECT '=== 验证：用户拥有的权限（前10个） ===' AS verification;
SELECT 
    u.username,
    m.perms AS permission,
    m.menu_name
FROM sys_user u
INNER JOIN sys_user_role ur ON u.user_id = ur.user_id
INNER JOIN sys_role_menu rm ON ur.role_id = rm.role_id
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE u.username = 'admin'
  AND m.perms IS NOT NULL
  AND m.perms != ''
ORDER BY m.perms
LIMIT 10;

SELECT '=== 修复完成！请重启后端并重新登录 ===' AS final_message;

