-- 智慧煤矿ERP管理系统 - 中文示例数据
-- 用于演示和测试系统功能

-- ============================================
-- 1. 部门数据
-- ============================================
INSERT INTO sys_dept (dept_name, parent_id, order_num, leader, phone, email, status, create_time, update_time) VALUES
('智慧煤矿集团', 0, 1, '张总', '010-88888888', 'zhang@coal.com', '0', NOW(), NOW()),
('生产部', 1, 1, '李经理', '010-88888801', 'li@coal.com', '0', NOW(), NOW()),
('安全部', 1, 2, '王经理', '010-88888802', 'wang@coal.com', '0', NOW(), NOW()),
('财务部', 1, 3, '赵经理', '010-88888803', 'zhao@coal.com', '0', NOW(), NOW()),
('采购部', 1, 4, '钱经理', '010-88888804', 'qian@coal.com', '0', NOW(), NOW()),
('维修部', 1, 5, '孙经理', '010-88888805', 'sun@coal.com', '0', NOW(), NOW()),
('人力资源部', 1, 6, '周经理', '010-88888806', 'zhou@coal.com', '0', NOW(), NOW()),
('采煤一队', 2, 1, '吴队长', '010-88888811', 'wu@coal.com', '0', NOW(), NOW()),
('采煤二队', 2, 2, '郑队长', '010-88888812', 'zheng@coal.com', '0', NOW(), NOW()),
('掘进队', 2, 3, '冯队长', '010-88888813', 'feng@coal.com', '0', NOW(), NOW());

-- ============================================
-- 2. 资产数据（煤矿设备）
-- ============================================
INSERT INTO asset (asset_code, asset_name, asset_type, category, manufacturer, model, serial_number, 
                   purchase_date, purchase_price, status, location, dept_id, is_explosion_proof, 
                   explosion_proof_expire_date, create_time, update_time, remark) VALUES
