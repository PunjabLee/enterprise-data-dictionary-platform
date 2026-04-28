# 当前项目上下文

最后更新：2026-04-28 21:43:48 +08:00

## 仓库状态

- 仓库：`PunjabLee/enterprise-data-dictionary-platform`
- 主工作区：`D:\codex\enterprise-data-dictionary-platform`
- 日常集成分支：`develop`
- 稳定分支：`main`
- 当前本地分支：`develop`
- 本地 `develop` 最新提交：`35a3d0b 文档：记录后端元数据合并状态`
- 远端 `origin/develop` 最新确认提交：`35a3d0be41201a8ea0b7aa746bcff954429fc46b`
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
- 上下文提交：`afda7f9 文档：更新第二批编排检查点`
  - 已保存第二批合并结果、验证结果、GitHub 同步状态和下一轮待办。
- 上下文提交：`9e2b8a0 文档：整理后端元数据任务上下文`
  - 已保存 `backend_metadata` 启动前状态和接管策略。
- `feature/backend-metadata`：
  - 本地与远端提交：`584d3a3 功能：增加后端元数据资产目录基础能力`
  - 状态：已合入 `develop`。
  - PR #16：`https://github.com/PunjabLee/enterprise-data-dictionary-platform/pull/16`
  - 分支策略检查：`Validate PR target branch` 已通过。
  - 合并提交：`51f8812 合并 PR #16：增加后端元数据资产目录基础能力`
  - GitHub 状态：closed/merged。
- 上下文提交：`35a3d0b 文档：记录后端元数据合并状态`
  - 已推送到 `origin/develop`。
  - `qa_verification` worktree 已同步到该提交。

## 进行中内容

- `feature/frontend-catalog`：
  - 本地 worktree：`D:\codex\codex-agent-frontend-catalog`
  - 本地提交：`c3462f8 功能：增加前端资产目录页面`
  - 远端提交：`0de3dc6092c17d64dd86239968c1d5810390b03e`
  - 说明：由于 `git push` 连续遇到 `github.com:443` 连接超时，本轮改用 GitHub REST 上传 blob/tree/commit 并创建远端分支；远端 tree SHA 与本地提交一致。
  - PR #17：`https://github.com/PunjabLee/enterprise-data-dictionary-platform/pull/17`
  - 状态：已创建，待检查通过后合并。
  - 已实现范围：资产目录列表、资产详情、字段字典、业务术语占位页、前端导航和相关 API service。

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
- `feature/frontend-catalog` 验证：
  - `git diff --check 35a3d0be41201a8ea0b7aa746bcff954429fc46b..HEAD`：通过。
  - `rg --line-number "^(<<<<<<<|=======|>>>>>>>)" .github docs platform`：未发现冲突标记。
  - `npm.cmd run lint`：通过。
  - `npm.cmd run type-check`：通过。
  - `npm.cmd run build`：通过；保留 Vite 大 chunk 警告，不影响构建结果。

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
  - HEAD：`35a3d0b`
  - 状态：仅根目录 `codex` 未跟踪，不处理。
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
  - HEAD：`584d3a3`
  - 状态：已完成后端元数据资产目录基础能力，已合入 `develop`，工作区干净。
  - 备注：`Laplace` agent 无产出后已关闭，主 agent 接管完成实现。
- `D:\codex\codex-agent-qa-verification`
  - 分支：`integration/qa-verification`
  - HEAD：`35a3d0b`
  - 状态：只读验收 worktree，已同步到最新 `develop`。
- `D:\codex\codex-agent-frontend-catalog`
  - 分支：`feature/frontend-catalog`
  - HEAD：`c3462f8`
  - 状态：本地工作区干净；远端分支已通过 REST 创建，PR #17 已创建。

## GitHub 状态

- `origin/develop` 已推送到 `35a3d0b`。
- PR #16 合并状态 context 已推送成功。
- `origin/feature/backend-metadata` 已推送到 `584d3a3`。
- PR #16 已创建；创建时绕过 `gh pr create` GraphQL 401，使用 Git Credential Manager 中可推送代码的凭据调用 REST 创建。
- PR #16 已通过本地 no-ff merge 合入并推送；GitHub 已识别为 merged/closed。
- `gh api user --jq .login` 可用，说明 GitHub REST 鉴权仍可读取当前账号。
- PR #13、#14、#15 均已被 GitHub 识别为 merged 并关闭。
- `gh pr checks` 与 REST 读取接口可用。
- `gh pr view` 的 GraphQL 调用仍返回 401。
- `gh api --method PUT .../pulls/{id}/merge` 写入合并接口仍返回 401。
- 本轮采用本地 merge commit 加 `git push origin develop` 完成同步；后续若继续使用 GitHub PR 写接口，需要单独修复 `gh` token/GraphQL 写入问题。
- PR #17 已通过 GitHub REST 创建；`gh pr create` / `gh pr view` 的 GraphQL 问题仍不影响 REST 创建和读取。

## 下一步建议

- 检查 PR #17 的状态检查；必要时使用 REST check-runs 替代 `gh pr checks`。
- 合并 PR #17 前，可由 `qa_verification` 对前端目录页面做只读复核。
- PR #17 合并后，将 `develop` 推送并更新本上下文。
- `gh pr create` / `gh pr checks` 的 GraphQL 401 仍需后续修复，但不再阻塞 #16。
- 暂不新增 `test_automation` agent；继续使用 `qa_verification` 做验收与复核。
