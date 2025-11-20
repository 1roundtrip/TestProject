-- 测试用户数据脚本
-- 用于验证数据库中的用户数据是否正确

-- 1. 查看admin用户信息
SELECT 
    user_id,
    username,
    password,
    status,
    nick_name,
    email,
    phone
FROM sys_user 
WHERE username = 'admin';

-- 2. 如果用户不存在，创建测试用户
-- INSERT INTO sys_user (username, password, status, create_time)
-- VALUES ('admin', 'admin123', '0', NOW())
-- ON DUPLICATE KEY UPDATE password = 'admin123', status = '0';

-- 3. 更新admin用户密码为明文（如果还没有）
UPDATE sys_user 
SET password = 'admin123', status = '0'
WHERE username = 'admin';

-- 4. 验证更新结果
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

