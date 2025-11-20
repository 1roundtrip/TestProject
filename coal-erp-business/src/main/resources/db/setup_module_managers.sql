-- ============================================
-- 模块负责人角色权限配置脚本
-- ============================================
-- 说明：创建5个模块负责人角色，每个角色拥有自己模块的全部权限和其他模块的只读权限
-- 同时修改ceshi用户名为admin2，并确保admin和admin2都是超级管理员
--
-- 角色说明：
-- 1. 资产中心负责人：拥有资产中心全部权限，其他模块只读
-- 2. 采购中心负责人：拥有采购中心全部权限，其他模块只读
-- 3. 维修管理负责人：拥有维修管理全部权限，其他模块只读
-- 4. 库存中心负责人：拥有库存中心全部权限，其他模块只读
-- 5. 预警中心负责人：拥有预警中心全部权限，其他模块只读
-- 6. 超级管理员（admin/admin2）：拥有系统一切权限
-- ============================================

USE coal_erp;

-- ============================================
-- 第一部分：修改ceshi用户名为admin2
-- ============================================

-- 1. 检查ceshi用户是否存在
SELECT user_id, username, status 
FROM sys_user 
WHERE username = 'ceshi';

-- 2. 如果ceshi用户存在，修改用户名为admin2
UPDATE sys_user 
SET username = 'admin2', 
    update_time = NOW()
WHERE username = 'ceshi';

-- 3. 验证修改结果
SELECT user_id, username, status 
FROM sys_user 
WHERE username IN ('admin', 'admin2');

-- ============================================
-- 第二部分：确保超级管理员角色存在
-- ============================================

-- 1. 创建或更新超级管理员角色
INSERT INTO sys_role (role_name, role_key, role_sort, status, create_time, update_time, remark)
SELECT '超级管理员', 'admin', 1, '0', NOW(), NOW(), '系统超级管理员，拥有所有权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_role WHERE role_key = 'admin' OR role_name = '超级管理员'
);

-- 2. 获取超级管理员角色ID
SET @admin_role_id = (
    SELECT role_id FROM sys_role 
    WHERE role_key = 'admin' OR role_name = '超级管理员'
    ORDER BY role_id ASC
    LIMIT 1
);

SELECT CONCAT('超级管理员角色ID: ', @admin_role_id) AS info;

-- 3. 为超级管理员角色分配所有菜单权限
DELETE FROM sys_role_menu WHERE role_id = @admin_role_id;

INSERT INTO sys_role_menu (role_id, menu_id)
SELECT DISTINCT @admin_role_id, m.menu_id
FROM sys_menu m
WHERE m.status = '0' OR m.status IS NULL;

-- 4. 确保admin用户关联超级管理员角色
SET @admin_user_id = (SELECT user_id FROM sys_user WHERE username = 'admin' LIMIT 1);
DELETE FROM sys_user_role WHERE user_id = @admin_user_id;

INSERT INTO sys_user_role (user_id, role_id)
SELECT @admin_user_id, @admin_role_id
WHERE @admin_user_id IS NOT NULL AND @admin_role_id IS NOT NULL
ON DUPLICATE KEY UPDATE role_id = @admin_role_id;

-- 5. 确保admin2用户关联超级管理员角色
SET @admin2_user_id = (SELECT user_id FROM sys_user WHERE username = 'admin2' LIMIT 1);
DELETE FROM sys_user_role WHERE user_id = @admin2_user_id;

INSERT INTO sys_user_role (user_id, role_id)
SELECT @admin2_user_id, @admin_role_id
WHERE @admin2_user_id IS NOT NULL AND @admin_role_id IS NOT NULL
ON DUPLICATE KEY UPDATE role_id = @admin_role_id;

-- ============================================
-- 第三部分：创建模块负责人角色
-- ============================================

-- 1. 创建资产中心负责人角色
INSERT INTO sys_role (role_name, role_key, role_sort, status, create_time, update_time, remark)
SELECT '资产中心负责人', 'asset_manager', 2, '0', NOW(), NOW(), '资产中心负责人，拥有资产中心全部权限，其他模块只读'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_role WHERE role_key = 'asset_manager'
);

SET @asset_manager_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'asset_manager' LIMIT 1);

-- 2. 创建采购中心负责人角色
INSERT INTO sys_role (role_name, role_key, role_sort, status, create_time, update_time, remark)
SELECT '采购中心负责人', 'purchase_manager', 3, '0', NOW(), NOW(), '采购中心负责人，拥有采购中心全部权限，其他模块只读'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_role WHERE role_key = 'purchase_manager'
);

SET @purchase_manager_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'purchase_manager' LIMIT 1);

