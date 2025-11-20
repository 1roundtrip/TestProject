-- ============================================
-- 直接权限修复脚本（不使用变量）
-- ============================================
-- 此脚本直接执行，不依赖MySQL变量
-- ============================================

USE coal_erp;

-- ============================================
-- 步骤1: 确保管理员角色存在
-- ============================================
INSERT INTO sys_role (role_name, role_key, role_sort, status, create_time, update_time, remark)
VALUES ('Super Admin', 'admin', 1, '0', NOW(), NOW(), '超级管理员')
ON DUPLICATE KEY UPDATE role_name = 'Super Admin', role_key = 'admin';

-- ============================================
-- 步骤2: 删除并重新关联用户和角色
-- ============================================
-- 删除admin用户的所有角色关联
DELETE ur FROM sys_user_role ur
INNER JOIN sys_user u ON ur.user_id = u.user_id
WHERE u.username = 'admin';

-- 删除ceshi用户的所有角色关联
DELETE ur FROM sys_user_role ur
INNER JOIN sys_user u ON ur.user_id = u.user_id
WHERE u.username = 'ceshi';

-- 关联admin用户到管理员角色
INSERT INTO sys_user_role (user_id, role_id)
SELECT u.user_id, r.role_id
FROM sys_user u
CROSS JOIN sys_role r
WHERE u.username = 'admin'
  AND r.role_key = 'admin'
  AND NOT EXISTS (
    SELECT 1 FROM sys_user_role ur 
    WHERE ur.user_id = u.user_id AND ur.role_id = r.role_id
  );

-- 关联ceshi用户到管理员角色
INSERT INTO sys_user_role (user_id, role_id)
SELECT u.user_id, r.role_id
FROM sys_user u
CROSS JOIN sys_role r
WHERE u.username = 'ceshi'
  AND r.role_key = 'admin'
  AND NOT EXISTS (
    SELECT 1 FROM sys_user_role ur 
    WHERE ur.user_id = u.user_id AND ur.role_id = r.role_id
  );

-- ============================================
-- 步骤3: 为管理员角色分配所有菜单权限
-- ============================================
-- 分配所有资产权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT r.role_id, m.menu_id
FROM sys_role r
CROSS JOIN sys_menu m
WHERE r.role_key = 'admin'
  AND m.perms LIKE 'asset:%'
  AND NOT EXISTS (
    SELECT 1 FROM sys_role_menu rm 
    WHERE rm.role_id = r.role_id AND rm.menu_id = m.menu_id
  );

-- 分配所有采购权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT r.role_id, m.menu_id
FROM sys_role r
CROSS JOIN sys_menu m
WHERE r.role_key = 'admin'
  AND m.perms LIKE 'purchase:%'
  AND NOT EXISTS (
    SELECT 1 FROM sys_role_menu rm 
    WHERE rm.role_id = r.role_id AND rm.menu_id = m.menu_id
  );

-- 分配其他业务权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT r.role_id, m.menu_id
FROM sys_role r
CROSS JOIN sys_menu m
WHERE r.role_key = 'admin'
  AND (m.perms LIKE 'inventory:%' 
       OR m.perms LIKE 'repair:%' 
       OR m.perms LIKE 'warning:%' 
       OR m.perms LIKE 'system:%' 
       OR m.perms LIKE 'dashboard:%')
  AND NOT EXISTS (
    SELECT 1 FROM sys_role_menu rm 
    WHERE rm.role_id = r.role_id AND rm.menu_id = m.menu_id
  );

-- 如果上面没有分配任何权限，则分配所有状态为0的菜单（备用方案）
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT r.role_id, m.menu_id
FROM sys_role r
CROSS JOIN sys_menu m
WHERE r.role_key = 'admin'
  AND m.status = '0'
  AND m.perms IS NOT NULL
  AND m.perms != ''
  AND NOT EXISTS (
    SELECT 1 FROM sys_role_menu rm 
    WHERE rm.role_id = r.role_id AND rm.menu_id = m.menu_id
  );

-- ============================================
-- 步骤4: 验证结果
-- ============================================
SELECT '=== 验证1: 用户角色关联 ===' AS verification;
SELECT 
    u.username,
    r.role_name,
    r.role_key
FROM sys_user u
LEFT JOIN sys_user_role ur ON u.user_id = ur.user_id
LEFT JOIN sys_role r ON ur.role_id = r.role_id
WHERE u.username IN ('admin', 'ceshi');

SELECT '=== 验证2: 角色权限数量 ===' AS verification;
SELECT 
    r.role_name,
    COUNT(rm.menu_id) AS permission_count
FROM sys_role r
LEFT JOIN sys_role_menu rm ON r.role_id = rm.role_id
WHERE r.role_key = 'admin'
GROUP BY r.role_id, r.role_name;

SELECT '=== 验证3: 菜单权限总数 ===' AS verification;
SELECT 
    CASE 
        WHEN perms LIKE 'asset:%' THEN '资产权限'
        WHEN perms LIKE 'purchase:%' THEN '采购权限'
        WHEN perms LIKE 'system:%' THEN '系统权限'
        ELSE '其他权限'
    END AS permission_type,
    COUNT(*) AS count
FROM sys_menu
WHERE perms IS NOT NULL AND perms != ''
GROUP BY permission_type;

SELECT '=== 验证4: 用户拥有的权限（前10个） ===' AS verification;
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

SELECT '=== 修复完成！请重启后端并重新登录系统 ===' AS final_message;

