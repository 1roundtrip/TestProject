-- ============================================
-- 采购计划管理表结构
-- ============================================

USE coal_erp;

-- 采购计划表
CREATE TABLE IF NOT EXISTS `purchase_plan` (
  `plan_id` bigint NOT NULL AUTO_INCREMENT COMMENT '计划ID',
  `plan_no` varchar(50) NOT NULL COMMENT '计划编号',
  `plan_name` varchar(200) NOT NULL COMMENT '计划名称',
  `plan_year` int NOT NULL COMMENT '计划年度',
  `plan_quarter` int DEFAULT NULL COMMENT '计划季度(1-4)',
  `plan_month` int DEFAULT NULL COMMENT '计划月份(1-12)',
  `dept_id` bigint DEFAULT NULL COMMENT '申请部门ID',
  `dept_name` varchar(100) DEFAULT NULL COMMENT '申请部门名称',
  `budget_amount` decimal(16,2) DEFAULT '0.00' COMMENT '预算金额',
  `total_amount` decimal(16,2) DEFAULT '0.00' COMMENT '计划总金额',
  `status` varchar(20) DEFAULT 'DRAFT' COMMENT '状态(DRAFT-草稿,SUBMITTED-已提交,APPROVED-已审批,REJECTED-已驳回,EXECUTING-执行中,COMPLETED-已完成,CANCELLED-已取消)',
  `approve_user_id` bigint DEFAULT NULL COMMENT '审批人ID',
  `approve_user_name` varchar(50) DEFAULT NULL COMMENT '审批人姓名',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `approve_remark` varchar(500) DEFAULT NULL COMMENT '审批意见',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`plan_id`),
  UNIQUE KEY `uk_plan_no` (`plan_no`),
  KEY `idx_plan_year` (`plan_year`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购计划表';

-- 采购计划明细表
CREATE TABLE IF NOT EXISTS `purchase_plan_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `plan_id` bigint NOT NULL COMMENT '计划ID',
  `item_name` varchar(200) NOT NULL COMMENT '物料名称',
  `item_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `specification` varchar(200) DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `quantity` decimal(16,2) DEFAULT '0.00' COMMENT '计划数量',
  `estimated_price` decimal(16,2) DEFAULT '0.00' COMMENT '预估单价',
  `estimated_amount` decimal(16,2) DEFAULT '0.00' COMMENT '预估金额',
  `purpose` varchar(500) DEFAULT NULL COMMENT '用途说明',
  `required_date` date DEFAULT NULL COMMENT '需求日期',
  `priority` varchar(20) DEFAULT 'NORMAL' COMMENT '优先级(HIGH-高,MEDIUM-中,LOW-低)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`detail_id`),
  KEY `idx_plan_id` (`plan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购计划明细表';

