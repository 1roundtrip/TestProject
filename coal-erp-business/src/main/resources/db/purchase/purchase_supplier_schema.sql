-- ============================================
-- 供应商管理表结构
-- ============================================

USE coal_erp;

-- 供应商表（扩展财务模块的供应商表）
CREATE TABLE IF NOT EXISTS `purchase_supplier` (
  `supplier_id` bigint NOT NULL AUTO_INCREMENT COMMENT '供应商ID',
  `supplier_code` varchar(50) NOT NULL COMMENT '供应商编码',
  `supplier_name` varchar(200) NOT NULL COMMENT '供应商名称',
  `supplier_type` varchar(50) DEFAULT NULL COMMENT '供应商类型(MAIN-主要供应商,AUXILIARY-辅助供应商,STRATEGIC-战略供应商)',
  `credit_level` varchar(20) DEFAULT NULL COMMENT '信用等级(AAA-优秀,AA-良好,A-一般,B-较差)',
  `cooperation_years` int DEFAULT NULL COMMENT '合作年限',
  `business_license` varchar(100) DEFAULT NULL COMMENT '营业执照号',
  `tax_number` varchar(50) DEFAULT NULL COMMENT '税号',
  `legal_person` varchar(50) DEFAULT NULL COMMENT '法人代表',
  `registered_capital` decimal(16,2) DEFAULT NULL COMMENT '注册资本',
  `contact_person` varchar(50) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `contact_email` varchar(100) DEFAULT NULL COMMENT '联系邮箱',
  `address` varchar(500) DEFAULT NULL COMMENT '地址',
  `bank_name` varchar(100) DEFAULT NULL COMMENT '开户银行',
  `bank_account` varchar(50) DEFAULT NULL COMMENT '银行账号',
  `account_name` varchar(100) DEFAULT NULL COMMENT '账户名称',
  `payment_terms` varchar(100) DEFAULT NULL COMMENT '付款条件',
  `delivery_terms` varchar(100) DEFAULT NULL COMMENT '交货条件',
  `quality_rating` decimal(3,1) DEFAULT NULL COMMENT '质量评分(0-10)',
  `service_rating` decimal(3,1) DEFAULT NULL COMMENT '服务评分(0-10)',
  `price_rating` decimal(3,1) DEFAULT NULL COMMENT '价格评分(0-10)',
  `total_rating` decimal(3,1) DEFAULT NULL COMMENT '综合评分(0-10)',
  `status` varchar(20) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE-启用,INACTIVE-停用,BLACKLIST-黑名单)',
  `blacklist_reason` varchar(500) DEFAULT NULL COMMENT '黑名单原因',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`supplier_id`),
  UNIQUE KEY `uk_supplier_code` (`supplier_code`),
  KEY `idx_supplier_name` (`supplier_name`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商表';

-- 供应商产品目录表
CREATE TABLE IF NOT EXISTS `purchase_supplier_product` (
  `product_id` bigint NOT NULL AUTO_INCREMENT COMMENT '产品ID',
  `supplier_id` bigint NOT NULL COMMENT '供应商ID',
  `product_name` varchar(200) NOT NULL COMMENT '产品名称',
  `product_code` varchar(50) DEFAULT NULL COMMENT '产品编码',
  `specification` varchar(200) DEFAULT NULL COMMENT '规格型号',
  `brand` varchar(100) DEFAULT NULL COMMENT '品牌',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `unit_price` decimal(16,2) DEFAULT '0.00' COMMENT '单价',
  `currency` varchar(10) DEFAULT 'CNY' COMMENT '币种',
  `min_order_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '最小订货量',
  `delivery_days` int DEFAULT NULL COMMENT '交货天数',
  `warranty_period` int DEFAULT NULL COMMENT '质保期(月)',
  `status` varchar(20) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE-启用,INACTIVE-停用)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`product_id`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_product_code` (`product_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商产品目录表';

-- 供应商评价记录表
CREATE TABLE IF NOT EXISTS `purchase_supplier_evaluation` (
  `evaluation_id` bigint NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `supplier_id` bigint NOT NULL COMMENT '供应商ID',
  `order_id` bigint DEFAULT NULL COMMENT '关联订单ID',
  `order_no` varchar(50) DEFAULT NULL COMMENT '关联订单号',
  `evaluation_date` date NOT NULL COMMENT '评价日期',
  `quality_score` decimal(3,1) DEFAULT NULL COMMENT '质量评分(0-10)',
  `delivery_score` decimal(3,1) DEFAULT NULL COMMENT '交货评分(0-10)',
  `service_score` decimal(3,1) DEFAULT NULL COMMENT '服务评分(0-10)',
  `price_score` decimal(3,1) DEFAULT NULL COMMENT '价格评分(0-10)',
  `total_score` decimal(3,1) DEFAULT NULL COMMENT '综合评分(0-10)',
  `evaluation_content` varchar(1000) DEFAULT NULL COMMENT '评价内容',
  `evaluator_id` bigint DEFAULT NULL COMMENT '评价人ID',
  `evaluator_name` varchar(50) DEFAULT NULL COMMENT '评价人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`evaluation_id`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_evaluation_date` (`evaluation_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商评价记录表';

