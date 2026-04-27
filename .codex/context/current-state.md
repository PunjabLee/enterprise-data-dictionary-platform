# Current Project Context

Last updated: 2026-04-27

## Repository

- Repository: `PunjabLee/enterprise-data-dictionary-platform`
- Main local workspace: `D:\codex\enterprise-data-dictionary-platform`
- Daily integration branch: `develop`
- Stable branch: `main`
- Current local branch for this update: `owner/orchestration-status`

## Current Baseline

- `chore/agent-definitions` has been merged into `develop` through PR #8.
- `owner/architecture-board` has been merged into `develop` through PR #9.
- `owner/freeze-gate` has been merged into `develop` through PR #10.
- `feature/platform-skeleton` has been merged into `develop` through PR #11.
- Remote and local `develop` latest known commit: `cd3eec787a7c12cf2d97da8b597e837360c6d738`.

## Completed Owner Work

PR #9 added architecture specification and detail documents for:

- Business architecture.
- Application architecture.
- Technology architecture.
- Data architecture.

It also updated the architecture README and document inventory.

PR #10 refreshed Codex restart context and marked the coding freeze checklist as confirmed.

## Completed Platform Skeleton

PR #11 added:

- Root Maven reactor `pom.xml`.
- Spring Boot 3 backend skeleton at `platform/backend/metadata-platform`.
- Vue 3 + TypeScript + Vite + Ant Design Vue frontend skeleton at `platform/frontend/web-console`.
- ESLint, type-check, and build gates for the frontend skeleton.
- Local PostgreSQL 16 and Redis Docker Compose runtime at `platform/deploy`.

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

Second-batch worktrees have been created from `develop`:

- `D:\codex\codex-agent-backend-system` on `feature/backend-system`.
- `D:\codex\codex-agent-backend-metadata` on `feature/backend-metadata`.
- `D:\codex\codex-agent-frontend-shell` on `feature/frontend-shell`.
- `D:\codex\codex-agent-deploy-local` on `feature/deploy-local`.

Next implementation should start with `frontend_shell` and `deploy_local`, plus a tightly scoped `backend_system` foundation. Do not let `backend_system` and `backend_metadata` both write `platform/backend/**/db/migration/**` in parallel.
