# Git 分支与版本管理规范

## 1. 目标

本规范用于管理企业级数据字典管理系统的文档、模板、平台代码、部署脚本和后续交付物，确保多人协作、版本追溯、评审合并和发布节奏可控。

## 2. 分支模型

采用轻量 Git Flow：

| 分支 | 类型 | 用途 | 生命周期 |
| --- | --- | --- | --- |
| `main` | 主干保护分支 | 保存已评审、可交付、可发布的稳定版本 | 长期 |
| `develop` | 集成分支 | 汇总已完成但尚未发布的文档和代码 | 长期 |
| `feature/*` | 功能分支 | 开发具体产品能力或平台模块 | 短期 |
| `docs/*` | 文档分支 | 编写、修订和评审专项文档 | 短期 |
| `release/*` | 发布分支 | 发布前冻结、测试、修复和打标签 | 短期 |
| `hotfix/*` | 紧急修复分支 | 修复 `main` 上的紧急问题 | 短期 |
| `poc/*` | 验证分支 | 技术验证、选型实验、原型验证 | 短期，可丢弃 |
| `chore/*` | 仓库维护分支 | 分支同步、CI 配置、仓库治理等非业务变更 | 短期 |

## 3. 首批规划分支清单

| 分支名 | 目标 | 说明 |
| --- | --- | --- |
| `main` | 当前稳定基线 | 当前文档库和模板的正式基线 |
| `develop` | 后续集成分支 | 工程骨架、详细文档和代码先合入此分支 |
| `docs/p0-5-design-freeze` | 编码前收口文档 | 技术决策、API DTO、数据库物理模型、导入规范、安全分级、测试用例 |
| `poc/platform-stack` | 技术栈验证 | 验证 Spring Boot、Vue、PostgreSQL、OpenSearch、Flowable 的最小集成 |
| `feature/platform-skeleton` | 平台工程骨架 | 创建前后端工程、部署脚本、基础 README |
| `feature/backend-core` | 后端核心底座 | 用户、角色、权限、审计、元模型、资产核心 API |
| `feature/frontend-console` | 前端控制台 | 布局、路由、菜单、资产目录、字典基础页面 |
| `feature/import-export` | Excel 导入导出 | 模板下载、批次导入、错误明细、导出 |
| `feature/lineage-impact` | 血缘与影响分析 | 血缘关系维护、图查询、影响分析 |
| `feature/governance-workflow` | 治理流程 | 架构评审、模型变更、标准发布、血缘确认、责任认领 |
| `feature/deploy-local` | 本地部署 | Docker Compose、初始化脚本、样例数据 |
| `release/v0.1-mvp` | MVP 发布 | 首个可演示版本发布分支 |

## 4. 分支命名规则

| 类型 | 命名 |
| --- | --- |
| 功能开发 | `feature/<module-or-capability>` |
| 文档修订 | `docs/<topic>` |
| 技术验证 | `poc/<topic>` |
| 发布准备 | `release/v<major>.<minor>-<name>` |
| 紧急修复 | `hotfix/<issue-or-topic>` |
| 仓库维护 | `chore/<topic>` |

命名要求：

- 使用英文小写和连字符。
- 不使用中文分支名。
- 分支名应表达目标，不使用 `test`、`temp`、`new` 等模糊名称。

## 5. 合并规则

- `main` 只接受来自 `release/*`、`hotfix/*` 或经过评审的稳定变更。
- `develop` 接受来自 `feature/*`、`docs/*`、`poc/*` 中被确认保留的变更。
- `feature/*` 从 `develop` 拉出，完成后合回 `develop`。
- `docs/*` 从 `develop` 拉出，评审通过后合回 `develop`。
- `release/*` 从 `develop` 拉出，完成测试后合入 `main` 并回合 `develop`。
- `hotfix/*` 从 `main` 拉出，修复后合入 `main` 并回合 `develop`。

## 6. 多 Agent 并行协作规范

多个 agent 同时工作时，必须遵守分支隔离和写入范围隔离。禁止多个 agent 在同一个工作区、同一个分支上并行修改。

详细执行机制见 `docs/06-实施交付/02-多Agent协作与PR质控机制-v0.1.md`。其中 PR Steward Agent 负责 PR 队列、质量门禁、集成顺序和冲突协调，默认不承接业务功能开发。

### 6.1 分支隔离

每个 agent 使用独立分支：

| 场景 | 分支示例 |
| --- | --- |
| 文档收口 | `docs/p0-5-design-freeze/agent-api-dto` |
| 后端元模型 | `feature/backend-core/agent-metadata` |
| 后端权限 | `feature/backend-core/agent-auth` |
| 前端资产目录 | `feature/frontend-console/agent-catalog` |
| Excel 导入 | `feature/import-export/agent-import` |
| 血缘分析 | `feature/lineage-impact/agent-graph` |

如果任务足够独立，也可以直接使用一级任务分支，例如 `feature/import-export`；但同一分支同一时间只能有一个 agent 写入。

### 6.2 Worktree 隔离

推荐每个 agent 使用独立 worktree，避免相互覆盖工作区文件：

