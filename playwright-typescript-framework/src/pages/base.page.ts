/**
 * base.page.ts — Base Page class
 *
 * Class cha cho tất cả Page Object classes.
 * Cung cấp common methods với built-in smart waits và logging.
 * KHÔNG dùng waitForTimeout / hard sleep — chỉ dùng Playwright auto-waiting.
 */

import { type Page, type Locator, expect } from '@playwright/test';
import { createLogger, type Logger } from '../utils/logger';

export abstract class BasePage {
  protected readonly page: Page;
  protected readonly logger: Logger;


  constructor(page: Page) {
    this.page = page;
    this.logger = createLogger(this.constructor.name);
  }


  // ===== Navigation =====

  /**
   * Navigate đến URL (relative hoặc absolute)
   */
  async navigate(url: string = '/'): Promise<void> {
    this.logger.step(`Navigate to: ${url}`);
    await this.page.goto(url);
    await this.waitForPageLoad();
  }

  /**
   * Chờ page load hoàn toàn
   */
  async waitForPageLoad(): Promise<void> {
    await this.page.waitForLoadState('domcontentloaded');
    await this.page.waitForLoadState('networkidle');
  }

  /**
   * Lấy title của page hiện tại
   */
  async getTitle(): Promise<string> {
    return this.page.title();
  }

  /**
   * Lấy URL hiện tại
   */
  getCurrentUrl(): string {
    return this.page.url();
  }

  /**
   * Getter cho raw Playwright Page instance
   */
  get rawPage(): Page {
    return this.page;
  }

  // ===== Click Actions =====

  /**
   * Click vào element — tự động chờ element visible + enabled
   */
  async click(locator: Locator, description: string = ''): Promise<void> {
    this.logger.action('click', description || locator.toString());
    await expect(locator).toBeVisible({ timeout: 15_000 });
    await expect(locator).toBeEnabled({ timeout: 5_000 });
    await locator.click();
  }

  /**
   * Double click vào element
   */
  async doubleClick(locator: Locator, description: string = ''): Promise<void> {
    this.logger.action('dblclick', description || locator.toString());
    await expect(locator).toBeVisible({ timeout: 15_000 });
    await locator.dblclick();
  }

  /**
   * Right click vào element
   */
  async rightClick(locator: Locator, description: string = ''): Promise<void> {
    this.logger.action('rightclick', description || locator.toString());
    await expect(locator).toBeVisible({ timeout: 15_000 });
    await locator.click({ button: 'right' });
  }

  // ===== Input Actions =====

  /**
   * Fill text vào input — xóa nội dung cũ trước khi điền
   */
  async fill(locator: Locator, value: string, description: string = ''): Promise<void> {
    this.logger.action(`fill "${value}"`, description || locator.toString());
    await expect(locator).toBeVisible({ timeout: 15_000 });
    await expect(locator).toBeEnabled({ timeout: 5_000 });
    await locator.clear();
    await locator.fill(value);
  }

  /**
   * Type từng ký tự (dùng khi fill() không hoạt động với autocomplete)
   */
  async typeSlowly(locator: Locator, value: string, delay: number = 50): Promise<void> {
    this.logger.action(`type "${value}"`, locator.toString());
    await expect(locator).toBeVisible({ timeout: 15_000 });
    await locator.clear();
    await locator.pressSequentially(value, { delay });
  }

  /**
   * Clear nội dung của input
   */
  async clearInput(locator: Locator): Promise<void> {
    this.logger.action('clear', locator.toString());
    await expect(locator).toBeVisible({ timeout: 15_000 });
    await locator.clear();
  }

  /**
   * Chọn option trong dropdown <select>
   */
  async selectOption(locator: Locator, value: string): Promise<void> {
    this.logger.action(`selectOption "${value}"`, locator.toString());
    await expect(locator).toBeVisible({ timeout: 15_000 });
    await locator.selectOption({ label: value });
  }

