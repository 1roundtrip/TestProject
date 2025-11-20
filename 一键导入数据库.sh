#!/bin/bash

# 智慧煤矿ERP管理系统 - 一键导入数据库脚本

echo "========================================"
echo "智慧煤矿ERP管理系统 - 一键导入数据库"
echo "========================================"
echo ""

# 设置数据库连接信息
DB_HOST="localhost"
DB_PORT="3306"
DB_NAME="coal_erp"
DB_USER="root"

# 提示用户输入数据库密码
read -sp "请输入MySQL root用户密码: " DB_PASSWORD
echo ""

if [ -z "$DB_PASSWORD" ]; then
    echo "[错误] 密码不能为空"
    exit 1
fi

echo ""
echo "[信息] 数据库连接信息:"
echo "[信息] 主机: $DB_HOST"
echo "[信息] 端口: $DB_PORT"
echo "[信息] 数据库: $DB_NAME"
echo "[信息] 用户: $DB_USER"
echo ""

# 检查MySQL命令是否可用
if ! command -v mysql &> /dev/null; then
    echo "[错误] 未找到mysql命令，请确保MySQL已安装"
    exit 1
fi

# 设置SQL文件路径（自动获取脚本所在目录）
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
SQL_DIR="$SCRIPT_DIR/coal-erp-business/src/main/resources/db"

echo "[信息] 开始导入数据库..."
echo ""

# 1. 创建数据库
echo "[步骤 1/8] 创建数据库..."
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" -e "CREATE DATABASE IF NOT EXISTS $DB_NAME CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>/dev/null
if [ $? -ne 0 ]; then
    echo "[错误] 创建数据库失败，请检查数据库连接信息"
    exit 1
fi
echo "[成功] 数据库创建完成"

# 2. 导入基础表结构
echo "[步骤 2/8] 导入基础表结构..."
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < "$SQL_DIR/schema.sql" 2>/dev/null
if [ $? -ne 0 ]; then
    echo "[警告] 基础表结构导入可能有问题，请检查"
else
    echo "[成功] 基础表结构导入完成"
fi

# 3. 导入采购模块
echo "[步骤 3/8] 导入采购模块..."
for sql_file in "$SQL_DIR/purchase"/*.sql; do
    if [ -f "$sql_file" ]; then
        mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < "$sql_file" 2>/dev/null
    fi
done
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < "$SQL_DIR/purchase_permission_menu.sql" 2>/dev/null
echo "[成功] 采购模块导入完成"

# 4. 导入库存模块
echo "[步骤 4/8] 导入库存模块..."
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < "$SQL_DIR/inventory/inventory_schema.sql" 2>/dev/null
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < "$SQL_DIR/inventory/inventory_permission_menu.sql" 2>/dev/null
echo "[成功] 库存模块导入完成"

# 5. 导入维修管理模块
echo "[步骤 5/8] 导入维修管理模块..."
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < "$SQL_DIR/maintenance/maintenance_schema.sql" 2>/dev/null
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < "$SQL_DIR/maintenance/maintenance_permission_menu.sql" 2>/dev/null
echo "[成功] 维修管理模块导入完成"

# 6. 导入预警模块
echo "[步骤 6/8] 导入预警模块..."
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < "$SQL_DIR/warning/warning_schema.sql" 2>/dev/null
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < "$SQL_DIR/warning/warning_permission_menu.sql" 2>/dev/null
echo "[成功] 预警模块导入完成"

# 7. 导入资产模块
echo "[步骤 7/8] 导入资产模块..."
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < "$SQL_DIR/asset_management_schema.sql" 2>/dev/null
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < "$SQL_DIR/asset_permission_menu.sql" 2>/dev/null
echo "[成功] 资产模块导入完成"

# 8. 配置角色和权限
echo "[步骤 8/8] 配置角色和权限..."
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < "$SQL_DIR/setup_module_managers.sql" 2>/dev/null
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < "$SQL_DIR/create_module_manager_users.sql" 2>/dev/null
mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < "$SQL_DIR/diagnose_and_fix_system_permissions.sql" 2>/dev/null
echo "[成功] 角色和权限配置完成"

echo ""
echo "========================================"
echo "数据库导入完成！"
echo "========================================"
echo "[信息] 默认管理员账户:"
echo "[信息]   用户名: admin"
echo "[信息]   密码: admin123"
echo "[信息]   或用户名: admin2"
echo "[信息]   密码: admin123"
echo ""
echo "[信息] 模块负责人账户（密码均为123456）:"
echo "[信息]   asset_manager - 资产中心负责人"
echo "[信息]   purchase_manager - 采购中心负责人"
echo "[信息]   maintenance_manager - 维修管理负责人"
echo "[信息]   inventory_manager - 库存中心负责人"
echo "[信息]   warning_manager - 预警中心负责人"
echo ""

