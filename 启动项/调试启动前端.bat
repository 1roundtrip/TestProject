@echo off
REM 这个脚本用于调试，会显示所有详细信息
chcp 65001
echo ========================================
echo 前端启动调试脚本
echo ========================================
echo.

echo [步骤1] 检查当前目录
echo 当前目录: %CD%
echo 脚本位置: %~dp0
echo.

echo [步骤2] 切换到项目根目录
set "PROJECT_ROOT=%~dp0\.."
echo 目标目录: %PROJECT_ROOT%
cd /d "%PROJECT_ROOT%"
if %errorlevel% neq 0 (
    echo [失败] 无法切换目录
    pause
    exit /b 1
)
echo [成功] 当前目录: %CD%
echo.

echo [步骤3] 检查Node.js
where node >nul 2>&1
if %errorlevel% neq 0 (
    echo [失败] 未找到node命令
    echo 请确保Node.js已安装并添加到PATH环境变量
    pause
    exit /b 1
)
echo [成功] Node.js路径:
where node
node -v
echo.

echo [步骤4] 检查npm
where npm >nul 2>&1
if %errorlevel% neq 0 (
    echo [失败] 未找到npm命令
    pause
    exit /b 1
)
echo [成功] npm路径:
where npm
npm -v
echo.

echo [步骤5] 检查frontend目录
if not exist "frontend" (
    echo [失败] 未找到frontend目录
    echo 当前目录内容:
    dir /b
    pause
    exit /b 1
)
echo [成功] frontend目录存在
echo.

echo [步骤6] 进入frontend目录
cd frontend
if %errorlevel% neq 0 (
    echo [失败] 无法进入frontend目录
    pause
    exit /b 1
)
echo [成功] 当前目录: %CD%
echo.

echo [步骤7] 检查package.json
if not exist "package.json" (
    echo [失败] 未找到package.json
    echo 当前目录内容:
    dir /b
    pause
    exit /b 1
)
echo [成功] package.json存在
echo.

echo [步骤8] 检查node_modules
if not exist "node_modules" (
    echo [警告] node_modules不存在，需要安装依赖
    echo 是否现在安装? (Y/N)
    set /p INSTALL="请输入: "
    if /i "%INSTALL%"=="Y" (
        echo 正在安装依赖...
        npm install
        if %errorlevel% neq 0 (
            echo [失败] 依赖安装失败
            pause
            exit /b 1
        )
    ) else (
        echo 跳过依赖安装
    )
) else (
    echo [成功] node_modules存在
)
echo.

echo [步骤9] 检查端口5173是否被占用
netstat -ano | findstr :5173 >nul 2>&1
if %errorlevel% equ 0 (
    echo [警告] 端口5173已被占用
    echo 占用端口的进程:
    netstat -ano | findstr :5173
    echo.
    echo 是否继续? (Y/N)
    set /p CONTINUE="请输入: "
    if /i not "%CONTINUE%"=="Y" (
        echo 已取消
        pause
        exit /b 0
    )
) else (
    echo [成功] 端口5173可用
)
echo.

echo [步骤10] 启动前端服务
echo ========================================
echo 正在启动前端服务...
echo 前端地址: http://localhost:5173
echo 按 Ctrl+C 停止服务
echo ========================================
echo.

npm run dev

set "EXIT_CODE=%errorlevel%"
echo.
echo ========================================
echo 服务已停止
echo 退出代码: %EXIT_CODE%
echo ========================================
pause
exit /b %EXIT_CODE%

