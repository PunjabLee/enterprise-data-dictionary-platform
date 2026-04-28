# Platform Skeleton

This directory contains the first engineering foundation for the enterprise data dictionary platform.

```text
platform/
  backend/metadata-platform/   Spring Boot 3 Maven application
  frontend/web-console/        Vue 3 TypeScript Vite console
  deploy/                      Local PostgreSQL and Redis runtime
  scripts/                     Local runtime helper scripts
  samples/                     Stable local demo fixtures
```

The skeleton intentionally avoids business modules. It only establishes local runtime boundaries, dependency baselines, package layout, and startup commands.

## Prerequisites

- Java 21
- Maven 3.9+
- Node.js 20+
- npm 10+
- Docker Compose v2

## Local Dependencies

Start PostgreSQL 16 and Redis:

```powershell
.\platform\scripts\local-up.ps1 -Wait
```

Equivalent direct command:

```powershell
docker compose -f platform/deploy/docker-compose.yml up -d
```

The compose file uses local-only placeholder credentials. Override values with environment variables or a local `.env` file if needed. Do not commit real credentials.

OpenSearch, Flowable, RocketMQ, and Kafka are reserved as optional future middleware and are not started by the local compose file.

## Backend

```powershell
cd platform/backend/metadata-platform
mvn spring-boot:run
```

The local profile is enabled by default and expects PostgreSQL and Redis from `platform/deploy/docker-compose.yml`.

Useful checks:

```powershell
mvn -pl platform/backend/metadata-platform -am -DskipTests compile
mvn -pl platform/backend/metadata-platform -am test
```

Health endpoint:

```text
http://localhost:8080/api/actuator/health
```

## Frontend

Install dependencies and start Vite:

```powershell
cd platform/frontend/web-console
npm install
npm run dev
```

The Vite dev server proxies `/api` to `http://localhost:8080`.

Useful checks:

```powershell
npm run type-check
npm run build
```
