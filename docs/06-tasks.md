# ULoans Tasks - 7 Days MVP

## Phase 1 — Model (DONE 2026-10-10, BUILD SUCCESS via `.\mvnw.cmd compile`)
- [x] Customer.java <!-- DONE: src/main/java/com/bjb/uloans/model/Customer.java, table=customers, nik UNIQUE 16, id=Long, verified compile -->
- [x] LoanHistory.java <!-- DONE: src/main/java/com/bjb/uloans/model/LoanHistory.java, table=loan_histories, FK customer_id, amount=long, date=LocalDate -->
- [x] LoanApplication.java <!-- DONE: src/main/java/com/bjb/uloans/model/LoanApplication.java, table=loan_applications, decision=LoanStatus enum STRING, score=int -->
- [x] User.java <!-- DONE: src/main/java/com/bjb/uloans/model/User.java, table=users, username UNIQUE, role=ANALYST -->
- [x] LoanStatus.java <!-- DONE: src/main/java/com/bjb/uloans/model/LoanStatus.java, enum PENDING/APPROVED/REJECTED -->
- [x] ComplianceLog.java <!-- ADDED to close gap 2026-10-10: src/main/java/com/bjb/uloans/model/ComplianceLog.java, table=compliance_logs, FK application_id, needed by Phase 2 ComplianceLogRepository -->
- [x] AuditLog.java <!-- ADDED to close gap 2026-10-10: src/main/java/com/bjb/uloans/model/AuditLog.java, table=audit_logs, fields username/action/entityId/timestamp, needed by Phase 2 AuditLogRepository -->

## Phase 2 — Data Access (NOT STARTED as of 2026-10-10, folder src/main/java/com/bjb/uloans/repository/ empty)
- [ ] CustomerRepository.java <!-- TODO: expected src/main/java/com/bjb/uloans/repository/CustomerRepository.java, interface JpaRepository<Customer,Long> + findByNik -->
- [ ] LoanHistoryRepository.java <!-- TODO: expected src/main/java/com/bjb/uloans/repository/LoanHistoryRepository.java, interface JpaRepository<LoanHistory,Long> -->
- [ ] LoanApplicationRepository.java <!-- TODO: expected src/main/java/com/bjb/uloans/repository/LoanApplicationRepository.java, interface JpaRepository<LoanApplication,Long> -->
- [ ] UserRepository.java <!-- TODO: expected src/main/java/com/bjb/uloans/repository/UserRepository.java, interface JpaRepository<User,Long> + findByUsername -->
- [ ] ComplianceLogRepository.java <!-- TODO: expected src/main/java/com/bjb/uloans/repository/ComplianceLogRepository.java, requires model/ComplianceLog.java DONE -->
- [ ] AuditLogRepository.java <!-- TODO: expected src/main/java/com/bjb/uloans/repository/AuditLogRepository.java, requires model/AuditLog.java DONE -->

## Phase 3 — Compliance & Scoring (Core) (NOT STARTED, folders compliance/ scoring/ service/ empty)
- [ ] BiCheckingService.java (interface) <!-- TODO: expected src/main/java/com/bjb/uloans/compliance/BiCheckingService.java, method BiStatus check(String nik) -->
- [ ] MockBiCheckingService.java <!-- TODO: expected src/main/java/com/bjb/uloans/compliance/MockBiCheckingService.java, rule odd=HAS_DEFAULT even=CLEAR 000...=BLACKLIST -->
- [ ] AmloService.java (interface) <!-- TODO: expected src/main/java/com/bjb/uloans/compliance/AmloService.java -->
- [ ] MockAmloService.java <!-- TODO: expected src/main/java/com/bjb/uloans/compliance/MockAmloService.java -->
- [ ] ScoringStrategy.java (interface) <!-- TODO: expected src/main/java/com/bjb/uloans/scoring/ScoringStrategy.java, method int calculate(Customer, List<LoanHistory>) -->
- [ ] UkmScoringStrategy.java <!-- TODO: expected src/main/java/com/bjb/uloans/scoring/UkmScoringStrategy.java, rule +40/+20/+20/+20 -->
- [ ] LoanApplicationService.java (@Transactional) <!-- TODO: expected src/main/java/com/bjb/uloans/service/LoanApplicationService.java, facade apply() 7 steps per docs/04-backend-rules.md -->

