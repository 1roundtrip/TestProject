-- 预警记录表
CREATE TABLE IF NOT EXISTS `warning_alert` (
  `alert_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '预警ID',
  `alert_type` varchar(50) DEFAULT NULL COMMENT '预警类型（EXPLOSION_PROOF-防爆设备）',
  `alert_level` varchar(20) DEFAULT NULL COMMENT '预警级别（YELLOW-黄色，ORANGE-橙色，RED-红色）',
  `asset_id` bigint(20) DEFAULT NULL COMMENT '资产ID',
  `asset_code` varchar(50) DEFAULT NULL COMMENT '资产编码',
  `asset_name` varchar(100) DEFAULT NULL COMMENT '资产名称',
  `alert_title` varchar(200) DEFAULT NULL COMMENT '预警标题',
  `alert_content` varchar(500) DEFAULT NULL COMMENT '预警内容',
  `expire_date` date DEFAULT NULL COMMENT '到期日期',
  `days_remaining` int(11) DEFAULT NULL COMMENT '剩余天数',
  `status` char(1) DEFAULT '0' COMMENT '状态（0-未处理，1-已处理）',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`alert_id`),
  KEY `idx_asset_id` (`asset_id`),
  KEY `idx_alert_level` (`alert_level`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警记录表';















