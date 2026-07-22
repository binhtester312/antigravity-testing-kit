/**
 * base.fixture.ts — Extended test fixtures
 */

import { test as base, expect } from '@playwright/test';
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
