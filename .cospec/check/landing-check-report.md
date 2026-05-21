# 架构落地检查报告

## 检查信息

| 项目 | 内容 |
|------|------|
| 检查时间 | 2025-07-07 |
| 架构设计文档 | .cospec/architecture/design.md |
| 仓库根路径 | d:/workspace/0530/AMS |

## 检查总评

### 评分卡

**总分：0 / 100** ❌ 不通过

| 扣分明细 | 扣分 |
|---------|------|
| 高危扣分小计（1项 × 15分） | -15 |
| 中风险扣分小计（16项 × 5分） | -80 |
| 低风险扣分小计（23项 × 2分） | -46 |

| 维度 | 初始分 | 扣分 | 得分 |
|------|-------|------|------|
| 项目目录结构 | 100 | -14 | 86 |
| ER模型 | 100 | -4 | 96 |
| 技术栈合规 | 100 | -23 | 77 |
| 安全规范 | 100 | -55 | 45 |
| 对外API | 100 | -8 | 92 |
| 系统集成关系 | 100 | -2 | 98 |
| 日志规范 | 100 | -35 | 65 |
| **总计** | - | - | **0** |

| 维度 | 高危 | 中风险 | 低风险 | 得分 | 结论 |
|------|------|--------|--------|------|------|
| 项目目录结构 | 0 | 0 | 7 | 86 | 通过 |
| ER模型 | 0 | 0 | 2 | 96 | 通过 |
| 技术栈合规 | 0 | 1 | 9 | 77 | 通过 |
| 安全规范 | 0 | 11 | 0 | 45 | 不通过 |
| 对外API | 0 | 0 | 4 | 92 | 通过 |
| 系统集成关系 | 0 | 0 | 1 | 98 | 通过 |
| 日志规范 | 1 | 4 | 0 | 65 | 不通过 |
| **总计** | **1** | **16** | **23** | **0** | **不通过** |

---

## 1. 项目目录结构检查

### 1.1 设计文档规划的目录结构

```
ams/                     # 项目根目录（前后端一体化仓库）
├── ams-web/             # 前端工程模块
│   ├── src/
│   │   ├── api/         # 后端接口请求封装
│   │   ├── assets/      # 静态资源（图片/样式/字体）
│   │   ├── components/  # 公共可复用组件
│   │   ├── layouts/     # 页面布局组件
│   │   ├── router/      # 前端路由配置
│   │   ├── stores/      # 全局状态管理（Pinia）
│   │   ├── utils/       # 前端工具函数
│   │   └── views/       # 业务页面组件
│   │       ├── system/      # 系统清单管理页面
│   │       ├── techstack/   # 技术栈管理页面
│   │       ├── diagram/     # 逻辑部署架构图页面
│   │       ├── relation/    # 调用关系管理页面
│   │       └── user/        # 用户与权限管理页面
│   ├── public/          # 静态入口资源
│   └── package.json     # 前端依赖&脚本配置
├── ams-app/             # 后端工程模块（SpringBoot单体）
│   ├── src/main/java/com/cmhk/ams/
│   │   ├── controller/      # API 接口控制层
│   │   ├── service/         # 业务逻辑层
│   │   │   └── impl/
│   │   ├── model/           # 数据模型（DO/DTO/VO）
│   │   ├── mapper/          # 数据库访问层（MyBatis）
│   │   ├── config/          # 项目配置类
│   │   ├── exception/       # 全局异常处理
│   │   ├── security/        # 认证与权限控制
│   │   └── AmsApplication.java  # 启动类
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   ├── application-dev.yml
│   │   ├── application-test.yml
│   │   └── application-prod.yml
│   └── pom.xml
├── deploy/              # 自动化部署配置目录
│   └── docker/
│       ├── Dockerfile-web
│       └── Dockerfile-app
├── .gitignore
└── README.md
```

### 1.2 代码实际目录结构

