-- ============================================
-- 采购付款管理表结构
-- ============================================

USE coal_erp;

-- 采购付款表
CREATE TABLE IF NOT EXISTS `purchase_payment` (
  `payment_id` bigint NOT NULL AUTO_INCREMENT COMMENT '付款ID',
  `payment_no` varchar(50) NOT NULL COMMENT '付款单号',
  `order_id` bigint DEFAULT NULL COMMENT '关联订单ID',
  `order_no` varchar(50) DEFAULT NULL COMMENT '关联订单号',
  `contract_id` bigint DEFAULT NULL COMMENT '关联合同ID',
  `contract_no` varchar(50) DEFAULT NULL COMMENT '关联合同号',
  `supplier_id` bigint NOT NULL COMMENT '供应商ID',
  `supplier_name` varchar(200) NOT NULL COMMENT '供应商名称',
  `payment_type` varchar(50) DEFAULT NULL COMMENT '付款类型(ADVANCE-预付款,PROGRESS-进度款,FINAL-尾款,OTHER-其他)',
  `payment_date` date NOT NULL COMMENT '付款日期',
  `payment_method` varchar(50) DEFAULT NULL COMMENT '付款方式(TRANSFER-转账,CHECK-支票,CASH-现金,OTHER-其他)',
  `currency` varchar(10) DEFAULT 'CNY' COMMENT '币种',
  `payment_amount` decimal(16,2) DEFAULT '0.00' COMMENT '付款金额',
  `order_amount` decimal(16,2) DEFAULT '0.00' COMMENT '订单金额',
  `paid_amount` decimal(16,2) DEFAULT '0.00' COMMENT '已付金额',
  `balance_amount` decimal(16,2) DEFAULT '0.00' COMMENT '余额',
  `bank_name` varchar(100) DEFAULT NULL COMMENT '付款银行',
  `bank_account` varchar(50) DEFAULT NULL COMMENT '付款账号',
  `account_name` varchar(100) DEFAULT NULL COMMENT '账户名称',
  `voucher_no` varchar(50) DEFAULT NULL COMMENT '关联凭证号',
  `status` varchar(20) DEFAULT 'DRAFT' COMMENT '状态(DRAFT-草稿,SUBMITTED-已提交,APPROVED-已审批,PAID-已付款,REJECTED-已驳回,CANCELLED-已取消)',
  `approve_user_id` bigint DEFAULT NULL COMMENT '审批人ID',
  `approve_user_name` varchar(50) DEFAULT NULL COMMENT '审批人姓名',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `pay_user_id` bigint DEFAULT NULL COMMENT '付款人ID',
  `pay_user_name` varchar(50) DEFAULT NULL COMMENT '付款人姓名',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`payment_id`),
  UNIQUE KEY `uk_payment_no` (`payment_no`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_contract_id` (`contract_id`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_status` (`status`),
  KEY `idx_payment_date` (`payment_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购付款表';

-- 采购付款明细表
CREATE TABLE IF NOT EXISTS `purchase_payment_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `payment_id` bigint NOT NULL COMMENT '付款ID',
  `order_id` bigint DEFAULT NULL COMMENT '关联订单ID',
  `order_no` varchar(50) DEFAULT NULL COMMENT '关联订单号',
  `receiving_id` bigint DEFAULT NULL COMMENT '关联收货ID',
  `receiving_no` varchar(50) DEFAULT NULL COMMENT '关联收货单号',
  `item_name` varchar(200) DEFAULT NULL COMMENT '物料名称',
  `payment_amount` decimal(16,2) DEFAULT '0.00' COMMENT '付款金额',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`detail_id`),
  KEY `idx_payment_id` (`payment_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_receiving_id` (`receiving_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购付款明细表';

