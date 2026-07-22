/**
 * env.config.ts — Centralized environment configuration reader
 *
 * Đọc tất cả biến môi trường từ .env và export dưới dạng typed config object.
 * Tất cả config phải lấy từ đây — KHÔNG hardcode trong test hoặc page.
 */

import * as dotenv from 'dotenv';
import * as path from 'path';

// Load .env từ root project
dotenv.config({ path: path.resolve(process.cwd(), '.env') });


function optionalEnv(name: string, defaultValue: string): string {
  return process.env[name] ?? defaultValue;
}

export const EnvConfig = {
  // URLs
  baseUrl: optionalEnv('BASE_URL', 'https://example.com'),
  apiBaseUrl: optionalEnv('API_BASE_URL', 'https://api.example.com'),

  // Environment
  env: optionalEnv('ENV', 'staging') as 'dev' | 'staging' | 'prod',

  // Credentials
  defaultUsername: optionalEnv('DEFAULT_USERNAME', ''),
  defaultPassword: optionalEnv('DEFAULT_PASSWORD', ''),

  // Browser
  browser: optionalEnv('BROWSER', 'chromium') as 'chromium' | 'firefox' | 'webkit',
  headless: optionalEnv('HEADLESS', 'true') !== 'false',

  // Timeouts
  defaultTimeout: parseInt(optionalEnv('DEFAULT_TIMEOUT', '30000'), 10),
  navigationTimeout: parseInt(optionalEnv('NAVIGATION_TIMEOUT', '60000'), 10),

  // Retry
  retryCount: parseInt(optionalEnv('TEST_RETRY_COUNT', '1'), 10),
} as const;

export type EnvConfigType = typeof EnvConfig;