('ZC001', '防爆电机', '电气设备', '电机', '山东电机厂', 'YBK2-280M-4', 'SN2024001', '2024-01-15', 15000.00, '0', '主井口车间', 8, '1', '2025-12-31', NOW(), NOW(), '主提升系统配套设备'),
('ZC002', '防爆开关', '电气设备', '开关', '上海电气', 'BXM-100', 'SN2024002', '2024-02-20', 5000.00, '0', '井下配电室', 8, '1', '2025-11-30', NOW(), NOW(), '井下供电系统'),
('ZC003', '采煤机', '机械设备', '采掘设备', '三一重工', 'MG300/700-WD', 'SN2024003', '2023-06-10', 2800000.00, '0', '采煤工作面', 8, '1', '2026-06-10', NOW(), NOW(), '综采工作面主要设备'),
('ZC004', '刮板输送机', '机械设备', '运输设备', '中煤装备', 'SGZ764/630', 'SN2024004', '2023-07-15', 1200000.00, '0', '采煤工作面', 8, '1', '2026-07-15', NOW(), NOW(), '煤炭运输设备'),
('ZC005', '液压支架', '机械设备', '支护设备', '郑煤机', 'ZY6800/14/32', 'SN2024005', '2023-08-20', 450000.00, '0', '采煤工作面', 8, '1', '2026-08-20', NOW(), NOW(), '工作面支护设备，共120架'),
('ZC006', '掘进机', '机械设备', '采掘设备', '北方重工', 'EBZ160', 'SN2024006', '2023-09-25', 1800000.00, '0', '掘进工作面', 10, '1', '2026-09-25', NOW(), NOW(), '巷道掘进设备'),
('ZC007', '瓦斯监测系统', '监控设备', '安全监控', '天地科技', 'KJ90', 'SN2024007', '2024-03-10', 350000.00, '0', '井下各监测点', 3, '1', '2025-03-10', NOW(), NOW(), '瓦斯浓度实时监测'),
('ZC008', '人员定位系统', '监控设备', '安全监控', '中煤科工', 'KJ251', 'SN2024008', '2024-03-15', 280000.00, '0', '井下各区域', 3, '1', '2025-03-15', NOW(), NOW(), '井下人员实时定位'),
('ZC009', '防爆摄像头', '监控设备', '视频监控', '海康威视', 'DS-2CD7A47HWD', 'SN2024009', '2024-04-01', 3500.00, '0', '主井口', 3, '1', '2025-04-01', NOW(), NOW(), '井口安全监控'),
('ZC010', '防爆照明灯', '电气设备', '照明设备', '飞利浦', 'BLC-100', 'SN2024010', '2024-04-10', 800.00, '0', '井下各巷道', 8, '1', '2025-04-10', NOW(), NOW(), '井下照明设备，共200盏'),
('ZC011', '通风机', '机械设备', '通风设备', '沈鼓集团', 'FBCDZ-6-No.18', 'SN2024011', '2023-10-15', 850000.00, '0', '风井口', 2, '0', NULL, NOW(), NOW(), '主通风系统'),
('ZC012', '压风机', '机械设备', '压风设备', '开山集团', 'LG-75/8', 'SN2024012', '2023-11-20', 420000.00, '0', '压风机房', 2, '0', NULL, NOW(), NOW(), '井下压风系统'),
('ZC013', '提升机', '机械设备', '提升设备', '中信重工', 'JKMD-3.5×4', 'SN2024013', '2023-12-05', 3200000.00, '0', '主井口', 2, '0', NULL, NOW(), NOW(), '主提升系统'),
('ZC014', '排水泵', '机械设备', '排水设备', '上海凯泉', 'MD280-65×6', 'SN2024014', '2024-01-20', 85000.00, '0', '井下泵房', 2, '1', '2025-01-20', NOW(), NOW(), '井下排水系统'),
('ZC015', '防爆电话', '通信设备', '通信设备', '华为', 'KTW125', 'SN2024015', '2024-02-10', 1200.00, '0', '井下各区域', 8, '1', '2025-02-10', NOW(), NOW(), '井下通信设备，共50部');

-- ============================================
-- 3. 采购订单数据
-- ============================================
INSERT INTO purchase_order (order_no, supplier, order_type, total_amount, status, create_user_id, create_time, update_time, remark) VALUES
('CG202411001', '山东电机厂', '设备采购', 150000.00, 'COMPLETED', 1, '2024-11-01 09:00:00', NOW(), '防爆电机批量采购'),
('CG202411002', '三一重工', '设备采购', 2800000.00, 'PENDING', 1, '2024-11-05 10:30:00', NOW(), '采煤机采购订单'),
('CG202411003', '中煤装备', '设备采购', 1200000.00, 'PROCESSING', 1, '2024-11-10 14:20:00', NOW(), '刮板输送机采购'),
('CG202411004', '郑煤机', '设备采购', 5400000.00, 'COMPLETED', 1, '2024-11-15 11:00:00', NOW(), '液压支架采购（120架）'),
('CG202411005', '天地科技', '设备采购', 350000.00, 'COMPLETED', 1, '2024-11-20 15:30:00', NOW(), '瓦斯监测系统采购'),
('CG202411006', '海康威视', '设备采购', 70000.00, 'PENDING', 1, '2024-11-25 09:15:00', NOW(), '防爆摄像头采购（20个）'),
('CG202411007', '飞利浦', '设备采购', 160000.00, 'COMPLETED', 1, '2024-11-28 10:00:00', NOW(), '防爆照明灯采购（200盏）'),
('CG202411008', '上海凯泉', '设备采购', 170000.00, 'PROCESSING', 1, '2024-12-01 13:45:00', NOW(), '排水泵采购（2台）');

