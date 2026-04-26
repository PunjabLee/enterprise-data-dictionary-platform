# Enterprise Data Dictionary Platform

Enterprise Data Dictionary Platform is a planned enterprise-grade metadata governance, data architecture, data dictionary, business glossary, and lineage management platform.

The project is currently in the documentation, architecture, governance, and engineering-skeleton preparation stage. It already contains product planning, PRD, architecture decisions, metadata model design, API/DTO specifications, Git governance, and multi-agent collaboration definitions. The application code skeleton has not been created yet.

[中文说明](README.zh-CN.md)

## Why This Project Exists

Large enterprises often have many IT systems, inconsistent data definitions, fragmented data models, unclear ownership, and weak impact analysis before system changes. This platform is designed to become the governance authority for enterprise metadata, data models, business terms, data standards, lineage, and architecture review workflows.

It is not only a CRUD-style data dictionary. The intended product scope is an enterprise data architecture governance and metadata operations platform.

## Methodology Alignment

The platform design is aligned with:

- DAMA-DMBOK2: data governance, metadata management, data architecture, data quality, data stewardship, reference data, and data security.
- TOGAF 10: architecture governance, architecture repository, ADM-aligned review, architecture artifacts, and enterprise architecture traceability.

## Product Scope

Planned core capabilities:

- Enterprise data asset catalog for systems, databases, schemas, tables, fields, interfaces, reports, jobs, and data products.
- Data dictionary management for physical fields, business definitions, constraints, data types, owners, and lifecycle states.
- Conceptual, logical, and physical data model management.
- Business Glossary for `BusinessTerm`, term relationships, references, conflicts, approvals, and impact analysis.
- Data standards and standard-reference governance.
- Lineage and impact analysis across systems, interfaces, tables, reports, and key fields.
- Architecture governance workflows for system construction review, model change approval, lineage confirmation, standard publishing, and responsibility claiming.
- Automated governance engines for rules, diffs, tasks, events, notifications, reports, issue tracking, and remediation loops.
- RBAC, data scope, sensitive-field control, audit, and export governance.

## Current Stage

Current status:

- Documentation baseline is established.
- Product scope and MVP requirements are documented.
- Architecture and technical decisions are documented.
- Metadata model, physical database model, API DTOs, Excel import/export, security classification, workflow, and test cases are documented.
- Git branch flow and PR target branch policy are enforced through documentation and GitHub Actions.
- Multi-agent and subagent collaboration definitions are available under `.codex/agents`.

Not yet started:

- Backend Maven/Spring Boot project skeleton.
- Frontend Vue/Vite/Ant Design Vue project skeleton.
- Docker Compose runtime skeleton.
- Flyway executable migrations.
- Production-ready application features.

## Architecture Decisions

Key decisions already captured in ADR and architecture documents:

| Area | Decision |
| --- | --- |
| Backend | Java 21, Spring Boot 3, Maven |
| Frontend | Vue 3, TypeScript, Vite, Ant Design Vue |
| Architecture style | DDD layered modular monolith |
| Persistence | PostgreSQL 16, Flyway, MyBatis |
| Data access chain | `Controller -> ApplicationService -> Repository -> DAO -> MyBatis Mapper` |
| Cache | Redis included in first phase |
| Search | OpenSearch reserved, not a first-phase hard dependency |
| Workflow | Lightweight workflow/task/status model first; Flowable reserved |
| Messaging | `DomainEventPublisher` abstraction; RocketMQ/Kafka pluggable later |
| Rules | Lightweight configurable governance rules first; rule engines reserved |
| Multi-tenancy | No SaaS multi-tenant first phase; reserve `tenant_id` |
| Security | RBAC, data scope, sensitive-field control, audit |

## DDD Layering Rules

The backend must follow DDD layering:

```text
interfaces -> application -> domain
                         -> infrastructure
```

Mandatory persistence access path:

```text
Controller
  -> ApplicationService
  -> Repository
  -> DAO
  -> MyBatis Mapper
```

Application services must not call MyBatis mappers directly.

## Repository Structure

```text
.
|-- .codex/                  # Project-level agent definitions, workflows, rules, context
|-- .github/                 # PR template and branch policy workflow
|-- docs/                    # Planning, PRD, architecture, governance, delivery, testing
|-- tools/                   # Repository automation scripts
|-- templates/               # Legacy/generated workspace artifacts, ignored by Git
|-- README.md                # English project overview
|-- README.zh-CN.md          # Chinese project overview
```

