-- ============================================
-- 创建模块负责人用户脚本
-- ============================================
-- 说明：为5个模块负责人创建用户账户并分配对应角色
-- 注意：系统使用明文密码，密码直接存储为明文
-- ============================================

USE coal_erp;

-- ============================================
-- 创建资产中心负责人用户
-- ============================================

-- 1. 创建或更新资产中心负责人用户
INSERT INTO sys_user (username, password, nick_name, status, create_time, update_time, remark)
VALUES ('asset_manager', '123456', '资产中心负责人', '0', NOW(), NOW(), '资产中心负责人账户')
ON DUPLICATE KEY UPDATE
    password = '123456',
    nick_name = '资产中心负责人',
    status = '0',
    update_time = NOW();

-- 2. 获取用户ID和角色ID
SET @asset_user_id = (SELECT user_id FROM sys_user WHERE username = 'asset_manager' LIMIT 1);
SET @asset_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'asset_manager' LIMIT 1);

-- 3. 删除用户现有角色关联
DELETE FROM sys_user_role WHERE user_id = @asset_user_id;

-- 4. 关联用户到资产中心负责人角色
INSERT INTO sys_user_role (user_id, role_id)
SELECT @asset_user_id, @asset_role_id
WHERE @asset_user_id IS NOT NULL AND @asset_role_id IS NOT NULL
ON DUPLICATE KEY UPDATE role_id = @asset_role_id;

-- ============================================
-- 创建采购中心负责人用户
-- ============================================

-- 1. 创建或更新采购中心负责人用户
INSERT INTO sys_user (username, password, nick_name, status, create_time, update_time, remark)
VALUES ('purchase_manager', '123456', '采购中心负责人', '0', NOW(), NOW(), '采购中心负责人账户')
ON DUPLICATE KEY UPDATE
    password = '123456',
    nick_name = '采购中心负责人',
    status = '0',
    update_time = NOW();

-- 2. 获取用户ID和角色ID
SET @purchase_user_id = (SELECT user_id FROM sys_user WHERE username = 'purchase_manager' LIMIT 1);
SET @purchase_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'purchase_manager' LIMIT 1);

-- 3. 删除用户现有角色关联
DELETE FROM sys_user_role WHERE user_id = @purchase_user_id;

-- 4. 关联用户到采购中心负责人角色
INSERT INTO sys_user_role (user_id, role_id)
SELECT @purchase_user_id, @purchase_role_id
WHERE @purchase_user_id IS NOT NULL AND @purchase_role_id IS NOT NULL
ON DUPLICATE KEY UPDATE role_id = @purchase_role_id;

-- ============================================
-- 创建维修管理负责人用户
-- ============================================

-- 1. 创建或更新维修管理负责人用户
INSERT INTO sys_user (username, password, nick_name, status, create_time, update_time, remark)
VALUES ('maintenance_manager', '123456', '维修管理负责人', '0', NOW(), NOW(), '维修管理负责人账户')
ON DUPLICATE KEY UPDATE
    password = '123456',
    nick_name = '维修管理负责人',
    status = '0',
    update_time = NOW();

-- 2. 获取用户ID和角色ID
SET @maintenance_user_id = (SELECT user_id FROM sys_user WHERE username = 'maintenance_manager' LIMIT 1);
SET @maintenance_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'maintenance_manager' LIMIT 1);

-- 3. 删除用户现有角色关联
DELETE FROM sys_user_role WHERE user_id = @maintenance_user_id;

-- 4. 关联用户到维修管理负责人角色
INSERT INTO sys_user_role (user_id, role_id)
SELECT @maintenance_user_id, @maintenance_role_id
WHERE @maintenance_user_id IS NOT NULL AND @maintenance_role_id IS NOT NULL
ON DUPLICATE KEY UPDATE role_id = @maintenance_role_id;

-- ============================================
-- 创建库存中心负责人用户
-- ============================================

-- 1. 创建或更新库存中心负责人用户
INSERT INTO sys_user (username, password, nick_name, status, create_time, update_time, remark)
VALUES ('inventory_manager', '123456', '库存中心负责人', '0', NOW(), NOW(), '库存中心负责人账户')
ON DUPLICATE KEY UPDATE
    password = '123456',
    nick_name = '库存中心负责人',
    status = '0',
    update_time = NOW();

-- 2. 获取用户ID和角色ID
SET @inventory_user_id = (SELECT user_id FROM sys_user WHERE username = 'inventory_manager' LIMIT 1);
SET @inventory_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'inventory_manager' LIMIT 1);

-- 3. 删除用户现有角色关联
DELETE FROM sys_user_role WHERE user_id = @inventory_user_id;

-- 4. 关联用户到库存中心负责人角色
INSERT INTO sys_user_role (user_id, role_id)
SELECT @inventory_user_id, @inventory_role_id
WHERE @inventory_user_id IS NOT NULL AND @inventory_role_id IS NOT NULL
ON DUPLICATE KEY UPDATE role_id = @inventory_role_id;

-- ============================================
-- 创建预警中心负责人用户
-- ============================================

-- 1. 创建或更新预警中心负责人用户
INSERT INTO sys_user (username, password, nick_name, status, create_time, update_time, remark)
VALUES ('warning_manager', '123456', '预警中心负责人', '0', NOW(), NOW(), '预警中心负责人账户')
ON DUPLICATE KEY UPDATE
    password = '123456',
    nick_name = '预警中心负责人',
    status = '0',
    update_time = NOW();

-- 2. 获取用户ID和角色ID
SET @warning_user_id = (SELECT user_id FROM sys_user WHERE username = 'warning_manager' LIMIT 1);
SET @warning_role_id = (SELECT role_id FROM sys_role WHERE role_key = 'warning_manager' LIMIT 1);

-- 3. 删除用户现有角色关联
DELETE FROM sys_user_role WHERE user_id = @warning_user_id;

-- 4. 关联用户到预警中心负责人角色
INSERT INTO sys_user_role (user_id, role_id)
SELECT @warning_user_id, @warning_role_id
WHERE @warning_user_id IS NOT NULL AND @warning_role_id IS NOT NULL
ON DUPLICATE KEY UPDATE role_id = @warning_role_id;

-- ============================================
-- 验证和显示结果
-- ============================================

-- 1. 显示所有模块负责人用户信息
SELECT 
    u.user_id,
    u.username,
    u.nick_name,
    u.status AS user_status,
    r.role_name,
    r.role_key,
    r.status AS role_status
FROM sys_user u
INNER JOIN sys_user_role ur ON u.user_id = ur.user_id
INNER JOIN sys_role r ON ur.role_id = r.role_id
WHERE u.username IN ('asset_manager', 'purchase_manager', 'maintenance_manager', 'inventory_manager', 'warning_manager')
ORDER BY u.username;

-- 2. 显示所有模块负责人用户的登录信息
SELECT 
    username AS '用户名',
    password AS '密码',
    nick_name AS '昵称',
    status AS '状态',
    CASE 
        WHEN status = '0' THEN '启用'
        ELSE '禁用'
    END AS '状态说明'
FROM sys_user
WHERE username IN ('asset_manager', 'purchase_manager', 'maintenance_manager', 'inventory_manager', 'warning_manager')
ORDER BY username;

-- 3. 显示配置完成信息
SELECT '模块负责人用户创建完成！' AS message;
SELECT '所有用户的默认密码都是：123456' AS password_info;
SELECT '请登录后及时修改密码！' AS reminder;

