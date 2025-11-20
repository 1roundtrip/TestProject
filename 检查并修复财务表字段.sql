-- 检查并修复财务模块表字段的SQL脚本
-- 安全版本：先检查字段是否存在，再决定是否添加

USE coal_erp;

-- ============================================
-- 检查并添加finance_customer表的缺失字段
-- ============================================

-- 检查contact_person字段
SELECT COUNT(*) INTO @contact_person_exists 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'coal_erp' 
  AND TABLE_NAME = 'finance_customer' 
  AND COLUMN_NAME = 'contact_person';

SET @sql = IF(@contact_person_exists = 0,
    'ALTER TABLE finance_customer ADD COLUMN contact_person varchar(50) DEFAULT NULL COMMENT ''联系人''',
    'SELECT ''contact_person字段已存在'' AS message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查credit_level字段
SELECT COUNT(*) INTO @credit_level_exists 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'coal_erp' 
  AND TABLE_NAME = 'finance_customer' 
  AND COLUMN_NAME = 'credit_level';

SET @sql = IF(@credit_level_exists = 0,
    'ALTER TABLE finance_customer ADD COLUMN credit_level varchar(20) DEFAULT NULL COMMENT ''信用等级''',
    'SELECT ''credit_level字段已存在'' AS message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ============================================
-- 检查并添加finance_supplier表的缺失字段
-- ============================================

-- 检查contact_person字段
SELECT COUNT(*) INTO @supplier_contact_exists 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'coal_erp' 
  AND TABLE_NAME = 'finance_supplier' 
  AND COLUMN_NAME = 'contact_person';

SET @sql = IF(@supplier_contact_exists = 0,
    'ALTER TABLE finance_supplier ADD COLUMN contact_person varchar(50) DEFAULT NULL COMMENT ''联系人''',
    'SELECT ''contact_person字段已存在'' AS message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查evaluation_level字段
SELECT COUNT(*) INTO @evaluation_level_exists 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'coal_erp' 
  AND TABLE_NAME = 'finance_supplier' 
  AND COLUMN_NAME = 'evaluation_level';

SET @sql = IF(@evaluation_level_exists = 0,
    'ALTER TABLE finance_supplier ADD COLUMN evaluation_level varchar(20) DEFAULT NULL COMMENT ''评估等级''',
    'SELECT ''evaluation_level字段已存在'' AS message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ============================================
-- 显示修复结果
-- ============================================
SELECT '字段检查完成！' AS result;
SELECT 
    'finance_customer表字段：' AS table_name,
    CASE WHEN @contact_person_exists > 0 THEN 'contact_person ✓' ELSE 'contact_person ✗' END AS contact_person,
    CASE WHEN @credit_level_exists > 0 THEN 'credit_level ✓' ELSE 'credit_level ✗' END AS credit_level
UNION ALL
SELECT 
    'finance_supplier表字段：' AS table_name,
    CASE WHEN @supplier_contact_exists > 0 THEN 'contact_person ✓' ELSE 'contact_person ✗' END AS contact_person,
    CASE WHEN @evaluation_level_exists > 0 THEN 'evaluation_level ✓' ELSE 'evaluation_level ✗' END AS credit_level;

