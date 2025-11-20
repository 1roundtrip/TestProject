-- ============================================
-- 智慧煤矿ERP管理系统 - 完整测试数据
-- ============================================
-- 说明：本文件包含各模块的完整测试数据，数据之间相互关联
-- 使用前请先清空各模块的业务数据（保留系统基础数据）
-- ============================================
-- 
-- ⚠️ 重要提示：
-- 1. 请使用文件导入方式执行，不要复制粘贴SQL语句
-- 2. 推荐使用：mysql -u root -p coal_erp < complete_test_data.sql
-- 3. 或使用提供的导入脚本：导入测试数据.bat 或 导入测试数据.sh
-- 4. 如果复制粘贴，可能会因为命令行长度限制导致SQL被截断
-- ============================================

USE coal_erp;

-- ============================================
-- 第一部分：清空业务数据（保留系统基础数据）
-- ============================================
-- 注意：如果表不存在，TRUNCATE会报错，请确保已执行表结构创建脚本

-- 关闭外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- 清空预警模块数据
TRUNCATE TABLE warning_record;
TRUNCATE TABLE warning_rule;
TRUNCATE TABLE warning_notification;
TRUNCATE TABLE warning_handle_record;

-- 清空维修管理模块数据
TRUNCATE TABLE maintenance_work_order_detail;
TRUNCATE TABLE maintenance_work_order;
TRUNCATE TABLE maintenance_plan_execution;
TRUNCATE TABLE maintenance_plan;
TRUNCATE TABLE maintenance_team_member;
TRUNCATE TABLE maintenance_team;
TRUNCATE TABLE maintenance_part_requisition_detail;
TRUNCATE TABLE maintenance_part_requisition;
TRUNCATE TABLE maintenance_quality_check;
TRUNCATE TABLE maintenance_cost;
TRUNCATE TABLE maintenance_fault_record;
TRUNCATE TABLE maintenance_performance;
TRUNCATE TABLE maintenance_statistics;
TRUNCATE TABLE maintenance_mobile_checkin;

-- 清空库存模块数据
TRUNCATE TABLE inventory_stocktaking_detail;
TRUNCATE TABLE inventory_stocktaking;
TRUNCATE TABLE inventory_adjustment_detail;
TRUNCATE TABLE inventory_adjustment;
TRUNCATE TABLE inventory_transfer_detail;
TRUNCATE TABLE inventory_transfer;
TRUNCATE TABLE inventory_outbound_detail;
TRUNCATE TABLE inventory_outbound;
TRUNCATE TABLE inventory_inbound_detail;
TRUNCATE TABLE inventory_inbound;
TRUNCATE TABLE inventory_stock;
TRUNCATE TABLE inventory_material;
TRUNCATE TABLE inventory_location;
TRUNCATE TABLE inventory_warning;
TRUNCATE TABLE inventory_statistics;

-- 清空采购模块数据
TRUNCATE TABLE purchase_receiving_detail;
TRUNCATE TABLE purchase_receiving;
TRUNCATE TABLE purchase_quality_check;
TRUNCATE TABLE purchase_order_detail;
TRUNCATE TABLE purchase_order;
TRUNCATE TABLE purchase_requisition_detail;
TRUNCATE TABLE purchase_requisition;
TRUNCATE TABLE purchase_contract;
TRUNCATE TABLE purchase_payment;
TRUNCATE TABLE purchase_return;
TRUNCATE TABLE purchase_supplier_evaluation;
TRUNCATE TABLE purchase_supplier_product;
TRUNCATE TABLE purchase_supplier;

-- 清空资产模块数据（保留基础表结构）
TRUNCATE TABLE asset;

-- 恢复外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 第二部分：基础数据准备
-- ============================================

-- 确保有部门数据（如果已存在则跳过）
INSERT INTO sys_dept (dept_id, dept_name, parent_id, order_num, leader, phone, email, status, create_time, update_time) VALUES
(1, '智慧煤矿集团', 0, 1, '张总', '010-88888888', 'zhang@coal.com', '0', NOW(), NOW()),
(2, '生产部', 1, 1, '李经理', '010-88888801', 'li@coal.com', '0', NOW(), NOW()),
(3, '安全部', 1, 2, '王经理', '010-88888802', 'wang@coal.com', '0', NOW(), NOW()),
(4, '财务部', 1, 3, '赵经理', '010-88888803', 'zhao@coal.com', '0', NOW(), NOW()),
(5, '采购部', 1, 4, '钱经理', '010-88888804', 'qian@coal.com', '0', NOW(), NOW()),
(6, '维修部', 1, 5, '孙经理', '010-88888805', 'sun@coal.com', '0', NOW(), NOW()),
(7, '库存部', 1, 6, '周经理', '010-88888806', 'zhou@coal.com', '0', NOW(), NOW()),
(8, '采煤一队', 2, 1, '吴队长', '010-88888811', 'wu@coal.com', '0', NOW(), NOW()),
(9, '采煤二队', 2, 2, '郑队长', '010-88888812', 'zheng@coal.com', '0', NOW(), NOW()),
(10, '掘进队', 2, 3, '冯队长', '010-88888813', 'feng@coal.com', '0', NOW(), NOW())
ON DUPLICATE KEY UPDATE dept_name=VALUES(dept_name);

-- ============================================
-- 第三部分：供应商数据
-- ============================================

INSERT INTO purchase_supplier (supplier_code, supplier_name, supplier_type, credit_level, cooperation_years, 
                                business_license, tax_number, legal_person, registered_capital,
                                contact_person, contact_phone, contact_email, address,
                                bank_name, bank_account, account_name, payment_terms, delivery_terms,
                                quality_rating, service_rating, price_rating, total_rating, status,
                                create_user_id, create_time, update_time, remark) VALUES
