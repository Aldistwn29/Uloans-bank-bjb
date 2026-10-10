# 03 - Architecture

Style: Layered Monolith

Client (Thymeleaf dashboard.html + JS fetch Bearer Token)
  ↓
SecurityConfig + JwtAuthenticationFilter (OncePerRequestFilter)
  ↓
Controller (DTO + @Valid + ResponseEntity)
  ↓
Service (@Transactional, depends on Interfaces)
  ├─ BiCheckingService (interface) -> MockBiCheckingService
  ├─ AmloService (interface) -> MockAmloService
  └─ ScoringStrategy (interface) -> UkmScoringStrategy
  ↓
Repository (JpaRepository, interface only)
  ↓
Entity (Anemic) -> PostgreSQL

Tables:
- users(id, username UNIQUE, password BCrypt, role)
- customers(id, nik UNIQUE 16 digits, full_name, business_age, omzet)
- loan_histories(id, customer_id FK, amount, status, date)
- loan_applications(id, customer_id FK, requested_amount, bi_status, amlo_status, final_score, decision, recommended_amount, created_by, created_at)
- compliance_logs(id, application_id FK, nik, bi_status, amlo_status, checked_by, checked_at)
- audit_logs(id, username, action, entity_id, timestamp)
