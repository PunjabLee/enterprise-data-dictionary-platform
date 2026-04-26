# 企业级数据字典管理平台

企业级数据字典管理平台是一个面向企业内部 IT 系统的数据模型、元数据、业务术语、数据标准、数据血缘和架构治理的一体化平台规划项目。

当前项目处于文档、架构、治理机制和工程骨架准备阶段。仓库已经沉淀产品规划、PRD、架构决策、元模型设计、API/DTO 规范、Git 治理、多 Agent 协作机制和 Subagent 定义。应用工程代码骨架尚未创建。

[English README](README.md)

## 项目定位

本项目不是单纯的 CRUD 数据字典，而是企业级数据架构治理与元数据运营平台。

它的目标是成为企业内部数据定义、数据模型、业务术语、数据标准、责任归属、血缘确认、架构评审和影响分析的治理权威源。

## 方法论对齐

平台设计对齐：

- DAMA-DMBOK2：数据治理、元数据管理、数据架构、数据质量、数据责任、参考数据、数据安全。
- TOGAF 10：架构治理、架构资产库、ADM 架构评审、架构制品、企业架构可追溯。

## 产品能力范围

规划核心能力：

- 企业数据资产目录：系统、数据库、Schema、表、字段、接口、报表、任务、数据产品。
- 数据字典管理：物理字段、业务定义、约束、类型、责任人、生命周期状态。
- 数据模型管理：概念模型、逻辑模型、物理模型。
- 业务术语管理：`BusinessTerm`、术语关系、术语引用、冲突检查、审批、影响分析。
- 数据标准管理：标准定义、标准引用、标准落地检查。
- 数据血缘与影响分析：系统、接口、表、报表、关键字段级血缘。
- 架构治理流程：系统建设评审、模型变更审批、血缘确认、标准发布、责任认领。
- 自动化治理：规则、差异、任务、事件、通知、报表、问题台账、整改闭环。
- 权限与安全：RBAC、数据范围、敏感字段控制、审计、导出管控。

## 当前阶段

已完成：

- 文档体系和文档清单。
- 方案总纲、详细 PRD、页面交互设计、产品功能全景。
- 技术架构、应用架构、部署架构、技术组件架构。
- 企业元模型、业务术语、血缘、状态机、工作流、安全分类分级。
- 数据库物理模型 v0.2、API DTO 详细规范、Excel 导入导出规范。
- Git 分支模型、PR 流向控制、GitHub Actions 分支策略检查。
- 多 Agent、PR Steward、Product/Architecture Owner 和 Subagent 定义。

尚未开始：

- 后端 Maven/Spring Boot 工程骨架。
- 前端 Vue/Vite/Ant Design Vue 工程骨架。
- Docker Compose 运行骨架。
- Flyway 可执行迁移脚本。
- 可运行的业务功能代码。

## 技术与架构决策

| 领域 | 决策 |
| --- | --- |
| 后端 | Java 21、Spring Boot 3、Maven |
| 前端 | Vue 3、TypeScript、Vite、Ant Design Vue |
| 架构风格 | DDD 分层模块化单体 |
| 数据库 | PostgreSQL 16 |
| 迁移 | Flyway |
| ORM/SQL | MyBatis |
| 缓存 | Redis 首期纳入 |
| 搜索 | OpenSearch 预留，首期非强依赖 |
| 工作流 | 首期轻量任务/状态模型，Flowable 预留 |
| 消息 | `DomainEventPublisher` 抽象，RocketMQ/Kafka 后续可插拔 |
| 规则 | 首期轻量可配置治理规则，外部规则引擎预留 |
| 多租户 | 首期不做 SaaS 多租户，但预留 `tenant_id` |
| 权限安全 | RBAC、数据范围、敏感字段、审计 |

## DDD 分层约束

后端必须遵循：

```text
interfaces -> application -> domain
                         -> infrastructure
```

强制数据访问链路：

```text
Controller
  -> ApplicationService
  -> Repository
  -> DAO
  -> MyBatis Mapper
```

禁止 Service 或 ApplicationService 直接访问 MyBatis Mapper。

## 仓库结构