代码仓库包含设计文档中声明的所有目录结构，同时存在以下未在设计中声明的额外目录：`ams-web/src/directives/`、`ams-app/.../model/enums/`、`ams-app/.../model/excel/`、`ams-app/.../model/dto/`、`ams-app/.../model/vo/`、`ams-app/.../util/`、`ams-app/src/main/resources/mapper/`。

### 1.3 差异分析

| 目录/模块 | 设计文档 | 实际代码 | 状态 | 严重级别 |
|---------|---------|---------|------|---------|
| `ams-web/src/directives/` | - | 存在（权限指令） | 多余 | 低风险 |
| `ams-app/.../model/enums/` | - | 存在（枚举类） | 多余 | 低风险 |
| `ams-app/.../model/excel/` | - | 存在（Excel导入导出DTO） | 多余 | 低风险 |
| `ams-app/.../model/dto/` | - | 存在（DTO子目录） | 多余 | 低风险 |
| `ams-app/.../model/vo/` | - | 存在（VO子目录） | 多余 | 低风险 |
| `ams-app/.../util/` | - | 存在（后端工具函数） | 多余 | 低风险 |
| `ams-app/src/main/resources/mapper/` | - | 存在（MyBatis XML映射） | 多余 | 低风险 |

---

## 2. ER模型检查

### 2.1 设计文档声明的数据模型

设计文档第4.1.1节列出了9张数据表：systems、sub_systems、deploy_sub_systems、release_units、tech_stacks、release_unit_tech_stack、release_unit_relations、users、roles，包含完整的字段定义、主键、外键、唯一约束等。

### 2.2 代码中实际数据模型

代码中实现了上述9张表的所有Java实体类及对应的schema.sql建表语句，同时额外实现了`diagram_layouts`表。

### 2.3 差异分析

| 实体/表 | 设计字段 | 实际字段 | 差异类型 | 状态 | 严重级别 |
|---------|---------|---------|---------|------|---------|
| `diagram_layouts` | - | 6个字段（id, deploy_sys_no, layout_data, remark, created_at, updated_at） | 多余表 | 代码存在但设计ER模型未声明 | 低风险 |
| `systems.split_from_other` | ER图：VARCHAR；明细表：TINYINT(1) | TINYINT | 内部不一致 | 代码与明细表一致，ER图与明细表类型分歧 | 低风险 |

其余9张声明表的所有字段均完全一致。

---

## 3. 技术栈合规检查

### 3.1 设计文档声明的技术栈

| 层级 | 分类 | 技术栈 | 版本 |
|------|------|--------|------|
| 前端 | 开发语言 | TypeScript | 5.8+ |
| 前端 | 开发框架 | Vue | 3.5+ |
| 前端 | 开源组件 | Element Plus | 2.10+ |
| 前端 | 构建组件 | Vite | 6.3+ |
| 前端 | 状态管理 | Pinia | 3.0+ |
| 后端 | 开发语言 | Java (JDK 17) | - |
| 后端 | 开发框架 | Spring Boot | 3.5+ |
| 后端 | 开源组件 | MyBatis | 3.5+ |
| 后端 | 开源组件 | MyBatis Plus | 3.5+ |
| 后端 | 开源组件 | Knife4j | 4.5+ |
| 后端 | 开源组件 | Lombok | 1.18+ |
| 后端 | 开源组件 | Fastjson2 | 2.0+ |
| 后端 | 构建组件 | Maven | 3.9+ |
| 数据库 | 关系数据库 | TDSQL | 8.0 |

### 3.2 代码中实际技术栈

从 pom.xml 和 package.json 中提取。

### 3.3 差异分析