('GYS001', '山东防爆电机厂', 'MAIN', 'AAA', 5, '91370000123456789X', '91370000123456789X', '张总', 50000000.00,
 '李经理', '0531-88888888', 'li@sdmotor.com', '山东省济南市历下区工业路100号',
 '中国工商银行济南分行', '6222021234567890123', '山东防爆电机厂', '月结30天', '送货上门',
 9.5, 9.0, 8.5, 9.0, 'ACTIVE', 1, NOW(), NOW(), '主要供应商，长期合作'),
('GYS002', '三一重工股份有限公司', 'STRATEGIC', 'AAA', 8, '91430000987654321Y', '91430000987654321Y', '王总', 2000000000.00,
 '赵经理', '0731-66666666', 'zhao@sany.com', '湖南省长沙市经济技术开发区',
 '中国建设银行长沙分行', '6222029876543210987', '三一重工股份有限公司', '月结45天', '物流配送',
 9.8, 9.5, 8.8, 9.4, 'ACTIVE', 1, NOW(), NOW(), '战略供应商，设备质量优秀'),
('GYS003', '中煤装备制造有限公司', 'MAIN', 'AA', 6, '91110000111222333Z', '91110000111222333Z', '李总', 800000000.00,
 '钱经理', '010-55555555', 'qian@zmzb.com', '北京市朝阳区建国路88号',
 '中国银行北京分行', '6222021112223334445', '中煤装备制造有限公司', '月结30天', '自提或物流',
 9.0, 8.8, 9.0, 8.9, 'ACTIVE', 1, NOW(), NOW(), '主要供应商，价格优势明显'),
('GYS004', '郑煤机集团股份有限公司', 'MAIN', 'AAA', 7, '91410000222333444A', '91410000222333444A', '周总', 1500000000.00,
 '孙经理', '0371-77777777', 'sun@zmj.com', '河南省郑州市经济技术开发区',
 '中国工商银行郑州分行', '6222022223334445556', '郑煤机集团股份有限公司', '月结30天', '物流配送',
 9.6, 9.2, 8.7, 9.2, 'ACTIVE', 1, NOW(), NOW(), '主要供应商，产品质量稳定'),
('GYS005', '天地科技股份有限公司', 'MAIN', 'AA', 4, '91100000333444555B', '91100000333444555B', '吴总', 600000000.00,
 '郑经理', '010-44444444', 'zheng@tdkj.com', '北京市海淀区学院路30号',
 '中国建设银行北京分行', '6222023334445556667', '天地科技股份有限公司', '月结30天', '送货上门',
 9.2, 9.0, 8.9, 9.0, 'ACTIVE', 1, NOW(), NOW(), '主要供应商，技术服务好'),
('GYS006', '海康威视数字技术股份有限公司', 'AUXILIARY', 'AAA', 3, '91330000444555666C', '91330000444555666C', '冯总', 5000000000.00,
 '陈经理', '0571-33333333', 'chen@hikvision.com', '浙江省杭州市滨江区',
 '中国工商银行杭州分行', '6222024445556667778', '海康威视数字技术股份有限公司', '现款现货', '快递配送',
 9.8, 9.5, 9.0, 9.4, 'ACTIVE', 1, NOW(), NOW(), '辅助供应商，产品性价比高'),
('GYS007', '飞利浦照明（中国）投资有限公司', 'AUXILIARY', 'AA', 2, '91310000555666777D', '91310000555666777D', '褚总', 3000000000.00,
 '卫经理', '021-22222222', 'wei@philips.com', '上海市浦东新区张江高科技园区',
 '中国银行上海分行', '6222025556667778889', '飞利浦照明（中国）投资有限公司', '月结30天', '物流配送',
 9.5, 9.3, 8.8, 9.2, 'ACTIVE', 1, NOW(), NOW(), '辅助供应商，品牌知名度高'),
('GYS008', '上海凯泉泵业（集团）有限公司', 'MAIN', 'AA', 5, '91310000666777888E', '91310000666777888E', '蒋总', 1000000000.00,
 '沈经理', '021-11111111', 'shen@kaiquan.com', '上海市嘉定区曹安公路4255号',
 '中国建设银行上海分行', '6222026667778889990', '上海凯泉泵业（集团）有限公司', '月结30天', '物流配送',
 9.0, 8.8, 9.2, 9.0, 'ACTIVE', 1, NOW(), NOW(), '主要供应商，产品性能稳定');

-- 供应商产品目录
INSERT INTO purchase_supplier_product (supplier_id, product_name, product_code, specification, brand, unit, 
                                        unit_price, currency, min_order_quantity, delivery_days, warranty_period, status, create_time) VALUES