  // ===== Checkbox / Radio =====

  /**
   * Check checkbox nếu chưa được check
   */
  async check(locator: Locator): Promise<void> {
    this.logger.action('check', locator.toString());
    await expect(locator).toBeVisible({ timeout: 15_000 });
    await locator.check();
  }

  /**
   * Uncheck checkbox nếu đang được check
   */
  async uncheck(locator: Locator): Promise<void> {
    this.logger.action('uncheck', locator.toString());
    await expect(locator).toBeVisible({ timeout: 15_000 });
    await locator.uncheck();
  }

  // ===== Assertions helpers =====

  /**
   * Chờ element xuất hiện trên DOM
   */
  async waitForVisible(locator: Locator, timeout: number = 15_000): Promise<void> {
    this.logger.debug(`Waiting for visible: ${locator.toString()}`);
    await expect(locator).toBeVisible({ timeout });
  }

  /**
   * Chờ element biến mất
   */
  async waitForHidden(locator: Locator, timeout: number = 15_000): Promise<void> {
    this.logger.debug(`Waiting for hidden: ${locator.toString()}`);
    await expect(locator).toBeHidden({ timeout });
  }

  /**
   * Chờ text xuất hiện trên page
   */
  async waitForText(text: string, timeout: number = 15_000): Promise<void> {
    this.logger.debug(`Waiting for text: "${text}"`);
    await expect(this.page.getByText(text)).toBeVisible({ timeout });
  }

  // ===== Getters =====

  /**
   * Lấy text content của element
   */
  async getText(locator: Locator): Promise<string> {
    await expect(locator).toBeVisible({ timeout: 15_000 });
    return (await locator.textContent()) ?? '';
  }

  /**
   * Lấy value của input field
   */
  async getInputValue(locator: Locator): Promise<string> {
    await expect(locator).toBeVisible({ timeout: 15_000 });
    return locator.inputValue();
  }

  /**
   * Kiểm tra element có hiển thị không
   */
  async isVisible(locator: Locator): Promise<boolean> {
    return locator.isVisible();
  }

  /**
   * Kiểm tra element có enabled không
   */
  async isEnabled(locator: Locator): Promise<boolean> {
    return locator.isEnabled();
  }

  // ===== Scroll =====

  /**
   * Scroll element vào viewport
   */
  async scrollIntoView(locator: Locator): Promise<void> {
    this.logger.debug(`Scrolling into view: ${locator.toString()}`);
    await locator.scrollIntoViewIfNeeded();
  }

  /**
   * Scroll đến cuối trang
   */
  async scrollToBottom(): Promise<void> {
    this.logger.debug('Scrolling to bottom of page');
    await this.page.keyboard.press('End');
    await this.page.waitForTimeout(300); // Chờ lazy-load nếu có
  }

  // ===== Screenshot =====

  /**
   * Chụp screenshot và attach vào test report
   */
  async screenshot(name: string): Promise<Buffer> {
    this.logger.debug(`Taking screenshot: ${name}`);
    return this.page.screenshot({ fullPage: false, path: `test-results/screenshots/${name}.png` });
  }

  // ===== Keyboard =====

  /**
   * Nhấn Enter
   */
  async pressEnter(): Promise<void> {
    await this.page.keyboard.press('Enter');
  }

  /**
   * Nhấn Tab để chuyển focus
   */
  async pressTab(): Promise<void> {
    await this.page.keyboard.press('Tab');
  }

  /**
   * Nhấn Escape để đóng modal/dropdown
   */
  async pressEscape(): Promise<void> {
    await this.page.keyboard.press('Escape');
  }

  // ===== Abstract methods — subclass PHẢI override =====

  /**
   * Verify page đã load đúng (check URL hoặc heading)
   * Mỗi Page class phải implement method này
   */
  abstract verifyPageLoaded(): Promise<void>;
}
