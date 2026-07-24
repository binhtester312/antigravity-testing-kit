---
name: qa_automation_engineer_update
description: "Bổ sung lessons learned cho skill qa_automation_engineer — kinh nghiệm thực tế từ: (1) session convert 21 TC Selenium Java sang Playwright TypeScript, (2) tích hợp Allure Report + GitHub Actions CI/CD. Đọc khi: scaffold Playwright TS framework mới, tích hợp Allure, setup GitHub Actions CI, debug Variables vs Secrets."
---

# QA Automation Engineer — UPDATE (Lessons Learned)

> **File này bổ sung cho `qa_automation_engineer/SKILL.md` (giữ nguyên bản gốc)**
> Ngày cập nhật: 2026-07-24 | Session 1: Convert 21 TC Selenium Java → Playwright TypeScript
> Session 2: Tích hợp Allure Report + GitHub Actions CI/CD

---

## 🎯 Khi Nào Đọc File Này

Đọc file này KHI:
- Scaffold Playwright TypeScript framework mới từ đầu
- Convert test cases từ Selenium Java sang Playwright TypeScript
- Gặp TypeScript strict mode compilation errors
- Config multi-browser (Chrome + Firefox) trên macOS
- Debug auth setup / storageState issues
- HTML report port conflict

---

## 📋 Tóm Tắt Lessons Learned

### 1. Chrome Thật vs Chromium Engine

```typescript
// BẮT BUỘC thêm channel: 'chrome' để dùng Google Chrome thật
use: { ...devices['Desktop Chrome'], channel: 'chrome' }
```

Chromium engine (default) có thể không tương thích với một số CRM/web app thật.

### 2. TypeScript TS4094 — Export Logger

```typescript
// ❌ Lỗi: class Logger không được export
class Logger { }

// ✅ Đúng: export để dùng làm type annotation
export class Logger { }
```

### 3. Protected Field — Khai báo + Khởi tạo đúng chỗ

```typescript
// ✅ Pattern đúng cho BasePage strict TypeScript
import { createLogger, type Logger } from '../utils/logger';

export abstract class BasePage {
  protected readonly page: Page;
  protected readonly logger: Logger;  // type annotation

  constructor(page: Page) {
    this.page = page;
    this.logger = createLogger(this.constructor.name);  // khởi tạo trong constructor
  }
}
```

### 4. Auth Setup — Mkdir Recursive

```typescript
// Trong auth.setup.ts — luôn tạo thư mục trước
const dir = path.dirname('playwright/.auth/user.json');
if (!fs.existsSync(dir)) {
  fs.mkdirSync(dir, { recursive: true });
}
```

### 5. Playwright 1.47 — Workers Type

```typescript
workers: process.env['CI'] ? 4 : 1,   // 'auto' không được hỗ trợ
```

### 6. Relative Path — Đếm Cấp Thư Mục

```
src/tests/auth/login.spec.ts → '../../../test-data/users.json'  (3 cấp)
src/tests/login.spec.ts      → '../../test-data/users.json'    (2 cấp)
```

---

## 🔢 Thứ Tự Chạy Lệnh — Framework Mới

```bash
# Bắt buộc theo thứ tự này:
npm install
npx playwright install chromium firefox
npm run type-check               # → phải 0 errors
npx playwright test --list       # → đếm số tests được nhận diện
npx playwright test --headed     # → chạy và verify pass
```

---

## 🗺️ Mapping Selenium Java → Playwright TypeScript

| Java | TypeScript |
|---|---|
| `@FindBy(id="x")` | `page.locator('#x')` hoặc `page.getByLabel()` |
| `WebDriverWait(10).until(...)` | Auto-waiting (không cần viết) |
| `driver.findElement().click()` | `await page.locator().click()` |
| `Thread.sleep(2000)` | Xóa bỏ — Playwright tự wait |
| `@BeforeMethod login()` | `auth.setup.ts` + storageState |
| `Assert.assertEquals(a,b)` | `await expect(locator).toHaveText(b)` |
| `@DataProvider` | `test.each` hoặc loop trong test |
| `System.out.println()` | `console.log()` — xóa trước khi deliver |

