#!/bin/bash

# 数据库初始化脚本

echo "========================================"
echo "数据库初始化脚本"
echo "========================================"
echo ""

# 设置数据库配置
DB_USER=${DB_USER:-root}
DB_NAME=${DB_NAME:-coal_erp}
SQL_FILE="coal-erp-business/src/main/resources/db/schema.sql"

echo "[信息] 正在初始化数据库..."
echo "[信息] 数据库名: $DB_NAME"
echo "[信息] SQL文件: $SQL_FILE"
echo ""

# 检查SQL文件是否存在
if [ ! -f "$SQL_FILE" ]; then
    echo "[错误] SQL文件不存在: $SQL_FILE"
    echo "[提示] 请确保在项目根目录执行此脚本"
    exit 1
fi

# 执行SQL脚本
echo "[信息] 请输入MySQL root密码:"
mysql -u $DB_USER -p $DB_NAME < "$SQL_FILE"

if [ $? -eq 0 ]; then
    echo ""
    echo "[成功] 数据库初始化完成！"
    echo ""
    echo "[下一步] 请设置管理员密码，参考 SETUP_PASSWORD.md"
else
    echo ""
    echo "[错误] 数据库初始化失败"
    echo "[提示] 请检查："
    echo "  1. MySQL服务是否启动"
    echo "  2. 数据库 $DB_NAME 是否已创建"
    echo "  3. 用户名和密码是否正确"
    exit 1
fi















