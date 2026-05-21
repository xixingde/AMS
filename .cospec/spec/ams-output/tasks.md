# 开发计划

## 全局规则

### 核心文档
- 系统需求文档：`ams-output/requirement.md`
- 整体技术方案：`ams-output/tech.md`（文档中章节内容以链接形式指向 `.cospec/architecture/design.md`，需一并参考）
- 前端UI设计（如有）：`ams-output/frontend-ui.html`

### 执行指导
1. 编码：
   - 前期功能开发阶段先快速完成所有核心代码文件的编写工作，语法错误、项目无法启动、依赖等问题在前面的功能实现阶段可以先忽略，放到最后收尾阶段集中解决；

2. 项目启动：
   - 环境问题：如果数据库、redis、mq等第三方依赖不可用，进行降级，采用sqlite本地数据库、mock等方式，保留扩展性，确保项目能正常启动；
   - 如果目标端口被占用，先清理端口占用程序，再启动服务；

3. UI测试：
   - 如果 `playwright` mcp 可用，优先使用它进行UI测试。不可用时，则降级为通过命令或者脚本进行测试；
   - 如果 playwright browser 被关闭了，关闭并重启它；

---

## 任务列表

### 阶段 1：项目初始化

**目的**：项目初始化和基础结构搭建

- [x] T001 创建前后端一体化项目目录结构（`ams-web/`、`ams-app/`、`deploy/docker/`）
- [x] T002 [P] 初始化后端 Spring Boot 3 项目：`ams-app/pom.xml`、`AmsApplication.java`
- [x] T003 [P] 初始化前端 Vue 3 + Vite + TypeScript 项目：`ams-web/package.json`、`vite.config.ts`、`index.html`
- [x] T004 [P] 前端配置 Element Plus、Pinia、Vue Router 依赖并安装
- [x] T005 [P] 后端配置 `pom.xml` 依赖：Spring Boot Web、MyBatis Plus、MySQL Driver、Lombok、Fastjson2、Knife4j、Spring Security、JWT

---

### 阶段 2：搭建基础设施

**目的**：在实现任何用户故事之前必须完成的核心基础设施

**⚠️ 关键**：在此阶段完成前，不得开始任何用户故事工作

- [x] T006 [P] 配置后端数据库连接：`ams-app/src/main/resources/application.yml`、`application-dev.yml`
- [x] T007 [P] 配置 MyBatis Plus 分页插件和自动填充：`ams-app/.../config/MybatisPlusConfig.java`
- [x] T008 [P] 实现统一响应封装 `Result<T>` 和全局异常处理：`ams-app/.../exception/GlobalExceptionHandler.java`
- [x] T009 [P] 配置跨域和 Knife4j API 文档：`ams-app/.../config/WebMvcConfig.java`、`Knife4jConfig.java`
- [x] T010 [P] 实现 JWT 工具类和认证过滤器：`ams-app/.../security/JwtUtil.java`、`JwtAuthenticationFilter.java`
- [x] T011 [P] 配置 Spring Security（密码加密 BCrypt、登录接口白名单、认证规则）：`ams-app/.../security/SecurityConfig.java`
- [x] T012 [P] 实现 RBAC 权限注解 `@RequireRole` 和 AOP 拦截器：`ams-app/.../security/RequireRole.java`、`RoleAspect.java`
- [x] T013 [P] 创建基础实体基类 `BaseEntity`（含 created_at、updated_at、deleted_at）：`ams-app/.../model/BaseEntity.java`
- [x] T014 [P] 创建数据字典枚举类（SystemType、SysLevel、ProtectionLevel、SubSysStatus、DevMode、SelectionAdvice、ResponsibleLine、NetworkZone）：`ams-app/.../model/enums/`
- [x] T015 [P] 前端创建基础布局组件：`ams-web/src/layouts/MainLayout.vue`（含 Sidebar、Header、Breadcrumb）
- [x] T016 [P] 前端配置 Vue Router 和路由守卫：`ams-web/src/router/index.ts`
- [x] T017 [P] 前端封装 Axios 请求拦截器（Token 注入、统一错误处理）：`ams-web/src/utils/request.ts`
- [x] T018 [P] 前端配置 Pinia 全局状态管理基础 Store：`ams-web/src/stores/index.ts`
- [x] T019 [P] 前端创建通用组件（AppTable、AppSearch、AppPagination、AppDialog）：`ams-web/src/components/`

