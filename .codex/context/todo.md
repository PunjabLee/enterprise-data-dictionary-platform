# TODO

## P0

- Push local branch `chore/agent-definitions` to GitHub.
- Create PR `chore/agent-definitions -> develop`.
- Verify branch policy check passes for the PR.
- Merge PR after review.

## P1

- Delete or archive obsolete local branches after confirming no local-only work is needed:
  - `chore/sync-main-to-develop`
  - `docs/agent-pr-governance`
  - `hotfix/git-governance-policy`
- Review whether remote branches can be deleted if still present:
  - `origin/docs/business-glossary-design`
  - `origin/docs/p0-5-design-freeze`
- Create `owner/architecture-board` worktree for final skeleton scope review.
- Create `feature/platform-skeleton` worktree for Maven/Vue/Docker project skeleton.
- Create `integration/pr-steward` worktree for PR quality control.

## P2

- Configure GitHub branch protection rules manually in repository settings:
  - Protect `main`.
  - Protect `develop`.
  - Require status check: `Validate PR target branch`.
- Decide whether `.codex/prompts`, `.codex/rules`, and `.codex/skills` need concrete project templates now or later.
