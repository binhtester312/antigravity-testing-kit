# Quy Tắc Playwright — UPDATE (Bài Học Kinh Nghiệm Thực Tế)

> **File này bổ sung cho `playwright_rules.md` (giữ nguyên bản gốc)**
> Ghi nhận những vấn đề thực tế gặp phải khi triển khai Playwright TypeScript framework.
> Ngày cập nhật: 2026-07-24 | Session 1: Chuyển 21 TC Selenium Java → Playwright TypeScript
> Session 2: Tích hợp Allure Report + GitHub Actions CI/CD

---

## L1. Cấu Hình Browser — Firefox & Chrome Thật (QUAN TRỌNG)

### Vấn đề: `chromium` engine ≠ Google Chrome thật

Khi chạy trên macOS local, `devices['Desktop Chrome']` mặc định dùng chromium engine (built-in).
Nhiều web app CRM yêu cầu Chrome thật để render đúng → cần thêm `channel: 'chrome'`.

```typescript
// ✅ playwright.config.ts — Dùng Chrome thật + Firefox
projects: [
  {
    name: 'setup',
    testMatch: '**/auth.setup.ts',
  },
  {
    name: 'chrome',
    use: {
      ...devices['Desktop Chrome'],
      channel: 'chrome',                    // ← BẮT BUỘC để dùng Google Chrome thật
      viewport: { width: 1920, height: 1080 },
      storageState: 'playwright/.auth/user.json',
    },
    dependencies: ['setup'],
  },
  {
    name: 'firefox',
    use: {
      ...devices['Desktop Firefox'],
      viewport: { width: 1920, height: 1080 },
      storageState: 'playwright/.auth/user.json',
    },
    dependencies: ['setup'],
  },
],
```

**Lệnh cài browsers trước khi chạy lần đầu:**
```bash
npx playwright install chromium firefox
# Hoặc cài tất cả kèm deps:
npx playwright install --with-deps
```

---

## L2. TypeScript Strict Mode — 3 Lỗi Phổ Biến

### Lỗi 1: TS4094 — Logger class không export

```typescript
// ❌ Gây TS4094: private name không dùng được làm type
class Logger { ... }

// ✅ Phải export để dùng làm type annotation
export class Logger { ... }
```

### Lỗi 2: Protected field phải khai báo type + khởi tạo trong constructor

```typescript
// ❌ Lỗi khi strict mode
export abstract class BasePage {
  protected readonly logger = createLogger(this.constructor.name);
}

// ✅ Đúng — type annotation rõ ràng + khởi tạo trong constructor
import { createLogger, type Logger } from '../utils/logger';

export abstract class BasePage {
  protected readonly page: Page;
  protected readonly logger: Logger;

  constructor(page: Page) {
    this.page = page;
    this.logger = createLogger(this.constructor.name);
  }
}
```

### Lỗi 3: `workers: 'auto'` không hỗ trợ trong Playwright 1.47

```typescript
// ❌ Playwright 1.47 không hỗ trợ 'auto'
workers: process.env['CI'] ? 4 : 'auto',

// ✅ Dùng số nguyên
workers: process.env['CI'] ? 4 : 1,
```

---

## L3. Relative Path — Tính Số Cấp `../`

Test file `src/tests/auth/login.spec.ts` → cần 3 cấp để về root:

```
src/         (cấp 1 - ../../../)
└── tests/   (cấp 2 - ../../)
    └── auth/ (cấp 3 - ../)
        └── login.spec.ts
```

```typescript
// ❌ Sai — quá nhiều cấp
require('../../../../test-data/users.json')

// ✅ Đúng
require('../../../test-data/users.json')
```

---

## L4. Auth Setup — Tạo Thư Mục Tự Động

`playwright/.auth/user.json` PHẢI tồn tại trước khi test browsers chạy.

```typescript
// auth.setup.ts — bắt buộc tạo thư mục
import * as fs from 'fs';
import * as path from 'path';

const authFile = 'playwright/.auth/user.json';

setup('authenticate', async ({ page }) => {
  const dir = path.dirname(authFile);
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir, { recursive: true });  // ← tạo cả playwright/ và .auth/
  }
  // ... login steps ...
  await page.context().storageState({ path: authFile });
});
```

**`.gitignore` bắt buộc có:**
```
playwright/.auth/
```

---

## L5. HTML Report — Port Conflict

```bash
# Lỗi: EADDRINUSE :::9323
# Fix — kill port cũ:
kill -9 $(lsof -ti:9323)

# Hoặc dùng port khác:
npx playwright show-report --port 9324

# Hoặc mở trực tiếp file (không cần server):
open playwright-report/index.html
```

---

