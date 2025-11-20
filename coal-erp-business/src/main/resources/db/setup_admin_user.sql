-- ============================================
-- 设置 admin 用户为超级管理员（密码：admin123）
-- ============================================
-- 说明：确保账户名为 admin，密码为 admin123 的用户拥有系统的最高级权限

USE coal_erp;

-- 1. 查看当前 admin 用户信息
SELECT user_id, username, status, create_time
FROM sys_user
WHERE username = 'admin';

-- 2. 创建或更新 admin 用户
-- 注意：后端使用明文密码编码器（PlainPasswordEncoder），密码直接存储为明文
-- 如果用户已存在，更新其信息和密码；如果不存在，创建新用户
INSERT INTO sys_user (username, password, nick_name, status, create_time, update_time)
VALUES ('admin', 'admin123', '系统管理员', '0', NOW(), NOW())
ON DUPLICATE KEY UPDATE
    password = 'admin123',
    nick_name = '系统管理员',
    status = '0',
    update_time = NOW();

-- 注意：密码直接设置为 'admin123'（明文），因为后端使用 PlainPasswordEncoder

-- 3. 获取 admin 用户ID
SET @admin_user_id = (
    SELECT user_id FROM sys_user WHERE username = 'admin' LIMIT 1
);

SELECT CONCAT('admin 用户ID: ', @admin_user_id) AS info;

-- 4. 查找或创建超级管理员角色
INSERT INTO sys_role (role_name, role_key, role_sort, status, create_time, update_time, remark)
SELECT '超级管理员', 'admin', 1, '0', NOW(), NOW(), '系统超级管理员，拥有所有权限'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_role WHERE role_key = 'admin' OR role_name = '超级管理员'
);

-- 5. 获取超级管理员角色ID
SET @admin_role_id = (
    SELECT role_id FROM sys_role 
    WHERE role_key = 'admin' OR role_name = '超级管理员'
    ORDER BY role_id ASC
    LIMIT 1
);

SELECT CONCAT('超级管理员角色ID: ', @admin_role_id) AS info;

-- 6. 删除 admin 用户的所有现有角色关联
DELETE FROM sys_user_role WHERE user_id = @admin_user_id;

-- 7. 将 admin 用户关联到超级管理员角色
INSERT INTO sys_user_role (user_id, role_id)
VALUES (@admin_user_id, @admin_role_id)
ON DUPLICATE KEY UPDATE role_id = @admin_role_id;

-- 8. 确保超级管理员角色拥有所有菜单权限
-- 删除超级管理员角色的所有现有菜单权限（重新分配）
DELETE FROM sys_role_menu WHERE role_id = @admin_role_id;

-- 9. 为超级管理员角色分配所有菜单权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT DISTINCT @admin_role_id, m.menu_id
FROM sys_menu m
WHERE m.status = '0' OR m.status IS NULL;

-- 10. 确保 admin 用户状态为正常
UPDATE sys_user 
SET status = '0' 
WHERE username = 'admin' AND (status != '0' OR status IS NULL);

-- 11. 验证 admin 用户的角色关联
SELECT u.user_id, u.username, u.status AS user_status, r.role_id, r.role_name, r.role_key, r.status AS role_status
FROM sys_user u
INNER JOIN sys_user_role ur ON u.user_id = ur.user_id
INNER JOIN sys_role r ON ur.role_id = r.role_id
WHERE u.username = 'admin';

-- 12. 验证超级管理员角色拥有的权限数量
SELECT 
    r.role_name,
    COUNT(rm.menu_id) AS permission_count
FROM sys_role r
LEFT JOIN sys_role_menu rm ON r.role_id = rm.role_id
WHERE r.role_id = @admin_role_id
GROUP BY r.role_id, r.role_name;

-- 13. 显示 admin 用户拥有的所有权限（前30个）
SELECT DISTINCT
    u.username,
    m.menu_id,
    m.menu_name,
    m.perms,
    m.menu_type
FROM sys_user u
INNER JOIN sys_user_role ur ON u.user_id = ur.user_id
INNER JOIN sys_role_menu rm ON ur.role_id = rm.role_id
INNER JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE u.username = 'admin'
AND m.perms IS NOT NULL
AND m.perms != ''
ORDER BY m.menu_id
LIMIT 30;

-- 14. 最终验证：显示 admin 用户的完整信息
SELECT 
    u.user_id,
    u.username,
    u.nick_name,
    u.status AS user_status,
    r.role_id,
    r.role_name,
    r.role_key,
    r.status AS role_status,
    COUNT(rm.menu_id) AS permission_count
FROM sys_user u
INNER JOIN sys_user_role ur ON u.user_id = ur.user_id
INNER JOIN sys_role r ON ur.role_id = r.role_id
LEFT JOIN sys_role_menu rm ON r.role_id = rm.role_id
WHERE u.username = 'admin'
GROUP BY u.user_id, u.username, u.nick_name, u.status, r.role_id, r.role_name, r.role_key, r.status;

-- ============================================
-- 重要提示：
-- ============================================
-- 1. 密码设置：密码直接设置为 'admin123'（明文）
--    因为后端使用 PlainPasswordEncoder（明文密码编码器）
-- 
-- 2. 执行此脚本后，需要：
--    a) 退出当前登录
--    b) 使用 admin / admin123 重新登录
--    c) 验证是否拥有所有系统管理权限
-- 
-- 3. 如果登录时密码不正确，请检查：
--    - 确认后端使用的是 PlainPasswordEncoder
--    - 确认数据库中的密码字段值确实是 'admin123'
--    - 查看后端日志中的密码验证信息