-- 3. 创建维修管理负责人角色
INSERT INTO sys_role (role_name, role_key, role_sort, status, create_time, update_time, remark)
SELECT '维修管理负责人', 'maintenance_manager', 4, '0', NOW(), NOW(), '维修管理负责人，拥有维修管理全部权限，其他模块只读'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_role WHERE role_key = 'maintenance_manager'
);

SET @maintenance_manager_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'maintenance_manager' LIMIT 1);

-- 4. 创建库存中心负责人角色
INSERT INTO sys_role (role_name, role_key, role_sort, status, create_time, update_time, remark)
SELECT '库存中心负责人', 'inventory_manager', 5, '0', NOW(), NOW(), '库存中心负责人，拥有库存中心全部权限，其他模块只读'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_role WHERE role_key = 'inventory_manager'
);

SET @inventory_manager_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'inventory_manager' LIMIT 1);

-- 5. 创建预警中心负责人角色
INSERT INTO sys_role (role_name, role_key, role_sort, status, create_time, update_time, remark)
SELECT '预警中心负责人', 'warning_manager', 6, '0', NOW(), NOW(), '预警中心负责人，拥有预警中心全部权限，其他模块只读'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_role WHERE role_key = 'warning_manager'
);

SET @warning_manager_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'warning_manager' LIMIT 1);

-- ============================================
-- 第四部分：为资产中心负责人分配权限
-- ============================================

-- 删除现有权限
DELETE FROM sys_role_menu WHERE role_id = @asset_manager_role_id;

-- 资产中心全部权限（包括所有操作：list、add、edit、remove等）
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @asset_manager_role_id, menu_id
FROM sys_menu
WHERE perms LIKE 'asset:%'
  AND (status = '0' OR status IS NULL);

-- 其他模块只读权限（菜单和只读按钮权限：list、view、query等，排除add、edit、remove、approve、confirm等）
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @asset_manager_role_id, menu_id
FROM sys_menu
WHERE (
    -- 其他模块的菜单（目录和菜单类型）
    (menu_type IN ('M', 'C') AND (
        menu_name IN ('采购中心', '维修管理', '库存中心', '预警中心') OR
        parent_id IN (SELECT menu_id FROM sys_menu WHERE menu_name IN ('采购中心', '维修管理', '库存中心', '预警中心') AND parent_id = 0)
    )) OR
    -- 其他模块的只读按钮权限
    (menu_type = 'F' AND (
        (perms LIKE 'purchase:%:list' OR perms LIKE 'purchase:%:view' OR perms LIKE 'purchase:%:query') OR
        (perms LIKE 'maintenance:%:list' OR perms LIKE 'maintenance:%:view' OR perms LIKE 'maintenance:%:query') OR
        (perms LIKE 'inventory:%:list' OR perms LIKE 'inventory:%:view' OR perms LIKE 'inventory:%:query') OR
        (perms LIKE 'warning:%:list' OR perms LIKE 'warning:%:view' OR perms LIKE 'warning:%:query')
    ) AND perms NOT LIKE '%:add' AND perms NOT LIKE '%:edit' AND perms NOT LIKE '%:remove' 
    AND perms NOT LIKE '%:delete' AND perms NOT LIKE '%:approve' AND perms NOT LIKE '%:confirm'
    AND perms NOT LIKE '%:sign' AND perms NOT LIKE '%:handle' AND perms NOT LIKE '%:close')
)
  AND (status = '0' OR status IS NULL);

-- ============================================
-- 第五部分：为采购中心负责人分配权限
-- ============================================

-- 删除现有权限
DELETE FROM sys_role_menu WHERE role_id = @purchase_manager_role_id;

-- 采购中心全部权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @purchase_manager_role_id, menu_id
FROM sys_menu
WHERE perms LIKE 'purchase:%'
  AND (status = '0' OR status IS NULL);

-- 其他模块只读权限（菜单和只读按钮权限）
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @purchase_manager_role_id, menu_id
FROM sys_menu
WHERE (
    -- 其他模块的菜单（目录和菜单类型）
    (menu_type IN ('M', 'C') AND (
        menu_name IN ('资产中心', '维修管理', '库存中心', '预警中心') OR
        parent_id IN (SELECT menu_id FROM sys_menu WHERE menu_name IN ('资产中心', '维修管理', '库存中心', '预警中心') AND parent_id = 0)
    )) OR
    -- 其他模块的只读按钮权限
    (menu_type = 'F' AND (
        (perms LIKE 'asset:%:list' OR perms LIKE 'asset:%:view' OR perms LIKE 'asset:%:query') OR
        (perms LIKE 'maintenance:%:list' OR perms LIKE 'maintenance:%:view' OR perms LIKE 'maintenance:%:query') OR
        (perms LIKE 'inventory:%:list' OR perms LIKE 'inventory:%:view' OR perms LIKE 'inventory:%:query') OR
        (perms LIKE 'warning:%:list' OR perms LIKE 'warning:%:view' OR perms LIKE 'warning:%:query')
    ) AND perms NOT LIKE '%:add' AND perms NOT LIKE '%:edit' AND perms NOT LIKE '%:remove' 
    AND perms NOT LIKE '%:delete' AND perms NOT LIKE '%:approve' AND perms NOT LIKE '%:confirm'
    AND perms NOT LIKE '%:sign' AND perms NOT LIKE '%:handle' AND perms NOT LIKE '%:close')
)
  AND (status = '0' OR status IS NULL);

