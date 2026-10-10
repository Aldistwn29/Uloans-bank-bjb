# 05 - Frontend Rules - React Reusable & Maintainable Pattern

Stack: Vite + React + TS + Tailwind + Axios + React Router + React Hook Form + Zod

## Required Frontend Pattern #1: Service Adapter Pattern (api.ts)
Why: So switching baseURL from Vercel -> Render does not change every component.
File: src/services/api.ts
- Create Axios instance: apiClient = Axios.create({baseURL: import.meta.env.VITE_API_URL})
- Interceptors: attach Bearer token, handle 401 redirect.
- All components call apiClient.post('/api/loan-applications'), not manual fetch.

## Required Pattern #2: Custom Hook Pattern (Reusable Logic)
Why: Fetch logic does not belong in components, keeps them reusable & readable.
- useAuth(): { login(), logout(), token, isAuthenticated }
- useLoanApplication(): { applyLoan(), loading, result, error }
- useDashboardStats(): { stats, loading } -> fetch /api/dashboard/stats
Components stay clean: const { applyLoan, result } = useLoanApplication();

## Required Pattern #3: Atomic Reusable Component Pattern
Why: So Tailwind classes are not repeated. Build once, use everywhere.
- components/ui/: Button.tsx, Card.tsx, Input.tsx, Badge.tsx
- Example Button.tsx: type ButtonProps = { variant: 'primary'|'danger', isLoading?: boolean } -> return <button className={variantClasses[variant]}>
- All pages use <Button variant="primary">Check Score</Button>, not <button class="bg-blue-600...">

## Required Pattern #4: Container / Presentational (Clean)
- Container (Logic): DashboardPage.tsx -> calls hooks, handles submit
- Presentational (UI): StatsCard.tsx, LoanForm.tsx, ResultCard.tsx -> only receive props, no fetch
- For readability: anyone reading DashboardPage knows the flow, reading StatsCard knows only the UI.

## Required Pattern #5: DTO & Mapper in Frontend too
- types/api.ts: type LoanApplicationRequest { nik: string, requestedAmount: number }
- No any. Use TypeScript interfaces for readability.
- Mapper: function mapApiResponseToUi(data: ApiResponse): UiResult

## Maintainable Tailwind Best Practices:
- Create constants: const COLORS = { primary: 'bg-blue-600', success: 'bg-green-100' }
- Do not hardcode long classes in 10 files, use clsx / cva.
- Folders: src/components/ui/ for reusable, src/pages/ for pages.

## FORBIDDEN:
- No direct fetch in components without going through services/api.ts
- No copy-pasting long Tailwind classNames in every file, must create Button/Card components
- No NIK validation logic in components, use Zod schema in hooks/useLoanApplication.ts
