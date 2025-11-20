-- 煤炭行业成本核算系统数据库设计

-- 成本中心表
CREATE TABLE IF NOT EXISTS cost_center (
  center_id bigint NOT NULL AUTO_INCREMENT,
  center_code varchar(50) NOT NULL COMMENT '成本中心编码',
  center_name varchar(100) NOT NULL COMMENT '成本中心名称',
  center_type varchar(20) NOT NULL COMMENT '类型(MINING-采矿区,PLANT-选煤厂,MAINTENANCE-维修车间,ADMIN-管理部门)',
  parent_id bigint DEFAULT NULL COMMENT '父中心ID',
  manager_id bigint DEFAULT NULL COMMENT '负责人',
  status tinyint DEFAULT 1 COMMENT '状态(0-停用 1-启用)',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (center_id),
  UNIQUE KEY uk_center_code (center_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成本中心';

-- 成本要素表
CREATE TABLE IF NOT EXISTS cost_element (
  element_id bigint NOT NULL AUTO_INCREMENT,
  element_code varchar(50) NOT NULL COMMENT '要素编码',
  element_name varchar(100) NOT NULL COMMENT '要素名称',
  element_type varchar(20) NOT NULL COMMENT '类型(MATERIAL-材料,LABOR-人工,OVERHEAD-制造费用,SAFETY-安全费用,ENV-环境成本)',
  data_type varchar(20) DEFAULT 'NUMBER' COMMENT '数据类型',
  unit varchar(20) DEFAULT NULL COMMENT '单位',
  is_direct tinyint DEFAULT 1 COMMENT '是否直接成本',
  status tinyint DEFAULT 1 COMMENT '状态(0-停用 1-启用)',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (element_id),
  UNIQUE KEY uk_element_code (element_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成本要素';

-- 产品表
CREATE TABLE IF NOT EXISTS cost_product (
  product_id bigint NOT NULL AUTO_INCREMENT,
  product_code varchar(50) NOT NULL COMMENT '产品编码',
  product_name varchar(100) NOT NULL COMMENT '产品名称',
  product_type varchar(20) NOT NULL COMMENT '类型(RAW-原煤,CLEAN-精煤,SLIME-煤泥,OTHER-其他)',
  spec varchar(100) DEFAULT NULL COMMENT '规格',
  unit varchar(20) DEFAULT '吨' COMMENT '计量单位',
  status tinyint DEFAULT 1 COMMENT '状态(0-停用 1-启用)',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (product_id),
  UNIQUE KEY uk_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品成本对象';

-- 费用归集表
CREATE TABLE IF NOT EXISTS cost_collection (
  collection_id bigint NOT NULL AUTO_INCREMENT,
  period varchar(20) NOT NULL COMMENT '会计期间',
  center_id bigint NOT NULL COMMENT '成本中心',
  element_id bigint NOT NULL COMMENT '成本要素',
  amount decimal(18,4) NOT NULL COMMENT '金额',
  quantity decimal(18,4) DEFAULT NULL COMMENT '数量',
  unit_price decimal(18,4) DEFAULT NULL COMMENT '单价',
  source_type varchar(20) DEFAULT NULL COMMENT '来源类型',
  source_no varchar(50) DEFAULT NULL COMMENT '来源单号',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  create_user_id bigint DEFAULT NULL COMMENT '创建人',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (collection_id),
  UNIQUE KEY uk_period_center_element (period, center_id, element_id, source_no),
  KEY idx_center_id (center_id),
  KEY idx_element_id (element_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='费用归集表';

-- 成本分配表
CREATE TABLE IF NOT EXISTS cost_allocation (
  allocation_id bigint NOT NULL AUTO_INCREMENT,
  period varchar(20) NOT NULL COMMENT '会计期间',
  from_center_id bigint NOT NULL COMMENT '来源成本中心',
  to_center_id bigint DEFAULT NULL COMMENT '目标成本中心',
  to_product_id bigint DEFAULT NULL COMMENT '目标产品',
  element_id bigint NOT NULL COMMENT '成本要素',
  amount decimal(18,4) NOT NULL COMMENT '分配金额',
  driver_type varchar(20) DEFAULT NULL COMMENT '分配动因类型',
  driver_value decimal(18,4) DEFAULT NULL COMMENT '分配动因值',
  allocation_rate decimal(18,6) DEFAULT NULL COMMENT '分配率',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (allocation_id),
  KEY idx_period_center (period, from_center_id),
  KEY idx_element (element_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成本分配表';

-- 产品成本表
CREATE TABLE IF NOT EXISTS product_cost (
  cost_id bigint NOT NULL AUTO_INCREMENT,
  period varchar(20) NOT NULL COMMENT '会计期间',
  product_id bigint NOT NULL COMMENT '产品',
  element_id bigint NOT NULL COMMENT '成本要素',
  quantity decimal(18,4) DEFAULT NULL COMMENT '产量',
  unit_cost decimal(18,4) DEFAULT NULL COMMENT '单位成本',
  total_cost decimal(18,4) DEFAULT NULL COMMENT '总成本',
  calc_time datetime DEFAULT NULL COMMENT '计算时间',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (cost_id),
  UNIQUE KEY uk_period_product_element (period, product_id, element_id),
  KEY idx_period (period),
  KEY idx_product (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品成本表';

-- 初始化煤炭行业成本中心
INSERT INTO cost_center (center_code, center_name, center_type, status) VALUES 
('MINE-001', '第一采区', 'MINING', 1),
('MINE-002', '第二采区', 'MINING', 1),
('PLANT-001', '主洗选厂', 'PLANT', 1),
('MTN-001', '机电维修车间', 'MAINTENANCE', 1),
('ADM-001', '生产管理部', 'ADMIN', 1);

-- 初始化煤炭行业成本要素
INSERT INTO cost_element (element_code, element_name, element_type, unit, is_direct) VALUES 
('MAT-001', '爆破材料', 'MATERIAL', '千克', 1),
('MAT-002', '支护材料', 'MATERIAL', '件', 1),
('MAT-003', '油脂材料', 'MATERIAL', '升', 1),
('LAB-001', '采煤工资', 'LABOR', '人', 1),
('LAB-002', '井下津贴', 'LABOR', '人', 1),
('OHD-001', '设备折旧', 'OVERHEAD', NULL, 0),
('OHD-002', '电力费用', 'OVERHEAD', '度', 0),
('SAF-001', '安全投入', 'SAFETY', NULL, 0),
('ENV-001', '环境治理', 'ENV', NULL, 0);

-- 初始化煤炭产品
INSERT INTO cost_product (product_code, product_name, product_type, unit) VALUES 
('COAL-RAW', '原煤', 'RAW', '吨'),
('COAL-CLEAN', '精煤', 'CLEAN', '吨'),
('COAL-SLIME', '煤泥', 'SLIME', '吨');