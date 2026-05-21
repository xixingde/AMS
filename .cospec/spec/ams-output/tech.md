# 技术方案

## 发布单元列表
见 [`.cospec/architecture/design.md`](.cospec/architecture/design.md) 发布单元列表章节

## 技术栈
见 [`.cospec/architecture/design.md`](.cospec/architecture/design.md) 技术栈章节

## 项目目录结构

见 [`.cospec/architecture/design.md`](.cospec/architecture/design.md) 项目目录结构章节

## 系统内部接口

### 接口规范

- 统一url格式：
/{context-path}/api/v1/{resource}

- 统一返回格式：
正常：
```json
{
  "code": 0,
  "message": "success",
  "data": {}
}
```
异常：
```json
{
  "code": 10001,
  "message": "用户名已存在",
  "data": null
}
```

- 统一分页格式：
```json
请求：GET /api/v1/systems?page=1&pageSize=20

响应：
{
  "code": 0,
  "message": "success",
  "data": {
    "list": [],
    "total": 100,
    "page": 1,
    "pageSize": 20
  }
}
```

- Excel 导入：请求使用 `multipart/form-data`，响应返回导入结果摘要（成功数、失败数、失败明细）。
- Excel 导出：GET 请求，响应返回二进制文件流，`Content-Disposition: attachment`。

### 接口列表

#### 系统清单管理

| 接口名 | 请求方式 | 接口URL | 请求格式 | 响应格式 | 备注 |
|--------|----------|---------|----------|----------|------|
| 查询系统列表 | GET | /api/v1/systems | query | json | 支持按编号、名称、类型、属主、级别等分页查询 |
| 创建系统 | POST | /api/v1/systems | json | json | — |
| 查询系统详情 | GET | /api/v1/systems/{sysNo} | path | json | — |
| 更新系统 | PUT | /api/v1/systems/{sysNo} | json | json | — |
| 删除系统 | DELETE | /api/v1/systems/{sysNo} | path | json | 存在下级子系统时禁止删除 |
| 导入系统Excel | POST | /api/v1/systems/import | multipart | json | 按sysNo覆盖更新，返回导入摘要 |
| 导出系统Excel | GET | /api/v1/systems/export | query | binary | 按当前查询条件过滤后导出 |
| 下载系统导入模板 | GET | /api/v1/systems/template | — | binary | 含必填字段标识及枚举值说明 |
| 查询子系统列表 | GET | /api/v1/sub-systems | query | json | 支持按系统编号、子系统编号、名称、级别等分页查询 |
| 创建子系统 | POST | /api/v1/sub-systems | json | json | — |
| 查询子系统详情 | GET | /api/v1/sub-systems/{subSysNo} | path | json | — |
| 更新子系统 | PUT | /api/v1/sub-systems/{subSysNo} | json | json | — |
| 删除子系统 | DELETE | /api/v1/sub-systems/{subSysNo} | path | json | 存在下级部署子系统时禁止删除 |
| 导入子系统Excel | POST | /api/v1/sub-systems/import | multipart | json | 按subSysNo覆盖更新 |
| 导出子系统Excel | GET | /api/v1/sub-systems/export | query | binary | — |
| 下载子系统导入模板 | GET | /api/v1/sub-systems/template | — | binary | — |
| 查询部署子系统列表 | GET | /api/v1/deploy-sub-systems | query | json | 支持按子系统编号、部署子系统编号、名称等分页查询 |
| 创建部署子系统 | POST | /api/v1/deploy-sub-systems | json | json | — |
| 查询部署子系统详情 | GET | /api/v1/deploy-sub-systems/{deploySysNo} | path | json | — |
| 更新部署子系统 | PUT | /api/v1/deploy-sub-systems/{deploySysNo} | json | json | — |
| 删除部署子系统 | DELETE | /api/v1/deploy-sub-systems/{deploySysNo} | path | json | 存在下级发布单元时禁止删除 |
| 导入部署子系统Excel | POST | /api/v1/deploy-sub-systems/import | multipart | json | 按deploySysNo覆盖更新 |
| 导出部署子系统Excel | GET | /api/v1/deploy-sub-systems/export | query | binary | — |
| 下载部署子系统导入模板 | GET | /api/v1/deploy-sub-systems/template | — | binary | — |
| 查询发布单元列表 | GET | /api/v1/release-units | query | json | 支持按部署子系统编号、编号、名称、类型、部署方式等分页查询 |
| 创建发布单元 | POST | /api/v1/release-units | json | json | — |
| 查询发布单元详情 | GET | /api/v1/release-units/{unitNo} | path | json | — |
| 更新发布单元 | PUT | /api/v1/release-units/{unitNo} | json | json | — |
| 删除发布单元 | DELETE | /api/v1/release-units/{unitNo} | path | json | — |
| 导入发布单元Excel | POST | /api/v1/release-units/import | multipart | json | 按unitNo覆盖更新 |
| 导出发布单元Excel | GET | /api/v1/release-units/export | query | binary | — |
| 下载发布单元导入模板 | GET | /api/v1/release-units/template | — | binary | — |

