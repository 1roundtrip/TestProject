-- ============================================
-- 将角色表中的英文数据更新为中文
-- ============================================
-- 说明：更新角色名称、角色标识和备注为中文

USE coal_erp;

-- 1. 查看当前角色数据
SELECT role_id, role_name, role_key, remark, status
FROM sys_role
ORDER BY role_id;

-- 2. 更新超级管理员角色
UPDATE sys_role
SET 
    role_name = '超级管理员',
    role_key = 'admin',
    remark = '系统超级管理员，拥有所有权限'
WHERE role_name = 'Super Admin' OR role_key = 'admin';

-- 3. 更新管理员角色（如果存在）
UPDATE sys_role
SET 
    role_name = '管理员',
    role_key = 'manager',
    remark = '系统管理员，拥有大部分权限'
WHERE role_name = 'Admin' AND role_key != 'admin';

-- 4. 更新普通用户角色（如果存在）
UPDATE sys_role
SET 
    role_name = '普通用户',
    role_key = 'user',
    remark = '普通用户，拥有基本权限'
WHERE role_name = 'User' OR role_name = 'user';

-- 5. 更新访客角色（如果存在）
UPDATE sys_role
SET 
    role_name = '访客',
    role_key = 'guest',
    remark = '访客用户，拥有只读权限'
WHERE role_name = 'Guest' OR role_name = 'guest';

-- 6. 更新其他可能的英文角色名称
-- 如果角色名称包含常见英文单词，也进行更新
UPDATE sys_role
SET role_name = CASE 
    WHEN role_name LIKE '%Super%' OR role_name LIKE '%super%' THEN '超级管理员'
    WHEN role_name LIKE '%Admin%' OR role_name LIKE '%admin%' THEN '管理员'
    WHEN role_name LIKE '%User%' OR role_name LIKE '%user%' THEN '普通用户'
    WHEN role_name LIKE '%Guest%' OR role_name LIKE '%guest%' THEN '访客'
    ELSE role_name
END
WHERE role_name REGEXP '[A-Za-z]' 
AND role_name NOT IN ('超级管理员', '管理员', '普通用户', '访客');

-- 7. 更新角色标识为中文（如果标识是英文单词）
UPDATE sys_role
SET role_key = CASE 
    WHEN role_key = 'admin' OR role_key = 'super_admin' THEN 'admin'
    WHEN role_key = 'manager' THEN 'manager'
    WHEN role_key = 'user' THEN 'user'
    WHEN role_key = 'guest' THEN 'guest'
    ELSE role_key
END
WHERE role_key REGEXP '[A-Za-z]';

-- 8. 更新备注为中文（如果备注是英文或为空）
UPDATE sys_role
SET remark = CASE 
    WHEN role_name = '超级管理员' AND (remark IS NULL OR remark = '' OR remark NOT REGEXP '[\\u4e00-\\u9fa5]') THEN '系统超级管理员，拥有所有权限'
    WHEN role_name = '管理员' AND (remark IS NULL OR remark = '' OR remark NOT REGEXP '[\\u4e00-\\u9fa5]') THEN '系统管理员，拥有大部分权限'
    WHEN role_name = '普通用户' AND (remark IS NULL OR remark = '' OR remark NOT REGEXP '[\\u4e00-\\u9fa5]') THEN '普通用户，拥有基本权限'
    WHEN role_name = '访客' AND (remark IS NULL OR remark = '' OR remark NOT REGEXP '[\\u4e00-\\u9fa5]') THEN '访客用户，拥有只读权限'
    ELSE remark
END
WHERE remark IS NULL OR remark = '' OR remark NOT REGEXP '[\\u4e00-\\u9fa5]';

-- 9. 验证更新结果
SELECT role_id, role_name, role_key, remark, status, create_time
FROM sys_role
ORDER BY role_id;

-- 10. 显示更新统计
SELECT 
    COUNT(*) AS total_roles,
    SUM(CASE WHEN role_name REGEXP '[\\u4e00-\\u9fa5]' THEN 1 ELSE 0 END) AS chinese_names,
    SUM(CASE WHEN remark REGEXP '[\\u4e00-\\u9fa5]' OR remark IS NULL OR remark = '' THEN 1 ELSE 0 END) AS chinese_remarks
FROM sys_role;

