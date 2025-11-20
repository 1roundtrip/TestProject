-- ============================================
-- 采购收货管理表结构
-- ============================================

USE coal_erp;

-- 采购收货表
CREATE TABLE IF NOT EXISTS `purchase_receiving` (
  `receiving_id` bigint NOT NULL AUTO_INCREMENT COMMENT '收货ID',
  `receiving_no` varchar(50) NOT NULL COMMENT '收货单号',
  `order_id` bigint NOT NULL COMMENT '关联订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '关联订单号',
  `contract_id` bigint DEFAULT NULL COMMENT '关联合同ID',
  `contract_no` varchar(50) DEFAULT NULL COMMENT '关联合同号',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商ID',
  `supplier_name` varchar(200) DEFAULT NULL COMMENT '供应商名称',
  `receiving_date` date NOT NULL COMMENT '收货日期',
  `warehouse` varchar(100) DEFAULT NULL COMMENT '仓库',
  `location` varchar(200) DEFAULT NULL COMMENT '存放位置',
  `delivery_no` varchar(50) DEFAULT NULL COMMENT '送货单号',
  `logistics_company` varchar(100) DEFAULT NULL COMMENT '物流公司',
  `logistics_no` varchar(50) DEFAULT NULL COMMENT '物流单号',
  `total_amount` decimal(16,2) DEFAULT '0.00' COMMENT '收货总金额',
  `status` varchar(20) DEFAULT 'DRAFT' COMMENT '状态(DRAFT-草稿,CONFIRMED-已确认,QUALITY_CHECKING-质检中,QUALITY_PASSED-质检通过,QUALITY_FAILED-质检不合格,STORED-已入库,CANCELLED-已取消)',
  `receiver_id` bigint DEFAULT NULL COMMENT '收货人ID',
  `receiver_name` varchar(50) DEFAULT NULL COMMENT '收货人姓名',
  `warehouse_keeper_id` bigint DEFAULT NULL COMMENT '仓管员ID',
  `warehouse_keeper_name` varchar(50) DEFAULT NULL COMMENT '仓管员姓名',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`receiving_id`),
  UNIQUE KEY `uk_receiving_no` (`receiving_no`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_contract_id` (`contract_id`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_status` (`status`),
  KEY `idx_receiving_date` (`receiving_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购收货表';

-- 采购收货明细表
CREATE TABLE IF NOT EXISTS `purchase_receiving_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `receiving_id` bigint NOT NULL COMMENT '收货ID',
  `order_detail_id` bigint DEFAULT NULL COMMENT '关联订单明细ID',
  `item_name` varchar(200) NOT NULL COMMENT '物料名称',
  `item_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `specification` varchar(200) DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `order_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '订单数量',
  `received_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '收货数量',
  `qualified_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '合格数量',
  `unqualified_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '不合格数量',
  `unit_price` decimal(16,2) DEFAULT '0.00' COMMENT '单价',
  `total_amount` decimal(16,2) DEFAULT '0.00' COMMENT '总金额',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次号',
  `production_date` date DEFAULT NULL COMMENT '生产日期',
  `expiry_date` date DEFAULT NULL COMMENT '有效期至',
  `quality_status` varchar(20) DEFAULT NULL COMMENT '质检状态(PENDING-待质检,PASSED-合格,FAILED-不合格)',
  `storage_status` varchar(20) DEFAULT 'PENDING' COMMENT '入库状态(PENDING-待入库,STORED-已入库)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`detail_id`),
  KEY `idx_receiving_id` (`receiving_id`),
  KEY `idx_order_detail_id` (`order_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购收货明细表';

