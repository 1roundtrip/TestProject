-- 修复财务模块表字段的SQL脚本
-- 如果finance_customer和finance_supplier表缺少字段，执行此脚本添加

USE coal_erp;

-- 为finance_customer表添加缺失字段
ALTER TABLE finance_customer 
ADD COLUMN IF NOT EXISTS contact_person varchar(50) DEFAULT NULL COMMENT '联系人',
ADD COLUMN IF NOT EXISTS credit_level varchar(20) DEFAULT NULL COMMENT '信用等级';

-- 为finance_supplier表添加缺失字段
ALTER TABLE finance_supplier 
ADD COLUMN IF NOT EXISTS contact_person varchar(50) DEFAULT NULL COMMENT '联系人',
ADD COLUMN IF NOT EXISTS evaluation_level varchar(20) DEFAULT NULL COMMENT '评估等级';

-- 注意：MySQL 5.7及以下版本不支持IF NOT EXISTS，如果报错，请手动检查字段是否存在后再执行

