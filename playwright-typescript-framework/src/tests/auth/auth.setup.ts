/**
 * auth.setup.ts — Global Authentication Setup
 *
 * Chạy MỘT LẦN trước toàn bộ test suite để:
 * 1. Login vào ứng dụng
 * 2. Lưu storage state (cookies + localStorage)
 * 3. Tái sử dụng session trong tất cả các tests → không login lại mỗi test
 *
 * Được khai báo trong playwright.config.ts project 'setup'.
 */

import { test as setup, expect } from '@playwright/test';
import * as path from 'path';
import * as fs from 'fs';
import { EnvConfig } from '../../utils/env.config';
import { LoginPage } from '../../pages/login.page';
import { createLogger } from '../../utils/logger';

const logger = createLogger('AuthSetup');

// File lưu auth state — gitignored
const AUTH_FILE = path.join('playwright', '.auth', 'user.json');

setup('Authenticate and save session', async ({ page }) => {
  logger.info('Starting authentication setup...');
  logger.info(`Base URL: ${EnvConfig.baseUrl}`);
  logger.info(`Username: ${EnvConfig.defaultUsername}`);

  // Đảm bảo thư mục tồn tại
  const authDir = path.dirname(AUTH_FILE);
  if (!fs.existsSync(authDir)) {
    fs.mkdirSync(authDir, { recursive: true });
  }

  const loginPage = new LoginPage(page);

  // Navigate đến trang login
  await loginPage.navigate();

  // Thực hiện login
  await loginPage.loginWithDefaultCredentials();

  // Verify login thành công — REPLACE với assertion phù hợp với app của bạn
  // Option 1: Verify redirect ra khỏi trang login
  await expect(page, 'Should be redirected away from login page after successful authentication')
    .not.toHaveURL(/.*authentication.*/i, { timeout: 15_000 });

  // Option 2: Verify URL chứa /dashboard (uncomment nếu cần)
  // await expect(page).toHaveURL(/.*\/dashboard.*/i, { timeout: 15_000 });

  logger.info('Authentication successful. Saving storage state...');

  // Lưu authentication state
  await page.context().storageState({ path: AUTH_FILE });

  logger.info(`Auth state saved to: ${AUTH_FILE}`);
});
