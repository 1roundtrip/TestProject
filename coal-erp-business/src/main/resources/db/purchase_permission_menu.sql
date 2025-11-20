-- ============================================
-- 采购管理系统权限菜单初始化脚本
-- ============================================
-- 说明：为采购管理系统创建菜单和权限标识
-- 执行前请确保 sys_menu 表已存在

USE coal_erp;

-- 1. 创建采购中心父菜单（如果不存在）
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购中心', 0, 4, '/purchase', NULL, 'M', 'purchase', 'ShoppingOutlined', '0', NOW(), NOW(), '采购管理系统主菜单'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE menu_name = '采购中心' AND parent_id = 0
);

-- 获取采购中心菜单ID（用于后续子菜单）
SET @purchase_parent_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '采购中心' AND parent_id = 0 LIMIT 1);

-- 2. 采购计划管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购计划', @purchase_parent_id, 1, '/purchase/plan', 'Purchase/Plan', 'C', 'purchase:plan', 'FileTextOutlined', '0', NOW(), NOW(), '采购计划管理'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE menu_name = '采购计划' AND parent_id = @purchase_parent_id
);

-- 采购计划按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购计划-查询', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购计划' AND parent_id = @purchase_parent_id LIMIT 1), 1, '', NULL, 'F', 'purchase:plan:list', NULL, '0', NOW(), NOW(), '采购计划查询权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:plan:list');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购计划-新增', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购计划' AND parent_id = @purchase_parent_id LIMIT 1), 2, '', NULL, 'F', 'purchase:plan:add', NULL, '0', NOW(), NOW(), '采购计划新增权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:plan:add');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购计划-编辑', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购计划' AND parent_id = @purchase_parent_id LIMIT 1), 3, '', NULL, 'F', 'purchase:plan:edit', NULL, '0', NOW(), NOW(), '采购计划编辑权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:plan:edit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购计划-删除', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购计划' AND parent_id = @purchase_parent_id LIMIT 1), 4, '', NULL, 'F', 'purchase:plan:remove', NULL, '0', NOW(), NOW(), '采购计划删除权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:plan:remove');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购计划-提交', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购计划' AND parent_id = @purchase_parent_id LIMIT 1), 5, '', NULL, 'F', 'purchase:plan:submit', NULL, '0', NOW(), NOW(), '采购计划提交权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:plan:submit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购计划-审批', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购计划' AND parent_id = @purchase_parent_id LIMIT 1), 6, '', NULL, 'F', 'purchase:plan:approve', NULL, '0', NOW(), NOW(), '采购计划审批权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:plan:approve');

-- 3. 采购申请管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购申请', @purchase_parent_id, 2, '/purchase/requisition', 'Purchase/Requisition', 'C', 'purchase:requisition', 'FileAddOutlined', '0', NOW(), NOW(), '采购申请管理'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = '采购申请' AND parent_id = @purchase_parent_id);

-- 采购申请按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购申请-查询', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购申请' AND parent_id = @purchase_parent_id LIMIT 1), 1, '', NULL, 'F', 'purchase:requisition:list', NULL, '0', NOW(), NOW(), '采购申请查询权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:requisition:list');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购申请-新增', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购申请' AND parent_id = @purchase_parent_id LIMIT 1), 2, '', NULL, 'F', 'purchase:requisition:add', NULL, '0', NOW(), NOW(), '采购申请新增权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:requisition:add');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购申请-编辑', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购申请' AND parent_id = @purchase_parent_id LIMIT 1), 3, '', NULL, 'F', 'purchase:requisition:edit', NULL, '0', NOW(), NOW(), '采购申请编辑权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:requisition:edit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购申请-删除', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购申请' AND parent_id = @purchase_parent_id LIMIT 1), 4, '', NULL, 'F', 'purchase:requisition:remove', NULL, '0', NOW(), NOW(), '采购申请删除权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:requisition:remove');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购申请-提交', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购申请' AND parent_id = @purchase_parent_id LIMIT 1), 5, '', NULL, 'F', 'purchase:requisition:submit', NULL, '0', NOW(), NOW(), '采购申请提交权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:requisition:submit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购申请-审批', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购申请' AND parent_id = @purchase_parent_id LIMIT 1), 6, '', NULL, 'F', 'purchase:requisition:approve', NULL, '0', NOW(), NOW(), '采购申请审批权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:requisition:approve');

