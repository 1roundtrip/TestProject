-- 用户登录问题诊断脚本
-- 用于检查数据库中用户数据是否存在以及配置是否正确

-- ============================================
-- 1. 检查数据库和表是否存在
-- ============================================
SELECT DATABASE() as current_database;

SHOW TABLES LIKE 'sys_user';

-- ============================================
-- 2. 检查表结构
-- ============================================
DESCRIBE sys_user;

-- ============================================
-- 3. 检查所有用户数据
-- ============================================
SELECT 
    user_id,
    username,
    LENGTH(password) as password_length,
    SUBSTRING(password, 1, 20) as password_preview,
    status,
    nick_name,
    create_time,
    update_time
FROM sys_user
ORDER BY user_id;

-- ============================================
-- 4. 检查admin用户是否存在
-- ============================================
SELECT 
    user_id,
    username,
    password,
    status,
    nick_name,
    CASE 
        WHEN password = 'admin123' THEN '✓ 密码正确（明文）'
        WHEN password LIKE '$2a$%' THEN '✗ 密码是BCrypt加密格式，需要改为明文'
        ELSE '? 密码格式未知'
    END as password_status,
    CASE 
        WHEN status = '0' THEN '✓ 用户已启用'
        WHEN status = '1' THEN '✗ 用户已禁用'
        ELSE '? 状态未知'
    END as status_check
FROM sys_user 
WHERE username = 'admin'
   OR username = 'ADMIN'
   OR username = 'Admin';

-- ============================================
-- 5. 检查用户数量统计
-- ============================================
SELECT 
    COUNT(*) as total_users,
    SUM(CASE WHEN status = '0' THEN 1 ELSE 0 END) as enabled_users,
    SUM(CASE WHEN status = '1' THEN 1 ELSE 0 END) as disabled_users,
    SUM(CASE WHEN username = 'admin' THEN 1 ELSE 0 END) as admin_count
FROM sys_user;

-- ============================================
-- 6. 如果admin用户不存在，创建用户
-- ============================================
-- 取消下面的注释来执行创建用户操作
/*
INSERT INTO sys_user (username, password, nick_name, status, create_time, update_time)
SELECT 'admin', 'admin123', '系统管理员', '0', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'admin');
*/

-- ============================================
-- 7. 如果admin用户存在但密码不对，更新密码
-- ============================================
-- 取消下面的注释来执行更新密码操作
/*
UPDATE sys_user 
SET password = 'admin123', 
    status = '0',
    nick_name = COALESCE(nick_name, '系统管理员'),
    update_time = NOW()
WHERE username = 'admin';
*/

-- ============================================
-- 8. 最终验证
-- ============================================
SELECT 
    '验证结果' as check_type,
    CASE 
        WHEN EXISTS (SELECT 1 FROM sys_user WHERE username = 'admin') THEN '✓ admin用户存在'
        ELSE '✗ admin用户不存在'
    END as user_exists,
    CASE 
        WHEN EXISTS (SELECT 1 FROM sys_user WHERE username = 'admin' AND password = 'admin123') THEN '✓ 密码正确'
        ELSE '✗ 密码不正确'
    END as password_correct,
    CASE 
        WHEN EXISTS (SELECT 1 FROM sys_user WHERE username = 'admin' AND status = '0') THEN '✓ 用户已启用'
        ELSE '✗ 用户未启用'
    END as user_enabled;

