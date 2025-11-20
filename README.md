======================
智慧煤矿ERP管理系统部署指南
======================

本指南详细说明如何在Windows和Linux系统上部署智慧煤矿ERP管理系统。

============================
一、系统要求
============================

1. 后端要求：
   - JDK 1.8 或更高版本
   - Maven 3.6 或更高版本
   - MySQL 5.7 或更高版本（推荐 8.0+）
   - Redis 5.0 或更高版本（可选，用于缓存）

2. 前端要求：
   - Node.js 16.0 或更高版本
   - npm 8.0 或更高版本（或使用 yarn）

3. 操作系统：
   - Windows 10/11 或 Windows Server 2016+
   - Linux（Ubuntu 20.04+, CentOS 7+, Debian 10+）

==========================
二、Windows系统部署
===========================

2.1 环境准备

1. 安装JDK
   - 下载JDK 1.8或更高版本
   - 安装后配置JAVA_HOME环境变量
   - 验证：打开命令提示符，输入 java -version

2. 安装Maven
   - 下载Maven 3.6或更高版本
   - 解压到目录（如 C:\Program Files\Apache\maven）
   - 配置MAVEN_HOME环境变量
   - 将 %MAVEN_HOME%\bin 添加到PATH
   - 验证：输入 mvn -version

3. 安装MySQL
   - 下载MySQL 8.0安装包
   - 安装MySQL服务器
   - 记住root用户密码
   - 启动MySQL服务

4. 安装Node.js
   - 下载Node.js 16+安装包
   - 安装Node.js和npm
   - 验证：输入 node -v 和 npm -v

5. 安装Redis（可选）
   - 下载Redis for Windows
   - 或使用WSL安装Redis

2.2 数据库初始化

1. 创建数据库：
   打开MySQL命令行或客户端，执行：
   
   CREATE DATABASE coal_erp CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

2. 执行初始化脚本：
   按照以下顺序执行SQL脚本（在 coal-erp-business/src/main/resources/db/ 目录下）：
   
   a) schema.sql - 创建基础表结构
   b) 各模块的schema文件（如 purchase_schema.sql, inventory_schema.sql 等）
   c) 各模块的权限菜单文件（如 purchase_permission_menu.sql 等）
   d) setup_module_managers.sql - 创建模块负责人角色
   e) create_module_manager_users.sql - 创建模块负责人用户
   f) diagnose_and_fix_system_permissions.sql - 配置系统管理权限

3. 配置数据库连接：
   编辑 coal-erp-business/src/main/resources/application-dev.yml 或 application-prod.yml
   修改数据库连接信息：
   
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/coal_erp?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true
       username: root
       password: 你的数据库密码

2.3 后端部署

方法一：使用Maven打包运行（开发环境）

1. 打开命令提示符，进入项目根目录

2. 编译打包：
   mvn clean package -DskipTests

3. 启动后端：
   双击运行 start-backend.bat
   或手动执行：
   java -jar coal-erp-business/target/coal-erp-business-*.jar

方法二：使用IDE运行（开发环境）

1. 使用IntelliJ IDEA或Eclipse导入项目
2. 找到主类：com.coal.erp.CoalErpApplication
3. 运行主类

2.4 前端部署

1. 打开命令提示符，进入 frontend 目录

2. 安装依赖：
   npm install
   或使用国内镜像：
   npm install --registry=https://registry.npmmirror.com

3. 配置API地址：
   编辑 frontend/src/config/api.ts
   修改后端API地址（默认：http://localhost:8080）

4. 开发环境启动：
   双击运行 start-frontend.bat
   或手动执行：
   npm run dev

5. 生产环境构建：
   npm run build
   构建后的文件在 frontend/dist 目录

2.5 访问系统

1. 前端地址：http://localhost:5173（开发环境）或 http://localhost:80（生产环境）
2. 后端API：http://localhost:8080
3. 默认管理员账户：
   - 用户名：admin
   - 密码：admin123
   - 或用户名：admin2
   - 密码：admin123

============================
三、Linux系统部署
============================

3.1 环境准备

1. 安装JDK：
   Ubuntu/Debian:
   sudo apt update
   sudo apt install openjdk-8-jdk
   
   CentOS/RHEL:
   sudo yum install java-1.8.0-openjdk-devel
   
   验证：java -version

2. 安装Maven：
   Ubuntu/Debian:
   sudo apt install maven
   
   CentOS/RHEL:
   sudo yum install maven
   
   验证：mvn -version

3. 安装MySQL：
   Ubuntu/Debian:
   sudo apt install mysql-server
   sudo systemctl start mysql
   sudo systemctl enable mysql
   
   CentOS/RHEL:
   sudo yum install mysql-server
   sudo systemctl start mysqld
   sudo systemctl enable mysqld
   
   设置root密码：
   sudo mysql_secure_installation

4. 安装Node.js：
   使用NodeSource仓库：
   curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
   sudo apt install -y nodejs
   
   验证：node -v 和 npm -v

5. 安装Redis（可选）：
   Ubuntu/Debian:
   sudo apt install redis-server
   sudo systemctl start redis
   sudo systemctl enable redis
   
   CentOS/RHEL:
   sudo yum install redis
   sudo systemctl start redis
   sudo systemctl enable redis

3.2 数据库初始化

1. 登录MySQL：
   mysql -u root -p