(1, '防爆电机', 'YBK2-280M-4', 'YBK2-280M-4 75KW', '山东防爆', '台', 15000.00, 'CNY', 1.00, 15, 12, 'ACTIVE', NOW()),
(1, '防爆开关', 'BXM-100', 'BXM-100 100A', '山东防爆', '台', 5000.00, 'CNY', 1.00, 10, 12, 'ACTIVE', NOW()),
(2, '采煤机', 'MG300/700-WD', 'MG300/700-WD 700KW', '三一重工', '台', 2800000.00, 'CNY', 1.00, 90, 24, 'ACTIVE', NOW()),
(3, '刮板输送机', 'SGZ764/630', 'SGZ764/630 630KW', '中煤装备', '台', 1200000.00, 'CNY', 1.00, 60, 18, 'ACTIVE', NOW()),
(4, '液压支架', 'ZY6800/14/32', 'ZY6800/14/32', '郑煤机', '架', 450000.00, 'CNY', 10.00, 45, 18, 'ACTIVE', NOW()),
(5, '瓦斯监测系统', 'KJ90', 'KJ90 多参数监测', '天地科技', '套', 350000.00, 'CNY', 1.00, 30, 24, 'ACTIVE', NOW()),
(6, '防爆摄像头', 'DS-2CD7A47HWD', 'DS-2CD7A47HWD 200万像素', '海康威视', '台', 3500.00, 'CNY', 5.00, 7, 12, 'ACTIVE', NOW()),
(7, '防爆照明灯', 'BLC-100', 'BLC-100 100W LED', '飞利浦', '盏', 800.00, 'CNY', 10.00, 5, 12, 'ACTIVE', NOW()),
(8, '排水泵', 'MD280-65×6', 'MD280-65×6 280m³/h', '上海凯泉', '台', 85000.00, 'CNY', 1.00, 20, 18, 'ACTIVE', NOW());

-- ============================================
-- 第四部分：资产数据
-- ============================================

INSERT INTO asset (asset_code, asset_name, asset_type, category, manufacturer, model, serial_number,
                   purchase_date, purchase_price, status, location, dept_id, is_explosion_proof,
                   explosion_proof_expire_date, create_time, update_time, remark) VALUES
('ZC001', '防爆电机', '电气设备', '电机', '山东防爆电机厂', 'YBK2-280M-4', 'SN2024001',
 '2024-01-15', 15000.00, '0', '主井口车间', 8, '1', '2025-12-31', NOW(), NOW(), '主提升系统配套设备'),
('ZC002', '防爆开关', '电气设备', '开关', '山东防爆电机厂', 'BXM-100', 'SN2024002',
 '2024-02-20', 5000.00, '0', '井下配电室', 8, '1', '2025-11-30', NOW(), NOW(), '井下供电系统'),
('ZC003', '采煤机', '机械设备', '采掘设备', '三一重工', 'MG300/700-WD', 'SN2024003',
 '2023-06-10', 2800000.00, '0', '采煤工作面', 8, '1', '2026-06-10', NOW(), NOW(), '综采工作面主要设备'),
('ZC004', '刮板输送机', '机械设备', '运输设备', '中煤装备', 'SGZ764/630', 'SN2024004',
 '2023-07-15', 1200000.00, '0', '采煤工作面', 8, '1', '2026-07-15', NOW(), NOW(), '煤炭运输设备'),
('ZC005', '液压支架', '机械设备', '支护设备', '郑煤机', 'ZY6800/14/32', 'SN2024005',
 '2023-08-20', 450000.00, '0', '采煤工作面', 8, '1', '2026-08-20', NOW(), NOW(), '工作面支护设备，共120架'),
('ZC006', '掘进机', '机械设备', '采掘设备', '北方重工', 'EBZ160', 'SN2024006',
 '2023-09-25', 1800000.00, '0', '掘进工作面', 10, '1', '2026-09-25', NOW(), NOW(), '巷道掘进设备'),
('ZC007', '瓦斯监测系统', '监控设备', '安全监控', '天地科技', 'KJ90', 'SN2024007',
 '2024-03-10', 350000.00, '0', '井下各监测点', 3, '1', '2025-03-10', NOW(), NOW(), '瓦斯浓度实时监测'),
('ZC008', '人员定位系统', '监控设备', '安全监控', '中煤科工', 'KJ251', 'SN2024008',
 '2024-03-15', 280000.00, '0', '井下各区域', 3, '1', '2025-03-15', NOW(), NOW(), '井下人员实时定位'),
('ZC009', '防爆摄像头', '监控设备', '视频监控', '海康威视', 'DS-2CD7A47HWD', 'SN2024009',
 '2024-04-01', 3500.00, '0', '主井口', 3, '1', '2025-04-01', NOW(), NOW(), '井口安全监控'),
('ZC010', '防爆照明灯', '电气设备', '照明设备', '飞利浦', 'BLC-100', 'SN2024010',
 '2024-04-10', 800.00, '0', '井下各巷道', 8, '1', '2025-04-10', NOW(), NOW(), '井下照明设备，共200盏'),
('ZC011', '通风机', '机械设备', '通风设备', '沈鼓集团', 'FBCDZ-6-No.18', 'SN2024011',
 '2023-10-15', 850000.00, '0', '风井口', 2, '0', NULL, NOW(), NOW(), '主通风系统'),
('ZC012', '压风机', '机械设备', '压风设备', '开山集团', 'LG-75/8', 'SN2024012',
 '2023-11-20', 420000.00, '0', '压风机房', 2, '0', NULL, NOW(), NOW(), '井下压风系统'),
('ZC013', '提升机', '机械设备', '提升设备', '中信重工', 'JKMD-3.5×4', 'SN2024013',
 '2023-12-05', 3200000.00, '0', '主井口', 2, '0', NULL, NOW(), NOW(), '主提升系统'),
('ZC014', '排水泵', '机械设备', '排水设备', '上海凯泉', 'MD280-65×6', 'SN2024014',
 '2024-01-20', 85000.00, '0', '井下泵房', 2, '1', '2025-01-20', NOW(), NOW(), '井下排水系统'),
('ZC015', '防爆电话', '通信设备', '通信设备', '华为', 'KTW125', 'SN2024015',
 '2024-02-10', 1200.00, '0', '井下各区域', 8, '1', '2025-02-10', NOW(), NOW(), '井下通信设备，共50部');

