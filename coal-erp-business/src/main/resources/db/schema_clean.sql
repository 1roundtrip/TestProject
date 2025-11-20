-- 智慧煤矿ERP管理系统数据库表结构

CREATE TABLE IF NOT EXISTS sys_dept (
  dept_id bigint NOT NULL AUTO_INCREMENT,
  dept_name varchar(50) NOT NULL,
  parent_id bigint DEFAULT 0,
  order_num int DEFAULT 0,
  leader varchar(50) DEFAULT NULL,
  phone varchar(20) DEFAULT NULL,
  email varchar(50) DEFAULT NULL,
  status char(1) DEFAULT '0',
  create_time datetime DEFAULT NULL,
  update_time datetime DEFAULT NULL,
  PRIMARY KEY (dept_id)
);

CREATE TABLE IF NOT EXISTS sys_user (
  user_id bigint NOT NULL AUTO_INCREMENT,
  username varchar(50) NOT NULL,
  password varchar(100) NOT NULL,
  nick_name varchar(50) DEFAULT NULL,
  email varchar(50) DEFAULT NULL,
  phone varchar(20) DEFAULT NULL,
  sex char(1) DEFAULT NULL,
  avatar varchar(200) DEFAULT NULL,
  status char(1) DEFAULT '0',
  dept_id bigint DEFAULT NULL,
  create_time datetime DEFAULT NULL,
  update_time datetime DEFAULT NULL,
  remark varchar(500) DEFAULT NULL,
  PRIMARY KEY (user_id),
  UNIQUE KEY uk_username (username)
);

CREATE TABLE IF NOT EXISTS sys_role (
  role_id bigint NOT NULL AUTO_INCREMENT,
  role_name varchar(50) NOT NULL,
  role_key varchar(50) NOT NULL,
  role_sort int DEFAULT 0,
  status char(1) DEFAULT '0',
  create_time datetime DEFAULT NULL,
  update_time datetime DEFAULT NULL,
  remark varchar(500) DEFAULT NULL,
  PRIMARY KEY (role_id)
);

CREATE TABLE IF NOT EXISTS sys_menu (
  menu_id bigint NOT NULL AUTO_INCREMENT,
  menu_name varchar(50) NOT NULL,
  parent_id bigint DEFAULT 0,
  order_num int DEFAULT 0,
  path varchar(200) DEFAULT NULL,
  component varchar(255) DEFAULT NULL,
  menu_type char(1) DEFAULT NULL,
  perms varchar(100) DEFAULT NULL,
  icon varchar(100) DEFAULT NULL,
  status char(1) DEFAULT '0',
  create_time datetime DEFAULT NULL,
  update_time datetime DEFAULT NULL,
  remark varchar(500) DEFAULT NULL,
  PRIMARY KEY (menu_id)
);

CREATE TABLE IF NOT EXISTS sys_user_role (
  user_id bigint NOT NULL,
  role_id bigint NOT NULL,
  PRIMARY KEY (user_id,role_id)
);

CREATE TABLE IF NOT EXISTS sys_role_menu (
  role_id bigint NOT NULL,
  menu_id bigint NOT NULL,
  PRIMARY KEY (role_id,menu_id)
);

CREATE TABLE IF NOT EXISTS asset (
  asset_id bigint NOT NULL AUTO_INCREMENT,
  asset_code varchar(50) NOT NULL,
  asset_name varchar(100) NOT NULL,
  asset_type varchar(50) DEFAULT NULL,
  category varchar(50) DEFAULT NULL,
  manufacturer varchar(100) DEFAULT NULL,
  model varchar(100) DEFAULT NULL,
  serial_number varchar(100) DEFAULT NULL,
  purchase_date date DEFAULT NULL,
  purchase_price decimal(10,2) DEFAULT NULL,
  status char(1) DEFAULT '0',
  location varchar(200) DEFAULT NULL,
  dept_id bigint DEFAULT NULL,
  is_explosion_proof char(1) DEFAULT '0',
  explosion_proof_expire_date date DEFAULT NULL,
  create_time datetime DEFAULT NULL,
  update_time datetime DEFAULT NULL,
  remark varchar(500) DEFAULT NULL,
  PRIMARY KEY (asset_id),
  UNIQUE KEY uk_asset_code (asset_code)
);

