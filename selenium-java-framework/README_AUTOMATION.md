# Perfex CRM — Selenium 4 Web Automation Framework

[![Selenium](https://img.shields.io/badge/Selenium-4.27.0-43B02A?logo=selenium)](https://www.selenium.dev/)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk)](https://openjdk.org/)
[![TestNG](https://img.shields.io/badge/TestNG-7.10.2-orange)](https://testng.org/)
[![Allure](https://img.shields.io/badge/Allure-2.29.1-blue)](https://allurereport.org/)
[![CI](https://github.com/binhtester312/antigravity-testing-kit/actions/workflows/selenium.yml/badge.svg)](https://github.com/binhtester312/antigravity-testing-kit/actions)

## 📋 Tổng quan

Automation framework chuyên nghiệp cho **Perfex CRM** (crm.anhtester.com), xây dựng theo
**Page Object Model (POM)** với đầy đủ reporting, logging và CI/CD.

**Hệ thống được test:** https://crm.anhtester.com  
**Module hiện tại:** Login / Forgot Password / Logout (21 TCs)

---

## 🏗️ Cấu trúc Project

```
perfexcrm-selenium-automation/
├── pom.xml                          # Maven config — dependencies + plugins
├── .env.example                     # Template cấu hình (copy → config.properties)
├── .gitignore
├── README.md
│
├── src/main/java/com/anhtester/
│   ├── config/
│   │   └── ConfigReader.java        # Đọc config — hỗ trợ env override cho CI/CD
│   ├── driver/
│   │   └── DriverFactory.java       # Factory + ThreadLocal WebDriver
│   ├── pages/                       # Page Object Model
│   │   ├── BasePage.java            # Common methods (click, type, wait...)
│   │   ├── LoginPage.java
│   │   ├── ForgotPasswordPage.java
│   │   └── DashboardPage.java
│   └── utils/
│       ├── WaitHelper.java          # Smart waits (ExpectedConditions)
│       ├── AllureUtils.java         # Screenshot + Allure attachments
│       └── TestDataGenerator.java   # Sinh data unique + traceable
│
├── src/main/resources/
│   └── log4j2.xml                   # Log4j 2 — console + file rolling
│
├── src/test/java/com/anhtester/
│   ├── base/
│   │   └── BaseTest.java            # Setup/Teardown + screenshot on fail
│   └── tests/login/
│       └── LoginTest.java           # 20 automated TCs
│
├── src/test/resources/
│   ├── config.properties            # Cấu hình local (không commit!)
│   ├── allure.properties
│   └── testng.xml                   # TestNG suite — nhóm theo priority
│
├── test-data/
│   └── users.json                   # External test data
│
└── .github/workflows/
    └── selenium.yml                 # GitHub Actions CI/CD
```

---

## ⚙️ Yêu cầu hệ thống

| Công cụ | Phiên bản tối thiểu | Kiểm tra |
|---------|---------------------|---------|
| Java JDK | 17+ | `java -version` |
| Maven | 3.8+ | `mvn -version` |
| Google Chrome | Mới nhất | Tự động qua WebDriverManager |
| Allure CLI | 2.x (tùy chọn) | `allure --version` |

---

## 🚀 Cài đặt & Chạy

### 1. Clone repo

```bash
git clone https://github.com/binhtester312/antigravity-testing-kit.git
cd antigravity-testing-kit
```

### 2. Cấu hình

```bash
# Copy template → config thực tế
cp .env.example src/test/resources/config.properties

# Mở file và điền credentials thực
# vi src/test/resources/config.properties
```

Chỉnh sửa `config.properties`:
```properties
base.url=https://crm.anhtester.com/admin/authentication
admin.email=your_admin@email.com
admin.password=your_password
browser=chrome
headless=false
```

### 3. Compile

```bash
mvn clean compile test-compile
```

### 4. Chạy test

```bash
# Chạy toàn bộ test suite
mvn clean test

# Chỉ chạy HIGH priority tests
mvn clean test -Dgroups=high

# Chạy trên Firefox
mvn clean test -Dbrowser=firefox

# Chạy headless (không mở browser)
mvn clean test -Dheadless=true

# Chạy 1 test cụ thể
mvn clean test -Dtest=LoginTest#TC_001_loginSuccessWithValidCredentials
```

### 5. Xem Allure Report

```bash
# Sinh report HTML
mvn allure:report

# Mở report trên browser (tự động)
mvn allure:serve
```

---

## 📊 Test Suite — Module Login

| Priority | Số TCs | Nhóm |
|----------|--------|------|
| 🔴 HIGH | 11 TCs | TC_001~009, TC_017~019 |
| 🟡 MEDIUM | 6 TCs | TC_007, TC_010, TC_011, TC_012, TC_014, TC_016 |
| 🟢 LOW | 3 TCs | TC_013, TC_020, TC_021 |
| ⚫ SKIP | 1 TC | TC_015 (CSRF — manual only) |
| **TOTAL** | **20 TCs** | |

---

## 🔧 Cấu hình nâng cao

### Chạy với browser khác

```bash
# Firefox
mvn test -Dbrowser=firefox

# Edge
mvn test -Dbrowser=edge
```

### Override config qua System properties

```bash
mvn test -Dbase.url=https://staging.example.com -Dadmin.email=test@test.com
```

### Cài Allure CLI để xem report offline

```bash
# macOS
brew install allure

# Sau khi chạy test
allure serve target/allure-results
```

---

## 🏛️ Design Principles

| Nguyên tắc | Áp dụng |
|---|---|
| **Page Object Model** | Mỗi page → 1 class, locators khai báo trong page |
| **ThreadLocal Driver** | DriverFactory đảm bảo parallel test an toàn |
| **Smart Waits Only** | KHÔNG có `Thread.sleep()` — chỉ dùng `ExpectedConditions` |
| **Config over Code** | Tất cả env config trong `config.properties` |
| **Fail Fast, Log Rich** | Screenshot tự động khi FAIL, Log4j2 mọi action |
| **Unique Test Data** | Email/username luôn có timestamp để tránh conflict |

---

## 🤝 Đóng góp

1. Fork repo
2. Tạo branch: `git checkout -b feat/ten-tinh-nang`
3. Commit với message rõ ràng (tiếng Việt OK)
4. Push và tạo Pull Request

---

## 📞 Liên hệ

- **Author:** Anh Tester
- **Community:** [Cộng đồng Tester Việt Nam](https://github.com/binhtester312)
- **Kit:** Antigravity Testing Kit
