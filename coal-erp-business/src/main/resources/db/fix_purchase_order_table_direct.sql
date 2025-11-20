-- ============================================
-- 直接修复采购订单表结构（不使用存储过程）
-- ============================================
-- 如果字段已存在会报错，但可以忽略

USE coal_erp;

-- 检查并添加字段（如果字段不存在，执行会失败，但可以继续）
-- 注意：如果字段已存在，会报错，但可以忽略

-- 添加 requisition_id 字段
ALTER TABLE `purchase_order` 
ADD COLUMN `requisition_id` bigint DEFAULT NULL COMMENT '关联申请ID' AFTER `order_no`;

-- 添加 requisition_no 字段
ALTER TABLE `purchase_order` 
ADD COLUMN `requisition_no` varchar(50) DEFAULT NULL COMMENT '关联申请单号' AFTER `requisition_id`;

-- 添加 supplier_id 字段
ALTER TABLE `purchase_order` 
ADD COLUMN `supplier_id` bigint DEFAULT NULL COMMENT '供应商ID' AFTER `requisition_no`;

-- 添加 supplier_name 字段
ALTER TABLE `purchase_order` 
ADD COLUMN `supplier_name` varchar(200) DEFAULT NULL COMMENT '供应商名称' AFTER `supplier_id`;

-- 添加 supplier_code 字段
ALTER TABLE `purchase_order` 
ADD COLUMN `supplier_code` varchar(50) DEFAULT NULL COMMENT '供应商编码' AFTER `supplier_name`;

-- 添加 order_date 字段
ALTER TABLE `purchase_order` 
ADD COLUMN `order_date` date DEFAULT NULL COMMENT '订单日期' AFTER `order_type`;

-- 添加 delivery_date 字段
ALTER TABLE `purchase_order` 
ADD COLUMN `delivery_date` date DEFAULT NULL COMMENT '交货日期' AFTER `order_date`;

-- 添加 delivery_address 字段
ALTER TABLE `purchase_order` 
ADD COLUMN `delivery_address` varchar(500) DEFAULT NULL COMMENT '交货地址' AFTER `delivery_date`;

-- 添加 delivery_method 字段
ALTER TABLE `purchase_order` 
ADD COLUMN `delivery_method` varchar(50) DEFAULT NULL COMMENT '交货方式' AFTER `delivery_address`;

-- 添加 payment_terms 字段
ALTER TABLE `purchase_order` 
ADD COLUMN `payment_terms` varchar(100) DEFAULT NULL COMMENT '付款条件' AFTER `delivery_method`;

-- 添加 currency 字段
ALTER TABLE `purchase_order` 
ADD COLUMN `currency` varchar(10) DEFAULT 'CNY' COMMENT '币种' AFTER `payment_terms`;

-- 添加 tax_amount 字段
ALTER TABLE `purchase_order` 
ADD COLUMN `tax_amount` decimal(16,2) DEFAULT '0.00' COMMENT '税额' AFTER `total_amount`;

-- 添加 total_amount_with_tax 字段
ALTER TABLE `purchase_order` 
ADD COLUMN `total_amount_with_tax` decimal(16,2) DEFAULT '0.00' COMMENT '含税总额' AFTER `tax_amount`;

-- 添加 approve_user_id 字段
ALTER TABLE `purchase_order` 
ADD COLUMN `approve_user_id` bigint DEFAULT NULL COMMENT '审批人ID' AFTER `status`;

-- 添加 approve_user_name 字段
ALTER TABLE `purchase_order` 
ADD COLUMN `approve_user_name` varchar(50) DEFAULT NULL COMMENT '审批人姓名' AFTER `approve_user_id`;

-- 添加 approve_time 字段
ALTER TABLE `purchase_order` 
ADD COLUMN `approve_time` datetime DEFAULT NULL COMMENT '审批时间' AFTER `approve_user_name`;

-- 添加 buyer_id 字段
ALTER TABLE `purchase_order` 
ADD COLUMN `buyer_id` bigint DEFAULT NULL COMMENT '采购员ID' AFTER `approve_time`;

-- 添加 buyer_name 字段
ALTER TABLE `purchase_order` 
ADD COLUMN `buyer_name` varchar(50) DEFAULT NULL COMMENT '采购员姓名' AFTER `buyer_id`;

-- 添加 create_user_name 字段
ALTER TABLE `purchase_order` 
ADD COLUMN `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名' AFTER `create_user_id`;

SELECT '采购订单表结构修复完成！请检查是否有字段已存在的错误（可以忽略）' AS message;

