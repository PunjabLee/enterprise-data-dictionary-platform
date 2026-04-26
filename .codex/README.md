# Codex Agent Workspace

This directory stores project-level agent definitions, workflows, prompts, rules, and skills for the enterprise data dictionary platform.

Tracked directories:

- `agents/`: Main agent and subagent role definitions.
- `workflows/`: Repeatable multi-agent execution workflows.
- `prompts/`: Shared prompt templates.
- `rules/`: Project-specific rules and guardrails.
- `skills/`: Project-local skills, if needed later.

Rules:

- Agent definitions are versioned with the repository.
- Secrets, tokens, credentials, and personal runtime state must not be stored here.
- Every implementation subagent must use an isolated branch and worktree.
- PRs from `docs/*`, `feature/*`, `fix/*`, `owner/*`, `chore/*`, `poc/*`, and `integration/*` must target `develop`.
