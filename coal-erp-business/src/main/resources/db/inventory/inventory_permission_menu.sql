-- ============================================
-- 库存管理系统权限和菜单配置
-- ============================================

USE coal_erp;

-- ============================================
-- 1. 创建库存管理菜单
-- ============================================

-- 库存管理主菜单
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('库存中心', 0, 4, 'inventory', NULL, 'M', NULL, 'inbox', '0', NOW(), NOW(), '库存管理系统')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @inventory_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '库存中心' AND parent_id = 0 LIMIT 1);

-- 仓库管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('仓库管理', @inventory_menu_id, 1, 'warehouse', 'Inventory/Warehouse/index', 'C', 'inventory:warehouse:list', 'home', '0', NOW(), NOW(), '仓库管理')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @warehouse_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '仓库管理' AND parent_id = @inventory_menu_id LIMIT 1);

-- 仓库管理按钮权限
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @warehouse_menu_id, 1, '', '', 'F', 'inventory:warehouse:add', '', '0', NOW(), NOW(), ''),
('编辑', @warehouse_menu_id, 2, '', '', 'F', 'inventory:warehouse:edit', '', '0', NOW(), NOW(), ''),
('删除', @warehouse_menu_id, 3, '', '', 'F', 'inventory:warehouse:remove', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 库位管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('库位管理', @inventory_menu_id, 2, 'location', 'Inventory/Location/index', 'C', 'inventory:location:list', 'appstore', '0', NOW(), NOW(), '库位管理')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @location_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '库位管理' AND parent_id = @inventory_menu_id LIMIT 1);

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @location_menu_id, 1, '', '', 'F', 'inventory:location:add', '', '0', NOW(), NOW(), ''),
('编辑', @location_menu_id, 2, '', '', 'F', 'inventory:location:edit', '', '0', NOW(), NOW(), ''),
('删除', @location_menu_id, 3, '', '', 'F', 'inventory:location:remove', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 库存物品管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('库存物品', @inventory_menu_id, 3, 'material', 'Inventory/Material/index', 'C', 'inventory:material:list', 'database', '0', NOW(), NOW(), '库存物品管理')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @material_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '库存物品' AND parent_id = @inventory_menu_id LIMIT 1);

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @material_menu_id, 1, '', '', 'F', 'inventory:material:add', '', '0', NOW(), NOW(), ''),
('编辑', @material_menu_id, 2, '', '', 'F', 'inventory:material:edit', '', '0', NOW(), NOW(), ''),
('删除', @material_menu_id, 3, '', '', 'F', 'inventory:material:remove', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 入库管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('入库管理', @inventory_menu_id, 4, 'inbound', 'Inventory/Inbound/index', 'C', 'inventory:inbound:list', 'arrow-down', '0', NOW(), NOW(), '入库管理')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @inbound_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '入库管理' AND parent_id = @inventory_menu_id LIMIT 1);

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @inbound_menu_id, 1, '', '', 'F', 'inventory:inbound:add', '', '0', NOW(), NOW(), ''),
('编辑', @inbound_menu_id, 2, '', '', 'F', 'inventory:inbound:edit', '', '0', NOW(), NOW(), ''),
('删除', @inbound_menu_id, 3, '', '', 'F', 'inventory:inbound:remove', '', '0', NOW(), NOW(), ''),
('审批', @inbound_menu_id, 4, '', '', 'F', 'inventory:inbound:approve', '', '0', NOW(), NOW(), ''),
('收货', @inbound_menu_id, 5, '', '', 'F', 'inventory:inbound:receive', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 出库管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('出库管理', @inventory_menu_id, 5, 'outbound', 'Inventory/Outbound/index', 'C', 'inventory:outbound:list', 'arrow-up', '0', NOW(), NOW(), '出库管理')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @outbound_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '出库管理' AND parent_id = @inventory_menu_id LIMIT 1);

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @outbound_menu_id, 1, '', '', 'F', 'inventory:outbound:add', '', '0', NOW(), NOW(), ''),
('编辑', @outbound_menu_id, 2, '', '', 'F', 'inventory:outbound:edit', '', '0', NOW(), NOW(), ''),
('删除', @outbound_menu_id, 3, '', '', 'F', 'inventory:outbound:remove', '', '0', NOW(), NOW(), ''),
('审批', @outbound_menu_id, 4, '', '', 'F', 'inventory:outbound:approve', '', '0', NOW(), NOW(), ''),
('发放', @outbound_menu_id, 5, '', '', 'F', 'inventory:outbound:issue', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 库存调拨
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('库存调拨', @inventory_menu_id, 6, 'transfer', 'Inventory/Transfer/index', 'C', 'inventory:transfer:list', 'swap', '0', NOW(), NOW(), '库存调拨管理')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @transfer_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '库存调拨' AND parent_id = @inventory_menu_id LIMIT 1);

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @transfer_menu_id, 1, '', '', 'F', 'inventory:transfer:add', '', '0', NOW(), NOW(), ''),
('编辑', @transfer_menu_id, 2, '', '', 'F', 'inventory:transfer:edit', '', '0', NOW(), NOW(), ''),
('删除', @transfer_menu_id, 3, '', '', 'F', 'inventory:transfer:remove', '', '0', NOW(), NOW(), ''),
('审批', @transfer_menu_id, 4, '', '', 'F', 'inventory:transfer:approve', '', '0', NOW(), NOW(), ''),
('出库', @transfer_menu_id, 5, '', '', 'F', 'inventory:transfer:outbound', '', '0', NOW(), NOW(), ''),
('入库', @transfer_menu_id, 6, '', '', 'F', 'inventory:transfer:inbound', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 库存调整
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('库存调整', @inventory_menu_id, 7, 'adjustment', 'Inventory/Adjustment/index', 'C', 'inventory:adjustment:list', 'edit', '0', NOW(), NOW(), '库存调整管理')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @adjustment_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '库存调整' AND parent_id = @inventory_menu_id LIMIT 1);

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @adjustment_menu_id, 1, '', '', 'F', 'inventory:adjustment:add', '', '0', NOW(), NOW(), ''),
('编辑', @adjustment_menu_id, 2, '', '', 'F', 'inventory:adjustment:edit', '', '0', NOW(), NOW(), ''),
('删除', @adjustment_menu_id, 3, '', '', 'F', 'inventory:adjustment:remove', '', '0', NOW(), NOW(), ''),
('审批', @adjustment_menu_id, 4, '', '', 'F', 'inventory:adjustment:approve', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 库存盘点
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('库存盘点', @inventory_menu_id, 8, 'stocktaking', 'Inventory/Stocktaking/index', 'C', 'inventory:stocktaking:list', 'file-search', '0', NOW(), NOW(), '库存盘点管理')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @stocktaking_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '库存盘点' AND parent_id = @inventory_menu_id LIMIT 1);

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @stocktaking_menu_id, 1, '', '', 'F', 'inventory:stocktaking:add', '', '0', NOW(), NOW(), ''),
('编辑', @stocktaking_menu_id, 2, '', '', 'F', 'inventory:stocktaking:edit', '', '0', NOW(), NOW(), ''),
('删除', @stocktaking_menu_id, 3, '', '', 'F', 'inventory:stocktaking:remove', '', '0', NOW(), NOW(), ''),
('开始盘点', @stocktaking_menu_id, 4, '', '', 'F', 'inventory:stocktaking:start', '', '0', NOW(), NOW(), ''),
('完成盘点', @stocktaking_menu_id, 5, '', '', 'F', 'inventory:stocktaking:complete', '', '0', NOW(), NOW(), ''),
('确认', @stocktaking_menu_id, 6, '', '', 'F', 'inventory:stocktaking:confirm', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 库存预警
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('库存预警', @inventory_menu_id, 9, 'warning', 'Inventory/Warning/index', 'C', 'inventory:warning:list', 'warning', '0', NOW(), NOW(), '库存预警管理')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @warning_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '库存预警' AND parent_id = @inventory_menu_id LIMIT 1);

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('处理', @warning_menu_id, 1, '', '', 'F', 'inventory:warning:handle', '', '0', NOW(), NOW(), ''),
('忽略', @warning_menu_id, 2, '', '', 'F', 'inventory:warning:ignore', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 库存报表分析
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('库存报表', @inventory_menu_id, 10, 'report', 'Inventory/Report/index', 'C', 'inventory:report:list', 'bar-chart', '0', NOW(), NOW(), '库存报表分析')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @report_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '库存报表' AND parent_id = @inventory_menu_id LIMIT 1);

-- 库存报表按钮权限
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('查看', @report_menu_id, 1, '', '', 'F', 'inventory:report:view', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- ============================================
-- 2. 为超级管理员角色分配所有权限
-- ============================================

SET @admin_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'admin' LIMIT 1);

-- 获取所有库存管理相关的菜单ID并分配给超级管理员
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @admin_role_id, menu_id 
FROM sys_menu 
WHERE menu_name IN ('库存中心', '仓库管理', '库位管理', '库存物品', '入库管理', '出库管理', '库存调拨', '库存调整', '库存盘点', '库存预警', '库存报表')
   OR perms LIKE 'inventory:%'
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);