2. 创建数据库：
   CREATE DATABASE coal_erp CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   USE coal_erp;

3. 执行初始化脚本：
   source /path/to/coal-erp-business/src/main/resources/db/schema.sql;
   source /path/to/coal-erp-business/src/main/resources/db/purchase_permission_menu.sql;
   # ... 执行其他必要的SQL脚本

3.3 后端部署

1. 进入项目根目录

2. 编译打包：
   mvn clean package -DskipTests

3. 启动后端：
   方式一：使用启动脚本
   chmod +x start-backend.sh
   ./start-backend.sh
   
   方式二：手动启动
   java -Xms512m -Xmx1024m -jar coal-erp-business/target/coal-erp-business-*.jar --spring.profiles.active=prod

4. 后台运行（使用nohup）：
   nohup java -Xms512m -Xmx1024m -jar coal-erp-business/target/coal-erp-business-*.jar --spring.profiles.active=prod > logs/backend.log 2>&1 &

5. 使用systemd服务（推荐）：
   创建服务文件 /etc/systemd/system/coal-erp.service：
   
   [Unit]
   Description=Coal ERP System
   After=network.target mysql.service
   
   [Service]
   Type=simple
   User=your-user
   WorkingDirectory=/path/to/project
   ExecStart=/usr/bin/java -Xms512m -Xmx1024m -jar /path/to/project/coal-erp-business/target/coal-erp-business-*.jar --spring.profiles.active=prod
   Restart=always
   RestartSec=10
   
   [Install]
   WantedBy=multi-user.target
   
   启动服务：
   sudo systemctl daemon-reload
   sudo systemctl start coal-erp
   sudo systemctl enable coal-erp

3.4 前端部署

1. 进入frontend目录

2. 安装依赖：
   npm install
   或使用国内镜像：
   npm install --registry=https://registry.npmmirror.com

3. 配置API地址：
   编辑 frontend/src/config/api.ts

4. 开发环境启动：
   chmod +x start-frontend.sh
   ./start-frontend.sh
   或手动执行：
   npm run dev

5. 生产环境构建：
   npm run build

6. 使用Nginx部署（推荐）：
   
   a) 安装Nginx：
   sudo apt install nginx
   
   b) 配置Nginx：
   编辑 /etc/nginx/sites-available/coal-erp：
   
   server {
       listen 80;
       server_name your-domain.com;
       
       root /path/to/project/frontend/dist;
       index index.html;
       
       location / {
           try_files $uri $uri/ /index.html;
       }
       
       location /api {
           proxy_pass http://localhost:8080;
           proxy_set_header Host $host;
           proxy_set_header X-Real-IP $remote_addr;
           proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
       }
   }
   
   c) 启用配置：
   sudo ln -s /etc/nginx/sites-available/coal-erp /etc/nginx/sites-enabled/
   sudo nginx -t
   sudo systemctl reload nginx

3.5 防火墙配置

1. 开放端口：
   Ubuntu/Debian:
   sudo ufw allow 8080/tcp
   sudo ufw allow 80/tcp
   sudo ufw allow 443/tcp
   
   CentOS/RHEL:
   sudo firewall-cmd --permanent --add-port=8080/tcp
   sudo firewall-cmd --permanent --add-port=80/tcp
   sudo firewall-cmd --permanent --add-port=443/tcp
   sudo firewall-cmd --reload

========================================
四、常见问题
========================================

4.1 后端启动失败

1. 检查Java版本：java -version（需要1.8+）
2. 检查端口占用：netstat -ano | findstr 8080（Windows）或 lsof -i:8080（Linux）
3. 检查数据库连接：确认MySQL服务运行正常，数据库连接信息正确
4. 查看日志：logs/coal-erp-dev.log 或 logs/coal-erp-prod.log

4.2 前端启动失败

1. 检查Node.js版本：node -v（需要16+）
2. 清除缓存：删除 node_modules 和 package-lock.json，重新 npm install
3. 检查端口占用：netstat -ano | findstr 5173（Windows）或 lsof -i:5173（Linux）

4.3 数据库连接失败

1. 检查MySQL服务是否运行
2. 检查数据库用户名和密码
3. 检查防火墙是否阻止3306端口
4. 检查MySQL是否允许远程连接（如果需要）

4.4 权限问题

1. 执行SQL脚本初始化权限
2. 使用admin/admin123登录
3. 在系统管理中配置用户和角色

===============================
五、启动脚本说明
===============================

Windows系统：
- start-backend.bat：启动后端服务
- start-frontend.bat：启动前端服务
- start-all.bat：同时启动后端和前端

Linux系统：
- start-backend.sh：启动后端服务
- start-frontend.sh：启动前端服务
- start-all.sh：同时启动后端和前端

使用方法：
1. Windows：双击.bat文件即可
2. Linux：chmod +x *.sh 然后 ./start-*.sh

==========================
六、生产环境建议
==========================

1. 使用Nginx作为反向代理
2. 配置HTTPS证书
3. 定期备份数据库
4. 配置日志轮转
5. 监控系统资源使用情况
6. 设置自动重启机制
7. 配置防火墙规则
8. 定期更新系统和依赖

=========================
七、技术支持
=========================

如遇到问题，请检查：
1. 日志文件（logs目录）
2. 数据库连接配置
3. 端口占用情况
4. 防火墙设置
5. 系统资源（内存、磁盘空间）

==========================
