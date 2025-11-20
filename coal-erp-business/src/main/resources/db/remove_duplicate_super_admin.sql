-- ============================================
-- 删除重复的超级管理员角色
-- ============================================
-- 说明：保留一个超级管理员角色，删除其他重复的
-- 如果其他角色关联了用户，会将用户关联迁移到保留的角色上

USE coal_erp;

-- 1. 查找所有超级管理员角色
SELECT role_id, role_name, role_key, status, create_time
FROM sys_role
WHERE role_name = 'Super Admin' OR role_key = 'admin'
ORDER BY role_id;

-- 2. 选择保留的角色ID（选择最小的role_id，或者创建时间最早的）
SET @keep_role_id = (
    SELECT role_id 
    FROM sys_role 
    WHERE role_name = 'Super Admin' OR role_key = 'admin'
    ORDER BY role_id ASC
    LIMIT 1
);

SELECT CONCAT('将保留角色ID: ', @keep_role_id) AS info;

-- 3. 查找需要删除的角色ID（除了保留的那个）
SELECT role_id, role_name, role_key
FROM sys_role
WHERE (role_name = 'Super Admin' OR role_key = 'admin')
AND role_id != @keep_role_id;

-- 4. 将其他超级管理员角色的用户关联迁移到保留的角色
-- 先查找需要迁移的用户
SELECT ur.user_id, ur.role_id, u.username
FROM sys_user_role ur
INNER JOIN sys_user u ON ur.user_id = u.user_id
INNER JOIN sys_role r ON ur.role_id = r.role_id
WHERE (r.role_name = 'Super Admin' OR r.role_key = 'admin')
AND ur.role_id != @keep_role_id;

-- 5. 迁移用户角色关联
-- 对于每个需要删除的角色，将其用户关联迁移到保留的角色
-- 如果用户已经关联了保留的角色，则跳过
INSERT INTO sys_user_role (user_id, role_id)
SELECT DISTINCT ur.user_id, @keep_role_id
FROM sys_user_role ur
INNER JOIN sys_role r ON ur.role_id = r.role_id
WHERE (r.role_name = 'Super Admin' OR r.role_key = 'admin')
AND ur.role_id != @keep_role_id
AND NOT EXISTS (
    SELECT 1 FROM sys_user_role ur2 
    WHERE ur2.user_id = ur.user_id 
    AND ur2.role_id = @keep_role_id
);

-- 6. 删除其他超级管理员角色的菜单权限关联（可选，因为要删除角色了）
DELETE rm FROM sys_role_menu rm
INNER JOIN sys_role r ON rm.role_id = r.role_id
WHERE (r.role_name = 'Super Admin' OR r.role_key = 'admin')
AND rm.role_id != @keep_role_id;

-- 7. 删除其他超级管理员角色的用户关联
DELETE ur FROM sys_user_role ur
INNER JOIN sys_role r ON ur.role_id = r.role_id
WHERE (r.role_name = 'Super Admin' OR r.role_key = 'admin')
AND ur.role_id != @keep_role_id;

-- 8. 删除其他超级管理员角色
DELETE FROM sys_role
WHERE (role_name = 'Super Admin' OR role_key = 'admin')
AND role_id != @keep_role_id;

-- 9. 验证结果：应该只剩下一个超级管理员角色
SELECT role_id, role_name, role_key, status, create_time
FROM sys_role
WHERE role_name = 'Super Admin' OR role_key = 'admin';

-- 10. 验证用户关联：显示所有关联了超级管理员角色的用户
SELECT u.user_id, u.username, r.role_id, r.role_name, r.role_key
FROM sys_user u
INNER JOIN sys_user_role ur ON u.user_id = ur.user_id
INNER JOIN sys_role r ON ur.role_id = r.role_id
WHERE r.role_name = 'Super Admin' OR r.role_key = 'admin'
ORDER BY u.user_id;

