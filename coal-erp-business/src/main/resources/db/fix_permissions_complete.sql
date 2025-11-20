-- ============================================
-- 完整权限修复脚本
-- ============================================
-- 此脚本将：
-- 1. 确保菜单权限已初始化
-- 2. 确保用户关联到角色
-- 3. 确保角色拥有所有权限
-- ============================================

USE coal_erp;

-- ============================================
-- 步骤1: 确保管理员角色存在
-- ============================================
INSERT INTO sys_role (role_name, role_key, role_sort, status, create_time, update_time, remark)
SELECT 'Super Admin', 'admin', 1, '0', NOW(), NOW(), '超级管理员，拥有所有权限'
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE role_key = 'admin' OR role_name = 'Super Admin');

-- 获取管理员角色ID
SET @admin_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'admin' OR role_name = 'Super Admin' LIMIT 1);

SELECT CONCAT('管理员角色ID: ', @admin_role_id) AS info;

-- ============================================
-- 步骤2: 确保admin用户关联到管理员角色
-- ============================================
-- 获取admin用户ID
SET @admin_user_id = (SELECT user_id FROM sys_user WHERE username = 'admin' LIMIT 1);

-- 关联用户和角色
INSERT INTO sys_user_role (user_id, role_id)
SELECT @admin_user_id, @admin_role_id
WHERE @admin_user_id IS NOT NULL 
  AND @admin_role_id IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM sys_user_role 
    WHERE user_id = @admin_user_id AND role_id = @admin_role_id
  );

-- 如果ceshi用户存在，也关联到管理员角色
SET @ceshi_user_id = (SELECT user_id FROM sys_user WHERE username = 'ceshi' LIMIT 1);
INSERT INTO sys_user_role (user_id, role_id)
SELECT @ceshi_user_id, @admin_role_id
WHERE @ceshi_user_id IS NOT NULL 
  AND @admin_role_id IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM sys_user_role 
    WHERE user_id = @ceshi_user_id AND role_id = @admin_role_id
  );

-- ============================================
-- 步骤3: 为管理员角色分配所有菜单权限
-- ============================================
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

-- 如果菜单权限表为空，分配所有菜单（作为备用方案）
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @admin_role_id, menu_id 
FROM sys_menu 
WHERE status = '0'
  AND menu_id NOT IN (SELECT menu_id FROM sys_role_menu WHERE role_id = @admin_role_id)
LIMIT 1000;

-- ============================================
-- 步骤4: 验证结果
-- ============================================
SELECT '=== 用户角色关联 ===' AS info;
SELECT 
    u.username,
    r.role_name,
    r.role_key
FROM sys_user u
LEFT JOIN sys_user_role ur ON u.user_id = ur.user_id
LEFT JOIN sys_role r ON ur.role_id = r.role_id
WHERE u.username IN ('admin', 'ceshi');

SELECT '=== 角色权限统计 ===' AS info;
SELECT 
    r.role_name,
    COUNT(rm.menu_id) AS permission_count
FROM sys_role r
LEFT JOIN sys_role_menu rm ON r.role_id = rm.role_id
WHERE r.role_id = @admin_role_id
GROUP BY r.role_id, r.role_name;

SELECT '=== 用户拥有的权限示例（前20个） ===' AS info;
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
LIMIT 20;

SELECT '=== 菜单权限统计 ===' AS info;
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

SELECT '权限修复完成！请重新登录系统。' AS message;

