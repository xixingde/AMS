# 架构管理系统 AMS

## 项目简介
架构管理系统（Architecture Management System，AMS）面向企业 IT 架构资产管理场景，提供系统、子系统、部署子系统、发布单元、技术栈的统一维护，支持基于 Mermaid 的逻辑部署架构图自动生成与手动调整，以及发布单元调用关系管理。

## 技术栈

### 后端
- Java 17
- Spring Boot 3.2.5
- MyBatis Plus 3.5.5
- Spring Security + JWT
- Knife4j (OpenAPI 文档)
- SQLite / MySQL

### 前端
- Vue 3.5+
- Vite 6.3+
- TypeScript 5.8+
- Element Plus 2.10+
- Pinia 3.0+
- Vue Router 4.5+
- Mermaid.js 11.0+

## 目录结构
```
ams/
├── ams-app/          # 后端工程（Spring Boot）
├── ams-web/          # 前端工程（Vue 3 + Vite）
├── deploy/docker/    # Docker 部署配置
└── ams-output/       # 项目文档输出
```

## 本地启动

### 后端
```bash
cd ams-app
mvn clean package
java -jar target/ams-app-1.0.0.jar
```
后端默认运行在 http://localhost:8080

### 前端
```bash
cd ams-web
npm install
npm run dev
```
前端默认运行在 http://localhost:3000

### 默认账号
- 用户名：admin
- 密码：admin123

## API 文档
启动后端后访问：http://localhost:8080/doc.html

## Docker 构建

### 前端镜像
```bash
cd ams-web
npm run build
docker build -f ../deploy/docker/Dockerfile-web -t ams-web .
```

### 后端镜像
```bash
cd ams-app
mvn clean package
docker build -f ../deploy/docker/Dockerfile-app -t ams-app .
```