-- 4. 供应商管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '供应商管理', @purchase_parent_id, 3, '/purchase/supplier', 'Purchase/Supplier', 'C', 'purchase:supplier', 'TeamOutlined', '0', NOW(), NOW(), '供应商管理'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = '供应商管理' AND parent_id = @purchase_parent_id);

-- 供应商按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '供应商-查询', (SELECT menu_id FROM sys_menu WHERE menu_name = '供应商管理' AND parent_id = @purchase_parent_id LIMIT 1), 1, '', NULL, 'F', 'purchase:supplier:list', NULL, '0', NOW(), NOW(), '供应商查询权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:supplier:list');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '供应商-新增', (SELECT menu_id FROM sys_menu WHERE menu_name = '供应商管理' AND parent_id = @purchase_parent_id LIMIT 1), 2, '', NULL, 'F', 'purchase:supplier:add', NULL, '0', NOW(), NOW(), '供应商新增权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:supplier:add');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '供应商-编辑', (SELECT menu_id FROM sys_menu WHERE menu_name = '供应商管理' AND parent_id = @purchase_parent_id LIMIT 1), 3, '', NULL, 'F', 'purchase:supplier:edit', NULL, '0', NOW(), NOW(), '供应商编辑权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:supplier:edit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '供应商-删除', (SELECT menu_id FROM sys_menu WHERE menu_name = '供应商管理' AND parent_id = @purchase_parent_id LIMIT 1), 4, '', NULL, 'F', 'purchase:supplier:remove', NULL, '0', NOW(), NOW(), '供应商删除权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:supplier:remove');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '供应商-评价', (SELECT menu_id FROM sys_menu WHERE menu_name = '供应商管理' AND parent_id = @purchase_parent_id LIMIT 1), 5, '', NULL, 'F', 'purchase:supplier:evaluate', NULL, '0', NOW(), NOW(), '供应商评价权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:supplier:evaluate');

-- 5. 采购订单管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购订单', @purchase_parent_id, 4, '/purchase/order', 'Purchase/Order', 'C', 'purchase:order', 'ShoppingCartOutlined', '0', NOW(), NOW(), '采购订单管理'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = '采购订单' AND parent_id = @purchase_parent_id);

-- 采购订单按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购订单-查询', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购订单' AND parent_id = @purchase_parent_id LIMIT 1), 1, '', NULL, 'F', 'purchase:order:list', NULL, '0', NOW(), NOW(), '采购订单查询权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:order:list');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购订单-新增', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购订单' AND parent_id = @purchase_parent_id LIMIT 1), 2, '', NULL, 'F', 'purchase:order:add', NULL, '0', NOW(), NOW(), '采购订单新增权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:order:add');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购订单-编辑', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购订单' AND parent_id = @purchase_parent_id LIMIT 1), 3, '', NULL, 'F', 'purchase:order:edit', NULL, '0', NOW(), NOW(), '采购订单编辑权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:order:edit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购订单-删除', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购订单' AND parent_id = @purchase_parent_id LIMIT 1), 4, '', NULL, 'F', 'purchase:order:remove', NULL, '0', NOW(), NOW(), '采购订单删除权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:order:remove');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购订单-提交', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购订单' AND parent_id = @purchase_parent_id LIMIT 1), 5, '', NULL, 'F', 'purchase:order:submit', NULL, '0', NOW(), NOW(), '采购订单提交权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:order:submit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购订单-审批', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购订单' AND parent_id = @purchase_parent_id LIMIT 1), 6, '', NULL, 'F', 'purchase:order:approve', NULL, '0', NOW(), NOW(), '采购订单审批权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:order:approve');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购订单-确认', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购订单' AND parent_id = @purchase_parent_id LIMIT 1), 7, '', NULL, 'F', 'purchase:order:confirm', NULL, '0', NOW(), NOW(), '采购订单确认权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:order:confirm');

-- 6. 采购合同管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购合同', @purchase_parent_id, 5, '/purchase/contract', 'Purchase/Contract', 'C', 'purchase:contract', 'FileTextOutlined', '0', NOW(), NOW(), '采购合同管理'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = '采购合同' AND parent_id = @purchase_parent_id);