-- ============================================
-- 第六部分：为维修管理负责人分配权限
-- ============================================

-- 删除现有权限
DELETE FROM sys_role_menu WHERE role_id = @maintenance_manager_role_id;

-- 维修管理全部权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @maintenance_manager_role_id, menu_id
FROM sys_menu
WHERE perms LIKE 'maintenance:%'
  AND (status = '0' OR status IS NULL);

-- 其他模块只读权限（菜单和只读按钮权限）
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @maintenance_manager_role_id, menu_id
FROM sys_menu
WHERE (
    -- 其他模块的菜单（目录和菜单类型）
    (menu_type IN ('M', 'C') AND (
        menu_name IN ('资产中心', '采购中心', '库存中心', '预警中心') OR
        parent_id IN (SELECT menu_id FROM sys_menu WHERE menu_name IN ('资产中心', '采购中心', '库存中心', '预警中心') AND parent_id = 0)
    )) OR
    -- 其他模块的只读按钮权限
    (menu_type = 'F' AND (
        (perms LIKE 'asset:%:list' OR perms LIKE 'asset:%:view' OR perms LIKE 'asset:%:query') OR
        (perms LIKE 'purchase:%:list' OR perms LIKE 'purchase:%:view' OR perms LIKE 'purchase:%:query') OR
        (perms LIKE 'inventory:%:list' OR perms LIKE 'inventory:%:view' OR perms LIKE 'inventory:%:query') OR
        (perms LIKE 'warning:%:list' OR perms LIKE 'warning:%:view' OR perms LIKE 'warning:%:query')
    ) AND perms NOT LIKE '%:add' AND perms NOT LIKE '%:edit' AND perms NOT LIKE '%:remove' 
    AND perms NOT LIKE '%:delete' AND perms NOT LIKE '%:approve' AND perms NOT LIKE '%:confirm'
    AND perms NOT LIKE '%:sign' AND perms NOT LIKE '%:handle' AND perms NOT LIKE '%:close')
)
  AND (status = '0' OR status IS NULL);

-- ============================================
-- 第七部分：为库存中心负责人分配权限
-- ============================================

-- 删除现有权限
DELETE FROM sys_role_menu WHERE role_id = @inventory_manager_role_id;

-- 库存中心全部权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @inventory_manager_role_id, menu_id
FROM sys_menu
WHERE perms LIKE 'inventory:%'
  AND (status = '0' OR status IS NULL);

-- 其他模块只读权限（菜单和只读按钮权限）
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @inventory_manager_role_id, menu_id
FROM sys_menu
WHERE (
    -- 其他模块的菜单（目录和菜单类型）
    (menu_type IN ('M', 'C') AND (
        menu_name IN ('资产中心', '采购中心', '维修管理', '预警中心') OR
        parent_id IN (SELECT menu_id FROM sys_menu WHERE menu_name IN ('资产中心', '采购中心', '维修管理', '预警中心') AND parent_id = 0)
    )) OR
    -- 其他模块的只读按钮权限
    (menu_type = 'F' AND (
        (perms LIKE 'asset:%:list' OR perms LIKE 'asset:%:view' OR perms LIKE 'asset:%:query') OR
        (perms LIKE 'purchase:%:list' OR perms LIKE 'purchase:%:view' OR perms LIKE 'purchase:%:query') OR
        (perms LIKE 'maintenance:%:list' OR perms LIKE 'maintenance:%:view' OR perms LIKE 'maintenance:%:query') OR
        (perms LIKE 'warning:%:list' OR perms LIKE 'warning:%:view' OR perms LIKE 'warning:%:query')
    ) AND perms NOT LIKE '%:add' AND perms NOT LIKE '%:edit' AND perms NOT LIKE '%:remove' 
    AND perms NOT LIKE '%:delete' AND perms NOT LIKE '%:approve' AND perms NOT LIKE '%:confirm'
    AND perms NOT LIKE '%:sign' AND perms NOT LIKE '%:handle' AND perms NOT LIKE '%:close')
)
  AND (status = '0' OR status IS NULL);

