-- 登录测试脚本
-- 用于验证数据库中的用户数据

-- 1. 查看admin用户完整信息
SELECT 
    user_id,
    username,
    password,
    status,
    nick_name,
    email,
    phone,
    create_time
FROM sys_user 
WHERE username = 'admin';

-- 2. 如果用户不存在，创建admin用户
INSERT INTO sys_user (username, password, status, create_time)
SELECT 'admin', 'admin123', '0', NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'admin');

-- 3. 确保密码和状态正确
UPDATE sys_user 
SET password = 'admin123', status = '0'
WHERE username = 'admin';

-- 4. 验证更新结果
SELECT 
    user_id,
    username,
    password,
    status,
    LENGTH(password) as password_length,
    CASE 
        WHEN password = 'admin123' THEN '密码正确'
        ELSE '密码不匹配'
    END as password_check
FROM sys_user 
WHERE username = 'admin';

