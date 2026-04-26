# Multi-Agent Development Workflow

## 1. Prepare Base

```powershell
git switch develop
git pull --ff-only
```

## 2. Create Worktrees

```powershell
git worktree add ..\codex-agent-owner -b owner/architecture-board develop
git worktree add ..\codex-agent-foundation -b feature/platform-skeleton develop
git worktree add ..\codex-agent-pr-steward -b integration/pr-steward develop
```

Create additional worktrees only after platform skeleton is merged:

```powershell
git worktree add ..\codex-agent-backend-system -b feature/backend-system develop
git worktree add ..\codex-agent-backend-metadata -b feature/backend-metadata develop
git worktree add ..\codex-agent-frontend-shell -b feature/frontend-shell develop
git worktree add ..\codex-agent-deploy-local -b feature/deploy-local develop
```

## 3. Handoff Template

Every subagent receives:

- Goal.
- Branch.
- Worktree.
- Write scope.
- Files it must not touch.
- Dependencies.
- Acceptance criteria.
- Validation commands.

## 4. Merge Order

1. `owner/architecture-board`
2. `feature/platform-skeleton`
3. `feature/backend-system`
4. `feature/backend-metadata`
5. `feature/frontend-shell`
6. `feature/backend-glossary`
7. `feature/frontend-catalog`
8. `feature/backend-lineage`
9. `feature/deploy-local`
10. `integration/qa-verification`

## 5. PR Rules

- All feature, docs, owner, chore, fix, poc, and integration PRs target `develop`.
- Only release and hotfix PRs target `main`.
- PR Steward checks branch policy first.
- No PR is reviewed for content if its base branch is wrong.