---

## 🚀 Lệnh Chạy Test — Tham Chiếu Nhanh

```bash
npx playwright test                              # Toàn suite (headless)
npx playwright test --headed                     # Có UI
npx playwright test --project=chrome             # Chỉ Chrome
npx playwright test --project=firefox            # Chỉ Firefox
npx playwright test src/tests/auth/login.spec.ts # 1 file
npx playwright test --grep @smoke                # Theo tag
npx playwright show-report                       # HTML report
npx playwright show-report --port 9324           # Nếu port 9323 bận
open playwright-report/index.html                # Mở file trực tiếp
```

---

## 📁 Cấu Trúc Thư Mục Đã Verified (43 tests PASS)

```
playwright-typescript-framework/
├── playwright.config.ts    ← channel:'chrome' + firefox + auth setup
├── src/
│   ├── fixtures/
│   │   └── auth.setup.ts   ← mkdir recursive + storageState
│   ├── pages/
│   │   ├── base.page.ts    ← abstract + export Logger type
│   │   ├── login.page.ts
│   │   ├── dashboard.page.ts
│   │   └── forgot-password.page.ts
│   ├── tests/auth/
│   │   └── login.spec.ts   ← 21 TCs, 43 runs (Chrome + Firefox)
│   └── utils/
│       ├── logger.ts       ← export class Logger (bắt buộc!)
│       └── data-generator.ts
├── test-data/users.json
└── playwright/.auth/       ← KHÔNG commit (.gitignore)
```

---

## 📦 SESSION 2: Allure Report + GitHub Actions CI/CD

### 7. Allure Report — Tích Hợp Chuẩn

**Cài đặt:**
```bash
npm install --save-dev allure-playwright allure-commandline
npm install -g allure-commandline    # ← CLI global để dùng lệnh allure
```

**Config bắt buộc trong `playwright.config.ts`:**
```typescript
['allure-playwright', {
  detail: false,    // ← PHẢI là false — tránh log spam Playwright API calls
  outputFolder: 'allure-results',
  suiteTitle: true,
  environmentInfo: { Framework: 'Playwright Test', Language: 'TypeScript' }
}]
```

### 8. Allure — Flat Steps (KHÔNG dùng wrapper Arrange/Act/Assert)

```typescript
// ✅ Gọi thẳng page methods trong test body
test('TC_001', async ({ loginPage, dashboardPage }) => {
  await loginPage.loginWithEmail(email, password);     // parent step
  await dashboardPage.verifyPageLoaded();              // parent step
});
// Allure tự hiển thị hierarchical: page method → base action
```

### 9. GitHub Actions — 3 Quy Tắc Quan Trọng

**Quy tắc 1: Workflow file PHẢI ở root repo**
```
✅ <repo-root>/.github/workflows/playwright.yml
❌ playwright-typescript-framework/.github/workflows/playwright.yml  → GitHub bỏ qua
```

**Quy tắc 2: Sub-directory → dùng `defaults.run.working-directory`**
```yaml
defaults:
  run:
    working-directory: playwright-typescript-framework
```

**Quy tắc 3: Artifact path từ workspace root, không phải working-directory**
```yaml
path: playwright-typescript-framework/allure-results   # ✅
path: allure-results                                    # ❌
```

### 10. GitHub Actions — Variables vs Secrets

| Tạo ở | Cú pháp | Dùng khi |
|-------|---------|----------|
| Tab **Variables** | `${{ vars.BASE_URL }}` | URL, username — không nhạy cảm |
| Tab **Secrets** | `${{ secrets.MY_TOKEN }}` | Password, API key nhạy cảm |

Nhầm lẫn 2 loại → biến ra **empty string** trong CI → test fail do credentials trống.

### 11. GitHub Pages — Bật Thủ Công 1 Lần

```
Settings → Pages → Source: Deploy from a branch
Branch: gh-pages → Folder: / (root) → Save
```

Report online tại: `https://<username>.github.io/<repo-name>/`
