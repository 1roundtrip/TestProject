@echo off
chcp 65001 >nul
echo ========================================
echo 智慧煤矿ERP管理系统 - 导入测试数据
echo ========================================
echo.

REM 设置数据库连接信息
set DB_HOST=localhost
set DB_PORT=3306
set DB_NAME=coal_erp
set DB_USER=root
set DB_PASSWORD=

REM 提示用户输入数据库密码
set /p DB_PASSWORD="请输入MySQL root用户密码: "

if "%DB_PASSWORD%"=="" (
    echo [错误] 密码不能为空
    pause
    exit /b 1
)

echo.
echo [信息] 数据库连接信息:
echo [信息] 主机: %DB_HOST%
echo [信息] 端口: %DB_PORT%
echo [信息] 数据库: %DB_NAME%
echo [信息] 用户: %DB_USER%
echo.

REM 检查MySQL命令是否可用
mysql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未找到mysql命令，请确保MySQL已安装并添加到PATH
    pause
    exit /b 1
)

REM 设置SQL文件路径（自动获取脚本所在目录）
cd /d "%~dp0"
set SQL_FILE=%CD%\coal-erp-business\src\main\resources\db\complete_test_data.sql

echo [信息] SQL文件路径: %SQL_FILE%
echo.

REM 检查SQL文件是否存在
if not exist "%SQL_FILE%" (
    echo [错误] SQL文件不存在: %SQL_FILE%
    echo [提示] 请确保在项目根目录执行此脚本
    pause
    exit /b 1
)

echo [信息] 开始导入测试数据...
echo [警告] 此操作将清空所有业务数据，请确认！
echo.
set /p CONFIRM="是否继续? (Y/N): "
if /i not "%CONFIRM%"=="Y" (
    echo 已取消导入
    pause
    exit /b 0
)

echo.
echo [步骤] 正在导入测试数据...
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% %DB_NAME% < "%SQL_FILE%"

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo [成功] 测试数据导入完成！
    echo ========================================
    echo.
    echo [信息] 已导入的数据包括：
    echo [信息]   - 供应商数据
    echo [信息]   - 资产数据
    echo [信息]   - 采购申请、订单、收货数据
    echo [信息]   - 库存数据
    echo [信息]   - 维修工单数据
    echo [信息]   - 预警数据
    echo.
) else (
    echo.
    echo ========================================
    echo [错误] 测试数据导入失败！
    echo ========================================
    echo.
    echo [可能的原因]
    echo 1. 数据库连接失败
    echo 2. 表结构不存在（请先执行表结构创建脚本）
    echo 3. SQL语法错误
    echo.
    echo [建议]
    echo 1. 检查数据库连接信息
    echo 2. 确保已执行表结构创建脚本
    echo 3. 查看上方的错误信息
    echo.
)

pause

