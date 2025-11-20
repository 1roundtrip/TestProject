-- ============================================
-- 采购申请管理表结构
-- ============================================

USE coal_erp;

-- 采购申请表
CREATE TABLE IF NOT EXISTS `purchase_requisition` (
  `requisition_id` bigint NOT NULL AUTO_INCREMENT COMMENT '申请ID',
  `requisition_no` varchar(50) NOT NULL COMMENT '申请单号',
  `plan_id` bigint DEFAULT NULL COMMENT '关联计划ID',
  `plan_no` varchar(50) DEFAULT NULL COMMENT '关联计划编号',
  `requisition_name` varchar(200) NOT NULL COMMENT '申请名称',
  `dept_id` bigint DEFAULT NULL COMMENT '申请部门ID',
  `dept_name` varchar(100) DEFAULT NULL COMMENT '申请部门名称',
  `applicant_id` bigint DEFAULT NULL COMMENT '申请人ID',
  `applicant_name` varchar(50) DEFAULT NULL COMMENT '申请人姓名',
  `total_amount` decimal(16,2) DEFAULT '0.00' COMMENT '申请总金额',
  `urgent_level` varchar(20) DEFAULT 'NORMAL' COMMENT '紧急程度(URGENT-紧急,NORMAL-正常,LOW-不急)',
  `purpose` varchar(500) DEFAULT NULL COMMENT '申请用途',
  `status` varchar(20) DEFAULT 'DRAFT' COMMENT '状态(DRAFT-草稿,SUBMITTED-已提交,APPROVING-审批中,APPROVED-已审批,REJECTED-已驳回,ORDERED-已下单,CANCELLED-已取消)',
  `approve_user_id` bigint DEFAULT NULL COMMENT '审批人ID',
  `approve_user_name` varchar(50) DEFAULT NULL COMMENT '审批人姓名',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `approve_remark` varchar(500) DEFAULT NULL COMMENT '审批意见',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`requisition_id`),
  UNIQUE KEY `uk_requisition_no` (`requisition_no`),
  KEY `idx_plan_id` (`plan_id`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_status` (`status`),
  KEY `idx_applicant_id` (`applicant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购申请表';

-- 采购申请明细表
CREATE TABLE IF NOT EXISTS `purchase_requisition_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `requisition_id` bigint NOT NULL COMMENT '申请ID',
  `item_name` varchar(200) NOT NULL COMMENT '物料名称',
  `item_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `specification` varchar(200) DEFAULT NULL COMMENT '规格型号',
  `brand` varchar(100) DEFAULT NULL COMMENT '品牌',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `quantity` decimal(16,2) DEFAULT '0.00' COMMENT '申请数量',
  `estimated_price` decimal(16,2) DEFAULT '0.00' COMMENT '预估单价',
  `estimated_amount` decimal(16,2) DEFAULT '0.00' COMMENT '预估金额',
  `required_date` date DEFAULT NULL COMMENT '需求日期',
  `purpose` varchar(500) DEFAULT NULL COMMENT '用途说明',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`detail_id`),
  KEY `idx_requisition_id` (`requisition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购申请明细表';