## Phase 4 — Security JWT (Learning Goal) (NOT STARTED, folder security/ empty, BLOCKED: pom.xml missing spring-boot-starter-security as of 2026-10-10)
- [ ] SecurityConfig.java <!-- TODO: expected src/main/java/com/bjb/uloans/security/SecurityConfig.java, BLOCKED needs starter-security dep -->
- [ ] JwtTokenProvider.java <!-- TODO: expected src/main/java/com/bjb/uloans/security/JwtTokenProvider.java, uses jjwt 0.12.6 already in pom -->
- [ ] JwtAuthenticationFilter.java <!-- TODO: expected src/main/java/com/bjb/uloans/security/JwtAuthenticationFilter.java, extends OncePerRequestFilter -->
- [ ] CustomUserDetailsService.java <!-- TODO: expected src/main/java/com/bjb/uloans/security/CustomUserDetailsService.java, uses UserRepository NOT YET EXIST -->
- [ ] AuthController.java <!-- TODO: expected src/main/java/com/bjb/uloans/controller/AuthController.java, POST /api/auth/login -->

## Phase 5 — Controller + HRD UI (NOT STARTED, folder controller/ dto/ mapper/ empty, UloansApplication.java still hello-world)
- [ ] CustomerController.java <!-- TODO: expected src/main/java/com/bjb/uloans/controller/CustomerController.java, thin max 30 lines -->
- [ ] LoanApplicationController.java <!-- TODO: expected src/main/java/com/bjb/uloans/controller/LoanApplicationController.java, POST /api/loan-applications calls service.apply() only -->
- [ ] DashboardController.java (@GetMapping /dashboard) <!-- TODO: expected src/main/java/com/bjb/uloans/controller/DashboardController.java -->
- [ ] templates/dashboard.html (Bootstrap 5 CDN) <!-- TODO: expected src/main/resources/templates/dashboard.html, CONFLICT: docs/01-overview.md says React decoupled, decide React vs Thymeleaf before start -->

## Phase 6 — Testing & Docs (NOT STARTED)
- [ ] UkmScoringStrategyTest.java <!-- TODO: expected src/test/java/com/bjb/uloans/scoring/UkmScoringStrategyTest.java, Given-When-Then, no DB -->
- [ ] LoanApplicationServiceTest.java (Mockito) <!-- TODO: expected src/test/java/com/bjb/uloans/service/LoanApplicationServiceTest.java -->
- [ ] LoanApplicationControllerTest.java (@WebMvcTest) <!-- TODO: expected src/test/java/com/bjb/uloans/controller/LoanApplicationControllerTest.java -->
- [ ] GlobalExceptionHandler.java <!-- TODO: expected src/main/java/com/bjb/uloans/exception/GlobalExceptionHandler.java, returns 400/401/404 consistently -->
- [ ] Swagger Config <!-- TODO: expected src/main/java/com/bjb/uloans/config/SwaggerConfig.java, NOT in pom yet, add springdoc later -->

## Final Checklist (ALL NOT DONE as of 2026-10-10)
- [ ] mvn test -> all green <!-- STATUS: not run green yet, only mvn compile SUCCESS, test needs datasource -->
- [ ] mvn spring-boot:run -> /dashboard is accessible <!-- BLOCKED: application.properties only 1 line, no datasource url -->
- [ ] Create customer via API <!-- BLOCKED: needs Phase 2+5 -->
- [ ] Apply loan -> green APPROVED / red REJECTED <!-- BLOCKED: needs Phase 3+5 -->
- [ ] Check compliance_logs & audit_logs are inserted <!-- BLOCKED: needs Phase 3 event + Phase 2 repos -->
- [ ] Record 1 min video demo for LinkedIn <!-- TODO last -->

## Notes
- Source of truth = disk files + `mvn compile` output, not checklist claims. Only mark [x] if file exists + compile green + date + command recorded.
- Learning style: full-source retype. Agent sends full file, learner types manually (no copy-paste).
- Phase 1 done via full-source (7 models, BUILD SUCCESS 2026-10-10 via `.\mvnw.cmd compile`). Next: Phase 2.
- Known gaps 2026-10-10: pom.xml has spring-security-test but missing spring-boot-starter-security; application.properties only spring.application.name, no datasource/jwt.secret; UloansApplication.java still hello-world @RestController.
- Conventions: id=Long, nik=String 16 digits UNIQUE, money=long, score=int 0-100
- Mock Rule: odd NIK = HAS_DEFAULT, even = CLEAR, "0000000000000000" = BLACKLIST
- Scoring: +40 no GAGAL, +20 business>2yrs, +20 paid-off>=2, +20 BI CLEAR & AMLO SAFE
- Decision: score>=80 APPROVED 100%, 60-79 APPROVED 70%, <60 REJECTED
- Security: 1 role only ANALYST, password BCrypt
- How to run: mvn spring-boot:run -> http://localhost:8080/dashboard
