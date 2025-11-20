-- ============================================
-- 维修管理系统数据库表结构
-- ============================================

USE coal_erp;

-- ============================================
-- 1. 维修工单管理
-- ============================================

-- 维修工单表
CREATE TABLE IF NOT EXISTS `maintenance_work_order` (
  `work_order_id` bigint NOT NULL AUTO_INCREMENT COMMENT '工单ID',
  `work_order_no` varchar(50) NOT NULL COMMENT '工单编号',
  `work_order_type` varchar(20) DEFAULT 'REPAIR' COMMENT '工单类型(REPAIR-维修,MAINTENANCE-保养,INSPECTION-检查,EMERGENCY-紧急)',
  `priority` varchar(20) DEFAULT 'NORMAL' COMMENT '优先级(LOW-低,NORMAL-正常,HIGH-高,URGENT-紧急)',
  `asset_id` bigint NOT NULL COMMENT '设备ID',
  `asset_code` varchar(50) DEFAULT NULL COMMENT '设备编码',
  `asset_name` varchar(200) DEFAULT NULL COMMENT '设备名称',
  `fault_type` varchar(50) DEFAULT NULL COMMENT '故障类型',
  `fault_description` text COMMENT '故障描述',
  `reported_by` bigint DEFAULT NULL COMMENT '报修人ID',
  `reported_by_name` varchar(50) DEFAULT NULL COMMENT '报修人姓名',
  `reported_time` datetime DEFAULT NULL COMMENT '报修时间',
  `assigned_team_id` bigint DEFAULT NULL COMMENT '分配维修团队ID',
  `assigned_team_name` varchar(100) DEFAULT NULL COMMENT '分配维修团队名称',
  `assigned_technician_id` bigint DEFAULT NULL COMMENT '分配技师ID',
  `assigned_technician_name` varchar(50) DEFAULT NULL COMMENT '分配技师姓名',
  `scheduled_start_time` datetime DEFAULT NULL COMMENT '计划开始时间',
  `scheduled_end_time` datetime DEFAULT NULL COMMENT '计划结束时间',
  `actual_start_time` datetime DEFAULT NULL COMMENT '实际开始时间',
  `actual_end_time` datetime DEFAULT NULL COMMENT '实际结束时间',
  `status` varchar(20) DEFAULT 'PENDING' COMMENT '状态(PENDING-待分配,ASSIGNED-已分配,IN_PROGRESS-进行中,PAUSED-暂停,COMPLETED-已完成,CANCELLED-已取消)',
  `completion_rate` decimal(5,2) DEFAULT '0.00' COMMENT '完成进度(%)',
  `labor_cost` decimal(16,2) DEFAULT '0.00' COMMENT '人工成本',
  `material_cost` decimal(16,2) DEFAULT '0.00' COMMENT '材料成本',
  `total_cost` decimal(16,2) DEFAULT '0.00' COMMENT '总成本',
  `quality_score` decimal(5,2) DEFAULT NULL COMMENT '质量评分',
  `quality_comment` varchar(500) DEFAULT NULL COMMENT '质量评价',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`work_order_id`),
  UNIQUE KEY `uk_work_order_no` (`work_order_no`),
  KEY `idx_asset_id` (`asset_id`),
  KEY `idx_status` (`status`),
  KEY `idx_reported_time` (`reported_time`),
  KEY `idx_assigned_team_id` (`assigned_team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修工单表';

-- 维修工单明细表（维修步骤/操作记录）
CREATE TABLE IF NOT EXISTS `maintenance_work_order_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `work_order_id` bigint NOT NULL COMMENT '工单ID',
  `step_no` int DEFAULT NULL COMMENT '步骤序号',
  `step_name` varchar(200) DEFAULT NULL COMMENT '步骤名称',
  `step_description` text COMMENT '步骤描述',
  `technician_id` bigint DEFAULT NULL COMMENT '执行技师ID',
  `technician_name` varchar(50) DEFAULT NULL COMMENT '执行技师姓名',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `duration` int DEFAULT NULL COMMENT '耗时(分钟)',
  `status` varchar(20) DEFAULT 'PENDING' COMMENT '状态(PENDING-待执行,IN_PROGRESS-进行中,COMPLETED-已完成)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`detail_id`),
  KEY `idx_work_order_id` (`work_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修工单明细表';

-- ============================================
-- 2. 预防性维护计划
-- ============================================

-- 预防性维护计划表
CREATE TABLE IF NOT EXISTS `maintenance_plan` (
  `plan_id` bigint NOT NULL AUTO_INCREMENT COMMENT '计划ID',
  `plan_no` varchar(50) NOT NULL COMMENT '计划编号',
  `plan_name` varchar(200) NOT NULL COMMENT '计划名称',
  `plan_type` varchar(20) DEFAULT 'PREVENTIVE' COMMENT '计划类型(PREVENTIVE-预防性,PREDICTIVE-预测性,CORRECTIVE-纠正性)',
  `asset_id` bigint NOT NULL COMMENT '设备ID',
  `asset_code` varchar(50) DEFAULT NULL COMMENT '设备编码',
  `asset_name` varchar(200) DEFAULT NULL COMMENT '设备名称',
  `maintenance_type` varchar(50) DEFAULT NULL COMMENT '维护类型(DAILY-日常,WEEKLY-周度,MONTHLY-月度,QUARTERLY-季度,YEARLY-年度)',
  `cycle_type` varchar(20) DEFAULT 'TIME' COMMENT '周期类型(TIME-时间,METER-计量,CONDITION-状态)',
  `cycle_value` int DEFAULT NULL COMMENT '周期值',
  `cycle_unit` varchar(20) DEFAULT NULL COMMENT '周期单位(DAY-天,WEEK-周,MONTH-月,YEAR-年,HOUR-小时,KM-公里)',
  `next_maintenance_date` date DEFAULT NULL COMMENT '下次维护日期',
  `last_maintenance_date` date DEFAULT NULL COMMENT '上次维护日期',
  `maintenance_content` text COMMENT '维护内容',
  `required_tools` varchar(500) DEFAULT NULL COMMENT '所需工具',
  `required_materials` varchar(500) DEFAULT NULL COMMENT '所需材料',
  `estimated_duration` int DEFAULT NULL COMMENT '预计耗时(分钟)',
  `estimated_cost` decimal(16,2) DEFAULT '0.00' COMMENT '预计成本',
  `status` varchar(20) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE-激活,PAUSED-暂停,COMPLETED-已完成,CANCELLED-已取消)',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`plan_id`),
  UNIQUE KEY `uk_plan_no` (`plan_no`),
  KEY `idx_asset_id` (`asset_id`),
  KEY `idx_status` (`status`),
  KEY `idx_next_maintenance_date` (`next_maintenance_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预防性维护计划表';

-- 维护计划执行记录表
CREATE TABLE IF NOT EXISTS `maintenance_plan_execution` (
  `execution_id` bigint NOT NULL AUTO_INCREMENT COMMENT '执行ID',
  `plan_id` bigint NOT NULL COMMENT '计划ID',
  `plan_no` varchar(50) DEFAULT NULL COMMENT '计划编号',
  `work_order_id` bigint DEFAULT NULL COMMENT '关联工单ID',
  `work_order_no` varchar(50) DEFAULT NULL COMMENT '关联工单编号',
  `scheduled_date` date NOT NULL COMMENT '计划执行日期',
  `actual_date` date DEFAULT NULL COMMENT '实际执行日期',
  `executed_by` bigint DEFAULT NULL COMMENT '执行人ID',
  `executed_by_name` varchar(50) DEFAULT NULL COMMENT '执行人姓名',
  `execution_status` varchar(20) DEFAULT 'PENDING' COMMENT '执行状态(PENDING-待执行,IN_PROGRESS-进行中,COMPLETED-已完成,SKIPPED-已跳过)',
  `actual_duration` int DEFAULT NULL COMMENT '实际耗时(分钟)',
  `actual_cost` decimal(16,2) DEFAULT '0.00' COMMENT '实际成本',
  `quality_score` decimal(5,2) DEFAULT NULL COMMENT '质量评分',
  `execution_comment` varchar(500) DEFAULT NULL COMMENT '执行备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`execution_id`),
  KEY `idx_plan_id` (`plan_id`),
  KEY `idx_work_order_id` (`work_order_id`),
  KEY `idx_scheduled_date` (`scheduled_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维护计划执行记录表';

-- ============================================
-- 3. 维修团队管理
-- ============================================

-- 维修团队表
CREATE TABLE IF NOT EXISTS `maintenance_team` (
  `team_id` bigint NOT NULL AUTO_INCREMENT COMMENT '团队ID',
  `team_code` varchar(50) NOT NULL COMMENT '团队编码',
  `team_name` varchar(100) NOT NULL COMMENT '团队名称',
  `team_type` varchar(20) DEFAULT 'GENERAL' COMMENT '团队类型(GENERAL-通用,ELECTRICAL-电气,MECHANICAL-机械,INSTRUMENT-仪表)',
  `leader_id` bigint DEFAULT NULL COMMENT '团队负责人ID',
  `leader_name` varchar(50) DEFAULT NULL COMMENT '团队负责人姓名',
  `member_count` int DEFAULT '0' COMMENT '成员数量',
  `specialty` varchar(500) DEFAULT NULL COMMENT '专业技能',
  `status` varchar(20) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE-激活,INACTIVE-停用)',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`team_id`),
  UNIQUE KEY `uk_team_code` (`team_code`),
  KEY `idx_leader_id` (`leader_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修团队表';

-- 维修团队成员表
CREATE TABLE IF NOT EXISTS `maintenance_team_member` (
  `member_id` bigint NOT NULL AUTO_INCREMENT COMMENT '成员ID',
  `team_id` bigint NOT NULL COMMENT '团队ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户姓名',
  `role` varchar(20) DEFAULT 'MEMBER' COMMENT '角色(LEADER-负责人,MEMBER-成员)',
  `skill_level` varchar(20) DEFAULT 'JUNIOR' COMMENT '技能等级(JUNIOR-初级,INTERMEDIATE-中级,SENIOR-高级,EXPERT-专家)',
  `specialty` varchar(200) DEFAULT NULL COMMENT '专长',
  `join_date` date DEFAULT NULL COMMENT '加入日期',
  `status` varchar(20) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE-激活,INACTIVE-停用)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`member_id`),
  UNIQUE KEY `uk_team_user` (`team_id`, `user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修团队成员表';

-- ============================================
-- 4. 维修备件管理
-- ============================================

-- 维修备件领用表
CREATE TABLE IF NOT EXISTS `maintenance_part_requisition` (
  `requisition_id` bigint NOT NULL AUTO_INCREMENT COMMENT '领用ID',
  `requisition_no` varchar(50) NOT NULL COMMENT '领用单号',
  `work_order_id` bigint DEFAULT NULL COMMENT '关联工单ID',
  `work_order_no` varchar(50) DEFAULT NULL COMMENT '关联工单编号',
  `requisition_type` varchar(20) DEFAULT 'REPAIR' COMMENT '领用类型(REPAIR-维修,MAINTENANCE-保养,STOCK-库存)',
  `requisition_date` date NOT NULL COMMENT '领用日期',
  `requisition_by` bigint DEFAULT NULL COMMENT '领用人ID',
  `requisition_by_name` varchar(50) DEFAULT NULL COMMENT '领用人姓名',
  `warehouse_id` bigint DEFAULT NULL COMMENT '仓库ID',
  `warehouse_name` varchar(100) DEFAULT NULL COMMENT '仓库名称',
  `total_amount` decimal(16,2) DEFAULT '0.00' COMMENT '总金额',
  `status` varchar(20) DEFAULT 'PENDING' COMMENT '状态(PENDING-待审批,APPROVED-已审批,ISSUED-已发放,REJECTED-已拒绝)',
  `approve_by` bigint DEFAULT NULL COMMENT '审批人ID',
  `approve_by_name` varchar(50) DEFAULT NULL COMMENT '审批人姓名',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `issue_by` bigint DEFAULT NULL COMMENT '发放人ID',
  `issue_by_name` varchar(50) DEFAULT NULL COMMENT '发放人姓名',
  `issue_time` datetime DEFAULT NULL COMMENT '发放时间',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`requisition_id`),
  UNIQUE KEY `uk_requisition_no` (`requisition_no`),
  KEY `idx_work_order_id` (`work_order_id`),
  KEY `idx_status` (`status`),
  KEY `idx_requisition_date` (`requisition_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修备件领用表';

-- 维修备件领用明细表
CREATE TABLE IF NOT EXISTS `maintenance_part_requisition_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `requisition_id` bigint NOT NULL COMMENT '领用ID',
  `material_id` bigint DEFAULT NULL COMMENT '物料ID',
  `material_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `material_name` varchar(200) DEFAULT NULL COMMENT '物料名称',
  `specification` varchar(200) DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `quantity` decimal(16,2) DEFAULT '0.00' COMMENT '领用数量',
  `issued_quantity` decimal(16,2) DEFAULT '0.00' COMMENT '已发放数量',
  `unit_price` decimal(16,2) DEFAULT '0.00' COMMENT '单价',
  `amount` decimal(16,2) DEFAULT '0.00' COMMENT '金额',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`detail_id`),
  KEY `idx_requisition_id` (`requisition_id`),
  KEY `idx_material_id` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修备件领用明细表';

-- ============================================
-- 5. 维修质量管理
-- ============================================

-- 维修质量检查表
CREATE TABLE IF NOT EXISTS `maintenance_quality_check` (
  `check_id` bigint NOT NULL AUTO_INCREMENT COMMENT '检查ID',
  `check_no` varchar(50) NOT NULL COMMENT '检查单号',
  `work_order_id` bigint NOT NULL COMMENT '工单ID',
  `work_order_no` varchar(50) DEFAULT NULL COMMENT '工单编号',
  `check_type` varchar(20) DEFAULT 'FINAL' COMMENT '检查类型(INSPECTION-巡检,FINAL-终检,REPAIR-返修)',
  `check_date` date NOT NULL COMMENT '检查日期',
  `checker_id` bigint DEFAULT NULL COMMENT '检查人ID',
  `checker_name` varchar(50) DEFAULT NULL COMMENT '检查人姓名',
  `check_items` text COMMENT '检查项目(JSON格式)',
  `check_result` varchar(20) DEFAULT 'PASS' COMMENT '检查结果(PASS-合格,FAIL-不合格,CONDITIONAL-有条件合格)',
  `quality_score` decimal(5,2) DEFAULT NULL COMMENT '质量评分',
  `defect_description` text COMMENT '缺陷描述',
  `rectification_required` varchar(1) DEFAULT 'N' COMMENT '是否需要整改(Y-是,N-否)',
  `rectification_deadline` date DEFAULT NULL COMMENT '整改期限',
  `rectification_status` varchar(20) DEFAULT NULL COMMENT '整改状态(PENDING-待整改,IN_PROGRESS-整改中,COMPLETED-已完成)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`check_id`),
  UNIQUE KEY `uk_check_no` (`check_no`),
  KEY `idx_work_order_id` (`work_order_id`),
  KEY `idx_check_date` (`check_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修质量检查表';

-- ============================================
-- 6. 维修成本管理
-- ============================================

-- 维修成本表
CREATE TABLE IF NOT EXISTS `maintenance_cost` (
  `cost_id` bigint NOT NULL AUTO_INCREMENT COMMENT '成本ID',
  `work_order_id` bigint NOT NULL COMMENT '工单ID',
  `work_order_no` varchar(50) DEFAULT NULL COMMENT '工单编号',
  `cost_type` varchar(20) DEFAULT 'LABOR' COMMENT '成本类型(LABOR-人工,MATERIAL-材料,OUTSOURCING-外包,OTHER-其他)',
  `cost_category` varchar(50) DEFAULT NULL COMMENT '成本分类',
  `cost_item` varchar(200) DEFAULT NULL COMMENT '成本项目',
  `quantity` decimal(16,2) DEFAULT '0.00' COMMENT '数量',
  `unit_price` decimal(16,2) DEFAULT '0.00' COMMENT '单价',
  `amount` decimal(16,2) DEFAULT '0.00' COMMENT '金额',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商ID',
  `supplier_name` varchar(200) DEFAULT NULL COMMENT '供应商名称',
  `invoice_no` varchar(50) DEFAULT NULL COMMENT '发票号',
  `cost_date` date DEFAULT NULL COMMENT '成本发生日期',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`cost_id`),
  KEY `idx_work_order_id` (`work_order_id`),
  KEY `idx_cost_type` (`cost_type`),
  KEY `idx_cost_date` (`cost_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修成本表';

-- ============================================
-- 7. 设备故障分析
-- ============================================

-- 设备故障记录表
CREATE TABLE IF NOT EXISTS `maintenance_fault_record` (
  `fault_id` bigint NOT NULL AUTO_INCREMENT COMMENT '故障ID',
  `fault_no` varchar(50) NOT NULL COMMENT '故障编号',
  `asset_id` bigint NOT NULL COMMENT '设备ID',
  `asset_code` varchar(50) DEFAULT NULL COMMENT '设备编码',
  `asset_name` varchar(200) DEFAULT NULL COMMENT '设备名称',
  `fault_type` varchar(50) DEFAULT NULL COMMENT '故障类型',
  `fault_category` varchar(50) DEFAULT NULL COMMENT '故障分类',
  `fault_severity` varchar(20) DEFAULT 'MEDIUM' COMMENT '故障严重程度(LOW-低,MEDIUM-中,HIGH-高,CRITICAL-严重)',
  `fault_description` text COMMENT '故障描述',
  `fault_cause` text COMMENT '故障原因',
  `fault_symptom` text COMMENT '故障现象',
  `occurred_time` datetime DEFAULT NULL COMMENT '发生时间',
  `reported_time` datetime DEFAULT NULL COMMENT '报告时间',
  `resolved_time` datetime DEFAULT NULL COMMENT '解决时间',
  `downtime` int DEFAULT NULL COMMENT '停机时间(分钟)',
  `work_order_id` bigint DEFAULT NULL COMMENT '关联工单ID',
  `work_order_no` varchar(50) DEFAULT NULL COMMENT '关联工单编号',
  `resolution_method` text COMMENT '解决方法',
  `prevention_measure` text COMMENT '预防措施',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`fault_id`),
  UNIQUE KEY `uk_fault_no` (`fault_no`),
  KEY `idx_asset_id` (`asset_id`),
  KEY `idx_fault_type` (`fault_type`),
  KEY `idx_occurred_time` (`occurred_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备故障记录表';

-- ============================================
-- 8. 维修绩效考核
-- ============================================

-- 维修绩效考核表
CREATE TABLE IF NOT EXISTS `maintenance_performance` (
  `performance_id` bigint NOT NULL AUTO_INCREMENT COMMENT '绩效ID',
  `evaluation_period` varchar(20) DEFAULT NULL COMMENT '考核周期(MONTHLY-月度,QUARTERLY-季度,YEARLY-年度)',
  `evaluation_date` date NOT NULL COMMENT '考核日期',
  `evaluated_user_id` bigint NOT NULL COMMENT '被考核人ID',
  `evaluated_user_name` varchar(50) DEFAULT NULL COMMENT '被考核人姓名',
  `team_id` bigint DEFAULT NULL COMMENT '团队ID',
  `team_name` varchar(100) DEFAULT NULL COMMENT '团队名称',
  `work_order_count` int DEFAULT '0' COMMENT '工单数量',
  `completed_count` int DEFAULT '0' COMMENT '完成数量',
  `completion_rate` decimal(5,2) DEFAULT '0.00' COMMENT '完成率(%)',
  `average_completion_time` decimal(10,2) DEFAULT NULL COMMENT '平均完成时间(小时)',
  `quality_score` decimal(5,2) DEFAULT NULL COMMENT '质量评分',
  `customer_satisfaction` decimal(5,2) DEFAULT NULL COMMENT '客户满意度',
  `cost_efficiency` decimal(5,2) DEFAULT NULL COMMENT '成本效率',
  `total_score` decimal(5,2) DEFAULT NULL COMMENT '总分',
  `performance_level` varchar(20) DEFAULT NULL COMMENT '绩效等级(EXCELLENT-优秀,GOOD-良好,AVERAGE-一般,POOR-较差)',
  `evaluator_id` bigint DEFAULT NULL COMMENT '考核人ID',
  `evaluator_name` varchar(50) DEFAULT NULL COMMENT '考核人姓名',
  `evaluation_comment` varchar(500) DEFAULT NULL COMMENT '考核评价',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`performance_id`),
  KEY `idx_evaluated_user_id` (`evaluated_user_id`),
  KEY `idx_evaluation_date` (`evaluation_date`),
  KEY `idx_team_id` (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修绩效考核表';

-- ============================================
-- 9. 维修报表分析（视图和统计表）
-- ============================================

-- 维修统计汇总表（用于报表分析）
CREATE TABLE IF NOT EXISTS `maintenance_statistics` (
  `stat_id` bigint NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `stat_date` date NOT NULL COMMENT '统计日期',
  `stat_type` varchar(20) DEFAULT 'DAILY' COMMENT '统计类型(DAILY-日,WEEKLY-周,MONTHLY-月,QUARTERLY-季,YEARLY-年)',
  `total_work_orders` int DEFAULT '0' COMMENT '工单总数',
  `completed_work_orders` int DEFAULT '0' COMMENT '完成工单数',
  `pending_work_orders` int DEFAULT '0' COMMENT '待处理工单数',
  `average_completion_time` decimal(10,2) DEFAULT NULL COMMENT '平均完成时间(小时)',
  `total_labor_cost` decimal(16,2) DEFAULT '0.00' COMMENT '总人工成本',
  `total_material_cost` decimal(16,2) DEFAULT '0.00' COMMENT '总材料成本',
  `total_cost` decimal(16,2) DEFAULT '0.00' COMMENT '总成本',
  `average_quality_score` decimal(5,2) DEFAULT NULL COMMENT '平均质量评分',
  `fault_count` int DEFAULT '0' COMMENT '故障次数',
  `downtime_hours` decimal(10,2) DEFAULT '0.00' COMMENT '停机时间(小时)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`stat_id`),
  UNIQUE KEY `uk_stat_date_type` (`stat_date`, `stat_type`),
  KEY `idx_stat_date` (`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修统计汇总表';

-- ============================================
-- 10. 移动维修支持（扩展字段）
-- ============================================

-- 维修工单移动端扩展字段（在maintenance_work_order表中已包含）
-- 移动端签到记录表
CREATE TABLE IF NOT EXISTS `maintenance_mobile_checkin` (
  `checkin_id` bigint NOT NULL AUTO_INCREMENT COMMENT '签到ID',
  `work_order_id` bigint NOT NULL COMMENT '工单ID',
  `technician_id` bigint NOT NULL COMMENT '技师ID',
  `technician_name` varchar(50) DEFAULT NULL COMMENT '技师姓名',
  `checkin_type` varchar(20) DEFAULT 'ARRIVAL' COMMENT '签到类型(ARRIVAL-到达,START-开始,COMPLETE-完成)',
  `checkin_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
  `location` varchar(200) DEFAULT NULL COMMENT '位置信息',
  `latitude` decimal(10,7) DEFAULT NULL COMMENT '纬度',
  `longitude` decimal(10,7) DEFAULT NULL COMMENT '经度',
  `photo_url` varchar(500) DEFAULT NULL COMMENT '现场照片URL',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`checkin_id`),
  KEY `idx_work_order_id` (`work_order_id`),
  KEY `idx_technician_id` (`technician_id`),
  KEY `idx_checkin_time` (`checkin_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='移动端签到记录表';

-- ============================================
-- 初始化数据
-- ============================================

-- 插入默认维修团队
INSERT INTO `maintenance_team` (`team_code`, `team_name`, `team_type`, `status`, `create_user_name`) VALUES
('MT001', '电气维修组', 'ELECTRICAL', 'ACTIVE', '系统'),
('MT002', '机械维修组', 'MECHANICAL', 'ACTIVE', '系统'),
('MT003', '仪表维修组', 'INSTRUMENT', 'ACTIVE', '系统'),
('MT004', '综合维修组', 'GENERAL', 'ACTIVE', '系统')
ON DUPLICATE KEY UPDATE `team_name` = VALUES(`team_name`);

