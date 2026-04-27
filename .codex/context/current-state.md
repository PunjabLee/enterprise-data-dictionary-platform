# Current Project Context

Last updated: 2026-04-27

## Repository

- Repository: `PunjabLee/enterprise-data-dictionary-platform`
- Main local workspace: `D:\codex\enterprise-data-dictionary-platform`
- Daily integration branch: `develop`
- Stable branch: `main`
- Current local branch for this update: `owner/freeze-gate`

## Current Baseline

- `chore/agent-definitions` has been merged into `develop` through PR #8.
- `owner/architecture-board` has been merged into `develop` through PR #9.
- Remote `develop` latest known commit: `64b8ef9998f8e8973803e351bf33cc0e93db6def`.
- Local Git transport to GitHub is currently failing with connection reset/timeouts, so local `develop` still needs a successful `git pull --ff-only` when connectivity recovers.

## Completed Owner Work

PR #9 added architecture specification and detail documents for:

- Business architecture.
- Application architecture.
- Technology architecture.
- Data architecture.

It also updated the architecture README and document inventory.

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
- Authentication: local account/JWT first, OIDC/LDAP/AD reserved.

## Git Governance

- `main` only accepts `release/*` and `hotfix/*`.
- `develop` accepts daily integration branches.
- `docs/*`, `feature/*`, `fix/*`, `chore/*`, `owner/*`, `poc/*`, and `integration/*` must target `develop`.
- Use explicit compare links: `compare/<base>...<branch>?expand=1`.
- Branch policy workflow exists at `.github/workflows/branch-policy.yml`.

## Multi-Agent Model

Project-level Codex custom agent definitions live in `.codex/agents`.

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
- PR Steward checks branch policy first.

## Next Recommended Step

After this freeze/context update is merged, sync local `develop` and create the first implementation worktrees:

```powershell
git switch develop
git pull --ff-only
git worktree add ..\codex-agent-foundation -b feature/platform-skeleton develop
git worktree add ..\codex-agent-pr-steward -b integration/pr-steward develop
```

Start with `foundation` and `pr_steward`; defer backend, frontend, DBA, deploy, and QA implementation agents until the platform skeleton PR is merged.
