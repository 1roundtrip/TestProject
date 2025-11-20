-- ============================================
-- 资产管理系统权限菜单初始化脚本
-- ============================================
-- 说明：为资产管理系统创建菜单和权限标识
-- 执行前请确保 sys_menu 表已存在

USE coal_erp;

-- 1. 创建资产中心父菜单（如果不存在）
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产中心', 0, 3, '/asset', NULL, 'M', 'asset', 'DatabaseOutlined', '0', NOW(), NOW(), '资产管理系统主菜单'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE menu_name = '资产中心' AND parent_id = 0
);

-- 获取资产中心菜单ID（用于后续子菜单）
SET @asset_parent_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '资产中心' AND parent_id = 0 LIMIT 1);

-- 2. 资产档案管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产档案', @asset_parent_id, 1, '/asset/archive', 'Asset/Archive', 'C', 'asset:archive', 'DatabaseOutlined', '0', NOW(), NOW(), '资产档案管理'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE menu_name = '资产档案' AND parent_id = @asset_parent_id
);

-- 资产档案按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产档案-查询', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产档案' AND parent_id = @asset_parent_id LIMIT 1), 1, '', NULL, 'F', 'asset:archive:list', NULL, '0', NOW(), NOW(), '资产档案查询权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:archive:list'
);

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产档案-新增', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产档案' AND parent_id = @asset_parent_id LIMIT 1), 2, '', NULL, 'F', 'asset:archive:add', NULL, '0', NOW(), NOW(), '资产档案新增权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:archive:add'
);

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产档案-编辑', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产档案' AND parent_id = @asset_parent_id LIMIT 1), 3, '', NULL, 'F', 'asset:archive:edit', NULL, '0', NOW(), NOW(), '资产档案编辑权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:archive:edit'
);

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产档案-删除', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产档案' AND parent_id = @asset_parent_id LIMIT 1), 4, '', NULL, 'F', 'asset:archive:remove', NULL, '0', NOW(), NOW(), '资产档案删除权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:archive:remove'
);

-- 3. 资产入库管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产入库', @asset_parent_id, 2, '/asset/storage', 'Asset/Storage', 'C', 'asset:storage', 'InboxOutlined', '0', NOW(), NOW(), '资产入库管理'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE menu_name = '资产入库' AND parent_id = @asset_parent_id
);

-- 资产入库按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产入库-查询', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产入库' AND parent_id = @asset_parent_id LIMIT 1), 1, '', NULL, 'F', 'asset:storage:list', NULL, '0', NOW(), NOW(), '资产入库查询权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:storage:list'
);

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产入库-新增', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产入库' AND parent_id = @asset_parent_id LIMIT 1), 2, '', NULL, 'F', 'asset:storage:add', NULL, '0', NOW(), NOW(), '资产入库新增权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:storage:add'
);

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产入库-编辑', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产入库' AND parent_id = @asset_parent_id LIMIT 1), 3, '', NULL, 'F', 'asset:storage:edit', NULL, '0', NOW(), NOW(), '资产入库编辑权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:storage:edit'
);

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产入库-删除', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产入库' AND parent_id = @asset_parent_id LIMIT 1), 4, '', NULL, 'F', 'asset:storage:remove', NULL, '0', NOW(), NOW(), '资产入库删除权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:storage:remove'
);

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产入库-确认', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产入库' AND parent_id = @asset_parent_id LIMIT 1), 5, '', NULL, 'F', 'asset:storage:confirm', NULL, '0', NOW(), NOW(), '资产入库确认权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:storage:confirm'
);

-- 4. 资产领用退库菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产领用退库', @asset_parent_id, 3, '/asset/borrow', 'Asset/Borrow', 'C', 'asset:borrow', 'ExportOutlined', '0', NOW(), NOW(), '资产领用退库管理'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE menu_name = '资产领用退库' AND parent_id = @asset_parent_id
);

-- 资产领用退库按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产领用-查询', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产领用退库' AND parent_id = @asset_parent_id LIMIT 1), 1, '', NULL, 'F', 'asset:borrow:list', NULL, '0', NOW(), NOW(), '资产领用查询权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:borrow:list'
);

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产领用-新增', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产领用退库' AND parent_id = @asset_parent_id LIMIT 1), 2, '', NULL, 'F', 'asset:borrow:add', NULL, '0', NOW(), NOW(), '资产领用新增权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:borrow:add'
);

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产领用-退库', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产领用退库' AND parent_id = @asset_parent_id LIMIT 1), 3, '', NULL, 'F', 'asset:borrow:return', NULL, '0', NOW(), NOW(), '资产退库权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:borrow:return'
);