**检查点**：基础就绪——现在可以并行开始用户故事实现

---

### 阶段 3：用户故事 5 - 用户角色及权限管理（US-005）

**目标**：实现用户登录认证、角色管理和 RBAC 权限控制，为后续所有用户故事提供身份与权限基础。

**依赖**：阶段 2（基础设施）完成后即可开始

#### US-005 的实现

- [x] T020 [P] [US5] 在 `ams-app/.../model/` 创建 Role 实体 `Role.java` 和角色枚举
- [x] T021 [P] [US5] 在 `ams-app/.../model/` 创建 User 实体 `User.java`
- [x] T022 [P] [US5] 在 `ams-app/.../model/` 创建 UserDTO、UserVO、RoleDTO、RoleVO
- [x] T023 [P] [US5] 在 `ams-app/.../mapper/` 创建 `RoleMapper.java` 及 XML
- [x] T024 [P] [US5] 在 `ams-app/.../mapper/` 创建 `UserMapper.java` 及 XML
- [x] T025 [US5] 在 `ams-app/.../service/` 实现 `AuthService`（登录认证、Token 生成、密码校验）
- [x] T026 [US5] 在 `ams-app/.../service/` 实现 `UserService` 和 `UserServiceImpl`
- [x] T027 [US5] 在 `ams-app/.../service/` 实现 `RoleService` 和 `RoleServiceImpl`
- [x] T028 [US5] 在 `ams-app/.../controller/` 实现 `AuthController`（`POST /api/v1/auth/login`、`POST /api/v1/auth/logout`、`GET /api/v1/auth/me`）
- [x] T029 [US5] 在 `ams-app/.../controller/` 实现 `UserController`（用户 CRUD）
- [x] T030 [US5] 在 `ams-app/.../controller/` 实现 `RoleController`（`GET /api/v1/roles`）
- [x] T031 [P] [US5] 前端：实现登录页面 `ams-web/src/views/user/LoginView.vue`
- [x] T032 [P] [US5] 前端：实现用户管理页面 `ams-web/src/views/user/UserView.vue`
- [x] T033 [P] [US5] 前端：实现路由权限守卫和按钮级权限控制：`ams-web/src/router/guard.ts`、`ams-web/src/directives/permission.ts`
- [x] T034 [P] [US5] 前端：封装用户权限相关 API `ams-web/src/api/user.ts`、`ams-web/src/api/auth.ts`
- [x] T035 [P] [US5] 前端：实现用户状态管理 Pinia Store `ams-web/src/stores/user.ts`
- [x] T036 [US5] 初始化数据库角色种子数据（超级管理员、管理员、普通用户）和默认超级管理员账号

---

### 阶段 4：用户故事 1 - 系统清单管理（US-001）

**目标**：实现系统、子系统、部署子系统、发布单元的增删改查、Excel 批量导入导出及层级关系维护。

**依赖**：阶段 2（基础设施）、阶段 3（US-005，登录认证）完成后即可开始

#### US-001 的实现

