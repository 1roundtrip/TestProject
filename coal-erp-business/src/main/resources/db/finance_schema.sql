-- 财务模块数据库表结构

-- 会计科目表
CREATE TABLE IF NOT EXISTS `account_subject` (
  `subject_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '科目ID',
  `subject_code` varchar(20) NOT NULL COMMENT '科目编码',
  `subject_name` varchar(100) NOT NULL COMMENT '科目名称',
  `subject_level` int(2) NOT NULL COMMENT '科目级别(1-一级科目,2-二级科目)',
  `subject_type` varchar(20) NOT NULL COMMENT '科目类型(ASSET-资产类,LIABILITY-负债类,EQUITY-权益类,COST-成本类,PROFIT-损益类)',
  `balance_direction` varchar(10) NOT NULL COMMENT '余额方向(1-借方,-1-贷方)',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父科目ID',
  `is_leaf` char(1) DEFAULT '1' COMMENT '是否末级科目(0-否,1-是)',
  `status` char(1) DEFAULT '0' COMMENT '状态(0-正常,1-停用)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`subject_id`),
  UNIQUE KEY `uk_subject_code` (`subject_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会计科目表';

-- 凭证主表
CREATE TABLE IF NOT EXISTS `voucher` (
  `voucher_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '凭证ID',
  `voucher_no` varchar(20) NOT NULL COMMENT '凭证号',
  `voucher_date` date NOT NULL COMMENT '凭证日期',
  `period` varchar(6) NOT NULL COMMENT '会计期间(YYYYMM)',
  `attach_count` int(4) DEFAULT '0' COMMENT '附件数',
  `total_amount` decimal(16,2) DEFAULT '0.00' COMMENT '合计金额',
  `maker_id` bigint(20) DEFAULT NULL COMMENT '制单人ID',
  `maker_name` varchar(50) DEFAULT NULL COMMENT '制单人',
  `maker_time` datetime DEFAULT NULL COMMENT '制单时间',
  `auditor_id` bigint(20) DEFAULT NULL COMMENT '审核人ID',
  `auditor_name` varchar(50) DEFAULT NULL COMMENT '审核人',
  `auditor_time` datetime DEFAULT NULL COMMENT '审核时间',
  `poster_id` bigint(20) DEFAULT NULL COMMENT '记账人ID',
  `poster_name` varchar(50) DEFAULT NULL COMMENT '记账人',
  `poster_time` datetime DEFAULT NULL COMMENT '记账时间',
  `status` varchar(20) DEFAULT 'DRAFT' COMMENT '状态(DRAFT-草稿,AUDITED-已审核,POSTED-已记账)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`voucher_id`),
  UNIQUE KEY `uk_voucher_no` (`voucher_no`,`period`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='凭证主表';

-- 凭证明细表
CREATE TABLE IF NOT EXISTS `voucher_detail` (
  `detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `voucher_id` bigint(20) NOT NULL COMMENT '凭证ID',
  `entry_no` int(4) NOT NULL COMMENT '分录号',
  `subject_id` bigint(20) NOT NULL COMMENT '科目ID',
  `subject_code` varchar(20) DEFAULT NULL COMMENT '科目编码',
  `subject_name` varchar(100) DEFAULT NULL COMMENT '科目名称',
  `direction` varchar(10) NOT NULL COMMENT '方向(1-借方,-1-贷方)',
  `amount` decimal(16,2) NOT NULL COMMENT '金额',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID(辅助核算)',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID(辅助核算)',
  `staff_id` bigint(20) DEFAULT NULL COMMENT '人员ID(辅助核算)',
  `summary` varchar(200) DEFAULT NULL COMMENT '摘要',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`detail_id`),
  KEY `idx_voucher_id` (`voucher_id`),
  KEY `idx_subject_id` (`subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='凭证明细表';

-- 科目余额表
CREATE TABLE IF NOT EXISTS `account_balance` (
  `balance_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '余额ID',
  `subject_id` bigint(20) NOT NULL COMMENT '科目ID',
  `subject_code` varchar(20) DEFAULT NULL COMMENT '科目编码',
  `subject_name` varchar(100) DEFAULT NULL COMMENT '科目名称',
  `period` varchar(6) NOT NULL COMMENT '会计期间(YYYYMM)',
  `begin_direction` varchar(10) DEFAULT NULL COMMENT '期初余额方向(1-借方,-1-贷方)',
  `begin_amount` decimal(16,2) DEFAULT '0.00' COMMENT '期初余额',
  `debit_amount` decimal(16,2) DEFAULT '0.00' COMMENT '本期借方',
  `credit_amount` decimal(16,2) DEFAULT '0.00' COMMENT '本期贷方',
  `end_direction` varchar(10) DEFAULT NULL COMMENT '期末余额方向(1-借方,-1-贷方)',
  `end_amount` decimal(16,2) DEFAULT '0.00' COMMENT '期末余额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`balance_id`),
  UNIQUE KEY `uk_subject_period` (`subject_id`,`period`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科目余额表';

-- 初始化会计科目
INSERT INTO `account_subject` (`subject_code`, `subject_name`, `subject_level`, `subject_type`, `balance_direction`, `parent_id`, `is_leaf`, `status`) VALUES
('1001', '库存现金', 1, 'ASSET', '1', 0, '1', '0'),
('1002', '银行存款', 1, 'ASSET', '1', 0, '1', '0'),
('1122', '应收账款', 1, 'ASSET', '1', 0, '1', '0'),
('1403', '原材料', 1, 'ASSET', '1', 0, '1', '0'),
('1601', '固定资产', 1, 'ASSET', '1', 0, '1', '0'),
('2001', '短期借款', 1, 'LIABILITY', '-1', 0, '1', '0'),
('2202', '应付账款', 1, 'LIABILITY', '-1', 0, '1', '0'),
('4001', '实收资本', 1, 'EQUITY', '-1', 0, '1', '0'),
('5001', '生产成本', 1, 'COST', '1', 0, '1', '0'),
('6001', '主营业务收入', 1, 'PROFIT', '-1', 0, '1', '0'),
('6401', '主营业务成本', 1, 'PROFIT', '1', 0, '1', '0'),
('6601', '销售费用', 1, 'PROFIT', '1', 0, '1', '0'),
('6602', '管理费用', 1, 'PROFIT', '1', 0, '1', '0');