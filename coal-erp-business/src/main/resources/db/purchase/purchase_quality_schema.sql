-- ============================================
-- 采购质检管理表结构
-- ============================================

USE coal_erp;

-- 采购质检表
CREATE TABLE IF NOT EXISTS `purchase_quality_check` (
  `check_id` bigint NOT NULL AUTO_INCREMENT COMMENT '质检ID',
  `check_no` varchar(50) NOT NULL COMMENT '质检单号',
  `receiving_id` bigint NOT NULL COMMENT '关联收货ID',
  `receiving_no` varchar(50) NOT NULL COMMENT '关联收货单号',
  `order_id` bigint DEFAULT NULL COMMENT '关联订单ID',
  `order_no` varchar(50) DEFAULT NULL COMMENT '关联订单号',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商ID',
  `supplier_name` varchar(200) DEFAULT NULL COMMENT '供应商名称',
  `check_date` date NOT NULL COMMENT '质检日期',
  `check_type` varchar(50) DEFAULT NULL COMMENT '质检类型(INCOMING-来料检验,PROCESS-过程检验,FINAL-最终检验)',
  `check_method` varchar(100) DEFAULT NULL COMMENT '检验方法',
  `check_standard` varchar(500) DEFAULT NULL COMMENT '检验标准',
  `total_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '检验总数量',
  `qualified_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '合格数量',
  `unqualified_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '不合格数量',
  `qualified_rate` decimal(5,2) DEFAULT '0.00' COMMENT '合格率(%)',
  `check_result` varchar(20) DEFAULT NULL COMMENT '检验结果(PASSED-合格,FAILED-不合格,PARTIAL-部分合格)',
  `checker_id` bigint DEFAULT NULL COMMENT '检验人ID',
  `checker_name` varchar(50) DEFAULT NULL COMMENT '检验人姓名',
  `approve_user_id` bigint DEFAULT NULL COMMENT '审核人ID',
  `approve_user_name` varchar(50) DEFAULT NULL COMMENT '审核人姓名',
  `approve_time` datetime DEFAULT NULL COMMENT '审核时间',
  `status` varchar(20) DEFAULT 'DRAFT' COMMENT '状态(DRAFT-草稿,CHECKING-检验中,APPROVED-已审核,COMPLETED-已完成)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`check_id`),
  UNIQUE KEY `uk_check_no` (`check_no`),
  KEY `idx_receiving_id` (`receiving_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_status` (`status`),
  KEY `idx_check_date` (`check_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购质检表';

-- 采购质检明细表
CREATE TABLE IF NOT EXISTS `purchase_quality_check_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `check_id` bigint NOT NULL COMMENT '质检ID',
  `receiving_detail_id` bigint DEFAULT NULL COMMENT '关联收货明细ID',
  `item_name` varchar(200) NOT NULL COMMENT '物料名称',
  `item_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `specification` varchar(200) DEFAULT NULL COMMENT '规格型号',
  `check_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '检验数量',
  `qualified_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '合格数量',
  `unqualified_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '不合格数量',
  `check_item` varchar(200) DEFAULT NULL COMMENT '检验项目',
  `check_result` varchar(20) DEFAULT NULL COMMENT '检验结果(PASSED-合格,FAILED-不合格)',
  `defect_description` varchar(500) DEFAULT NULL COMMENT '缺陷描述',
  `disposal_method` varchar(100) DEFAULT NULL COMMENT '处理方式(ACCEPT-接收,REJECT-拒收,REPAIR-返修,REPLACE-更换)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`detail_id`),
  KEY `idx_check_id` (`check_id`),
  KEY `idx_receiving_detail_id` (`receiving_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购质检明细表';