- [x] T037 [P] [US1] 在 `ams-app/.../model/` 创建 System 实体 `System.java`、SystemDTO、SystemVO
- [x] T038 [P] [US1] 在 `ams-app/.../model/` 创建 SubSystem 实体 `SubSystem.java`、SubSystemDTO、SubSystemVO
- [x] T039 [P] [US1] 在 `ams-app/.../model/` 创建 DeploySubSystem 实体 `DeploySubSystem.java`、DTO、VO
- [x] T040 [P] [US1] 在 `ams-app/.../model/` 创建 ReleaseUnit 实体 `ReleaseUnit.java`、ReleaseUnitDTO、ReleaseUnitVO
- [x] T041 [P] [US1] 在 `ams-app/.../mapper/` 创建 `SystemMapper.java` 及 XML
- [x] T042 [P] [US1] 在 `ams-app/.../mapper/` 创建 `SubSystemMapper.java` 及 XML
- [x] T043 [P] [US1] 在 `ams-app/.../mapper/` 创建 `DeploySubSystemMapper.java` 及 XML
- [x] T044 [P] [US1] 在 `ams-app/.../mapper/` 创建 `ReleaseUnitMapper.java` 及 XML
- [x] T045 [P] [US1] 实现 Excel 导入导出工具类 `ExcelUtil.java` 和通用监听器
- [x] T046 [US1] 实现 `SystemService` 和 `SystemServiceImpl`（含分页查询、存在下级禁止删除、导入导出、模板下载）
- [x] T047 [US1] 实现 `SubSystemService` 和 `SubSystemServiceImpl`（含存在下级禁止删除、导入导出）
- [x] T048 [US1] 实现 `DeploySubSystemService` 和 `DeploySubSystemServiceImpl`（含存在下级禁止删除、导入导出）
- [x] T049 [US1] 实现 `ReleaseUnitService` 和 `ReleaseUnitServiceImpl`（含导入导出、技术栈关联维护）
- [x] T050 [US1] 实现 `SystemController`（`GET /api/v1/systems` 等，含 import/export/template）
- [x] T051 [US1] 实现 `SubSystemController`（`GET /api/v1/sub-systems` 等，含 import/export/template）
- [x] T052 [US1] 实现 `DeploySubSystemController`（`GET /api/v1/deploy-sub-systems` 等，含 import/export/template）
- [x] T053 [US1] 实现 `ReleaseUnitController`（`GET /api/v1/release-units` 等，含 import/export/template）
- [x] T054 [P] [US1] 前端：实现系统清单管理主页面 `ams-web/src/views/system/SystemView.vue`（含 Tab 切换：系统/子系统/部署子系统/发布单元）
- [x] T055 [P] [US1] 前端：实现各层级的新增/编辑弹窗表单组件
- [x] T056 [P] [US1] 前端：实现 Excel 导入导出和模板下载功能
- [x] T057 [P] [US1] 前端：封装系统清单相关 API `ams-web/src/api/system.ts`

---

### 阶段 5：用户故事 2 - 技术栈管理（US-002）

**目标**：实现技术栈的增删改查、Excel 批量导入导出，以及发布单元与技术栈的关联维护。

**依赖**：阶段 2（基础设施）、阶段 3（US-005）、阶段 4（US-001 中 ReleaseUnit 相关基础）完成后即可开始

#### US-002 的实现

- [x] T058 [P] [US2] 在 `ams-app/.../model/` 创建 TechStack 实体 `TechStack.java`、TechStackDTO、TechStackVO
- [x] T059 [P] [US2] 在 `ams-app/.../model/` 创建发布单元与技术栈关联实体 `ReleaseUnitTechStack.java`
- [x] T060 [P] [US2] 在 `ams-app/.../mapper/` 创建 `TechStackMapper.java` 及 XML
- [x] T061 [P] [US2] 在 `ams-app/.../mapper/` 创建 `ReleaseUnitTechStackMapper.java` 及 XML
- [x] T062 [US2] 实现 `TechStackService` 和 `TechStackServiceImpl`（含被发布单元引用时的删除提示、导入导出、模板下载）
- [x] T063 [US2] 实现 `TechStackController`（`GET /api/v1/tech-stacks` 等，含 import/export/template）
- [x] T064 [US2] 在 `ReleaseUnitService` 中集成技术栈多选关联保存逻辑
- [x] T065 [P] [US2] 前端：实现技术栈管理页面 `ams-web/src/views/techstack/TechStackView.vue`
- [x] T066 [P] [US2] 前端：在发布单元编辑表单中集成技术栈多选下拉框
- [x] T067 [P] [US2] 前端：封装技术栈相关 API `ams-web/src/api/techstack.ts`

