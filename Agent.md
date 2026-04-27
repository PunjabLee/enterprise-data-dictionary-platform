# Agent Operating Notes

## GitHub CLI Authorization

For this repository, GitHub CLI commands related to normal PR and branch orchestration are pre-approved by the user. Agents should run `gh` commands directly when they are needed for the task, without pausing to ask for confirmation first.

This includes commands such as:

- `gh auth status`
- `gh pr list`
- `gh pr view`
- `gh pr checks`
- `gh pr diff`
- `gh pr create`
- `gh pr edit`
- `gh pr merge`
- `gh api` calls needed to inspect or update repository PR, branch, commit, and ref state

## Git Authorization

For this repository, normal Git commands related to status checks, branching, worktrees, staging, committing, pulling, pushing, and PR branch cleanup are also pre-approved by the user. Agents should run required `git` commands directly when they are needed for the task, without pausing to ask for confirmation first.

This includes commands such as:

- `git status`
- `git diff`
- `git log`
- `git branch`
- `git switch`
- `git worktree`
- `git add`
- `git commit`
- `git pull`
- `git push`
- `git ls-remote`

Safety boundaries:

- Do not print, request, store, or commit GitHub tokens or other credentials.
- Do not write credentials into repository files, `.codex`, docs, scripts, or environment templates.
- Do not run destructive commands such as `git reset --hard`, `git clean`, or forced branch deletion unless the user explicitly requests that specific operation.
- For unusual destructive actions outside normal PR cleanup, explain the intended action in the work log before running it.
- Keep PR base/head policy unchanged: daily work targets `develop`; only `release/*` and `hotfix/*` target `main`.