-- ============================================
-- 第五部分：采购申请数据
-- ============================================

INSERT INTO purchase_requisition (requisition_no, requisition_name, dept_id, dept_name, applicant_id, applicant_name,
                                   total_amount, urgent_level, purpose, status, create_time, update_time, remark) VALUES
('CG202412001', '防爆电机采购申请', 5, '采购部', 1, '采购员', 150000.00, 'NORMAL', '主提升系统设备更新', 'APPROVED', 
 '2024-12-01 09:00:00', NOW(), '已审批通过'),
('CG202412002', '采煤机配件采购申请', 5, '采购部', 1, '采购员', 280000.00, 'URGENT', '采煤机维修配件', 'APPROVED',
 '2024-12-05 10:30:00', NOW(), '紧急采购，已审批'),
('CG202412003', '刮板输送机采购申请', 5, '采购部', 1, '采购员', 1200000.00, 'NORMAL', '新工作面设备采购', 'APPROVED',
 '2024-12-10 14:20:00', NOW(), '已审批通过'),
('CG202412004', '液压支架采购申请', 5, '采购部', 1, '采购员', 5400000.00, 'NORMAL', '新工作面支护设备', 'APPROVED',
 '2024-12-15 11:00:00', NOW(), '已审批通过'),
('CG202412005', '瓦斯监测系统采购申请', 5, '采购部', 1, '采购员', 350000.00, 'URGENT', '安全监控设备更新', 'APPROVED',
 '2024-12-20 15:30:00', NOW(), '安全设备，已审批');

-- 采购申请明细
INSERT INTO purchase_requisition_detail (requisition_id, item_name, item_code, specification, brand, unit,
                                          quantity, estimated_price, estimated_amount, required_date, purpose, remark) VALUES
(1, '防爆电机', 'YBK2-280M-4', 'YBK2-280M-4 75KW', '山东防爆', '台', 10.00, 15000.00, 150000.00, '2024-12-20', '主提升系统更新', NULL),
(2, '采煤机截割部', 'MG300-CUT', 'MG300/700-WD截割部', '三一重工', '套', 1.00, 280000.00, 280000.00, '2024-12-25', '维修配件', NULL),
(3, '刮板输送机', 'SGZ764/630', 'SGZ764/630 630KW', '中煤装备', '台', 1.00, 1200000.00, 1200000.00, '2025-01-15', '新工作面设备', NULL),
(4, '液压支架', 'ZY6800/14/32', 'ZY6800/14/32', '郑煤机', '架', 120.00, 45000.00, 5400000.00, '2025-01-20', '新工作面支护', NULL),
(5, '瓦斯监测系统', 'KJ90', 'KJ90 多参数监测', '天地科技', '套', 1.00, 350000.00, 350000.00, '2024-12-30', '安全监控更新', NULL);

-- ============================================
-- 第六部分：采购订单数据
-- ============================================

INSERT INTO purchase_order (order_no, requisition_id, requisition_no, supplier_id, supplier_name, supplier_code,
                             order_type, order_date, delivery_date, delivery_address, delivery_method, payment_terms,
                             currency, total_amount, tax_amount, total_amount_with_tax, status,
                             buyer_id, buyer_name, create_user_id, create_user_name, create_time, update_time, remark) VALUES
('CGDD202412001', 1, 'CG202412001', 1, '山东防爆电机厂', 'GYS001',
 'NORMAL', '2024-12-02', '2024-12-20', '智慧煤矿集团主仓库', 'LOGISTICS', '月结30天',
 'CNY', 150000.00, 19500.00, 169500.00, 'RECEIVED',
 1, '采购员', 1, '采购员', '2024-12-02 10:00:00', NOW(), '已收货'),
('CGDD202412002', 2, 'CG202412002', 2, '三一重工股份有限公司', 'GYS002',
 'URGENT', '2024-12-06', '2024-12-25', '智慧煤矿集团主仓库', 'LOGISTICS', '月结45天',
 'CNY', 280000.00, 36400.00, 316400.00, 'EXECUTING',
 1, '采购员', 1, '采购员', '2024-12-06 11:00:00', NOW(), '执行中'),
('CGDD202412003', 3, 'CG202412003', 3, '中煤装备制造有限公司', 'GYS003',
 'NORMAL', '2024-12-11', '2025-01-15', '智慧煤矿集团主仓库', 'LOGISTICS', '月结30天',
 'CNY', 1200000.00, 156000.00, 1356000.00, 'CONFIRMED',
 1, '采购员', 1, '采购员', '2024-12-11 15:00:00', NOW(), '已确认'),
('CGDD202412004', 4, 'CG202412004', 4, '郑煤机集团股份有限公司', 'GYS004',
 'NORMAL', '2024-12-16', '2025-01-20', '智慧煤矿集团主仓库', 'LOGISTICS', '月结30天',
 'CNY', 5400000.00, 702000.00, 6102000.00, 'CONFIRMED',
 1, '采购员', 1, '采购员', '2024-12-16 12:00:00', NOW(), '已确认'),
('CGDD202412005', 5, 'CG202412005', 5, '天地科技股份有限公司', 'GYS005',
 'URGENT', '2024-12-21', '2024-12-30', '智慧煤矿集团主仓库', 'LOGISTICS', '月结30天',
 'CNY', 350000.00, 45500.00, 395500.00, 'EXECUTING',
 1, '采购员', 1, '采购员', '2024-12-21 16:00:00', NOW(), '执行中');

