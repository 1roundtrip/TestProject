-- 应收应付模块数据库表结构

-- 客户档案表
CREATE TABLE `finance_customer` (
  `customer_id` bigint NOT NULL AUTO_INCREMENT,
  `customer_code` varchar(50) NOT NULL COMMENT '客户编码',
  `customer_name` varchar(100) NOT NULL COMMENT '客户名称',
  `customer_type` varchar(20) NOT NULL COMMENT '客户类型(ELECTRIC_PLANT-电厂,STEEL_PLANT-钢厂,TRADER-贸易商)',
  `credit_level` varchar(20) DEFAULT NULL COMMENT '信用等级',
  `credit_amount` decimal(16,2) DEFAULT '0.00' COMMENT '信用额度',
  `payment_terms` varchar(100) DEFAULT NULL COMMENT '付款条件',
  `contact_person` varchar(50) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `tax_number` varchar(50) DEFAULT NULL COMMENT '税号',
  `bank_account` varchar(50) DEFAULT NULL COMMENT '银行账号',
  `bank_name` varchar(100) DEFAULT NULL COMMENT '开户行',
  `status` char(1) DEFAULT '0' COMMENT '状态(0-正常 1-停用)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `uk_customer_code` (`customer_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户档案表';

-- 供应商档案表
CREATE TABLE `finance_supplier` (
  `supplier_id` bigint NOT NULL AUTO_INCREMENT,
  `supplier_code` varchar(50) NOT NULL COMMENT '供应商编码',
  `supplier_name` varchar(100) NOT NULL COMMENT '供应商名称',
  `supplier_type` varchar(20) NOT NULL COMMENT '供应商类型(EQUIPMENT-设备,SERVICE-服务,MATERIAL-材料)',
  `evaluation_level` varchar(20) DEFAULT NULL COMMENT '评估等级',
  `payment_terms` varchar(100) DEFAULT NULL COMMENT '付款条件',
  `contact_person` varchar(50) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `tax_number` varchar(50) DEFAULT NULL COMMENT '税号',
  `bank_account` varchar(50) DEFAULT NULL COMMENT '银行账号',
  `bank_name` varchar(100) DEFAULT NULL COMMENT '开户行',
  `status` char(1) DEFAULT '0' COMMENT '状态(0-正常 1-停用)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`supplier_id`),
  UNIQUE KEY `uk_supplier_code` (`supplier_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商档案表';

-- 应收单据表
CREATE TABLE `finance_receivable` (
  `receivable_id` bigint NOT NULL AUTO_INCREMENT,
  `receivable_no` varchar(50) NOT NULL COMMENT '应收单号',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `customer_code` varchar(50) DEFAULT NULL COMMENT '客户编码',
  `customer_name` varchar(100) DEFAULT NULL COMMENT '客户名称',
  `source_type` varchar(20) NOT NULL COMMENT '来源类型(SALE-销售,OTHER-其他)',
  `source_no` varchar(50) DEFAULT NULL COMMENT '来源单号',
  `amount` decimal(16,2) NOT NULL COMMENT '应收金额',
  `received_amount` decimal(16,2) DEFAULT '0.00' COMMENT '已收金额',
  `balance_amount` decimal(16,2) DEFAULT '0.00' COMMENT '余额',
  `currency` varchar(10) DEFAULT 'CNY' COMMENT '币种',
  `exchange_rate` decimal(10,6) DEFAULT '1.000000' COMMENT '汇率',
  `issue_date` date NOT NULL COMMENT '单据日期',
  `due_date` date NOT NULL COMMENT '到期日',
  `status` varchar(20) DEFAULT 'UNPAID' COMMENT '状态(UNPAID-未付,PARTIAL-部分付,PAID-已付,CANCELLED-已取消)',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`receivable_id`),
  UNIQUE KEY `uk_receivable_no` (`receivable_no`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_due_date` (`due_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应收单据表';

-- 应付单据表
CREATE TABLE `finance_payable` (
  `payable_id` bigint NOT NULL AUTO_INCREMENT,
  `payable_no` varchar(50) NOT NULL COMMENT '应付单号',
  `supplier_id` bigint NOT NULL COMMENT '供应商ID',
  `supplier_code` varchar(50) DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(100) DEFAULT NULL COMMENT '供应商名称',
  `source_type` varchar(20) NOT NULL COMMENT '来源类型(PURCHASE-采购,OTHER-其他)',
  `source_no` varchar(50) DEFAULT NULL COMMENT '来源单号',
  `amount` decimal(16,2) NOT NULL COMMENT '应付金额',
  `paid_amount` decimal(16,2) DEFAULT '0.00' COMMENT '已付金额',
  `balance_amount` decimal(16,2) DEFAULT '0.00' COMMENT '余额',
  `currency` varchar(10) DEFAULT 'CNY' COMMENT '币种',
  `exchange_rate` decimal(10,6) DEFAULT '1.000000' COMMENT '汇率',
  `issue_date` date NOT NULL COMMENT '单据日期',
  `due_date` date NOT NULL COMMENT '到期日',
  `status` varchar(20) DEFAULT 'UNPAID' COMMENT '状态(UNPAID-未付,PARTIAL-部分付,PAID-已付,CANCELLED-已取消)',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`payable_id`),
  UNIQUE KEY `uk_payable_no` (`payable_no`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_due_date` (`due_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应付单据表';

-- 收付款单表
CREATE TABLE `finance_payment` (
  `payment_id` bigint NOT NULL AUTO_INCREMENT,
  `payment_no` varchar(50) NOT NULL COMMENT '收付款单号',
  `payment_type` varchar(20) NOT NULL COMMENT '类型(RECEIVE-收款,PAY-付款,ADVANCE-预收预付)',
  `customer_id` bigint DEFAULT NULL COMMENT '客户ID(收款时使用)',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商ID(付款时使用)',
  `amount` decimal(16,2) NOT NULL COMMENT '金额',
  `currency` varchar(10) DEFAULT 'CNY' COMMENT '币种',
  `exchange_rate` decimal(10,6) DEFAULT '1.000000' COMMENT '汇率',
  `payment_date` date NOT NULL COMMENT '收付款日期',
  `payment_method` varchar(20) DEFAULT NULL COMMENT '收付款方式',
  `bank_account` varchar(50) DEFAULT NULL COMMENT '银行账号',
  `status` varchar(20) DEFAULT 'UNCONFIRMED' COMMENT '状态(UNCONFIRMED-未确认,CONFIRMED-已确认,CANCELLED-已取消)',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`payment_id`),
  UNIQUE KEY `uk_payment_no` (`payment_no`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_supplier_id` (`supplier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收付款单表';

-- 收付款核销明细表
CREATE TABLE `finance_payment_settlement` (
  `settlement_id` bigint NOT NULL AUTO_INCREMENT,
  `payment_id` bigint NOT NULL COMMENT '收付款单ID',
  `source_type` varchar(20) NOT NULL COMMENT '来源类型(RECEIVABLE-应收,PAYABLE-应付)',
  `source_id` bigint NOT NULL COMMENT '来源单ID',
  `source_no` varchar(50) DEFAULT NULL COMMENT '来源单号',
  `settle_amount` decimal(16,2) NOT NULL COMMENT '核销金额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`settlement_id`),
  KEY `idx_payment_id` (`payment_id`),
  KEY `idx_source_id` (`source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收付款核销明细表';

-- 账龄分析表（视图）
CREATE VIEW `finance_aging_analysis` AS
SELECT 
    'RECEIVABLE' AS source_type,
    r.receivable_id AS source_id,
    r.receivable_no AS source_no,
    r.customer_id,
    r.customer_name,
    r.amount,
    r.balance_amount,
    r.due_date,
    DATEDIFF(CURDATE(), r.due_date) AS days_overdue,
    CASE 
        WHEN DATEDIFF(CURDATE(), r.due_date) <= 0 THEN '0-未到期'
        WHEN DATEDIFF(CURDATE(), r.due_date) <= 30 THEN '1-30天'
        WHEN DATEDIFF(CURDATE(), r.due_date) <= 60 THEN '31-60天'
        WHEN DATEDIFF(CURDATE(), r.due_date) <= 90 THEN '61-90天'
        ELSE '90天以上'
    END AS aging_bucket
FROM finance_receivable r
WHERE r.balance_amount > 0
UNION ALL
SELECT 
    'PAYABLE' AS source_type,
    p.payable_id AS source_id,
    p.payable_no AS source_no,
    p.supplier_id AS customer_id,
    p.supplier_name AS customer_name,
    p.amount,
    p.balance_amount,
    p.due_date,
    DATEDIFF(CURDATE(), p.due_date) AS days_overdue,
    CASE 
        WHEN DATEDIFF(CURDATE(), p.due_date) <= 0 THEN '0-未到期'
        WHEN DATEDIFF(CURDATE(), p.due_date) <= 30 THEN '1-30天'
        WHEN DATEDIFF(CURDATE(), p.due_date) <= 60 THEN '31-60天'
        WHEN DATEDIFF(CURDATE(), p.due_date) <= 90 THEN '61-90天'
        ELSE '90天以上'
    END AS aging_bucket
FROM finance_payable p
WHERE p.balance_amount > 0;

-- 初始化煤炭行业客户类型
INSERT INTO `finance_customer` (
    `customer_code`, `customer_name`, `customer_type`, 
    `credit_level`, `credit_amount`, `payment_terms`,
    `status`, `create_time`
) VALUES
('CUST001', '国电电力有限公司', 'ELECTRIC_PLANT', 'A', 1000000.00, '月结30天', '0', NOW()),
('CUST002', '宝山钢铁股份有限公司', 'STEEL_PLANT', 'A', 800000.00, '月结30天', '0', NOW()),
('CUST003', '中国煤炭贸易有限公司', 'TRADER', 'B', 500000.00, '预付30%', '0', NOW());

-- 初始化煤炭行业供应商类型
INSERT INTO `finance_supplier` (
    `supplier_code`, `supplier_name`, `supplier_type`,
    `evaluation_level`, `payment_terms`, `status`, `create_time`
) VALUES
('SUPP001', '三一重工股份有限公司', 'EQUIPMENT', 'A', '预付30%，货到付60%，验收付10%', '0', NOW()),
('SUPP002', '中煤建设集团有限公司', 'SERVICE', 'B', '月结30天', '0', NOW()),
('SUPP003', '山西焦煤集团', 'MATERIAL', 'A', '月结15天', '0', NOW());