-- 采购合同按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购合同-查询', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购合同' AND parent_id = @purchase_parent_id LIMIT 1), 1, '', NULL, 'F', 'purchase:contract:list', NULL, '0', NOW(), NOW(), '采购合同查询权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:contract:list');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购合同-新增', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购合同' AND parent_id = @purchase_parent_id LIMIT 1), 2, '', NULL, 'F', 'purchase:contract:add', NULL, '0', NOW(), NOW(), '采购合同新增权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:contract:add');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购合同-编辑', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购合同' AND parent_id = @purchase_parent_id LIMIT 1), 3, '', NULL, 'F', 'purchase:contract:edit', NULL, '0', NOW(), NOW(), '采购合同编辑权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:contract:edit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购合同-删除', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购合同' AND parent_id = @purchase_parent_id LIMIT 1), 4, '', NULL, 'F', 'purchase:contract:remove', NULL, '0', NOW(), NOW(), '采购合同删除权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:contract:remove');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购合同-提交', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购合同' AND parent_id = @purchase_parent_id LIMIT 1), 5, '', NULL, 'F', 'purchase:contract:submit', NULL, '0', NOW(), NOW(), '采购合同提交权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:contract:submit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购合同-审批', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购合同' AND parent_id = @purchase_parent_id LIMIT 1), 6, '', NULL, 'F', 'purchase:contract:approve', NULL, '0', NOW(), NOW(), '采购合同审批权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:contract:approve');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购合同-签订', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购合同' AND parent_id = @purchase_parent_id LIMIT 1), 7, '', NULL, 'F', 'purchase:contract:sign', NULL, '0', NOW(), NOW(), '采购合同签订权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:contract:sign');

-- 7. 采购收货管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购收货', @purchase_parent_id, 6, '/purchase/receiving', 'Purchase/Receiving', 'C', 'purchase:receiving', 'InboxOutlined', '0', NOW(), NOW(), '采购收货管理'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = '采购收货' AND parent_id = @purchase_parent_id);

-- 采购收货按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购收货-查询', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购收货' AND parent_id = @purchase_parent_id LIMIT 1), 1, '', NULL, 'F', 'purchase:receiving:list', NULL, '0', NOW(), NOW(), '采购收货查询权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:receiving:list');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购收货-新增', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购收货' AND parent_id = @purchase_parent_id LIMIT 1), 2, '', NULL, 'F', 'purchase:receiving:add', NULL, '0', NOW(), NOW(), '采购收货新增权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:receiving:add');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购收货-编辑', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购收货' AND parent_id = @purchase_parent_id LIMIT 1), 3, '', NULL, 'F', 'purchase:receiving:edit', NULL, '0', NOW(), NOW(), '采购收货编辑权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:receiving:edit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购收货-删除', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购收货' AND parent_id = @purchase_parent_id LIMIT 1), 4, '', NULL, 'F', 'purchase:receiving:remove', NULL, '0', NOW(), NOW(), '采购收货删除权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:receiving:remove');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购收货-确认', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购收货' AND parent_id = @purchase_parent_id LIMIT 1), 5, '', NULL, 'F', 'purchase:receiving:confirm', NULL, '0', NOW(), NOW(), '采购收货确认权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:receiving:confirm');

-- 8. 采购质检管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购质检', @purchase_parent_id, 7, '/purchase/quality', 'Purchase/Quality', 'C', 'purchase:quality', 'SafetyCertificateOutlined', '0', NOW(), NOW(), '采购质检管理'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = '采购质检' AND parent_id = @purchase_parent_id);

-- 采购质检按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购质检-查询', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购质检' AND parent_id = @purchase_parent_id LIMIT 1), 1, '', NULL, 'F', 'purchase:quality:list', NULL, '0', NOW(), NOW(), '采购质检查询权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:quality:list');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购质检-新增', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购质检' AND parent_id = @purchase_parent_id LIMIT 1), 2, '', NULL, 'F', 'purchase:quality:add', NULL, '0', NOW(), NOW(), '采购质检新增权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:quality:add');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购质检-编辑', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购质检' AND parent_id = @purchase_parent_id LIMIT 1), 3, '', NULL, 'F', 'purchase:quality:edit', NULL, '0', NOW(), NOW(), '采购质检编辑权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:quality:edit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购质检-完成', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购质检' AND parent_id = @purchase_parent_id LIMIT 1), 4, '', NULL, 'F', 'purchase:quality:complete', NULL, '0', NOW(), NOW(), '采购质检完成权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:quality:complete');

-- 9. 采购退货管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购退货', @purchase_parent_id, 8, '/purchase/return', 'Purchase/Return', 'C', 'purchase:return', 'RollbackOutlined', '0', NOW(), NOW(), '采购退货管理'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = '采购退货' AND parent_id = @purchase_parent_id);

