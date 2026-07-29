# Test Strategy

## Hướng Dẫn Sử Dụng

File này định nghĩa chiến lược testing cho dự án. Agent tham khảo file này để hiểu scope, ưu tiên, và approach khi sinh test cases hoặc automation scripts.

> ⚠️ **Bạn cần cập nhật file này** cho mỗi dự án cụ thể. Dưới đây là template.

---

## Testing Objectives

- Đảm bảo chất lượng phần mềm qua các cấp độ test
- Phát hiện lỗi sớm trong development lifecycle
- Duy trì regression suite ổn định cho CI/CD

## Scope of Testing

| Loại Test | Áp dụng | Tool/Framework |
|-----------|---------|----------------|
| UI Functional Testing | ✅ | Selenium 4 (Java 17) |
| API Testing | ⬜ | N/A (Dự án tập trung 100% UI Automation) |
| Unit Testing | ⬜ | N/A (Phạm vi của phát triển/Dev) |
| UI Integration Testing | ✅ | TestNG + Selenium (Cross-module E2E Flow) |
| Performance Testing | ⬜ | JMeter / k6 |
| Security Testing | ⬜ | OWASP ZAP |
| Mobile Testing | ⬜ | Appium |

## Test Automation Strategy

### Framework Architecture
- **Design Pattern:** Page Object Model (POM)
- **Language:** Java 17
- **Test Runner:** TestNG
- **Build Tool:** Maven
- **Reporting:** Allure Report + Log4j 2

### Automation Scope
- Smoke tests: Bao phủ happy path của các chức năng chính (Login, Customer, Project, Contract)
- Regression tests: Bao phủ tất cả test cases đã pass
- Data-driven tests: Sử dụng external data sources (JSON, JavaFaker)

## Test Data Management

- Sử dụng random data có prefix + timestamp để traceable
- Tách biệt test data khỏi test logic (Folder `test-data/` & `TestDataGenerator`)
- Không hard-code credentials trong code (`config.properties` / `.env`)

## Execution Plan

| Phase | Mô tả | Trigger |
|-------|--------|---------|
| Smoke Test | Happy path chính các module | Mỗi build / PR |
| Regression | Full suite các module UI | Trước release |
| UI Integration | Luồng E2E nối tiếp nhiều module UI | Hàng ngày / Nightly |

## Test Environment

- Test chạy trên môi trường Staging
- CI/CD pipeline chạy headless mode
- Local debug chạy headed mode (viewport 1920x1080)
