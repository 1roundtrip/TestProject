-- ============================================
-- 采购退货管理表结构
-- ============================================

USE coal_erp;

-- 采购退货表
CREATE TABLE IF NOT EXISTS `purchase_return` (
  `return_id` bigint NOT NULL AUTO_INCREMENT COMMENT '退货ID',
  `return_no` varchar(50) NOT NULL COMMENT '退货单号',
  `receiving_id` bigint DEFAULT NULL COMMENT '关联收货ID',
  `receiving_no` varchar(50) DEFAULT NULL COMMENT '关联收货单号',
  `order_id` bigint DEFAULT NULL COMMENT '关联订单ID',
  `order_no` varchar(50) DEFAULT NULL COMMENT '关联订单号',
  `supplier_id` bigint NOT NULL COMMENT '供应商ID',
  `supplier_name` varchar(200) NOT NULL COMMENT '供应商名称',
  `return_date` date NOT NULL COMMENT '退货日期',
  `return_type` varchar(50) DEFAULT NULL COMMENT '退货类型(QUALITY-质量问题,QUANTITY-数量错误,SPECIFICATION-规格不符,OTHER-其他)',
  `return_reason` varchar(500) DEFAULT NULL COMMENT '退货原因',
  `total_amount` decimal(16,2) DEFAULT '0.00' COMMENT '退货总金额',
  `logistics_company` varchar(100) DEFAULT NULL COMMENT '物流公司',
  `logistics_no` varchar(50) DEFAULT NULL COMMENT '物流单号',
  `status` varchar(20) DEFAULT 'DRAFT' COMMENT '状态(DRAFT-草稿,SUBMITTED-已提交,APPROVED-已审批,CONFIRMED-已确认,RETURNING-退货中,RETURNED-已退货,REJECTED-已驳回,CANCELLED-已取消)',
  `approve_user_id` bigint DEFAULT NULL COMMENT '审批人ID',
  `approve_user_name` varchar(50) DEFAULT NULL COMMENT '审批人姓名',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `return_user_id` bigint DEFAULT NULL COMMENT '退货人ID',
  `return_user_name` varchar(50) DEFAULT NULL COMMENT '退货人姓名',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`return_id`),
  UNIQUE KEY `uk_return_no` (`return_no`),
  KEY `idx_receiving_id` (`receiving_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_status` (`status`),
  KEY `idx_return_date` (`return_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购退货表';

-- 采购退货明细表
CREATE TABLE IF NOT EXISTS `purchase_return_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `return_id` bigint NOT NULL COMMENT '退货ID',
  `receiving_detail_id` bigint DEFAULT NULL COMMENT '关联收货明细ID',
  `item_name` varchar(200) NOT NULL COMMENT '物料名称',
  `item_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `specification` varchar(200) DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `return_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '退货数量',
  `unit_price` decimal(16,2) DEFAULT '0.00' COMMENT '单价',
  `total_amount` decimal(16,2) DEFAULT '0.00' COMMENT '总金额',
  `return_reason` varchar(500) DEFAULT NULL COMMENT '退货原因',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次号',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`detail_id`),
  KEY `idx_return_id` (`return_id`),
  KEY `idx_receiving_detail_id` (`receiving_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购退货明细表';

