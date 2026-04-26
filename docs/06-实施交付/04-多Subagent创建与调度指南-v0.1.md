# 多 Subagent 创建与调度指南 v0.1

## 1. 目标

本指南说明如何基于 `.codex/agents` 创建主 Agent 与多个 Subagent，并在 Git 分支、worktree、写入范围和 PR 质量门禁上保持隔离。

## 2. 创建原则

- 一个 Subagent 一个独立分支。
- 一个 Subagent 一个独立 worktree。
- 一个 Subagent 一个明确写入范围。
- `develop` 是日常集成分支。
- `main` 只接收 `release/*` 和 `hotfix/*`。
- PR Steward Agent 不做业务功能开发，只做 PR 质量控制和集成协调。
- Product/Architecture Owner 不直接写业务代码，只做范围、架构、ADR、验收口径和决策收口。

## 3. Agent 定义目录

Agent 定义文件位于：

```text
.codex/agents
```

当前建议角色：

| Agent | 定义文件 | 分支 |
| --- | --- | --- |
| Main Orchestrator | `.codex/agents/main-orchestrator.toml` | `develop` |
| Product/Architecture Owner | `.codex/agents/owner-architecture.toml` | `owner/architecture-board` |
| PR Steward | `.codex/agents/pr-steward.toml` | `integration/pr-steward` |
| Platform Foundation | `.codex/agents/foundation.toml` | `feature/platform-skeleton` |
| Backend System | `.codex/agents/backend-system.toml` | `feature/backend-system` |
| Backend Metadata | `.codex/agents/backend-metadata.toml` | `feature/backend-metadata` |
| Backend Glossary | `.codex/agents/backend-glossary.toml` | `feature/backend-glossary` |
| Backend Lineage | `.codex/agents/backend-lineage.toml` | `feature/backend-lineage` |
| Frontend Shell | `.codex/agents/frontend-shell.toml` | `feature/frontend-shell` |
| Frontend Catalog | `.codex/agents/frontend-catalog.toml` | `feature/frontend-catalog` |
| DBA | `.codex/agents/dba.toml` | `feature/database-migration` |
| Deploy Local | `.codex/agents/deploy-local.toml` | `feature/deploy-local` |
| QA Verification | `.codex/agents/qa-verification.toml` | `integration/qa-verification` |

## 4. 首批创建顺序

第一批不要并行拆太细，应先稳定工程骨架：

```powershell
git switch develop
git pull --ff-only
git worktree add ..\codex-agent-owner -b owner/architecture-board develop
git worktree add ..\codex-agent-foundation -b feature/platform-skeleton develop
git worktree add ..\codex-agent-pr-steward -b integration/pr-steward develop
```

第一批职责：

| Worktree | 职责 |
| --- | --- |
| `..\codex-agent-owner` | 确认工程骨架范围、ADR、验收口径 |
| `..\codex-agent-foundation` | 创建 Maven、Vue、Docker、基础目录 |
| `..\codex-agent-pr-steward` | 检查 PR base、写入范围、质量门禁 |

## 5. 第二批创建顺序

`feature/platform-skeleton` 合入 `develop` 后，再创建业务开发 Subagent：

```powershell
git switch develop
git pull --ff-only
git worktree add ..\codex-agent-backend-system -b feature/backend-system develop
git worktree add ..\codex-agent-backend-metadata -b feature/backend-metadata develop
git worktree add ..\codex-agent-frontend-shell -b feature/frontend-shell develop
git worktree add ..\codex-agent-deploy-local -b feature/deploy-local develop
```

第三批再创建：

```powershell
git worktree add ..\codex-agent-dba -b feature/database-migration develop
git worktree add ..\codex-agent-backend-glossary -b feature/backend-glossary develop
git worktree add ..\codex-agent-backend-lineage -b feature/backend-lineage develop
git worktree add ..\codex-agent-frontend-catalog -b feature/frontend-catalog develop
git worktree add ..\codex-agent-qa -b integration/qa-verification develop
```

## 6. 调度流程

主 Agent 每次派发任务时，必须提供：

| 字段 | 说明 |
| --- | --- |
| Goal | 本次目标 |
| Branch | 分支名 |
| Worktree | 工作区 |
| Write scope | 允许修改的目录 |
| Do not touch | 禁止修改的目录 |
| Dependencies | 前置依赖 |
| Acceptance criteria | 验收标准 |
| Validation commands | 必须运行的检查 |

## 7. PR 与合并顺序

推荐合并顺序：

1. `owner/architecture-board`
2. `feature/platform-skeleton`
3. `feature/backend-system`
4. `feature/backend-metadata`
5. `feature/frontend-shell`
6. `feature/database-migration`
7. `feature/backend-glossary`
8. `feature/frontend-catalog`
9. `feature/backend-lineage`
10. `feature/deploy-local`
11. `integration/qa-verification`

## 8. 禁止事项

- 禁止多个 Subagent 共用同一分支写代码。
- 禁止多个 Subagent 同时改同一文件。
- 禁止业务开发分支直接 PR 到 `main`。
- 禁止 Service 直接调用 MyBatis Mapper。
- 禁止 Subagent 自行改变技术栈或领域边界。
- 禁止把 token、密码、密钥写入 `.codex` 或仓库。
