-- ============================================
-- 检查采购订单表结构
-- ============================================

USE coal_erp;

-- 查看表结构
DESC purchase_order;

-- 或者使用
-- SHOW COLUMNS FROM purchase_order;

-- 检查关键字段是否存在
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT,
    COLUMN_COMMENT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'coal_erp'
  AND TABLE_NAME = 'purchase_order'
ORDER BY ORDINAL_POSITION;