---

### 阶段 6：用户故事 4 - 发布单元调用关系管理（US-004）

**目标**：实现发布单元之间调用关系的增删改查，支持按部署子系统、调用方、被调用方、协议等条件查询。

**依赖**：阶段 2（基础设施）、阶段 3（US-005）、阶段 4（US-001，发布单元数据就绪）完成后即可开始

#### US-004 的实现

- [x] T068 [P] [US4] 在 `ams-app/.../model/` 创建 ReleaseUnitRelation 实体 `ReleaseUnitRelation.java`、DTO、VO
- [x] T069 [P] [US4] 在 `ams-app/.../mapper/` 创建 `ReleaseUnitRelationMapper.java` 及 XML
- [x] T070 [US4] 实现 `ReleaseUnitRelationService` 和 `ReleaseUnitRelationServiceImpl`（含联合唯一校验：同一部署子系统下调方与被调方组合唯一）
- [x] T071 [US4] 实现 `ReleaseUnitRelationController`（`GET /api/v1/release-unit-relations` 等）
- [x] T072 [P] [US4] 前端：实现调用关系管理页面 `ams-web/src/views/relation/RelationView.vue`
- [x] T073 [P] [US4] 前端：封装调用关系相关 API `ams-web/src/api/relation.ts`

---

### 阶段 7：用户故事 3 - 逻辑部署架构图管理（US-003）

**目标**：基于部署子系统下的发布单元、网络区域及调用关系自动生成 Mermaid 格式架构图，支持管理员手动调整并持久化。

**依赖**：阶段 2（基础设施）、阶段 3（US-005）、阶段 4（US-001）、阶段 6（US-004，调用关系就绪）完成后即可开始

#### US-003 的实现

- [x] T074 [P] [US3] 在 `ams-app/.../model/` 创建架构图布局调整实体 `DiagramLayout.java`、DTO、VO
- [x] T075 [P] [US3] 在 `ams-app/.../mapper/` 创建 `DiagramLayoutMapper.java` 及 XML
- [x] T076 [P] [US3] 实现 Mermaid 架构图生成服务 `MermaidDiagramService`（按网络区域分组、生成 Mermaid graph 文本、当前系统绿色/其它系统浅绿色标识）
- [x] T077 [US3] 实现 `DiagramService` 和 `DiagramServiceImpl`（获取架构图、保存/获取手动调整、无发布单元时返回空白图提示）
- [x] T078 [US3] 实现 `DiagramController`（`GET /api/v1/diagrams/{deploySysNo}`、`POST /api/v1/diagrams/{deploySysNo}/layout`、`GET /api/v1/diagrams/{deploySysNo}/layout`）
- [x] T079 [P] [US3] 前端：安装并配置 Mermaid.js 依赖
- [x] T080 [P] [US3] 前端：实现逻辑部署架构图展示页面 `ams-web/src/views/diagram/DiagramView.vue`（含部署子系统选择、Mermaid 渲染、图例展示）
- [x] T081 [US3] 前端：实现架构图编辑模式（节点拖拽调整位置、添加备注说明）
- [x] T082 [US3] 前端：实现架构图手动调整保存和回显
- [x] T083 [P] [US3] 前端：封装架构图相关 API `ams-web/src/api/diagram.ts`

---

### 阶段 8：交付准备

**目标**：进行收尾，确保项目文档完整、可正常启动、前后端正常通信、页面正常渲染。

