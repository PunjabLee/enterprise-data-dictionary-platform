# Current Project Context

Last updated: 2026-04-26

## Repository

- Repository: `PunjabLee/enterprise-data-dictionary-platform`
- Local workspace: `D:\codex`
- Current local branch: `chore/agent-definitions`
- Base branch for daily integration: `develop`
- Stable branch: `main`

## Current Local Work

- Branch `chore/agent-definitions` is local-first work and has not yet been pushed to GitHub.
- It contains `a04f9d7 chore: add codex agent definitions`.
- It also adds restart context files and refreshed project README files in the follow-up local commit.
- Use `git log --oneline --decorate -5` after restart to get the exact latest local commit.

## Latest Known Remote Baseline

- `origin/main`: `4f8beca`
- `origin/develop`: `9af6059`

## Important Completed PRs

- PR #4: `hotfix/git-governance-policy -> main`, merged.
- PR #5: `hotfix/git-governance-policy -> develop`, merged.
- PR #6: `docs/agent-pr-governance -> develop`, merged.

## Branch Contents

The branch `chore/agent-definitions` adds:

- `.codex/agents/*.toml` agent definitions.
- `.codex/workflows/multi-agent-development.md`.
- `.codex/context/*` restart context files.
- `docs/06-实施交付/04-多Subagent创建与调度指南-v0.1.md`.
- Documentation registry updates.
- Root README refresh and Chinese README creation.

## Product Positioning

The platform is an enterprise data architecture governance and metadata operations platform, not only a CRUD data dictionary.

Primary outcomes:

- Enterprise metadata and data model inventory.
- Business glossary and data standard governance.
- Data architecture and lineage visualization.
- Architecture review and model change governance.
- Automated governance rules, issue tracking, tasks, notifications, and impact analysis.

Methodology alignment:

- DAMA-DMBOK2 for data governance, metadata, data quality, reference/master data, architecture, and stewardship.
- TOGAF 10 for architecture governance, architecture repository, ADM-aligned review, and enterprise architecture traceability.

## Key Architecture Decisions

- Backend: Java 21, Spring Boot 3, Maven.
- Frontend: Vue 3, TypeScript, Vite, Ant Design Vue.
- Architecture: DDD layered modular monolith.
- Persistence: PostgreSQL 16, Flyway, MyBatis.
- Mandatory data access chain: `Controller -> ApplicationService -> Repository -> DAO -> MyBatis Mapper`.
- Cache: Redis included in first phase.
- Search: OpenSearch reserved, not first-phase hard dependency.
- Workflow: lightweight task/status model first, Flowable reserved.
- Messaging: `DomainEventPublisher` abstraction; RocketMQ/Kafka pluggable later.
- Rules: lightweight configurable rules first; external rule engines reserved.
- Multi-tenancy: no SaaS multi-tenant first phase, but reserve `tenant_id`.

## Git Governance

- `main` only accepts `release/*` and `hotfix/*`.
- `develop` accepts daily integration branches.
- `docs/*`, `feature/*`, `fix/*`, `chore/*`, `owner/*`, `poc/*`, and `integration/*` must target `develop`.
- Do not use `pull/new/<branch>` links.
- Use explicit compare links: `compare/<base>...<branch>?expand=1`.
- Branch policy workflow exists at `.github/workflows/branch-policy.yml`.

## Multi-Agent Model

Project-level Codex custom agent definitions live in `.codex/agents`.

Codex discovers these agents when launched from `D:\codex`, or with `codex -C D:\codex`. Use `/agent` in the TUI to inspect or switch active agent threads; `/subagent` is not a supported slash command.

Primary agents:

- `main_orchestrator`.
- `owner_architecture`.
- `pr_steward`.
- `foundation`.
- `backend_system`.
- `backend_metadata`.
- `backend_glossary`.
- `backend_lineage`.
- `frontend_shell`.
- `frontend_catalog`.
- `dba`.
- `deploy_local`.
- `qa_verification`.

Execution rule:

- One subagent, one branch, one worktree, one write scope.
- Do not let multiple subagents write the same files concurrently.
- PR Steward checks branch policy before content review.

## Next Recommended Step

Push `chore/agent-definitions` to GitHub and create a PR into `develop`.

Correct PR URL format:

```text
https://github.com/PunjabLee/enterprise-data-dictionary-platform/compare/develop...chore/agent-definitions?expand=1
```

After that PR is merged, create `feature/platform-skeleton` and start the actual engineering skeleton.
