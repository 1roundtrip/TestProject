#!/bin/bash

# MySQL数据库初始化脚本

echo "========================================"
echo "MySQL数据库初始化脚本"
echo "========================================"
echo ""

# 数据库配置
DB_HOST=${DB_HOST:-localhost}
DB_PORT=${DB_PORT:-3306}
DB_USER=${DB_USER:-root}
DB_PASSWORD=${DB_PASSWORD:-root}
DB_NAME=${DB_NAME:-coal_erp}

# SQL文件路径
SQL_FILE="../coal-erp-business/src/main/resources/db/schema.sql"

# 检查SQL文件是否存在
if [ ! -f "$SQL_FILE" ]; then
    echo "[错误] SQL文件不存在: $SQL_FILE"
    exit 1
fi

echo "[信息] 数据库配置:"
echo "  主机: $DB_HOST"
echo "  端口: $DB_PORT"
echo "  用户: $DB_USER"
echo "  数据库: $DB_NAME"
echo ""

# 执行SQL文件
echo "[信息] 正在执行数据库初始化..."
mysql -h$DB_HOST -P$DB_PORT -u$DB_USER -p$DB_PASSWORD <<EOF
CREATE DATABASE IF NOT EXISTS $DB_NAME DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE $DB_NAME;
SOURCE $SQL_FILE;
EOF

if [ $? -eq 0 ]; then
    echo "[成功] 数据库初始化完成"
else
    echo "[错误] 数据库初始化失败"
    exit 1
fi