-- 采购订单明细
INSERT INTO purchase_order_detail (order_id, item_name, item_code, specification, brand, unit,
                                    quantity, unit_price, tax_rate, amount, tax_amount, amount_with_tax,
                                    received_quantity, required_date, remark) VALUES
(1, '防爆电机', 'YBK2-280M-4', 'YBK2-280M-4 75KW', '山东防爆', '台', 10.00, 15000.00, 13.00, 150000.00, 19500.00, 169500.00, 10.00, '2024-12-20', NULL),
(2, '采煤机截割部', 'MG300-CUT', 'MG300/700-WD截割部', '三一重工', '套', 1.00, 280000.00, 13.00, 280000.00, 36400.00, 316400.00, 0.00, '2024-12-25', NULL),
(3, '刮板输送机', 'SGZ764/630', 'SGZ764/630 630KW', '中煤装备', '台', 1.00, 1200000.00, 13.00, 1200000.00, 156000.00, 1356000.00, 0.00, '2025-01-15', NULL),
(4, '液压支架', 'ZY6800/14/32', 'ZY6800/14/32', '郑煤机', '架', 120.00, 45000.00, 13.00, 5400000.00, 702000.00, 6102000.00, 0.00, '2025-01-20', NULL),
(5, '瓦斯监测系统', 'KJ90', 'KJ90 多参数监测', '天地科技', '套', 1.00, 350000.00, 13.00, 350000.00, 45500.00, 395500.00, 0.00, '2024-12-30', NULL);

-- ============================================
-- 第七部分：采购收货数据
-- ============================================

INSERT INTO purchase_receiving (receiving_no, order_id, order_no, supplier_id, supplier_name,
                                 receiving_date, warehouse, location, delivery_no, logistics_company, logistics_no,
                                 total_amount, status, receiver_id, receiver_name, warehouse_keeper_id, warehouse_keeper_name,
                                 create_user_id, create_user_name, create_time, update_time, remark) VALUES
('CGSH202412001', 1, 'CGDD202412001', 1, '山东防爆电机厂',
 '2024-12-20', '主仓库', 'A区-01', 'SH2024122001', '顺丰物流', 'SF1234567890',
 169500.00, 'STORED', 1, '收货员', 1, '仓管员',
 1, '收货员', '2024-12-20 14:00:00', NOW(), '已入库');

-- 采购收货明细
INSERT INTO purchase_receiving_detail (receiving_id, order_detail_id, item_name, item_code, specification, unit,
                                        order_quantity, received_quantity, qualified_quantity, unqualified_quantity,
                                        unit_price, total_amount, batch_no, production_date, expiry_date,
                                        quality_status, storage_status, remark) VALUES
(1, 1, '防爆电机', 'YBK2-280M-4', 'YBK2-280M-4 75KW', '台',
 10.00, 10.00, 10.00, 0.00,
 15000.00, 150000.00, 'BATCH2024122001', '2024-12-01', NULL,
 'PASSED', 'STORED', '全部合格，已入库');

-- ============================================
-- 第八部分：库存模块数据
-- ============================================

-- 仓库数据（如果不存在则插入）
INSERT INTO inventory_warehouse (warehouse_id, warehouse_code, warehouse_name, warehouse_type, location,
                                  manager_id, manager_name, contact_phone, area, capacity, capacity_unit, status,
                                  create_user_id, create_user_name, create_time, update_time) VALUES
(1, 'WH001', '主仓库', 'GENERAL', '智慧煤矿集团厂区A区',
 1, '仓管员', '010-88888807', 5000.00, 10000.00, '吨', 'ACTIVE',
 1, '系统', NOW(), NOW()),
(2, 'WH002', '备件仓库', 'SPARE_PART', '智慧煤矿集团厂区B区',
 1, '仓管员', '010-88888808', 2000.00, 5000.00, '吨', 'ACTIVE',
 1, '系统', NOW(), NOW()),
(3, 'WH003', '原材料仓库', 'RAW_MATERIAL', '智慧煤矿集团厂区C区',
 1, '仓管员', '010-88888809', 3000.00, 8000.00, '吨', 'ACTIVE',
 1, '系统', NOW(), NOW())
ON DUPLICATE KEY UPDATE warehouse_name=VALUES(warehouse_name);

-- 库位数据
INSERT INTO inventory_location (warehouse_id, warehouse_code, warehouse_name, location_code, location_name,
                                 location_type, zone, aisle, shelf, level, position, status,
                                 create_user_id, create_user_name, create_time, update_time) VALUES
(1, 'WH001', '主仓库', 'A-01-01-01', 'A区1号通道1号货架1层', 'NORMAL', 'A区', '01', '01', '01', '01', 'ACTIVE', 1, '系统', NOW(), NOW()),
(1, 'WH001', '主仓库', 'A-01-01-02', 'A区1号通道1号货架1层', 'NORMAL', 'A区', '01', '01', '02', '02', 'ACTIVE', 1, '系统', NOW(), NOW()),
(1, 'WH001', '主仓库', 'B-02-01-01', 'B区2号通道1号货架1层', 'NORMAL', 'B区', '02', '01', '01', '01', 'ACTIVE', 1, '系统', NOW(), NOW()),
(2, 'WH002', '备件仓库', 'C-01-01-01', 'C区1号通道1号货架1层', 'NORMAL', 'C区', '01', '01', '01', '01', 'ACTIVE', 1, '系统', NOW(), NOW()),
(3, 'WH003', '原材料仓库', 'D-01-01-01', 'D区1号通道1号货架1层', 'NORMAL', 'D区', '01', '01', '01', '01', 'ACTIVE', 1, '系统', NOW(), NOW());

