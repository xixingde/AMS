# 架构信息模型

## 系统基本信息
| 项目 | 内容 |
| --- | --- |
| 系统中文名 | 架构管理系统 |
| 系统英文名 | ams |
| 子系统中文名 | 架构管理系统子系统 |
| 子系统英文名 | ams |

## 技术栈信息

| 层级   | 分类           | 是否涉及 | 技术栈                | 版本     | 开源/商用 |
|--------|----------------|----------|-----------------------|----------|-----------|
| 前端   | 开发语言       | 是       | TypeScript            | 5.8+     | 开源      |
| 前端   | 开发框架       | 是       | Vue                   | 3.5+     | 开源      |
| 前端   | 开源组件       | 是       | Element Plus          | 2.10+    | 开源      |
| 前端   | 构建组件       | 是       | Vite                  | 6.3+     | 开源      |
| 前端   | 状态管理       | 是       | Pinia                 | 3.0+     | 开源      |
| 后端   | 开发语言       | 是       | Java                  | JDK 17   | 开源      |
| 后端   | 开发框架       | 是       | Spring Boot           | 3.5+     | 开源      |
| 后端   | 开源组件       | 是       | MyBatis               | 3.5+     | 开源      |
| 后端   | 开源组件       | 是       | MyBatis Plus          | 3.5+     | 开源      |
| 后端   | 开源组件       | 是       | Knife4j               | 4.5+     | 开源      |
| 后端   | 开源组件       | 是       | Lombok                | 1.18+    | 开源      |
| 后端   | 开源组件       | 是       | Fastjson2             | 2.0+     | 开源      |
| 后端   | 构建组件       | 是       | Maven                 | 3.9+     | 开源      |
| 数据库 | 关系数据库     | 是       | TDSQL                 | 8.0      | 商用      |
| 基础设施 | 容器/虚拟机   | 是       | 信创容器（K8s）       | —        | 商用      |
| 基础设施 | 操作系统      | 是       | 麒麟                  | —        | 商用      |
| 基础设施 | 芯片          | 是       | ARM（鲲鹏）           | —        | 商用      |

## 部署架构图

```mermaid
graph LR

    %% 样式定义：当前系统（绿色）、其它系统（浅绿色）
    classDef current fill:#90EE90,stroke:#333,stroke-width:1px;
    classDef other fill:#E6F7E6,stroke:#333,stroke-width:1px;

    subgraph INTRANET
        %%其它系统发布单元（浅绿色）
        BROWSER_PC[浏览器PC端]:::other
    end

    %%网络区域
    subgraph INTERNET
        %%其它系统发布单元（浅绿色）
        BROWSER_MOBILE[浏览器移动端]:::other
    end

    %%网络区域
    subgraph CMHK-XC-DMZ
        %%当前系统发布单元（绿色）
        LB[负载均衡 F5或软负载]:::current
    end

    %%网络区域
    subgraph CMHK-XC-CORE
        %%当前系统发布单元（绿色）
        KONG[API网关 KONG]:::current
        AMS_WEB[AMS子系统WEB前端服务]:::current
        AMS_APP[AMS子系统WEB后端服务]:::current
        TDSQL[TDSQL主库]:::current
    end

    %% 连接关系（带协议标注）
    BROWSER_PC -- HTTPS --> LB
    BROWSER_MOBILE -- HTTPS --> LB

    LB -- HTTP --> KONG
    KONG -- HTTP --> AMS_WEB
    KONG -- HTTP --> AMS_APP

    AMS_APP -- JDBC --> TDSQL
```