#### 技术栈管理

| 接口名 | 请求方式 | 接口URL | 请求格式 | 响应格式 | 备注 |
|--------|----------|---------|----------|----------|------|
| 查询技术栈列表 | GET | /api/v1/tech-stacks | query | json | 支持按编号、分类、名称、选型建议、负责条线等分页查询 |
| 创建技术栈 | POST | /api/v1/tech-stacks | json | json | — |
| 查询技术栈详情 | GET | /api/v1/tech-stacks/{stackNo} | path | json | — |
| 更新技术栈 | PUT | /api/v1/tech-stacks/{stackNo} | json | json | — |
| 删除技术栈 | DELETE | /api/v1/tech-stacks/{stackNo} | path | json | 被发布单元引用时提示引用关系 |
| 导入技术栈Excel | POST | /api/v1/tech-stacks/import | multipart | json | 按stackNo覆盖更新 |
| 导出技术栈Excel | GET | /api/v1/tech-stacks/export | query | binary | — |
| 下载技术栈导入模板 | GET | /api/v1/tech-stacks/template | — | binary | — |

#### 逻辑部署架构图管理

| 接口名 | 请求方式 | 接口URL | 请求格式 | 响应格式 | 备注 |
|--------|----------|---------|----------|----------|------|
| 获取架构图 | GET | /api/v1/diagrams/{deploySysNo} | path | json | 返回Mermaid格式文本及元数据；无发布单元时返回空白图提示 |
| 保存手动调整 | POST | /api/v1/diagrams/{deploySysNo}/layout | json | json | 保存节点位置、备注等手动调整信息 |
| 获取手动调整 | GET | /api/v1/diagrams/{deploySysNo}/layout | path | json | 获取已保存的手动调整信息 |

#### 发布单元调用关系管理

| 接口名 | 请求方式 | 接口URL | 请求格式 | 响应格式 | 备注 |
|--------|----------|---------|----------|----------|------|
| 查询调用关系列表 | GET | /api/v1/release-unit-relations | query | json | 支持按部署子系统编号、调用方、被调用方、协议等分页查询 |
| 创建调用关系 | POST | /api/v1/release-unit-relations | json | json | 同一部署子系统下调方与被调方组合唯一 |
| 查询调用关系详情 | GET | /api/v1/release-unit-relations/{id} | path | json | — |
| 更新调用关系 | PUT | /api/v1/release-unit-relations/{id} | json | json | — |
| 删除调用关系 | DELETE | /api/v1/release-unit-relations/{id} | path | json | 仅删除关系，不影响发布单元实体 |

#### 用户角色及权限管理

| 接口名 | 请求方式 | 接口URL | 请求格式 | 响应格式 | 备注 |
|--------|----------|---------|----------|----------|------|
| 用户登录 | POST | /api/v1/auth/login | json | json | 自有账号密码登录，返回Token |
| 用户登出 | POST | /api/v1/auth/logout | header | json | — |
| 获取当前用户信息 | GET | /api/v1/auth/me | header | json | 返回用户基本信息及角色权限 |
| 查询用户列表 | GET | /api/v1/users | query | json | 分页查询 |
| 创建用户 | POST | /api/v1/users | json | json | 超级管理员可创建并分配角色 |
| 查询用户详情 | GET | /api/v1/users/{id} | path | json | — |
| 更新用户 | PUT | /api/v1/users/{id} | json | json | 含角色变更 |
| 删除用户 | DELETE | /api/v1/users/{id} | path | json | — |
| 查询角色列表 | GET | /api/v1/roles | — | json | 返回超级管理员、管理员、普通用户 |

#### 数据字典

| 接口名 | 请求方式 | 接口URL | 请求格式 | 响应格式 | 备注 |
|--------|----------|---------|----------|----------|------|
| 查询字典项 | GET | /api/v1/dictionaries/{category} | path | json | 按分类查询枚举值，如system_type、sys_level等 |

## OpenAPI对外接口
见 [`.cospec/architecture/design.md`](.cospec/architecture/design.md) OpenAPI对外接口

## 依赖的外部接口
见 [`.cospec/architecture/design.md`](.cospec/architecture/design.md) 依赖的外部接口章节

## 数据模型设计
见 [`.cospec/architecture/design.md`](.cospec/architecture/design.md) 数据模型设计章节

## 系统集成
见 [`.cospec/architecture/design.md`](.cospec/architecture/design.md) 系统集成章节

## 非功能设计
见 [`.cospec/architecture/design.md`](.cospec/architecture/design.md) 非功能设计章节
