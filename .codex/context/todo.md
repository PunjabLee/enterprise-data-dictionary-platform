# TODO

## P0

- Push and create PR for `owner/freeze-gate -> develop` after Git transport connectivity recovers.
- Verify the freeze/context PR passes branch policy and contains only governance/context changes.
- Merge the freeze/context PR into `develop`.
- Sync local `develop` with `git pull --ff-only`.

## P1

- Create first implementation worktrees from updated `develop`:
  - `..\codex-agent-foundation` on `feature/platform-skeleton`.
  - `..\codex-agent-pr-steward` on `integration/pr-steward`.
- Assign `foundation` the platform skeleton only:
  - Maven/Spring Boot backend skeleton.
  - Vue/Vite/Ant Design Vue frontend skeleton.
  - Docker Compose for PostgreSQL and Redis.
  - Flyway migration directory.
  - Local startup README/scripts.
- Assign `pr_steward` read-only PR quality control for branch policy, write scope, validation, and merge readiness.

## P2

- After `feature/platform-skeleton` merges, create second-batch worktrees:
  - `backend_system`.
  - `backend_metadata`.
  - `frontend_shell`.
  - `deploy_local`.
- Configure GitHub branch protection rules manually in repository settings:
  - Protect `main`.
  - Protect `develop`.
  - Require status check: `Validate PR target branch`.
