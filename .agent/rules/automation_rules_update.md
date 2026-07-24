# Quy Tắc Automation Chung — UPDATE (Bài Học Kinh Nghiệm Thực Tế)

> **File này bổ sung cho `automation_rules.md` (giữ nguyên bản gốc)**
> Ngày cập nhật: 2026-07-22 | Session: Convert Selenium Java → Playwright TypeScript

---

## L1. Quy Trình Chuyển Đổi Selenium Java → Playwright TypeScript

### Mapping kỹ thuật cốt lõi

| Selenium Java | Playwright TypeScript |
|---|---|
| `@FindBy(id="email")` | `page.locator('#email')` hoặc `page.getByLabel('Email')` |
| `WebDriverWait(driver, 10).until(...)` | Auto-waiting (không cần viết) |
| `driver.findElement(By.id(...)).click()` | `await page.locator(...).click()` |
| `@BeforeMethod / @AfterMethod` | `test.beforeEach / test.afterEach` |
| `Assert.assertEquals(actual, expected)` | `await expect(locator).toHaveText(expected)` |
| `@DataProvider` | `for...of` loop hoặc `test.each` |
| `driver.navigate().to(url)` | `await page.goto(url)` |
| `Thread.sleep(2000)` | Không cần (auto-wait) |

### Thứ tự convert đúng

1. **Page Objects trước** — convert tất cả `Page.java` → `*.page.ts`
2. **BasePage** — abstract class với `page`, `logger` (đã export)
3. **Test Data** — `users.json` giữ nguyên, chỉ thêm TypeScript type
4. **Auth Setup** — tạo `auth.setup.ts` thay cho `@BeforeClass` login
5. **Test Classes** — convert `*Test.java` → `*.spec.ts`

---

## L2. Chạy Test — Tối Đa 3 Lần Vòng Lặp Auto-Heal

Khi user yêu cầu "chạy kiểm tra, chạy tối đa 3 lần":

```
Vòng 1: npx playwright test --headed → đọc log → fix
Vòng 2: npx playwright test --headed → đọc log → fix  
Vòng 3: npx playwright test --headed → báo cáo kết quả
```

- Mỗi vòng phải chạy **cả suite** không chỉ 1 file
- Sau 3 vòng nếu vẫn còn FAIL → báo cáo rõ TC nào fail, lý do, giải pháp đề xuất
- Không hỏi user trong quá trình fix lỗi (trừ business rule mâu thuẫn)

---

## L3. Validation Sau Khi Scaffold Framework

Thứ tự bắt buộc:
```bash
1. npm install
2. npx playwright install chromium firefox
3. npm run type-check   → phải 0 errors
4. npx playwright test --list  → đếm số tests
5. npx playwright test [1 file] --headed  → verify 1 test PASS
6. npx playwright test → chạy toàn suite
```

Không bỏ qua bước 3 (type-check) — TypeScript lỗi compilation ≠ test FAIL,
nhưng nếu không check trước thì khó debug khi test chạy fail.

---

## L4. Đặt Tên File Đúng Convention

| Loại file | Convention | Ví dụ |
|---|---|---|
| Page Object | `kebab-case.page.ts` | `login.page.ts`, `forgot-password.page.ts` |
| Test spec | `kebab-case.spec.ts` | `login.spec.ts` |
| Auth setup | `auth.setup.ts` | `src/fixtures/auth.setup.ts` |
| Utils | `kebab-case.ts` | `data-generator.ts`, `logger.ts` |
| Test data | `kebab-case.json` | `users.json` |

---

## L5. Test Independence — Vấn Đề Auth State

Khi dùng `storageState` (shared login):
- Tất cả tests dùng chung session → không test được "chưa đăng nhập"
- TC cần test trạng thái chưa đăng nhập → thêm `test.use({ storageState: undefined })`

```typescript
test.describe('Tests cần login', () => {
  // Dùng storageState từ config (mặc định)
  test('xem dashboard', async ({ page }) => { ... });
});

test.describe('Tests không cần login', () => {
  test.use({ storageState: undefined });  // ← override, không dùng auth state

  test('trang login hiển thị đúng', async ({ page }) => { ... });
  test('login với credentials sai', async ({ page }) => { ... });
});
```

---

## L6. CI/CD Pipeline — GitHub Actions (Session 2)

### Cấu trúc 4-job chuẩn cho Playwright + Allure

```yaml
jobs:
  install:       # 📦 npm ci + tsc --noEmit (type-check)
  test:          # 🧪 playwright test → upload allure-results artifact
  allure-report: # 📊 download artifact → allure generate → deploy gh-pages
  summary:       # 📋 tóm tắt pass/fail trong Job Summary
```

### Thứ tự phụ thuộc:
```
install → test → allure-report → summary
```

### Khi project nằm trong thư mục con — dùng `working-directory`

```yaml
jobs:
  test:
    defaults:
      run:
        working-directory: playwright-typescript-framework   # ← set toàn job
    steps:
      - uses: actions/checkout@v4
      - run: npm ci
      - run: npx playwright install chromium --with-deps
      - run: npx playwright test --project=chromium
```

---

## L7. Allure — Cài Global Cho Developer Local

```bash
# Cài 1 lần, dùng mãi
npm install -g allure-commandline

# Sau đó dùng trực tiếp:
allure serve allure-results   # Nhanh nhất — serve + auto open
allure generate allure-results --clean && allure open allure-report
```

> **Lỗi `allure: command not found`** → chưa cài global hoặc PATH chưa bao gồm npm global bin.
> Fix: `npm install -g allure-commandline` rồi mở terminal mới.

---

## L8. Monorepo — Artifact Upload/Download Path

Khi dùng `working-directory`, artifact path phải là **đường dẫn từ workspace root**, không phải từ working-directory:

```yaml
# ✅ Đúng — path từ workspace root
- uses: actions/upload-artifact@v4
  with:
    name: allure-results
    path: playwright-typescript-framework/allure-results

# ❌ Sai — chỉ là tên thư mục (thiếu prefix)
    path: allure-results
```

---

## L9. Definition of Done — Checklist CI/CD

Trước khi coi CI là "done":

- [ ] Workflow file ở đúng vị trí: `<repo-root>/.github/workflows/*.yml`
- [ ] Tất cả jobs có status `Success` (xanh lá)
- [ ] Allure artifact được upload (Artifacts count > 0)
- [ ] Job Summary hiển thị bảng Pass/Fail
- [ ] GitHub Pages bật đúng branch `gh-pages`
- [ ] Allure Report URL online truy cập được

---

## L10. README — Cập Nhật Sau Mỗi Session Lớn

Sau mỗi session triển khai CI/CD hoặc tính năng mới, README gốc cần cập nhật:

1. **Badges** — CI status badge từ GitHub Actions
2. **Lệnh chạy mới** — thêm `npm run allure:serve`, `npm run test:allure`
3. **CI/CD section** — mô tả pipeline, variables cần thiết
4. **GitHub Pages link** — link Allure Report online

Xem template README tại root project: `README.md`
