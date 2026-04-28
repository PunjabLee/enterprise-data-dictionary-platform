# 当前项目上下文

最后更新：2026-04-28 14:05:46 +08:00

## 仓库状态

- 仓库：`PunjabLee/enterprise-data-dictionary-platform`
- 主工作区：`D:\codex\enterprise-data-dictionary-platform`
- 日常集成分支：`develop`
- 稳定分支：`main`
- 当前本地分支：`develop`
- 本地与远端 `develop` 最新提交：`5466ca305f265448ac929082145a36d59a664450`
- 根目录未跟踪文件 `codex` 是临时草稿文件，按用户确认不纳入版本控制。

## 已合入内容

- PR #9：架构规格与细化文档。
- PR #10：冻结清单与 Codex 重启上下文。
- PR #11：平台工程骨架。
- PR #12：编排状态与 `Agent.md`。
- PR #13：本地部署流程，已通过本地 `git merge --no-ff` 合入 `develop`。
  - 功能提交：`ac8a7cd chore: improve local deployment workflow`
  - 合并提交：`e095e0a715251d5b743c75d2d8c187eb0a0a4660`
- PR #14：前端控制台壳层，已通过本地 `git merge --no-ff` 合入 `develop`。
  - 功能提交：`f3270f2 feat: add frontend shell foundation`
  - 合并提交：`928f84f524727640de516a1310db538938478668`
- PR #15：后端系统、权限、审计基础，已通过本地 `git merge --no-ff` 合入 `develop`。
  - 功能提交：`4030a7c feat: add backend system foundation`
  - 合并提交：`5466ca305f265448ac929082145a36d59a664450`

## 本轮验证结果

- `git diff --check`：通过。
- `rg --line-number "^(<<<<<<<|=======|>>>>>>>)" .github docs platform`：未发现冲突标记。
- `java -version`：Java 21.0.10。
- 当前 Codex 会话的 `JAVA_HOME` 仍继承为 JDK 17；执行 Maven 验证时已显式设置为 `C:\Program Files\Java\jdk-21.0.10`。
- `mvn -pl platform/backend/metadata-platform -am test`：通过，编译参数为 `release 21`。
- `npm.cmd install`：已在主工作区前端目录执行，用于补齐 `node_modules`。
- `npm.cmd run lint`：通过。
- `npm.cmd run type-check`：通过。
- `npm.cmd run build`：通过；保留 Vite 大 chunk 警告，不影响构建结果。
- `docker compose -f platform/deploy/docker-compose.yml config`：通过。

## 架构与工程约定

- 后端标准：Java 21、Spring Boot 3、Maven。
- 前端标准：Vue 3、TypeScript、Vite、Ant Design Vue。
- 架构风格：DDD 分层模块化单体。
- 持久化：PostgreSQL 16、Flyway、MyBatis。
- 后端访问链路：`Controller -> ApplicationService -> Repository -> DAO -> MyBatis Mapper`。
- 第一阶段包含 Redis；OpenSearch、Flowable、RocketMQ、Kafka 等能力暂保留为后续可插拔扩展。
- 代码作者标记统一使用 `Punjab`。
- 新增代码中的注释、SQL 注释、备注说明应使用中文。

## 当前 worktree 状态

- `D:\codex\enterprise-data-dictionary-platform`
  - 分支：`develop`
  - HEAD：`5466ca3`
  - 状态：仅 `.codex/context/**` checkpoint 文件待提交，根目录 `codex` 不跟踪。
- `D:\codex\codex-agent-deploy-local`
  - 分支：`feature/deploy-local`
  - HEAD：`ac8a7cd`
  - 状态：已合入 `develop`，后续可保留或清理。
- `D:\codex\codex-agent-frontend-shell`
  - 分支：`feature/frontend-shell`
  - HEAD：`f3270f2`
  - 状态：已合入 `develop`，后续可保留或清理。
- `D:\codex\codex-agent-backend-system`
  - 分支：`feature/backend-system`
  - HEAD：`4030a7c`
  - 状态：已合入 `develop`，迁移目录占用结束。
- `D:\codex\codex-agent-backend-metadata`
  - 分支：`feature/backend-metadata`
  - HEAD：`1ed60e4`
  - 状态：尚未启动，下一步需要先同步到最新 `develop`。
- `D:\codex\codex-agent-qa-verification`
  - 分支：`integration/qa-verification`
  - HEAD：`1ed60e4`
  - 状态：只读验收 worktree，下一轮验证前需要同步到最新 `develop`。

## GitHub 状态

- `origin/develop` 已推送到 `5466ca3`。
- PR #13、#14、#15 均已被 GitHub 识别为 merged 并关闭。
- `gh pr checks` 与 REST 读取接口可用。
- `gh pr view` 的 GraphQL 调用仍返回 401。
- `gh api --method PUT .../pulls/{id}/merge` 写入合并接口仍返回 401。
- 本轮采用本地 merge commit 加 `git push origin develop` 完成同步；后续若继续使用 GitHub PR 写接口，需要单独修复 `gh` token/GraphQL 写入问题。

## 下一步建议

- 提交并推送本次 `.codex/context/**` checkpoint 更新。
- 将 `feature/backend-metadata` worktree 更新到 `5466ca3` 后启动 `backend_metadata`。
- 将 `integration/qa-verification` worktree 更新到 `5466ca3`，用于下一轮只读验收。
- 启动 `backend_metadata` 前，沿用 `V1__system_security_audit.sql` 已建立的迁移命名、审计字段、`tenant_id` 预留和中文 SQL 注释风格。
- 暂不新增 `test_automation` agent；继续使用 `qa_verification` 做验收与复核。
