# 待办清单

## P0

- 已完成并推送：
  - `.codex/context/current-state.md`
  - `.codex/context/todo.md`
  - `.codex/context/restart-checkpoint-2026-04-27-second-batch.md`
  - `.codex/context/session-handoff-2026-04-28-backend-metadata.md`
- 根目录未跟踪文件 `codex` 不纳入版本控制。
- 下一次重启后优先读取 `.codex/context/session-handoff-2026-04-28-backend-metadata.md`。

## P1

- 继续 `backend_metadata`：
  - 当前路径：`D:\codex\codex-agent-backend-metadata`
  - 当前分支：`feature/backend-metadata`
  - 当前 HEAD：`584d3a3`
  - 当前状态：实现完成，工作区干净，已推送到 `origin/feature/backend-metadata`。
  - 已实现范围：元数据模型、资产、资产属性、字段字典、版本能力。
  - 已新增迁移脚本：`V2__metadata_assets.sql`，未修改 `V1__system_security_audit.sql`。
  - 已验证：JDK 21 `mvn -pl platform/backend/metadata-platform -am test`、`git diff --cached --check`、冲突标记扫描。
- `qa_verification` worktree：
  - 当前路径：`D:\codex\codex-agent-qa-verification`
  - 当前分支：`integration/qa-verification`
  - 当前 HEAD：`afda7f9`
  - 用途：`backend_metadata` 完成后的只读验收与复核。

## GitHub 同步待办

- 已完成：
  - PR #13 `feature/deploy-local -> develop` 已合并并关闭。
  - PR #14 `feature/frontend-shell -> develop` 已合并并关闭。
  - PR #15 `feature/backend-system -> develop` 已合并并关闭。
  - `origin/develop` 已更新到 `afda7f95cfb2c2d252b73c323ba6e69e905c313d`。
- 待处理：
  - 推送本地 PR #16 合并状态 context 提交；当前 GitHub HTTPS 连接失败。
  - 同步 `integration/qa-verification` 到最新 `develop`。
  - 启动下一批前端资产目录 `frontend_catalog`。
  - `gh pr view` 的 GraphQL 401 问题仍需排查。
  - `gh api --method PUT .../pulls/{id}/merge` 写接口 401 问题仍需排查。
  - `gh pr checks` 当前也受 GraphQL 401 影响；可用 REST check-runs 查询替代。
  - 直接 `git push origin develop` 当前可用。
- 暂不清理远端功能分支，除非用户明确要求删除已合并分支。

## P2

- 可按需清理已合并分支和 worktree：
  - `feature/deploy-local`
  - `feature/frontend-shell`
  - `feature/backend-system`
  - `feature/backend-metadata`
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
