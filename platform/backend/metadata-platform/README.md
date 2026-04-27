# Metadata Platform Backend

Spring Boot 3 backend skeleton for the metadata platform.

## Baseline

- Java 21
- Maven
- Spring Boot 3
- PostgreSQL 16 JDBC driver
- Flyway migration directory
- MyBatis mapper directory
- Redis client dependency

## Package Boundaries

```text
com.company.metadata
  interfaces       HTTP/API adapters
  application      use-case orchestration and transaction boundaries
  domain           domain model and domain services
  infrastructure   repositories, DAO, MyBatis, cache, and external adapters
  common           cross-cutting foundation code
```

No business modules are implemented in this skeleton.

## Local Startup

Start dependencies first:

```powershell
cd ../../deploy
docker compose up -d
```

Run the backend:

```powershell
cd ../backend/metadata-platform
mvn spring-boot:run
```

Health check:

```text
GET http://localhost:8080/api/actuator/health
```