-- 采购退货按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购退货-查询', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购退货' AND parent_id = @purchase_parent_id LIMIT 1), 1, '', NULL, 'F', 'purchase:return:list', NULL, '0', NOW(), NOW(), '采购退货查询权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:return:list');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购退货-新增', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购退货' AND parent_id = @purchase_parent_id LIMIT 1), 2, '', NULL, 'F', 'purchase:return:add', NULL, '0', NOW(), NOW(), '采购退货新增权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:return:add');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购退货-编辑', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购退货' AND parent_id = @purchase_parent_id LIMIT 1), 3, '', NULL, 'F', 'purchase:return:edit', NULL, '0', NOW(), NOW(), '采购退货编辑权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:return:edit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购退货-删除', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购退货' AND parent_id = @purchase_parent_id LIMIT 1), 4, '', NULL, 'F', 'purchase:return:remove', NULL, '0', NOW(), NOW(), '采购退货删除权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:return:remove');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购退货-提交', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购退货' AND parent_id = @purchase_parent_id LIMIT 1), 5, '', NULL, 'F', 'purchase:return:submit', NULL, '0', NOW(), NOW(), '采购退货提交权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:return:submit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购退货-审批', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购退货' AND parent_id = @purchase_parent_id LIMIT 1), 6, '', NULL, 'F', 'purchase:return:approve', NULL, '0', NOW(), NOW(), '采购退货审批权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:return:approve');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购退货-确认', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购退货' AND parent_id = @purchase_parent_id LIMIT 1), 7, '', NULL, 'F', 'purchase:return:confirm', NULL, '0', NOW(), NOW(), '采购退货确认权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:return:confirm');

-- 10. 采购付款管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购付款', @purchase_parent_id, 9, '/purchase/payment', 'Purchase/Payment', 'C', 'purchase:payment', 'DollarOutlined', '0', NOW(), NOW(), '采购付款管理'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = '采购付款' AND parent_id = @purchase_parent_id);

-- 采购付款按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购付款-查询', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购付款' AND parent_id = @purchase_parent_id LIMIT 1), 1, '', NULL, 'F', 'purchase:payment:list', NULL, '0', NOW(), NOW(), '采购付款查询权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:payment:list');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购付款-新增', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购付款' AND parent_id = @purchase_parent_id LIMIT 1), 2, '', NULL, 'F', 'purchase:payment:add', NULL, '0', NOW(), NOW(), '采购付款新增权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:payment:add');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购付款-编辑', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购付款' AND parent_id = @purchase_parent_id LIMIT 1), 3, '', NULL, 'F', 'purchase:payment:edit', NULL, '0', NOW(), NOW(), '采购付款编辑权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:payment:edit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购付款-删除', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购付款' AND parent_id = @purchase_parent_id LIMIT 1), 4, '', NULL, 'F', 'purchase:payment:remove', NULL, '0', NOW(), NOW(), '采购付款删除权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:payment:remove');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购付款-提交', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购付款' AND parent_id = @purchase_parent_id LIMIT 1), 5, '', NULL, 'F', 'purchase:payment:submit', NULL, '0', NOW(), NOW(), '采购付款提交权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:payment:submit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购付款-审批', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购付款' AND parent_id = @purchase_parent_id LIMIT 1), 6, '', NULL, 'F', 'purchase:payment:approve', NULL, '0', NOW(), NOW(), '采购付款审批权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:payment:approve');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购付款-确认', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购付款' AND parent_id = @purchase_parent_id LIMIT 1), 7, '', NULL, 'F', 'purchase:payment:confirm', NULL, '0', NOW(), NOW(), '采购付款确认权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:payment:confirm');

-- 11. 采购报表分析菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购报表', @purchase_parent_id, 10, '/purchase/report', 'Purchase/Report', 'C', 'purchase:report', 'BarChartOutlined', '0', NOW(), NOW(), '采购报表分析'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = '采购报表' AND parent_id = @purchase_parent_id);

-- 采购报表按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, status, create_time, update_time, remark)
SELECT '采购报表-查看', (SELECT menu_id FROM sys_menu WHERE menu_name = '采购报表' AND parent_id = @purchase_parent_id LIMIT 1), 1, '', NULL, 'F', 'purchase:report:view', NULL, '0', NOW(), NOW(), '采购报表查看权限'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'purchase:report:view');

