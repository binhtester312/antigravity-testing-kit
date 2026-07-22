/**
 * helpers.ts — Common helper functions
 *
 * Các hàm tiện ích dùng chung trong toàn bộ framework.
 * KHÔNG chứa logic test — chỉ chứa pure utility functions.
 */

import * as fs from 'fs';
import * as path from 'path';

/**
 * Đọc và parse JSON file từ thư mục test-data/
 * @example loadTestData<User[]>('users.json')
 */
export function loadTestData<T>(fileName: string): T {
  const filePath = path.resolve(process.cwd(), 'test-data', fileName);
  if (!fs.existsSync(filePath)) {
    throw new Error(`[Helpers] Test data file not found: ${filePath}`);
  }
  const rawData = fs.readFileSync(filePath, 'utf-8');
  return JSON.parse(rawData) as T;
}

/**
 * Pause execution — CHỈ dùng cho debug, KHÔNG dùng trong production test
 * @deprecated Dùng Playwright expect() smart waits thay thế
 */
export async function debugPause(ms: number): Promise<void> {
  if (process.env['CI']) {
    throw new Error('[Helpers] debugPause() không được phép sử dụng trong CI environment');
  }
  return new Promise((resolve) => setTimeout(resolve, ms));
}

/**
 * Format currency số Việt Nam
 * @example formatCurrency(1500000) → '1.500.000 đ'
 */
export function formatCurrency(amount: number, currency: string = 'đ'): string {
  return `${amount.toLocaleString('vi-VN')} ${currency}`;
}

/**
 * Parse số từ chuỗi có format tiền tệ hoặc ký tự đặc biệt
 * @example parseNumber('1.500.000 đ') → 1500000
 */
export function parseNumber(value: string): number {
  const cleaned = value.replace(/[^\d]/g, '');
  const parsed = parseInt(cleaned, 10);
  if (isNaN(parsed)) {
    throw new Error(`[Helpers] Cannot parse number from value: "${value}"`);
  }
  return parsed;
}

/**
 * Chờ điều kiện thỏa mãn với polling (dùng khi Playwright expect() không đủ)
 * @example await waitForCondition(() => items.length > 0, 5000)
 */
export async function waitForCondition(
  condition: () => boolean | Promise<boolean>,
  timeoutMs: number = 10_000,
  intervalMs: number = 500
): Promise<void> {
  const startTime = Date.now();
  while (Date.now() - startTime < timeoutMs) {
    if (await condition()) return;
    await new Promise((resolve) => setTimeout(resolve, intervalMs));
  }
  throw new Error(`[Helpers] Condition not met within ${timeoutMs}ms`);
}

/**
 * Retry async function N lần với delay giữa các lần
 */
export async function retryAsync<T>(
  fn: () => Promise<T>,
  retries: number = 3,
  delayMs: number = 1000
): Promise<T> {
  let lastError: unknown;
  for (let attempt = 1; attempt <= retries; attempt++) {
    try {
      return await fn();
    } catch (error) {
      lastError = error;
      if (attempt < retries) {
        await new Promise((resolve) => setTimeout(resolve, delayMs));
      }
    }
  }
  throw lastError;
}

/**
 * Truncate chuỗi để log ngắn gọn
 */
export function truncate(text: string, maxLength: number = 100): string {
  return text.length > maxLength ? `${text.substring(0, maxLength)}...` : text;
}
