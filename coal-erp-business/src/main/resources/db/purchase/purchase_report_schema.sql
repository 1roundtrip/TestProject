-- ============================================
-- 采购报表分析相关表结构
-- ============================================

USE coal_erp;

-- 采购统计汇总表（用于报表分析，可定期汇总）
CREATE TABLE IF NOT EXISTS `purchase_statistics` (
  `stat_id` bigint NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `stat_date` date NOT NULL COMMENT '统计日期',
  `stat_type` varchar(50) NOT NULL COMMENT '统计类型(DAILY-日报,MONTHLY-月报,QUARTERLY-季报,YEARLY-年报)',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `dept_name` varchar(100) DEFAULT NULL COMMENT '部门名称',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商ID',
  `supplier_name` varchar(200) DEFAULT NULL COMMENT '供应商名称',
  `order_count` int DEFAULT '0' COMMENT '订单数量',
  `order_amount` decimal(16,2) DEFAULT '0.00' COMMENT '订单金额',
  `received_amount` decimal(16,2) DEFAULT '0.00' COMMENT '收货金额',
  `paid_amount` decimal(16,2) DEFAULT '0.00' COMMENT '付款金额',
  `return_amount` decimal(16,2) DEFAULT '0.00' COMMENT '退货金额',
  `quality_pass_rate` decimal(5,2) DEFAULT '0.00' COMMENT '质检合格率(%)',
  `on_time_delivery_rate` decimal(5,2) DEFAULT '0.00' COMMENT '准时交货率(%)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`stat_id`),
  UNIQUE KEY `uk_stat_date_type_dept_supplier` (`stat_date`, `stat_type`, `dept_id`, `supplier_id`),
  KEY `idx_stat_date` (`stat_date`),
  KEY `idx_stat_type` (`stat_type`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_supplier_id` (`supplier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购统计汇总表';

-- 采购预警配置表
CREATE TABLE IF NOT EXISTS `purchase_warning_config` (
  `config_id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `warning_type` varchar(50) NOT NULL COMMENT '预警类型(ORDER_OVERDUE-订单超期,PAYMENT_OVERDUE-付款超期,QUALITY_ISSUE-质量问题,SUPPLIER_RISK-供应商风险)',
  `warning_name` varchar(100) NOT NULL COMMENT '预警名称',
  `warning_rule` text DEFAULT NULL COMMENT '预警规则(JSON格式)',
  `warning_level` varchar(20) DEFAULT 'MEDIUM' COMMENT '预警级别(HIGH-高,MEDIUM-中,LOW-低)',
  `is_enabled` tinyint(1) DEFAULT '1' COMMENT '是否启用(0-否,1-是)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`config_id`),
  UNIQUE KEY `uk_warning_type` (`warning_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购预警配置表';

-- 初始化预警配置
INSERT INTO `purchase_warning_config` (`warning_type`, `warning_name`, `warning_rule`, `warning_level`, `is_enabled`) VALUES
('ORDER_OVERDUE', '订单超期预警', '{"days_before_delivery": 3, "days_after_delivery": 7}', 'HIGH', 1),
('PAYMENT_OVERDUE', '付款超期预警', '{"days_after_due": 7}', 'MEDIUM', 1),
('QUALITY_ISSUE', '质量问题预警', '{"quality_rate_threshold": 90}', 'HIGH', 1),
('SUPPLIER_RISK', '供应商风险预警', '{"evaluation_score_threshold": 6}', 'MEDIUM', 1)
ON DUPLICATE KEY UPDATE warning_name = VALUES(warning_name);

