#!/bin/bash

# 智慧煤矿ERP管理系统启动脚本

echo "========================================"
echo "智慧煤矿ERP管理系统启动脚本"
echo "========================================"
echo ""

# 检查Java环境
if ! command -v java &> /dev/null; then
    echo "[错误] 未检测到Java环境，请先安装JDK 1.8或更高版本"
    exit 1
fi

# 检查jar文件是否存在
JAR_FILE=$(find coal-erp-business/target -name "coal-erp-business-*.jar" | head -n 1)
if [ -z "$JAR_FILE" ]; then
    echo "[错误] 未找到jar文件，请先执行 mvn clean package 进行打包"
    exit 1
fi

# 设置JVM参数
JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Dfile.encoding=UTF-8"

# 设置环境变量
export SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-prod}

# 创建日志目录
mkdir -p logs

echo "[信息] 正在启动应用..."
echo "[信息] JVM参数: $JAVA_OPTS"
echo "[信息] 环境配置: $SPRING_PROFILES_ACTIVE"
echo "[信息] JAR文件: $JAR_FILE"
echo ""

# 启动应用
java $JAVA_OPTS -jar "$JAR_FILE" --spring.profiles.active=$SPRING_PROFILES_ACTIVE

# 检查启动结果
if [ $? -ne 0 ]; then
    echo ""
    echo "[错误] 应用启动失败，错误代码: $?"
    exit 1
fi















