# 待办清单

## P0

- 已完成并推送：
  - `.codex/context/current-state.md`
  - `.codex/context/todo.md`
  - `.codex/context/restart-checkpoint-2026-04-27-second-batch.md`
  - `.codex/context/session-handoff-2026-04-28-backend-metadata.md`
- 根目录未跟踪文件 `codex` 不纳入版本控制。
- 下一次重启后优先读取 `.codex/context/current-state.md` 与 `.codex/context/todo.md`。
- 恢复命令：`codex.cmd resume --last -C D:\codex\enterprise-data-dictionary-platform`

## P1

- `frontend_catalog` PR #17：
  - 当前路径：`D:\codex\codex-agent-frontend-catalog`
  - 当前分支：`feature/frontend-catalog`
  - 本地 HEAD：`c3462f8 功能：增加前端资产目录页面`
  - 远端 HEAD：`0de3dc6092c17d64dd86239968c1d5810390b03e`
  - PR 地址：`https://github.com/PunjabLee/enterprise-data-dictionary-platform/pull/17`
  - 当前状态：已合入 `develop`，GitHub 状态为 closed/merged。
  - 合并提交：`1b8521b 合并 PR #17：增加前端资产目录页面`
  - 已实现范围：资产目录列表、资产详情、字段字典、业务术语占位页、前端导航和相关 API service。
  - 已验证：`git diff --check 35a3d0be41201a8ea0b7aa746bcff954429fc46b..HEAD`、冲突标记扫描、`npm.cmd run lint`、`npm.cmd run type-check`、`npm.cmd run build`。
- `qa_verification` worktree：
  - 当前路径：`D:\codex\codex-agent-qa-verification`
  - 当前分支：`integration/qa-verification`
  - 当前 HEAD：当前上下文同步提交
  - 用途：PR #17 合并后的只读验收与复核。

## GitHub 同步待办

- 已完成：
  - PR #13 `feature/deploy-local -> develop` 已合并并关闭。
  - PR #14 `feature/frontend-shell -> develop` 已合并并关闭。
  - PR #15 `feature/backend-system -> develop` 已合并并关闭。
  - PR #16 `feature/backend-metadata -> develop` 已合并并关闭。
  - PR #17 `feature/frontend-catalog -> develop` 已合并并关闭。
  - `origin/develop` 已更新到当前上下文同步提交。
- 待处理：
  - 明天优先确认下一批范围：建议选择 `backend_glossary` 业务术语基础能力 + 前端术语页对接 + `qa_verification` 复核。
  - 正常业务批次预计 3-5 小时；建议先按约 3 小时拆小批推进。
  - 也可以先做 PR #17 合并后验收与小修，预计 1.5-2.5 小时。
  - `gh pr view` 的 GraphQL 401 问题仍需排查。
  - `gh api --method PUT .../pulls/{id}/merge` 写接口 401 问题仍需排查。
  - `gh pr checks` 当前也受 GraphQL 401 影响；可用 REST check-runs 查询替代。
  - 普通 `git push` 可能因 GitHub HTTPS 连接超时失败；必要时继续使用 GitHub REST 作为备用方案。
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
