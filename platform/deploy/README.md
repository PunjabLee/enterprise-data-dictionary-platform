# Local Runtime

Local runtime dependencies for the platform skeleton.

## Services

- PostgreSQL 16 on port `5432`
- Redis 7 on port `6379`

## Startup

```powershell
docker compose up -d
```

## Shutdown

```powershell
docker compose down
```

To remove local data volumes:

```powershell
docker compose down -v
```

The default values are development placeholders only. Override them with environment variables or a local `.env` file for your machine. Do not commit real credentials.
