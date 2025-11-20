@echo off
chcp 65001 >nul
echo ========================================
echo 智慧煤矿ERP管理系统 - 后端服务启动
echo ========================================
echo.

REM 检查Java环境
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未检测到Java环境，请先安装JDK 1.8或更高版本
    pause
    exit /b 1
)

REM 检查jar文件是否存在
set JAR_FILE=
for %%f in (coal-erp-business\target\coal-erp-business-*.jar) do (
    set JAR_FILE=%%f
    goto :found
)

:found
if "%JAR_FILE%"=="" (
    echo [错误] 未找到jar文件，请先执行 mvn clean package 进行打包
    pause
    exit /b 1
)

REM 设置JVM参数
set JAVA_OPTS=-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Dfile.encoding=UTF-8

REM 设置环境变量
set SPRING_PROFILES_ACTIVE=dev

REM 创建日志目录
if not exist "logs" mkdir logs

echo [信息] 正在启动后端服务...
echo [信息] JVM参数: %JAVA_OPTS%
echo [信息] 环境配置: %SPRING_PROFILES_ACTIVE%
echo [信息] JAR文件: %JAR_FILE%
echo [信息] 后端服务地址: http://localhost:8080
echo.

REM 启动应用
java %JAVA_OPTS% -jar "%JAR_FILE%" --spring.profiles.active=%SPRING_PROFILES_ACTIVE%

if %errorlevel% neq 0 (
    echo.
    echo [错误] 后端服务启动失败，错误代码: %errorlevel%
    pause
    exit /b %errorlevel%
)

pause

