-- ============================================
-- 采购合同管理表结构
-- ============================================

USE coal_erp;

-- 采购合同表
CREATE TABLE IF NOT EXISTS `purchase_contract` (
  `contract_id` bigint NOT NULL AUTO_INCREMENT COMMENT '合同ID',
  `contract_no` varchar(50) NOT NULL COMMENT '合同编号',
  `contract_name` varchar(200) NOT NULL COMMENT '合同名称',
  `order_id` bigint DEFAULT NULL COMMENT '关联订单ID',
  `order_no` varchar(50) DEFAULT NULL COMMENT '关联订单号',
  `supplier_id` bigint NOT NULL COMMENT '供应商ID',
  `supplier_name` varchar(200) NOT NULL COMMENT '供应商名称',
  `contract_type` varchar(50) DEFAULT NULL COMMENT '合同类型(FRAMEWORK-框架合同,SPECIFIC-具体合同)',
  `contract_date` date NOT NULL COMMENT '合同签订日期',
  `start_date` date NOT NULL COMMENT '合同开始日期',
  `end_date` date NOT NULL COMMENT '合同结束日期',
  `total_amount` decimal(16,2) DEFAULT '0.00' COMMENT '合同总金额',
  `currency` varchar(10) DEFAULT 'CNY' COMMENT '币种',
  `payment_method` varchar(100) DEFAULT NULL COMMENT '付款方式',
  `payment_schedule` text DEFAULT NULL COMMENT '付款计划(JSON格式)',
  `delivery_terms` varchar(500) DEFAULT NULL COMMENT '交货条款',
  `quality_terms` varchar(500) DEFAULT NULL COMMENT '质量条款',
  `warranty_terms` varchar(500) DEFAULT NULL COMMENT '质保条款',
  `penalty_terms` varchar(500) DEFAULT NULL COMMENT '违约条款',
  `contract_file` varchar(500) DEFAULT NULL COMMENT '合同文件路径',
  `status` varchar(20) DEFAULT 'DRAFT' COMMENT '状态(DRAFT-草稿,SUBMITTED-已提交,APPROVED-已审批,SIGNED-已签订,EXECUTING-执行中,COMPLETED-已完成,TERMINATED-已终止)',
  `approve_user_id` bigint DEFAULT NULL COMMENT '审批人ID',
  `approve_user_name` varchar(50) DEFAULT NULL COMMENT '审批人姓名',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `sign_user_id` bigint DEFAULT NULL COMMENT '签订人ID',
  `sign_user_name` varchar(50) DEFAULT NULL COMMENT '签订人姓名',
  `sign_time` datetime DEFAULT NULL COMMENT '签订时间',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`contract_id`),
  UNIQUE KEY `uk_contract_no` (`contract_no`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_status` (`status`),
  KEY `idx_contract_date` (`contract_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购合同表';

-- 采购合同明细表
CREATE TABLE IF NOT EXISTS `purchase_contract_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `contract_id` bigint NOT NULL COMMENT '合同ID',
  `item_name` varchar(200) NOT NULL COMMENT '物料名称',
  `item_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `specification` varchar(200) DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `quantity` decimal(16,2) DEFAULT '0.00' COMMENT '数量',
  `unit_price` decimal(16,2) DEFAULT '0.00' COMMENT '单价',
  `total_amount` decimal(16,2) DEFAULT '0.00' COMMENT '总金额',
  `delivery_date` date DEFAULT NULL COMMENT '交货日期',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`detail_id`),
  KEY `idx_contract_id` (`contract_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购合同明细表';