-- ============================================
-- 第八部分：为预警中心负责人分配权限
-- ============================================

-- 删除现有权限
DELETE FROM sys_role_menu WHERE role_id = @warning_manager_role_id;

-- 预警中心全部权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @warning_manager_role_id, menu_id
FROM sys_menu
WHERE perms LIKE 'warning:%'
  AND (status = '0' OR status IS NULL);

-- 其他模块只读权限（菜单和只读按钮权限）
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @warning_manager_role_id, menu_id
FROM sys_menu
WHERE (
    -- 其他模块的菜单（目录和菜单类型）
    (menu_type IN ('M', 'C') AND (
        menu_name IN ('资产中心', '采购中心', '维修管理', '库存中心') OR
        parent_id IN (SELECT menu_id FROM sys_menu WHERE menu_name IN ('资产中心', '采购中心', '维修管理', '库存中心') AND parent_id = 0)
    )) OR
    -- 其他模块的只读按钮权限
    (menu_type = 'F' AND (
        (perms LIKE 'asset:%:list' OR perms LIKE 'asset:%:view' OR perms LIKE 'asset:%:query') OR
        (perms LIKE 'purchase:%:list' OR perms LIKE 'purchase:%:view' OR perms LIKE 'purchase:%:query') OR
        (perms LIKE 'maintenance:%:list' OR perms LIKE 'maintenance:%:view' OR perms LIKE 'maintenance:%:query') OR
        (perms LIKE 'inventory:%:list' OR perms LIKE 'inventory:%:view' OR perms LIKE 'inventory:%:query')
    ) AND perms NOT LIKE '%:add' AND perms NOT LIKE '%:edit' AND perms NOT LIKE '%:remove' 
    AND perms NOT LIKE '%:delete' AND perms NOT LIKE '%:approve' AND perms NOT LIKE '%:confirm'
    AND perms NOT LIKE '%:sign' AND perms NOT LIKE '%:handle' AND perms NOT LIKE '%:close')
)
  AND (status = '0' OR status IS NULL);

-- ============================================
-- 第九部分：验证和显示结果
-- ============================================

-- 1. 显示所有角色信息
SELECT 
    role_id,
    role_name,
    role_key,
    role_sort,
    status,
    remark
FROM sys_role
WHERE role_key IN ('admin', 'asset_manager', 'purchase_manager', 'maintenance_manager', 'inventory_manager', 'warning_manager')
ORDER BY role_sort;

-- 2. 显示每个角色拥有的权限数量
SELECT 
    r.role_name,
    r.role_key,
    COUNT(rm.menu_id) AS permission_count
FROM sys_role r
LEFT JOIN sys_role_menu rm ON r.role_id = rm.role_id
WHERE r.role_key IN ('admin', 'asset_manager', 'purchase_manager', 'maintenance_manager', 'inventory_manager', 'warning_manager')
GROUP BY r.role_id, r.role_name, r.role_key
ORDER BY r.role_sort;

-- 3. 显示admin和admin2用户的角色关联
SELECT 
    u.user_id,
    u.username,
    u.nick_name,
    u.status AS user_status,
    r.role_id,
    r.role_name,
    r.role_key,
    r.status AS role_status
FROM sys_user u
INNER JOIN sys_user_role ur ON u.user_id = ur.user_id
INNER JOIN sys_role r ON ur.role_id = r.role_id
WHERE u.username IN ('admin', 'admin2')
ORDER BY u.username, r.role_sort;

-- 4. 显示资产中心负责人的权限示例（前20个）
SELECT 
    r.role_name,
    m.menu_name,
    m.perms,
    m.menu_type
FROM sys_role r
INNER JOIN sys_role_menu rm ON r.role_id = rm.role_id
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE r.role_key = 'asset_manager'
  AND m.perms IS NOT NULL
  AND m.perms != ''
ORDER BY m.perms
LIMIT 20;

-- 5. 显示采购中心负责人的权限示例（前20个）
SELECT 
    r.role_name,
    m.menu_name,
    m.perms,
    m.menu_type
FROM sys_role r
INNER JOIN sys_role_menu rm ON r.role_id = rm.role_id
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE r.role_key = 'purchase_manager'
  AND m.perms IS NOT NULL
  AND m.perms != ''
ORDER BY m.perms
LIMIT 20;

-- 显示配置完成信息
SELECT '权限配置完成！' AS message;
SELECT '已创建的角色：资产中心负责人、采购中心负责人、维修管理负责人、库存中心负责人、预警中心负责人、超级管理员' AS roles;
SELECT 'admin和admin2用户都是超级管理员，拥有所有权限' AS admin_info;