## L6. Lệnh Chạy Test — Tham Chiếu Nhanh

```bash
# Chạy tất cả (headless)
npx playwright test

# Chạy có UI để debug
npx playwright test --headed

# Chỉ Chrome hoặc Firefox
npx playwright test --project=chrome
npx playwright test --project=firefox

# 1 file cụ thể
npx playwright test src/tests/auth/login.spec.ts

# Theo tag
npx playwright test --grep @smoke
npx playwright test --grep @auth

# Xem report
npx playwright show-report
npx playwright show-report --port 9324    # nếu port 9323 bận
```

---

## L7. Checklist Framework Mới — Trước Khi Chạy Suite

```bash
# 1. Cài dependencies
npm install

# 2. Cài Playwright browsers
npx playwright install chromium firefox

# 3. TypeScript type-check — phải 0 errors
npm run type-check

# 4. Đếm tests được nhận diện
npx playwright test --list

# 5. Chạy thử 1 file nhỏ trước
npx playwright test src/tests/auth/login.spec.ts --headed
```

Chỉ chạy toàn suite sau khi checklist xanh hết.

---

## L8. Git Push (Không git pull)

```bash
# Chỉ add + commit + push — KHÔNG pull (theo quy tắc Git Pull Restriction)
git add .
git commit -m "feat: playwright-typescript-framework with 21 test cases"
git push origin main

# Nếu bị reject do remote có code mới hơn:
git push --force-with-lease origin main
```

---

## L9. Cấu Trúc Thư Mục Chuẩn (Đã Verified — 43 tests PASS)

```
playwright-typescript-framework/
├── playwright.config.ts          ← channel:'chrome' + auth setup + 2 browsers
├── package.json
├── tsconfig.json
├── .env                          ← KHÔNG commit (trong .gitignore)
├── .env.example                  ← Commit để team tham khảo
├── .gitignore                    ← Bao gồm: playwright/.auth/
│
├── src/
│   ├── fixtures/
│   │   └── auth.setup.ts         ← Tạo playwright/.auth/user.json
│   ├── pages/
│   │   ├── base.page.ts          ← abstract + export Logger type
│   │   ├── login.page.ts
│   │   ├── dashboard.page.ts
│   │   └── forgot-password.page.ts
│   ├── tests/
│   │   └── auth/
│   │       └── login.spec.ts     ← 21 test cases converted từ Selenium Java
│   └── utils/
│       ├── logger.ts             ← export class Logger (bắt buộc!)
│       └── data-generator.ts
│
├── test-data/
│   └── users.json
│
└── playwright/
    └── .auth/
        └── user.json             ← Auto-generated, KHÔNG commit
```

---

## L10. Allure Report — Tích Hợp Chuẩn (SESSION 2)

### Cài đặt

```bash
npm install --save-dev allure-playwright allure-commandline
```

### playwright.config.ts — reporter block

```typescript
reporter: [
  ['allure-playwright', {
    detail: false,          // ← QUAN TRỌNG: tắt auto-log Playwright API calls
    outputFolder: 'allure-results',
    suiteTitle: true,
    environmentInfo: {
      Framework: 'Playwright Test',
      Language: 'TypeScript',
      Browser: 'Chromium',
      Environment: process.env['ENV'] ?? 'staging',
      BaseURL: process.env['BASE_URL'] ?? 'https://crm.anhtester.com',
    },
  }],
  ['html', { outputFolder: 'playwright-report', open: 'never' }],
  ['list'],
  ['json', { outputFile: 'test-results/results.json' }],
],
```

### ⚠️ `detail: false` là bắt buộc

Nếu để `detail: true` (mặc định), Allure sẽ tự động log TẤT CẢ lệnh Playwright API nhỏ lẻ (`locator.click()`, `expect.toBeVisible()`, v.v.) → report rất dài và khó đọc.

---

## L11. Allure — Flat Step Hierarchy (KHÔNG dùng Arrange/Act/Assert)

### ❌ Sai — quá nhiều lớp wrapper rườm rà

```typescript
test('TC_001', async ({ loginPage, dashboardPage }) => {
  await test.step('[Arrange] Prepare credentials', async () => {});
  await test.step('[Act] Submit login', async () => {
    await loginPage.loginWithEmail('admin@example.com', '123456');
  });
  await test.step('[Assert] Verify Dashboard', async () => {
    await dashboardPage.verifyPageLoaded();
  });
});
```

### ✅ Đúng — gọi thẳng page methods

```typescript
test('TC_001', async ({ loginPage, dashboardPage }) => {
  await loginPage.loginWithEmail('admin@example.com', '123456');
  await dashboardPage.verifyPageLoaded();
});
```

