-- ============================================
-- 维修管理系统权限和菜单配置
-- ============================================

USE coal_erp;

-- ============================================
-- 1. 创建维修管理菜单
-- ============================================

-- 维修管理主菜单
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('维修管理', 0, 5, 'maintenance', NULL, 'M', NULL, 'tool', '0', NOW(), NOW(), '维修管理系统')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @maintenance_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '维修管理' AND parent_id = 0 LIMIT 1);

-- 维修工单管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('维修工单', @maintenance_menu_id, 1, 'work-order', 'Maintenance/WorkOrder/index', 'C', 'maintenance:workorder:list', 'file-text', '0', NOW(), NOW(), '维修工单管理')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @work_order_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '维修工单' AND parent_id = @maintenance_menu_id LIMIT 1);

-- 维修工单按钮权限
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @work_order_menu_id, 1, '', '', 'F', 'maintenance:workorder:add', '', '0', NOW(), NOW(), ''),
('编辑', @work_order_menu_id, 2, '', '', 'F', 'maintenance:workorder:edit', '', '0', NOW(), NOW(), ''),
('删除', @work_order_menu_id, 3, '', '', 'F', 'maintenance:workorder:remove', '', '0', NOW(), NOW(), ''),
('分配', @work_order_menu_id, 4, '', '', 'F', 'maintenance:workorder:assign', '', '0', NOW(), NOW(), ''),
('开始', @work_order_menu_id, 5, '', '', 'F', 'maintenance:workorder:start', '', '0', NOW(), NOW(), ''),
('完成', @work_order_menu_id, 6, '', '', 'F', 'maintenance:workorder:complete', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 预防性维护计划
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('维护计划', @maintenance_menu_id, 2, 'plan', 'Maintenance/Plan/index', 'C', 'maintenance:plan:list', 'calendar', '0', NOW(), NOW(), '预防性维护计划管理')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @plan_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '维护计划' AND parent_id = @maintenance_menu_id LIMIT 1);

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @plan_menu_id, 1, '', '', 'F', 'maintenance:plan:add', '', '0', NOW(), NOW(), ''),
('编辑', @plan_menu_id, 2, '', '', 'F', 'maintenance:plan:edit', '', '0', NOW(), NOW(), ''),
('删除', @plan_menu_id, 3, '', '', 'F', 'maintenance:plan:remove', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 维修团队管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('维修团队', @maintenance_menu_id, 3, 'team', 'Maintenance/Team/index', 'C', 'maintenance:team:list', 'team', '0', NOW(), NOW(), '维修团队管理')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @team_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '维修团队' AND parent_id = @maintenance_menu_id LIMIT 1);

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @team_menu_id, 1, '', '', 'F', 'maintenance:team:add', '', '0', NOW(), NOW(), ''),
('编辑', @team_menu_id, 2, '', '', 'F', 'maintenance:team:edit', '', '0', NOW(), NOW(), ''),
('删除', @team_menu_id, 3, '', '', 'F', 'maintenance:team:remove', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 维修备件管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('维修备件', @maintenance_menu_id, 4, 'part', 'Maintenance/Part/index', 'C', 'maintenance:part:list', 'inbox', '0', NOW(), NOW(), '维修备件领用管理')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @part_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '维修备件' AND parent_id = @maintenance_menu_id LIMIT 1);

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @part_menu_id, 1, '', '', 'F', 'maintenance:part:add', '', '0', NOW(), NOW(), ''),
('编辑', @part_menu_id, 2, '', '', 'F', 'maintenance:part:edit', '', '0', NOW(), NOW(), ''),
('删除', @part_menu_id, 3, '', '', 'F', 'maintenance:part:remove', '', '0', NOW(), NOW(), ''),
('审批', @part_menu_id, 4, '', '', 'F', 'maintenance:part:approve', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 维修质量管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('质量管理', @maintenance_menu_id, 5, 'quality', 'Maintenance/Quality/index', 'C', 'maintenance:quality:list', 'safety-certificate', '0', NOW(), NOW(), '维修质量管理')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @quality_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '质量管理' AND parent_id = @maintenance_menu_id LIMIT 1);

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @quality_menu_id, 1, '', '', 'F', 'maintenance:quality:add', '', '0', NOW(), NOW(), ''),
('编辑', @quality_menu_id, 2, '', '', 'F', 'maintenance:quality:edit', '', '0', NOW(), NOW(), ''),
('删除', @quality_menu_id, 3, '', '', 'F', 'maintenance:quality:remove', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 维修成本管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('成本管理', @maintenance_menu_id, 6, 'cost', 'Maintenance/Cost/index', 'C', 'maintenance:cost:list', 'dollar', '0', NOW(), NOW(), '维修成本管理')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @cost_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '成本管理' AND parent_id = @maintenance_menu_id LIMIT 1);

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @cost_menu_id, 1, '', '', 'F', 'maintenance:cost:add', '', '0', NOW(), NOW(), ''),
('编辑', @cost_menu_id, 2, '', '', 'F', 'maintenance:cost:edit', '', '0', NOW(), NOW(), ''),
('删除', @cost_menu_id, 3, '', '', 'F', 'maintenance:cost:remove', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 设备故障分析
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('故障分析', @maintenance_menu_id, 7, 'fault', 'Maintenance/Fault/index', 'C', 'maintenance:fault:list', 'warning', '0', NOW(), NOW(), '设备故障分析')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @fault_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '故障分析' AND parent_id = @maintenance_menu_id LIMIT 1);

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @fault_menu_id, 1, '', '', 'F', 'maintenance:fault:add', '', '0', NOW(), NOW(), ''),
('编辑', @fault_menu_id, 2, '', '', 'F', 'maintenance:fault:edit', '', '0', NOW(), NOW(), ''),
('删除', @fault_menu_id, 3, '', '', 'F', 'maintenance:fault:remove', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 维修绩效考核
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('绩效考核', @maintenance_menu_id, 8, 'performance', 'Maintenance/Performance/index', 'C', 'maintenance:performance:list', 'trophy', '0', NOW(), NOW(), '维修绩效考核')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @performance_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '绩效考核' AND parent_id = @maintenance_menu_id LIMIT 1);

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @performance_menu_id, 1, '', '', 'F', 'maintenance:performance:add', '', '0', NOW(), NOW(), ''),
('编辑', @performance_menu_id, 2, '', '', 'F', 'maintenance:performance:edit', '', '0', NOW(), NOW(), ''),
('删除', @performance_menu_id, 3, '', '', 'F', 'maintenance:performance:remove', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 维修报表分析
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('报表分析', @maintenance_menu_id, 9, 'report', 'Maintenance/Report/index', 'C', 'maintenance:report:list', 'bar-chart', '0', NOW(), NOW(), '维修报表分析')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @report_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '报表分析' AND parent_id = @maintenance_menu_id LIMIT 1);

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('查看', @report_menu_id, 1, '', '', 'F', 'maintenance:report:view', '', '0', NOW(), NOW(), ''),
('导出', @report_menu_id, 2, '', '', 'F', 'maintenance:report:export', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 移动维修支持
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('移动维修', @maintenance_menu_id, 10, 'mobile', 'Maintenance/Mobile/index', 'C', 'maintenance:mobile:list', 'mobile', '0', NOW(), NOW(), '移动维修支持')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @mobile_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '移动维修' AND parent_id = @maintenance_menu_id LIMIT 1);

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('查看', @mobile_menu_id, 1, '', '', 'F', 'maintenance:mobile:view', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- ============================================
-- 2. 为超级管理员角色分配所有维修管理权限
-- ============================================

-- 获取超级管理员角色ID
SET @admin_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'admin' OR role_name = '超级管理员' LIMIT 1);

-- 删除现有的维修管理权限（重新分配）
DELETE FROM sys_role_menu WHERE role_id = @admin_role_id AND menu_id IN (
    SELECT menu_id FROM sys_menu WHERE perms LIKE 'maintenance:%'
);

-- 为超级管理员分配所有维修管理权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @admin_role_id, menu_id
FROM sys_menu
WHERE perms LIKE 'maintenance:%'
ON DUPLICATE KEY UPDATE role_id = role_id;

-- ============================================
-- 3. 验证
-- ============================================

-- 查看维修管理菜单
SELECT menu_id, menu_name, parent_id, path, perms, menu_type
FROM sys_menu
WHERE menu_name = '维修管理' OR parent_id = @maintenance_menu_id
ORDER BY order_num;

-- 查看超级管理员拥有的维修管理权限数量
SELECT COUNT(*) AS permission_count
FROM sys_role_menu rm
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = @admin_role_id
AND m.perms LIKE 'maintenance:%';