| 技术栈项 | 设计文档 | 实际代码 | 企业清单标签 | 状态 | 严重级别 |
|---------|---------|---------|------------|------|---------|
| Spring Boot | v3.5+ | 3.2.5 | 管控 | 版本偏低 | 中风险 |
| Knife4j | v4.5+ | 4.4.0 | 不在清单 | 版本偏低 | 低风险 |
| MyBatis | v3.5+ | 未显式声明（内嵌于MP） | 不在清单 | 声明未显式使用 | 低风险 |
| Pinia | v3.0+ | ^3.0.0 | 不在清单 | 不在企业清单 | 低风险 |
| TDSQL | v8.0（推荐） | 开发环境用SQLite | 推荐 | 环境不一致 | 低风险 |
| jjwt (JWT) | 未声明 | 0.12.5 | 不在清单 | 实际使用未声明 | 低风险 |
| EasyExcel | 未声明 | 3.3.4 | 不在清单 | 实际使用未声明 | 低风险 |
| Vue Router | 未声明 | ^4.5.0 | 不在清单 | 实际使用未声明 | 低风险 |
| Axios | 未声明 | ^1.8.0 | 不在清单 | 实际使用未声明 | 低风险 |
| Mermaid | 未声明 | ^11.0.0 | 不在清单 | 实际使用未声明 | 低风险 |

---

## 4. 对外API检查

### 4.1 设计文档声明的对外API

设计文档3.1节声明"OpenAPI对外接口：无"，3.2节声明"依赖的外部接口：无"。

### 4.2 代码中实际实现的API

实际实现了约40+个API，涵盖系统、子系统、部署子系统、发布单元、技术栈、用户角色、调用关系、架构图、字典等全部业务模块的增删改查及Excel导入导出。

### 4.3 差异分析

| API | 设计声明 | 代码实现 | 差异类型 | 状态 | 严重级别 |
|-----|---------|---------|---------|------|---------|
| 全部业务API | 声明"无对外API" | 约40+个API | 设计缺失 | 代码实现但设计未声明 | 低风险 |
| GET /health/live | 声明（6.3节监控方案） | 实现 | 一致 | 一致 | - |
| GET /health/ready | 声明（6.3节监控方案） | 实现 | 一致 | 一致 | - |

**安全要求差异**：

| 设计声明 | 代码实际 | 差异说明 | 严重级别 |
|---------|---------|---------|---------|
| 公开白名单仅限登录相关接口 | 额外公开了Swagger文档、健康检查、OPTIONS预检 | 白名单超范围 | 低风险 |

---

## 5. 系统集成关系检查

### 5.1 集成点声明 vs 代码实现

| 集成系统 | 设计声明 | 代码实现 | 状态 | 严重级别 |
|---------|---------|---------|------|---------|
| NUC认证 | 无（自有账号） | 无 | 一致 | - |
| 招商随行 | 无 | 无 | 一致 | - |
| 信创容器（Docker） | 前端nginx + 后端openjdk:17 | deploy/docker/下存在Dockerfile | 一致 | - |
| LB/KONG网关 | HTTPS→LB→KONG→HTTP | 代码无实现（基础设施层） | 一致 | - |
| TDSQL数据库 | TDSQL 8.0 主库 | dev用SQLite，缺少prod配置 | 配置差异 | 低风险 |

---

## 6. 安全规范检查

### 6.1 代码实际安全实现

[从安全规范合规子任务提取]

### 6.2 差异分析

| 检查维度 | 规范要求 | 代码实际 | 状态 | 严重级别 |
|---------|----------|---------|------|----------|
| 输入校验 | 所有接口须使用 @Valid / @NotNull 等参数校验注解 | 所有 DTO 类均未使用任何校验注解 | 不合规 | 中风险 |
| 输入校验 | 服务端输出须做 XSS 转义 | 未搜索到 XSSFilter、HtmlUtils 等任何 XSS 防护代码 | 不合规 | 中风险 |
| 输入校验 | 文件上传须校验 MIME + 扩展名 + 大小 | 5 个导入接口直接使用 file.getInputStream()，未校验 | 不合规 | 中风险 |
| 敏感数据 | 密钥须使用 KMS/Vault 管理，禁止硬编码 | JWT secret 明文硬编码在 application.yml | 不合规 | 中风险 |
| 敏感数据 | 手机号/身份证等脱敏展示 | 未搜索到脱敏工具或使用 | 不合规 | 中风险 |
| 敏感数据 | 所有传输须使用 HTTPS | 未配置 server.ssl | 不合规 | 中风险 |
| 接口防护 | 登录等接口须限流（同一IP每分钟≤5次） | 未搜索到任何限流实现 | 不合规 | 中风险 |
| 接口防护 | 敏感操作须二次验证 | 未搜索到二次验证逻辑 | 不合规 | 中风险 |
| 接口防护 | 关键接口须 Nonce+时间戳防重放 | 未搜索到 nonce/timestamp 校验 | 不合规 | 中风险 |
| 防越权 | 数据操作前须校验数据归属权 | 未搜索到 checkOwnership 等校验 | 不合规 | 中风险 |
| 认证授权 | 所有需角色控制接口须使用 RBAC 注解 | @RequireRole 仅用于 UserController 3个方法 | 不合规 | 中风险 |

