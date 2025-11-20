-- ============================================
-- 诊断并修复系统管理权限问题
-- ============================================
-- 说明：先诊断哪些角色拥有系统管理权限，然后彻底清理
-- ============================================

USE coal_erp;

-- ============================================
-- 第一部分：诊断当前状态
-- ============================================

SELECT '第一步：诊断当前系统管理权限分配情况' AS step1;

-- 1. 显示所有拥有系统管理权限的角色
SELECT 
    r.role_id,
    r.role_name,
    r.role_key,
    COUNT(DISTINCT rm.menu_id) AS system_menu_count,
    GROUP_CONCAT(DISTINCT m.menu_name ORDER BY m.menu_name SEPARATOR ', ') AS system_menus
FROM sys_role r
INNER JOIN sys_role_menu rm ON r.role_id = rm.role_id
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE (m.perms LIKE 'system:%' 
       OR m.menu_name = '系统管理'
       OR m.path LIKE '/system%'
       OR m.path = '/system')
GROUP BY r.role_id, r.role_name, r.role_key
ORDER BY r.role_key;

-- 2. 显示系统管理菜单的详细信息
SELECT 
    menu_id,
    menu_name,
    parent_id,
    path,
    perms,
    menu_type,
    status
FROM sys_menu
WHERE menu_name = '系统管理' 
   OR perms LIKE 'system:%'
   OR path LIKE '/system%'
ORDER BY parent_id, menu_id;

-- ============================================
-- 第二部分：获取超级管理员角色ID
-- ============================================

SET @admin_role_id = (
    SELECT role_id FROM sys_role 
    WHERE role_key = 'admin' OR role_name = '超级管理员'
    ORDER BY role_id ASC
    LIMIT 1
);

SELECT CONCAT('超级管理员角色ID: ', @admin_role_id) AS admin_info;

-- 获取系统管理菜单ID
SET @system_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '系统管理' AND parent_id = 0 LIMIT 1);

-- ============================================
-- 第三部分：彻底清理所有非超级管理员的系统管理权限
-- ============================================

SELECT '第二步：开始清理非超级管理员的系统管理权限' AS step2;

-- 方法1：删除所有 system:* 权限标识的菜单权限
DELETE rm FROM sys_role_menu rm
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id != @admin_role_id
  AND rm.role_id IS NOT NULL
  AND m.perms LIKE 'system:%';

SELECT '已删除 system:* 权限' AS result1;

-- 方法2：删除系统管理主菜单及其所有子菜单
DELETE rm FROM sys_role_menu rm
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id != @admin_role_id
  AND rm.role_id IS NOT NULL
  AND (m.menu_name = '系统管理' OR m.parent_id = @system_menu_id);

SELECT '已删除系统管理菜单权限' AS result2;

-- 方法3：删除所有 /system 路径下的菜单权限
DELETE rm FROM sys_role_menu rm
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id != @admin_role_id
  AND rm.role_id IS NOT NULL
  AND (m.path LIKE '/system%' OR m.path = '/system');

SELECT '已删除 /system 路径权限' AS result3;

-- ============================================
-- 第四部分：确保超级管理员拥有所有系统管理权限
-- ============================================

SELECT '第三步：确保超级管理员拥有所有系统管理权限' AS step3;

-- 获取所有系统管理相关的菜单并分配给超级管理员
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT DISTINCT @admin_role_id, m.menu_id
FROM sys_menu m
WHERE (
    m.perms LIKE 'system:%' 
    OR m.menu_name = '系统管理'
    OR m.parent_id = @system_menu_id
    OR m.path LIKE '/system%'
    OR m.path = '/system'
)
  AND (m.status = '0' OR m.status IS NULL)
  AND NOT EXISTS (
    SELECT 1 FROM sys_role_menu rm 
    WHERE rm.role_id = @admin_role_id 
    AND rm.menu_id = m.menu_id
  );

SELECT '已为超级管理员分配系统管理权限' AS result4;

-- ============================================
-- 第五部分：最终验证
-- ============================================

SELECT '第四步：验证清理结果' AS step4;

-- 1. 显示所有角色拥有的系统管理权限数量（应该只有超级管理员有权限）
SELECT 
    r.role_id,
    r.role_name,
    r.role_key,
    COUNT(DISTINCT rm.menu_id) AS system_permission_count,
    CASE 
        WHEN r.role_id = @admin_role_id THEN '✓ 超级管理员（正常）'
        WHEN COUNT(DISTINCT rm.menu_id) = 0 THEN '✓ 已清理（正常）'
        ELSE '✗ 仍有权限（异常）'
    END AS status
FROM sys_role r
LEFT JOIN sys_role_menu rm ON r.role_id = rm.role_id
LEFT JOIN sys_menu m ON rm.menu_id = m.menu_id
  AND (m.perms LIKE 'system:%' 
       OR m.menu_name = '系统管理'
       OR m.parent_id = @system_menu_id
       OR m.path LIKE '/system%')
WHERE r.role_key IN ('admin', 'asset_manager', 'purchase_manager', 'maintenance_manager', 'inventory_manager', 'warning_manager')
GROUP BY r.role_id, r.role_name, r.role_key
ORDER BY r.role_key;

-- 2. 列出所有仍然拥有系统管理权限的非超级管理员角色（应该为空）
SELECT 
    r.role_id,
    r.role_name,
    r.role_key,
    m.menu_name,
    m.perms,
    m.path
FROM sys_role r
INNER JOIN sys_role_menu rm ON r.role_id = rm.role_id
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE r.role_id != @admin_role_id
  AND (m.perms LIKE 'system:%' 
       OR m.menu_name = '系统管理'
       OR m.parent_id = @system_menu_id
       OR m.path LIKE '/system%')
ORDER BY r.role_key, m.menu_id;

-- 3. 显示超级管理员拥有的系统管理权限
SELECT 
    m.menu_name,
    m.perms,
    m.path,
    m.menu_type
FROM sys_menu m
INNER JOIN sys_role_menu rm ON m.menu_id = rm.menu_id
WHERE rm.role_id = @admin_role_id
  AND (m.perms LIKE 'system:%' 
       OR m.menu_name = '系统管理'
       OR m.parent_id = @system_menu_id
       OR m.path LIKE '/system%')
ORDER BY m.menu_id;

-- ============================================
-- 完成信息
-- ============================================

SELECT '系统管理权限清理完成！' AS message;
SELECT '只有超级管理员（admin角色）可以访问系统管理模块' AS result;
SELECT '如果其他用户仍可见系统管理，请检查：' AS note1;
SELECT '1. 用户是否直接关联了超级管理员角色' AS note2;
SELECT '2. 前端是否有缓存，需要刷新页面' AS note3;
SELECT '3. 是否有其他脚本重新分配了权限' AS note4;

