-- ============================================
-- 完整权限修复脚本（最终版）
-- ============================================
-- 此脚本将：
-- 1. 确保管理员角色存在
-- 2. 关联用户和角色
-- 3. 为管理员角色分配所有菜单权限
-- 4. 验证配置结果
-- ============================================

USE coal_erp;

-- ============================================
-- 步骤1: 确保管理员角色存在
-- ============================================
INSERT INTO sys_role (role_name, role_key, role_sort, status, create_time, update_time, remark)
VALUES ('Super Admin', 'admin', 1, '0', NOW(), NOW(), '超级管理员')
ON DUPLICATE KEY UPDATE 
    role_name = 'Super Admin', 
    role_key = 'admin',
    status = '0',
    update_time = NOW();

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
-- 先删除管理员角色的所有现有权限（可选，如果需要完全重置）
-- DELETE FROM sys_role_menu WHERE role_id IN (SELECT role_id FROM sys_role WHERE role_key = 'admin');

-- 分配所有有权限标识的菜单（包括所有业务模块）
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT r.role_id, m.menu_id
FROM sys_role r
CROSS JOIN sys_menu m
WHERE r.role_key = 'admin'
  AND m.status = '0'  -- 只分配启用状态的菜单
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
    u.user_id,
    r.role_name,
    r.role_key,
    r.role_id
FROM sys_user u
LEFT JOIN sys_user_role ur ON u.user_id = ur.user_id
LEFT JOIN sys_role r ON ur.role_id = r.role_id
WHERE u.username IN ('admin', 'ceshi');

SELECT '=== 验证2: 角色权限数量 ===' AS verification;
SELECT 
    r.role_name,
    r.role_key,
    COUNT(rm.menu_id) AS permission_count
FROM sys_role r
LEFT JOIN sys_role_menu rm ON r.role_id = rm.role_id
WHERE r.role_key = 'admin'
GROUP BY r.role_id, r.role_name, r.role_key;

SELECT '=== 验证3: 菜单权限统计（按类型） ===' AS verification;
SELECT 
    CASE 
        WHEN perms LIKE 'asset:%' THEN '资产权限'
        WHEN perms LIKE 'purchase:%' THEN '采购权限'
        WHEN perms LIKE 'system:%' THEN '系统权限'
        WHEN perms LIKE 'inventory:%' THEN '库存权限'
        WHEN perms LIKE 'repair:%' THEN '维修权限'
        WHEN perms LIKE 'warning:%' THEN '预警权限'
        WHEN perms LIKE 'dashboard:%' THEN '仪表盘权限'
        ELSE '其他权限'
    END AS permission_type,
    COUNT(*) AS total_menus,
    SUM(CASE WHEN menu_id IN (SELECT menu_id FROM sys_role_menu WHERE role_id IN (SELECT role_id FROM sys_role WHERE role_key = 'admin')) THEN 1 ELSE 0 END) AS assigned_to_admin
FROM sys_menu
WHERE perms IS NOT NULL AND perms != '' AND status = '0'
GROUP BY permission_type
ORDER BY permission_type;

SELECT '=== 验证4: 用户拥有的权限（前20个） ===' AS verification;
SELECT 
    u.username,
    m.perms AS permission,
    m.menu_name,
    m.menu_type
FROM sys_user u
INNER JOIN sys_user_role ur ON u.user_id = ur.user_id
INNER JOIN sys_role_menu rm ON ur.role_id = rm.role_id
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE u.username = 'admin'
  AND m.perms IS NOT NULL
  AND m.perms != ''
ORDER BY m.perms
LIMIT 20;

SELECT '=== 验证5: 采购相关权限统计 ===' AS verification;
SELECT 
    COUNT(*) AS total_purchase_permissions,
    SUM(CASE WHEN menu_id IN (SELECT menu_id FROM sys_role_menu WHERE role_id IN (SELECT role_id FROM sys_role WHERE role_key = 'admin')) THEN 1 ELSE 0 END) AS assigned_to_admin
FROM sys_menu
WHERE perms LIKE 'purchase:%'
  AND perms IS NOT NULL
  AND perms != ''
  AND status = '0';

SELECT '=== 验证6: 关键采购权限检查 ===' AS verification;
SELECT 
    m.perms AS permission,
    m.menu_name,
    CASE WHEN rm.menu_id IS NOT NULL THEN '已分配' ELSE '未分配' END AS status
FROM sys_menu m
LEFT JOIN sys_role_menu rm ON m.menu_id = rm.menu_id 
    AND rm.role_id IN (SELECT role_id FROM sys_role WHERE role_key = 'admin')
WHERE m.perms IN (
    'purchase:order:add',
    'purchase:plan:add',
    'purchase:requisition:add',
    'purchase:supplier:add',
    'purchase:contract:add',
    'purchase:receiving:add',
    'purchase:quality:add',
    'purchase:return:add',
    'purchase:payment:add'
)
ORDER BY m.perms;

SELECT '=== 修复完成！请重启后端服务并重新登录系统 ===' AS final_message;
SELECT '=== 修复完成后，请检查：' AS note;
SELECT '1. 用户是否关联了角色' AS check1;
SELECT '2. 角色是否拥有权限（permission_count > 0）' AS check2;
SELECT '3. 关键采购权限是否已分配' AS check3;
SELECT '4. 重新登录后，前端权限列表应该不为空' AS check4;

