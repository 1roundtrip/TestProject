-- 智慧煤矿ERP系统测试数据脚本
-- 用于初始化各模块的测试数据

-- 1. 清空现有测试数据（可选，谨慎使用）
-- TRUNCATE TABLE asset;
-- TRUNCATE TABLE warning_alert;

-- 2. 插入资产数据
INSERT INTO `asset` (
    `asset_code`, `asset_name`, `asset_type`, `category`, 
    `manufacturer`, `model`, `serial_number`, 
    `purchase_date`, `purchase_price`, 
    `status`, `location`, `dept_id`, 
    `is_explosion_proof`, `explosion_proof_expire_date`, 
    `create_time`, `update_time`, `remark`
) VALUES
-- 正常设备
('ASSET001', '防爆电机', '电气设备', '电机类', '山东防爆电机厂', 'YBK2-280M-4', 'SN2024001', '2024-01-15', 15000.00, '0', '1号车间', 1, '1', '2025-12-31', NOW(), NOW(), '正常运行的防爆电机'),
('ASSET002', '防爆开关', '电气设备', '开关类', '上海防爆电器', 'BXM-100', 'SN2024002', '2024-02-20', 5000.00, '0', '2号车间', 1, '1', '2025-11-30', NOW(), NOW(), '防爆开关设备'),
('ASSET003', '矿用提升机', '机械设备', '提升设备', '河南矿山机械', 'JK-2.5', 'SN2024003', '2024-03-10', 250000.00, '0', '主井口', 1, '0', NULL, NOW(), NOW(), '主提升设备'),
('ASSET004', '通风机', '机械设备', '通风设备', '山西风机厂', 'FBD-6.3', 'SN2024004', '2024-01-25', 80000.00, '0', '通风机房', 1, '0', NULL, NOW(), NOW(), '主通风设备'),
('ASSET005', '防爆照明灯', '电气设备', '照明类', '江苏防爆照明', 'LED-100W', 'SN2024005', '2024-04-05', 800.00, '0', '井下巷道', 1, '1', '2025-10-15', NOW(), NOW(), '防爆LED照明'),
('ASSET006', '防爆电话', '通信设备', '通信类', '北京矿用通信', 'KTW125', 'SN2024006', '2024-02-18', 3000.00, '0', '调度室', 1, '1', '2025-09-20', NOW(), NOW(), '防爆通信设备'),
('ASSET007', '输送带', '机械设备', '输送设备', '河北输送带厂', 'DT-800', 'SN2024007', '2024-03-22', 120000.00, '0', '运输巷道', 1, '0', NULL, NOW(), NOW(), '主运输带'),
('ASSET008', '防爆监控摄像头', '监控设备', '监控类', '深圳安防科技', 'IPC-200', 'SN2024008', '2024-04-12', 5000.00, '0', '井下监控点', 1, '1', '2025-08-10', NOW(), NOW(), '防爆监控设备'),
('ASSET009', '空压机', '机械设备', '空压设备', '浙江空压机厂', 'LG-55', 'SN2024009', '2024-01-30', 45000.00, '0', '空压机房', 1, '0', NULL, NOW(), NOW(), '主空压设备'),
('ASSET010', '防爆传感器', '检测设备', '传感器类', '北京传感器厂', 'GJC4', 'SN2024010', '2024-05-08', 2000.00, '0', '井下检测点', 1, '1', '2025-07-25', NOW(), NOW(), '瓦斯检测传感器'),

-- 维修中设备
('ASSET011', '防爆电机（维修）', '电气设备', '电机类', '山东防爆电机厂', 'YBK2-280M-4', 'SN2024011', '2023-12-10', 15000.00, '1', '维修车间', 1, '1', '2024-12-31', NOW(), NOW(), '正在维修中'),
('ASSET012', '提升机（维修）', '机械设备', '提升设备', '河南矿山机械', 'JK-2.5', 'SN2024012', '2023-11-15', 250000.00, '1', '维修车间', 1, '0', NULL, NOW(), NOW(), '定期维护保养'),
('ASSET013', '通风机（维修）', '机械设备', '通风设备', '山西风机厂', 'FBD-6.3', 'SN2024013', '2023-10-20', 80000.00, '1', '维修车间', 1, '0', NULL, NOW(), NOW(), '故障维修'),

