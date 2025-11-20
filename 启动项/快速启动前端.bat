@echo off
REM 最简单的启动脚本，直接执行命令
chcp 65001 >nul
title 前端服务

cd /d "%~dp0\..\frontend"
if not exist "package.json" (
    echo [错误] 未找到package.json
    pause
    exit /b 1
)

if not exist "node_modules" (
    echo 正在安装依赖...
    npm install
    if errorlevel 1 (
        echo 依赖安装失败
        pause
        exit /b 1
    )
)

echo.
echo 启动前端服务...
echo 地址: http://localhost:5173
echo 按 Ctrl+C 停止
echo.

npm run dev

pause

