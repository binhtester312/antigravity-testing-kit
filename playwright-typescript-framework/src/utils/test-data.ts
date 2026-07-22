/**
 * test-data.ts — Test data generator
 *
 * Sinh test data unique, traceable, và random cho automation tests.
 * Format: prefix_timestamp để dễ truy vết khi test fail.
 */

/**
 * Sinh timestamp hiện tại dạng số nguyên (giây Unix)
 */
function timestamp(): number {
  return Math.floor(Date.now() / 1000);
}

/**
 * Sinh chuỗi ngẫu nhiên N ký tự (alphanumeric)
 */
function randomString(length: number = 6): string {
  const chars = 'abcdefghijklmnopqrstuvwxyz0123456789';
  let result = '';
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return result;
}

export const TestDataGenerator = {
  /**
   * Sinh email unique và traceable
   * Format: auto_{testName}_{timestamp}@test.local
   * @example TestDataGenerator.email('login') → auto_login_1712049200@test.local
   */
  email(prefix: string = 'user'): string {
    return `auto_${prefix}_${timestamp()}@test.local`;
  },

  /**
   * Sinh username unique
   * @example TestDataGenerator.username('admin') → auto_admin_1712049200
   */
  username(prefix: string = 'user'): string {
    return `auto_${prefix}_${timestamp()}`;
  },

  /**
   * Sinh password ngẫu nhiên thỏa mãn độ phức tạp thông thường
   */
  password(): string {
    return `Test@${randomString(4).toUpperCase()}${randomString(4)}${timestamp() % 1000}`;
  },

  /**
   * Sinh số điện thoại Việt Nam (định dạng 0xxxxxxxxx)
   */
  phone(): string {
    const prefixes = ['090', '091', '093', '094', '096', '097', '098', '032', '033', '034', '035', '036', '037', '038', '039'];
    const prefix = prefixes[Math.floor(Math.random() * prefixes.length)] as string;
    const suffix = String(Math.floor(Math.random() * 10_000_000)).padStart(7, '0');
    return `${prefix}${suffix}`;
  },

  /**
   * Sinh full name ngẫu nhiên tiếng Anh
   */
  fullName(): string {
    const firstNames = ['Alice', 'Bob', 'Charlie', 'Diana', 'Eve', 'Frank', 'Grace', 'Henry'];
    const lastNames = ['Smith', 'Johnson', 'Williams', 'Brown', 'Jones', 'Garcia', 'Miller'];
    const first = firstNames[Math.floor(Math.random() * firstNames.length)] as string;
    const last = lastNames[Math.floor(Math.random() * lastNames.length)] as string;
    return `${first} ${last}`;
  },

  /**
   * Sinh ID ngẫu nhiên (dùng cho test case code)
   * @example TestDataGenerator.testId('LOGIN') → TC_LOGIN_1712049200
   */
  testId(module: string = 'TEST'): string {
    return `TC_${module.toUpperCase()}_${timestamp()}`;
  },

  /**
   * Sinh số nguyên random trong khoảng [min, max]
   */
  randomInt(min: number, max: number): number {
    return Math.floor(Math.random() * (max - min + 1)) + min;
  },

  /**
   * Lấy ngày hôm nay theo format YYYY-MM-DD
   */
  todayDate(): string {
    return new Date().toISOString().split('T')[0] as string;
  },

  /**
   * Lấy ngày trong tương lai N ngày từ hôm nay
   */
  futureDate(daysFromNow: number): string {
    const date = new Date();
    date.setDate(date.getDate() + daysFromNow);
    return date.toISOString().split('T')[0] as string;
  },
};