-- 报废设备
('ASSET014', '旧防爆开关', '电气设备', '开关类', '上海防爆电器', 'BXM-100', 'SN2019001', '2019-05-10', 5000.00, '2', '报废仓库', 1, '1', '2022-12-31', NOW(), NOW(), '已报废，待处理'),
('ASSET015', '旧输送带', '机械设备', '输送设备', '河北输送带厂', 'DT-800', 'SN2018001', '2018-08-15', 120000.00, '2', '报废仓库', 1, '0', NULL, NOW(), NOW(), '使用年限到期，已报废'),

-- 即将到期的防爆设备（用于预警测试）
('ASSET016', '防爆电机（即将到期）', '电气设备', '电机类', '山东防爆电机厂', 'YBK2-280M-4', 'SN2024016', '2023-06-01', 15000.00, '0', '1号车间', 1, '1', DATE_ADD(NOW(), INTERVAL 25 DAY), NOW(), NOW(), '防爆证书即将到期'),
('ASSET017', '防爆开关（即将到期）', '电气设备', '开关类', '上海防爆电器', 'BXM-100', 'SN2024017', '2023-07-10', 5000.00, '0', '2号车间', 1, '1', DATE_ADD(NOW(), INTERVAL 15 DAY), NOW(), NOW(), '防爆证书即将到期'),
('ASSET018', '防爆照明灯（即将到期）', '电气设备', '照明类', '江苏防爆照明', 'LED-100W', 'SN2024018', '2023-08-20', 800.00, '0', '井下巷道', 1, '1', DATE_ADD(NOW(), INTERVAL 5 DAY), NOW(), NOW(), '防爆证书即将到期');

-- 3. 插入预警数据
INSERT INTO `warning_alert` (
    `alert_type`, `alert_level`, `asset_id`, `asset_code`, `asset_name`,
    `alert_title`, `alert_content`, `expire_date`, `days_remaining`,
    `status`, `create_user_id`, `create_time`, `update_time`, `remark`
) VALUES
-- 红色预警（紧急，5天内到期）
('EXPLOSION_PROOF', 'RED', 
 (SELECT asset_id FROM asset WHERE asset_code = 'ASSET018'),
 'ASSET018', '防爆照明灯（即将到期）',
 '防爆证书即将到期-红色预警', '防爆照明灯（ASSET018）的防爆证书将在5天内到期，请立即处理！', 
 DATE_ADD(NOW(), INTERVAL 5 DAY), 5,
 '0', 1, NOW(), NOW(), '紧急预警'),

-- 橙色预警（重要，15天内到期）
('EXPLOSION_PROOF', 'ORANGE',
 (SELECT asset_id FROM asset WHERE asset_code = 'ASSET017'),
 'ASSET017', '防爆开关（即将到期）',
 '防爆证书即将到期-橙色预警', '防爆开关（ASSET017）的防爆证书将在15天内到期，请尽快处理！',
 DATE_ADD(NOW(), INTERVAL 15 DAY), 15,
 '0', 1, NOW(), NOW(), '重要预警'),

-- 黄色预警（提醒，25天内到期）
('EXPLOSION_PROOF', 'YELLOW',
 (SELECT asset_id FROM asset WHERE asset_code = 'ASSET016'),
 'ASSET016', '防爆电机（即将到期）',
 '防爆证书即将到期-黄色预警', '防爆电机（ASSET016）的防爆证书将在25天内到期，请提前准备！',
 DATE_ADD(NOW(), INTERVAL 25 DAY), 25,
 '0', 1, NOW(), NOW(), '提醒预警'),

-- 已处理的预警（用于测试）
('EXPLOSION_PROOF', 'YELLOW',
 (SELECT asset_id FROM asset WHERE asset_code = 'ASSET001'),
 'ASSET001', '防爆电机',
 '防爆证书即将到期-已处理', '此预警已处理完成',
 DATE_ADD(NOW(), INTERVAL 30 DAY), 30,
 '1', 1, DATE_SUB(NOW(), INTERVAL 10 DAY), NOW(), '已处理');

-- 4. 验证数据
SELECT '资产数据统计' AS info;
SELECT status, COUNT(*) AS count FROM asset GROUP BY status;

SELECT '预警数据统计' AS info;
SELECT alert_level, status, COUNT(*) AS count FROM warning_alert GROUP BY alert_level, status;

SELECT '防爆设备统计' AS info;
SELECT is_explosion_proof, COUNT(*) AS count FROM asset GROUP BY is_explosion_proof;

