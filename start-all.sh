#!/bin/bash

# 智慧煤矿ERP管理系统 - 启动所有服务脚本

echo "========================================"
echo "智慧煤矿ERP管理系统 - 启动所有服务"
echo "========================================"
echo ""

# 检查Java环境
if ! command -v java &> /dev/null; then
    echo "[错误] 未检测到Java环境，请先安装JDK 1.8或更高版本"
    exit 1
fi

# 检查Node.js环境
if ! command -v node &> /dev/null; then
    echo "[错误] 未检测到Node.js环境，请先安装Node.js 16.0或更高版本"
    exit 1
fi

echo "[信息] 正在启动后端服务..."
nohup bash start-backend.sh > logs/backend-startup.log 2>&1 &
BACKEND_PID=$!

# 等待后端启动
sleep 5

echo "[信息] 正在启动前端服务..."
nohup bash start-frontend.sh > logs/frontend-startup.log 2>&1 &
FRONTEND_PID=$!

echo ""
echo "[信息] 所有服务已启动"
echo "[信息] 后端服务PID: $BACKEND_PID"
echo "[信息] 前端服务PID: $FRONTEND_PID"
echo "[信息] 后端服务地址: http://localhost:8080"
echo "[信息] 前端服务地址: http://localhost:5173"
echo "[提示] 查看日志: tail -f logs/backend-startup.log 或 logs/frontend-startup.log"
echo "[提示] 停止服务: kill $BACKEND_PID $FRONTEND_PID"
echo ""

