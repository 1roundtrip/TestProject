-- ============================================
-- 库存管理系统数据库表结构
-- ============================================

USE coal_erp;

-- ============================================
-- 1. 仓库管理
-- ============================================

-- 仓库表
CREATE TABLE IF NOT EXISTS `inventory_warehouse` (
  `warehouse_id` bigint NOT NULL AUTO_INCREMENT COMMENT '仓库ID',
  `warehouse_code` varchar(50) NOT NULL COMMENT '仓库编码',
  `warehouse_name` varchar(100) NOT NULL COMMENT '仓库名称',
  `warehouse_type` varchar(20) DEFAULT 'GENERAL' COMMENT '仓库类型(GENERAL-通用,RAW_MATERIAL-原材料,FINISHED-成品,SPARE_PART-备件,DANGEROUS-危险品)',
  `location` varchar(200) DEFAULT NULL COMMENT '仓库位置',
  `manager_id` bigint DEFAULT NULL COMMENT '仓库管理员ID',
  `manager_name` varchar(50) DEFAULT NULL COMMENT '仓库管理员姓名',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `area` decimal(10,2) DEFAULT NULL COMMENT '仓库面积(平方米)',
  `capacity` decimal(16,2) DEFAULT NULL COMMENT '仓库容量',
  `capacity_unit` varchar(20) DEFAULT NULL COMMENT '容量单位',
  `status` varchar(20) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE-激活,INACTIVE-停用)',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`warehouse_id`),
  UNIQUE KEY `uk_warehouse_code` (`warehouse_code`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库表';

-- ============================================
-- 2. 库位管理
-- ============================================

-- 库位表
CREATE TABLE IF NOT EXISTS `inventory_location` (
  `location_id` bigint NOT NULL AUTO_INCREMENT COMMENT '库位ID',
  `warehouse_id` bigint NOT NULL COMMENT '仓库ID',
  `warehouse_code` varchar(50) DEFAULT NULL COMMENT '仓库编码',
  `warehouse_name` varchar(100) DEFAULT NULL COMMENT '仓库名称',
  `location_code` varchar(50) NOT NULL COMMENT '库位编码',
  `location_name` varchar(100) DEFAULT NULL COMMENT '库位名称',
  `location_type` varchar(20) DEFAULT 'NORMAL' COMMENT '库位类型(NORMAL-普通,COLD-冷藏,FROZEN-冷冻,DANGEROUS-危险品)',
  `zone` varchar(50) DEFAULT NULL COMMENT '区域',
  `aisle` varchar(50) DEFAULT NULL COMMENT '通道',
  `shelf` varchar(50) DEFAULT NULL COMMENT '货架',
  `level` varchar(50) DEFAULT NULL COMMENT '层',
  `position` varchar(50) DEFAULT NULL COMMENT '位置',
  `capacity` decimal(16,2) DEFAULT NULL COMMENT '容量',
  `capacity_unit` varchar(20) DEFAULT NULL COMMENT '容量单位',
  `status` varchar(20) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE-激活,INACTIVE-停用,OCCUPIED-占用)',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`location_id`),
  UNIQUE KEY `uk_warehouse_location` (`warehouse_id`, `location_code`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库位表';

-- ============================================
-- 3. 库存物品管理
-- ============================================

-- 库存物品表（扩展原有inventory表）
CREATE TABLE IF NOT EXISTS `inventory_material` (
  `material_id` bigint NOT NULL AUTO_INCREMENT COMMENT '物料ID',
  `material_code` varchar(50) NOT NULL COMMENT '物料编码',
  `material_name` varchar(200) NOT NULL COMMENT '物料名称',
  `material_type` varchar(50) DEFAULT NULL COMMENT '物料类型',
  `category` varchar(50) DEFAULT NULL COMMENT '物料分类',
  `specification` varchar(200) DEFAULT NULL COMMENT '规格型号',
  `brand` varchar(100) DEFAULT NULL COMMENT '品牌',
  `manufacturer` varchar(200) DEFAULT NULL COMMENT '制造商',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `unit_price` decimal(16,2) DEFAULT '0.00' COMMENT '单价',
  `currency` varchar(10) DEFAULT 'CNY' COMMENT '币种',
  `min_stock` decimal(16,2) DEFAULT '0.00' COMMENT '最低库存',
  `max_stock` decimal(16,2) DEFAULT NULL COMMENT '最高库存',
  `safety_stock` decimal(16,2) DEFAULT '0.00' COMMENT '安全库存',
  `reorder_point` decimal(16,2) DEFAULT NULL COMMENT '再订货点',
  `reorder_quantity` decimal(16,2) DEFAULT NULL COMMENT '再订货量',
  `shelf_life` int DEFAULT NULL COMMENT '保质期(天)',
  `storage_condition` varchar(200) DEFAULT NULL COMMENT '存储条件',
  `status` varchar(20) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE-激活,INACTIVE-停用)',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`material_id`),
  UNIQUE KEY `uk_material_code` (`material_code`),
  KEY `idx_material_type` (`material_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存物品表';

-- 库存明细表（按仓库和库位）
CREATE TABLE IF NOT EXISTS `inventory_stock` (
  `stock_id` bigint NOT NULL AUTO_INCREMENT COMMENT '库存ID',
  `warehouse_id` bigint NOT NULL COMMENT '仓库ID',
  `warehouse_code` varchar(50) DEFAULT NULL COMMENT '仓库编码',
  `warehouse_name` varchar(100) DEFAULT NULL COMMENT '仓库名称',
  `location_id` bigint DEFAULT NULL COMMENT '库位ID',
  `location_code` varchar(50) DEFAULT NULL COMMENT '库位编码',
  `material_id` bigint NOT NULL COMMENT '物料ID',
  `material_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `material_name` varchar(200) DEFAULT NULL COMMENT '物料名称',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次号',
  `production_date` date DEFAULT NULL COMMENT '生产日期',
  `expiry_date` date DEFAULT NULL COMMENT '到期日期',
  `quantity` decimal(16,2) DEFAULT '0.00' COMMENT '库存数量',
  `available_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '可用数量',
  `frozen_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '冻结数量',
  `unit_price` decimal(16,2) DEFAULT '0.00' COMMENT '单价',
  `total_value` decimal(16,2) DEFAULT '0.00' COMMENT '总价值',
  `last_in_date` date DEFAULT NULL COMMENT '最后入库日期',
  `last_out_date` date DEFAULT NULL COMMENT '最后出库日期',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`stock_id`),
  UNIQUE KEY `uk_warehouse_location_material_batch` (`warehouse_id`, `location_id`, `material_id`, `batch_no`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  KEY `idx_location_id` (`location_id`),
  KEY `idx_material_id` (`material_id`),
  KEY `idx_material_code` (`material_code`),
  KEY `idx_batch_no` (`batch_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存明细表';

-- ============================================
-- 4. 入库管理
-- ============================================

-- 入库单表
CREATE TABLE IF NOT EXISTS `inventory_inbound` (
  `inbound_id` bigint NOT NULL AUTO_INCREMENT COMMENT '入库ID',
  `inbound_no` varchar(50) NOT NULL COMMENT '入库单号',
  `inbound_type` varchar(20) DEFAULT 'PURCHASE' COMMENT '入库类型(PURCHASE-采购入库,PRODUCTION-生产入库,RETURN-退货入库,TRANSFER-调拨入库,ADJUST-调整入库,OTHER-其他)',
  `warehouse_id` bigint NOT NULL COMMENT '仓库ID',
  `warehouse_code` varchar(50) DEFAULT NULL COMMENT '仓库编码',
  `warehouse_name` varchar(100) DEFAULT NULL COMMENT '仓库名称',
  `source_type` varchar(20) DEFAULT NULL COMMENT '来源类型(PURCHASE_ORDER-采购订单,PRODUCTION_ORDER-生产订单,TRANSFER_ORDER-调拨单)',
  `source_no` varchar(50) DEFAULT NULL COMMENT '来源单号',
  `source_id` bigint DEFAULT NULL COMMENT '来源ID',
  `inbound_date` date NOT NULL COMMENT '入库日期',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商ID',
  `supplier_name` varchar(200) DEFAULT NULL COMMENT '供应商名称',
  `total_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '总数量',
  `total_amount` decimal(16,2) DEFAULT '0.00' COMMENT '总金额',
  `handler_id` bigint DEFAULT NULL COMMENT '经办人ID',
  `handler_name` varchar(50) DEFAULT NULL COMMENT '经办人姓名',
  `receiver_id` bigint DEFAULT NULL COMMENT '收货人ID',
  `receiver_name` varchar(50) DEFAULT NULL COMMENT '收货人姓名',
  `status` varchar(20) DEFAULT 'DRAFT' COMMENT '状态(DRAFT-草稿,SUBMITTED-已提交,APPROVED-已审批,RECEIVED-已收货,COMPLETED-已完成,CANCELLED-已取消)',
  `approve_user_id` bigint DEFAULT NULL COMMENT '审批人ID',
  `approve_user_name` varchar(50) DEFAULT NULL COMMENT '审批人姓名',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`inbound_id`),
  UNIQUE KEY `uk_inbound_no` (`inbound_no`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  KEY `idx_inbound_type` (`inbound_type`),
  KEY `idx_inbound_date` (`inbound_date`),
  KEY `idx_status` (`status`),
  KEY `idx_source_no` (`source_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入库单表';

-- 入库明细表
CREATE TABLE IF NOT EXISTS `inventory_inbound_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `inbound_id` bigint NOT NULL COMMENT '入库ID',
  `material_id` bigint NOT NULL COMMENT '物料ID',
  `material_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `material_name` varchar(200) DEFAULT NULL COMMENT '物料名称',
  `specification` varchar(200) DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `quantity` decimal(16,2) DEFAULT '0.00' COMMENT '数量',
  `received_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '已收货数量',
  `unit_price` decimal(16,2) DEFAULT '0.00' COMMENT '单价',
  `amount` decimal(16,2) DEFAULT '0.00' COMMENT '金额',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次号',
  `production_date` date DEFAULT NULL COMMENT '生产日期',
  `expiry_date` date DEFAULT NULL COMMENT '到期日期',
  `location_id` bigint DEFAULT NULL COMMENT '库位ID',
  `location_code` varchar(50) DEFAULT NULL COMMENT '库位编码',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`detail_id`),
  KEY `idx_inbound_id` (`inbound_id`),
  KEY `idx_material_id` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入库明细表';

-- ============================================
-- 5. 出库管理
-- ============================================

-- 出库单表
CREATE TABLE IF NOT EXISTS `inventory_outbound` (
  `outbound_id` bigint NOT NULL AUTO_INCREMENT COMMENT '出库ID',
  `outbound_no` varchar(50) NOT NULL COMMENT '出库单号',
  `outbound_type` varchar(20) DEFAULT 'SALE' COMMENT '出库类型(SALE-销售出库,PRODUCTION-生产领用,MAINTENANCE-维修领用,TRANSFER-调拨出库,SCRAP-报废出库,OTHER-其他)',
  `warehouse_id` bigint NOT NULL COMMENT '仓库ID',
  `warehouse_code` varchar(50) DEFAULT NULL COMMENT '仓库编码',
  `warehouse_name` varchar(100) DEFAULT NULL COMMENT '仓库名称',
  `destination_type` varchar(20) DEFAULT NULL COMMENT '去向类型(SALE_ORDER-销售订单,PRODUCTION_ORDER-生产订单,MAINTENANCE_ORDER-维修工单)',
  `destination_no` varchar(50) DEFAULT NULL COMMENT '去向单号',
  `destination_id` bigint DEFAULT NULL COMMENT '去向ID',
  `outbound_date` date NOT NULL COMMENT '出库日期',
  `customer_id` bigint DEFAULT NULL COMMENT '客户ID',
  `customer_name` varchar(200) DEFAULT NULL COMMENT '客户名称',
  `dept_id` bigint DEFAULT NULL COMMENT '领用部门ID',
  `dept_name` varchar(50) DEFAULT NULL COMMENT '领用部门名称',
  `recipient_id` bigint DEFAULT NULL COMMENT '领用人ID',
  `recipient_name` varchar(50) DEFAULT NULL COMMENT '领用人姓名',
  `total_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '总数量',
  `total_amount` decimal(16,2) DEFAULT '0.00' COMMENT '总金额',
  `handler_id` bigint DEFAULT NULL COMMENT '经办人ID',
  `handler_name` varchar(50) DEFAULT NULL COMMENT '经办人姓名',
  `status` varchar(20) DEFAULT 'DRAFT' COMMENT '状态(DRAFT-草稿,SUBMITTED-已提交,APPROVED-已审批,ISSUED-已发放,COMPLETED-已完成,CANCELLED-已取消)',
  `approve_user_id` bigint DEFAULT NULL COMMENT '审批人ID',
  `approve_user_name` varchar(50) DEFAULT NULL COMMENT '审批人姓名',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `issue_user_id` bigint DEFAULT NULL COMMENT '发放人ID',
  `issue_user_name` varchar(50) DEFAULT NULL COMMENT '发放人姓名',
  `issue_time` datetime DEFAULT NULL COMMENT '发放时间',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`outbound_id`),
  UNIQUE KEY `uk_outbound_no` (`outbound_no`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  KEY `idx_outbound_type` (`outbound_type`),
  KEY `idx_outbound_date` (`outbound_date`),
  KEY `idx_status` (`status`),
  KEY `idx_destination_no` (`destination_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出库单表';

-- 出库明细表
CREATE TABLE IF NOT EXISTS `inventory_outbound_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `outbound_id` bigint NOT NULL COMMENT '出库ID',
  `material_id` bigint NOT NULL COMMENT '物料ID',
  `material_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `material_name` varchar(200) DEFAULT NULL COMMENT '物料名称',
  `specification` varchar(200) DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `quantity` decimal(16,2) DEFAULT '0.00' COMMENT '数量',
  `issued_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '已发放数量',
  `unit_price` decimal(16,2) DEFAULT '0.00' COMMENT '单价',
  `amount` decimal(16,2) DEFAULT '0.00' COMMENT '金额',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次号',
  `location_id` bigint DEFAULT NULL COMMENT '库位ID',
  `location_code` varchar(50) DEFAULT NULL COMMENT '库位编码',
  `stock_id` bigint DEFAULT NULL COMMENT '库存ID',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`detail_id`),
  KEY `idx_outbound_id` (`outbound_id`),
  KEY `idx_material_id` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出库明细表';

-- ============================================
-- 6. 库存调拨
-- ============================================

-- 调拨单表
CREATE TABLE IF NOT EXISTS `inventory_transfer` (
  `transfer_id` bigint NOT NULL AUTO_INCREMENT COMMENT '调拨ID',
  `transfer_no` varchar(50) NOT NULL COMMENT '调拨单号',
  `transfer_type` varchar(20) DEFAULT 'WAREHOUSE' COMMENT '调拨类型(WAREHOUSE-仓库调拨,LOCATION-库位调拨)',
  `from_warehouse_id` bigint NOT NULL COMMENT '源仓库ID',
  `from_warehouse_code` varchar(50) DEFAULT NULL COMMENT '源仓库编码',
  `from_warehouse_name` varchar(100) DEFAULT NULL COMMENT '源仓库名称',
  `from_location_id` bigint DEFAULT NULL COMMENT '源库位ID',
  `from_location_code` varchar(50) DEFAULT NULL COMMENT '源库位编码',
  `to_warehouse_id` bigint NOT NULL COMMENT '目标仓库ID',
  `to_warehouse_code` varchar(50) DEFAULT NULL COMMENT '目标仓库编码',
  `to_warehouse_name` varchar(100) DEFAULT NULL COMMENT '目标仓库名称',
  `to_location_id` bigint DEFAULT NULL COMMENT '目标库位ID',
  `to_location_code` varchar(50) DEFAULT NULL COMMENT '目标库位编码',
  `transfer_date` date NOT NULL COMMENT '调拨日期',
  `total_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '总数量',
  `total_amount` decimal(16,2) DEFAULT '0.00' COMMENT '总金额',
  `handler_id` bigint DEFAULT NULL COMMENT '经办人ID',
  `handler_name` varchar(50) DEFAULT NULL COMMENT '经办人姓名',
  `status` varchar(20) DEFAULT 'DRAFT' COMMENT '状态(DRAFT-草稿,SUBMITTED-已提交,APPROVED-已审批,OUTBOUND-已出库,INBOUND-已入库,COMPLETED-已完成,CANCELLED-已取消)',
  `approve_user_id` bigint DEFAULT NULL COMMENT '审批人ID',
  `approve_user_name` varchar(50) DEFAULT NULL COMMENT '审批人姓名',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `outbound_time` datetime DEFAULT NULL COMMENT '出库时间',
  `inbound_time` datetime DEFAULT NULL COMMENT '入库时间',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`transfer_id`),
  UNIQUE KEY `uk_transfer_no` (`transfer_no`),
  KEY `idx_from_warehouse_id` (`from_warehouse_id`),
  KEY `idx_to_warehouse_id` (`to_warehouse_id`),
  KEY `idx_transfer_date` (`transfer_date`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='调拨单表';

-- 调拨明细表
CREATE TABLE IF NOT EXISTS `inventory_transfer_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `transfer_id` bigint NOT NULL COMMENT '调拨ID',
  `material_id` bigint NOT NULL COMMENT '物料ID',
  `material_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `material_name` varchar(200) DEFAULT NULL COMMENT '物料名称',
  `specification` varchar(200) DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `quantity` decimal(16,2) DEFAULT '0.00' COMMENT '数量',
  `outbound_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '已出库数量',
  `inbound_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '已入库数量',
  `unit_price` decimal(16,2) DEFAULT '0.00' COMMENT '单价',
  `amount` decimal(16,2) DEFAULT '0.00' COMMENT '金额',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次号',
  `from_stock_id` bigint DEFAULT NULL COMMENT '源库存ID',
  `to_stock_id` bigint DEFAULT NULL COMMENT '目标库存ID',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`detail_id`),
  KEY `idx_transfer_id` (`transfer_id`),
  KEY `idx_material_id` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='调拨明细表';

-- ============================================
-- 7. 库存调整
-- ============================================

-- 库存调整单表
CREATE TABLE IF NOT EXISTS `inventory_adjustment` (
  `adjustment_id` bigint NOT NULL AUTO_INCREMENT COMMENT '调整ID',
  `adjustment_no` varchar(50) NOT NULL COMMENT '调整单号',
  `adjustment_type` varchar(20) DEFAULT 'QUANTITY' COMMENT '调整类型(QUANTITY-数量调整,PRICE-价格调整,VALUE-价值调整)',
  `adjustment_reason` varchar(50) DEFAULT NULL COMMENT '调整原因(INVENTORY-盘点差异,LOSS-损耗,GAIN-盘盈,ERROR-错误调整,OTHER-其他)',
  `warehouse_id` bigint NOT NULL COMMENT '仓库ID',
  `warehouse_code` varchar(50) DEFAULT NULL COMMENT '仓库编码',
  `warehouse_name` varchar(100) DEFAULT NULL COMMENT '仓库名称',
  `adjustment_date` date NOT NULL COMMENT '调整日期',
  `total_items` int DEFAULT '0' COMMENT '调整项数',
  `handler_id` bigint DEFAULT NULL COMMENT '经办人ID',
  `handler_name` varchar(50) DEFAULT NULL COMMENT '经办人姓名',
  `status` varchar(20) DEFAULT 'DRAFT' COMMENT '状态(DRAFT-草稿,SUBMITTED-已提交,APPROVED-已审批,COMPLETED-已完成,CANCELLED-已取消)',
  `approve_user_id` bigint DEFAULT NULL COMMENT '审批人ID',
  `approve_user_name` varchar(50) DEFAULT NULL COMMENT '审批人姓名',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`adjustment_id`),
  UNIQUE KEY `uk_adjustment_no` (`adjustment_no`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  KEY `idx_adjustment_date` (`adjustment_date`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存调整单表';

-- 库存调整明细表
CREATE TABLE IF NOT EXISTS `inventory_adjustment_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `adjustment_id` bigint NOT NULL COMMENT '调整ID',
  `stock_id` bigint DEFAULT NULL COMMENT '库存ID',
  `material_id` bigint NOT NULL COMMENT '物料ID',
  `material_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `material_name` varchar(200) DEFAULT NULL COMMENT '物料名称',
  `location_id` bigint DEFAULT NULL COMMENT '库位ID',
  `location_code` varchar(50) DEFAULT NULL COMMENT '库位编码',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次号',
  `before_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '调整前数量',
  `after_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '调整后数量',
  `adjustment_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '调整数量',
  `before_unit_price` decimal(16,2) DEFAULT '0.00' COMMENT '调整前单价',
  `after_unit_price` decimal(16,2) DEFAULT '0.00' COMMENT '调整后单价',
  `before_total_value` decimal(16,2) DEFAULT '0.00' COMMENT '调整前总价值',
  `after_total_value` decimal(16,2) DEFAULT '0.00' COMMENT '调整后总价值',
  `adjustment_value` decimal(16,2) DEFAULT '0.00' COMMENT '调整价值',
  `reason` varchar(200) DEFAULT NULL COMMENT '调整原因说明',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`detail_id`),
  KEY `idx_adjustment_id` (`adjustment_id`),
  KEY `idx_material_id` (`material_id`),
  KEY `idx_stock_id` (`stock_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存调整明细表';

-- ============================================
-- 8. 库存盘点
-- ============================================

-- 盘点单表（扩展原有stocktaking表）
CREATE TABLE IF NOT EXISTS `inventory_stocktaking` (
  `stocktaking_id` bigint NOT NULL AUTO_INCREMENT COMMENT '盘点ID',
  `stocktaking_no` varchar(50) NOT NULL COMMENT '盘点单号',
  `stocktaking_type` varchar(20) DEFAULT 'FULL' COMMENT '盘点类型(FULL-全面盘点,PARTIAL-部分盘点,SPOT-抽查盘点)',
  `warehouse_id` bigint NOT NULL COMMENT '仓库ID',
  `warehouse_code` varchar(50) DEFAULT NULL COMMENT '仓库编码',
  `warehouse_name` varchar(100) DEFAULT NULL COMMENT '仓库名称',
  `stocktaking_date` date NOT NULL COMMENT '盘点日期',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `total_items` int DEFAULT '0' COMMENT '应盘项数',
  `counted_items` int DEFAULT '0' COMMENT '已盘项数',
  `surplus_items` int DEFAULT '0' COMMENT '盘盈项数',
  `shortage_items` int DEFAULT '0' COMMENT '盘亏项数',
  `surplus_amount` decimal(16,2) DEFAULT '0.00' COMMENT '盘盈金额',
  `shortage_amount` decimal(16,2) DEFAULT '0.00' COMMENT '盘亏金额',
  `status` varchar(20) DEFAULT 'DRAFT' COMMENT '状态(DRAFT-草稿,IN_PROGRESS-盘点中,COMPLETED-已完成,CONFIRMED-已确认,CANCELLED-已取消)',
  `inventory_user_id` bigint DEFAULT NULL COMMENT '盘点人ID',
  `inventory_user_name` varchar(50) DEFAULT NULL COMMENT '盘点人姓名',
  `confirm_user_id` bigint DEFAULT NULL COMMENT '确认人ID',
  `confirm_user_name` varchar(50) DEFAULT NULL COMMENT '确认人姓名',
  `confirm_time` datetime DEFAULT NULL COMMENT '确认时间',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`stocktaking_id`),
  UNIQUE KEY `uk_stocktaking_no` (`stocktaking_no`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  KEY `idx_stocktaking_date` (`stocktaking_date`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存盘点表';

-- 盘点明细表
CREATE TABLE IF NOT EXISTS `inventory_stocktaking_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `stocktaking_id` bigint NOT NULL COMMENT '盘点ID',
  `stock_id` bigint DEFAULT NULL COMMENT '库存ID',
  `material_id` bigint NOT NULL COMMENT '物料ID',
  `material_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `material_name` varchar(200) DEFAULT NULL COMMENT '物料名称',
  `location_id` bigint DEFAULT NULL COMMENT '库位ID',
  `location_code` varchar(50) DEFAULT NULL COMMENT '库位编码',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次号',
  `book_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '账面数量',
  `actual_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '实盘数量',
  `difference_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '差异数量',
  `unit_price` decimal(16,2) DEFAULT '0.00' COMMENT '单价',
  `difference_amount` decimal(16,2) DEFAULT '0.00' COMMENT '差异金额',
  `difference_type` varchar(20) DEFAULT NULL COMMENT '差异类型(SURPLUS-盘盈,SHORTAGE-盘亏)',
  `reason` varchar(200) DEFAULT NULL COMMENT '差异原因',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`detail_id`),
  KEY `idx_stocktaking_id` (`stocktaking_id`),
  KEY `idx_material_id` (`material_id`),
  KEY `idx_stock_id` (`stock_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='盘点明细表';

-- ============================================
-- 9. 库存预警
-- ============================================

-- 库存预警记录表
CREATE TABLE IF NOT EXISTS `inventory_warning` (
  `warning_id` bigint NOT NULL AUTO_INCREMENT COMMENT '预警ID',
  `warning_no` varchar(50) NOT NULL COMMENT '预警编号',
  `warning_type` varchar(20) DEFAULT 'LOW_STOCK' COMMENT '预警类型(LOW_STOCK-低库存,HIGH_STOCK-高库存,EXPIRY-到期预警,ABNORMAL-异常预警)',
  `warning_level` varchar(20) DEFAULT 'NORMAL' COMMENT '预警级别(LOW-低,NORMAL-正常,HIGH-高,URGENT-紧急)',
  `warehouse_id` bigint DEFAULT NULL COMMENT '仓库ID',
  `warehouse_code` varchar(50) DEFAULT NULL COMMENT '仓库编码',
  `warehouse_name` varchar(100) DEFAULT NULL COMMENT '仓库名称',
  `material_id` bigint NOT NULL COMMENT '物料ID',
  `material_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `material_name` varchar(200) DEFAULT NULL COMMENT '物料名称',
  `current_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '当前库存',
  `min_stock` decimal(16,2) DEFAULT '0.00' COMMENT '最低库存',
  `max_stock` decimal(16,2) DEFAULT NULL COMMENT '最高库存',
  `safety_stock` decimal(16,2) DEFAULT '0.00' COMMENT '安全库存',
  `expiry_date` date DEFAULT NULL COMMENT '到期日期',
  `days_to_expiry` int DEFAULT NULL COMMENT '距离到期天数',
  `warning_message` varchar(500) DEFAULT NULL COMMENT '预警信息',
  `status` varchar(20) DEFAULT 'PENDING' COMMENT '状态(PENDING-待处理,PROCESSING-处理中,RESOLVED-已解决,IGNORED-已忽略)',
  `handler_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `handler_name` varchar(50) DEFAULT NULL COMMENT '处理人姓名',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_result` varchar(500) DEFAULT NULL COMMENT '处理结果',
  `warning_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '预警时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`warning_id`),
  UNIQUE KEY `uk_warning_no` (`warning_no`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  KEY `idx_material_id` (`material_id`),
  KEY `idx_warning_type` (`warning_type`),
  KEY `idx_warning_level` (`warning_level`),
  KEY `idx_status` (`status`),
  KEY `idx_warning_time` (`warning_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存预警记录表';

-- ============================================
-- 10. 库存报表分析（统计表）
-- ============================================

-- 库存统计汇总表
CREATE TABLE IF NOT EXISTS `inventory_statistics` (
  `stat_id` bigint NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `stat_date` date NOT NULL COMMENT '统计日期',
  `stat_type` varchar(20) DEFAULT 'DAILY' COMMENT '统计类型(DAILY-日,WEEKLY-周,MONTHLY-月,QUARTERLY-季,YEARLY-年)',
  `warehouse_id` bigint DEFAULT NULL COMMENT '仓库ID',
  `warehouse_code` varchar(50) DEFAULT NULL COMMENT '仓库编码',
  `warehouse_name` varchar(100) DEFAULT NULL COMMENT '仓库名称',
  `total_materials` int DEFAULT '0' COMMENT '物料种类数',
  `total_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '总库存数量',
  `total_value` decimal(16,2) DEFAULT '0.00' COMMENT '总库存价值',
  `inbound_count` int DEFAULT '0' COMMENT '入库单数',
  `inbound_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '入库数量',
  `inbound_amount` decimal(16,2) DEFAULT '0.00' COMMENT '入库金额',
  `outbound_count` int DEFAULT '0' COMMENT '出库单数',
  `outbound_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '出库数量',
  `outbound_amount` decimal(16,2) DEFAULT '0.00' COMMENT '出库金额',
  `transfer_count` int DEFAULT '0' COMMENT '调拨单数',
  `adjustment_count` int DEFAULT '0' COMMENT '调整单数',
  `stocktaking_count` int DEFAULT '0' COMMENT '盘点单数',
  `warning_count` int DEFAULT '0' COMMENT '预警数量',
  `turnover_rate` decimal(5,2) DEFAULT NULL COMMENT '周转率',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`stat_id`),
  UNIQUE KEY `uk_stat_date_type_warehouse` (`stat_date`, `stat_type`, `warehouse_id`),
  KEY `idx_stat_date` (`stat_date`),
  KEY `idx_warehouse_id` (`warehouse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存统计汇总表';

-- ============================================
-- 初始化数据
-- ============================================

-- 插入默认仓库
INSERT INTO `inventory_warehouse` (`warehouse_code`, `warehouse_name`, `warehouse_type`, `status`, `create_user_name`) VALUES
('WH001', '主仓库', 'GENERAL', 'ACTIVE', '系统'),
('WH002', '备件仓库', 'SPARE_PART', 'ACTIVE', '系统'),
('WH003', '原材料仓库', 'RAW_MATERIAL', 'ACTIVE', '系统')
ON DUPLICATE KEY UPDATE `warehouse_name` = VALUES(`warehouse_name`);

