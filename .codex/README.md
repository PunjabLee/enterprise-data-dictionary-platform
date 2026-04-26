# Codex Agent Workspace

This directory stores project-level agent definitions, workflows, prompts, rules, and skills for the enterprise data dictionary platform.

Tracked directories:

- `agents/`: Codex custom agent definitions for main and subagent roles.
- `workflows/`: Repeatable multi-agent execution workflows.
- `prompts/`: Shared prompt templates.
- `rules/`: Project-specific rules and guardrails.
- `skills/`: Project-local skills, if needed later.

Rules:

- Agent definitions are versioned with the repository.
- Codex discovers these project-level custom agents when launched from this repository root, or with `codex -C D:\codex`.
- Use `/agent` in the Codex TUI to inspect or switch active agent threads; `/subagent` is not a supported slash command.
- Secrets, tokens, credentials, and personal runtime state must not be stored here.
- Every implementation subagent must use an isolated branch and worktree.
- PRs from `docs/*`, `feature/*`, `fix/*`, `owner/*`, `chore/*`, `poc/*`, and `integration/*` must target `develop`.
