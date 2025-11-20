-- 快速创建管理员用户脚本
-- 执行此脚本可以确保admin用户存在且密码正确

-- 方法1：如果用户不存在，创建用户
INSERT INTO sys_user (username, password, nick_name, status, create_time, update_time)
SELECT 'admin', 'admin123', '系统管理员', '0', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'admin');

-- 方法2：如果用户已存在，更新密码和状态
UPDATE sys_user 
SET password = 'admin123', 
    status = '0',
    nick_name = COALESCE(nick_name, '系统管理员'),
    update_time = NOW()
WHERE username = 'admin';

-- 验证：查看用户信息
SELECT 
    user_id,
    username,
    password,
    status,
    nick_name,
    create_time
FROM sys_user 
WHERE username = 'admin';

