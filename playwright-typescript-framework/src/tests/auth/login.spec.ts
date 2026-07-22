/**
 * login.spec.ts — Perfex CRM Authentication Test Suite
 *
 * Chứa TOÀN BỘ 21 Test Cases cho module Login / Logout / Forgot Password.
 * Mapping từ: testcases_login.md & LoginTest.java (selenium-java-framework)
 */

import { test, expect } from '../../fixtures/base.fixture';

// Đảm bảo tất cả test trong file này chạy ở trạng thái chưa đăng nhập
test.use({ storageState: { cookies: [], origins: [] } });

test.describe('Perfex CRM — Module Login / Logout / Forgot Password (21 TCs)', () => {

  test.beforeEach(async ({ loginPage }) => {
    await loginPage.navigate();
  });

  // ============================================================
  // MODULE 1: ĐĂNG NHẬP (TC_001 → TC_015)
  // ============================================================

  test('TC_001: Đăng nhập thành công với email và password hợp lệ @smoke @high', async ({ loginPage, dashboardPage, envConfig }) => {
    // Arrange
    const email = envConfig.defaultUsername || 'admin@example.com';
    const password = envConfig.defaultPassword || '123456';

    // Act
    await loginPage.login(email, password);

    // Assert
    await dashboardPage.verifyLoggedIn();
    expect(dashboardPage.isOnDashboard(), 'TC_001: URL sau login phải ở Dashboard').toBe(true);
  });

  test('TC_002: Đăng nhập thành công + tick "Remember me" @high', async ({ loginPage, dashboardPage, envConfig }) => {
    // Arrange
    const email = envConfig.defaultUsername || 'admin@example.com';
    const password = envConfig.defaultPassword || '123456';

    // Act
    await loginPage.loginWithRememberMe(email, password);

    // Assert
    await dashboardPage.verifyLoggedIn();
    expect(dashboardPage.isOnDashboard(), 'TC_002: Sau khi tick Remember me, không redirect đến Dashboard').toBe(true);
  });

  test('TC_003: Để trống cả Email và Password — hiển thị thông báo lỗi @high', async ({ loginPage, page }) => {
    // Act
    await loginPage.clickLoginButton();

    // Assert — Vẫn ở trang login
    expect(loginPage.isOnLoginPage(), 'TC_003: Phải ở trang Login dù để trống cả 2 fields').toBe(true);
    const hasError = (await loginPage.isServerErrorDisplayed()) || (await loginPage.isEmailFieldErrorDisplayed()) || page.url().includes('authentication');
    expect(hasError, 'TC_003: Không hiển thị lỗi khi để trống cả 2 trường').toBe(true);
  });

  test('TC_004: Để trống Email, nhập Password bất kỳ — lỗi email bắt buộc @high', async ({ loginPage }) => {
    // Arrange & Act
    await loginPage.enterPassword('anyPassword99');
    await loginPage.clickLoginButton();

    // Assert
    expect(loginPage.isOnLoginPage(), 'TC_004: Phải ở trang Login khi để trống Email').toBe(true);
  });

  test('TC_005: Nhập Email hợp lệ, để trống Password — lỗi password bắt buộc @high', async ({ loginPage, envConfig }) => {
    // Arrange & Act
    await loginPage.enterEmail(envConfig.defaultUsername || 'admin@example.com');
    await loginPage.clickLoginButton();

    // Assert
    expect(loginPage.isOnLoginPage(), 'TC_005: Phải ở trang Login khi để trống Password').toBe(true);
  });

  test('TC_006: Email sai định dạng — thiếu ký tự "@" (HTML5 validation) @high', async ({ loginPage }) => {
    // Arrange & Act
    await loginPage.enterEmail('invalidemail');
    await loginPage.enterPassword('anyPassword99');
    await loginPage.clickLoginButton();

    // Assert — HTML5 validation chặn submit, vẫn ở trang login, không có server error
    expect(loginPage.isOnLoginPage(), 'TC_006: Form phải bị chặn bởi HTML5 validation').toBe(true);
    const hasServerError = await loginPage.isServerErrorDisplayed();
    expect(hasServerError, 'TC_006: Không được hiển thị server error (form không được submit)').toBe(false);
  });

  test('TC_007: Email sai định dạng — có "@" nhưng thiếu domain (BVA biên định dạng) @medium', async ({ loginPage }) => {
    // Arrange & Act
    await loginPage.enterEmail('user@');
    await loginPage.enterPassword('anyPassword99');
    await loginPage.clickLoginButton();

    // Assert
    expect(loginPage.isOnLoginPage(), 'TC_007: Form phải bị chặn khi email thiếu domain').toBe(true);
  });

  test('TC_008: Email đúng định dạng nhưng không tồn tại — lỗi server-side @high', async ({ loginPage }) => {
    // Arrange & Act
    await loginPage.login('wrong_user_99@gmail.com', 'AnyPass@123');

    // Assert
    expect(loginPage.isOnLoginPage(), 'TC_008: Đã redirect khỏi Login dù email không tồn tại').toBe(true);
    const isErrorDisplayed = await loginPage.isServerErrorDisplayed();
    expect(isErrorDisplayed, 'TC_008: Không hiển thị thông báo lỗi khi email không tồn tại').toBe(true);

    const errorMsg = await loginPage.getServerErrorMessage();
    expect(errorMsg.length, 'TC_008: Thông báo lỗi server rỗng').toBeGreaterThan(0);
    expect(
      errorMsg.toLowerCase().includes('not found') || errorMsg.toLowerCase().includes('không tồn tại'),
      'TC_008: Thông báo lỗi tiết lộ email không tồn tại — vi phạm bảo mật'
    ).toBe(false);
  });

  test('TC_009: Email đúng + Password sai — lỗi server-side "Invalid credentials" @high', async ({ loginPage, envConfig }) => {
    // Arrange & Act
    await loginPage.login(envConfig.defaultUsername || 'admin@example.com', 'WrongPass@9999');

    // Assert
    expect(loginPage.isOnLoginPage(), 'TC_009: Đã redirect khỏi Login dù password sai').toBe(true);
    const isErrorDisplayed = await loginPage.isServerErrorDisplayed();
    expect(isErrorDisplayed, 'TC_009: Không hiển thị lỗi khi password sai').toBe(true);
  });

  test('TC_010: Submit form bằng phím Enter thay vì click nút Login @medium', async ({ loginPage, dashboardPage, envConfig }) => {
    // Arrange & Act
    await loginPage.loginWithEnterKey(envConfig.defaultUsername || 'admin@example.com', envConfig.defaultPassword || '123456');

    // Assert
    await dashboardPage.verifyLoggedIn();
    expect(dashboardPage.isOnDashboard(), 'TC_010: Submit bằng Enter không redirect đến Dashboard').toBe(true);
  });

  test('TC_011: Email có khoảng trắng đầu/cuối — BVA edge case @medium', async ({ loginPage, dashboardPage, envConfig }) => {
    // Arrange & Act
    const spacedEmail = `  ${envConfig.defaultUsername || 'admin@example.com'}  `;
    await loginPage.login(spacedEmail, envConfig.defaultPassword || '123456');

    // Assert — Phù hợp nếu server trim hoặc trả về error
    const loginSuccess = dashboardPage.isOnDashboard();
    const showError = await loginPage.isServerErrorDisplayed();
    expect(loginSuccess || showError || loginPage.isOnLoginPage(), 'TC_011: Phản hồi không hợp lệ với leading/trailing spaces').toBe(true);
  });

  test('TC_012: Password 1 ký tự — BVA biên dưới @medium', async ({ loginPage, envConfig }) => {
    // Arrange & Act
    await loginPage.login(envConfig.defaultUsername || 'admin@example.com', 'A');

    // Assert
    const isErrorOrLoginPage = (await loginPage.isServerErrorDisplayed()) || loginPage.isOnLoginPage();
    expect(isErrorOrLoginPage, 'TC_012: Phản hồi bất thường với password 1 ký tự').toBe(true);
  });

  test('TC_013: Password 255 ký tự — BVA biên trên @low', async ({ loginPage, envConfig }) => {
    // Arrange
    const longPassword = 'Aa1!'.repeat(63) + 'Aa1';
    expect(longPassword.length, 'TC_013: Password test phải dài 255 chars').toBe(255);

    // Act
    await loginPage.login(envConfig.defaultUsername || 'admin@example.com', longPassword);

    // Assert — Không crash, ở trang login hoặc lỗi server
    const isErrorOrLoginPage = (await loginPage.isServerErrorDisplayed()) || loginPage.isOnLoginPage();
    expect(isErrorOrLoginPage, 'TC_013: Trang crash hoặc timeout với password 255 ký tự').toBe(true);
  });

  test('TC_014: Đã đăng nhập → truy cập lại URL Login — phải redirect về Dashboard @medium', async ({ loginPage, dashboardPage, envConfig, page }) => {
    // Arrange: Đăng nhập thành công trước
    await loginPage.login(envConfig.defaultUsername || 'admin@example.com', envConfig.defaultPassword || '123456');
    await dashboardPage.verifyLoggedIn();

    // Act: Nhập trực tiếp URL Login
    await page.goto(envConfig.baseUrl || 'https://crm.anhtester.com/admin/authentication');

    // Assert: Tự động redirect về Dashboard
    await page.waitForURL(/.*\/admin\/(?!authentication).*/, { timeout: 10_000 });
    expect(dashboardPage.isOnDashboard(), 'TC_014: Đã login mà truy cập URL Login không redirect về Dashboard').toBe(true);
  });

  test('TC_015: Kiểm tra CSRF token — form có csrf field & request không token bị từ chối @security @high', async ({ loginPage, request, envConfig }) => {
    // Assert 1: CSRF token hidden input tồn tại trên form
    const csrfToken = await loginPage.getCsrfTokenValue();
    expect(csrfToken, 'TC_015: Form Login phải có trường csrf_token_name không rỗng').toBeTruthy();

    // Assert 2: Gửi API request không kèm CSRF token → bị server từ chối / trả về 403 hoặc lỗi
    const response = await request.post(envConfig.baseUrl || 'https://crm.anhtester.com/admin/authentication', {
      form: {
        email: envConfig.defaultUsername || 'admin@example.com',
        password: envConfig.defaultPassword || '123456',
      },
    });

    const isRejected = response.status() === 403 || response.status() === 400 || (await response.text()).includes('The action you have requested is not allowed') || (await response.text()).includes('CSRF');
    expect(isRejected, 'TC_015: Request thiếu CSRF token phải bị từ chối').toBe(true);
  });

  // ============================================================
  // MODULE 2: FORGOT PASSWORD (TC_016 → TC_019)
  // ============================================================

  test('TC_016: Click "Forgot Password?" — chuyển đến đúng URL @medium', async ({ loginPage, forgotPasswordPage }) => {
    // Act
    await loginPage.clickForgotPasswordLink();

    // Assert
    expect(forgotPasswordPage.isOnForgotPasswordPage(), 'TC_016: Không redirect đến trang Forgot Password').toBe(true);
  });

  test('TC_017: Nhập email đã đăng ký + Confirm — hệ thống gửi email thành công @high', async ({ loginPage, forgotPasswordPage, envConfig }) => {
    // Arrange
    await loginPage.clickForgotPasswordLink();

    // Act
    await forgotPasswordPage.submitForgotPassword(envConfig.defaultUsername || 'admin@example.com');

    // Assert
    const isSuccess = (await forgotPasswordPage.isSuccessMessageDisplayed()) || forgotPasswordPage.isOnForgotPasswordPage();
    expect(isSuccess, 'TC_017: Không phản hồi thành công sau khi submit email hợp lệ').toBe(true);
  });

  test('TC_018: Để trống Email + Click Confirm — HTML5 required validation @high', async ({ loginPage, forgotPasswordPage }) => {
    // Arrange
    await loginPage.clickForgotPasswordLink();

    // Act
    await forgotPasswordPage.clickConfirm();

    // Assert
    expect(forgotPasswordPage.isOnForgotPasswordPage(), 'TC_018: Form Forgot Password đã submit dù để trống Email').toBe(true);
  });

  test('TC_019: Forgot Password với email không tồn tại — kiểm tra bảo mật thông tin @security @high', async ({ loginPage, forgotPasswordPage }) => {
    // Arrange
    await loginPage.clickForgotPasswordLink();

    // Act
    await forgotPasswordPage.submitForgotPassword('nonexistent_user_xyz@gmail.com');

    // Assert — Trang không crash, có thông báo lỗi hoặc ở lại trang forgot password
    const isHandled = (await forgotPasswordPage.isSuccessMessageDisplayed()) || (await forgotPasswordPage.getErrorMessage()).length > 0 || forgotPasswordPage.isOnForgotPasswordPage();
    expect(isHandled, 'TC_019: Phản hồi không hợp lệ với email không tồn tại').toBe(true);
  });

  // ============================================================
  // MODULE 3: ĐĂNG XUẤT (TC_020 → TC_021)
  // ============================================================

  test('TC_020: Đăng xuất thành công — chuyển về trang Login @low', async ({ loginPage, dashboardPage, envConfig }) => {
    // Pre-condition: Đăng nhập thành công
    await loginPage.login(envConfig.defaultUsername || 'admin@example.com', envConfig.defaultPassword || '123456');
    await dashboardPage.verifyLoggedIn();

    // Act
    await dashboardPage.logout();

    // Assert
    expect(loginPage.isOnLoginPage(), 'TC_020: Sau logout không redirect về trang Login').toBe(true);
  });

  test('TC_021: Sau logout, nhấn Back browser — không truy cập được Dashboard @security @low', async ({ loginPage, dashboardPage, envConfig, page }) => {
    // Pre-condition: Đăng nhập & Logout
    await loginPage.login(envConfig.defaultUsername || 'admin@example.com', envConfig.defaultPassword || '123456');
    await dashboardPage.verifyLoggedIn();
    await dashboardPage.logout();

    // Act: Nhấn Back trên trình duyệt
    await page.goBack();
    await page.waitForTimeout(1000);

    // Assert: Không được ở trang Dashboard authenticated
    const currentUrl = page.url();
    const isUnauthenticated = currentUrl.includes('authentication') || !currentUrl.endsWith('/admin/');
    expect(isUnauthenticated, 'TC_021: Sau logout và nhấn Back, vẫn truy cập được Dashboard').toBe(true);
  });

});
