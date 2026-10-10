# 03 - Backend Rules ULoans (First-Principle)

Goal: Reusable, maintainable, team-readable code.

## 0. First Principle

Backend = accept untrusted input from network, mutate durable state by rules, return reliable result.

Four realities drive all rules below:

A. Network and client are untrusted: HTTP is stateless, retryable, forgeable.
B. Data outlives code: schema is a contract, code changes weekly, customer data lives for years.
C. Requests run concurrently: two analysts can submit the same NIK at the same time.
D. Everything can partially fail: DB saves, external call times out, log write fails.

Six questions for every endpoint:
1. Who? (auth)
2. What state? (entity)
3. Valid? (reject bad data early)
4. Allowed? (authorization)
5. Half-failure? (what rolls back)
6. Traceable? (who, which NIK, which step failed)

Reusable = high cohesion + low coupling:
- Cohesion: one file, one reason to change.
- Coupling: depend on contract (interface), not concrete class.

## 1. Controller Is a Cashier [A]

- Accept DTO + `@Valid`, call one service method, wrap with `ResponseEntity`.
- Max 15-30 lines. No business `if-else`, no direct repository call, no state in fields.
- Example: `POST /api/loan-applications` only calls `loanApplicationService.apply(request)`.

## 2. DTO Is Not Entity [A + B]

- Entity = table shape: id, FK, timestamps.
- DTO = external contract: `LoanApplicationRequest(nik, requestedAmount)`, `LoanApplicationResponse(decision, finalScore, reason, recommendedAmount, biStatus, amloStatus)`.
- Use Java `record` for DTOs. Never return Entity from Controller.
- Map explicitly with `LoanApplicationMapper.toResponse(entity)`. No `BeanUtils.copyProperties`.

## 3. Validate at the Door [A]

- Check in DTO, not inside scoring logic.
- NIK: 16 digits, `String`, `UNIQUE`. Amount: `long`, `> 0`. Score: `int`, `0-100`.
- Bad input returns `400` before touching Service.
- Laravel analogy: `FormRequest` = DTO + Bean Validation annotations.

## 4. Service Is the Orchestrator + Transaction [B + D]

- `LoanApplicationService.apply()` order:
  1. `customerRepo.findByNik()`
  2. `biCheckingService.check()`
  3. `amloService.check()`
  4. `scoringStrategy.calculate()`
  5. `loanApplicationRepo.save()`
  6. `complianceLogRepo.save()`
  7. `publishEvent(new LoanCreatedEvent(...))`
- Annotate with `@Transactional`: one failure rolls back all writes.
- Money = `long`, never `float`. Laravel analogy: `DB::transaction()` = `@Transactional`.

## 5. Repository Is Only Storage [B]

- Interface only: `interface CustomerRepository extends JpaRepository<Customer, Long>`.
- Spring generates the implementation at runtime via Proxy.
- Use derived queries: `findByNik(String nik)`. No manual JDBC, no business formula here.

## 6. Depend on Contract, Inject via Constructor [D]

- Service depends on `BiCheckingService`, not `MockBiCheckingService`.
- Constructor injection only (`RequiredArgsConstructor`). No field `@Autowired`.
- Reason: easy to mock in tests, easy to swap mock with real OJK adapter later.

## 7. Wrap What Changes

- Strategy: many formulas for one job. `ScoringStrategy { int calculate(Customer, List<LoanHistory>) }`. Implementations: `UkmScoringStrategy`, `KurScoringStrategy`. Service only calls `calculate()`.
- Factory: one place to choose strategy. `ScoringStrategyFactory.getStrategy(loanType)`. No scattered `if-else`.
- Adapter: outside world has incompatible shape. `MockBiCheckingService` today, `OjkBiCheckingAdapter` tomorrow. Same interface.
- Builder: object with 7+ fields. Use Lombok `@Builder`: `LoanApplication.builder().customer(...).requestedAmount(...).finalScore(...).decision(...).build()`. Skip for 2-3 fields.
- Rule: add new type = add one new file, do not modify old Service (Open/Closed).

ULoans fixed rules:
- Mock: odd NIK = `HAS_DEFAULT`, even NIK = `CLEAR`, `0000000000000000` = `BLACKLIST`.
- Scoring: +40 no `GAGAL`, +20 business age > 2 years, +20 paid-off >= 2, +20 BI `CLEAR` and AMLO `SAFE`.
- Decision: score >= 80 APPROVED 100%, 60-79 APPROVED 70%, < 60 REJECTED.

## 8. Side Effects via Event, Not Mixed Into Core [D]

- Core finishes `save()`, then `applicationEventPublisher.publishEvent(new LoanCreatedEvent(savedApp))`.
- Separate listener: `@Component AuditLogListener { @EventListener ... auditRepo.save() }`.
- Add email/WhatsApp later = add new listener, core untouched.
- Laravel analogy: `Event::dispatch()` + Listener.

## 9. No Request State in Fields [C]

- Spring beans are singleton shared by thousands of requests.
- Forbidden: `private String nik` field in Service/Controller.
- All request data passes via method parameters.
- Same as Laravel container services: stateless service, stateful request.

## 10. Consistent Error Contract + Traceability [A + D]

- `200` + body on success, `400` + failing field on bad input, `401` unauthenticated, `403` forbidden, `404` missing customer.
- One `GlobalExceptionHandler`.
- Log minimum: username, NIK, step where it failed.
- Security: single role `ANALYST`, password BCrypt, JWT `Bearer` per request.

## FORBIDDEN

- No business logic `if-else` in Controller.
- No field injection `@Autowired`.
- No Entity returned from Controller.
- No manual JDBC query.
- No `auditRepo.save()` inside core business flow.

## Naming

- Interface: `BiCheckingService`, `AmloService`, `ScoringStrategy`
- Implementation: `MockBiCheckingService`, `MockAmloService`, `UkmScoringStrategy`
- Factory: `ScoringStrategyFactory`
- Mapper: `LoanApplicationMapper`
- Facade: `LoanApplicationService`
- DTO: `LoanApplicationRequest` (record), `LoanApplicationResponse` (record)
- Event: `LoanCreatedEvent`, listener `AuditLogListener`

## PR Checklist (Junior Team-Ready)

- [ ] Controller thin?
- [ ] DTO returned, not Entity?
- [ ] Constructor injection?
- [ ] `@Transactional` on Service?
- [ ] New feature = new file, not modified old core?
- [ ] `mvn test` green?
