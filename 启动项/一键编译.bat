@echo off
chcp 65001 >nul
echo ========================================
echo 智慧煤矿ERP管理系统 - 一键编译
echo ========================================
echo.

cd /d D:\Zh-MK

REM 检查Maven环境
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未检测到Maven环境，请先安装Maven 3.6或更高版本
    pause
    exit /b 1
)

echo [信息] 开始编译项目...
echo [信息] 这可能需要几分钟时间，请耐心等待...
echo.

REM 清理并编译打包
mvn clean package -DskipTests

if %errorlevel% neq 0 (
    echo.
    echo [错误] 编译失败，请检查错误信息
    pause
    exit /b %errorlevel%
)

echo.
echo ========================================
echo 编译完成！
echo ========================================
echo [信息] JAR文件位置: coal-erp-business\target\coal-erp-business-*.jar
echo [信息] 可以使用"一键启动后端.bat"启动服务
echo.

pause

