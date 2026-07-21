# PROJECT RULES: ECOMMERCE AUTOMATION (WEB)

1. **Tech Stack:**
   - Framework: Playwright (Node.js/TypeScript).
   - Test Runner: Playwright Test.
   - Pattern: BẮT BUỘC sử dụng Page Object Model (POM).

2. **Quy tắc Naming Convention:**
   - Tên file test: `[feature].spec.ts` (VD: `login.spec.ts`).
   - Tên biến/hàm: `camelCase`.
   - Tên class POM: `PascalCase` (VD: `LoginPage`).

3. **Locator Strategy (Chiến lược bắt element):**
   - Ưu tiên 1: `getByRole`, `getByText`.
   - Ưu tiên 2: `getByTestId` (data-testid).
   - CẤM: Tuyệt đối không dùng XPath tuyệt đối (như `/html/body/div[1]/...`) hoặc CSS Selector dính dáng đến class sinh tự động của Tailwind.