```text
.
|-- .codex/                  # 项目级 Agent 定义、工作流、规则、上下文
|-- .github/                 # PR 模板和分支策略工作流
|-- docs/                    # 规划、产品、架构、治理、交付、测试文档
|-- tools/                   # 仓库自动化脚本
|-- templates/               # 本地历史/生成目录，已忽略
|-- README.md                # 英文项目说明
|-- README.zh-CN.md          # 中文项目说明
```

文档目录：

```text
docs/
|-- 00-文档治理
|-- 01-总体规划
|-- 02-产品需求
|-- 03-架构设计
|-- 04-数据治理与元模型
|-- 05-接口与集成
|-- 06-实施交付
|-- 07-模板与表单
|-- 08-测试验收
|-- 09-会议纪要与决策
|-- 10-归档
```

## 推荐阅读

1. [文档库首页](docs/README.md)
2. [方案总纲](docs/01-总体规划/企业级数据字典管理系统方案总纲.md)
3. [详细 PRD](docs/02-产品需求/01-详细PRD-v0.2.md)
4. [产品功能全景与自动化业务说明](docs/02-产品需求/03-产品功能全景与自动化业务说明-v0.1.md)
5. [技术架构设计](docs/03-架构设计/01-技术架构设计-v0.1.md)
6. [应用架构设计](docs/03-架构设计/06-应用架构设计-v0.1.md)
7. [技术决策 ADR](docs/09-会议纪要与决策/01-技术决策ADR-v0.1.md)
8. [企业元模型设计](docs/04-数据治理与元模型/01-企业元模型设计说明书-v0.1.md)
9. [业务术语管理设计](docs/04-数据治理与元模型/08-业务术语管理设计-v0.1.md)
10. [Git 管理模型与 PR 流向控制规范](docs/00-文档治理/Git管理模型与PR流向控制规范-v0.1.md)
11. [多 Subagent 创建与调度指南](docs/06-实施交付/04-多Subagent创建与调度指南-v0.1.md)

完整清单见：[文档清单](docs/00-文档治理/文档清单.md)

## Git 管理模型

当前强制规则：

```text
main    <- 只接收 release/* 或 hotfix/*
develop <- 接收 docs/*、feature/*、fix/*、chore/*、owner/*、poc/*、integration/*
```

要求：

- `main` 是稳定分支。
- `develop` 是日常集成分支。
- 禁止使用 `pull/new/<branch>` 链接创建 PR。
- 必须使用显式 base 的 `compare/<base>...<branch>?expand=1` 链接。
- `.github/workflows/branch-policy.yml` 会检查 PR base/head 是否符合规则。

## 多 Agent 协同

项目级 Agent 定义位于：

```text
.codex/agents
```

当前定义：

- Main Orchestrator
- Product/Architecture Owner
- PR Steward
- Platform Foundation
- Backend System
- Backend Metadata
- Backend Glossary
- Backend Lineage
- Frontend Shell
- Frontend Catalog
- DBA
- Deploy Local
- QA Verification

核心规则：

```text
一个 Subagent，一个分支，一个 worktree，一个写入范围。
```

相关文件：

- [.codex Agent 定义](.codex/agents/README.md)
- [多 Agent 工作流](.codex/workflows/multi-agent-development.md)
- [多 Subagent 创建与调度指南](docs/06-实施交付/04-多Subagent创建与调度指南-v0.1.md)

## Codex 重启上下文

重启 Codex CLI 后优先阅读：

```text
.codex/context/current-state.md
.codex/context/todo.md
```

这些文件用于保存当前协作状态、待办事项、分支状态和下一步建议。禁止在其中保存 token、密码、密钥或个人凭据。

## 下一步工程工作

`chore/agent-definitions` 合入 `develop` 后，创建：

```text
feature/platform-skeleton
```

首期工程骨架范围：

- Maven 多模块后端工程。
- Vue 3 + TypeScript + Vite + Ant Design Vue 前端工程。
- PostgreSQL 与 Redis 的 Docker Compose。
- Flyway 迁移目录。
- 本地启动脚本和说明文档。

## 安全要求

- 禁止提交 token、密码、私钥、生产连接串或个人凭据。
- GitHub 凭据应通过 Git Credential Manager、GitHub CLI 或仓库外环境变量管理。
- 敏感字段访问、导出与审计是平台一等需求。

## License

暂未选择开源许可证。
