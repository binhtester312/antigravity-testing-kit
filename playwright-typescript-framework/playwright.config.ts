import { defineConfig, devices } from '@playwright/test';
import * as dotenv from 'dotenv';
import * as path from 'path';

// Load environment variables từ .env file
dotenv.config({ path: path.resolve(__dirname, '.env') });

const BASE_URL = process.env['BASE_URL'] ?? 'https://example.com';
const HEADLESS = process.env['HEADLESS'] !== 'false';
const DEFAULT_TIMEOUT = parseInt(process.env['DEFAULT_TIMEOUT'] ?? '30000', 10);
const NAVIGATION_TIMEOUT = parseInt(process.env['NAVIGATION_TIMEOUT'] ?? '60000', 10);
const TEST_RETRY_COUNT = parseInt(process.env['TEST_RETRY_COUNT'] ?? '1', 10);

export default defineConfig({
  // ===== Test Discovery =====
  testDir: './src/tests',
  testMatch: '**/*.spec.ts',

  // ===== Timeouts =====
  timeout: DEFAULT_TIMEOUT,
  expect: {
    timeout: 10_000,
  },

  // ===== Execution =====
  fullyParallel: true,
  forbidOnly: !!process.env['CI'],
  retries: process.env['CI'] ? TEST_RETRY_COUNT : 0,
  workers: process.env['CI'] ? 4 : 1,

  // ===== Reporting =====
  reporter: [
    ['html', { outputFolder: 'playwright-report', open: 'never' }],
    ['list'],
    ['json', { outputFile: 'test-results/results.json' }],
  ],

  // ===== Shared Test Settings =====
  use: {
    baseURL: BASE_URL,
    headless: HEADLESS,
    viewport: { width: 1920, height: 1080 },
    ignoreHTTPSErrors: true,
    actionTimeout: 15_000,
    navigationTimeout: NAVIGATION_TIMEOUT,

    // Artifacts on failure
    screenshot: 'only-on-failure',
    video: 'retain-on-failure',
    trace: 'retain-on-failure',
  },

  // ===== Projects (Browsers) =====
  projects: [
    // --- Setup Project (Authentication) ---
    {
      name: 'setup',
      testMatch: '**/auth.setup.ts',
    },

    // --- Chromium (Default) ---
    {
      name: 'chromium',
      use: {
        ...devices['Desktop Chrome'],
        viewport: { width: 1920, height: 1080 },
        storageState: 'playwright/.auth/user.json',
      },
      dependencies: ['setup'],
    },

    // --- Firefox ---
    {
      name: 'firefox',
      use: {
        ...devices['Desktop Firefox'],
        viewport: { width: 1920, height: 1080 },
        storageState: 'playwright/.auth/user.json',
      },
      dependencies: ['setup'],
    },
  ],

  // ===== Output Directories =====
  outputDir: 'test-results/',
});