-- 库存物料
INSERT INTO inventory_material (material_code, material_name, material_type, category, specification, brand,
                                 manufacturer, unit, unit_price, currency, min_stock, max_stock, safety_stock,
                                 reorder_point, reorder_quantity, shelf_life, storage_condition, status,
                                 create_user_id, create_user_name, create_time, update_time, remark) VALUES
('WL001', '防爆电机', '设备', '电机', 'YBK2-280M-4 75KW', '山东防爆', '山东防爆电机厂', '台', 15000.00, 'CNY', 5.00, 50.00, 10.00, 10.00, 20.00, NULL, '常温', 'ACTIVE', 1, '系统', NOW(), NOW(), '主提升系统设备'),
('WL002', '防爆电缆', '材料', '电气材料', 'MYJV22-3×95+1×50', '远东', '远东电缆', '米', 120.00, 'CNY', 2000.00, 10000.00, 3000.00, 3000.00, 5000.00, NULL, '干燥通风', 'ACTIVE', 1, '系统', NOW(), NOW(), '井下供电电缆'),
('WL003', '锚杆', '材料', '支护材料', 'Φ20×2000mm螺纹钢', '首钢', '首钢集团', '根', 35.00, 'CNY', 5000.00, 20000.00, 8000.00, 8000.00, 10000.00, NULL, '防潮', 'ACTIVE', 1, '系统', NOW(), NOW(), '巷道支护材料'),
('WL004', '液压油', '耗材', '油品', '46号抗磨液压油', '长城', '中石化', '升', 25.00, 'CNY', 2000.00, 10000.00, 3000.00, 3000.00, 5000.00, 365, '常温', 'ACTIVE', 1, '系统', NOW(), NOW(), '设备润滑用油'),
('WL005', '矿灯', '设备', '安全用品', 'LED矿灯 4小时续航', '神火', '神火股份', '盏', 180.00, 'CNY', 200.00, 1000.00, 300.00, 300.00, 500.00, NULL, '常温', 'ACTIVE', 1, '系统', NOW(), NOW(), '井下照明设备');

-- 库存明细（从采购收货入库）
INSERT INTO inventory_stock (warehouse_id, warehouse_code, warehouse_name, location_id, location_code,
                             material_id, material_code, material_name, batch_no, production_date, expiry_date,
                             quantity, available_quantity, frozen_quantity, unit_price, total_value,
                             last_in_date, last_out_date, create_time, update_time) VALUES
(1, 'WH001', '主仓库', 1, 'A-01-01-01',
 1, 'WL001', '防爆电机', 'BATCH2024122001', '2024-12-01', NULL,
 10.00, 10.00, 0.00, 15000.00, 150000.00,
 '2024-12-20', NULL, NOW(), NOW());

-- 入库单（从采购收货生成）
INSERT INTO inventory_inbound (inbound_no, inbound_type, warehouse_id, warehouse_code, warehouse_name,
                                source_type, source_no, source_id, inbound_date, supplier_id, supplier_name,
                                total_quantity, total_amount, handler_id, handler_name, receiver_id, receiver_name,
                                status, create_user_id, create_user_name, create_time, update_time, remark) VALUES
('RK2024122001', 'PURCHASE', 1, 'WH001', '主仓库',
 'PURCHASE_ORDER', 'CGDD202412001', 1, '2024-12-20', 1, '山东防爆电机厂',
 10.00, 150000.00, 1, '经办人', 1, '收货员',
 'COMPLETED', 1, '系统', '2024-12-20 15:00:00', NOW(), '采购收货入库');

-- 入库明细
INSERT INTO inventory_inbound_detail (inbound_id, material_id, material_code, material_name, specification, unit,
                                       quantity, received_quantity, unit_price, amount, batch_no, production_date,
                                       expiry_date, location_id, location_code, remark) VALUES
(1, 1, 'WL001', '防爆电机', 'YBK2-280M-4 75KW', '台',
 10.00, 10.00, 15000.00, 150000.00, 'BATCH2024122001', '2024-12-01', NULL,
 1, 'A-01-01-01', NULL);

-- ============================================
-- 第九部分：维修管理模块数据
-- ============================================

-- 维修团队
INSERT INTO maintenance_team (team_code, team_name, team_type, leader_id, leader_name, member_count,
                              specialty, status, create_user_id, create_user_name, create_time, update_time, remark) VALUES
('MT001', '电气维修组', 'ELECTRICAL', 1, '电气组长', 5,
 '电气设备维修、故障诊断', 'ACTIVE', 1, '系统', NOW(), NOW(), '负责电气设备维修'),
('MT002', '机械维修组', 'MECHANICAL', 1, '机械组长', 6,
 '机械设备维修、保养', 'ACTIVE', 1, '系统', NOW(), NOW(), '负责机械设备维修'),
('MT003', '综合维修组', 'GENERAL', 1, '综合组长', 4,
 '综合维修、应急处理', 'ACTIVE', 1, '系统', NOW(), NOW(), '负责综合维修任务');

-- 维修工单
INSERT INTO maintenance_work_order (work_order_no, work_order_type, priority, asset_id, asset_code, asset_name,
                                     fault_type, fault_description, reported_by, reported_by_name, reported_time,
                                     assigned_team_id, assigned_team_name, assigned_technician_id, assigned_technician_name,
                                     scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time,
                                     status, completion_rate, labor_cost, material_cost, total_cost,
                                     create_user_id, create_user_name, create_time, update_time, remark) VALUES
