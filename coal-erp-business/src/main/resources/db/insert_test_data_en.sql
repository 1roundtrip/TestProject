-- Test data for Coal ERP System

-- 1. Insert asset test data
INSERT INTO `asset` (
    `asset_code`, `asset_name`, `asset_type`, `category`, 
    `manufacturer`, `model`, `serial_number`, 
    `purchase_date`, `purchase_price`, 
    `status`, `location`, `dept_id`, 
    `is_explosion_proof`, `explosion_proof_expire_date`, 
    `create_time`, `update_time`, `remark`
) VALUES
('ASSET001', 'Explosion-proof Motor', 'Electrical', 'Motor', 'Shandong Motor', 'YBK2-280M-4', 'SN2024001', '2024-01-15', 15000.00, '0', 'Workshop 1', 1, '1', '2025-12-31', NOW(), NOW(), 'Normal operation'),
('ASSET002', 'Explosion-proof Switch', 'Electrical', 'Switch', 'Shanghai Electric', 'BXM-100', 'SN2024002', '2024-02-20', 5000.00, '0', 'Workshop 2', 1, '1', '2025-11-30', NOW(), NOW(), 'Explosion-proof device');

-- 2. Insert warning alert test data
INSERT INTO `warning_alert` (
    `alert_type`, `alert_level`, `asset_id`, `asset_code`, `asset_name`,
    `alert_title`, `alert_content`, `expire_date`, `days_remaining`,
    `status`, `create_user_id`, `create_time`, `update_time`, `remark`
) VALUES
('EXPLOSION_PROOF', 'RED', 
 (SELECT asset_id FROM asset WHERE asset_code = 'ASSET002'),
 'ASSET002', 'Explosion-proof Switch',
 'Certificate Expiration Alert', 'Explosion-proof certificate will expire in 5 days!', 
 DATE_ADD(NOW(), INTERVAL 5 DAY), 5,
 '0', 1, NOW(), NOW(), 'Urgent alert');