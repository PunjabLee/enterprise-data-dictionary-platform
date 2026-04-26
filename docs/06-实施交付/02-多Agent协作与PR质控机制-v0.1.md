# 多 Agent 协作与 PR 质控机制 v0.1

## 1. 目标

本机制用于支持多个 Agent 并行开发企业级数据字典平台，同时避免分支污染、重复劳动、架构漂移和低质量 PR 合入。

核心原则：

- 一个任务一个分支。
- 一个分支同一时间只允许一个开发 Agent 写入。
- 一个 PR 必须有明确写入范围、验收口径和回滚方式。
- PR Steward Agent 只负责集成、评审、质量控制和合并建议，默认不承接业务功能开发。
- `develop` 只接收通过检查的 PR，不直接接收开发 Agent 推送。

## 2. 角色分工

| 角色 | 职责 | 是否写业务代码 |
| --- | --- | --- |
| Product/Architecture Owner | 确认范围、优先级、领域边界、关键技术决策 | 否 |
| Development Agent | 实现具体文档、后端模块、前端页面、测试或脚本 | 是 |
| PR Steward Agent | 管理 PR 队列、检查质量门禁、协调冲突、给出合并建议 | 否，除非处理集成冲突 |
| QA/Verification Agent | 执行测试、回归、验收用例和缺陷复核 | 否，必要时补测试 |
| Security/Compliance Reviewer | 关注认证、权限、审计、敏感数据、密钥泄露风险 | 否 |

首期可以简化为三类：

| 简化角色 | 对应工作 |
| --- | --- |
| 架构决策者 | 用户 + 主控 Agent |
| 开发 Agent | 后端、前端、数据库、文档、部署分任务 |
| PR Steward Agent | PR 质量控制、集成顺序、合并建议 |

## 3. 任务拆分机制

开发任务必须先拆成可独立交付的 Work Package。

| Work Package | 建议分支 | 写入范围 | 前置依赖 |
| --- | --- | --- | --- |
| 平台工程骨架 | `feature/platform-skeleton` | `platform/**`、根级构建文件 | P0.5 文档冻结 |
| 后端基础工程 | `feature/backend-foundation` | `platform/backend/**` | 工程骨架 |
| 前端基础工程 | `feature/frontend-foundation` | `platform/frontend/**` | 工程骨架 |
| 数据库迁移 | `feature/database-migration` | `platform/backend/**/db/migration` | 数据库物理模型 |
| 系统权限模块 | `feature/backend-system` | 后端 `system` 限界上下文 | 后端基础工程 |
| 元模型与资产模块 | `feature/backend-metadata` | 后端 `metadata` 限界上下文 | 后端基础工程、数据库迁移 |
| 业务术语模块 | `feature/backend-glossary` | 后端 `glossary` 限界上下文 | 元模型、权限 |
| 血缘模块 | `feature/backend-lineage` | 后端 `lineage` 限界上下文 | 资产模块 |
| 控制台壳 | `feature/frontend-shell` | 前端布局、路由、菜单、权限守卫 | 前端基础工程 |
| 资产目录页面 | `feature/frontend-catalog` | 前端资产目录相关页面 | 控制台壳、资产 API |
| 导入导出 | `feature/import-export` | 后端导入、前端导入页面、模板 | 资产模块、Excel 规范 |
| 本地部署 | `feature/deploy-local` | `platform/deploy/**` | 工程骨架 |

拆分规则：

- 每个 Work Package 应在 1 到 3 天内可以形成 PR。
- 横跨前后端的任务应优先拆成 API 契约、后端实现、前端实现三个 PR。
- 数据库迁移、公共 DTO、公共错误码、权限模型属于基础依赖，应先合并。
- 不允许一个 Agent 在单个 PR 中同时完成“不相关文档 + 后端 + 前端 + 部署”的混合变更。

## 4. 分支与 Worktree 机制

每个 Development Agent 使用独立分支和独立 worktree。

推荐分支命名：

| 类型 | 示例 |
| --- | --- |
| 功能开发 | `feature/backend-metadata` |
| 子任务开发 | `feature/backend-metadata/agent-asset` |
| 文档修订 | `docs/agent-pr-governance` |
| 技术验证 | `poc/opensearch-search` |
| 缺陷修复 | `fix/import-validation` |
| 仓库维护 | `chore/sync-main-to-develop` |

推荐 worktree 命令：

```powershell
git switch develop
git pull --ff-only
git worktree add ..\codex-backend-metadata -b feature/backend-metadata develop
git worktree add ..\codex-frontend-shell -b feature/frontend-shell develop
git worktree add ..\codex-pr-steward -b integration/pr-steward develop
```

约束：

- 主工作区不并行给多个 Agent 使用。
- PR Steward Agent 使用只读检查为主，不直接在开发分支继续编码。
- 需要解决冲突时，PR Steward Agent 应创建 `integration/<topic>` 分支，不在 `develop` 直接操作。

## 5. PR 生命周期

### 5.1 创建前

Development Agent 必须完成：

- 从最新 `develop` 创建分支。
- 明确写入范围。
- 明确验收标准。
- 确认是否影响数据库、API、权限、部署、文档。

### 5.2 开发中

Development Agent 必须遵守：

- 不修改写入范围外文件，除非 PR 描述说明原因。
- 不绕过 DDD 分层和 DAO 访问规约。
- 不提交 token、密码、私钥、真实生产连接信息。
- 不引入未经确认的新中间件或框架。

