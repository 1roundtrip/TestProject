-- ============================================
-- 采购管理系统与其他模块集成表结构
-- ============================================

USE coal_erp;

-- 采购与资产入库关联表
CREATE TABLE IF NOT EXISTS `purchase_asset_storage_link` (
  `link_id` bigint NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `receiving_id` bigint NOT NULL COMMENT '收货ID',
  `receiving_no` varchar(50) NOT NULL COMMENT '收货单号',
  `storage_id` bigint NOT NULL COMMENT '资产入库ID',
  `storage_no` varchar(50) NOT NULL COMMENT '资产入库单号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`link_id`),
  UNIQUE KEY `uk_receiving_storage` (`receiving_id`, `storage_id`),
  KEY `idx_receiving_id` (`receiving_id`),
  KEY `idx_storage_id` (`storage_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购与资产入库关联表';

-- 采购与财务付款关联表
CREATE TABLE IF NOT EXISTS `purchase_finance_payment_link` (
  `link_id` bigint NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `purchase_payment_id` bigint NOT NULL COMMENT '采购付款ID',
  `purchase_payment_no` varchar(50) NOT NULL COMMENT '采购付款单号',
  `finance_payment_id` bigint NOT NULL COMMENT '财务付款ID',
  `finance_payment_no` varchar(50) NOT NULL COMMENT '财务付款单号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`link_id`),
  UNIQUE KEY `uk_purchase_finance_payment` (`purchase_payment_id`, `finance_payment_id`),
  KEY `idx_purchase_payment_id` (`purchase_payment_id`),
  KEY `idx_finance_payment_id` (`finance_payment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购与财务付款关联表';

-- 采购与库存入库关联表
CREATE TABLE IF NOT EXISTS `purchase_inventory_link` (
  `link_id` bigint NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `receiving_id` bigint NOT NULL COMMENT '收货ID',
  `receiving_no` varchar(50) NOT NULL COMMENT '收货单号',
  `inventory_id` bigint DEFAULT NULL COMMENT '库存ID',
  `inventory_type` varchar(50) DEFAULT NULL COMMENT '库存类型',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`link_id`),
  KEY `idx_receiving_id` (`receiving_id`),
  KEY `idx_inventory_id` (`inventory_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购与库存入库关联表';

-- 采购预警记录表（与预警中心集成）
CREATE TABLE IF NOT EXISTS `purchase_warning_record` (
  `warning_id` bigint NOT NULL AUTO_INCREMENT COMMENT '预警ID',
  `warning_type` varchar(50) NOT NULL COMMENT '预警类型',
  `warning_level` varchar(20) DEFAULT NULL COMMENT '预警级别',
  `order_id` bigint DEFAULT NULL COMMENT '关联订单ID',
  `order_no` varchar(50) DEFAULT NULL COMMENT '关联订单号',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商ID',
  `supplier_name` varchar(200) DEFAULT NULL COMMENT '供应商名称',
  `warning_content` varchar(500) DEFAULT NULL COMMENT '预警内容',
  `warning_date` date NOT NULL COMMENT '预警日期',
  `status` varchar(20) DEFAULT 'PENDING' COMMENT '状态(PENDING-待处理,PROCESSING-处理中,RESOLVED-已解决,IGNORED-已忽略)',
  `handle_user_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `handle_user_name` varchar(50) DEFAULT NULL COMMENT '处理人姓名',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_result` varchar(500) DEFAULT NULL COMMENT '处理结果',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`warning_id`),
  KEY `idx_warning_type` (`warning_type`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_status` (`status`),
  KEY `idx_warning_date` (`warning_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购预警记录表';

