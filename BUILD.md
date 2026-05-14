# 构建与启动指南

## 环境要求

- **后端**：JDK 17+、Maven 3.8+
- **前端**：Node.js 18+、npm 9+
- **数据库**：MySQL 8.0+（或 SQLite 降级模式）

## 快速启动

### Windows

1. **后端启动**：
   ```cmd
   cd ams-app
   start-backend.bat
   ```

2. **前端启动**：
   ```cmd
   cd ams-web
   start-frontend.bat
   ```

### 手动构建

#### 后端

```cmd
cd ams-app
mvn clean package -DskipTests
java -jar target/ams-app-1.0.0.jar --spring.profiles.active=dev
```

后端默认端口：`8080`
API 文档地址：http://localhost:8080/api/doc.html

#### 前端

```cmd
cd ams-web
npm install
npm run dev
```

前端默认端口：`5173`
访问地址：http://localhost:5173

## 数据库配置

项目默认使用 MySQL，配置文件位于 `ams-app/src/main/resources/application-dev.yml`。

如果本地无 MySQL，可修改为 SQLite（已含依赖）：

```yaml
spring:
  datasource:
    url: jdbc:sqlite:ams.db
    driver-class-name: org.sqlite.JDBC
```

> 注意：使用 SQLite 时部分 SQL 语法可能需要调整。

## 编译问题排查

1. **Lombok 注解未生效**：确保 IDE 已安装 Lombok 插件，或在命令行使用 `mvn clean compile`
2. **端口占用**：后端默认 8080，前端默认 5173，如果被占用请先关闭占用进程
3. **编码问题**：Windows 下如出现中文乱码，请确保使用 UTF-8 编码，或在 `mvn` 命令前添加 `chcp 65001`
