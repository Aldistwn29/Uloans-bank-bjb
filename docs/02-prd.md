# 02 - PRD (Product Requirement) - Decoupled React Version

User: BJB KCP Credit Analyst
Goal: React dashboard to input NIK + Amount -> Hit Spring Boot API -> Display APPROVED/REJECTED.

Business Flow (Same, but different Frontend):
1. Analyst opens https://uloans.vercel.app -> React Login page (POST /api/auth/login)
2. Backend returns JWT -> Frontend stores it in localStorage
3. React Dashboard (3 Stats Cards from GET /api/dashboard/stats + Form + Result Card)
4. Input NIK + Amount -> Frontend POST /api/loan-applications with header Authorization: Bearer <token>
5. Backend processes: BiChecking (mock) + Amlo (mock) + UkmScoringStrategy (same logic as old PRD)
6. Backend returns {decision, finalScore, reason, recommendedAmount, biStatus, amloStatus}
7. Frontend displays green APPROVED / red REJECTED Card + saves audit log

Frontend Requirements (React + Tailwind + Vercel):
- Page 1: /login - username/password form, Tailwind styled, calls /api/auth/login
- Page 2: /dashboard - Protected route (checks token). Contains:
    - Header + Logout button
    - 3 Stats Cards (Total Today, Approved, Total Limit) - fetched from /api/dashboard/stats
    - Apply Loan Form (16-digit NIK + Amount + Check Button) - frontend validation with Zod/React Hook Form
    - Result Card (appears after apply, green/red color + biStatus, amloStatus, finalScore, reason details)
    - History Table (list of last 10 applications from GET /api/loan-applications)
- Styling: TailwindCSS, responsive, clean banking look (like BJB dashboard)
- Deploy: Vercel, env VITE_API_URL = https://uloans-api.onrender.com
- Error handling: On 401 -> redirect to /login

Non-Goals: KTP upload, PDF, Next.js SSR, multi roles (ANALYST only).
