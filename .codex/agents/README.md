# Agent Definitions

This directory contains project-level Codex custom agents for parallel product development.

Codex discovers these agents when the CLI is launched from the repository root, or when started with:

```powershell
codex -C D:\codex
```

In the interactive TUI, use `/agent` to inspect or switch active agent threads. `/subagent` is not a supported slash command.

Each agent TOML uses the Codex custom agent schema:

```toml
name = "backend_metadata"
description = "Short discoverable description."
developer_instructions = '''
Role-specific instructions, write scope, branch, worktree, and validation commands.
'''
```

## Agent Types

| File | Custom agent name | Role |
| --- | --- | --- |
| `main-orchestrator.toml` | `main_orchestrator` | Main orchestration agent |
| `owner-architecture.toml` | `owner_architecture` | Product and architecture owner |
| `pr-steward.toml` | `pr_steward` | PR quality gate and integration steward |
| `foundation.toml` | `foundation` | Platform skeleton and common engineering foundation |
| `backend-system.toml` | `backend_system` | Users, organizations, RBAC, audit |
| `backend-metadata.toml` | `backend_metadata` | Metadata model, assets, field dictionary |
| `backend-glossary.toml` | `backend_glossary` | Business glossary and business terms |
| `backend-lineage.toml` | `backend_lineage` | Lineage and impact analysis |
| `frontend-shell.toml` | `frontend_shell` | Frontend application shell |
| `frontend-catalog.toml` | `frontend_catalog` | Catalog, assets, fields, glossary pages |
| `deploy-local.toml` | `deploy_local` | Local deployment and sample data |
| `qa-verification.toml` | `qa_verification` | Verification, checks, and acceptance |
| `dba.toml` | `dba` | Database model, migration, and SQL review |

## Creation Rule

Create one worktree per implementation agent:

```powershell
git switch develop
git pull --ff-only
git worktree add ..\codex-agent-foundation -b feature/platform-skeleton develop
git worktree add ..\codex-agent-owner -b owner/architecture-board develop
git worktree add ..\codex-agent-pr-steward -b integration/pr-steward develop
```

Do not let two agents write to the same branch or the same file set at the same time.
