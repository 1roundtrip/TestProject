-- ============================================
-- 智慧煤矿ERP系统 - 完整资产管理系统数据库表结构
-- ============================================

USE coal_erp;

-- ============================================
-- 1. 资产入库管理表
-- ============================================
CREATE TABLE IF NOT EXISTS `asset_storage` (
  `storage_id` bigint NOT NULL AUTO_INCREMENT COMMENT '入库ID',
  `storage_no` varchar(50) NOT NULL COMMENT '入库单号',
  `storage_type` varchar(20) NOT NULL COMMENT '入库类型(PURCHASE-采购入库,TRANSFER-调拨入库,REPAIR-维修入库,OTHER-其他)',
  `storage_date` date NOT NULL COMMENT '入库日期',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商ID',
  `supplier_name` varchar(100) DEFAULT NULL COMMENT '供应商名称',
  `total_amount` decimal(16,2) DEFAULT '0.00' COMMENT '总金额',
  `warehouse` varchar(100) DEFAULT NULL COMMENT '仓库',
  `location` varchar(200) DEFAULT NULL COMMENT '存放位置',
  `status` varchar(20) DEFAULT 'DRAFT' COMMENT '状态(DRAFT-草稿,CONFIRMED-已确认,CANCELLED-已取消)',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `audit_user_id` bigint DEFAULT NULL COMMENT '审核人ID',
  `audit_user_name` varchar(50) DEFAULT NULL COMMENT '审核人姓名',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`storage_id`),
  UNIQUE KEY `uk_storage_no` (`storage_no`),
  KEY `idx_storage_date` (`storage_date`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产入库管理表';

-- 资产入库明细表
CREATE TABLE IF NOT EXISTS `asset_storage_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `storage_id` bigint NOT NULL COMMENT '入库ID',
  `asset_id` bigint DEFAULT NULL COMMENT '资产ID（已存在资产）',
  `asset_code` varchar(50) DEFAULT NULL COMMENT '资产编码',
  `asset_name` varchar(100) NOT NULL COMMENT '资产名称',
  `asset_type` varchar(50) DEFAULT NULL COMMENT '资产类型',
  `category` varchar(50) DEFAULT NULL COMMENT '资产分类',
  `manufacturer` varchar(100) DEFAULT NULL COMMENT '制造商',
  `model` varchar(100) DEFAULT NULL COMMENT '型号',
  `serial_number` varchar(100) DEFAULT NULL COMMENT '序列号',
  `quantity` int DEFAULT '1' COMMENT '数量',
  `unit_price` decimal(16,2) DEFAULT '0.00' COMMENT '单价',
  `total_price` decimal(16,2) DEFAULT '0.00' COMMENT '总价',
  `purchase_date` date DEFAULT NULL COMMENT '采购日期',
  `warranty_period` int DEFAULT NULL COMMENT '保修期（月）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`detail_id`),
  KEY `idx_storage_id` (`storage_id`),
  KEY `idx_asset_id` (`asset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产入库明细表';

-- ============================================
-- 2. 资产领用退库管理表
-- ============================================
CREATE TABLE IF NOT EXISTS `asset_borrow` (
  `borrow_id` bigint NOT NULL AUTO_INCREMENT COMMENT '领用ID',
  `borrow_no` varchar(50) NOT NULL COMMENT '领用单号',
  `borrow_type` varchar(20) NOT NULL COMMENT '类型(BORROW-领用,RETURN-退库)',
  `borrow_date` date NOT NULL COMMENT '领用/退库日期',
  `asset_id` bigint NOT NULL COMMENT '资产ID',
  `asset_code` varchar(50) DEFAULT NULL COMMENT '资产编码',
  `asset_name` varchar(100) DEFAULT NULL COMMENT '资产名称',
  `borrower_id` bigint NOT NULL COMMENT '领用人ID',
  `borrower_name` varchar(50) NOT NULL COMMENT '领用人姓名',
  `borrower_dept_id` bigint DEFAULT NULL COMMENT '领用部门ID',
  `borrower_dept_name` varchar(50) DEFAULT NULL COMMENT '领用部门名称',
  `expected_return_date` date DEFAULT NULL COMMENT '预计归还日期',
  `actual_return_date` date DEFAULT NULL COMMENT '实际归还日期',
  `borrow_reason` varchar(500) DEFAULT NULL COMMENT '领用原因',
  `status` varchar(20) DEFAULT 'BORROWED' COMMENT '状态(BORROWED-已领用,RETURNED-已归还,OVERDUE-逾期)',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`borrow_id`),
  UNIQUE KEY `uk_borrow_no` (`borrow_no`),
  KEY `idx_asset_id` (`asset_id`),
  KEY `idx_borrower_id` (`borrower_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产领用退库管理表';

-- ============================================
-- 3. 资产转移调拨管理表
-- ============================================
CREATE TABLE IF NOT EXISTS `asset_transfer` (
  `transfer_id` bigint NOT NULL AUTO_INCREMENT COMMENT '转移ID',
  `transfer_no` varchar(50) NOT NULL COMMENT '转移单号',
  `transfer_date` date NOT NULL COMMENT '转移日期',
  `asset_id` bigint NOT NULL COMMENT '资产ID',
  `asset_code` varchar(50) DEFAULT NULL COMMENT '资产编码',
  `asset_name` varchar(100) DEFAULT NULL COMMENT '资产名称',
  `from_dept_id` bigint DEFAULT NULL COMMENT '原部门ID',
  `from_dept_name` varchar(50) DEFAULT NULL COMMENT '原部门名称',
  `from_location` varchar(200) DEFAULT NULL COMMENT '原位置',
  `to_dept_id` bigint NOT NULL COMMENT '目标部门ID',
  `to_dept_name` varchar(50) NOT NULL COMMENT '目标部门名称',
  `to_location` varchar(200) DEFAULT NULL COMMENT '目标位置',
  `transfer_reason` varchar(500) DEFAULT NULL COMMENT '转移原因',
  `status` varchar(20) DEFAULT 'PENDING' COMMENT '状态(PENDING-待转移,TRANSFERRED-已转移,CANCELLED-已取消)',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `approve_user_id` bigint DEFAULT NULL COMMENT '审批人ID',
  `approve_user_name` varchar(50) DEFAULT NULL COMMENT '审批人姓名',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `transfer_user_id` bigint DEFAULT NULL COMMENT '转移执行人ID',
  `transfer_user_name` varchar(50) DEFAULT NULL COMMENT '转移执行人姓名',
  `transfer_time` datetime DEFAULT NULL COMMENT '转移执行时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`transfer_id`),
  UNIQUE KEY `uk_transfer_no` (`transfer_no`),
  KEY `idx_asset_id` (`asset_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产转移调拨管理表';

-- ============================================
-- 4. 资产折旧管理表
-- ============================================
CREATE TABLE IF NOT EXISTS `asset_depreciation` (
  `depreciation_id` bigint NOT NULL AUTO_INCREMENT COMMENT '折旧ID',
  `asset_id` bigint NOT NULL COMMENT '资产ID',
  `asset_code` varchar(50) DEFAULT NULL COMMENT '资产编码',
  `asset_name` varchar(100) DEFAULT NULL COMMENT '资产名称',
  `depreciation_method` varchar(20) NOT NULL COMMENT '折旧方法(STRAIGHT_LINE-直线法,ACCELERATED-加速折旧法)',
  `original_value` decimal(16,2) NOT NULL COMMENT '原值',
  `residual_value` decimal(16,2) DEFAULT '0.00' COMMENT '残值',
  `useful_life` int NOT NULL COMMENT '使用年限（月）',
  `depreciation_rate` decimal(10,4) DEFAULT NULL COMMENT '折旧率（%）',
  `monthly_depreciation` decimal(16,2) DEFAULT '0.00' COMMENT '月折旧额',
  `accumulated_depreciation` decimal(16,2) DEFAULT '0.00' COMMENT '累计折旧',
  `net_value` decimal(16,2) DEFAULT '0.00' COMMENT '净值',
  `start_date` date NOT NULL COMMENT '开始折旧日期',
  `last_depreciation_date` date DEFAULT NULL COMMENT '最后折旧日期',
  `status` varchar(20) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE-折旧中,STOPPED-已停用,COMPLETED-已提完)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`depreciation_id`),
  UNIQUE KEY `uk_asset_id` (`asset_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产折旧管理表';

-- 资产折旧明细表（按月记录）
CREATE TABLE IF NOT EXISTS `asset_depreciation_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `depreciation_id` bigint NOT NULL COMMENT '折旧ID',
  `asset_id` bigint NOT NULL COMMENT '资产ID',
  `depreciation_month` varchar(7) NOT NULL COMMENT '折旧月份(YYYY-MM)',
  `depreciation_amount` decimal(16,2) NOT NULL COMMENT '折旧金额',
  `accumulated_amount` decimal(16,2) NOT NULL COMMENT '累计折旧金额',
  `net_value` decimal(16,2) NOT NULL COMMENT '净值',
  `status` varchar(20) DEFAULT 'PENDING' COMMENT '状态(PENDING-待计提,CONFIRMED-已确认,CANCELLED-已取消)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `confirm_time` datetime DEFAULT NULL COMMENT '确认时间',
  `confirm_user_id` bigint DEFAULT NULL COMMENT '确认人ID',
  PRIMARY KEY (`detail_id`),
  UNIQUE KEY `uk_depreciation_month` (`depreciation_id`, `depreciation_month`),
  KEY `idx_asset_id` (`asset_id`),
  KEY `idx_depreciation_month` (`depreciation_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产折旧明细表';

-- ============================================
-- 5. 资产盘点管理表
-- ============================================
CREATE TABLE IF NOT EXISTS `asset_inventory` (
  `inventory_id` bigint NOT NULL AUTO_INCREMENT COMMENT '盘点ID',
  `inventory_no` varchar(50) NOT NULL COMMENT '盘点单号',
  `inventory_type` varchar(20) NOT NULL COMMENT '盘点类型(FULL-全面盘点,PARTIAL-部分盘点,SPOT-抽查盘点)',
  `inventory_date` date NOT NULL COMMENT '盘点日期',
  `warehouse` varchar(100) DEFAULT NULL COMMENT '仓库',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `dept_name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `status` varchar(20) DEFAULT 'DRAFT' COMMENT '状态(DRAFT-草稿,IN_PROGRESS-盘点中,COMPLETED-已完成,CONFIRMED-已确认)',
  `total_count` int DEFAULT '0' COMMENT '应盘数量',
  `actual_count` int DEFAULT '0' COMMENT '实盘数量',
  `surplus_count` int DEFAULT '0' COMMENT '盘盈数量',
  `shortage_count` int DEFAULT '0' COMMENT '盘亏数量',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `inventory_user_id` bigint DEFAULT NULL COMMENT '盘点人ID',
  `inventory_user_name` varchar(50) DEFAULT NULL COMMENT '盘点人姓名',
  `confirm_user_id` bigint DEFAULT NULL COMMENT '确认人ID',
  `confirm_user_name` varchar(50) DEFAULT NULL COMMENT '确认人姓名',
  `confirm_time` datetime DEFAULT NULL COMMENT '确认时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`inventory_id`),
  UNIQUE KEY `uk_inventory_no` (`inventory_no`),
  KEY `idx_inventory_date` (`inventory_date`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产盘点管理表';

-- 资产盘点明细表
CREATE TABLE IF NOT EXISTS `asset_inventory_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `inventory_id` bigint NOT NULL COMMENT '盘点ID',
  `asset_id` bigint NOT NULL COMMENT '资产ID',
  `asset_code` varchar(50) DEFAULT NULL COMMENT '资产编码',
  `asset_name` varchar(100) DEFAULT NULL COMMENT '资产名称',
  `book_quantity` int DEFAULT '1' COMMENT '账面数量',
  `actual_quantity` int DEFAULT '0' COMMENT '实盘数量',
  `difference_quantity` int DEFAULT '0' COMMENT '差异数量',
  `difference_type` varchar(20) DEFAULT NULL COMMENT '差异类型(SURPLUS-盘盈,SHORTAGE-盘亏,NORMAL-正常)',
  `difference_reason` varchar(500) DEFAULT NULL COMMENT '差异原因',
  `handle_status` varchar(20) DEFAULT 'PENDING' COMMENT '处理状态(PENDING-待处理,PROCESSED-已处理)',
  `handle_remark` varchar(500) DEFAULT NULL COMMENT '处理备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`detail_id`),
  KEY `idx_inventory_id` (`inventory_id`),
  KEY `idx_asset_id` (`asset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产盘点明细表';

-- ============================================
-- 6. 资产报废管理表
-- ============================================
CREATE TABLE IF NOT EXISTS `asset_scrap` (
  `scrap_id` bigint NOT NULL AUTO_INCREMENT COMMENT '报废ID',
  `scrap_no` varchar(50) NOT NULL COMMENT '报废单号',
  `scrap_date` date NOT NULL COMMENT '报废日期',
  `asset_id` bigint NOT NULL COMMENT '资产ID',
  `asset_code` varchar(50) DEFAULT NULL COMMENT '资产编码',
  `asset_name` varchar(100) DEFAULT NULL COMMENT '资产名称',
  `scrap_reason` varchar(500) NOT NULL COMMENT '报废原因',
  `scrap_type` varchar(20) DEFAULT NULL COMMENT '报废类型(NATURAL-自然报废,DAMAGE-损坏报废,REPLACE-更新换代,OTHER-其他)',
  `original_value` decimal(16,2) DEFAULT NULL COMMENT '原值',
  `net_value` decimal(16,2) DEFAULT NULL COMMENT '净值',
  `scrap_value` decimal(16,2) DEFAULT '0.00' COMMENT '残值',
  `status` varchar(20) DEFAULT 'PENDING' COMMENT '状态(PENDING-待审批,APPROVED-已审批,REJECTED-已驳回,COMPLETED-已完成)',
  `apply_user_id` bigint DEFAULT NULL COMMENT '申请人ID',
  `apply_user_name` varchar(50) DEFAULT NULL COMMENT '申请人姓名',
  `apply_time` datetime DEFAULT NULL COMMENT '申请时间',
  `approve_user_id` bigint DEFAULT NULL COMMENT '审批人ID',
  `approve_user_name` varchar(50) DEFAULT NULL COMMENT '审批人姓名',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `approve_remark` varchar(500) DEFAULT NULL COMMENT '审批意见',
  `handle_user_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `handle_user_name` varchar(50) DEFAULT NULL COMMENT '处理人姓名',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`scrap_id`),
  UNIQUE KEY `uk_scrap_no` (`scrap_no`),
  KEY `idx_asset_id` (`asset_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产报废管理表';

-- ============================================
-- 7. 扩展资产表字段（如果需要）
-- ============================================
-- 为现有asset表添加新字段（如果不存在）
-- 使用存储过程来检查并添加字段，避免重复添加错误

DELIMITER $$

DROP PROCEDURE IF EXISTS add_column_if_not_exists$$

CREATE PROCEDURE add_column_if_not_exists(
    IN table_name VARCHAR(64),
    IN column_name VARCHAR(64),
    IN column_definition TEXT
)
BEGIN
    DECLARE column_exists INT DEFAULT 0;
    
    SELECT COUNT(*) INTO column_exists
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = table_name
      AND COLUMN_NAME = column_name;
    
    IF column_exists = 0 THEN
        SET @sql = CONCAT('ALTER TABLE `', table_name, '` ADD COLUMN `', column_name, '` ', column_definition);
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END$$

DELIMITER ;

-- 添加字段
CALL add_column_if_not_exists('asset', 'current_value', 'decimal(16,2) DEFAULT NULL COMMENT ''当前价值''');
CALL add_column_if_not_exists('asset', 'depreciation_status', 'varchar(20) DEFAULT NULL COMMENT ''折旧状态(NOT_STARTED-未开始,IN_PROGRESS-折旧中,COMPLETED-已提完)''');
CALL add_column_if_not_exists('asset', 'warehouse', 'varchar(100) DEFAULT NULL COMMENT ''仓库''');
CALL add_column_if_not_exists('asset', 'custodian_id', 'bigint DEFAULT NULL COMMENT ''保管人ID''');
CALL add_column_if_not_exists('asset', 'custodian_name', 'varchar(50) DEFAULT NULL COMMENT ''保管人姓名''');
CALL add_column_if_not_exists('asset', 'warranty_start_date', 'date DEFAULT NULL COMMENT ''保修开始日期''');
CALL add_column_if_not_exists('asset', 'warranty_end_date', 'date DEFAULT NULL COMMENT ''保修结束日期''');

-- 删除临时存储过程
DROP PROCEDURE IF EXISTS add_column_if_not_exists;