-- 5. 资产转移调拨菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产转移调拨', @asset_parent_id, 4, '/asset/transfer', 'Asset/Transfer', 'C', 'asset:transfer', 'SwapOutlined', '0', NOW(), NOW(), '资产转移调拨管理'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE menu_name = '资产转移调拨' AND parent_id = @asset_parent_id
);

-- 资产转移调拨按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产转移-新增', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产转移调拨' AND parent_id = @asset_parent_id LIMIT 1), 1, '', NULL, 'F', 'asset:transfer:add', NULL, '0', NOW(), NOW(), '资产转移新增权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:transfer:add'
);

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产转移-审批', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产转移调拨' AND parent_id = @asset_parent_id LIMIT 1), 2, '', NULL, 'F', 'asset:transfer:approve', NULL, '0', NOW(), NOW(), '资产转移审批权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:transfer:approve'
);

-- 6. 资产维修管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产维修管理', @asset_parent_id, 5, '/asset/repair', 'Asset/Repair', 'C', 'asset:repair', 'ToolOutlined', '0', NOW(), NOW(), '资产维修管理'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE menu_name = '资产维修管理' AND parent_id = @asset_parent_id
);

-- 7. 资产折旧管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产折旧管理', @asset_parent_id, 6, '/asset/depreciation', 'Asset/Depreciation', 'C', 'asset:depreciation', 'CalculatorOutlined', '0', NOW(), NOW(), '资产折旧管理'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE menu_name = '资产折旧管理' AND parent_id = @asset_parent_id
);

-- 资产折旧按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产折旧-配置', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产折旧管理' AND parent_id = @asset_parent_id LIMIT 1), 1, '', NULL, 'F', 'asset:depreciation:config', NULL, '0', NOW(), NOW(), '资产折旧配置权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:depreciation:config'
);

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产折旧-计算', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产折旧管理' AND parent_id = @asset_parent_id LIMIT 1), 2, '', NULL, 'F', 'asset:depreciation:calculate', NULL, '0', NOW(), NOW(), '资产折旧计算权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:depreciation:calculate'
);

-- 8. 资产盘点管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产盘点管理', @asset_parent_id, 7, '/asset/inventory', 'Asset/Inventory', 'C', 'asset:inventory', 'FileSearchOutlined', '0', NOW(), NOW(), '资产盘点管理'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE menu_name = '资产盘点管理' AND parent_id = @asset_parent_id
);

-- 资产盘点按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产盘点-新增', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产盘点管理' AND parent_id = @asset_parent_id LIMIT 1), 1, '', NULL, 'F', 'asset:inventory:add', NULL, '0', NOW(), NOW(), '资产盘点新增权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:inventory:add'
);

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产盘点-确认', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产盘点管理' AND parent_id = @asset_parent_id LIMIT 1), 2, '', NULL, 'F', 'asset:inventory:confirm', NULL, '0', NOW(), NOW(), '资产盘点确认权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:inventory:confirm'
);

-- 9. 资产报废管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产报废管理', @asset_parent_id, 8, '/asset/scrap', 'Asset/Scrap', 'C', 'asset:scrap', 'DeleteOutlined', '0', NOW(), NOW(), '资产报废管理'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE menu_name = '资产报废管理' AND parent_id = @asset_parent_id
);

-- 资产报废按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产报废-新增', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产报废管理' AND parent_id = @asset_parent_id LIMIT 1), 1, '', NULL, 'F', 'asset:scrap:add', NULL, '0', NOW(), NOW(), '资产报废新增权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:scrap:add'
);

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产报废-审批', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产报废管理' AND parent_id = @asset_parent_id LIMIT 1), 2, '', NULL, 'F', 'asset:scrap:approve', NULL, '0', NOW(), NOW(), '资产报废审批权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:scrap:approve'
);

-- 10. 资产报表分析菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产报表分析', @asset_parent_id, 9, '/asset/report', 'Asset/Report', 'C', 'asset:report', 'BarChartOutlined', '0', NOW(), NOW(), '资产报表分析'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE menu_name = '资产报表分析' AND parent_id = @asset_parent_id
);

-- 资产报表按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '资产报表-查看', (SELECT menu_id FROM sys_menu WHERE menu_name = '资产报表分析' AND parent_id = @asset_parent_id LIMIT 1), 1, '', NULL, 'F', 'asset:report:view', NULL, '0', NOW(), NOW(), '资产报表查看权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE perms = 'asset:report:view'
);

-- ============================================
-- 为管理员角色分配所有资产权限（可选）
-- ============================================
-- 注意：执行前请确保管理员角色ID为1，或根据实际情况修改
-- INSERT INTO sys_role_menu (role_id, menu_id)
-- SELECT 1, menu_id FROM sys_menu WHERE perms LIKE 'asset:%'
-- ON DUPLICATE KEY UPDATE role_id = role_id;

-- 完成提示
SELECT '资产管理系统权限菜单初始化完成！' AS message;

