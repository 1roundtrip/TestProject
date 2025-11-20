-- 报表分析平台数据库设计

-- 报表分类表
CREATE TABLE IF NOT EXISTS report_category (
  category_id bigint NOT NULL AUTO_INCREMENT,
  category_name varchar(50) NOT NULL COMMENT '分类名称',
  category_code varchar(50) NOT NULL COMMENT '分类编码',
  sort_order int DEFAULT 0 COMMENT '排序',
  icon varchar(100) DEFAULT NULL COMMENT '图标',
  status tinyint DEFAULT 1 COMMENT '状态(0-禁用 1-启用)',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (category_id),
  UNIQUE KEY uk_category_code (category_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报表分类';

-- 报表模板表
CREATE TABLE IF NOT EXISTS report_template (
  template_id bigint NOT NULL AUTO_INCREMENT,
  template_name varchar(100) NOT NULL COMMENT '模板名称',
  template_code varchar(50) NOT NULL COMMENT '模板编码',
  category_id bigint NOT NULL COMMENT '分类ID',
  description varchar(500) DEFAULT NULL COMMENT '描述',
  thumbnail varchar(255) DEFAULT NULL COMMENT '缩略图',
  config_json text COMMENT '配置JSON',
  data_source_type varchar(20) DEFAULT 'SQL' COMMENT '数据源类型',
  data_source_config text COMMENT '数据源配置',
  status tinyint DEFAULT 1 COMMENT '状态(0-禁用 1-启用)',
  is_system tinyint DEFAULT 0 COMMENT '是否系统预设',
  create_user_id bigint DEFAULT NULL COMMENT '创建人',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (template_id),
  UNIQUE KEY uk_template_code (template_code),
  KEY idx_category_id (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报表模板';

-- 报表维度表
CREATE TABLE IF NOT EXISTS report_dimension (
  dimension_id bigint NOT NULL AUTO_INCREMENT,
  dimension_name varchar(50) NOT NULL COMMENT '维度名称',
  dimension_code varchar(50) NOT NULL COMMENT '维度编码',
  dimension_type varchar(20) NOT NULL COMMENT '维度类型(TIME-时间,ORG-部门,PROJECT-项目,PRODUCT-产品,CUSTOM-自定义)',
  expression varchar(200) DEFAULT NULL COMMENT '表达式',
  data_type varchar(20) DEFAULT 'STRING' COMMENT '数据类型',
  sortable tinyint DEFAULT 1 COMMENT '是否可排序',
  filterable tinyint DEFAULT 1 COMMENT '是否可过滤',
  status tinyint DEFAULT 1 COMMENT '状态(0-禁用 1-启用)',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (dimension_id),
  UNIQUE KEY uk_dimension_code (dimension_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报表维度';

-- 报表指标表
CREATE TABLE IF NOT EXISTS report_metric (
  metric_id bigint NOT NULL AUTO_INCREMENT,
  metric_name varchar(50) NOT NULL COMMENT '指标名称',
  metric_code varchar(50) NOT NULL COMMENT '指标编码',
  expression varchar(200) NOT NULL COMMENT '计算表达式',
  data_type varchar(20) DEFAULT 'NUMBER' COMMENT '数据类型',
  format varchar(50) DEFAULT NULL COMMENT '显示格式',
  aggregator varchar(20) DEFAULT 'SUM' COMMENT '聚合方式',
  status tinyint DEFAULT 1 COMMENT '状态(0-禁用 1-启用)',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (metric_id),
  UNIQUE KEY uk_metric_code (metric_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报表指标';

-- 报表实例表
CREATE TABLE IF NOT EXISTS report_instance (
  instance_id bigint NOT NULL AUTO_INCREMENT,
  template_id bigint NOT NULL COMMENT '模板ID',
  instance_name varchar(100) NOT NULL COMMENT '实例名称',
  config_json text COMMENT '配置JSON',
  filter_condition text COMMENT '过滤条件',
  status tinyint DEFAULT 1 COMMENT '状态(0-禁用 1-启用)',
  create_user_id bigint DEFAULT NULL COMMENT '创建人',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (instance_id),
  KEY idx_template_id (template_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报表实例';

-- 报表权限表
CREATE TABLE IF NOT EXISTS report_permission (
  permission_id bigint NOT NULL AUTO_INCREMENT,
  instance_id bigint NOT NULL COMMENT '实例ID',
  role_id bigint NOT NULL COMMENT '角色ID',
  permission_type varchar(20) DEFAULT 'VIEW' COMMENT '权限类型(VIEW-查看,EDIT-编辑,MANAGE-管理)',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (permission_id),
  UNIQUE KEY uk_instance_role (instance_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报表权限';

-- 初始化预置分类
INSERT INTO report_category (category_name, category_code, sort_order, icon) VALUES 
('经营分析', 'business', 1, 'AreaChartOutlined'),
('财务分析', 'finance', 2, 'PieChartOutlined'),
('生产分析', 'production', 3, 'BarChartOutlined'),
('人力资源', 'hr', 4, 'RadarChartOutlined');

-- 初始化常用维度
INSERT INTO report_dimension (dimension_name, dimension_code, dimension_type, data_type) VALUES
('时间', 'time', 'TIME', 'DATE'),
('年', 'year', 'TIME', 'NUMBER'),
('季度', 'quarter', 'TIME', 'STRING'), 
('月', 'month', 'TIME', 'STRING'),
('部门', 'department', 'ORG', 'STRING'),
('项目', 'project', 'PROJECT', 'STRING'),
('产品', 'product', 'PRODUCT', 'STRING');

-- 初始化常用指标
INSERT INTO report_metric (metric_name, metric_code, expression, data_type, aggregator) VALUES
('销售额', 'sales_amount', 'amount', 'NUMBER', 'SUM'),
('销售数量', 'sales_quantity', 'quantity', 'NUMBER', 'SUM'),
('平均单价', 'avg_price', 'amount/quantity', 'NUMBER', 'AVG'),
('利润率', 'profit_rate', 'profit/amount', 'NUMBER', 'AVG');