-- ============================================
-- 4. 维修工单数据
-- ============================================
INSERT INTO repair_order (repair_no, asset_id, fault_description, repair_type, repair_cost, status, 
                          repair_user_id, repair_start_time, repair_end_time, create_time, update_time, remark) VALUES
('WX202411001', 1, '电机轴承磨损，运行有异响', '日常维修', 2500.00, 'COMPLETED', 1, '2024-11-05 08:00:00', '2024-11-05 16:30:00', '2024-11-05 07:30:00', NOW(), '已更换轴承，运行正常'),
('WX202411002', 3, '采煤机截割部液压系统漏油', '故障维修', 15000.00, 'COMPLETED', 1, '2024-11-08 09:00:00', '2024-11-09 17:00:00', '2024-11-08 08:30:00', NOW(), '更换密封件，已修复'),
('WX202411003', 4, '刮板输送机链条断裂', '紧急维修', 8000.00, 'PROCESSING', 1, '2024-11-12 10:00:00', NULL, '2024-11-12 09:30:00', NOW(), '正在更换链条'),
('WX202411004', 6, '掘进机行走部故障', '故障维修', 12000.00, 'PENDING', 1, NULL, NULL, '2024-11-15 14:00:00', NOW(), '待安排维修'),
('WX202411005', 7, '瓦斯监测系统传感器故障', '日常维修', 3500.00, 'COMPLETED', 1, '2024-11-18 08:30:00', '2024-11-18 12:00:00', '2024-11-18 08:00:00', NOW(), '更换传感器，已恢复正常'),
('WX202411006', 11, '通风机叶片磨损', '预防性维修', 45000.00, 'SCHEDULED', 1, '2024-11-25 08:00:00', NULL, '2024-11-20 15:00:00', NOW(), '计划维修'),
('WX202411007', 13, '提升机减速器异响', '故障维修', 28000.00, 'PROCESSING', 1, '2024-11-22 09:00:00', NULL, '2024-11-22 08:30:00', NOW(), '正在检查维修');

-- ============================================
-- 5. 库存数据（煤矿物资）
-- ============================================
INSERT INTO inventory (material_code, material_name, material_type, unit, quantity, min_stock, max_stock, 
                       warehouse, location, create_time, update_time, remark) VALUES
