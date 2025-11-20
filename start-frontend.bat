@echo off
chcp 65001 >nul
echo ========================================
echo 智慧煤矿ERP管理系统 - 前端服务启动
echo ========================================
echo.

REM 检查Node.js环境
node -v >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未检测到Node.js环境，请先安装Node.js 16.0或更高版本
    pause
    exit /b 1
)

REM 检查npm环境
npm -v >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未检测到npm，请先安装Node.js
    pause
    exit /b 1
)

REM 进入frontend目录
cd frontend
if %errorlevel% neq 0 (
    echo [错误] 无法进入frontend目录
    pause
    exit /b 1
)

REM 检查node_modules是否存在
if not exist "node_modules" (
    echo [信息] 检测到未安装依赖，正在安装...
    echo [信息] 这可能需要几分钟时间，请耐心等待...
    npm install
    if %errorlevel% neq 0 (
        echo [错误] 依赖安装失败
        pause
        exit /b 1
    )
)

echo [信息] 正在启动前端服务...
echo [信息] 前端服务地址: http://localhost:5173
echo [信息] 后端API地址: http://localhost:8080
echo [提示] 按 Ctrl+C 停止服务
echo.

REM 启动前端开发服务器
npm run dev

if %errorlevel% neq 0 (
    echo.
    echo [错误] 前端服务启动失败，错误代码: %errorlevel%
    pause
    exit /b %errorlevel%
)

pause

