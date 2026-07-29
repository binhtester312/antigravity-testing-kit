# 🧪 Antigravity Testing Kit — Perfex CRM Test Automation

[![🎭 Playwright E2E Tests + Allure Report](https://github.com/binhtester312/antigravity-testing-kit/actions/workflows/playwright.yml/badge.svg)](https://github.com/binhtester312/antigravity-testing-kit/actions/workflows/playwright.yml)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.4-3178C6?logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Playwright](https://img.shields.io/badge/Playwright-1.47-45ba4b?logo=playwright&logoColor=white)](https://playwright.dev/)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Selenium](https://img.shields.io/badge/Selenium-4.27-43B02A?logo=selenium&logoColor=white)](https://www.selenium.dev/)
[![Allure Report](https://img.shields.io/badge/Allure-Report-orange)](https://allurereport.org/)

Hệ thống tự động hóa kiểm thử (Test Automation Framework) chuẩn **Page Object Model (POM)** cho ứng dụng **Perfex CRM**, hỗ trợ 2 bộ framework song song:

| Framework | Tech Stack | Phạm vi Module đã triển khai |
|---|---|---|
| **`selenium-java-framework/`** | Java 17 + Selenium 4 + TestNG + Allure | **Login, Dashboard, Customers, Projects, Contracts** |
| **`playwright-typescript-framework/`** | TypeScript + Playwright Test + Allure | **Authentication (Login/Logout/Forgot PW)** |

---

## 📁 Cấu Trúc Dự Án

```text
antigravity-testing-kit/
├── .github/workflows/           # CI/CD Pipeline (GitHub Actions)
├── playwright-typescript-framework/ # Framework Playwright TypeScript
│   ├── src/pages/              # Page Objects (BasePage, LoginPage, DashboardPage...)
│   ├── src/tests/              # Playwright E2E Tests
│   └── playwright.config.ts
├── selenium-java-framework/     # Framework Selenium Java TestNG
│   ├── src/main/java/com/anhtester/
│   │   ├── pages/              # Page Objects (BasePage, LoginPage, DashboardPage, CustomerPage, ProjectPage, ContractPage)
│   │   └── utils/              # WaitHelper, TestDataGenerator, ScreenshotUtil
│   ├── src/test/java/com/anhtester/
│   │   └── tests/              # Test Suites (Login, Dashboard, Customer, Project, Contract)
│   ├── test-data/              # Test cases CSV & Data-driven files
│   └── pom.xml
└── README.md
```

---

## ☕ Selenium Java Framework

Framework chuẩn Enterprise với **Selenium WebDriver 4**, **TestNG**, **Log4j2**, và **Allure Report**.

### 🚀 Hướng dẫn chạy Test

```bash
cd selenium-java-framework

# 1. Chạy toàn bộ Test Suite
mvn test

# 2. Chạy lẻ theo Module cụ thể
mvn test -Dtest=DashboardTest
mvn test -Dtest=ProjectTest
mvn test -Dtest=CustomerTest
mvn test -Dtest=ContractTest

# 3. Chạy duy nhất 1 Test Case hoặc Batch
mvn test -Dtest=DashboardTest#CRM_DASHBOARD_TC_007_verifyQuickStatsInvoicesAwaitingPayment

# 4. Xem Allure Report trực quan
mvn allure:serve
```

---

## 🎭 Playwright TypeScript Framework

Framework hiện đại tối ưu tốc độ E2E Test với **Playwright**, **TypeScript Strict**, tích hợp **Allure Report**.

### 🚀 Hướng dẫn chạy Test

```bash
cd playwright-typescript-framework

# 1. Cài đặt phụ thuộc
npm install
npx playwright install chromium

# 2. Thực thi Test Suite
npm test               # Headless mode
npm run test:headed    # UI Headed mode
npm run test:ui        # Interactive Playwright UI mode

# 3. Xem báo cáo Allure Report
npm run test:allure
```

---

## 📊 CI/CD & Allure Report Online

Tự động thực thi kịch bản kiểm thử và publish Allure Report lên **GitHub Pages** sau mỗi lần `git push`:

🌐 **Allure Report Online**: [https://binhtester312.github.io/antigravity-testing-kit/](https://binhtester312.github.io/antigravity-testing-kit/)

---

## 📐 Quy Chuẩn Code (Contribution Rules)

1. **Page Object Model (POM):** Tuyệt đối không hardcode locators trong file test class.
2. **Smart Waits Only:** Không sử dụng `Thread.sleep()`. Luôn dùng `WebDriverWait` hoặc `expect()`.
3. **Traceable Test Data:** Mọi dữ liệu tạo mới phải tự động sinh ngẫu nhiên kèm timestamp (`auto_company_1712049200`).
4. **Clean Code:** Gỡ bỏ `console.log`, debug logs và code comment thừa trước khi commit.
