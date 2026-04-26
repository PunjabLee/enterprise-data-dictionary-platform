# Git 管理模型与 PR 流向控制规范 v0.1

## 1. 目标

本规范用于约束企业级数据字典平台的分支流向、PR base 选择、主干保护和自动化检查，防止文档分支、功能分支或 Owner 分支误合并到 `main`。

本规范是 `Git分支与版本管理规范.md` 的强制执行细则。若两者存在表述差异，以本规范为准。

## 2. 当前纠偏结论

当前已经发生过 `docs/*` 分支误合并到 `main` 的情况。纠偏策略如下：

1. 不回滚 `main`，因为文档内容本身有效，回滚会制造额外历史和冲突。
2. 使用 `chore/sync-main-to-develop` 将 `main` 已有内容同步回 `develop`。
3. 使用 `hotfix/git-governance-policy` 将分支策略检查尽快合入 `main`，让默认分支具备自动阻断能力。
4. 将同一 `hotfix/git-governance-policy` 回合到 `develop`，保持长期集成分支一致。
5. 后续所有 `docs/*`、`feature/*`、`owner/*`、`poc/*`、`chore/*`、`fix/*` 默认只允许合入 `develop`。
6. `main` 仅接收 `release/*` 和 `hotfix/*`。
7. 使用 `.github/workflows/branch-policy.yml` 对错误 PR 流向做自动检查。

## 3. 分支职责

| 分支 | 职责 | 是否长期存在 |
| --- | --- | --- |
| `main` | 已发布、可交付、稳定版本 | 是 |
| `develop` | 日常集成分支，承接文档、功能、修复、治理变更 | 是 |
| `docs/*` | 文档新增和修订 | 否 |
| `feature/*` | 功能开发 | 否 |
| `fix/*` | 非生产紧急缺陷修复 | 否 |
| `owner/*` | 产品/架构 Owner 决策、范围、ADR、验收口径 | 否 |
| `poc/*` | 技术验证和可丢弃实验 | 否 |
| `chore/*` | 仓库治理、CI、分支同步、脚本维护 | 否 |
| `integration/*` | 多 PR 冲突整合和临时集成验证 | 否 |
| `release/*` | 从 `develop` 拉出，准备发布到 `main` | 否 |
| `hotfix/*` | 从 `main` 拉出，修复生产紧急问题 | 否 |

## 4. 允许的 PR 流向

| Head 分支 | Base 分支 | 是否允许 | 说明 |
| --- | --- | --- | --- |
| `docs/*` | `develop` | 允许 | 文档变更先进入集成分支 |
| `feature/*` | `develop` | 允许 | 功能开发先进入集成分支 |
| `fix/*` | `develop` | 允许 | 普通缺陷修复先进入集成分支 |
| `owner/*` | `develop` | 允许 | Owner 决策先进入集成分支 |
| `poc/*` | `develop` | 允许 | 仅被确认保留的验证结果合入 |
| `chore/*` | `develop` | 允许 | 仓库治理和分支同步 |
| `integration/*` | `develop` | 允许 | 多 PR 整合验证后进入集成分支 |
| `release/*` | `main` | 允许 | 发布候选合入稳定分支 |
| `hotfix/*` | `main` | 允许 | 生产紧急修复 |
| `release/*` | `develop` | 允许 | 发布修复回合到集成分支 |
| `hotfix/*` | `develop` | 允许 | 热修复回合到集成分支 |

## 5. 禁止的 PR 流向

| Head 分支 | Base 分支 | 处理方式 |
| --- | --- | --- |
| `docs/*` | `main` | 关闭 PR，重新以 `develop` 为 base 创建 |
| `feature/*` | `main` | 关闭 PR，重新以 `develop` 为 base 创建 |
| `fix/*` | `main` | 除非升级为 `hotfix/*`，否则关闭 |
| `owner/*` | `main` | 关闭 PR，重新以 `develop` 为 base 创建 |
| `poc/*` | `main` | 关闭 PR，不允许实验分支进稳定分支 |
| `chore/*` | `main` | 除发布治理特殊场景外关闭；默认先入 `develop` |
| `integration/*` | `main` | 关闭 PR，集成分支不能直接发布 |

