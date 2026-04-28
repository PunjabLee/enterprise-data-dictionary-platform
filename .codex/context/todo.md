# 待办清单

## P0

- 提交并推送 `.codex/context/current-state.md`、`.codex/context/todo.md`、`.codex/context/restart-checkpoint-2026-04-27-second-batch.md`。
- 根目录未跟踪文件 `codex` 不纳入版本控制。
- 下一次重启后优先读取 `.codex/context/restart-checkpoint-2026-04-27-second-batch.md`。

## P1

- 同步 `backend_metadata` worktree：
  - 当前路径：`D:\codex\codex-agent-backend-metadata`
  - 当前分支：`feature/backend-metadata`
  - 当前 HEAD：`1ed60e4`
  - 目标基线：`origin/develop` 的 `5466ca3`
- 启动 `backend_metadata`：
  - 负责元数据模型、资产、资产属性、字段字典、版本能力。
  - 迁移脚本需接续后端系统基础能力的约定。
  - 代码注释、SQL 注释和备注使用中文。
  - 作者标记使用 `Punjab`。
- 同步 `qa_verification` worktree：
  - 当前路径：`D:\codex\codex-agent-qa-verification`
  - 当前分支：`integration/qa-verification`
  - 目标基线：`origin/develop` 的 `5466ca3`
  - 用途：下一轮 PR 的只读验收与复核。

## GitHub 同步待办

- 已完成：
  - PR #13 `feature/deploy-local -> develop` 已合并并关闭。
  - PR #14 `feature/frontend-shell -> develop` 已合并并关闭。
  - PR #15 `feature/backend-system -> develop` 已合并并关闭。
  - `origin/develop` 已更新到 `5466ca305f265448ac929082145a36d59a664450`。
- 待处理：
  - `gh pr view` 的 GraphQL 401 问题仍需排查。
  - `gh api --method PUT .../pulls/{id}/merge` 写接口 401 问题仍需排查。
  - 直接 `git push origin develop` 当前可用。
- 暂不清理远端功能分支，除非用户明确要求删除已合并分支。

## P2

- 启动业务能力前，确认后端迁移脚本编号策略：
  - 当前已有：`V1__system_security_audit.sql`
  - 后续建议：按模块顺序追加 `V2__...sql`，避免多个 agent 并行写同一个迁移编号。
- 配置 GitHub 分支保护规则：
  - 保护 `main`。
  - 保护 `develop`。
  - 要求状态检查：`Validate PR target branch`。
- 后续每轮 PR 至少执行：
  - `git diff --check`
  - `rg --line-number "^(<<<<<<<|=======|>>>>>>>)" .github docs platform`
  - `mvn -pl platform/backend/metadata-platform -am test`，执行前显式确认 Maven 使用 JDK 21。
  - `npm.cmd run lint`
  - `npm.cmd run type-check`
  - `npm.cmd run build`
  - `docker compose -f platform/deploy/docker-compose.yml config`
