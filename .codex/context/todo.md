# TODO

## P0

- Push and create PR for `owner/orchestration-status -> develop`.
- Verify the orchestration-status PR passes branch policy and contains only `.codex/context/**` updates.
- Merge the orchestration-status PR into `develop`.

## P1

- Assign `frontend_shell` on `feature/frontend-shell`:
  - Own frontend layout, route guards, shared API client, stores, and theme only.
  - Do not implement deep catalog/glossary pages.
  - Validate with `npm run lint`, `npm run type-check`, and `npm run build`.
- Assign `deploy_local` on `feature/deploy-local`:
  - Own local startup scripts, sample data folders, and deployment README improvements.
  - Do not make OpenSearch, Flowable, MQ, or Kafka required.
  - Validate with `docker compose -f platform/deploy/docker-compose.yml config`.
- Assign `backend_system` on `feature/backend-system` only after its migration ownership is explicitly reserved:
  - Own system module, security, audit, users, roles, permissions, and data scope.
  - Keep `db/migration/**` exclusive to this branch until merged.
  - Validate with JDK 21 and `mvn -pl platform/backend/metadata-platform -am test`.

## P2

- Start `backend_metadata` after `backend_system` or a dedicated DBA migration PR establishes shared database conventions:
  - Own metadata model, assets, attributes, field dictionary, and versions.
  - Do not write `db/migration/**` while `backend_system` owns migration changes.
- Configure GitHub branch protection rules manually in repository settings:
  - Protect `main`.
  - Protect `develop`.
  - Require status check: `Validate PR target branch`.
