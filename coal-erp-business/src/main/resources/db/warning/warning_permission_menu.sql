-- ============================================
-- 预警管理系统权限和菜单配置
-- ============================================

USE coal_erp;

-- ============================================
-- 1. 创建预警管理菜单
-- ============================================

-- 预警管理主菜单
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('预警中心', 0, 5, 'warning', NULL, 'M', NULL, 'warning', '0', NOW(), NOW(), '预警管理系统')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @warning_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '预警中心' AND parent_id = 0 LIMIT 1);

-- 预警规则管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('预警规则', @warning_menu_id, 1, 'rule', 'Warning/Rule/index', 'C', 'warning:rule:list', 'file-text', '0', NOW(), NOW(), '预警规则管理')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @rule_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '预警规则' AND parent_id = @warning_menu_id LIMIT 1);

-- 预警规则按钮权限
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @rule_menu_id, 1, '', '', 'F', 'warning:rule:add', '', '0', NOW(), NOW(), ''),
('编辑', @rule_menu_id, 2, '', '', 'F', 'warning:rule:edit', '', '0', NOW(), NOW(), ''),
('删除', @rule_menu_id, 3, '', '', 'F', 'warning:rule:remove', '', '0', NOW(), NOW(), ''),
('启用/停用', @rule_menu_id, 4, '', '', 'F', 'warning:rule:enable', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 预警监控引擎
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('预警监控', @warning_menu_id, 2, 'monitor', 'Warning/Monitor/index', 'C', 'warning:monitor:list', 'radar-chart', '0', NOW(), NOW(), '预警监控引擎')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @monitor_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '预警监控' AND parent_id = @warning_menu_id LIMIT 1);

-- 预警监控按钮权限
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @monitor_menu_id, 1, '', '', 'F', 'warning:monitor:add', '', '0', NOW(), NOW(), ''),
('处理', @monitor_menu_id, 2, '', '', 'F', 'warning:monitor:handle', '', '0', NOW(), NOW(), ''),
('忽略', @monitor_menu_id, 3, '', '', 'F', 'warning:monitor:ignore', '', '0', NOW(), NOW(), ''),
('关闭', @monitor_menu_id, 4, '', '', 'F', 'warning:monitor:close', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 预警通知管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('预警通知', @warning_menu_id, 3, 'notification', 'Warning/Notification/index', 'C', 'warning:notification:list', 'bell', '0', NOW(), NOW(), '预警通知管理')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @notification_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '预警通知' AND parent_id = @warning_menu_id LIMIT 1);

-- 预警通知按钮权限
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @notification_menu_id, 1, '', '', 'F', 'warning:notification:add', '', '0', NOW(), NOW(), ''),
('重发', @notification_menu_id, 2, '', '', 'F', 'warning:notification:resend', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 预警处理跟踪
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('处理跟踪', @warning_menu_id, 4, 'tracking', 'Warning/Tracking/index', 'C', 'warning:tracking:list', 'audit', '0', NOW(), NOW(), '预警处理跟踪')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @tracking_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '处理跟踪' AND parent_id = @warning_menu_id LIMIT 1);

-- 预警处理跟踪按钮权限
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @tracking_menu_id, 1, '', '', 'F', 'warning:tracking:add', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 预警统计分析
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('统计分析', @warning_menu_id, 5, 'statistics', 'Warning/Statistics/index', 'C', 'warning:statistics:list', 'bar-chart', '0', NOW(), NOW(), '预警统计分析')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @statistics_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '统计分析' AND parent_id = @warning_menu_id LIMIT 1);

-- 预警级别管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('预警级别', @warning_menu_id, 6, 'level', 'Warning/Level/index', 'C', 'warning:level:list', 'flag', '0', NOW(), NOW(), '预警级别管理')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @level_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '预警级别' AND parent_id = @warning_menu_id LIMIT 1);

-- 预警级别按钮权限
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @level_menu_id, 1, '', '', 'F', 'warning:level:add', '', '0', NOW(), NOW(), ''),
('编辑', @level_menu_id, 2, '', '', 'F', 'warning:level:edit', '', '0', NOW(), NOW(), ''),
('删除', @level_menu_id, 3, '', '', 'F', 'warning:level:remove', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 预警模板管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('预警模板', @warning_menu_id, 7, 'template', 'Warning/Template/index', 'C', 'warning:template:list', 'file', '0', NOW(), NOW(), '预警模板管理')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @template_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '预警模板' AND parent_id = @warning_menu_id LIMIT 1);

-- 预警模板按钮权限
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('新增', @template_menu_id, 1, '', '', 'F', 'warning:template:add', '', '0', NOW(), NOW(), ''),
('编辑', @template_menu_id, 2, '', '', 'F', 'warning:template:edit', '', '0', NOW(), NOW(), ''),
('删除', @template_menu_id, 3, '', '', 'F', 'warning:template:remove', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 预警渠道管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('预警渠道', @warning_menu_id, 8, 'channel', 'Warning/Channel/index', 'C', 'warning:channel:list', 'api', '0', NOW(), NOW(), '预警渠道管理')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @channel_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '预警渠道' AND parent_id = @warning_menu_id LIMIT 1);

-- 预警渠道按钮权限
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('编辑', @channel_menu_id, 1, '', '', 'F', 'warning:channel:edit', '', '0', NOW(), NOW(), ''),
('启用/停用', @channel_menu_id, 2, '', '', 'F', 'warning:channel:enable', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 预警看板
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('预警看板', @warning_menu_id, 9, 'dashboard', 'Warning/Dashboard/index', 'C', 'warning:dashboard:view', 'dashboard', '0', NOW(), NOW(), '预警看板展示')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 预警报表
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('预警报表', @warning_menu_id, 10, 'report', 'Warning/Report/index', 'C', 'warning:report:list', 'line-chart', '0', NOW(), NOW(), '预警报表分析')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

SET @report_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '预警报表' AND parent_id = @warning_menu_id LIMIT 1);

-- 预警报表按钮权限
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`, `status`, `create_time`, `update_time`, `remark`)
VALUES 
('查看', @report_menu_id, 1, '', '', 'F', 'warning:report:view', '', '0', NOW(), NOW(), '')
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- ============================================
-- 2. 为超级管理员角色分配所有权限
-- ============================================

SET @admin_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'admin' LIMIT 1);

-- 获取所有预警管理相关的菜单ID并分配给超级管理员
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT @admin_role_id, menu_id 
FROM sys_menu 
WHERE menu_name IN ('预警中心', '预警规则', '预警监控', '预警通知', '处理跟踪', '统计分析', '预警级别', '预警模板', '预警渠道', '预警看板', '预警报表')
   OR perms LIKE 'warning:%'
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);

