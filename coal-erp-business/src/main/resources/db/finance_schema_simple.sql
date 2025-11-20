-- 财务模块数据库表结构（简化版）

-- 会计科目表
CREATE TABLE account_subject (
  subject_id bigint AUTO_INCREMENT PRIMARY KEY,
  subject_code varchar(20) NOT NULL,
  subject_name varchar(100) NOT NULL,
  subject_type varchar(20) NOT NULL,
  balance_direction varchar(10) NOT NULL,
  status char(1) DEFAULT '0',
  create_time datetime,
  update_time datetime,
  UNIQUE KEY (subject_code)
);

-- 凭证主表
CREATE TABLE voucher (
  voucher_id bigint AUTO_INCREMENT PRIMARY KEY,
  voucher_no varchar(20) NOT NULL,
  voucher_date date NOT NULL,
  period varchar(6) NOT NULL,
  status varchar(20) DEFAULT 'DRAFT',
  total_amount decimal(16,2) DEFAULT 0.00,
  maker_name varchar(50),
  maker_time datetime,
  auditor_name varchar(50),
  auditor_time datetime,
  poster_name varchar(50),
  poster_time datetime,
  create_time datetime,
  update_time datetime,
  UNIQUE KEY (voucher_no, period)
);

-- 凭证明细表
CREATE TABLE voucher_detail (
  detail_id bigint AUTO_INCREMENT PRIMARY KEY,
  voucher_id bigint NOT NULL,
  subject_id bigint NOT NULL,
  direction varchar(10) NOT NULL,
  amount decimal(16,2) NOT NULL,
  summary varchar(200),
  create_time datetime,
  KEY (voucher_id),
  KEY (subject_id)
);

-- 初始化会计科目
INSERT INTO account_subject (subject_code, subject_name, subject_type, balance_direction, status) VALUES
('1001', '库存现金', 'ASSET', '1', '0'),
('1002', '银行存款', 'ASSET', '1', '0'),
('1122', '应收账款', 'ASSET', '1', '0'),
('2001', '短期借款', 'LIABILITY', '-1', '0'),
('2202', '应付账款', 'LIABILITY', '-1', '0'),
('4001', '实收资本', 'EQUITY', '-1', '0'),
('5001', '生产成本', 'COST', '1', '0'),
('6001', '主营业务收入', 'PROFIT', '-1', '0'),
('6401', '主营业务成本', 'PROFIT', '1', '0');