Documentation structure:

```text
docs/
|-- 00-文档治理
|-- 01-总体规划
|-- 02-产品需求
|-- 03-架构设计
|-- 04-数据治理与元模型
|-- 05-接口与集成
|-- 06-实施交付
|-- 07-模板与表单
|-- 08-测试验收
|-- 09-会议纪要与决策
|-- 10-归档
```

## Key Documents

Recommended reading order:

1. [Documentation Home](docs/README.md)
2. [Solution Outline](docs/01-总体规划/企业级数据字典管理系统方案总纲.md)
3. [Detailed PRD](docs/02-产品需求/01-详细PRD-v0.2.md)
4. [Product Function Panorama And Automation Scope](docs/02-产品需求/03-产品功能全景与自动化业务说明-v0.1.md)
5. [Technical Architecture](docs/03-架构设计/01-技术架构设计-v0.1.md)
6. [Application Architecture](docs/03-架构设计/06-应用架构设计-v0.1.md)
7. [Technical Decisions ADR](docs/09-会议纪要与决策/01-技术决策ADR-v0.1.md)
8. [Enterprise Metadata Model](docs/04-数据治理与元模型/01-企业元模型设计说明书-v0.1.md)
9. [Business Glossary Design](docs/04-数据治理与元模型/08-业务术语管理设计-v0.1.md)
10. [Git PR Flow Policy](docs/00-文档治理/Git管理模型与PR流向控制规范-v0.1.md)
11. [Multi-Subagent Guide](docs/06-实施交付/04-多Subagent创建与调度指南-v0.1.md)

Full registry:

- [Document Registry](docs/00-文档治理/文档清单.md)

## Git Branch Model

Current governance rule:

```text
main    <- release/* or hotfix/* only
develop <- docs/*, feature/*, fix/*, chore/*, owner/*, poc/*, integration/*
```

Rules:

- `main` is the stable branch.
- `develop` is the daily integration branch.
- Do not use `pull/new/<branch>` links because GitHub may default the base branch to `main`.
- Use explicit compare links: `compare/<base>...<branch>?expand=1`.
- Branch flow is enforced by `.github/workflows/branch-policy.yml`.

## Multi-Agent Collaboration

Project-level agent definitions are stored in:

```text
.codex/agents
```

These files are Codex custom agent TOML definitions. Start Codex from this repository root, or use `codex -C D:\codex`, so project-level agents are discovered. In the Codex TUI, use `/agent` to inspect or switch active agent threads; `/subagent` is not a supported slash command.

Primary agents:

- `main_orchestrator`
- `owner_architecture`
- `pr_steward`
- `foundation`
- `backend_system`
- `backend_metadata`
- `backend_glossary`
- `backend_lineage`
- `frontend_shell`
- `frontend_catalog`
- `dba`
- `deploy_local`
- `qa_verification`

Core rule:

```text
One subagent, one branch, one worktree, one write scope.
```

See:

- [.codex Agent Definitions](.codex/agents/README.md)
- [Multi-Agent Workflow](.codex/workflows/multi-agent-development.md)
- [Multi-Subagent Creation Guide](docs/06-实施交付/04-多Subagent创建与调度指南-v0.1.md)

## Restart Context

For Codex CLI restart or handoff, read:

```text
.codex/context/current-state.md
.codex/context/todo.md
```

These files capture the latest local context, pending tasks, branch status, and next recommended actions. Secrets must not be stored there.

## Next Engineering Step

After `chore/agent-definitions` is pushed and merged into `develop`, create:

```text
feature/platform-skeleton
```

Initial skeleton scope:

- Maven multi-module backend foundation.
- Vue 3 + TypeScript + Vite + Ant Design Vue frontend foundation.
- Docker Compose for PostgreSQL and Redis.
- Flyway migration structure.
- Local development README and scripts.

## Security Notes

- Do not commit tokens, passwords, private keys, production connection strings, or personal credentials.
- GitHub credentials must be handled through Git Credential Manager, GitHub CLI, or environment variables outside the repository.
- Sensitive-field access, export, and audit are first-class platform requirements.

## License

No license has been selected yet.
