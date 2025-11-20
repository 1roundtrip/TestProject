-- ============================================
-- 采购订单管理表结构（扩展现有表）
-- ============================================

USE coal_erp;

-- 扩展采购订单表（如果表已存在，使用ALTER TABLE添加字段）
-- 检查表是否存在，如果不存在则创建
CREATE TABLE IF NOT EXISTS `purchase_order` (
  `order_id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单编号',
  `requisition_id` bigint DEFAULT NULL COMMENT '关联申请ID',
  `requisition_no` varchar(50) DEFAULT NULL COMMENT '关联申请单号',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商ID',
  `supplier_name` varchar(200) DEFAULT NULL COMMENT '供应商名称',
  `supplier_code` varchar(50) DEFAULT NULL COMMENT '供应商编码',
  `order_type` varchar(50) DEFAULT NULL COMMENT '订单类型(NORMAL-普通,URGENT-紧急,CONTRACT-合同订单)',
  `order_date` date NOT NULL COMMENT '订单日期',
  `delivery_date` date DEFAULT NULL COMMENT '交货日期',
  `delivery_address` varchar(500) DEFAULT NULL COMMENT '交货地址',
  `delivery_method` varchar(50) DEFAULT NULL COMMENT '交货方式(EXPRESS-快递,LOGISTICS-物流,SELF-自提)',
  `payment_terms` varchar(100) DEFAULT NULL COMMENT '付款条件',
  `currency` varchar(10) DEFAULT 'CNY' COMMENT '币种',
  `total_amount` decimal(16,2) DEFAULT '0.00' COMMENT '订单总额',
  `tax_amount` decimal(16,2) DEFAULT '0.00' COMMENT '税额',
  `total_amount_with_tax` decimal(16,2) DEFAULT '0.00' COMMENT '含税总额',
  `status` varchar(20) DEFAULT 'DRAFT' COMMENT '状态(DRAFT-草稿,SUBMITTED-已提交,APPROVED-已审批,CONFIRMED-已确认,EXECUTING-执行中,PARTIAL_RECEIVED-部分收货,RECEIVED-已收货,COMPLETED-已完成,CANCELLED-已取消)',
  `approve_user_id` bigint DEFAULT NULL COMMENT '审批人ID',
  `approve_user_name` varchar(50) DEFAULT NULL COMMENT '审批人姓名',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `buyer_id` bigint DEFAULT NULL COMMENT '采购员ID',
  `buyer_name` varchar(50) DEFAULT NULL COMMENT '采购员姓名',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_requisition_id` (`requisition_id`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_status` (`status`),
  KEY `idx_order_date` (`order_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购订单表';

-- 如果表已存在，添加新字段
-- 使用存储过程来安全地添加字段
DELIMITER $$

DROP PROCEDURE IF EXISTS add_purchase_order_column_if_not_exists$$

CREATE PROCEDURE add_purchase_order_column_if_not_exists(
    IN column_name VARCHAR(64),
    IN column_definition TEXT
)
BEGIN
    DECLARE column_exists INT DEFAULT 0;

    SELECT COUNT(*) INTO column_exists
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'purchase_order'
      AND COLUMN_NAME = column_name;

    IF column_exists = 0 THEN
        SET @sql = CONCAT('ALTER TABLE `purchase_order` ADD COLUMN `', column_name, '` ', column_definition);
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END$$

DELIMITER ;

-- 添加新字段（如果不存在）
CALL add_purchase_order_column_if_not_exists('requisition_id', 'bigint DEFAULT NULL COMMENT ''关联申请ID''');
CALL add_purchase_order_column_if_not_exists('requisition_no', 'varchar(50) DEFAULT NULL COMMENT ''关联申请单号''');
CALL add_purchase_order_column_if_not_exists('supplier_id', 'bigint DEFAULT NULL COMMENT ''供应商ID''');
CALL add_purchase_order_column_if_not_exists('supplier_code', 'varchar(50) DEFAULT NULL COMMENT ''供应商编码''');
CALL add_purchase_order_column_if_not_exists('order_date', 'date NOT NULL COMMENT ''订单日期''');
CALL add_purchase_order_column_if_not_exists('delivery_date', 'date DEFAULT NULL COMMENT ''交货日期''');
CALL add_purchase_order_column_if_not_exists('delivery_address', 'varchar(500) DEFAULT NULL COMMENT ''交货地址''');
CALL add_purchase_order_column_if_not_exists('delivery_method', 'varchar(50) DEFAULT NULL COMMENT ''交货方式''');
CALL add_purchase_order_column_if_not_exists('payment_terms', 'varchar(100) DEFAULT NULL COMMENT ''付款条件''');
CALL add_purchase_order_column_if_not_exists('currency', 'varchar(10) DEFAULT ''CNY'' COMMENT ''币种''');
CALL add_purchase_order_column_if_not_exists('tax_amount', 'decimal(16,2) DEFAULT ''0.00'' COMMENT ''税额''');
CALL add_purchase_order_column_if_not_exists('total_amount_with_tax', 'decimal(16,2) DEFAULT ''0.00'' COMMENT ''含税总额''');
CALL add_purchase_order_column_if_not_exists('approve_user_id', 'bigint DEFAULT NULL COMMENT ''审批人ID''');
CALL add_purchase_order_column_if_not_exists('approve_user_name', 'varchar(50) DEFAULT NULL COMMENT ''审批人姓名''');
CALL add_purchase_order_column_if_not_exists('approve_time', 'datetime DEFAULT NULL COMMENT ''审批时间''');
CALL add_purchase_order_column_if_not_exists('buyer_id', 'bigint DEFAULT NULL COMMENT ''采购员ID''');
CALL add_purchase_order_column_if_not_exists('buyer_name', 'varchar(50) DEFAULT NULL COMMENT ''采购员姓名''');
CALL add_purchase_order_column_if_not_exists('create_user_name', 'varchar(50) DEFAULT NULL COMMENT ''创建人姓名''');

-- 删除临时存储过程
DROP PROCEDURE IF EXISTS add_purchase_order_column_if_not_exists;

-- 采购订单明细表
CREATE TABLE IF NOT EXISTS `purchase_order_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `item_name` varchar(200) NOT NULL COMMENT '物料名称',
  `item_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `specification` varchar(200) DEFAULT NULL COMMENT '规格型号',
  `brand` varchar(100) DEFAULT NULL COMMENT '品牌',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `quantity` decimal(16,2) DEFAULT '0.00' COMMENT '数量',
  `unit_price` decimal(16,2) DEFAULT '0.00' COMMENT '单价',
  `tax_rate` decimal(5,2) DEFAULT '0.00' COMMENT '税率(%)',
  `amount` decimal(16,2) DEFAULT '0.00' COMMENT '金额',
  `tax_amount` decimal(16,2) DEFAULT '0.00' COMMENT '税额',
  `amount_with_tax` decimal(16,2) DEFAULT '0.00' COMMENT '含税金额',
  `received_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '已收货数量',
  `required_date` date DEFAULT NULL COMMENT '需求日期',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`detail_id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购订单明细表';

