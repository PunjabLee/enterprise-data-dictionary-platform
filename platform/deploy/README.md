# Local Runtime

This directory defines the local dependency runtime for the enterprise data dictionary platform.

## Scope

Required first-phase services:

- PostgreSQL 16 on host port `5432`
- Redis 7 on host port `6379`

Reserved but not enabled as default services:

- OpenSearch
- Flowable
- RocketMQ
- Kafka

These optional middleware components are recorded as compose placeholders only. Do not add them as hard dependencies unless the first-phase architecture decision changes.

## Quick Start

From the repository root:

```powershell
Copy-Item platform/deploy/.env.example platform/deploy/.env
docker compose --env-file platform/deploy/.env -f platform/deploy/docker-compose.yml up -d
docker compose --env-file platform/deploy/.env -f platform/deploy/docker-compose.yml ps
```

Or use the helper script:

```powershell
.\platform\scripts\local-up.ps1 -Wait
```

The helper script creates `platform/deploy/.env` from `.env.example` when it is missing. The local `.env` file is ignored by Git.

## Configuration

| Variable | Default | Purpose |
| --- | --- | --- |
| `POSTGRES_DB` | `metadata_platform` | Local database name |
| `POSTGRES_USER` | `metadata_app` | Local database user |
| `POSTGRES_PASSWORD` | `metadata_app_dev_password` | Local placeholder password |
| `POSTGRES_PORT` | `5432` | Host port mapped to PostgreSQL |
| `REDIS_PORT` | `6379` | Host port mapped to Redis |
| `METADATA_DB_URL` | `jdbc:postgresql://localhost:5432/metadata_platform` | Backend local JDBC URL |
| `METADATA_DB_USERNAME` | `metadata_app` | Backend local database user |
| `METADATA_DB_PASSWORD` | `metadata_app_dev_password` | Backend local database password |
| `METADATA_REDIS_HOST` | `localhost` | Backend local Redis host |
| `METADATA_REDIS_PORT` | `6379` | Backend local Redis port |

The values are development placeholders. Do not commit real credentials.

## Status

```powershell
.\platform\scripts\local-status.ps1
```

Direct Docker Compose command:

```powershell
docker compose -f platform/deploy/docker-compose.yml ps
```

## Shutdown

Stop containers and keep local volumes:

```powershell
.\platform\scripts\local-down.ps1
```

Remove containers and local data volumes:

```powershell
.\platform\scripts\local-down.ps1 -Volumes
```

Direct Docker Compose command:

```powershell
docker compose -f platform/deploy/docker-compose.yml down
docker compose -f platform/deploy/docker-compose.yml down -v
```

## Sample Data

Sample catalog, glossary, and lineage files live under `platform/samples/local-demo`. They are intentionally not mounted into PostgreSQL init scripts because database migrations and import APIs are owned by separate workstreams.

Use those files as manual import fixtures or API test payload sources after the backend import capability exists.

## Validation

Validate compose syntax without starting containers:

```powershell
docker compose -f platform/deploy/docker-compose.yml config
```