('WL001', '液压油', '油品', '升', 5000.00, 2000.00, 10000.00, '主仓库', 'A区-01', NOW(), NOW(), '46号抗磨液压油'),
('WL002', '防爆电缆', '电气材料', '米', 5000.00, 2000.00, 10000.00, '主仓库', 'B区-05', NOW(), NOW(), 'MYJV22-3×95+1×50'),
('WL003', '锚杆', '支护材料', '根', 10000.00, 5000.00, 20000.00, '主仓库', 'C区-10', NOW(), NOW(), 'Φ20×2000mm螺纹钢锚杆'),
('WL004', '锚索', '支护材料', '根', 2000.00, 1000.00, 5000.00, '主仓库', 'C区-11', NOW(), NOW(), 'Φ17.8×6300mm钢绞线锚索'),
('WL005', '金属网', '支护材料', '平方米', 5000.00, 2000.00, 10000.00, '主仓库', 'C区-12', NOW(), NOW(), '8#铁丝编织网'),
('WL006', '水泥', '建材', '吨', 200.00, 100.00, 500.00, '主仓库', 'D区-20', NOW(), NOW(), 'P.O 42.5普通硅酸盐水泥'),
('WL007', '砂子', '建材', '立方米', 500.00, 200.00, 1000.00, '主仓库', 'D区-21', NOW(), NOW(), '中粗砂'),
('WL008', '石子', '建材', '立方米', 800.00, 300.00, 1500.00, '主仓库', 'D区-22', NOW(), NOW(), '5-20mm碎石'),
('WL009', '风筒', '通风材料', '米', 3000.00, 1000.00, 5000.00, '主仓库', 'E区-30', NOW(), NOW(), 'Φ800mm阻燃风筒'),
('WL010', '风门', '通风材料', '扇', 50.00, 20.00, 100.00, '主仓库', 'E区-31', NOW(), NOW(), '钢制风门'),
('WL011', '矿灯', '安全用品', '盏', 500.00, 200.00, 1000.00, '主仓库', 'F区-40', NOW(), NOW(), 'LED矿灯，4小时续航'),
('WL012', '自救器', '安全用品', '个', 1000.00, 500.00, 2000.00, '主仓库', 'F区-41', NOW(), NOW(), '压缩氧自救器'),
('WL013', '安全帽', '安全用品', '顶', 800.00, 300.00, 1500.00, '主仓库', 'F区-42', NOW(), NOW(), 'ABS安全帽'),
('WL014', '工作服', '劳保用品', '套', 600.00, 200.00, 1000.00, '主仓库', 'G区-50', NOW(), NOW(), '阻燃工作服'),
('WL015', '防尘口罩', '劳保用品', '个', 2000.00, 1000.00, 5000.00, '主仓库', 'G区-51', NOW(), NOW(), 'KN95防尘口罩'),
('WL016', '钢丝绳', '材料', '米', 2000.00, 1000.00, 5000.00, '主仓库', 'H区-60', NOW(), NOW(), 'Φ18.5mm钢丝绳'),
('WL017', 'U型钢', '支护材料', '根', 500.00, 200.00, 1000.00, '主仓库', 'C区-13', NOW(), NOW(), '29U型钢支架'),
('WL018', '工字钢', '支护材料', '根', 300.00, 100.00, 500.00, '主仓库', 'C区-14', NOW(), NOW(), '11#工字钢'),
('WL019', '道轨', '材料', '米', 5000.00, 2000.00, 10000.00, '主仓库', 'H区-61', NOW(), NOW(), '30kg/m道轨'),
('WL020', '道岔', '材料', '组', 20.00, 10.00, 50.00, '主仓库', 'H区-62', NOW(), NOW(), '30kg/m道岔');

-- ============================================
-- 6. 预警数据
-- ============================================
INSERT INTO warning_alert (alert_type, alert_level, asset_id, asset_code, asset_name, alert_title, 
                          alert_content, expire_date, days_remaining, status, create_user_id, create_time, update_time, remark) VALUES
('EXPLOSION_PROOF', 'RED', 2, 'ZC002', '防爆开关', '防爆证书即将到期', '防爆开关(ZC002)的防爆证书将在15天后到期，请及时安排检测！', DATE_ADD(NOW(), INTERVAL 15 DAY), 15, '0', 1, NOW(), NOW(), '紧急处理'),
('EXPLOSION_PROOF', 'YELLOW', 7, 'ZC007', '瓦斯监测系统', '防爆证书即将到期', '瓦斯监测系统(ZC007)的防爆证书将在30天后到期，请提前安排检测。', DATE_ADD(NOW(), INTERVAL 30 DAY), 30, '0', 1, NOW(), NOW(), '提前准备'),
('MAINTENANCE', 'YELLOW', 3, 'ZC003', '采煤机', '设备保养提醒', '采煤机(ZC003)已运行1500小时，建议进行定期保养。', DATE_ADD(NOW(), INTERVAL 7 DAY), 7, '0', 1, NOW(), NOW(), '定期保养'),
('MAINTENANCE', 'GREEN', 4, 'ZC004', '刮板输送机', '设备保养提醒', '刮板输送机(ZC004)已运行1200小时，建议进行定期保养。', DATE_ADD(NOW(), INTERVAL 15 DAY), 15, '0', 1, NOW(), NOW(), '定期保养'),
('INVENTORY', 'YELLOW', NULL, NULL, NULL, '库存不足预警', '液压油(WL001)当前库存5000升，低于安全库存线，请及时采购。', NULL, NULL, '0', 1, NOW(), NOW(), '库存预警'),
('INVENTORY', 'RED', NULL, NULL, NULL, '库存严重不足', '防尘口罩(WL015)当前库存2000个，低于最低库存线，请立即采购！', NULL, NULL, '0', 1, NOW(), NOW(), '紧急采购');