Kết quả trong Allure Test Body:
```
├─ Login to CRM with email: "admin@example.com"       ← parent step từ loginPage
│    ├─ Enter email into #email                        ← child step từ basePage
│    ├─ Enter password: "••••••••"                     ← child step (masked)
│    └─ Click Login button                             ← child step
└─ Verify Dashboard page loaded successfully           ← parent step từ dashboardPage
```

---

## L12. Allure — Tự Động Che Mật Khẩu (Password Masking)

Trong `base.page.ts`, khi dùng `allure.step()` cho action fill:

```typescript
// Phát hiện field password bằng inputType
const inputType = await locator.getAttribute('type');
const isPassword = inputType === 'password';
const displayValue = isPassword ? '•'.repeat(8) : value;

await allure.step(`Enter ${label}: "${displayValue}" into input "${selectorStr}"`, async () => {
  await locator.fill(value);
});
```

---

## L13. Allure — Auto Screenshot Khi Test PASS

Trong `base.fixture.ts`, dùng `test.afterEach` để đính kèm screenshot sau khi test PASS:

```typescript
test.afterEach(async ({ page }, testInfo) => {
  if (testInfo.status === 'passed') {
    const screenshot = await page.screenshot({ fullPage: false });
    await allure.attachment('final-screenshot-passed', screenshot, {
      contentType: 'image/png',
    });
  }
});
```

---

## L14. Allure — Lệnh CLI Local

```bash
# Cài Allure CLI global (chạy 1 lần)
npm install -g allure-commandline

# Generate HTML report
allure generate allure-results --clean

# Mở report vừa generate
allure open allure-report

# Hoặc serve trực tiếp (không cần generate trước)
allure serve allure-results
```

### package.json scripts tiêu chuẩn

```json
"allure:clean":    "rimraf allure-results allure-report",
"allure:generate": "allure generate allure-results -o allure-report --clean",
"allure:open":     "allure open allure-report",
"allure:serve":    "allure serve allure-results",
"test:allure":     "playwright test && npm run allure:generate && npm run allure:open"
```

---

## L15. GitHub Actions — Vị Trí File Workflow (QUAN TRỌNG)

GitHub Actions **chỉ** nhận diện workflow ở thư mục gốc của repo:
```
<repo-root>/.github/workflows/*.yml   ← ĐÂY mới đúng
```

**Không được** đặt trong thư mục con:
```
playwright-typescript-framework/.github/workflows/playwright.yml  ← SAI, GitHub bỏ qua
```

### Khi project nằm trong thư mục con, dùng `working-directory`:

```yaml
jobs:
  test:
    runs-on: ubuntu-latest
    defaults:
      run:
        working-directory: playwright-typescript-framework   # ← set cho toàn job
    steps:
      - run: npm ci
      - run: npx playwright test --project=chromium
```

---

## L16. GitHub Actions — Variables vs Secrets

GitHub có 2 loại lưu trữ giá trị:

| Loại | Cú pháp trong workflow | Dùng khi |
|------|------------------------|----------|
| **Repository Secrets** | `${{ secrets.MY_SECRET }}` | Mật khẩu, token, API key nhạy cảm |
| **Repository Variables** | `${{ vars.MY_VAR }}` | Giá trị không nhạy cảm (URL, username) |

Bạn dùng loại nào thì phải viết đúng cú pháp tương ứng.

```yaml
# ✅ Đúng — dùng vars.XXX khi user đã tạo Repository Variables
- name: Create .env
  run: |
    echo "BASE_URL=${{ vars.BASE_URL }}" >> .env
    echo "DEFAULT_USERNAME=${{ vars.DEFAULT_USERNAME }}" >> .env
    echo "DEFAULT_PASSWORD=${{ vars.DEFAULT_PASSWORD }}" >> .env

# ❌ Sai — dùng secrets.XXX nhưng user chỉ tạo Variables (không phải Secrets)
    echo "BASE_URL=${{ secrets.BASE_URL }}" >> .env
```

**Cách kiểm tra**: Vào `Settings → Secrets and variables → Actions`:
- Tab **Secrets** → dùng `secrets.XXX`
- Tab **Variables** → dùng `vars.XXX`

---

## L17. GitHub Pages — Bật Thủ Công Lần Đầu

Deploy Allure Report lên GitHub Pages cần bật thủ công 1 lần trong Settings:

```
Settings → Pages → Build and deployment
  Source: Deploy from a branch
  Branch: gh-pages
  Folder: / (root)
  → Save
```

Sau khi bật, mỗi lần CI chạy và deploy thành công, report sẽ cập nhật tại:
`https://<github-username>.github.io/<repo-name>/`