CREATE TABLE IF NOT EXISTS purchase_order (
  order_id bigint NOT NULL AUTO_INCREMENT,
  order_no varchar(50) NOT NULL,
  supplier varchar(100) DEFAULT NULL,
  order_type varchar(50) DEFAULT NULL,
  total_amount decimal(10,2) DEFAULT NULL,
  status varchar(20) DEFAULT NULL,
  create_user_id bigint DEFAULT NULL,
  create_time datetime DEFAULT NULL,
  update_time datetime DEFAULT NULL,
  remark varchar(500) DEFAULT NULL,
  PRIMARY KEY (order_id),
  UNIQUE KEY uk_order_no (order_no)
);

CREATE TABLE IF NOT EXISTS repair_order (
  repair_id bigint NOT NULL AUTO_INCREMENT,
  repair_no varchar(50) NOT NULL,
  asset_id bigint NOT NULL,
  fault_description varchar(500) DEFAULT NULL,
  repair_type varchar(50) DEFAULT NULL,
  repair_cost decimal(10,2) DEFAULT NULL,
  status varchar(20) DEFAULT NULL,
  repair_user_id bigint DEFAULT NULL,
  repair_start_time datetime DEFAULT NULL,
  repair_end_time datetime DEFAULT NULL,
  create_time datetime DEFAULT NULL,
  update_time datetime DEFAULT NULL,
  remark varchar(500) DEFAULT NULL,
  PRIMARY KEY (repair_id),
  UNIQUE KEY uk_repair_no (repair_no)
);

CREATE TABLE IF NOT EXISTS inventory (
  inventory_id bigint NOT NULL AUTO_INCREMENT,
  material_code varchar(50) NOT NULL,
  material_name varchar(100) NOT NULL,
  material_type varchar(50) DEFAULT NULL,
  unit varchar(20) DEFAULT NULL,
  quantity decimal(10,2) DEFAULT '0.00',
  min_stock decimal(10,2) DEFAULT NULL,
  max_stock decimal(10,2) DEFAULT NULL,
  warehouse varchar(100) DEFAULT NULL,
  location varchar(200) DEFAULT NULL,
  create_time datetime DEFAULT NULL,
  update_time datetime DEFAULT NULL,
  remark varchar(500) DEFAULT NULL,
  PRIMARY KEY (inventory_id),
  UNIQUE KEY uk_material_code (material_code)
);

CREATE TABLE IF NOT EXISTS stocktaking (
  stocktaking_id bigint NOT NULL AUTO_INCREMENT,
  stocktaking_no varchar(50) NOT NULL,
  warehouse varchar(100) DEFAULT NULL,
  stocktaking_date date DEFAULT NULL,
  status varchar(20) DEFAULT NULL,
  create_user_id bigint DEFAULT NULL,
  create_time datetime DEFAULT NULL,
  update_time datetime DEFAULT NULL,
  remark varchar(500) DEFAULT NULL,
  PRIMARY KEY (stocktaking_id),
  UNIQUE KEY uk_stocktaking_no (stocktaking_no)
);

CREATE TABLE IF NOT EXISTS warning_alert (
  alert_id bigint NOT NULL AUTO_INCREMENT,
  alert_type varchar(50) DEFAULT NULL,
  alert_level varchar(20) DEFAULT NULL,
  asset_id bigint DEFAULT NULL,
  asset_code varchar(50) DEFAULT NULL,
  asset_name varchar(100) DEFAULT NULL,
  alert_title varchar(200) DEFAULT NULL,
  alert_content varchar(500) DEFAULT NULL,
  expire_date date DEFAULT NULL,
  days_remaining int DEFAULT NULL,
  status char(1) DEFAULT '0',
  create_user_id bigint DEFAULT NULL,
  create_time datetime DEFAULT NULL,
  update_time datetime DEFAULT NULL,
  remark varchar(500) DEFAULT NULL,
  PRIMARY KEY (alert_id),
  KEY idx_asset_id (asset_id),
  KEY idx_alert_level (alert_level),
  KEY idx_status (status),
  KEY idx_create_time (create_time)
);