-- ============================================
-- 7. 财务模块 - 客户数据
-- ============================================
-- 检查并添加缺失的字段（如果不存在）
SET @dbname = DATABASE();
SET @tablename = 'finance_customer';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = 'contact_person')
  ) > 0,
  'SELECT 1',
  'ALTER TABLE finance_customer ADD COLUMN contact_person varchar(50) DEFAULT NULL COMMENT ''联系人'''
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = 'credit_level')
  ) > 0,
  'SELECT 1',
  'ALTER TABLE finance_customer ADD COLUMN credit_level varchar(20) DEFAULT NULL COMMENT ''信用等级'''
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 插入客户数据（兼容有无字段的情况）
INSERT INTO finance_customer (customer_code, customer_name, customer_type, credit_level, credit_amount, 
                              payment_terms, contact_person, contact_phone, address, tax_number, 
                              bank_account, bank_name, status, create_time, update_time, remark) VALUES
('KH001', '华能电力集团', 'ELECTRIC_PLANT', 'AAA', 50000000.00, '月结30天', '张经理', '010-66666601', '北京市朝阳区', '91110000123456789X', '6222021234567890123', '中国工商银行北京分行', '0', NOW(), NOW(), '主要客户'),
('KH002', '国电电力集团', 'ELECTRIC_PLANT', 'AAA', 30000000.00, '月结30天', '李经理', '010-66666602', '北京市海淀区', '91110000987654321Y', '6222029876543210987', '中国建设银行北京分行', '0', NOW(), NOW(), '主要客户'),
('KH003', '首钢集团', 'STEEL_PLANT', 'AA', 20000000.00, '月结45天', '王经理', '010-66666603', '北京市石景山区', '91110000111222333Z', '6222021112223334445', '中国银行北京分行', '0', NOW(), NOW(), '长期合作'),
('KH004', '河北钢铁集团', 'STEEL_PLANT', 'AA', 15000000.00, '月结45天', '赵经理', '0311-88888801', '河北省石家庄市', '91130000222333444A', '6222022223334445556', '中国农业银行石家庄分行', '0', NOW(), NOW(), '长期合作'),
('KH005', '山西煤炭贸易公司', 'TRADER', 'A', 10000000.00, '现款现货', '钱经理', '0351-77777701', '山西省太原市', '91140000333444555B', '6222023334445556667', '中国工商银行太原分行', '0', NOW(), NOW(), '贸易商')
ON DUPLICATE KEY UPDATE customer_name=VALUES(customer_name);

-- ============================================
-- 8. 财务模块 - 供应商数据
-- ============================================
-- 检查并添加缺失的字段（如果不存在）
SET @tablename = 'finance_supplier';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = 'contact_person')
  ) > 0,
  'SELECT 1',
  'ALTER TABLE finance_supplier ADD COLUMN contact_person varchar(50) DEFAULT NULL COMMENT ''联系人'''
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = 'evaluation_level')
  ) > 0,
  'SELECT 1',
  'ALTER TABLE finance_supplier ADD COLUMN evaluation_level varchar(20) DEFAULT NULL COMMENT ''评估等级'''
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 插入供应商数据（使用evaluation_level替代credit_level）
INSERT INTO finance_supplier (supplier_code, supplier_name, supplier_type, evaluation_level, payment_terms, 
                             contact_person, contact_phone, address, tax_number, bank_account, bank_name, 
                             status, create_time, update_time, remark) VALUES
