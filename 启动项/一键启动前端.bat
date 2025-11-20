@echo off
REM 设置代码页为UTF-8
chcp 65001 >nul 2>&1

REM 显示标题
title 前端服务启动
echo.
echo ========================================
echo 智慧煤矿ERP管理系统 - 前端服务启动
echo ========================================
echo.

REM 切换到frontend目录（基于快速启动的成功经验）
cd /d "%~dp0\..\frontend"
if not exist "package.json" (
    echo [错误] 未找到package.json文件
    echo [当前目录] %CD%
    echo.
    pause
    exit /b 1
)

REM 检查并安装依赖
if not exist "node_modules" (
    echo [信息] 检测到未安装依赖，正在安装...
    echo [提示] 这可能需要几分钟，请耐心等待...
    echo.
    npm install
    if errorlevel 1 (
        echo.
        echo [错误] 依赖安装失败
        echo [提示] 请检查网络连接或npm配置
        echo.
        pause
        exit /b 1
    )
    echo.
    echo [成功] 依赖安装完成
    echo.
)

REM 启动服务
echo ========================================
echo 正在启动前端服务...
echo ========================================
echo.
echo [信息] 前端地址: http://localhost:5173
echo [信息] 后端API: http://localhost:8080
echo [提示] 按 Ctrl+C 停止服务
echo.
echo ========================================
echo.

REM 执行npm run dev
npm run dev

REM 如果启动失败，显示错误信息
if errorlevel 1 (
    echo.
    echo [错误] 前端服务启动失败
    echo [错误代码] %errorlevel%
    echo.
    echo [可能的原因]
    echo 1. 端口5173已被占用
    echo 2. 依赖包安装不完整
    echo 3. Node.js版本不兼容
    echo.
)

pause
