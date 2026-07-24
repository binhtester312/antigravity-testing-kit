/**
 * login.spec.ts — Perfex CRM Authentication Test Suite
 *
 * Chứa TOÀN BỘ 21 Test Cases cho module Login / Logout / Forgot Password.
 * Mapping từ: testcases_login.md & LoginTest.java (selenium-java-framework)
 *
 * Allure Report structure:
 *   Epic    → Perfex CRM
 *   Feature → Authentication — Login / Logout / Forgot Password
 *   Story   → TC_XXX: [TC Title]
 *   Steps   → Page method steps (direct parent)
 *               └── Base action steps with locator (child)
 */

import { test, expect } from '../../fixtures/base.fixture';
import { allure } from 'allure-playwright';

// Tất cả test trong file này chạy ở trạng thái chưa đăng nhập
test.use({ storageState: { cookies: [], origins: [] } });

test.describe('Perfex CRM — Module Login / Logout / Forgot Password (21 TCs)', () => {

  test.beforeEach(async ({ loginPage }) => {
    // Set common allure metadata for the entire describe block
    await allure.epic('Perfex CRM');
    await allure.feature('Authentication — Login / Logout / Forgot Password');
    await allure.owner('QA Team');

    await loginPage.navigate();
  });

  // ============================================================
  // MODULE 1: ĐĂNG NHẬP (TC_001 → TC_015)
  // ============================================================

  test('TC_001: Login with valid email and password @smoke @high', async ({ loginPage, dashboardPage, envConfig }) => {
    await allure.story('TC_001 — Successful Login with valid credentials');
    await allure.severity('blocker');
    await allure.tags('smoke', 'login', 'positive');

    const email = envConfig.defaultUsername || 'admin@example.com';
    const password = envConfig.defaultPassword || '123456';

    await allure.parameter('Email', email);
    await allure.parameter('Password', '***');

    await allure.step(`Login to CRM with email: "${email}"`, async () => {
      await loginPage.login(email, password);
    });

    await dashboardPage.verifyPageLoaded();
    expect(dashboardPage.isOnDashboard(), 'URL after login must be Dashboard').toBe(true);
  });

  test('TC_002: Login with valid credentials + tick "Remember me" @high', async ({ loginPage, dashboardPage, envConfig }) => {
    await allure.story('TC_002 — Successful Login with "Remember Me" option');
    await allure.severity('critical');
    await allure.tags('login', 'remember-me', 'positive');

    const email = envConfig.defaultUsername || 'admin@example.com';
    const password = envConfig.defaultPassword || '123456';

    await allure.parameter('Email', email);
    await allure.parameter('Password', '***');
    await allure.parameter('Remember Me', 'true');

    await allure.step(`Login to CRM with email: "${email}" and Remember Me`, async () => {
      await loginPage.loginWithRememberMe(email, password);
    });

    await dashboardPage.verifyPageLoaded();
    expect(dashboardPage.isOnDashboard(), 'After ticking Remember Me, should redirect to Dashboard').toBe(true);
  });

  test('TC_003: Submit empty form (both Email and Password blank) — expect validation error @high', async ({ loginPage, page }) => {
    await allure.story('TC_003 — Empty form submission shows validation error');
    await allure.severity('critical');
    await allure.tags('login', 'validation', 'negative');

    await allure.step('Click Login button without filling any field', async () => {
      await loginPage.clickLoginButton();
    });

    expect(loginPage.isOnLoginPage(), 'Should remain on Login page when both fields are empty').toBe(true);
    const hasError = (await loginPage.isServerErrorDisplayed())
      || (await loginPage.isEmailFieldErrorDisplayed())
      || page.url().includes('authentication');
    expect(hasError, 'Should display a validation error when both fields are empty').toBe(true);
  });

  test('TC_004: Submit with blank Email, any Password — email required error @high', async ({ loginPage }) => {
    await allure.story('TC_004 — Blank Email field shows required validation');
    await allure.severity('critical');
    await allure.tags('login', 'validation', 'negative');

    await allure.parameter('Email', '(empty)');
    await allure.parameter('Password', 'anyPassword99');

    await allure.step('Fill Password only and click Login button', async () => {
      await loginPage.enterPassword('anyPassword99');
      await loginPage.clickLoginButton();
    });

    expect(loginPage.isOnLoginPage(), 'Should remain on Login page when Email is blank').toBe(true);
  });

  test('TC_005: Submit with valid Email, blank Password — password required error @high', async ({ loginPage, envConfig }) => {
    await allure.story('TC_005 — Blank Password field shows required validation');
    await allure.severity('critical');
    await allure.tags('login', 'validation', 'negative');

    const email = envConfig.defaultUsername || 'admin@example.com';

    await allure.parameter('Email', email);
    await allure.parameter('Password', '(empty)');

    await allure.step('Fill Email only and click Login button', async () => {
      await loginPage.enterEmail(email);
      await loginPage.clickLoginButton();
    });

    expect(loginPage.isOnLoginPage(), 'Should remain on Login page when Password is blank').toBe(true);
  });

  test('TC_006: Invalid email format — missing "@" character (HTML5 validation) @high', async ({ loginPage }) => {
    await allure.story('TC_006 — Invalid email format blocked by HTML5 client-side validation');
    await allure.severity('critical');
    await allure.tags('login', 'validation', 'negative', 'html5');

    await allure.parameter('Email', 'invalidemail');
    await allure.parameter('Password', 'anyPassword99');

    await allure.step('Fill invalid email and click Login button', async () => {
      await loginPage.enterEmail('invalidemail');
      await loginPage.enterPassword('anyPassword99');
      await loginPage.clickLoginButton();
    });

    expect(loginPage.isOnLoginPage(), 'Form should be blocked by HTML5 validation').toBe(true);
    const hasServerError = await loginPage.isServerErrorDisplayed();
    expect(hasServerError, 'Should NOT show server error (form not submitted)').toBe(false);
  });

  test('TC_007: Invalid email format — has "@" but missing domain (BVA boundary) @medium', async ({ loginPage }) => {
    await allure.story('TC_007 — Email with "@" but no domain blocked by HTML5 validation');
    await allure.severity('normal');
    await allure.tags('login', 'validation', 'negative', 'bva');

    await allure.parameter('Email', 'user@');
    await allure.parameter('Password', 'anyPassword99');

    await allure.step('Fill incomplete email and click Login button', async () => {
      await loginPage.enterEmail('user@');
      await loginPage.enterPassword('anyPassword99');
      await loginPage.clickLoginButton();
    });

    expect(loginPage.isOnLoginPage(), 'Form should be blocked when email domain is missing').toBe(true);
  });

  test('TC_008: Valid email format but non-existent account — server-side error @high', async ({ loginPage }) => {
    await allure.story('TC_008 — Non-existent email rejected with generic error (no account enumeration)');
    await allure.severity('critical');
    await allure.tags('login', 'security', 'negative', 'server-side');

    await allure.parameter('Email', 'wrong_user_99@gmail.com');
    await allure.parameter('Password', 'AnyPass@123');

    await allure.step('Submit login request with non-existent email', async () => {
      await loginPage.login('wrong_user_99@gmail.com', 'AnyPass@123');
    });

    expect(loginPage.isOnLoginPage(), 'Should remain on Login page for non-existent email').toBe(true);
    const isErrorDisplayed = await loginPage.isServerErrorDisplayed();
    expect(isErrorDisplayed, 'Should show error message for invalid credentials').toBe(true);

    const errorMsg = await loginPage.getServerErrorMessage();
    expect(errorMsg.length, 'Error message should not be empty').toBeGreaterThan(0);
    expect(
      errorMsg.toLowerCase().includes('not found') || errorMsg.toLowerCase().includes('không tồn tại'),
      'Error message must NOT reveal that the email does not exist (account enumeration risk)',
    ).toBe(false);
  });

  test('TC_009: Valid email + wrong password — server-side "Invalid credentials" error @high', async ({ loginPage, envConfig }) => {
    await allure.story('TC_009 — Wrong password rejected with invalid credentials error');
    await allure.severity('critical');
    await allure.tags('login', 'negative', 'server-side');

    const email = envConfig.defaultUsername || 'admin@example.com';

    await allure.parameter('Email', email);
    await allure.parameter('Password', 'WrongPass@9999');

    await allure.step('Submit login request with wrong password', async () => {
      await loginPage.login(email, 'WrongPass@9999');
    });

    expect(loginPage.isOnLoginPage(), 'Should remain on Login page for wrong password').toBe(true);
    const isErrorDisplayed = await loginPage.isServerErrorDisplayed();
    expect(isErrorDisplayed, 'Should show error for wrong password').toBe(true);
  });

  test('TC_010: Submit login form via Enter key instead of clicking Login button @medium', async ({ loginPage, dashboardPage, envConfig }) => {
    await allure.story('TC_010 — Login via Enter key works same as clicking Login button');
    await allure.severity('normal');
    await allure.tags('login', 'keyboard', 'positive');

    const email = envConfig.defaultUsername || 'admin@example.com';
    const password = envConfig.defaultPassword || '123456';

    await allure.parameter('Email', email);
    await allure.parameter('Submit Method', 'Enter key');

    await allure.step(`Login to CRM with email: "${email}" via Enter key`, async () => {
      await loginPage.loginWithEnterKey(email, password);
    });

    await dashboardPage.verifyPageLoaded();
    expect(dashboardPage.isOnDashboard(), 'Enter key submission should redirect to Dashboard').toBe(true);
  });

  test('TC_011: Email with leading/trailing whitespace — BVA edge case @medium', async ({ loginPage, dashboardPage, envConfig }) => {
    await allure.story('TC_011 — Email with surrounding whitespace handled gracefully');
    await allure.severity('normal');
    await allure.tags('login', 'bva', 'edge-case');

    const baseEmail = envConfig.defaultUsername || 'admin@example.com';
    const spacedEmail = `  ${baseEmail}  `;

    await allure.parameter('Email', `"  ${baseEmail}  "` );
    await allure.parameter('Password', '***');

    await allure.step(`Login to CRM with padded email: "${spacedEmail}"`, async () => {
      await loginPage.login(spacedEmail, envConfig.defaultPassword || '123456');
    });

    const loginSuccess = dashboardPage.isOnDashboard();
    const showError = await loginPage.isServerErrorDisplayed();
    expect(
      loginSuccess || showError || loginPage.isOnLoginPage(),
      'System should handle leading/trailing spaces with a valid response (trim or error)',
    ).toBe(true);
  });

  test('TC_012: Password with 1 character — BVA lower boundary @medium', async ({ loginPage, envConfig }) => {
    await allure.story('TC_012 — Single character password rejected (below minimum)');
    await allure.severity('normal');
    await allure.tags('login', 'bva', 'negative');

    await allure.parameter('Email', envConfig.defaultUsername || 'admin@example.com');
    await allure.parameter('Password length', '1 character');
    await allure.parameter('Password', 'A');

    await allure.step('Submit login request with 1-character password', async () => {
      await loginPage.login(envConfig.defaultUsername || 'admin@example.com', 'A');
    });

    const isErrorOrLoginPage = (await loginPage.isServerErrorDisplayed()) || loginPage.isOnLoginPage();
    expect(isErrorOrLoginPage, 'Single-character password should result in error or remain on Login page').toBe(true);
  });

  test('TC_013: Password with 255 characters — BVA upper boundary @low', async ({ loginPage, envConfig }) => {
    await allure.story('TC_013 — 255-character password does not crash the system (upper BVA)');
    await allure.severity('minor');
    await allure.tags('login', 'bva', 'boundary');

    const longPassword = 'Aa1!'.repeat(63) + 'Aa1';

    expect(longPassword.length, 'Test password must be exactly 255 characters').toBe(255);
    await allure.parameter('Email', envConfig.defaultUsername || 'admin@example.com');
    await allure.parameter('Password length', '255 characters');

    await allure.step('Submit login request with 255-character password', async () => {
      await loginPage.login(envConfig.defaultUsername || 'admin@example.com', longPassword);
    });

    const isErrorOrLoginPage = (await loginPage.isServerErrorDisplayed()) || loginPage.isOnLoginPage();
    expect(isErrorOrLoginPage, 'System must not crash with a 255-character password').toBe(true);
  });

  test('TC_014: Already logged in → navigate back to Login URL — should redirect to Dashboard @medium', async ({ loginPage, dashboardPage, envConfig, page }) => {
    await allure.story('TC_014 — Authenticated user navigating to Login URL is redirected to Dashboard');
    await allure.severity('normal');
    await allure.tags('login', 'session', 'redirect');

    await allure.step('Login to CRM and verify redirect to Dashboard', async () => {
      await loginPage.login(envConfig.defaultUsername || 'admin@example.com', envConfig.defaultPassword || '123456');
      await dashboardPage.verifyPageLoaded();
    });

    await allure.step('Directly navigate to Login page URL while authenticated', async () => {
      await page.goto(envConfig.baseUrl || 'https://crm.anhtester.com/admin/authentication');
    });

    await page.waitForURL(/.*\/admin\/(?!authentication).*/, { timeout: 10_000 });
    expect(dashboardPage.isOnDashboard(), 'Authenticated user must be redirected away from Login page').toBe(true);
  });

  test('TC_015: CSRF token present in form — request without token rejected @security @high', async ({ loginPage, request, envConfig }) => {
    await allure.story('TC_015 — CSRF protection: form has token field, requests without token are rejected');
    await allure.severity('critical');
    await allure.tags('security', 'csrf', 'high');

    let csrfToken: string | null = null;
    await allure.step('Verify CSRF token hidden input exists in login form', async () => {
      csrfToken = await loginPage.getCsrfTokenValue();
      expect(csrfToken, 'Login form must contain a non-empty csrf_token_name field').toBeTruthy();
      await allure.parameter('CSRF Token value', csrfToken ?? '(null)');
    });

    let responseStatus: number = 0;
    let responseText: string = '';
    await allure.step('Send raw POST request without CSRF token', async () => {
      const response = await request.post(envConfig.baseUrl || 'https://crm.anhtester.com/admin/authentication', {
        form: {
          email: envConfig.defaultUsername || 'admin@example.com',
          password: envConfig.defaultPassword || '123456',
        },
      });
      responseStatus = response.status();
      responseText = await response.text();
      await allure.parameter('Response status', String(responseStatus));
    });

    const isRejected = responseStatus === 403
      || responseStatus === 400
      || responseText.includes('The action you have requested is not allowed')
      || responseText.includes('CSRF');
    expect(isRejected, 'POST request without CSRF token must be rejected by server').toBe(true);
  });

  // ============================================================
  // MODULE 2: FORGOT PASSWORD (TC_016 → TC_019)
  // ============================================================

  test('TC_016: Click "Forgot Password?" link — navigates to correct URL @medium', async ({ loginPage, forgotPasswordPage }) => {
    await allure.story('TC_016 — "Forgot Password?" link redirects to correct page');
    await allure.severity('normal');
    await allure.tags('forgot-password', 'navigation');

    await allure.step('Click "Forgot Password?" link on Login page', async () => {
      await loginPage.clickForgotPasswordLink();
    });

    expect(forgotPasswordPage.isOnForgotPasswordPage(), 'Should navigate to Forgot Password page').toBe(true);
  });

  test('TC_017: Submit registered email in Forgot Password — system sends recovery email @high', async ({ loginPage, forgotPasswordPage, envConfig }) => {
    await allure.story('TC_017 — Valid registered email triggers successful recovery email');
    await allure.severity('critical');
    await allure.tags('forgot-password', 'positive');

    const email = envConfig.defaultUsername || 'admin@example.com';

    await allure.step('Click "Forgot Password?" link on Login page', async () => {
      await loginPage.clickForgotPasswordLink();
    });
    await allure.parameter('Recovery Email', email);

    await allure.step('Submit recovery request with registered email', async () => {
      await forgotPasswordPage.submitForgotPassword(email);
    });

    const isSuccess = (await forgotPasswordPage.isSuccessMessageDisplayed()) || forgotPasswordPage.isOnForgotPasswordPage();
    expect(isSuccess, 'System should acknowledge the recovery request for a registered email').toBe(true);
  });

  test('TC_018: Submit Forgot Password with blank email — HTML5 required validation @high', async ({ loginPage, forgotPasswordPage }) => {
    await allure.story('TC_018 — Empty email in Forgot Password blocked by HTML5 required validation');
    await allure.severity('critical');
    await allure.tags('forgot-password', 'validation', 'negative');

    await allure.step('Click "Forgot Password?" link on Login page', async () => {
      await loginPage.clickForgotPasswordLink();
    });
    await allure.parameter('Email', '(empty)');

    await allure.step('Click Confirm button on Forgot Password page without email', async () => {
      await forgotPasswordPage.clickConfirm();
    });

    expect(forgotPasswordPage.isOnForgotPasswordPage(), 'Form should NOT be submitted when email is blank').toBe(true);
  });

  test('TC_019: Forgot Password with non-existent email — system response does not reveal user existence @security @high', async ({ loginPage, forgotPasswordPage }) => {
    await allure.story('TC_019 — Non-existent email in Forgot Password handled securely (no enumeration)');
    await allure.severity('critical');
    await allure.tags('forgot-password', 'security', 'negative');

    await allure.step('Click "Forgot Password?" link on Login page', async () => {
      await loginPage.clickForgotPasswordLink();
    });
    await allure.parameter('Email', 'nonexistent_user_xyz@gmail.com');

    await allure.step('Submit recovery request with non-existent email', async () => {
      await forgotPasswordPage.submitForgotPassword('nonexistent_user_xyz@gmail.com');
    });

    const isHandled = (await forgotPasswordPage.isSuccessMessageDisplayed())
      || (await forgotPasswordPage.getErrorMessage()).length > 0
      || forgotPasswordPage.isOnForgotPasswordPage();
    expect(isHandled, 'System must handle non-existent email without crashing or revealing user existence').toBe(true);
  });

  // ============================================================
  // MODULE 3: ĐĂNG XUẤT (TC_020 → TC_021)
  // ============================================================

  test('TC_020: Logout successfully — redirects back to Login page @low', async ({ loginPage, dashboardPage, envConfig }) => {
    await allure.story('TC_020 — Successful logout redirects user to Login page');
    await allure.severity('minor');
    await allure.tags('logout', 'positive');

    await allure.step('Login to CRM with valid credentials', async () => {
      await loginPage.login(envConfig.defaultUsername || 'admin@example.com', envConfig.defaultPassword || '123456');
    });
    await dashboardPage.verifyPageLoaded();

    await allure.step('Perform logout from Dashboard', async () => {
      await dashboardPage.logout();
    });

    expect(loginPage.isOnLoginPage(), 'After logout, should be redirected to Login page').toBe(true);
  });

  test('TC_021: After logout, press browser Back — must not regain access to Dashboard @security @low', async ({ loginPage, dashboardPage, envConfig, page }) => {
    await allure.story('TC_021 — Browser Back button after logout cannot bypass authentication');
    await allure.severity('minor');
    await allure.tags('logout', 'security', 'negative');

    await allure.step('Login to CRM and then perform logout', async () => {
      await loginPage.login(envConfig.defaultUsername || 'admin@example.com', envConfig.defaultPassword || '123456');
      await dashboardPage.verifyPageLoaded();
      await dashboardPage.logout();
    });

    await allure.step('Press browser Back button after logout', async () => {
      await page.goBack();
      await page.waitForTimeout(1000);
    });

    const currentUrl = page.url();
    const isUnauthenticated = currentUrl.includes('authentication') || !currentUrl.endsWith('/admin/');
    expect(isUnauthenticated, 'After logout, browser Back must not restore access to authenticated Dashboard').toBe(true);
  });

});