('GYS001', '山东电机厂', 'EQUIPMENT', 'AA', '货到付款', '孙经理', '0531-55555501', '山东省济南市', '91370000111122233C', '6222024445556667778', '中国建设银行济南分行', '0', NOW(), NOW(), '设备供应商'),
('GYS002', '三一重工', 'EQUIPMENT', 'AAA', '月结30天', '周经理', '0731-44444401', '湖南省长沙市', '91430000222233344D', '6222025556667778889', '中国工商银行长沙分行', '0', NOW(), NOW(), '大型设备供应商'),
('GYS003', '中煤装备', 'EQUIPMENT', 'AA', '月结30天', '吴经理', '010-33333301', '北京市丰台区', '91110000333344455E', '6222026667778889990', '中国银行北京分行', '0', NOW(), NOW(), '煤矿设备供应商'),
('GYS004', '郑煤机', 'EQUIPMENT', 'AAA', '月结30天', '郑经理', '0371-22222201', '河南省郑州市', '91410000444455566F', '6222027778889990001', '中国建设银行郑州分行', '0', NOW(), NOW(), '液压支架供应商'),
('GYS005', '天地科技', 'EQUIPMENT', 'AA', '月结30天', '冯经理', '010-11111101', '北京市朝阳区', '91110000555566677G', '6222028889990001112', '中国工商银行北京分行', '0', NOW(), NOW(), '安全设备供应商'),
('GYS006', '山西煤炭物资公司', 'MATERIAL', 'A', '现款现货', '陈经理', '0351-99999901', '山西省太原市', '91140000666677788H', '6222029990001112223', '中国农业银行太原分行', '0', NOW(), NOW(), '物资供应商')
ON DUPLICATE KEY UPDATE supplier_name=VALUES(supplier_name);

-- ============================================
-- 9. 人力资源 - 部门扩展数据
-- ============================================
INSERT INTO hr_department (dept_id, dept_code, dept_type, establish_date, budget_count, actual_count, 
                          cost_center, is_production, is_safety_critical) VALUES
(2, 'SCB001', 'PRODUCTION', '2020-01-01', 200, 185, 'CC001', 1, 1),
(3, 'AQB001', 'SAFETY', '2020-01-01', 30, 28, 'CC002', 0, 1),
(4, 'CWB001', 'FINANCE', '2020-01-01', 15, 14, 'CC003', 0, 0),
(5, 'CGB001', 'PURCHASE', '2020-01-01', 20, 18, 'CC004', 0, 0),
(6, 'WXB001', 'MAINTENANCE', '2020-01-01', 50, 48, 'CC005', 0, 1),
(7, 'RLZYB001', 'HR', '2020-01-01', 12, 11, 'CC006', 0, 0),
(8, 'CMYD001', 'PRODUCTION', '2020-01-01', 80, 75, 'CC007', 1, 1),
(9, 'CMED001', 'PRODUCTION', '2020-01-01', 80, 72, 'CC008', 1, 1),
(10, 'JJD001', 'PRODUCTION', '2020-01-01', 40, 38, 'CC009', 1, 1);

-- ============================================
-- 10. 人力资源 - 员工数据（需要先有用户数据）
-- ============================================
-- 注意：这里假设已经有对应的用户数据，实际使用时需要先创建用户
-- 这里只插入员工扩展信息，user_id需要对应sys_user表中的user_id

-- ============================================
-- 数据导入完成提示
-- ============================================
SELECT '示例数据导入完成！' AS message;
SELECT '已导入：' AS summary;
SELECT '  - 10个部门' AS dept_count;
SELECT '  - 15个资产设备' AS asset_count;
SELECT '  - 8个采购订单' AS purchase_count;
SELECT '  - 7个维修工单' AS repair_count;
SELECT '  - 20个库存物资' AS inventory_count;
SELECT '  - 6个预警信息' AS warning_count;
SELECT '  - 5个客户' AS customer_count;
SELECT '  - 6个供应商' AS supplier_count;
SELECT '  - 9个部门扩展信息' AS hr_dept_count;