## 6. PR 链接规范

禁止使用 GitHub 默认的 `pull/new/<branch>` 链接，因为它通常会默认选择仓库默认分支 `main` 作为 base。

正确链接格式：

```text
https://github.com/PunjabLee/enterprise-data-dictionary-platform/compare/develop...<branch>?expand=1
```

示例：

```text
https://github.com/PunjabLee/enterprise-data-dictionary-platform/compare/develop...docs/agent-pr-governance?expand=1
https://github.com/PunjabLee/enterprise-data-dictionary-platform/compare/develop...hotfix/git-governance-policy?expand=1
```

只有 `release/*` 或 `hotfix/*` 面向 `main` 时才使用：

```text
https://github.com/PunjabLee/enterprise-data-dictionary-platform/compare/main...release/v0.1-mvp?expand=1
https://github.com/PunjabLee/enterprise-data-dictionary-platform/compare/main...hotfix/git-governance-policy?expand=1
```

## 7. 创建 PR 前检查

提交 PR 前必须确认：

| 检查项 | 要求 |
| --- | --- |
| Base 分支 | 非 `release/*`、`hotfix/*` 不得选择 `main` |
| Head 分支 | 分支名前缀必须符合规范 |
| 写入范围 | PR 描述必须说明写入目录 |
| 文档清单 | 新增文档必须更新 `docs/00-文档治理/文档清单.md` |
| 架构影响 | 涉及架构、API、数据库、权限、安全必须说明 |
| 验证结果 | 至少执行 `git diff --check` 和冲突标记检查 |

## 8. PR Steward 检查

PR Steward Agent 必须先检查 base/head，再评审内容质量。

处理规则：

1. PR base 错误时，不进入内容评审。
2. PR base 错误时，要求关闭并重建 PR，不建议在错误 PR 上继续追加提交。
3. `docs/* -> main`、`feature/* -> main`、`owner/* -> main` 必须直接拒绝。
4. 如果 GitHub Actions 已阻断错误流向，不允许管理员手工绕过，除非形成 ADR。

## 9. GitHub Actions 策略

`.github/workflows/branch-policy.yml` 执行以下规则：

- `main` 只允许 `release/*`、`hotfix/*` 作为 head 分支。
- `develop` 允许 `docs/*`、`feature/*`、`fix/*`、`chore/*`、`owner/*`、`poc/*`、`integration/*`、`release/*`、`hotfix/*`。
- 其他 base 分支不在当前治理范围内。

该检查不能替代人工评审，但可以阻止最关键的 PR 流向错误。

## 10. 主干保护建议

GitHub 仓库应配置：

| 分支 | 建议保护 |
| --- | --- |
| `main` | Require pull request、Require status checks、禁止直接 push、限制管理员绕过 |
| `develop` | Require pull request、Require status checks、禁止直接 push |

`main` 的 required status check 应包含：

- `Branch Policy / Validate PR target branch`

`develop` 的 required status check 应包含：

- `Branch Policy / Validate PR target branch`

## 11. 纠偏操作顺序

当前纠偏按以下顺序执行：

1. PR: `chore/sync-main-to-develop -> develop`，同步已经误入 `main` 的有效文档。
2. PR: `hotfix/git-governance-policy -> main`，让 `main` 立即具备分支流向自动检查。
3. PR: `hotfix/git-governance-policy -> develop`，将治理策略回合到日常集成分支。
4. PR: `docs/agent-pr-governance -> develop`，继续合入多 Agent 和 Owner 机制文档。
5. 后续所有工程骨架和功能开发分支从最新 `develop` 拉出。

## 12. 禁止事项

- 禁止给出或使用 `pull/new/<branch>` 作为 PR 链接。
- 禁止 `docs/*`、`feature/*`、`owner/*` 直接合入 `main`。
- 禁止为了省事将 `main` 当作日常集成分支。
- 禁止跳过 `develop` 直接发布。
- 禁止在 PR base 错误时继续评审业务内容。
- 禁止管理员绕过分支策略强行合并，除非形成书面 ADR。
