---
description: "[UPDATE] Bổ sung lessons learned cho workflow generate_automation_from_testcases — kinh nghiệm thực tế từ session convert 21 TC Selenium Java sang Playwright TypeScript."
skills:
  - qa_automation_engineer
  - ui_debug_agent
  - smart_locator_agent
  - test_data_generator
---

# Workflow Update: Sinh Automation từ Test Cases — Bài Học Thực Tế

> **File này bổ sung cho `generate_automation_from_testcases.md` (giữ nguyên bản gốc)**
> Ngày cập nhật: 2026-07-22

---

## ⚡ QUICK CHECKLIST — Trước Khi Bắt Đầu

Trước khi chạy workflow 6 bước, confirm với user:

- [ ] Tech stack đích là gì? (Playwright TypeScript / Selenium Java / ...)
- [ ] Project đã có sẵn hay scaffold mới?
- [ ] Browsers nào cần chạy? (Chrome / Firefox / cả hai)
- [ ] URL ứng dụng + credentials?
- [ ] Số lần tối đa retry nếu fail? (mặc định 3 vòng)

---

## ⚠️ PITFALLS Thực Tế — Playwright TypeScript

### P1. Browser Config — Chrome Thật vs Chromium Engine

Khi chuyển từ Selenium Java sang Playwright TypeScript, luôn dùng **Chrome thật**:

```typescript
// playwright.config.ts
{
  name: 'chrome',
  use: {
    ...devices['Desktop Chrome'],
    channel: 'chrome',   // ← QUAN TRỌNG: Chrome thật, không phải Chromium engine
  }
}
```

Xem chi tiết: `.agent/rules/playwright_rules_update.md` → **L1**

### P2. TypeScript Strict Mode — Export Logger

Khi tạo `Logger` utility class → **BẮT BUỘC export**:

```typescript
export class Logger { ... }    // ← export để dùng làm type annotation
```

Và khai báo `protected logger: Logger` với type annotation rõ ràng trong BasePage.

Xem chi tiết: `.agent/rules/playwright_rules_update.md` → **L2**

### P3. Auth Setup — Tạo Thư Mục Trước

`playwright/.auth/user.json` phải tồn tại trước khi test chạy. Trong `auth.setup.ts`:

```typescript
const dir = path.dirname(authFile);
if (!fs.existsSync(dir)) {
  fs.mkdirSync(dir, { recursive: true });
}
```

Xem chi tiết: `.agent/rules/playwright_rules_update.md` → **L4**

### P4. Relative Paths — Đếm Số Cấp `../`

Tính từ vị trí file test đến root để import test data:
- `src/tests/auth/login.spec.ts` → 3 cấp → `'../../../test-data/users.json'`
- `src/tests/login.spec.ts` → 2 cấp → `'../../test-data/users.json'`

---

## 🔄 Quy Trình Auto-Heal Khi Test FAIL

Thứ tự phân tích lỗi:

```
1. Đọc error log → xác định dòng fail
2. Phân loại lỗi:
   - TypeScript compile error → sửa type, export, import
   - Element not found → snapshot DOM → cập nhật locator
   - Auth error → kiểm tra playwright/.auth/user.json tồn tại chưa
   - Timeout → tăng timeout hoặc thêm wait condition  
   - Path error → đếm lại số cấp ../
   - Port conflict → kill port hoặc đổi port
3. Sửa code → chạy lại
4. Tối đa 3 vòng (nếu user yêu cầu 3)
```

---

## 📊 Template Báo Cáo Kết Quả

Sau khi hoàn thành, báo cáo theo format:

```
## Kết Quả Chạy Test

| Metric | Giá trị |
|---|---|
| Tổng TC | 21 |
| PASS | 21 |
| FAIL | 0 |
| SKIP | 0 |
| Thời gian | ~5 phút |
| Browsers | Chrome + Firefox |

### Files Đã Tạo/Sửa
- src/pages/login.page.ts
- src/pages/dashboard.page.ts
- src/pages/forgot-password.page.ts
- src/fixtures/auth.setup.ts
- src/tests/auth/login.spec.ts
- src/utils/logger.ts
- src/utils/data-generator.ts
- test-data/users.json

### Lệnh Chạy Test
npx playwright test                    # Toàn bộ suite
npx playwright test --project=chrome   # Chỉ Chrome
npx playwright test --project=firefox  # Chỉ Firefox

### Xem Report
npx playwright show-report
open playwright-report/index.html      # Nếu port 9323 bận
```
