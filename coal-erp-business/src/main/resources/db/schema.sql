-- 智慧煤矿ERP管理系统数据库表结构

-- 部门表
CREATE TABLE IF NOT EXISTS `sys_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `dept_name` varchar(50) NOT NULL COMMENT '部门名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父部门ID',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(50) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 用户表
CREATE TABLE IF NOT EXISTS `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '昵称',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `sex` char(1) DEFAULT NULL COMMENT '性别（0男 1女）',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `role_key` varchar(50) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(4) DEFAULT '0' COMMENT '显示顺序',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 菜单表
CREATE TABLE IF NOT EXISTS `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT NULL COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `menu_type` char(1) DEFAULT NULL COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT NULL COMMENT '菜单图标',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 角色菜单关联表
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 设备资产表
CREATE TABLE IF NOT EXISTS `asset` (
  `asset_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资产ID',
  `asset_code` varchar(50) NOT NULL COMMENT '资产编码',
  `asset_name` varchar(100) NOT NULL COMMENT '资产名称',
  `asset_type` varchar(50) DEFAULT NULL COMMENT '资产类型',
  `category` varchar(50) DEFAULT NULL COMMENT '资产分类',
  `manufacturer` varchar(100) DEFAULT NULL COMMENT '制造商',
  `model` varchar(100) DEFAULT NULL COMMENT '型号',
  `serial_number` varchar(100) DEFAULT NULL COMMENT '序列号',
  `purchase_date` date DEFAULT NULL COMMENT '采购日期',
  `purchase_price` decimal(10,2) DEFAULT NULL COMMENT '采购价格',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1维修中 2报废）',
  `location` varchar(200) DEFAULT NULL COMMENT '位置',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '所属部门',
  `is_explosion_proof` char(1) DEFAULT '0' COMMENT '是否防爆设备（0否 1是）',
  `explosion_proof_expire_date` date DEFAULT NULL COMMENT '防爆证书到期日期',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`asset_id`),
  UNIQUE KEY `uk_asset_code` (`asset_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备资产表';

-- 采购订单表
CREATE TABLE IF NOT EXISTS `purchase_order` (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单编号',
  `supplier` varchar(100) DEFAULT NULL COMMENT '供应商',
  `order_type` varchar(50) DEFAULT NULL COMMENT '订单类型',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '订单总额',
  `status` varchar(20) DEFAULT NULL COMMENT '订单状态',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购订单表';

-- 维修工单表
CREATE TABLE IF NOT EXISTS `repair_order` (
  `repair_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '维修ID',
  `repair_no` varchar(50) NOT NULL COMMENT '工单编号',
  `asset_id` bigint(20) NOT NULL COMMENT '资产ID',
  `fault_description` varchar(500) DEFAULT NULL COMMENT '故障描述',
  `repair_type` varchar(50) DEFAULT NULL COMMENT '维修类型',
  `repair_cost` decimal(10,2) DEFAULT NULL COMMENT '维修费用',
  `status` varchar(20) DEFAULT NULL COMMENT '状态',
  `repair_user_id` bigint(20) DEFAULT NULL COMMENT '维修人ID',
  `repair_start_time` datetime DEFAULT NULL COMMENT '维修开始时间',
  `repair_end_time` datetime DEFAULT NULL COMMENT '维修结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`repair_id`),
  UNIQUE KEY `uk_repair_no` (`repair_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修工单表';

-- 库存表
CREATE TABLE IF NOT EXISTS `inventory` (
  `inventory_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '库存ID',
  `material_code` varchar(50) NOT NULL COMMENT '物料编码',
  `material_name` varchar(100) NOT NULL COMMENT '物料名称',
  `material_type` varchar(50) DEFAULT NULL COMMENT '物料类型',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `quantity` decimal(10,2) DEFAULT '0.00' COMMENT '数量',
  `min_stock` decimal(10,2) DEFAULT NULL COMMENT '最低库存',
  `max_stock` decimal(10,2) DEFAULT NULL COMMENT '最高库存',
  `warehouse` varchar(100) DEFAULT NULL COMMENT '仓库',
  `location` varchar(200) DEFAULT NULL COMMENT '位置',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`inventory_id`),
  UNIQUE KEY `uk_material_code` (`material_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';

-- 盘点表
CREATE TABLE IF NOT EXISTS `stocktaking` (
  `stocktaking_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '盘点ID',
  `stocktaking_no` varchar(50) NOT NULL COMMENT '盘点单号',
  `warehouse` varchar(100) DEFAULT NULL COMMENT '仓库',
  `stocktaking_date` date DEFAULT NULL COMMENT '盘点日期',
  `status` varchar(20) DEFAULT NULL COMMENT '状态',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`stocktaking_id`),
  UNIQUE KEY `uk_stocktaking_no` (`stocktaking_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='盘点表';

-- 预警记录表
CREATE TABLE IF NOT EXISTS `warning_alert` (
  `alert_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '预警ID',
  `alert_type` varchar(50) DEFAULT NULL COMMENT '预警类型（EXPLOSION_PROOF-防爆设备）',
  `alert_level` varchar(20) DEFAULT NULL COMMENT '预警级别（YELLOW-黄色，ORANGE-橙色，RED-红色）',
  `asset_id` bigint(20) DEFAULT NULL COMMENT '资产ID',
  `asset_code` varchar(50) DEFAULT NULL COMMENT '资产编码',
  `asset_name` varchar(100) DEFAULT NULL COMMENT '资产名称',
  `alert_title` varchar(200) DEFAULT NULL COMMENT '预警标题',
  `alert_content` varchar(500) DEFAULT NULL COMMENT '预警内容',
  `expire_date` date DEFAULT NULL COMMENT '到期日期',
  `days_remaining` int(11) DEFAULT NULL COMMENT '剩余天数',
  `status` char(1) DEFAULT '0' COMMENT '状态（0-未处理，1-已处理）',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`alert_id`),
  KEY `idx_asset_id` (`asset_id`),
  KEY `idx_alert_level` (`alert_level`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警记录表';

-- 初始化数据
-- 默认管理员用户（密码：admin123）
INSERT INTO `sys_user` (`username`, `password`, `nick_name`, `status`, `create_time`) 
VALUES ('admin', '$2a$10$7JB720yubVSOfvVaMWdK0u5u8vKqJqJqJqJqJqJqJqJqJqJqJqJqJq', '管理员', '0', NOW())
ON DUPLICATE KEY UPDATE `username`=`username`;

-- 默认角色
INSERT INTO `sys_role` (`role_name`, `role_key`, `role_sort`, `status`, `create_time`) 
VALUES ('超级管理员', 'admin', 1, '0', NOW())
ON DUPLICATE KEY UPDATE `role_name`=`role_name`;