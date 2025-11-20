-- ============================================
-- 修复 sys_menu 表结构
-- ============================================
-- 说明：确保 sys_menu 表包含所有必需的字段

USE coal_erp;

-- 检查并添加 order_num 字段（如果不存在）
-- 注意：MySQL 5.7 及以下版本不支持 IF NOT EXISTS，使用存储过程处理
DELIMITER $$

DROP PROCEDURE IF EXISTS fix_sys_menu_table$$

CREATE PROCEDURE fix_sys_menu_table()
BEGIN
    -- 检查 order_num 字段是否存在
    IF NOT EXISTS (
        SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS 
        WHERE TABLE_SCHEMA = 'coal_erp' 
        AND TABLE_NAME = 'sys_menu' 
        AND COLUMN_NAME = 'order_num'
    ) THEN
        ALTER TABLE `sys_menu` 
        ADD COLUMN `order_num` int(4) DEFAULT '0' COMMENT '显示顺序' AFTER `parent_id`;
    END IF;
END$$

DELIMITER ;

-- 执行存储过程
CALL fix_sys_menu_table();

-- 删除存储过程
DROP PROCEDURE IF EXISTS fix_sys_menu_table;

-- 更新所有 order_num 为 NULL 的记录，设置为 0
UPDATE `sys_menu` SET `order_num` = 0 WHERE `order_num` IS NULL;

-- 验证表结构
SELECT 
    COLUMN_NAME, 
    DATA_TYPE, 
    IS_NULLABLE, 
    COLUMN_DEFAULT,
    COLUMN_COMMENT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'coal_erp' 
AND TABLE_NAME = 'sys_menu'
ORDER BY ORDINAL_POSITION;

-- 显示菜单数据（前10条）
SELECT 
    menu_id,
    menu_name,
    parent_id,
    order_num,
    menu_type,
    status
FROM `sys_menu`
ORDER BY order_num, menu_id
LIMIT 10;