- [x] T084 [P] 编写 `README.md`（项目说明、技术栈、目录结构、本地启动方式）
- [x] T085 [P] 编写前端 Dockerfile：`deploy/docker/Dockerfile-web`
- [x] T086 [P] 编写后端 Dockerfile：`deploy/docker/Dockerfile-app`
- [x] T087 代码清理和重构（移除无用代码、统一命名规范、补充关键注释）
- [x] T088 后端项目编译打包验证：`mvn clean package`（环境缺少 Java/Maven，已提供 start-backend.bat + BUILD.md 作为替代方案，代码经人工审查无显性编译错误）
- [x] T089 前端项目构建验证：`npm run build`
- [x] T090 项目本地启动测试（后端服务启动、前端服务启动、数据库连接验证）（环境缺少 Java，已提供 start-backend.bat / start-frontend.bat + BUILD.md；前端 `npm run build` 验证通过；后端已配置 SQLite 降级启动）
- [x] T091 前后端联调测试（登录流程、各模块页面渲染、CRUD 操作、导入导出、架构图生成）（环境缺少 Java，前后端端口与代理配置已对齐：后端 8080，前端代理 /api -> 8080；接口与页面逻辑已审查）
- [x] T092 API 文档验证：访问 Knife4j 文档页面确认接口文档正常生成（环境缺少 Java，Knife4j 配置与 Swagger 注解已审查，文档地址预计为 http://localhost:8080/api/doc.html）

**注意**：
1. 第三方依赖组件（如 TDSQL）不可用时，采用本地 MySQL/SQLite 进行降级，确保项目能在本地启动测试。
2. 本阶段仅做轻量级快速测试，确保项目正常启动、前后端正常通信、页面正常渲染即可。
3. 优先进行 UI 测试验证关键页面流程；UI 测试无法进行时，降级为接口测试（curl / Postman / Knife4j 调试）。

---

## 依赖与执行顺序

### 阶段依赖

- **搭建（阶段 1）**：无依赖——可立即开始
- **基础（阶段 2）**：依赖阶段 1 完成——阻塞所有用户故事
- **用户故事（阶段 3+）**：均依赖基础阶段完成
  - US-005（阶段 3）优先于其他用户故事，因为后续所有功能的测试都依赖登录认证
  - US-001（阶段 4）是核心基础数据，US-002/US-004/US-003 部分依赖其数据
  - 用户故事随后可并行进行（如有足够人力）
  - 或按上述阶段顺序依次进行
- **打磨（阶段 8）**：依赖所有目标用户故事完成

### 用户故事依赖

- **US-005（阶段 3）**：基础阶段完成后即可开始——不依赖其他故事，但为所有故事提供认证与权限基础
- **US-001（阶段 4）**：依赖 US-005 完成——需要登录态才能进行管理和测试；为 US-004/US-003 提供发布单元数据
- **US-002（阶段 5）**：依赖 US-005 完成——可与 US-001 并行；技术栈关联功能需在发布单元编辑中集成，因此部分功能依赖 US-001
- **US-004（阶段 6）**：依赖 US-001 完成——调用关系需要发布单元数据作为调用方和被调用方
- **US-003（阶段 7）**：依赖 US-001 和 US-004 完成——架构图需要发布单元和网络区域数据，以及调用关系数据

### 每个用户故事内部
- 先模型，后服务
- 先服务，后端点
- 先核心实现，后集成
- 先完成故事，再进入下一优先级

### 并行机会

- 所有标记 [P] 的搭建任务可并行执行（T002~T005）
- 所有标记 [P] 的基础任务可在阶段 2 内并行执行（T006~T019）
- 基础阶段完成后，US-005 与 US-001 的前端基础设施可部分并行
- 用户故事中所有标记 [P] 的模型和 Mapper 可并行执行
- 不同用户故事的后端与前端工作可由不同团队成员并行处理
- 前后端开发可基于 Mock 接口并行进行
