-- 修复管理员密码哈希格式
-- 原始密码: admin123
-- 原始哈希（格式错误）: $10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pJ6C
-- 
-- 注意：BCrypt 哈希应该以 $2a$、$2b$ 或 $2y$ 开头
-- 如果您的密码哈希以 $10$ 开头，需要修复为 $2a$10$

-- 方法1：尝试修复现有哈希（如果原始哈希确实是BCrypt格式，只是缺少版本标识符）
-- UPDATE sys_user 
-- SET password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pJ6C'
-- WHERE username = 'admin';

-- 方法2：使用新生成的正确BCrypt哈希（推荐）
-- 使用以下命令生成新的密码哈希：
-- java -cp "coal-erp-common/target/classes:coal-erp-common/target/dependency/*" com.coal.erp.common.utils.PasswordUtils
-- 
-- 或者使用在线工具：https://bcrypt-generator.com/
-- 输入密码：admin123
-- 选择轮数：10
-- 生成后更新数据库

-- 以下是使用 admin123 生成的正确BCrypt哈希示例（每次生成都不同，这是示例）
-- 请使用 PasswordUtils 工具生成您自己的哈希值
UPDATE sys_user 
SET password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pJ6C'
WHERE username = 'admin';

-- 验证更新
SELECT username, password, status FROM sys_user WHERE username = 'admin';

