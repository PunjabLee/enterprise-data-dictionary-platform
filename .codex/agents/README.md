# Agent Definitions

This directory defines the main agent and subagents used for parallel product development.

## Agent Types

| File | Role |
| --- | --- |
| `main-orchestrator.toml` | Main orchestration agent |
| `owner-architecture.toml` | Product and architecture owner |
| `pr-steward.toml` | PR quality gate and integration steward |
| `foundation.toml` | Platform skeleton and common engineering foundation |
| `backend-system.toml` | Users, organizations, RBAC, audit |
| `backend-metadata.toml` | Metadata model, assets, field dictionary |
| `backend-glossary.toml` | Business glossary and business terms |
| `backend-lineage.toml` | Lineage and impact analysis |
| `frontend-shell.toml` | Frontend application shell |
| `frontend-catalog.toml` | Catalog, assets, fields, glossary pages |
| `deploy-local.toml` | Local deployment and sample data |
| `qa-verification.toml` | Verification, checks, and acceptance |
| `dba.toml` | Database model, migration, and SQL review |

## Creation Rule

Create one worktree per agent:

```powershell
git switch develop
git pull --ff-only
git worktree add ..\codex-agent-foundation -b feature/platform-skeleton develop
git worktree add ..\codex-agent-owner -b owner/architecture-board develop
git worktree add ..\codex-agent-pr-steward -b integration/pr-steward develop
```

Do not let two agents write to the same branch or the same file set at the same time.
