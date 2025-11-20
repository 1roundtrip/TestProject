# 多阶段构建
# 阶段1: 构建前端
FROM node:18-alpine AS frontend-builder

WORKDIR /app/frontend

# 复制前端文件
COPY frontend/package*.json ./
RUN npm install

COPY frontend/ ./
RUN npm run build

# 阶段2: 构建后端
FROM maven:3.8.7-openjdk-8-slim AS backend-builder

WORKDIR /app

# 复制Maven配置
COPY pom.xml .
COPY coal-erp-common/pom.xml ./coal-erp-common/
COPY coal-erp-system/pom.xml ./coal-erp-system/
COPY coal-erp-business/pom.xml ./coal-erp-business/

# 下载依赖
RUN mvn dependency:go-offline -B

# 复制源代码
COPY coal-erp-common ./coal-erp-common
COPY coal-erp-system ./coal-erp-system
COPY coal-erp-business ./coal-erp-business

# 复制前端构建产物到resources/static
COPY --from=frontend-builder /app/frontend/dist ./coal-erp-business/src/main/resources/static

# 构建应用
RUN mvn clean package -DskipTests -B

# 阶段3: 运行镜像
FROM openjdk:8-jre-alpine

# 设置工作目录
WORKDIR /app

# 安装必要的工具
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    apk del tzdata

# 创建日志目录
RUN mkdir -p /app/logs

# 从构建阶段复制jar文件
COPY --from=backend-builder /app/coal-erp-business/target/coal-erp-business-*.jar app.jar

# 暴露端口
EXPOSE 8080

# JVM参数
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=prod"]















