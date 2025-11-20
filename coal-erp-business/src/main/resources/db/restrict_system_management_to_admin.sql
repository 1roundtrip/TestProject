-- ============================================
-- 限制系统管理模块仅超级管理员可见
-- ============================================
-- 说明：从所有非超级管理员角色中移除系统管理相关权限
-- 确保只有超级管理员（admin角色）可以访问系统管理模块
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
-- 第二部分：从所有非超级管理员角色中移除系统管理权限
-- ============================================

-- 获取系统管理菜单ID（包括主菜单和所有子菜单）
SET @system_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '系统管理' AND parent_id = 0 LIMIT 1);

-- 1. 从资产中心负责人角色中移除系统管理权限
SET @asset_manager_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'asset_manager' LIMIT 1);
DELETE rm FROM sys_role_menu rm
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = @asset_manager_role_id
  AND (m.perms LIKE 'system:%' 
       OR m.menu_name = '系统管理'
       OR m.parent_id = @system_menu_id);

-- 2. 从采购中心负责人角色中移除系统管理权限
SET @purchase_manager_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'purchase_manager' LIMIT 1);
DELETE rm FROM sys_role_menu rm
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = @purchase_manager_role_id
  AND (m.perms LIKE 'system:%' 
       OR m.menu_name = '系统管理'
       OR m.parent_id = @system_menu_id);

-- 3. 从维修管理负责人角色中移除系统管理权限
SET @maintenance_manager_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'maintenance_manager' LIMIT 1);
DELETE rm FROM sys_role_menu rm
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = @maintenance_manager_role_id
  AND (m.perms LIKE 'system:%' 
       OR m.menu_name = '系统管理'
       OR m.parent_id = @system_menu_id);

-- 4. 从库存中心负责人角色中移除系统管理权限
SET @inventory_manager_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'inventory_manager' LIMIT 1);
DELETE rm FROM sys_role_menu rm
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = @inventory_manager_role_id
  AND (m.perms LIKE 'system:%' 
       OR m.menu_name = '系统管理'
       OR m.parent_id = @system_menu_id);

-- 5. 从预警中心负责人角色中移除系统管理权限
SET @warning_manager_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'warning_manager' LIMIT 1);
DELETE rm FROM sys_role_menu rm
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = @warning_manager_role_id
  AND (m.perms LIKE 'system:%' 
       OR m.menu_name = '系统管理'
       OR m.parent_id = @system_menu_id);

-- 6. 从所有其他非超级管理员角色中移除系统管理权限
DELETE rm FROM sys_role_menu rm
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id != @admin_role_id
  AND rm.role_id IS NOT NULL
  AND (m.perms LIKE 'system:%' 
       OR m.menu_name = '系统管理'
       OR m.parent_id = @system_menu_id);

-- ============================================
-- 第三部分：确保超级管理员拥有所有系统管理权限
-- ============================================

-- 获取所有系统管理相关的菜单ID
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT DISTINCT @admin_role_id, m.menu_id
FROM sys_menu m
WHERE (m.perms LIKE 'system:%' 
   OR m.menu_name = '系统管理'
   OR m.parent_id = @system_menu_id)
  AND (m.status = '0' OR m.status IS NULL)
  AND NOT EXISTS (
    SELECT 1 FROM sys_role_menu rm 
    WHERE rm.role_id = @admin_role_id 
    AND rm.menu_id = m.menu_id
  );

-- ============================================
-- 第四部分：验证和显示结果
-- ============================================

-- 1. 显示超级管理员拥有的系统管理权限数量
SELECT 
    r.role_name,
    COUNT(rm.menu_id) AS system_permission_count
FROM sys_role r
INNER JOIN sys_role_menu rm ON r.role_id = rm.role_id
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE r.role_id = @admin_role_id
  AND (m.perms LIKE 'system:%' 
       OR m.menu_name = '系统管理'
       OR m.parent_id = @system_menu_id)
GROUP BY r.role_id, r.role_name;

-- 2. 显示所有模块负责人角色拥有的系统管理权限数量（应该为0）
SELECT 
    r.role_name,
    r.role_key,
    COUNT(rm.menu_id) AS system_permission_count
FROM sys_role r
LEFT JOIN sys_role_menu rm ON r.role_id = rm.role_id
LEFT JOIN sys_menu m ON rm.menu_id = m.menu_id
  AND (m.perms LIKE 'system:%' 
       OR m.menu_name = '系统管理'
       OR m.parent_id IN (SELECT menu_id FROM sys_menu WHERE menu_name = '系统管理' AND parent_id = 0))
WHERE r.role_key IN ('asset_manager', 'purchase_manager', 'maintenance_manager', 'inventory_manager', 'warning_manager')
GROUP BY r.role_id, r.role_name, r.role_key
ORDER BY r.role_key;

-- 3. 显示超级管理员拥有的系统管理权限列表
SELECT 
    m.menu_name,
    m.perms,
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
       OR m.parent_id IN (SELECT menu_id FROM sys_menu WHERE menu_name = '系统管理' AND parent_id = 0))
ORDER BY m.menu_id;

-- 4. 显示配置完成信息
SELECT '系统管理权限限制配置完成！' AS message;
SELECT '只有超级管理员（admin角色）可以访问系统管理模块' AS restriction_info;
SELECT '所有模块负责人角色已移除系统管理权限' AS module_manager_info;

