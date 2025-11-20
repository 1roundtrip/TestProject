-- 更新管理员密码为明文（内网环境简化版本）
-- 注意：仅适用于内网环境，生产环境建议使用密码加密

-- 更新admin用户密码为明文 admin123
UPDATE sys_user 
SET password = 'admin123'
WHERE username = 'admin';

-- 验证更新结果
SELECT username, password, status FROM sys_user WHERE username = 'admin';