---

## 7. 日志规范检查

### 7.1 代码实际日志实现

[从日志规范子任务提取]

### 7.2 差异分析

| 检查维度 | 规范要求 | 代码实际 | 状态 | 严重级别 |
|---------|----------|---------|------|----------|
| 日志配置 | 须存在日志配置文件（logback.xml / log4j2.xml 等） | 搜索 logback*.xml、log4j*.xml 均无结果 | 不合规 | **高危** |
| 链路追踪 MDC | 须有 TraceFilter 读取 B3 字段写入 MDC（4个字段） | 未搜索到任何 MDC.put() 调用；无 TraceFilter 类 | 不合规 | 中风险 |
| 访问日志 Filter | 须实现 AccessLogFilter，使用独立 ACCESS_LOG Logger | 仅存在 JwtAuthenticationFilter，无 AccessLogFilter | 不合规 | 中风险 |
| 敏感信息脱敏 | 须存在 SensitiveUtil（含 maskPhone 等方法） | 未搜索到 SensitiveUtil 或任何脱敏方法 | 不合规 | 中风险 |
| JVM GC 日志参数 | 启动脚本/Dockerfile 须配置 10 项 GC 日志参数 | start-backend.bat 和 Dockerfile-app 均无任何 JVM 参数 | 不合规 | 中风险 |

---

## 8. 问题清单

### 8.1 高危问题（必须修复）

| 编号 | 维度 | 问题描述 | 涉及文件 | 修复建议 | 可自动化 |
|------|------|---------|---------|---------|---------|
| H-001 | 日志规范 | 缺少日志配置文件（logback.xml 或 log4j2.xml），违反企业日志规范强制总则 | ams-app/src/main/resources/ | 添加 logback-spring.xml 配置文件，按企业规范配置日志级别、输出格式、滚动策略 | 否 |

### 8.2 中风险问题（建议修复）

