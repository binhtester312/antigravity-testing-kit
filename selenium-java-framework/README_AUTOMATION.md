# 🚀 PERFEX CRM — SELENIUM 4 AUTOMATION FRAMEWORK

Hệ thống Automation Testing hoàn chỉnh cho dự án **Perfex CRM** (https://crm.anhtester.com), được thiết kế theo mô hình **Page Object Model (POM)** chuẩn enterprise với ngôn ngữ **Java**, **Selenium WebDriver 4**, **TestNG**, **Allure Report**, và hỗ trợ **Data-Driven Testing (DDT)**.

---

## 📁 CẤU TRÚC DỰ ÁN (PROJECT STRUCTURE)

```text
selenium-java-framework/
├── requirements_login.md           # Tài liệu yêu cầu Module Login (21 TCs)
├── requirements_customer.md        # Tài liệu yêu cầu Module Customers (26 TCs)
├── testcases_login.md              # Bộ Test Cases RBT Module Login (Markdown)
├── testcases_customer.md           # Bộ Test Cases RBT Module Customers (Markdown)
├── test-data/
│   ├── users.json                  # Dữ liệu test tài khoản Login
│   ├── customers.json              # Dữ liệu test template Customers (DDT)
│   └── testcases_customer.csv      # File CSV Test Cases Customers (Import Jira/Xray)
├── src/
│   ├── main/java/com/anhtester/
│   │   ├── config/                 # ConfigReader (Đọc file config.properties)
│   │   ├── driver/                 # DriverFactory (ThreadLocal WebDriver setup)
│   │   ├── pages/                  # Page Object Classes
│   │   │   ├── BasePage.java       # Parent Page Object (UI Interactions & Waits)
│   │   │   ├── LoginPage.java      # Page Object Module Login
│   │   │   ├── DashboardPage.java  # Page Object Dashboard & Navigation
│   │   │   ├── CustomerPage.java   # Page Object Module Customers
│   │   │   └── ForgotPasswordPage.java
│   │   └── utils/                  # Helpers & Dynamic Data Generator
│   │       ├── WaitHelper.java     # Smart Waits (Explicit Waits)
│   │       ├── DataGenerator.java  # Generator dữ liệu ngẫu nhiên duy nhất (Zero hardcode)
│   │       └── ScreenshotUtil.java # Chụp ảnh màn hình tự động khi FAIL
│   └── test/java/com/anhtester/
│       ├── base/BaseTest.java      # Parent Test Class (@BeforeMethod / @AfterMethod)
│       └── tests/                  # Test Suites Execution
│           ├── login/LoginTest.java      # 21 Test Cases Automation Login
│           └── customer/CustomerTest.java # 26 Test Cases Automation Customers (DDT)
└── src/test/resources/
    ├── config.properties           # Cấu hình môi trường (URL, Browser, Wait Timeouts)
    └── testng.xml                  # File runner cấu hình Suite TestNG
```

---

## ⚡ HƯỚNG DẪN CHẠY TEST AUTOMATION (EXECUTION COMMANDS)

Luôn mở Terminal tại thư mục `selenium-java-framework` và sử dụng các lệnh Maven (có tiền tố `rtk` tối ưu token):

### 1. Kiểm tra biên dịch mã nguồn (Test Compile)
```bash
rtk mvn test-compile
```

### 2. Chạy toàn bộ Test Suites (Cả Login & Customers)
```bash
rtk mvn test
```

### 3. Chạy riêng từng Module:

* **Chạy riêng Module Login:**
  ```bash
  rtk mvn test -Dtest=LoginTest
  ```

* **Chạy riêng Module Customers:**
  ```bash
  rtk mvn test -Dtest=CustomerTest
  ```

### 4. Chạy theo Nhóm / Tags (TestNG Groups):

* **Chạy các test case Smoke / Critical:**
  ```bash
  rtk mvn test -Dgroups="smoke"
  ```

* **Chạy các test case thuộc nhóm High Risk:**
  ```bash
  rtk mvn test -Dgroups="high"
  ```

---

## 📊 XEM BÁO CÁO ALLURE REPORT

Sau khi chạy test xong, tạo và mở báo cáo đồ họa Allure Report bằng lệnh:

```bash
mvn allure:serve
```
