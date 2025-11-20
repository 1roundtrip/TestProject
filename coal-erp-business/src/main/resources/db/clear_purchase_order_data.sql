-- ============================================
-- 清空采购订单数据脚本
-- ============================================
-- 警告：此操作将删除所有采购订单数据，请谨慎执行！
-- 执行前请确保已备份重要数据
-- ============================================

USE coal_erp;

-- ============================================
-- 步骤1: 查看当前数据量（执行前）
-- ============================================
SELECT '=== 清空前数据统计 ===' AS info;
SELECT 
    '采购订单主表' AS table_name,
    COUNT(*) AS record_count
FROM purchase_order
UNION ALL
SELECT 
    '采购订单明细表' AS table_name,
    COUNT(*) AS record_count
FROM purchase_order_detail;

-- ============================================
-- 步骤2: 清空采购订单明细表（先删除明细，因为有外键关联）
-- ============================================
DELETE FROM purchase_order_detail;

-- 重置明细表自增ID（可选，如果需要从1开始）
-- ALTER TABLE purchase_order_detail AUTO_INCREMENT = 1;

-- ============================================
-- 步骤3: 清空采购订单主表
-- ============================================
DELETE FROM purchase_order;

-- 重置主表自增ID（可选，如果需要从1开始）
-- ALTER TABLE purchase_order AUTO_INCREMENT = 1;

-- ============================================
-- 步骤4: 验证清空结果
-- ============================================
SELECT '=== 清空后数据统计 ===' AS info;
SELECT 
    '采购订单主表' AS table_name,
    COUNT(*) AS record_count
FROM purchase_order
UNION ALL
SELECT 
    '采购订单明细表' AS table_name,
    COUNT(*) AS record_count
FROM purchase_order_detail;

SELECT '=== 清空完成！ ===' AS final_message;
SELECT '采购订单数据已全部清空' AS result;

