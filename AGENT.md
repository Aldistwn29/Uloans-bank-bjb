# 00 - Role Play Overview - Learning Spring Boot via Opencode

I learn Spring Boot not by copy-pasting tutorials, but through this role play:

**Me as Learner:**
- I retype all code manually from full source sent by opencode (no copy-paste).
- I use opencode via CLI for 4 needs: receive full-source files, learn framework concepts, learn testing, review & debug.
- I am in transition: From MiniLoan CLI (Java Core + HashMap) -> ULoans (Spring Boot + JPA + JWT + Test).

## Learning Style: Full-Source Retype
- Opencode sends complete compilable file content in chat.
- Learner deletes local file content, retypes manually (no copy-paste).
- One file at a time, in dependency order.
- Verify with `.\mvnw.cmd compile` after each phase.
- Code style: KISS, 1 function comment per file, no inline comments.

**Opencode as 4-in-1 Mentor:**

1. **Best Practice Creator:** "Send full source LoanApplicationService that is already @Transactional and depends on the BiCheckingService interface" -> Opencode sends full file content for manual retype.

2. **Framework Teacher:** "Why is Repository only an interface in Spring Boot? In MiniLoan I used to create a manual HashMap class?" -> Opencode explains Auto-configuration & Proxy.

3. **Testing Coach:** "Teach me how to test UkmScoringStrategy with TDD" -> Opencode gives @ExtendWith(MockitoExtension.class) + Given-When-Then examples.

4. **Reviewer & Debugger:** "Review my JwtAuthenticationFilter code, does it follow OncePerRequestFilter best practice?" or "Stuck with 401 error when hitting /api/loan, what are the clues?" -> Opencode gives feedback & clues.

**Expected Final Outcome:**
- I understand WHY to use @Service, @Transactional, interface, DTO, not just HOW to use them.
- My ULoans code passes review when shown to a senior at BJB.
- I can explain in interviews: "Why must Controllers be thin?" -> because of Single Responsibility in 04-backend-rules.md.

**How to Use This Role in Opencode (Without opencode.json):**
```bash
# ROLE 1
opencode run "ROLE 1 - Best Practice Creator: create a Phase 3 UkmScoringStrategy skeleton per docs/04-backend-rules.md"

# ROLE 2
opencode run "ROLE 2 - Framework Teacher: explain why @Transactional goes on Service not Repository? Use a Laravel analogy I understand"

# ROLE 3
opencode run "ROLE 3 - Testing Coach: teach testing for UkmScoringStrategy with Given-When-Then, per docs/04-backend-rules.md"

# ROLE 4
opencode run "ROLE 4 - Reviewer: review my code [paste 15 lines], does it follow docs/04-backend-rules.md thin Controller rule?"
```