| 编号 | 维度 | 问题描述 | 涉及文件 | 修复建议 | 可自动化 |
|------|------|---------|---------|---------|---------|
| M-001 | 技术栈 | Spring Boot 实际版本3.2.5低于设计声明3.5+，且属企业管控技术未经审批 | ams-app/pom.xml | 升级 Spring Boot 至3.5+版本，补充管控技术审批说明 | 是 |
| M-002 | 日志规范 | 缺少链路追踪 TraceFilter，未在MDC中设置 B3 TraceId 等字段 | 全局范围 | 实现 TraceFilter 过滤器，从请求头读取 B3 字段并写入 MDC | 否 |
| M-003 | 日志规范 | 缺少 AccessLogFilter，未使用独立 ACCESS_LOG Logger 记录访问日志 | 全局范围 | 实现 AccessLogFilter 继承 OncePerRequestFilter，使用独立 Logger | 否 |
| M-004 | 日志规范 | 缺少 SensitiveUtil 脱敏工具类 | 全局范围 | 实现敏感信息脱敏工具类 | 否 |
| M-005 | 日志规范 | 启动脚本/Dockerfile 缺少 JVM GC 日志参数 | ams-app/start-backend.bat, deploy/docker/Dockerfile-app | 在启动脚本和 Dockerfile 中配置 -XX:+PrintGCDetails 等 GC 参数 | 是 |
| M-006 | 安全规范 | 所有 DTO 类缺少 @Valid/@NotNull 等参数校验注解 | ams-app/.../model/dto/ 下所有 DTO | 为各字段添加 @NotBlank、@NotNull、@Size 等校验注解，Controller 入参添加 @Valid | 是 |
| M-007 | 安全规范 | 缺少 XSS 防护过滤 | 全局范围 | 实现 XSSFilter 或使用 Spring 的 HtmlUtils 对输出做转义 | 否 |
| M-008 | 安全规范 | 文件上传缺少 MIME/扩展名/大小校验 | DeploySubSystemController, SubSystemController, SystemController, TechStackController | 添加文件类型白名单校验（MIME+扩展名双重校验）及文件大小上限检查 | 是 |
| M-009 | 安全规范 | JWT secret 明文硬编码在配置文件中 | ams-app/src/main/resources/application.yml | 通过环境变量注入替换硬编码值，生产环境使用 KMS/Vault 管理 | 是 |
| M-010 | 安全规范 | 缺少敏感数据脱敏处理 | 全局范围 | 实现数据脱敏方案，对手机号、身份证等敏感字段脱敏展示 | 否 |
| M-011 | 安全规范 | 未配置 HTTPS/SSL | ams-app/src/main/resources/application.yml | 配置 server.ssl 证书及端口，生产环境强制 HTTPS | 是 |
| M-012 | 安全规范 | 缺少接口限流保护 | 全局范围 | 实现 RateLimiter（如 Bucket4j 或基于 Redis）对登录等接口限流 | 否 |
| M-013 | 安全规范 | 缺少敏感操作二次验证 | AuthController | 对密码修改、权限变更等操作增加短信验证码或原密码确认 | 否 |
| M-014 | 安全规范 | 缺少防重放攻击机制 | 全局范围 | 关键接口增加 Nonce+时间戳校验 | 否 |
| M-015 | 安全规范 | 缺少数据归属权校验 | 全局范围 | 数据操作前校验当前用户的数据归属权 | 否 |
| M-016 | 安全规范 | RBAC 角色注解覆盖不全，仅 UserController 3个方法有 @RequireRole | 各 Controller | 在所有需要权限控制的接口上补充 @RequireRole 注解 | 是 |

### 8.3 低风险问题（可选修复）

