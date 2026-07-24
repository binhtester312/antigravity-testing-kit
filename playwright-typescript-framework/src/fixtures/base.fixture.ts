/**
 * base.fixture.ts — Extended test fixtures
 *
 * Extends Playwright's base test with:
 * - Custom page fixtures (LoginPage, DashboardPage, ForgotPasswordPage)
 * - Automatic screenshot attachment for PASS tests (teardown)
 * - Video attachment when available
 */

import { test as base, expect } from '@playwright/test';
import { AllureHelper } from '../utils/allure.helper';
import { LoginPage } from '../pages/login.page';
import { DashboardPage } from '../pages/dashboard.page';
import { ForgotPasswordPage } from '../pages/forgot-password.page';
import { EnvConfig } from '../utils/env.config';
import { TestDataGenerator } from '../utils/test-data';

export type TestFixtures = {
  loginPage: LoginPage;
  dashboardPage: DashboardPage;
  forgotPasswordPage: ForgotPasswordPage;
  envConfig: typeof EnvConfig;
  testData: typeof TestDataGenerator;
};

export const test = base.extend<TestFixtures>({

  /**
   * Override built-in page fixture to add PASS-test screenshot teardown.
   * After each PASS test: captures a final-state screenshot and attaches to Allure.
   */
  page: async ({ page }, use, testInfo) => {
    await use(page);

    // Teardown: attach final screenshot for PASS tests
    if (testInfo.status === 'passed') {
      await AllureHelper.attachScreenshotToTestInfo(
        page,
        testInfo,
        '📸 Final State Screenshot (PASS)',
      );
    }
  },

  loginPage: async ({ page }, use) => {
    await use(new LoginPage(page));
  },

  dashboardPage: async ({ page }, use) => {
    await use(new DashboardPage(page));
  },

  forgotPasswordPage: async ({ page }, use) => {
    await use(new ForgotPasswordPage(page));
  },

  envConfig: async ({}, use) => {
    await use(EnvConfig);
  },

  testData: async ({}, use) => {
    await use(TestDataGenerator);
  },
});

export { expect };
