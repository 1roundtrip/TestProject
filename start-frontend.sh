#!/bin/bash

# 智慧煤矿ERP管理系统 - 前端服务启动脚本

echo "========================================"
echo "智慧煤矿ERP管理系统 - 前端服务启动"
echo "========================================"
echo ""

# 检查Node.js环境
if ! command -v node &> /dev/null; then
    echo "[错误] 未检测到Node.js环境，请先安装Node.js 16.0或更高版本"
    exit 1
fi

# 检查npm环境
if ! command -v npm &> /dev/null; then
    echo "[错误] 未检测到npm，请先安装Node.js"
    exit 1
fi

# 进入frontend目录
cd frontend || exit 1

# 检查node_modules是否存在
if [ ! -d "node_modules" ]; then
    echo "[信息] 检测到未安装依赖，正在安装..."
    echo "[信息] 这可能需要几分钟时间，请耐心等待..."
    npm install
    if [ $? -ne 0 ]; then
        echo "[错误] 依赖安装失败"
        exit 1
    fi
fi

echo "[信息] 正在启动前端服务..."
echo "[信息] 前端服务地址: http://localhost:5173"
echo "[信息] 后端API地址: http://localhost:8080"
echo "[提示] 按 Ctrl+C 停止服务"
echo ""

# 启动前端开发服务器
npm run dev

# 检查启动结果
if [ $? -ne 0 ]; then
    echo ""
    echo "[错误] 前端服务启动失败，错误代码: $?"
    exit 1
fi

