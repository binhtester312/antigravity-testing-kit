/**
 * auth.fixture.ts — Authentication fixture
 *
 * Quản lý authentication state cho toàn bộ test suite.
 * Dùng Playwright storageState để tái sử dụng session giữa các tests,
 * tránh login lại mỗi test → tiết kiệm thời gian chạy.
 *
 * Playwright sẽ chạy auth.setup.ts MỘT LẦN trước tất cả tests,
 * sau đó load storageState vào mỗi test context.
 */

import { test as base, expect } from '@playwright/test';


import { EnvConfig } from '../utils/env.config';
import { createLogger } from '../utils/logger';

const logger = createLogger('AuthFixture');

export type AuthFixtures = {
  authenticatedPage: {
    username: string;
    isLoggedIn: boolean;
  };
};

/**
 * Auth-extended test — dùng khi test cần verify authentication state
 */
export const authenticatedTest = base.extend<AuthFixtures>({
  authenticatedPage: async ({ page }, use) => {
    // LoginPage is available but not directly accessed here — session state
    // is already loaded from storageState via playwright.config.ts
    const currentUrl = page.url();
    const isOnLoginPage = currentUrl.includes('/login') || currentUrl.includes('/signin');

    logger.info(`Authentication state check: ${isOnLoginPage ? 'On login page' : 'Authenticated'}`);

    await use({
      username: EnvConfig.defaultUsername,
      isLoggedIn: !isOnLoginPage,
    });
  },
});

export { expect };