('WX202412001', 'REPAIR', 'HIGH', 1, 'ZC001', '防爆电机',
 '机械故障', '电机轴承磨损，运行有异响', 1, '报修人', '2024-12-05 08:00:00',
 2, '机械维修组', 1, '维修技师',
 '2024-12-05 09:00:00', '2024-12-05 17:00:00', '2024-12-05 09:00:00', '2024-12-05 16:30:00',
 'COMPLETED', 100.00, 800.00, 2500.00, 3300.00,
 1, '系统', '2024-12-05 08:30:00', NOW(), '已更换轴承，运行正常'),
('WX202412002', 'REPAIR', 'URGENT', 3, 'ZC003', '采煤机',
 '液压故障', '采煤机截割部液压系统漏油', 1, '报修人', '2024-12-08 09:00:00',
 2, '机械维修组', 1, '维修技师',
 '2024-12-08 10:00:00', '2024-12-09 18:00:00', '2024-12-08 10:00:00', '2024-12-09 17:00:00',
 'COMPLETED', 100.00, 2000.00, 15000.00, 17000.00,
 1, '系统', '2024-12-08 09:30:00', NOW(), '更换密封件，已修复'),
('WX202412003', 'REPAIR', 'NORMAL', 4, 'ZC004', '刮板输送机',
 '机械故障', '刮板输送机链条断裂', 1, '报修人', '2024-12-12 10:00:00',
 2, '机械维修组', 1, '维修技师',
 '2024-12-12 11:00:00', '2024-12-13 18:00:00', '2024-12-12 11:00:00', NULL,
 'IN_PROGRESS', 60.00, 1200.00, 8000.00, 9200.00,
 1, '系统', '2024-12-12 10:30:00', NOW(), '正在更换链条'),
('WX202412004', 'MAINTENANCE', 'NORMAL', 7, 'ZC007', '瓦斯监测系统',
 '日常保养', '定期维护保养', 1, '报修人', '2024-12-18 08:00:00',
 1, '电气维修组', 1, '维修技师',
 '2024-12-18 09:00:00', '2024-12-18 12:00:00', '2024-12-18 09:00:00', '2024-12-18 12:00:00',
 'COMPLETED', 100.00, 400.00, 3500.00, 3900.00,
 1, '系统', '2024-12-18 08:30:00', NOW(), '更换传感器，已恢复正常');

-- 维修工单明细
INSERT INTO maintenance_work_order_detail (work_order_id, step_no, step_name, step_description,
                                            technician_id, technician_name, start_time, end_time, duration, status, remark) VALUES
(1, 1, '故障诊断', '检查电机轴承磨损情况', 1, '维修技师', '2024-12-05 09:00:00', '2024-12-05 10:00:00', 60, 'COMPLETED', NULL),
(1, 2, '拆卸轴承', '拆卸旧轴承', 1, '维修技师', '2024-12-05 10:00:00', '2024-12-05 12:00:00', 120, 'COMPLETED', NULL),
(1, 3, '安装新轴承', '安装新轴承并调试', 1, '维修技师', '2024-12-05 13:00:00', '2024-12-05 16:30:00', 210, 'COMPLETED', NULL),
(2, 1, '故障诊断', '检查液压系统漏油位置', 1, '维修技师', '2024-12-08 10:00:00', '2024-12-08 11:30:00', 90, 'COMPLETED', NULL),
(2, 2, '更换密封件', '更换液压系统密封件', 1, '维修技师', '2024-12-08 14:00:00', '2024-12-09 16:00:00', 1320, 'COMPLETED', NULL),
(2, 3, '系统测试', '测试液压系统运行', 1, '维修技师', '2024-12-09 16:00:00', '2024-12-09 17:00:00', 60, 'COMPLETED', NULL);

-- 维修备件领用
INSERT INTO maintenance_part_requisition (requisition_no, work_order_id, work_order_no, requisition_type,
                                            requisition_date, requisition_by, requisition_by_name, warehouse_id, warehouse_name,
                                            total_amount, status, approve_by, approve_by_name, approve_time,
                                            issue_by, issue_by_name, issue_time, create_user_id, create_user_name,
                                            create_time, update_time, remark) VALUES
('WXLY202412001', 1, 'WX202412001', 'REPAIR',
 '2024-12-05', 1, '维修技师', 2, '备件仓库',
 2500.00, 'ISSUED', 1, '审批人', '2024-12-05 09:30:00',
 1, '仓管员', '2024-12-05 10:00:00', 1, '系统',
 NOW(), NOW(), '维修用备件'),
('WXLY202412002', 2, 'WX202412002', 'REPAIR',
 '2024-12-08', 1, '维修技师', 2, '备件仓库',
 15000.00, 'ISSUED', 1, '审批人', '2024-12-08 10:30:00',
 1, '仓管员', '2024-12-08 11:00:00', 1, '系统',
 NOW(), NOW(), '维修用备件');

-- 维修备件领用明细
INSERT INTO maintenance_part_requisition_detail (requisition_id, material_id, material_code, material_name,
                                                   specification, unit, quantity, issued_quantity, unit_price, amount, remark) VALUES
(1, 4, 'WL004', '液压油', '46号抗磨液压油', '升', 100.00, 100.00, 25.00, 2500.00, NULL),
(2, NULL, NULL, '采煤机密封件', 'MG300密封件', '套', 1.00, 1.00, 15000.00, 15000.00, NULL);

