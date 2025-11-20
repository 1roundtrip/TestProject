@echo off
chcp 65001 >nul
echo ========================================
echo MySQL数据库初始化脚本
echo ========================================
echo.

REM 数据库配置
set DB_HOST=localhost
set DB_PORT=3306
set DB_USER=root
set DB_PASSWORD=root
set DB_NAME=coal_erp

REM SQL文件路径
set SQL_FILE=..\coal-erp-business\src\main\resources\db\schema.sql

REM 检查SQL文件是否存在
if not exist "%SQL_FILE%" (
    echo [错误] SQL文件不存在: %SQL_FILE%
    pause
    exit /b 1
)

echo [信息] 数据库配置:
echo   主机: %DB_HOST%
echo   端口: %DB_PORT%
echo   用户: %DB_USER%
echo   数据库: %DB_NAME%
echo.

echo [信息] 正在执行数据库初始化...

REM 执行SQL文件
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% ^
    -e "CREATE DATABASE IF NOT EXISTS %DB_NAME% DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" ^
    -e "USE %DB_NAME%;" ^
    -e "SOURCE %SQL_FILE%;"

if %errorlevel% equ 0 (
    echo [成功] 数据库初始化完成
) else (
    echo [错误] 数据库初始化失败
    pause
    exit /b 1
)

pause















