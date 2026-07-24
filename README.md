# 🧪 Antigravity Testing Kit — Perfex CRM Automation

[![🎭 Playwright E2E Tests + Allure Report](https://github.com/binhtester312/antigravity-testing-kit/actions/workflows/playwright.yml/badge.svg)](https://github.com/binhtester312/antigravity-testing-kit/actions/workflows/playwright.yml)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.4-3178C6?logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Playwright](https://img.shields.io/badge/Playwright-1.47-45ba4b?logo=playwright&logoColor=white)](https://playwright.dev/)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Selenium](https://img.shields.io/badge/Selenium-4.27-43B02A?logo=selenium&logoColor=white)](https://www.selenium.dev/)
[![Allure Report](https://img.shields.io/badge/Allure-Report-orange)](https://allurereport.org/)

Bộ tự động hóa kiểm thử (Test Automation) cho ứng dụng **Perfex CRM**, bao gồm 2 framework song song:

| Framework | Ngôn ngữ | Mục đích |
|-----------|----------|----------|
| `playwright-typescript-framework/` | TypeScript | E2E UI Testing (Primary) |
| `selenium-java-framework/` | Java | E2E UI Testing (Legacy / Reference) |

---

## 📋 Mục lục

- [Yêu cầu hệ thống](#-yêu-cầu-hệ-thống)
- [Cấu trúc Project](#-cấu-trúc-project)
- [Playwright Framework (TypeScript)](#-playwright-framework-typescript)
- [Selenium Framework (Java)](#-selenium-framework-java)
- [Test Cases đã implement](#-test-cases-đã-implement)
- [CI/CD — GitHub Actions](#-cicd--github-actions)
- [Allure Report](#-allure-report)

---

## 🖥️ Yêu cầu hệ thống

| Tool | Phiên bản | Kiểm tra |
|------|-----------|----------|
| Node.js | >= 18.0.0 | `node --version` |
| npm | >= 9.0.0 | `npm --version` |
| Java JDK | 17 | `java --version` |
| Maven | >= 3.8 | `mvn --version` |
| Allure CLI | >= 2.27 | `allure --version` |
| Git | any | `git --version` |

---

## 📁 Cấu trúc Project

```
antigravity-testing-kit/
│
├── .github/
│   └── workflows/
│       └── playwright.yml          # CI/CD pipeline — GitHub Actions
│
├── playwright-typescript-framework/  # 🎭 Playwright E2E Framework (Primary)
│   ├── .github/workflows/            # (Legacy — đã chuyển lên root)
│   ├── src/
│   │   ├── pages/                    # Page Object Model
│   │   │   ├── base.page.ts          # Abstract base với Allure steps
│   │   │   ├── login.page.ts
│   │   │   ├── dashboard.page.ts
│   │   │   └── forgot-password.page.ts
│   │   ├── fixtures/                 # Custom test fixtures
│   │   │   ├── base.fixture.ts       # Extended test + auto screenshot attach
│   │   │   └── auth.fixture.ts
│   │   ├── utils/                    # Tiện ích
│   │   │   ├── allure.helper.ts      # Allure attachment helpers
│   │   │   ├── env.config.ts         # Typed env variables
│   │   │   ├── test-data.ts          # Random/traceable test data generator
│   │   │   ├── helpers.ts
│   │   │   └── logger.ts             # Structured logger
│   │   └── tests/
│   │       └── auth/
│   │           ├── auth.setup.ts     # Global auth state (chạy 1 lần)
│   │           └── login.spec.ts     # 21 Test Cases Login/Logout/ForgotPW
│   ├── playwright.config.ts
│   ├── package.json
│   ├── tsconfig.json
│   └── README.md
│
├── selenium-java-framework/          # ☕ Selenium Java Framework (Legacy)
│   ├── src/
│   │   ├── main/java/com/anhtester/
│   │   │   └── pages/                # Page Object Model (Java)
│   │   │       ├── BasePage.java
│   │   │       ├── LoginPage.java
│   │   │       ├── DashboardPage.java
│   │   │       └── ForgotPasswordPage.java
│   │   └── test/java/com/anhtester/
│   │       └── tests/                # TestNG test classes
│   ├── test-data/
│   ├── pom.xml
│   └── README_AUTOMATION.md
│
└── README.md                         # ← File này
```

---

## 🎭 Playwright Framework (TypeScript)

Framework chính — Playwright Test + TypeScript Strict Mode + Allure Report tích hợp hoàn toàn.

### Cài đặt

```bash
cd playwright-typescript-framework
npm install
npx playwright install chromium
cp .env.example .env
# Chỉnh sửa .env với thông tin thực tế
```

### Cấu hình `.env`

```env
BASE_URL=https://crm.anhtester.com/admin/authentication
DEFAULT_USERNAME=admin@example.com
DEFAULT_PASSWORD=123456
HEADLESS=false
ENV=staging
```

### Chạy Tests

```bash
# Toàn bộ test suite (headless)
npm test

# Chỉ project Chromium
npx playwright test --project=chromium

# Headed mode (có giao diện)
npm run test:headed

# Chạy theo tag
npm run test:smoke       # @smoke
npm run test:regression  # @regression
npm run test:security    # @security

# Interactive UI mode
npm run test:ui
```

### Xem Reports

```bash
# Allure Report — khuyến nghị dùng (đẹp, chi tiết nhất)
npm run test:allure          # Chạy test + generate + mở report tự động
npm run allure:generate      # Tạo HTML report từ allure-results
npm run allure:open          # Mở HTML report đã tạo
npm run allure:serve         # Serve trực tiếp từ allure-results

# Playwright HTML Report (cơ bản)
npm run report:playwright    # Mở tại localhost:9323
```

### Kiến trúc

```
login.spec.ts  →  base.fixture.ts  →  LoginPage / DashboardPage  →  base.page.ts
                                          ↓                              ↓
                                    Allure parent steps           Allure child steps
                                    (business level)              (selector + value)
```

**Allure Step Hierarchy (phẳng, khớp kịch bản kiểm thử):**
```
Test body
  ├─ Login to CRM with email: "admin@example.com"
  │    ├─ Enter email address: "admin@example.com" into input "#email"
  │    ├─ Enter password: "••••••••" into input "#password"
  │    └─ Click Login button on selector "button[type='submit']"
  └─ Verify Dashboard page loaded successfully
```

---

## ☕ Selenium Framework (Java)

Framework kế thừa — Java 17 + Selenium 4 + TestNG + Allure.

### Cài đặt & Chạy

```bash
cd selenium-java-framework
mvn clean install -DskipTests

# Chạy toàn bộ test
mvn test

# Chạy với tag cụ thể
mvn test -Dgroups=smoke

# Generate Allure Report
mvn allure:serve
```

### Tech Stack

| Thư viện | Phiên bản | Mục đích |
|----------|-----------|----------|
| Selenium WebDriver | 4.27.0 | Browser automation |
| TestNG | 7.10.2 | Test runner & assertion |
| WebDriverManager | 5.9.2 | Tự động quản lý driver |
| Allure TestNG | 2.27.0 | Test reporting |
| Log4j 2 | 2.24.3 | Structured logging |

---

## ✅ Test Cases đã implement

### Module: Login / Logout / Forgot Password — 21 TCs

| TC | Tên Test Case | Priority | Tags |
|----|---------------|----------|------|
| TC_001 | Login with valid email and password | 🔴 High | `@smoke` |
| TC_002 | Login with invalid email format | 🟡 Medium | — |
| TC_003 | Login with empty email | 🟡 Medium | — |
| TC_004 | Login with empty password | 🟡 Medium | — |
| TC_005 | Login with both fields empty | 🟡 Medium | — |
| TC_006 | Login with unregistered email | 🔴 High | — |
| TC_007 | Login with correct email + wrong password | 🔴 High | — |
| TC_008 | Brute force — 5 wrong attempts trigger lockout message | 🔴 High | `@security` |
| TC_009 | Valid email + wrong password → server-side error | 🔴 High | — |
| TC_010 | Submit login form via Enter key | 🟡 Medium | — |
| TC_011 | Email with leading/trailing whitespace — BVA edge case | 🟡 Medium | — |
| TC_012 | Password with 1 character — BVA lower boundary | 🟡 Medium | — |
| TC_013 | Password with 255 characters — BVA upper boundary | 🟢 Low | — |
| TC_014 | Already logged in → navigate back to Login URL → redirect | 🟡 Medium | — |
| TC_015 | CSRF token present in form — request without token rejected | 🔴 High | `@security` |
| TC_016 | Click "Forgot Password?" link → navigates to correct URL | 🟡 Medium | — |
| TC_017 | Submit registered email → system sends recovery email | 🔴 High | — |
| TC_018 | Submit Forgot Password with blank email → HTML5 validation | 🔴 High | — |
| TC_019 | Forgot Password with non-existent email → no info disclosure | 🔴 High | `@security` |
| TC_020 | Logout successfully → redirects to Login page | 🟢 Low | — |
| TC_021 | After logout, press browser Back → must not regain Dashboard | 🟢 Low | `@security` |

---

## 🌐 CI/CD — GitHub Actions

Workflow tự động tại [`.github/workflows/playwright.yml`](.github/workflows/playwright.yml).

**Kích hoạt:** Mỗi khi push hoặc tạo PR lên nhánh `main` hoặc `develop`.

### Pipeline (4 Jobs)

```
📦 Install & Validate (13s)
        ↓
🧪 Tests — Chromium (1m 20s)
        ↓
📊 Generate & Deploy Allure Report (57s)
        ↓
📋 Test Summary (3s)
```

| Job | Mô tả |
|-----|-------|
| 📦 Install & Validate | Cài npm, kiểm tra TypeScript (`tsc --noEmit`) |
| 🧪 Tests — Chromium | Chạy 22 tests headless, export `allure-results` |
| 📊 Generate & Deploy | Tạo HTML report, deploy lên GitHub Pages (nhánh `gh-pages`) |
| 📋 Test Summary | Bảng tổng hợp Pass/Fail, link Allure Report |

### GitHub Repository Variables cần thiết

```
Settings → Secrets and variables → Actions → Variables
```

| Tên biến | Giá trị ví dụ |
|----------|---------------|
| `BASE_URL` | `https://crm.anhtester.com/admin/authentication` |
| `DEFAULT_USERNAME` | `admin@example.com` |
| `DEFAULT_PASSWORD` | `123456` |

### Kích hoạt thủ công (Workflow Dispatch)

Vào **Actions → 🎭 Playwright E2E Tests + Allure Report → Run workflow** để:
- Chọn browser: `chromium` hoặc `all`
- Lọc test theo pattern: `@smoke`, `@security`, v.v.

### 🌐 Xem Allure Report Online (GitHub Pages)

> **Lưu ý:** Cần bật GitHub Pages trong Settings một lần:
> `Settings → Pages → Source: Deploy from a branch → Branch: gh-pages → Save`

Sau khi bật, Allure Report online sẽ tự động cập nhật sau mỗi lần pipeline chạy tại:

**`https://binhtester312.github.io/antigravity-testing-kit/`**

---

## 📊 Allure Report

### Cấu trúc phân cấp trong Report

```
Epic:    Perfex CRM
Feature: Authentication — Login / Logout / Forgot Password
Story:   TC_001: Login with valid email and password
```

### Tính năng nổi bật
- ✅ **Flat step hierarchy** — không có wrapper `[Arrange]/[Act]/[Assert]` rườm rà
- 📸 **Auto screenshot** — đính kèm ảnh màn hình sau bước cuối của test **PASSED**
- 🎥 **Video recording** — ghi lại toàn bộ quá trình chạy mỗi test
- 📈 **Trend graph** — biểu đồ xu hướng pass/fail theo thời gian (kế thừa lịch sử)
- 🔐 **Password masking** — mật khẩu hiển thị dạng `"••••••••"` trong report

---

## 🤝 Contribution Guide

1. Tạo branch từ `develop`
2. Tuân thủ **Page Object Model** — không viết locator trong test file
3. Đảm bảo `npm run type-check` pass
4. Test **PASS ổn định ít nhất 2 lần** trước khi tạo PR
5. Xóa debug logs và code comment thừa trước khi commit
