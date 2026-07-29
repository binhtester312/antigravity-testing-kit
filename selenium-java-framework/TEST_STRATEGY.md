# Perfex CRM — Web UI Test Strategy

## Testing Objectives

- Đảm bảo chất lượng phần mềm Perfex CRM qua việc tự động hóa kiểm thử giao diện (Web UI Automation).
- Phát hiện lỗi sớm trong quy trình phát triển và kiểm thử liên tục (CI/CD).
- Duy trì bộ kiểm thử hồi quỷ (Regression Suite) ổn định và đáng tin cậy.

---

## Scope of Testing (Phạm Vi Kiểm Thử)

| Loại Test | Áp dụng | Tool / Framework | Ghi chú |
|-----------|:---:|------------------|---------|
| **UI Functional Testing** | ✅ | Selenium 4 (Java 17) | Kiểm thử các chức năng giao diện Login, Customer, Project, Contract |
| **API Testing** | ⬜ | N/A | Không áp dụng (Dự án tập trung 100% vào Web UI Automation) |
| **Unit Testing** | ⬜ | N/A | Phạm vi trách nhiệm của Đội phát triển (Dev Team) |
| **UI Integration Testing** | ✅ | TestNG + Selenium | Luồng E2E nối tiếp giữa các module (Customer -> Project -> Contract) |
| **Performance Testing** | ⬜ | N/A | Chưa áp dụng trong giai đoạn này |
| **Security Testing** | ⬜ | N/A | Chưa áp dụng trong giai đoạn này |
| **Mobile Testing** | ⬜ | N/A | Chưa áp dụng trong giai đoạn này |

---

## Test Automation Strategy (Chiến Lược Tự Động Hóa)

### Kiến trúc Framework
- **Design Pattern:** Page Object Model (POM) — Tách biệt rõ ràng giữa Page Object (`com.anhtester.pages`) và Test Classes (`com.anhtester.tests`).
- **Language:** Java 17
- **Core Automation:** Selenium 4.27.0 + WebDriverManager
- **Test Runner:** TestNG 7.10.2 (Hỗ trợ DataProvider, TestNG Groups, Suite parallel)
- **Build Tool:** Maven (`pom.xml`)
- **Reporting & Logging:** Allure Report 2.27.0 + Log4j 2

### Phạm vi Test Cases Auto
- **Smoke Tests (`@Test(groups = {"smoke"})`):** Kiểm thử Happy Path của 4 module chính.
- **Regression Tests (`@Test(groups = {"high", "medium", "low"})`):** Toàn bộ test suite tự động hóa cho Login (21 TCs), Customer (26 TCs), Project, Contract.
- **Data-Driven Tests (DDT):** Đọc dữ liệu mẫu từ JSON (`test-data/users.json`) và sinh dữ liệu động qua `TestDataGenerator` (JavaFaker).

---

## Test Data Management (Quản Lý Dữ Liệu Test)

- **Random & Traceable:** Sử dụng `TestDataGenerator` kết hợp JavaFaker + Prefix + Timestamp để dữ liệu khởi tạo không trùng lặp và có thể truy vết khi test fail.
- **Tách biệt dữ liệu:** Test data lưu độc lập trong thư mục `test-data/`.
- **Bảo mật:** Không hard-code tài khoản/mật khẩu trong mã nguồn. Cấu hình linh hoạt qua `src/test/resources/config.properties` và biến môi trường (Environment Variables).

---

## Execution Plan (Kế Hoạch Thực Thi)

| Phase | Mô tả | Trigger |
|-------|--------|---------|
| **Smoke Test** | Chạy các test case critical / happy path | Mỗi khi có Push / Pull Request |
| **Regression Suite** | Chạy toàn bộ Test Suites của các module UI | Trước mỗi đợt Release |
| **UI E2E Integration** | Chạy luồng tích hợp đa module UI nối tiếp | Hàng ngày (Nightly Build) |

---

## Test Environment (Môi Trường Kiểm Thử)

- **Target System:** Perfex CRM Staging (`https://crm.anhtester.com/admin/authentication`)
- **CI/CD Execution:** Headless Mode trên Ubuntu Runner (GitHub Actions `selenium.yml`)
- **Local Debugging:** Headed Mode trên máy local (Viewport mặc định 1920x1080)