```powershell
git switch develop
git pull --ff-only
git worktree add ..\codex-agent-api-dto -b docs/p0-5-design-freeze/agent-api-dto develop
git worktree add ..\codex-agent-backend-auth -b feature/backend-core/agent-auth develop
```

常用查看和清理命令：

```powershell
git worktree list
git worktree remove ..\codex-agent-api-dto
git branch -d docs/p0-5-design-freeze/agent-api-dto
```

### 6.3 写入范围声明

每个 agent 开始前必须声明写入范围，PR 描述中也必须写明。

示例：

| Agent | 分支 | 写入范围 |
| --- | --- | --- |
| Agent A | `docs/p0-5-design-freeze/agent-api-dto` | `docs/05-接口与集成` |
| Agent B | `docs/p0-5-design-freeze/agent-db` | `docs/03-架构设计` |
| Agent C | `feature/frontend-console/agent-catalog` | `platform/frontend/web-console/src/pages/catalog` |
| Agent D | `feature/backend-core/agent-auth` | `platform/backend/.../system` |

规则：

- 不改其他 agent 的写入范围。
- 不做顺手格式化全仓库。
- 不移动对方正在编辑的文件。
- 必须避免同一文件多人并行修改；确需并行时，先拆文件或明确合并责任人。

### 6.4 同步规则

每个 agent 开始和提交前执行：

```powershell
git fetch origin
git rebase origin/develop
```

如果还没有远程仓库，使用本地 `develop`：

```powershell
git fetch --all
git rebase develop
```

冲突处理原则：

- 谁的分支后合并，谁负责解决冲突。
- 冲突涉及业务决策时，不允许 agent 自行猜测，应记录到 PR 评论或决策记录。
- 解决冲突后必须重新运行对应检查。

### 6.5 多 Agent 合并顺序

同一批次建议按依赖顺序合并：

1. 文档与技术决策。
2. 数据库迁移和通用类型。
3. 后端核心 API。
4. 前端页面和服务调用。
5. 集成、部署、测试。

禁止先合并依赖下游代码，再补基础模型或接口规范。

## 7. GitHub CLI 与 PR 规范

当前本机如需使用 GitHub CLI，应先安装并登录：

```powershell
winget install GitHub.cli
gh auth login
```

如果还没有远程仓库：

```powershell
gh repo create <org-or-user>/<repo-name> --private --source=. --remote=origin --push
git push origin develop
git push origin --tags
```

### 7.1 创建 PR

常用命令：

```powershell
git push -u origin <branch>
gh pr create --base develop --head <branch> --title "<type>: <summary>" --body-file .github/pull_request_template.md
```

示例：

```powershell
gh pr create --base develop --head docs/p0-5-design-freeze/agent-api-dto --title "docs: add detailed API DTO specification"
```

### 7.2 PR 检查

```powershell
gh pr status
gh pr view <number>
gh pr checks <number>
gh pr diff <number>
```

评审关注点：

- 是否只改声明的写入范围。
- 是否更新相关文档清单。
- 是否包含测试或验收说明。
- 是否存在未说明的技术决策。
- 是否引入安全、权限、审计或数据泄露风险。

### 7.3 PR 合并策略

默认使用 squash merge：

```powershell
gh pr merge <number> --squash --delete-branch
```

使用规则：

- `feature/*`、`docs/*` 默认 squash 合并到 `develop`。
- `release/*` 合并到 `main` 时可使用 merge commit，保留发布上下文。
- `hotfix/*` 合并到 `main` 后必须同步回 `develop`。
- 禁止未评审直接合并到 `main`。

### 7.4 PR 标题规范

PR 标题与提交信息保持一致：

- `docs: add design freeze documents`
- `feat: add metadata asset API`
- `fix: correct lineage impact traversal`
- `security: restrict sensitive field export`

### 7.5 PR 描述必须包含

- 变更范围。
- 写入范围。
- 关联文档或 issue。
- 测试结果。
- 风险与回滚方式。
- 是否影响数据库、权限、API、部署。

PR 模板存放在 `.github/pull_request_template.md`。

## 8. 提交信息规范

提交信息采用：

`<type>: <summary>`

常用类型：

| 类型 | 说明 |
| --- | --- |
| `docs` | 文档变更 |
| `feat` | 新功能 |
| `fix` | 缺陷修复 |
| `refactor` | 重构 |
| `test` | 测试 |
| `build` | 构建或依赖 |
| `chore` | 工程维护 |
| `security` | 安全相关 |

示例：

- `docs: add git branch governance guide`
- `feat: add metadata asset API`
- `security: add sensitive field access audit`

## 9. 标签与版本

| 标签 | 含义 |
| --- | --- |
| `v0.1-doc-baseline` | 文档基线版本 |
| `v0.1-mvp` | 首个 MVP 可演示版本 |
| `v0.2-pilot` | 首批业务域试点版本 |
| `v1.0` | 首个生产可用版本 |

发布标签只打在 `main` 分支。

## 10. 当前建议

当前仓库初始化后：

1. 在 `main` 提交当前文档库和模板基线。
2. 创建 `develop` 作为后续集成分支。
3. 下一步从 `develop` 拉出 `docs/p0-5-design-freeze`，补齐编码前收口文档。
4. 收口文档评审通过后，再从 `develop` 拉出 `feature/platform-skeleton` 创建工程骨架。
