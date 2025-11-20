@echo off
chcp 65001 >nul
echo ========================================
echo 智慧煤矿ERP管理系统 - 启动所有服务
echo ========================================
echo.

REM 检查Java环境
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未检测到Java环境，请先安装JDK 1.8或更高版本
    pause
    exit /b 1
)

REM 检查Node.js环境
node -v >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未检测到Node.js环境，请先安装Node.js 16.0或更高版本
    pause
    exit /b 1
)

echo [信息] 正在启动后端服务...
start "后端服务" cmd /k "start-backend.bat"

REM 等待后端启动
timeout /t 5 /nobreak >nul

echo [信息] 正在启动前端服务...
start "前端服务" cmd /k "start-frontend.bat"

echo.
echo [信息] 所有服务已启动
echo [信息] 后端服务地址: http://localhost:8080
echo [信息] 前端服务地址: http://localhost:5173
echo [提示] 关闭此窗口不会停止服务，请在对应的服务窗口中按 Ctrl+C 停止
echo.

pause

