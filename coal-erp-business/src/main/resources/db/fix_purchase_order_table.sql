-- ============================================
-- 修复采购订单表结构
-- ============================================
-- 此脚本使用存储过程安全地添加缺失的字段

USE coal_erp;

-- 创建存储过程来安全地添加字段
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
CALL add_purchase_order_column_if_not_exists('requisition_id', 'bigint DEFAULT NULL COMMENT ''关联申请ID'' AFTER `order_no`');
CALL add_purchase_order_column_if_not_exists('requisition_no', 'varchar(50) DEFAULT NULL COMMENT ''关联申请单号'' AFTER `requisition_id`');
CALL add_purchase_order_column_if_not_exists('supplier_id', 'bigint DEFAULT NULL COMMENT ''供应商ID'' AFTER `requisition_no`');
CALL add_purchase_order_column_if_not_exists('supplier_name', 'varchar(200) DEFAULT NULL COMMENT ''供应商名称'' AFTER `supplier_id`');
CALL add_purchase_order_column_if_not_exists('supplier_code', 'varchar(50) DEFAULT NULL COMMENT ''供应商编码'' AFTER `supplier_name`');
CALL add_purchase_order_column_if_not_exists('order_date', 'date DEFAULT NULL COMMENT ''订单日期'' AFTER `order_type`');
CALL add_purchase_order_column_if_not_exists('delivery_date', 'date DEFAULT NULL COMMENT ''交货日期'' AFTER `order_date`');
CALL add_purchase_order_column_if_not_exists('delivery_address', 'varchar(500) DEFAULT NULL COMMENT ''交货地址'' AFTER `delivery_date`');
CALL add_purchase_order_column_if_not_exists('delivery_method', 'varchar(50) DEFAULT NULL COMMENT ''交货方式'' AFTER `delivery_address`');
CALL add_purchase_order_column_if_not_exists('payment_terms', 'varchar(100) DEFAULT NULL COMMENT ''付款条件'' AFTER `delivery_method`');
CALL add_purchase_order_column_if_not_exists('currency', 'varchar(10) DEFAULT ''CNY'' COMMENT ''币种'' AFTER `payment_terms`');
CALL add_purchase_order_column_if_not_exists('tax_amount', 'decimal(16,2) DEFAULT ''0.00'' COMMENT ''税额'' AFTER `total_amount`');
CALL add_purchase_order_column_if_not_exists('total_amount_with_tax', 'decimal(16,2) DEFAULT ''0.00'' COMMENT ''含税总额'' AFTER `tax_amount`');
CALL add_purchase_order_column_if_not_exists('approve_user_id', 'bigint DEFAULT NULL COMMENT ''审批人ID'' AFTER `status`');
CALL add_purchase_order_column_if_not_exists('approve_user_name', 'varchar(50) DEFAULT NULL COMMENT ''审批人姓名'' AFTER `approve_user_id`');
CALL add_purchase_order_column_if_not_exists('approve_time', 'datetime DEFAULT NULL COMMENT ''审批时间'' AFTER `approve_user_name`');
CALL add_purchase_order_column_if_not_exists('buyer_id', 'bigint DEFAULT NULL COMMENT ''采购员ID'' AFTER `approve_time`');
CALL add_purchase_order_column_if_not_exists('buyer_name', 'varchar(50) DEFAULT NULL COMMENT ''采购员姓名'' AFTER `buyer_id`');
CALL add_purchase_order_column_if_not_exists('create_user_name', 'varchar(50) DEFAULT NULL COMMENT ''创建人姓名'' AFTER `create_user_id`');

-- 删除临时存储过程
DROP PROCEDURE IF EXISTS add_purchase_order_column_if_not_exists;

SELECT '采购订单表结构修复完成！' AS message;
