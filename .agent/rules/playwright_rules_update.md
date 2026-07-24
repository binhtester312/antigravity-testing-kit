# Quy Tắc Playwright — UPDATE (Bài Học Kinh Nghiệm Thực Tế)

> **File này bổ sung cho `playwright_rules.md` (giữ nguyên bản gốc)**
> Ghi nhận những vấn đề thực tế gặp phải khi triển khai Playwright TypeScript framework.
> Ngày cập nhật: 2026-07-22 | Session: Chuyển 21 TC Selenium Java → Playwright TypeScript

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
