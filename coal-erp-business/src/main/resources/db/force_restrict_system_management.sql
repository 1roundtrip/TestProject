-- ============================================
-- 强制限制系统管理模块仅超级管理员可见（强力版）
-- ============================================
-- 说明：彻底从所有非超级管理员角色中移除系统管理相关权限
-- 包括所有可能的权限标识和菜单
-- ============================================

USE coal_erp;

-- ============================================
-- 第一部分：获取超级管理员角色ID
-- ============================================

SET @admin_role_id = (
    SELECT role_id FROM sys_role 
    WHERE role_key = 'admin' OR role_name = '超级管理员'
    ORDER BY role_id ASC
    LIMIT 1
);

SELECT CONCAT('超级管理员角色ID: ', @admin_role_id) AS info;

-- ============================================
-- 第二部分：获取所有系统管理相关的菜单ID
-- ============================================

-- 获取系统管理主菜单ID
SET @system_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '系统管理' AND parent_id = 0 LIMIT 1);

-- 如果系统管理菜单不存在，创建它
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '系统管理', 0, 99, '/system', NULL, 'M', 'system', 'SettingOutlined', '0', NOW(), NOW(), '系统管理主菜单'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE menu_name = '系统管理' AND parent_id = 0
);

SET @system_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '系统管理' AND parent_id = 0 LIMIT 1);

SELECT CONCAT('系统管理菜单ID: ', @system_menu_id) AS info;

-- ============================================
-- 第三部分：从所有非超级管理员角色中移除系统管理权限
-- ============================================

-- 方法1：通过权限标识删除（system:*）
DELETE rm FROM sys_role_menu rm
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id != @admin_role_id
  AND rm.role_id IS NOT NULL
  AND m.perms LIKE 'system:%';

-- 方法2：通过菜单名称删除（系统管理及其子菜单）
DELETE rm FROM sys_role_menu rm
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id != @admin_role_id
  AND rm.role_id IS NOT NULL
  AND (m.menu_name = '系统管理' OR m.parent_id = @system_menu_id);

-- 方法3：通过路径删除（/system路径下的所有菜单）
DELETE rm FROM sys_role_menu rm
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id != @admin_role_id
  AND rm.role_id IS NOT NULL
  AND (m.path LIKE '/system%' OR m.path = '/system');

-- 方法4：通过父菜单ID删除（确保所有子菜单都被移除）
DELETE rm FROM sys_role_menu rm
WHERE rm.role_id != @admin_role_id
  AND rm.role_id IS NOT NULL
  AND rm.menu_id IN (
    SELECT menu_id FROM sys_menu 
    WHERE parent_id = @system_menu_id
  );

-- 方法5：删除系统管理主菜单本身（如果不是超级管理员）
DELETE rm FROM sys_role_menu rm
WHERE rm.role_id != @admin_role_id
  AND rm.role_id IS NOT NULL
  AND rm.menu_id = @system_menu_id;

-- ============================================
-- 第四部分：确保超级管理员拥有所有系统管理权限
-- ============================================

-- 获取所有系统管理相关的菜单（包括主菜单和所有子菜单）
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

-- ============================================
-- 第五部分：验证和显示结果
-- ============================================

-- 1. 显示所有角色拥有的系统管理权限数量
SELECT 
    r.role_id,
    r.role_name,
    r.role_key,
    COUNT(rm.menu_id) AS system_permission_count
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

-- 2. 显示所有拥有系统管理权限的角色（应该只有超级管理员）
SELECT 
    r.role_id,
    r.role_name,
    r.role_key,
    COUNT(rm.menu_id) AS system_permission_count
FROM sys_role r
INNER JOIN sys_role_menu rm ON r.role_id = rm.role_id
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE (m.perms LIKE 'system:%' 
       OR m.menu_name = '系统管理'
       OR m.parent_id = @system_menu_id
       OR m.path LIKE '/system%')
GROUP BY r.role_id, r.role_name, r.role_key
HAVING COUNT(rm.menu_id) > 0
ORDER BY r.role_key;

-- 3. 显示超级管理员拥有的系统管理权限列表
SELECT 
    m.menu_id,
    m.menu_name,
    m.perms,
    m.path,
    m.menu_type,
    CASE 
        WHEN m.menu_type = 'M' THEN '目录'
        WHEN m.menu_type = 'C' THEN '菜单'
        WHEN m.menu_type = 'F' THEN '按钮'
        ELSE '其他'
    END AS menu_type_name
FROM sys_menu m
INNER JOIN sys_role_menu rm ON m.menu_id = rm.menu_id
WHERE rm.role_id = @admin_role_id
  AND (m.perms LIKE 'system:%' 
       OR m.menu_name = '系统管理'
       OR m.parent_id = @system_menu_id
       OR m.path LIKE '/system%')
ORDER BY m.menu_id;

-- 4. 检查是否有其他角色仍然拥有系统管理权限
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

-- 5. 显示配置完成信息
SELECT '============================================' AS separator;
SELECT '系统管理权限强制限制配置完成！' AS message;
SELECT '============================================' AS separator;
SELECT '只有超级管理员（admin角色）可以访问系统管理模块' AS restriction_info;
SELECT '所有其他角色已彻底移除系统管理权限' AS cleanup_info;
SELECT '如果仍有其他角色可见系统管理，请检查是否有其他脚本重新分配了权限' AS note;