| 编号 | 维度 | 问题描述 | 涉及文件 | 修复建议 | 可自动化 |
|------|------|---------|---------|---------|---------|
| L-001 | 目录结构 | `ams-web/src/directives/` 目录未在设计文档中声明 | design.md | 补充声明该目录 | 否 |
| L-002 | 目录结构 | `ams-app/.../model/enums/` 目录未在设计文档中声明 | design.md | 补充声明该目录 | 否 |
| L-003 | 目录结构 | `ams-app/.../model/excel/` 目录未在设计文档中声明 | design.md | 补充声明该目录 | 否 |
| L-004 | 目录结构 | `ams-app/.../model/dto/` 目录未在设计文档中声明 | design.md | 补充声明该目录 | 否 |
| L-005 | 目录结构 | `ams-app/.../model/vo/` 目录未在设计文档中声明 | design.md | 补充声明该目录 | 否 |
| L-006 | 目录结构 | `ams-app/.../util/` 目录未在设计文档中声明 | design.md | 补充声明该目录 | 否 |
| L-007 | 目录结构 | `ams-app/src/main/resources/mapper/` 目录未在设计文档中声明 | design.md | 补充声明该目录 | 否 |
| L-008 | ER模型 | `diagram_layouts` 表代码存在但设计ER模型未声明 | design.md | 在ER模型中补充 diagram_layouts 表定义 | 否 |
| L-009 | ER模型 | `systems.split_from_other` ER图与明细表类型不一致（ER图VARCHAR vs 明细表TINYINT） | design.md | 统一字段类型，建议以TINYINT为准 | 是 |
| L-010 | 技术栈 | Knife4j 版本4.4.0低于设计声明4.5+ | ams-app/pom.xml | 升级至4.5+版本 | 是 |
| L-011 | 技术栈 | MyBatis 声明但未显式依赖（内嵌于MyBatis Plus） | design.md | 设计文档可说明由MP内嵌提供 | 否 |
| L-012 | 技术栈 | Pinia 不在企业技术栈清单中 | 无 | 确认是否允许使用并补充企业清单 | 否 |
| L-013 | 技术栈 | 开发环境使用 SQLite 而非 TDSQL | ams-app/src/main/resources/application-dev.yml | 补充 TDSQL 驱动配置，统一开发环境数据库 | 是 |
| L-014 | 技术栈 | jjwt (JWT) 实际使用但设计未声明 | design.md | 在技术选型表中补充声明 | 否 |
| L-015 | 技术栈 | EasyExcel 实际使用但设计未声明 | design.md | 在技术选型表中补充声明 | 否 |
| L-016 | 技术栈 | Vue Router 实际使用但设计未声明 | design.md | 在技术选型表中补充声明 | 否 |
| L-017 | 技术栈 | Axios 实际使用但设计未声明 | design.md | 在技术选型表中补充声明 | 否 |
| L-018 | 技术栈 | Mermaid 实际使用但设计未声明 | design.md | 在技术选型表中补充声明 | 否 |
| L-019 | 对外API | 设计文档声明"无对外API"，实际实现约40+个API | design.md | 补充完整的API接口设计列表 | 否 |
| L-020 | 对外API | 安全白名单额外公开了 Swagger 文档接口 | SecurityConfig.java | 在设计文档中补充说明Swagger白名单的合理性 | 否 |
| L-021 | 对外API | 安全白名单额外公开了健康检查接口 | SecurityConfig.java | 在设计文档中补充说明健康检查白名单的合理性 | 否 |
| L-022 | 对外API | 安全白名单额外公开了OPTIONS预检请求 | SecurityConfig.java | 在设计文档中补充说明CORS预检白名单的合理性 | 否 |
| L-023 | 系统集成 | 开发环境 TDSQL 配置缺失，使用 SQLite 替代 | application-dev.yml, application-prod.yml | 补充 application-prod.yml 生产环境配置 | 是 |

---

## 9. 修复建议与下一步

### 9.1 修复建议汇总

| 优先级 | 问题编号 | 维度 | 修复建议 | 涉及文件 | 预估工作量 | 可自动化 |
|--------|---------|------|---------|---------|-----------|---------|
| P0 | H-001 | 日志规范 | 添加 logback-spring.xml 日志配置文件 | ams-app/src/main/resources/ | 0.5h | 否 |
| P1 | M-001 | 技术栈 | 升级 Spring Boot 至3.5+，补充管控审批 | ams-app/pom.xml | 1h | 是 |
| P1 | M-002 | 日志规范 | 实现 TraceFilter 链路追踪 | 全局 | 1h | 否 |
| P1 | M-003 | 日志规范 | 实现 AccessLogFilter 访问日志 | 全局 | 0.5h | 否 |
| P1 | M-004 | 日志规范 | 实现 SensitiveUtil 脱敏工具 | 全局 | 0.5h | 否 |
| P1 | M-005 | 日志规范 | 启动脚本添加 JVM GC 参数 | start-backend.bat, Dockerfile-app | 0.5h | 是 |
| P1 | M-006~M-016 | 安全规范 | 依次修复11项中风险安全合规问题 | 多文件 | 8h | 部分 |
| P2 | L-001~L-023 | 多维度 | 补充设计文档记录与代码一致 | design.md 等多文件 | 3h | 部分 |

### 9.2 下一步操作

- **❌ 检查未通过（存在高危问题）**：必须修复全部高危项后方可重检。
  1. 立即修复 **高危问题 H-001**（日志配置文件缺失）
  2. 同时修复 **16项中风险问题**（建议按优先级 P1 顺序）
  3. 可选修复 **23项低风险问题**
  4. 修复完成后请重新运行架构落地检查

> 💡 以上问题清单即为修复建议清单。请根据修复建议逐项修复，修复完成后请重新运行架构落地检查。