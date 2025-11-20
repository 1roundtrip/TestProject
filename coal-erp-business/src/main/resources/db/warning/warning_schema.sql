-- ============================================
-- 预警管理系统数据库表结构
-- ============================================

USE coal_erp;

-- ============================================
-- 1. 预警规则管理
-- ============================================

-- 预警规则表
CREATE TABLE IF NOT EXISTS `warning_rule` (
  `rule_id` bigint NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `rule_code` varchar(50) NOT NULL COMMENT '规则编码',
  `rule_name` varchar(100) NOT NULL COMMENT '规则名称',
  `rule_type` varchar(50) NOT NULL COMMENT '规则类型(ASSET-资产,INVENTORY-库存,PURCHASE-采购,MAINTENANCE-维修,FINANCE-财务)',
  `warning_category` varchar(50) NOT NULL COMMENT '预警分类',
  `warning_level_id` bigint DEFAULT NULL COMMENT '预警级别ID',
  `rule_condition` text COMMENT '规则条件(JSON格式)',
  `rule_expression` text COMMENT '规则表达式',
  `check_frequency` varchar(20) DEFAULT 'REALTIME' COMMENT '检查频率(REALTIME-实时,HOURLY-每小时,DAILY-每天,WEEKLY-每周)',
  `is_enabled` tinyint(1) DEFAULT '1' COMMENT '是否启用(0-否,1-是)',
  `priority` int DEFAULT '0' COMMENT '优先级(数字越大优先级越高)',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`rule_id`),
  UNIQUE KEY `uk_rule_code` (`rule_code`),
  KEY `idx_rule_type` (`rule_type`),
  KEY `idx_warning_category` (`warning_category`),
  KEY `idx_is_enabled` (`is_enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警规则表';

-- ============================================
-- 2. 预警级别管理
-- ============================================

-- 预警级别表
CREATE TABLE IF NOT EXISTS `warning_level` (
  `level_id` bigint NOT NULL AUTO_INCREMENT COMMENT '级别ID',
  `level_code` varchar(20) NOT NULL COMMENT '级别编码',
  `level_name` varchar(50) NOT NULL COMMENT '级别名称',
  `level_color` varchar(20) DEFAULT NULL COMMENT '级别颜色',
  `level_order` int DEFAULT '0' COMMENT '级别排序',
  `notification_channels` varchar(200) DEFAULT NULL COMMENT '通知渠道(多个用逗号分隔)',
  `escalation_rule` text COMMENT '升级规则(JSON格式)',
  `is_enabled` tinyint(1) DEFAULT '1' COMMENT '是否启用(0-否,1-是)',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`level_id`),
  UNIQUE KEY `uk_level_code` (`level_code`),
  KEY `idx_level_order` (`level_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警级别表';

-- ============================================
-- 3. 预警监控引擎（预警记录表）
-- ============================================

-- 预警记录表（扩展原有warning_alert表）
CREATE TABLE IF NOT EXISTS `warning_record` (
  `record_id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `rule_id` bigint DEFAULT NULL COMMENT '规则ID',
  `rule_code` varchar(50) DEFAULT NULL COMMENT '规则编码',
  `rule_name` varchar(100) DEFAULT NULL COMMENT '规则名称',
  `warning_type` varchar(50) NOT NULL COMMENT '预警类型',
  `warning_category` varchar(50) DEFAULT NULL COMMENT '预警分类',
  `warning_level_id` bigint DEFAULT NULL COMMENT '预警级别ID',
  `warning_level_code` varchar(20) DEFAULT NULL COMMENT '预警级别编码',
  `warning_level_name` varchar(50) DEFAULT NULL COMMENT '预警级别名称',
  `warning_title` varchar(200) NOT NULL COMMENT '预警标题',
  `warning_content` text COMMENT '预警内容',
  `warning_data` text COMMENT '预警数据(JSON格式)',
  `source_type` varchar(50) DEFAULT NULL COMMENT '来源类型(ASSET-资产,INVENTORY-库存,PURCHASE-采购,MAINTENANCE-维修,FINANCE-财务)',
  `source_id` bigint DEFAULT NULL COMMENT '来源ID',
  `source_code` varchar(50) DEFAULT NULL COMMENT '来源编码',
  `source_name` varchar(200) DEFAULT NULL COMMENT '来源名称',
  `trigger_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '触发时间',
  `status` varchar(20) DEFAULT 'PENDING' COMMENT '状态(PENDING-待处理,PROCESSING-处理中,RESOLVED-已解决,IGNORED-已忽略,CLOSED-已关闭)',
  `handler_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `handler_name` varchar(50) DEFAULT NULL COMMENT '处理人姓名',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_result` text COMMENT '处理结果',
  `resolve_time` datetime DEFAULT NULL COMMENT '解决时间',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`record_id`),
  KEY `idx_rule_id` (`rule_id`),
  KEY `idx_warning_type` (`warning_type`),
  KEY `idx_warning_level_id` (`warning_level_id`),
  KEY `idx_source_type` (`source_type`),
  KEY `idx_source_id` (`source_id`),
  KEY `idx_status` (`status`),
  KEY `idx_trigger_time` (`trigger_time`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警记录表';

-- ============================================
-- 4. 预警通知管理
-- ============================================

-- 预警通知表
CREATE TABLE IF NOT EXISTS `warning_notification` (
  `notification_id` bigint NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `record_id` bigint NOT NULL COMMENT '预警记录ID',
  `channel_type` varchar(20) NOT NULL COMMENT '渠道类型(IN_APP-站内信,EMAIL-邮件,SMS-短信,WECHAT-微信,DINGTALK-钉钉)',
  `recipient_id` bigint DEFAULT NULL COMMENT '接收人ID',
  `recipient_name` varchar(50) DEFAULT NULL COMMENT '接收人姓名',
  `recipient_email` varchar(100) DEFAULT NULL COMMENT '接收人邮箱',
  `recipient_phone` varchar(20) DEFAULT NULL COMMENT '接收人手机',
  `notification_title` varchar(200) NOT NULL COMMENT '通知标题',
  `notification_content` text COMMENT '通知内容',
  `template_id` bigint DEFAULT NULL COMMENT '模板ID',
  `send_status` varchar(20) DEFAULT 'PENDING' COMMENT '发送状态(PENDING-待发送,SENDING-发送中,SUCCESS-成功,FAILED-失败)',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `send_result` varchar(500) DEFAULT NULL COMMENT '发送结果',
  `retry_count` int DEFAULT '0' COMMENT '重试次数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`notification_id`),
  KEY `idx_record_id` (`record_id`),
  KEY `idx_channel_type` (`channel_type`),
  KEY `idx_recipient_id` (`recipient_id`),
  KEY `idx_send_status` (`send_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警通知表';

-- ============================================
-- 5. 预警处理跟踪
-- ============================================

-- 预警处理记录表
CREATE TABLE IF NOT EXISTS `warning_handle_record` (
  `handle_id` bigint NOT NULL AUTO_INCREMENT COMMENT '处理记录ID',
  `record_id` bigint NOT NULL COMMENT '预警记录ID',
  `handle_type` varchar(20) DEFAULT NULL COMMENT '处理类型(ASSIGN-分配,PROCESS-处理,RESOLVE-解决,ESCALATE-升级,TRANSFER-转交)',
  `handler_id` bigint NOT NULL COMMENT '处理人ID',
  `handler_name` varchar(50) NOT NULL COMMENT '处理人姓名',
  `handle_action` varchar(50) DEFAULT NULL COMMENT '处理动作',
  `handle_content` text COMMENT '处理内容',
  `handle_attachment` varchar(500) DEFAULT NULL COMMENT '处理附件',
  `next_handler_id` bigint DEFAULT NULL COMMENT '下一处理人ID',
  `next_handler_name` varchar(50) DEFAULT NULL COMMENT '下一处理人姓名',
  `handle_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '处理时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`handle_id`),
  KEY `idx_record_id` (`record_id`),
  KEY `idx_handler_id` (`handler_id`),
  KEY `idx_handle_time` (`handle_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警处理记录表';

-- ============================================
-- 6. 预警统计分析
-- ============================================

-- 预警统计表
CREATE TABLE IF NOT EXISTS `warning_statistics` (
  `stat_id` bigint NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `stat_date` date NOT NULL COMMENT '统计日期',
  `stat_type` varchar(20) DEFAULT 'DAILY' COMMENT '统计类型(DAILY-日,WEEKLY-周,MONTHLY-月)',
  `warning_type` varchar(50) DEFAULT NULL COMMENT '预警类型',
  `warning_level_id` bigint DEFAULT NULL COMMENT '预警级别ID',
  `total_count` int DEFAULT '0' COMMENT '总数量',
  `pending_count` int DEFAULT '0' COMMENT '待处理数量',
  `processing_count` int DEFAULT '0' COMMENT '处理中数量',
  `resolved_count` int DEFAULT '0' COMMENT '已解决数量',
  `ignored_count` int DEFAULT '0' COMMENT '已忽略数量',
  `avg_resolve_time` int DEFAULT NULL COMMENT '平均解决时间(分钟)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`stat_id`),
  UNIQUE KEY `uk_stat_date_type_level` (`stat_date`, `stat_type`, `warning_type`, `warning_level_id`),
  KEY `idx_stat_date` (`stat_date`),
  KEY `idx_warning_type` (`warning_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警统计表';

-- ============================================
-- 7. 预警模板管理
-- ============================================

-- 预警模板表
CREATE TABLE IF NOT EXISTS `warning_template` (
  `template_id` bigint NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `template_code` varchar(50) NOT NULL COMMENT '模板编码',
  `template_name` varchar(100) NOT NULL COMMENT '模板名称',
  `template_type` varchar(20) NOT NULL COMMENT '模板类型(IN_APP-站内信,EMAIL-邮件,SMS-短信,WECHAT-微信,DINGTALK-钉钉)',
  `warning_type` varchar(50) DEFAULT NULL COMMENT '预警类型',
  `template_subject` varchar(200) DEFAULT NULL COMMENT '模板主题',
  `template_content` text NOT NULL COMMENT '模板内容',
  `template_variables` text COMMENT '模板变量(JSON格式)',
  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否默认模板(0-否,1-是)',
  `is_enabled` tinyint(1) DEFAULT '1' COMMENT '是否启用(0-否,1-是)',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`template_id`),
  UNIQUE KEY `uk_template_code` (`template_code`),
  KEY `idx_template_type` (`template_type`),
  KEY `idx_warning_type` (`warning_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警模板表';

-- ============================================
-- 8. 预警渠道管理
-- ============================================

-- 预警渠道配置表
CREATE TABLE IF NOT EXISTS `warning_channel` (
  `channel_id` bigint NOT NULL AUTO_INCREMENT COMMENT '渠道ID',
  `channel_code` varchar(20) NOT NULL COMMENT '渠道编码',
  `channel_name` varchar(50) NOT NULL COMMENT '渠道名称',
  `channel_type` varchar(20) NOT NULL COMMENT '渠道类型(IN_APP-站内信,EMAIL-邮件,SMS-短信,WECHAT-微信,DINGTALK-钉钉)',
  `channel_config` text COMMENT '渠道配置(JSON格式)',
  `is_enabled` tinyint(1) DEFAULT '1' COMMENT '是否启用(0-否,1-是)',
  `priority` int DEFAULT '0' COMMENT '优先级(数字越大优先级越高)',
  `daily_limit` int DEFAULT NULL COMMENT '每日发送限制',
  `current_count` int DEFAULT '0' COMMENT '当前发送数量',
  `reset_time` datetime DEFAULT NULL COMMENT '重置时间',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`channel_id`),
  UNIQUE KEY `uk_channel_code` (`channel_code`),
  KEY `idx_channel_type` (`channel_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警渠道配置表';

-- ============================================
-- 9. 预警看板展示（使用预警记录表，无需单独表）
-- ============================================

-- ============================================
-- 10. 预警报表分析（使用预警统计表，无需单独表）
-- ============================================

-- ============================================
-- 初始化数据
-- ============================================

-- 初始化预警级别
INSERT INTO `warning_level` (`level_code`, `level_name`, `level_color`, `level_order`, `notification_channels`, `is_enabled`, `create_time`) VALUES
('LOW', '低', '#52c41a', 1, 'IN_APP', 1, NOW()),
('MEDIUM', '中', '#faad14', 2, 'IN_APP,EMAIL', 1, NOW()),
('HIGH', '高', '#fa8c16', 3, 'IN_APP,EMAIL,SMS', 1, NOW()),
('CRITICAL', '紧急', '#f5222d', 4, 'IN_APP,EMAIL,SMS,WECHAT', 1, NOW())
ON DUPLICATE KEY UPDATE `level_name` = VALUES(`level_name`);

-- 初始化预警渠道
INSERT INTO `warning_channel` (`channel_code`, `channel_name`, `channel_type`, `channel_config`, `is_enabled`, `priority`, `create_time`) VALUES
('IN_APP', '站内信', 'IN_APP', '{}', 1, 1, NOW()),
('EMAIL', '邮件', 'EMAIL', '{"smtp_host":"","smtp_port":25,"smtp_user":"","smtp_password":""}', 1, 2, NOW()),
('SMS', '短信', 'SMS', '{"api_url":"","api_key":"","api_secret":""}', 0, 3, NOW()),
('WECHAT', '微信', 'WECHAT', '{"app_id":"","app_secret":""}', 0, 4, NOW()),
('DINGTALK', '钉钉', 'DINGTALK', '{"webhook_url":""}', 0, 5, NOW())
ON DUPLICATE KEY UPDATE `channel_name` = VALUES(`channel_name`);

-- 初始化预警模板
INSERT INTO `warning_template` (`template_code`, `template_name`, `template_type`, `template_subject`, `template_content`, `is_default`, `is_enabled`, `create_time`) VALUES
('DEFAULT_IN_APP', '默认站内信模板', 'IN_APP', NULL, '【预警通知】{warning_title}\n{warning_content}\n触发时间：{trigger_time}', 1, 1, NOW()),
('DEFAULT_EMAIL', '默认邮件模板', 'EMAIL', '【预警通知】{warning_title}', '<h2>预警通知</h2><p><strong>预警标题：</strong>{warning_title}</p><p><strong>预警内容：</strong>{warning_content}</p><p><strong>触发时间：</strong>{trigger_time}</p>', 1, 1, NOW()),
('DEFAULT_SMS', '默认短信模板', 'SMS', NULL, '【预警通知】{warning_title}，请及时处理。', 1, 1, NOW())
ON DUPLICATE KEY UPDATE `template_name` = VALUES(`template_name`);