### 5.3 提交 PR

PR 描述必须包含：

| 字段 | 内容 |
| --- | --- |
| 变更范围 | 本 PR 完成什么 |
| 写入范围 | 哪些目录或文件被修改 |
| 架构影响 | 是否影响 DDD 边界、数据库、API、权限、部署 |
| 验证结果 | 执行了哪些测试或检查 |
| 风险 | 已知风险和限制 |
| 回滚 | 如何回滚或关闭功能 |

### 5.4 PR Steward 检查

PR Steward Agent 必须逐项检查：

| 门禁 | 检查内容 |
| --- | --- |
| Scope Gate | 是否只修改声明范围 |
| Architecture Gate | 是否符合模块化单体、DDD、Repository/DAO/Mapper 规约 |
| API Gate | DTO、错误码、分页、权限、异步任务是否一致 |
| Data Gate | Flyway、约束、索引、枚举、`tenant_id`、审计字段是否一致 |
| Security Gate | 认证、授权、数据范围、敏感字段、导出、日志脱敏是否合规 |
| Test Gate | 是否有单元测试、集成测试或手工验证说明 |
| Docs Gate | 是否更新相关设计文档、README、文档清单 |
| Operations Gate | 是否影响部署、配置、环境变量、数据库初始化 |

### 5.5 合并

默认策略：

- 文档分支和功能分支合入 `develop` 使用 squash merge。
- 多个 PR 有依赖时，PR Steward Agent 维护合并顺序。
- 发现冲突时，先让 Development Agent rebase；跨模块冲突才由 PR Steward Agent 建 `integration/*` 分支处理。
- 合并后删除远端功能分支，保留标签和 release 分支。

## 6. PR Steward Agent 工作清单

PR Steward Agent 每轮处理 PR 时执行以下清单：

1. 同步远端分支和 PR 列表。
2. 按依赖关系排序 PR。
3. 检查每个 PR 的写入范围和 diff。
4. 运行对应测试或至少检查构建脚本可执行性。
5. 检查文档、数据库、API、权限、安全影响。
6. 对不合格 PR 写出明确整改意见。
7. 对合格 PR 给出合并建议。
8. 合并后确认 `develop` 可继续构建和测试。
9. 更新当轮集成记录。

不允许 PR Steward Agent 做的事：

- 不替 Development Agent 大规模重写业务实现。
- 不在未讨论的情况下改变领域模型。
- 不因为“能通过编译”就忽略架构边界。
- 不把多个未评审 PR 手工拼成一次大合并。

## 7. 质量门禁基线

工程骨架阶段最低检查：

```powershell
git diff --check
mvn -version
node -v
npm -v
```

后端具备工程后最低检查：

```powershell
mvn -pl platform/backend/metadata-platform -am test
```

前端具备工程后最低检查：

```powershell
cd platform/frontend/web-console
npm run lint
npm run type-check
npm run build
```

部署具备后最低检查：

```powershell
docker compose -f platform/deploy/docker-compose.yml config
```

文档变更最低检查：

```powershell
git diff --check
rg --line-number "TODO|TBD|FIXME" docs
rg --line-number "^(<<<<<<<|=======|>>>>>>>)" docs
```

## 8. 冲突处理机制

冲突分三类：

| 类型 | 处理人 | 处理方式 |
| --- | --- | --- |
| 同文件普通冲突 | 后提交 PR 的 Development Agent | rebase 并解决 |
| 跨模块契约冲突 | PR Steward Agent 协调 | 建议拆公共契约 PR |
| 领域/架构决策冲突 | Product/Architecture Owner | 形成 ADR 或会议决策 |

禁止事项：

- 禁止用覆盖方式解决冲突。
- 禁止删除对方已合入能力。
- 禁止在冲突解决时顺手做额外重构。
- 禁止未跑检查就标记冲突已解决。

## 9. 首期建议执行节奏

首期进入编码后建议按以下批次：

| 批次 | PR 主题 | 说明 |
| --- | --- | --- |
| Batch 1 | `feature/platform-skeleton` | Maven、多模块目录、Vue、Docker、基础 README |
| Batch 2 | `feature/backend-foundation`、`feature/frontend-foundation` | 后端启动、前端启动、基础配置 |
| Batch 3 | `feature/database-migration`、`feature/backend-system` | Flyway、用户、组织、角色、权限 |
| Batch 4 | `feature/backend-metadata`、`feature/frontend-catalog` | 元模型、资产目录、字段字典 |
| Batch 5 | `feature/backend-glossary`、`feature/import-export` | 业务术语、Excel 导入导出 |
| Batch 6 | `feature/backend-lineage`、`feature/governance-workflow` | 血缘、影响分析、审批任务 |
| Batch 7 | `feature/deploy-local`、`release/v0.1-mvp` | 本地部署、样例数据、验收 |

每个批次结束后，PR Steward Agent 输出一次集成结论：

- 已合并 PR。
- 被拒绝或退回 PR。
- 未解决风险。
- 下一批次前置条件。

## 10. 与现有规约关系

本机制是 `Git分支与版本管理规范.md` 的执行细化，编码规约仍以 `编码规约与架构约束-v0.1.md` 为准。

如发生冲突，优先级如下：

1. 安全和密钥保护要求。
2. 用户明确决策和 ADR。
3. 编码规约与架构约束。
4. Git 分支与版本管理规范。
5. 本多 Agent 协作机制。
