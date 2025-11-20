-- 初始化管理员用户脚本
-- 用于创建或更新默认管理员账户

-- 1. 检查用户是否存在
SELECT 
    user_id,
    username,
    password,
    status,
    nick_name,
    create_time
FROM sys_user 
WHERE username = 'admin';

-- 2. 如果用户不存在，创建admin用户（明文密码：admin123）
INSERT INTO sys_user (username, password, nick_name, status, create_time, update_time)
SELECT 'admin', 'admin123', '系统管理员', '0', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'admin');

-- 3. 如果用户已存在，更新密码为明文（admin123）并确保状态正常
UPDATE sys_user 
SET password = 'admin123', 
    status = '0',
    nick_name = COALESCE(nick_name, '系统管理员'),
    update_time = NOW()
WHERE username = 'admin';

-- 4. 验证结果
SELECT 
    user_id,
    username,
    password,
    status,
    nick_name,
    CASE 
        WHEN password = 'admin123' THEN '✓ 密码正确（明文）'
        ELSE '✗ 密码不匹配'
    END as password_check,
    CASE 
        WHEN status = '0' THEN '✓ 用户已启用'
        ELSE '✗ 用户已禁用'
    END as status_check,
    create_time,
    update_time
FROM sys_user 
WHERE username = 'admin';

-- 5. 如果角色表存在，确保admin用户有超级管理员角色
-- 首先检查角色是否存在
SELECT role_id, role_name, role_key, status 
FROM sys_role 
WHERE role_key = 'admin' OR role_name = '超级管理员';

-- 如果角色不存在，创建超级管理员角色
INSERT INTO sys_role (role_name, role_key, role_sort, status, create_time, update_time)
SELECT '超级管理员', 'admin', 1, '0', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE role_key = 'admin' OR role_name = '超级管理员');

-- 关联用户和角色
INSERT INTO sys_user_role (user_id, role_id)
SELECT u.user_id, r.role_id
FROM sys_user u, sys_role r
WHERE u.username = 'admin' 
  AND (r.role_key = 'admin' OR r.role_name = '超级管理员')
  AND NOT EXISTS (
      SELECT 1 FROM sys_user_role ur 
      WHERE ur.user_id = u.user_id AND ur.role_id = r.role_id
  );

-- 6. 最终验证：查看admin用户的完整信息
SELECT 
    u.user_id,
    u.username,
    u.password,
    u.status as user_status,
    u.nick_name,
    r.role_name,
    r.role_key,
    r.status as role_status
FROM sys_user u
LEFT JOIN sys_user_role ur ON u.user_id = ur.user_id
LEFT JOIN sys_role r ON ur.role_id = r.role_id
WHERE u.username = 'admin';

