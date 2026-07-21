package com.anhtester.tests.login;

import com.anhtester.base.BaseTest;
import com.anhtester.config.ConfigReader;
import com.anhtester.pages.DashboardPage;
import com.anhtester.pages.ForgotPasswordPage;
import com.anhtester.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

/**
 * LoginTest — Bộ test case cho module Login/Logout/ForgotPassword
 * Hệ thống: Perfex CRM — https://crm.anhtester.com
 * Mapping: testcases_login.md (21 TCs — TC_015 skip vì cần manual)
 */
@Epic("Perfex CRM")
@Feature("Xác thực người dùng (Authentication)")
public class LoginTest extends BaseTest {

    // ============================================================
    // MODULE: ĐĂNG NHẬP (TC_001 → TC_015)
    // ============================================================

    @Test(description = "TC_001: Đăng nhập thành công với email và password hợp lệ",
          groups = {"login", "smoke", "high"})
    @Story("Đăng nhập")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Happy Path: Đăng nhập thành công → redirect Dashboard.\n" +
                 "Pre-condition: Tài khoản tồn tại, chưa đăng nhập.")
    public void TC_001_loginSuccessWithValidCredentials() {
        LoginPage loginPage = new LoginPage();
        loginPage.login(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword());

        DashboardPage dashboardPage = new DashboardPage();
        Assert.assertTrue(dashboardPage.isOnDashboard(),
            "TC_001 FAIL: URL sau login không chứa '/admin/'. URL hiện tại: "
            + dashboardPage.getCurrentUrl());
    }

    @Test(description = "TC_002: Đăng nhập thành công + tick 'Remember me'",
          groups = {"login", "high"})
    @Story("Đăng nhập")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Tick 'Remember me' rồi login → kiểm tra redirect thành công.\n" +
                 "[A-04] Giả định: phiên duy trì 30 ngày (không thể tự động verify sau đóng browser).")
    public void TC_002_loginSuccessWithRememberMe() {
        LoginPage loginPage = new LoginPage();
        loginPage.loginWithRememberMe(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword());

        DashboardPage dashboardPage = new DashboardPage();
        Assert.assertTrue(dashboardPage.isOnDashboard(),
            "TC_002 FAIL: Sau khi tick Remember me, không redirect đến Dashboard.");
    }

    @Test(description = "TC_003: Để trống cả Email và Password — hiển thị 2 thông báo lỗi",
          groups = {"login", "high"})
    @Story("Đăng nhập")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Negative: Để trống cả 2 field rồi click Login → phải hiện đồng thời 2 lỗi required.")
    public void TC_003_loginWithBothFieldsEmpty() {
        LoginPage loginPage = new LoginPage();
        loginPage.clickLoginButton();

        // Kiểm tra lỗi Email
        Assert.assertTrue(loginPage.isEmailErrorDisplayed() || loginPage.isServerErrorDisplayed(),
            "TC_003 FAIL: Không hiển thị lỗi khi để trống Email.");

        // Kiểm tra lỗi Password
        Assert.assertTrue(loginPage.isPasswordErrorDisplayed() || loginPage.isServerErrorDisplayed(),
            "TC_003 FAIL: Không hiển thị lỗi khi để trống Password.");

        // Phải vẫn ở trang Login
        Assert.assertTrue(loginPage.isOnLoginPage(),
            "TC_003 FAIL: Đã rời khỏi trang Login dù để trống cả 2 fields.");
    }

    @Test(description = "TC_004: Để trống Email, nhập Password bất kỳ — lỗi email bắt buộc",
          groups = {"login", "high"})
    @Story("Đăng nhập")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Negative: Email rỗng + Password có giá trị → lỗi 'Email required', không lỗi Password.")
    public void TC_004_loginWithEmptyEmail() {
        LoginPage loginPage = new LoginPage();
        loginPage.enterPassword("anyPassword99");
        loginPage.clickLoginButton();

        Assert.assertTrue(loginPage.isEmailErrorDisplayed() || loginPage.isServerErrorDisplayed(),
            "TC_004 FAIL: Không hiện lỗi khi để trống Email.");
        Assert.assertTrue(loginPage.isOnLoginPage(),
            "TC_004 FAIL: Đã rời khỏi trang Login dù để trống Email.");
    }

    @Test(description = "TC_005: Nhập Email hợp lệ, để trống Password — lỗi password bắt buộc",
          groups = {"login", "high"})
    @Story("Đăng nhập")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Negative: Email hợp lệ + Password rỗng → lỗi 'Password required'.")
    public void TC_005_loginWithEmptyPassword() {
        LoginPage loginPage = new LoginPage();
        loginPage.enterEmail(ConfigReader.getAdminEmail());
        loginPage.clickLoginButton();

        Assert.assertTrue(loginPage.isPasswordErrorDisplayed() || loginPage.isServerErrorDisplayed(),
            "TC_005 FAIL: Không hiện lỗi khi để trống Password.");
        Assert.assertTrue(loginPage.isOnLoginPage(),
            "TC_005 FAIL: Đã rời khỏi trang Login dù để trống Password.");
    }

    @Test(description = "TC_006: Email sai định dạng — thiếu ký tự '@' (HTML5 validation)",
          groups = {"login", "high"})
    @Story("Đăng nhập")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Negative: Email không có '@' → trình duyệt chặn form, không submit lên server.")
    public void TC_006_loginWithEmailMissingAtSign() {
        LoginPage loginPage = new LoginPage();
        loginPage.enterEmail("invalidemail");
        loginPage.enterPassword("anyPassword99");
        loginPage.clickLoginButton();

        // HTML5 validation: form không submit → vẫn ở trang Login
        Assert.assertTrue(loginPage.isOnLoginPage(),
            "TC_006 FAIL: Form đã submit dù email thiếu '@'. Trình duyệt lẽ ra phải chặn.");
        // Không có lỗi server-side
        Assert.assertFalse(loginPage.isServerErrorDisplayed(),
            "TC_006 FAIL: Có lỗi server-side — tức form đã được submit (HTML5 validation không hoạt động).");
    }

    @Test(description = "TC_007: Email sai định dạng — có '@' nhưng thiếu domain (BVA biên định dạng)",
          groups = {"login", "medium"})
    @Story("Đăng nhập")
    @Severity(SeverityLevel.NORMAL)
    @Description("BVA: Email 'user@' có '@' nhưng không có domain → HTML5 validation chặn form.")
    public void TC_007_loginWithEmailMissingDomain() {
        LoginPage loginPage = new LoginPage();
        loginPage.enterEmail("user@");
        loginPage.enterPassword("anyPassword99");
        loginPage.clickLoginButton();

        Assert.assertTrue(loginPage.isOnLoginPage(),
            "TC_007 FAIL: Form đã submit dù email thiếu domain sau '@'.");
    }

    @Test(description = "TC_008: Email đúng định dạng nhưng không tồn tại — lỗi server-side",
          groups = {"login", "high"})
    @Story("Đăng nhập")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Negative: Email không tồn tại → server trả về thông báo lỗi chung (không tiết lộ account).")
    public void TC_008_loginWithNonExistentEmail() {
        LoginPage loginPage = new LoginPage();
        loginPage.login("wrong_user_99@gmail.com", "AnyPass@123");

        Assert.assertTrue(loginPage.isServerErrorDisplayed(),
            "TC_008 FAIL: Không hiển thị thông báo lỗi khi email không tồn tại.");
        Assert.assertTrue(loginPage.isOnLoginPage(),
            "TC_008 FAIL: Đã redirect khỏi trang Login dù thông tin sai.");

        String errorMsg = loginPage.getServerErrorMessage();
        Assert.assertFalse(errorMsg.isBlank(),
            "TC_008 FAIL: Thông báo lỗi server rỗng.");
        // Thông báo phải là generic, không tiết lộ "email không tồn tại"
        Assert.assertFalse(errorMsg.toLowerCase().contains("not found") ||
                           errorMsg.toLowerCase().contains("không tồn tại"),
            "TC_008 FAIL: Thông báo lỗi tiết lộ email không tồn tại — vi phạm bảo mật.");
    }

    @Test(description = "TC_009: Email đúng + Password sai — lỗi server-side 'Invalid credentials'",
          groups = {"login", "high"})
    @Story("Đăng nhập")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Negative: Email hợp lệ nhưng password sai → server trả về lỗi chung.")
    public void TC_009_loginWithWrongPassword() {
        LoginPage loginPage = new LoginPage();
        loginPage.login(ConfigReader.getAdminEmail(), "WrongPass@9999");

        Assert.assertTrue(loginPage.isServerErrorDisplayed(),
            "TC_009 FAIL: Không hiển thị lỗi khi password sai.");
        Assert.assertTrue(loginPage.isOnLoginPage(),
            "TC_009 FAIL: Đã redirect khỏi Login dù password sai.");
    }

    @Test(description = "TC_010: Submit form bằng phím Enter thay vì click nút Login",
          groups = {"login", "medium"})
    @Story("Đăng nhập")
    @Severity(SeverityLevel.NORMAL)
    @Description("Alternate Path: Nhấn Enter thay vì click Login → form vẫn submit và redirect Dashboard.\n" +
                 "[A-08] Giả định: form hỗ trợ Enter.")
    public void TC_010_loginByPressingEnterKey() {
        LoginPage loginPage = new LoginPage();
        loginPage.loginWithEnterKey(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword());

        DashboardPage dashboardPage = new DashboardPage();
        Assert.assertTrue(dashboardPage.isOnDashboard(),
            "TC_010 FAIL: Submit bằng Enter không redirect đến Dashboard.\n[A-08] Form có thể không hỗ trợ Enter.");
    }

    @Test(description = "TC_011: Email có khoảng trắng đầu/cuối — BVA edge case",
          groups = {"login", "medium"})
    @Story("Đăng nhập")
    @Severity(SeverityLevel.NORMAL)
    @Description("BVA: Email '  admin@example.com  ' (2 spaces đầu, 2 cuối).\n" +
                 "[A-03] Nếu server trim → login thành công; nếu không trim → lỗi invalid credentials.")
    public void TC_011_loginWithEmailHavingLeadingTrailingSpaces() {
        LoginPage loginPage = new LoginPage();
        loginPage.login("  " + ConfigReader.getAdminEmail() + "  ", ConfigReader.getAdminPassword());

        // Hai kết quả đều hợp lệ theo [A-03] — test verify một trong hai
        boolean loginSuccess = new DashboardPage().isOnDashboard();
        boolean showError = loginPage.isServerErrorDisplayed();

        Assert.assertTrue(loginSuccess || showError,
            "TC_011 FAIL: Hệ thống không phản hồi hợp lệ với email có leading/trailing spaces.");
    }

    @Test(description = "TC_012: Password 1 ký tự — BVA biên dưới",
          groups = {"login", "medium"})
    @Story("Đăng nhập")
    @Severity(SeverityLevel.NORMAL)
    @Description("BVA lower bound: Password chỉ 1 ký tự 'A'.\n" +
                 "[A-02] Không có min length → form submit, server trả lỗi invalid credentials.")
    public void TC_012_loginWithSingleCharPassword() {
        LoginPage loginPage = new LoginPage();
        loginPage.login(ConfigReader.getAdminEmail(), "A");

        // Không có client-side validation về length → phải submit lên server
        // Kết quả mong đợi: lỗi server-side (password sai, không phải length error)
        Assert.assertTrue(loginPage.isServerErrorDisplayed() || loginPage.isOnLoginPage(),
            "TC_012 FAIL: Unexpected behavior với password 1 ký tự.");

        // Không nên bị chặn bởi client-side
        Assert.assertFalse(loginPage.isPasswordErrorDisplayed(),
            "TC_012 FAIL: Có client-side validation về độ dài password — vi phạm [A-02].");
    }

    @Test(description = "TC_013: Password 255 ký tự — BVA biên trên",
          groups = {"login", "low"})
    @Story("Đăng nhập")
    @Severity(SeverityLevel.MINOR)
    @Description("BVA upper bound: Password dài 255 ký tự.\n" +
                 "[A-02] Form không crash, không timeout, server trả lỗi bình thường.")
    public void TC_013_loginWith255CharPassword() {
        // Tạo password 255 ký tự: lặp "Aa1!" (4 chars) x 63 = 252, thêm "Aa1" = 255
        String longPassword = "Aa1!".repeat(63) + "Aa1";
        Assert.assertEquals(longPassword.length(), 255, "Độ dài password test phải là 255");

        LoginPage loginPage = new LoginPage();
        loginPage.login(ConfigReader.getAdminEmail(), longPassword);

        // Không crash, không timeout
        Assert.assertTrue(loginPage.isServerErrorDisplayed() || loginPage.isOnLoginPage(),
            "TC_013 FAIL: Hệ thống crash hoặc không phản hồi với password 255 ký tự.");
    }

    @Test(description = "TC_014: Đã đăng nhập → truy cập lại URL Login — phải redirect về Dashboard",
          groups = {"login", "medium"})
    @Story("Đăng nhập")
    @Severity(SeverityLevel.NORMAL)
    @Description("State Transition: Người dùng đã login → gõ URL login → hệ thống redirect về Dashboard.\n" +
                 "[A-07] Giả định: đã login mà truy cập auth URL → redirect Dashboard.")
    public void TC_014_loggedInUserAccessLoginUrl() {
        // Pre-condition: đăng nhập thành công
        performValidLogin();

        // Bây giờ navigate thủ công về URL login
        LoginPage loginPage = new LoginPage();
        loginPage.openUrl(ConfigReader.getBaseUrl());

        // [A-07]: Hệ thống phải redirect về Dashboard, không hiển thị form login lại
        DashboardPage dashboardPage = new DashboardPage();
        Assert.assertTrue(dashboardPage.isOnDashboard(),
            "TC_014 FAIL: Đã đăng nhập nhưng truy cập URL Login không redirect về Dashboard.\n" +
            "URL hiện tại: " + dashboardPage.getCurrentUrl() +
            "\n[A-07] cần được PO/BA xác nhận.");
    }

    @Test(description = "TC_015: Kiểm tra CSRF token — request thiếu token bị server từ chối (NFR-01)",
          groups = {"login", "security", "high"},
          enabled = false) // Skip: cần thực hiện manual qua Postman/DevTools
    @Story("Đăng nhập")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Security NFR-01: POST đến /admin/authentication không kèm csrf_token → HTTP 403.\n" +
                 "TC này cần thực hiện MANUAL bằng Postman/cURL. Đã disable trong automated suite.")
    @Ignore("TC_015: Manual test — thực hiện qua Postman, không tự động hóa được bằng Selenium.")
    public void TC_015_csrfProtectionCheck() {
        // Không implement — cần thực hiện bằng Postman
        // Xem mô tả trong testcases_login.md: CRM_LOGIN_TC_015
    }

    // ============================================================
    // MODULE: FORGOT PASSWORD (TC_016 → TC_019)
    // ============================================================

    @Test(description = "TC_016: Click 'Forgot Password?' — chuyển đến đúng URL",
          groups = {"forgot-password", "medium"})
    @Story("Quên mật khẩu")
    @Severity(SeverityLevel.NORMAL)
    @Description("Happy Path: Click link 'Forgot Password?' → redirect đến trang forgot_password.")
    public void TC_016_clickForgotPasswordLink() {
        LoginPage loginPage = new LoginPage();
        loginPage.clickForgotPasswordLink();

        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage();
        Assert.assertTrue(forgotPasswordPage.isOnForgotPasswordPage(),
            "TC_016 FAIL: Không redirect đến trang Forgot Password.\nURL: "
            + forgotPasswordPage.getCurrentUrl());
    }

    @Test(description = "TC_017: Nhập email đã đăng ký + Confirm — hệ thống gửi email thành công",
          groups = {"forgot-password", "high"})
    @Story("Quên mật khẩu")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Happy Path: Nhập email hợp lệ đã đăng ký → hệ thống xác nhận đã gửi email reset.")
    public void TC_017_forgotPasswordWithRegisteredEmail() {
        LoginPage loginPage = new LoginPage();
        loginPage.clickForgotPasswordLink();

        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage();
        forgotPasswordPage.submitForgotPassword(ConfigReader.getAdminEmail());

        Assert.assertTrue(forgotPasswordPage.isSuccessMessageDisplayed(),
            "TC_017 FAIL: Không hiển thị thông báo thành công sau khi submit email hợp lệ.\n" +
            "URL: " + forgotPasswordPage.getCurrentUrl());
    }

    @Test(description = "TC_018: Để trống Email + Click Confirm — HTML5 required validation",
          groups = {"forgot-password", "high"})
    @Story("Quên mật khẩu")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Negative: Forgot Password form để trống → HTML5 validation chặn submit.")
    public void TC_018_forgotPasswordWithEmptyEmail() {
        LoginPage loginPage = new LoginPage();
        loginPage.clickForgotPasswordLink();

        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage();
        forgotPasswordPage.clickConfirm(); // Không nhập email

        // HTML5 required → vẫn ở trang forgot_password
        Assert.assertTrue(forgotPasswordPage.isOnForgotPasswordPage(),
            "TC_018 FAIL: Form Forgot Password đã submit dù để trống Email.");
    }

    @Test(description = "TC_019: Forgot Password với email không tồn tại — kiểm tra bảo mật thông tin",
          groups = {"forgot-password", "security", "high"})
    @Story("Quên mật khẩu")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Security [A-06]: Email không tồn tại → hệ thống KHÔNG tiết lộ 'email không tồn tại'.\n" +
                 "Thông báo phải là generic để tránh user enumeration attack.")
    public void TC_019_forgotPasswordWithNonExistentEmail() {
        LoginPage loginPage = new LoginPage();
        loginPage.clickForgotPasswordLink();

        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage();
        forgotPasswordPage.submitForgotPassword("nonexistent_user_xyz@gmail.com");

        // [A-06]: Phải hiện thông báo generic (không nói "email không tồn tại")
        boolean hasSuccessMsg = forgotPasswordPage.isSuccessMessageDisplayed();
        String errorMsg = forgotPasswordPage.getErrorMessage();

        // Không được tiết lộ thông tin account
        if (!errorMsg.isBlank()) {
            Assert.assertFalse(
                errorMsg.toLowerCase().contains("not found") ||
                errorMsg.toLowerCase().contains("không tồn tại") ||
                errorMsg.toLowerCase().contains("does not exist"),
                "TC_019 FAIL [Bảo mật]: Thông báo lỗi tiết lộ email không tồn tại — vi phạm [A-06].\n" +
                "Thông báo: " + errorMsg
            );
        }

        // Một trong hai: hiển thị success message (thông báo chung) HOẶC không crash
        Assert.assertTrue(hasSuccessMsg || forgotPasswordPage.isOnForgotPasswordPage(),
            "TC_019 FAIL: Hệ thống không phản hồi hợp lệ với email không tồn tại.");
    }

    // ============================================================
    // MODULE: ĐĂNG XUẤT (TC_020 → TC_021)
    // ============================================================

    @Test(description = "TC_020: Đăng xuất thành công — chuyển về trang Login",
          groups = {"logout", "low"})
    @Story("Đăng xuất")
    @Severity(SeverityLevel.NORMAL)
    @Description("State Transition: Đang ở Dashboard → Click Logout → redirect về trang Login.")
    public void TC_020_logoutSuccessfully() {
        // Pre-condition: đăng nhập thành công
        performValidLogin();

        DashboardPage dashboardPage = new DashboardPage();
        Assert.assertTrue(dashboardPage.isOnDashboard(),
            "TC_020 PRE-CONDITION FAIL: Không vào được Dashboard để thực hiện logout.");

        dashboardPage.clickLogout();

        LoginPage loginPage = new LoginPage();
        Assert.assertTrue(loginPage.isOnLoginPage(),
            "TC_020 FAIL: Sau khi logout không redirect về trang Login.\n" +
            "URL hiện tại: " + loginPage.getCurrentUrl());
    }

    @Test(description = "TC_021: Sau logout, nhấn Back browser — không truy cập được Dashboard",
          groups = {"logout", "security", "low"})
    @Story("Đăng xuất")
    @Severity(SeverityLevel.NORMAL)
    @Description("State Transition + Security: Sau logout → nhấn Back → hệ thống không cho vào Dashboard.\n" +
                 "Session cookie phải bị hủy sau khi logout.")
    public void TC_021_afterLogoutBrowserBackNotAllowed() {
        // Pre-condition: đăng nhập và đăng xuất
        performValidLogin();
        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage.clickLogout();

        // Nhấn nút Back của trình duyệt
        com.anhtester.driver.DriverManager.getDriver().navigate().back();


        // Chờ trang load
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String currentUrl = dashboardPage.getCurrentUrl();
        LoginPage loginPage = new LoginPage();

        // Sau Back: phải ở trang Login hoặc redirect về Login (không được ở Dashboard)
        Assert.assertFalse(
            currentUrl.contains("/admin/") && !currentUrl.contains("authentication"),
            "TC_021 FAIL: Sau khi logout và nhấn Back, vẫn truy cập được Dashboard.\n" +
            "URL: " + currentUrl + "\nSession cookie chưa bị hủy đúng cách."
        );
    }
}
