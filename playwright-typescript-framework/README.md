# n_framework — Playwright + TypeScript E2E Automation Framework

[![Playwright Tests](https://github.com/your-org/n_framework/actions/workflows/playwright.yml/badge.svg)](https://github.com/your-org/n_framework/actions/workflows/playwright.yml)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.4-blue)](https://www.typescriptlang.org/)
[![Playwright](https://img.shields.io/badge/Playwright-1.47-green)](https://playwright.dev/)

Framework E2E Web UI Automation được xây dựng với Playwright Test + TypeScript (Strict Mode), tuân thủ Page Object Model và các best practices automation.

---

## 📋 Prerequisites

| Tool | Phiên bản yêu cầu | Kiểm tra |
|------|-------------------|----------|
| Node.js | >= 18.0.0 | `node --version` |
| npm | >= 9.0.0 | `npm --version` |
| Git | any | `git --version` |

---

## 🚀 Cài đặt

### 1. Clone repository

```bash
git clone https://github.com/your-org/n_framework.git
cd n_framework
```

### 2. Cài đặt dependencies

```bash
npm install
```

### 3. Cài đặt Playwright browsers

```bash
npx playwright install
# Hoặc chỉ cài chromium (nhanh hơn):
npx playwright install chromium
```

### 4. Cấu hình environment

```bash
# Copy template và điền giá trị thực
cp .env.example .env
```

Mở file `.env` và cập nhật:

```env
BASE_URL=https://your-app-url.com
DEFAULT_USERNAME=your_test_user@example.com
DEFAULT_PASSWORD=YourPassword@123
```

---

## 🧪 Chạy Tests

### Chạy toàn bộ test suite

```bash
npm test
```

### Chạy với browser hiển thị (headed mode)

```bash
npm run test:headed
```

### Chạy với Playwright UI (interactive)

```bash
npm run test:ui
```

### Chạy theo tag

```bash
# Smoke tests
npm run test:smoke

# Regression tests
npm run test:regression

# Auth tests
npm run test:auth
```

### Chạy test cụ thể

```bash
# Chạy theo file
npx playwright test src/tests/auth/login.spec.ts

# Chạy theo tên test
npx playwright test --grep "TC_LOGIN_001"

# Chạy trên browser cụ thể
npx playwright test --project=firefox
```

### Xem HTML Report

```bash
npm run test:report
```

### Debug mode

```bash
npm run test:debug
```

---

## 📁 Project Structure

```
n_framework/
├── playwright.config.ts          # Playwright configuration
├── package.json                  # Dependencies + npm scripts
├── tsconfig.json                 # TypeScript strict mode config
├── .env.example                  # Environment variables template
├── .env                          # Environment variables (gitignored)
├── .gitignore
├── README.md
│
├── src/
│   ├── pages/                    # Page Object classes
│   │   ├── base.page.ts          # Abstract base page (common methods)
│   │   ├── login.page.ts         # Login page
│   │   └── dashboard.page.ts     # Dashboard page
│   │
│   ├── fixtures/                 # Custom test fixtures
│   │   ├── base.fixture.ts       # Extended test + all page objects
│   │   └── auth.fixture.ts       # Authentication state fixture
│   │
│   ├── utils/                    # Utility helpers
│   │   ├── env.config.ts         # Typed environment config reader
│   │   ├── test-data.ts          # Test data generators (unique + traceable)
│   │   ├── helpers.ts            # Common helper functions
│   │   └── logger.ts             # Structured logger (replaces console.log)
│   │
│   └── tests/                    # Test specifications
│       └── auth/
│           ├── auth.setup.ts     # Global auth setup (runs once)
│           └── login.spec.ts     # Login test cases
│
├── test-data/                    # External test data (JSON/CSV)
│   └── users.json
│
├── playwright-report/            # HTML test report (gitignored)
├── test-results/                 # Raw test results (gitignored)
└── .github/
    └── workflows/
        └── playwright.yml        # GitHub Actions CI pipeline
```

---

## 🏗️ Architecture

### Page Object Model (POM)

```
Test File (login.spec.ts)
    ↓ uses fixtures
Base Fixture (base.fixture.ts)
    ↓ injects
Page Objects (LoginPage, DashboardPage)
    ↓ extends
Base Page (base.page.ts)
    ↓ uses
Utils (logger, env.config, test-data)
```

### Test Flow

```
auth.setup.ts → Login + Save Session
    ↓
*.spec.ts → Load Session + Run Tests
    ↓
playwright-report/ → HTML Report
```

---

## 📝 Coding Conventions

### Naming

| Item | Convention | Ví dụ |
|------|------------|-------|
| File (Page) | `kebab-case.page.ts` | `login.page.ts` |
| File (Test) | `kebab-case.spec.ts` | `login.spec.ts` |
| File (Fixture) | `kebab-case.fixture.ts` | `base.fixture.ts` |
| Class | `PascalCase` | `LoginPage` |
| Method | `camelCase` | `clickLoginButton()` |
| Locator | `camelCase + meaningful name` | `usernameInput` |
| Test ID | `TC_MODULE_NNN` | `TC_LOGIN_001` |

### Test Tags

| Tag | Mục đích |
|-----|----------|
| `@smoke` | Tests chạy nhanh, verify core flow |
| `@regression` | Bộ regression đầy đủ |
| `@auth` | Tests liên quan đến authentication |

### Import Order

```typescript
// 1. Playwright imports
import { test, expect } from '../../fixtures/base.fixture';

// 2. Page imports (nếu cần thêm ngoài fixture)
import { LoginPage } from '../../pages/login.page';

// 3. Utils
import { TestDataGenerator } from '../../utils/test-data';
```

---

## 🔧 Thêm Page mới

1. Tạo file `src/pages/your-feature.page.ts`
2. Extend `BasePage`
3. Implement `verifyPageLoaded()` abstract method
4. Thêm vào `src/fixtures/base.fixture.ts`

```typescript
// your-feature.page.ts
import { BasePage } from './base.page';
import { type Page, expect } from '@playwright/test';

export class YourFeaturePage extends BasePage {
  readonly someButton = this.page.getByRole('button', { name: 'Do Something' });

  constructor(page: Page) {
    super(page);
  }

  async verifyPageLoaded(): Promise<void> {
    await expect(this.someButton).toBeVisible();
  }
}
```

---

## 🌐 CI/CD

Framework đã tích hợp **GitHub Actions** tại `.github/workflows/playwright.yml` để tự động hóa kiểm thử và tạo báo cáo Allure.

### GitHub Secrets cần thiết

| Secret | Mô tả |
|--------|-------|
| `BASE_URL` | URL của ứng dụng cần test |
| `DEFAULT_USERNAME` | Username cho test account |
| `DEFAULT_PASSWORD` | Password cho test account |

### Thêm Secrets vào GitHub

```
GitHub repo → Settings → Secrets and variables → Actions → New repository secret
```

### Pipeline Jobs
Khi push hoặc tạo PR lên `main` hoặc `develop`, workflow sẽ chạy 4 jobs liên tiếp:
1. **📦 Install & Validate** — Cài đặt npm dependencies, chạy TypeScript validation (`npm run type-check`).
2. **🧪 Tests — Chromium** — Chạy toàn bộ test suites trên Chromium headless, lưu trữ screenshots/video khi fail và xuất dữ liệu `allure-results`.
3. **📊 Generate & Deploy Allure Report** — Tải về results, kế thừa lịch sử (trend graph), tạo HTML report và tự động deploy lên **GitHub Pages** (nhánh `gh-pages`).
4. **📋 Test Summary** — Hiển thị bảng tổng hợp kết quả (Pass/Fail) và đính kèm trực tiếp link Allure Report trên giao diện tóm tắt của Job.

### 🌐 Truy cập Allure Report online trên GitHub Pages
Sau khi pipeline hoàn tất, báo cáo Allure Report online của dự án sẽ sẵn sàng tại địa chỉ:
`https://<github-username>.github.io/<repo-name>/`

---

## 🐛 Troubleshooting

### Lỗi: `Auth file not found`

```bash
# Chạy auth setup thủ công
npx playwright test src/tests/auth/auth.setup.ts
```

### Lỗi: `Element not found`

Locators trong Page classes có ghi chú `// REPLACE:` — cần update sau khi inspect DOM thực tế:

```bash
# Mở browser và inspect
npx playwright codegen https://your-app-url.com
```

### Lỗi: TypeScript compile errors

```bash
npm run type-check
```

### Chạy test không ổn định (flaky)

1. Tăng timeout trong `.env`: `DEFAULT_TIMEOUT=60000`
2. Tăng retry trong `.env`: `TEST_RETRY_COUNT=2`
3. Kiểm tra smart waits trong Page class

---

## 📊 Báo cáo

Framework hỗ trợ cả **Playwright HTML Report** (cơ bản) và **Allure Report** (nâng cao, có biểu đồ và phân cấp kịch bản nghiệp vụ trực quan).

### 1. Allure Report (Nâng cao - Khuyên dùng)
Allure Report đã được tích hợp đầy đủ, cấu hình phẳng sạch khớp 100% kịch bản kiểm thử nghiệp vụ, tự động đính kèm screenshot khi test passed/failed và video execution.

* **Chạy tests và tự động generate + mở Allure report:**
  ```bash
  npm run test:allure
  ```
* **Tạo Allure HTML Report từ dữ liệu sẵn có:**
  ```bash
  npm run allure:generate
  ```
* **Mở Allure Report đã tạo:**
  ```bash
  npm run allure:open
  ```
* **Serve nhanh dữ liệu Allure kết quả (không tạo file HTML tĩnh):**
  ```bash
  npm run allure:serve
  ```

### 2. Playwright HTML Report (Cơ bản)
* **Xem report mặc định của Playwright:**
  ```bash
  npm run test:report
  # → Mở localhost server tại cổng 9323
  ```

Report bao gồm:
- ✅ / ❌ Test results chi tiết
- 📸 Screenshots đính kèm khi test Passed (bước cuối) hoặc khi Failed (tại thời điểm lỗi)
- 🎥 Video recordings quá trình chạy test
- 🔍 Trace viewer để debug step-by-step khi fail

---

## 🤝 Contribution

1. Tạo branch từ `develop`
2. Tuân thủ coding conventions
3. Đảm bảo `npm run type-check` pass
4. Test PASS ổn định ít nhất 2 lần trước khi PR
5. Xóa debug logs, commented code trước khi commit