-- 出库单（维修领用）
INSERT INTO inventory_outbound (outbound_no, outbound_type, warehouse_id, warehouse_code, warehouse_name,
                                destination_type, destination_no, destination_id, outbound_date,
                                dept_id, dept_name, recipient_id, recipient_name,
                                total_quantity, total_amount, handler_id, handler_name,
                                status, issue_user_id, issue_user_name, issue_time,
                                create_user_id, create_user_name, create_time, update_time, remark) VALUES
('CK2024120501', 'MAINTENANCE', 2, 'WH002', '备件仓库',
 'MAINTENANCE_ORDER', 'WX202412001', 1, '2024-12-05',
 6, '维修部', 1, '维修技师',
 100.00, 2500.00, 1, '经办人',
 'COMPLETED', 1, '仓管员', '2024-12-05 10:00:00',
 1, '系统', '2024-12-05 10:00:00', NOW(), '维修领用出库');

-- 出库明细
INSERT INTO inventory_outbound_detail (outbound_id, material_id, material_code, material_name, specification, unit,
                                       quantity, issued_quantity, unit_price, amount, batch_no, location_id, location_code, remark) VALUES
(1, 4, 'WL004', '液压油', '46号抗磨液压油', '升',
 100.00, 100.00, 25.00, 2500.00, NULL, 4, 'C-01-01-01', NULL);

-- ============================================
-- 第十部分：预警模块数据
-- ============================================

-- 预警规则（根据表结构调整字段）
INSERT INTO warning_rule (rule_code, rule_name, rule_type, warning_category,
                          rule_condition, rule_expression, check_frequency, is_enabled, priority,
                          create_user_id, create_user_name, create_time, update_time, remark) VALUES
('WX001', '防爆证书到期预警', 'ASSET', 'EXPLOSION_PROOF',
 '{"days_before_expiry": 30}', 'explosion_proof_expire_date <= DATE_ADD(NOW(), INTERVAL 30 DAY)', 'DAILY', 1, 10,
 1, '系统', NOW(), NOW(), '防爆设备证书到期前30天预警'),
('WX002', '库存低库存预警', 'INVENTORY', 'LOW_STOCK',
 '{"compare": "quantity < safety_stock"}', 'quantity < safety_stock', 'DAILY', 1, 5,
 1, '系统', NOW(), NOW(), '库存低于安全库存时预警'),
('WX003', '设备故障预警', 'ASSET', 'FAULT',
 '{"fault_count": 3, "time_range": "7 DAY"}', 'fault_count >= 3', 'DAILY', 1, 8,
 1, '系统', NOW(), NOW(), '设备连续故障3次预警');

-- 预警记录（根据表结构调整字段）
INSERT INTO warning_record (rule_id, rule_code, rule_name, warning_type, warning_category,
                             warning_level_id, warning_level_code, warning_level_name,
                             warning_title, warning_content, warning_data,
                             source_type, source_id, source_code, source_name,
                             trigger_time, status, handler_id, handler_name, handle_time, handle_result,
                             create_user_id, create_user_name, create_time, update_time, remark) VALUES
(1, 'WX001', '防爆证书到期预警', 'ASSET', 'EXPLOSION_PROOF',
 3, 'HIGH', '高',
 '防爆证书即将到期', '防爆开关(ZC002)的防爆证书将在30天内到期，请及时处理',
 '{"asset_id": 2, "asset_code": "ZC002", "asset_name": "防爆开关", "expire_date": "2025-11-30"}',
 'ASSET', 2, 'ZC002', '防爆开关',
 '2024-12-01 08:00:00', 'RESOLVED', 1, '处理人', '2024-12-01 10:00:00', '已联系供应商更新证书', 1, '系统', NOW(), NOW(), NULL),
(2, 'WX002', '库存低库存预警', 'INVENTORY', 'LOW_STOCK',
 2, 'MEDIUM', '中',
 '库存低于安全库存', '防爆电缆(WL002)当前库存低于安全库存，请及时补货',
 '{"material_id": 2, "material_code": "WL002", "material_name": "防爆电缆", "current_quantity": 1500, "safety_stock": 3000}',
 'INVENTORY', 2, 'WL002', '防爆电缆',
 '2024-12-15 09:00:00', 'PENDING', NULL, NULL, NULL, NULL, 1, '系统', NOW(), NOW(), NULL);

-- ============================================
-- 数据导入完成提示
-- ============================================

SELECT '============================================' AS separator;
SELECT '测试数据导入完成！' AS message;
SELECT '============================================' AS separator;
SELECT '已导入数据统计：' AS summary;
SELECT CONCAT('  - 供应商：', COUNT(*), ' 条') AS supplier_count FROM purchase_supplier;
SELECT CONCAT('  - 资产：', COUNT(*), ' 条') AS asset_count FROM asset;
SELECT CONCAT('  - 采购申请：', COUNT(*), ' 条') AS requisition_count FROM purchase_requisition;
SELECT CONCAT('  - 采购订单：', COUNT(*), ' 条') AS order_count FROM purchase_order;
SELECT CONCAT('  - 采购收货：', COUNT(*), ' 条') AS receiving_count FROM purchase_receiving;
SELECT CONCAT('  - 库存物料：', COUNT(*), ' 条') AS material_count FROM inventory_material;
SELECT CONCAT('  - 库存明细：', COUNT(*), ' 条') AS stock_count FROM inventory_stock;
SELECT CONCAT('  - 维修工单：', COUNT(*), ' 条') AS work_order_count FROM maintenance_work_order;
SELECT CONCAT('  - 预警记录：', COUNT(*), ' 条') AS warning_count FROM warning_record;
SELECT '============================================' AS separator;

