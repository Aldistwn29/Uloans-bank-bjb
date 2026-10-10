# ULoans — Micro Loan Scoring API for BJB

Analyst inputs NIK + Amount → Spring Boot checks BI Checking + AMLO + UKM Score → returns APPROVED / REJECTED.

> Status: Backend `DOING` (Sprints 1-4), Frontend React `BLOCKED` until the API contract is stable. API tested via Postman.

## Problem

BJB KCP credit analysts need a fast, traceable way to pre-screen micro-loan (UKM) applications. Manual BI + history checks are slow and unauditable.

ULoans centralizes it into one call: `POST /api/loan-applications`.

## Features

### Implemented (DONE)

- Domain models (`src/main/java/com/bjb/uloans/model/`):
  `Customer` (NIK UNIQUE 16 chars), `LoanHistory`, `LoanApplication` (decision enum `APPROVED/REJECTED`), `User` (role `ANALYST`), `ComplianceLog`, `AuditLog`
- Data access (`src/main/java/com/bjb/uloans/repository/`):
  6x `JpaRepository` interfaces with derived queries (`findByNik`, `findByCustomer`)
- DB wiring: PostgreSQL via Docker (`uloans-pg:5433`), `ddl-auto=update`

### In Progress (DOING — Backend First)

- Sprint 1: `BiCheckingService` + `AmloService` (mock) + `UkmScoringStrategy` + `LoanApplicationService` (`@Transactional`)
- Sprint 2: JWT login (`POST /api/auth/login`, `Bearer` auth)
- Sprint 3: Thin controllers + DTO records + `GlobalExceptionHandler` + audit via events
- Sprint 4: `mvn test` green (Mockito + `@WebMvcTest`)

### Business Rules

- Mock BI: odd NIK = `HAS_DEFAULT`, even NIK = `CLEAR`, `0000000000000000` = `BLACKLIST`
- Score: +40 no `GAGAL`, +20 business age > 2 years, +20 paid-off >= 2, +20 BI `CLEAR` & AMLO `SAFE`
- Decision: >= 80 APPROVED 100%, 60-79 APPROVED 70%, < 60 REJECTED
- Conventions: `id=Long`, `money=long`, `score=int 0-100`, BCrypt passwords, single `ANALYST` role

## Tech Stack

- Backend: Java 21, Spring Boot 4.1.1, Spring Data JPA, Bean Validation, Lombok, JJWT 0.12.6
- Database: PostgreSQL
- Testing / Tools: JUnit, Mockito, Postman (no frontend needed for MVP)
- Planned Frontend (BLOCKED): Vite + React + TS + Tailwind + Axios + React Hook Form + Zod

## Architecture

Layered Monolith:

```text
Client (Postman now, React later)
  ↓
SecurityConfig + JwtAuthenticationFilter
  ↓
Controller (DTO + @Valid, thin)
  ↓
Service (@Transactional, depends on interfaces)
  ├─ BiCheckingService → MockBiCheckingService
  ├─ AmloService → MockAmloService
  └─ ScoringStrategy → UkmScoringStrategy
  ↓
Repository (JpaRepository) → PostgreSQL
```

Key rules: controllers contain no `if-else`, services own the transaction, audit writes go through events, constructor injection only.

## Getting Started

### Prerequisites

- Java 21, Maven wrapper (`./mvnw`), Docker, Postman

### 1. Start the Database

```bash
docker run --name uloans-pg -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=uloans -p 5433:5432 -d postgres
```

### 2. Configure

Copy the template and fill in local credentials (never commit the real file):

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/uloans
spring.datasource.username=postgres
spring.datasource.password=CHANGE_ME
spring.jpa.hibernate.ddl-auto=update
```

### 3. Run

```bash
.\mvnw.cmd compile
.\mvnw.cmd spring-boot:run
# API: http://localhost:8080
```

## API Endpoints

| Method | Path | Auth | Body |
|---|---|---|---|
| POST | `/api/auth/login` | No | `{username, password}` → `{token}` |
| POST | `/api/loan-applications` | Bearer | `{nik: "16 digits", requestedAmount: 10000000}` → `{decision, finalScore, reason, recommendedAmount, biStatus, amloStatus}` |
| GET | `/api/loan-applications` | Bearer | Last 10 applications (history table) |
| GET | `/api/dashboard/stats` | Bearer | `{totalToday, approved, totalLimit}` |

Error contract: `400` bad input, `401` missing token, `403` forbidden, `404` customer not found.

## Testing (Postman)

1. `POST /api/auth/login` → copy the JWT
2. `POST /api/loan-applications` with `Authorization: Bearer <token>`
   - Even NIK → APPROVED path
   - Odd NIK → lower score
   - `0000000000000000` → REJECTED
3. Verify `compliance_logs` + `audit_logs` rows are inserted

Automated: `.\mvnw.cmd test` (Sprint 4 goal: all green).

## Project Structure

```text
src/main/java/com/bjb/uloans/
├── model/        # Entities (anemic)
├── repository/   # JpaRepository interfaces
├── compliance/   # BiCheckingService, AmloService (DOING)
├── scoring/      # ScoringStrategy, UkmScoringStrategy (DOING)
├── service/      # LoanApplicationService @Transactional (DOING)
├── security/     # JWT filter + config (DOING)
├── controller/   # Thin controllers (DOING)
├── dto/ mapper/ event/ listener/ exception/
```

## Roadmap — Future Improvements

Backend:

- [ ] Replace mocks with real adapters (`OjkBiCheckingAdapter`, real AMLO provider)
- [ ] Add `KurScoringStrategy` + `ScoringStrategyFactory` (Open/Closed)
- [ ] Add `GET /api/dashboard/stats` aggregation query
- [ ] Pagination + filtering on `GET /api/loan-applications`
- [ ] Idempotency key for double-submit (two analysts, same NIK)
- [ ] Add `springdoc-openapi` + polish `GlobalExceptionHandler`

Security / Reliability:

- [ ] Add missing `spring-boot-starter-security` (currently only `security-test`)
- [ ] Refresh tokens + logout blacklist
- [ ] Rate limiting on `/api/auth/login`
- [ ] Flyway migrations (replace `ddl-auto=update`)

Testing / DevOps:

- [ ] Unit tests (Given-When-Then, no DB) + `@WebMvcTest` + Testcontainers
- [ ] `docker-compose.yml` (api + db), CI (`mvn test` on push), Render deploy

Frontend (only if time allows):

- [ ] `services/api.ts` (axios + 401 redirect), `useAuth` / `useLoanApplication` / `useDashboardStats`
- [ ] Atomic UI (`Button`, `Card`, `Input`, `Badge`) + `DashboardPage` (stats + form + result + history)

## Contributing

1. Pick one `DOING` item from the sprint board
2. Retype one file at a time, run `.\mvnw.cmd compile` per file
3. Keep controllers thin, DTOs as `record`, money as `long`
4. Run `mvn test` before opening a PR

## License

Internal learning project (BJB). No license yet — add MIT / Apache-2.0 if open-sourcing.
