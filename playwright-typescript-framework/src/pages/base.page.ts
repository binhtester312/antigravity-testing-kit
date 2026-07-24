/**
 * base.page.ts — Base Page class
 *
 * Class cha cho tất cả Page Object classes.
 * Cung cấp common methods với built-in smart waits, logging, và Allure step integration.
 *
 * Allure step hierarchy:
 *   Page class method (parent step)
 *     └── Base action (child step) ← includes locator + value
 *
 * KHÔNG dùng waitForTimeout / hard sleep — chỉ dùng Playwright auto-waiting.
 */

import { type Page, type Locator, expect } from '@playwright/test';
import { allure } from 'allure-playwright';
import { createLogger, type Logger } from '../utils/logger';

export abstract class BasePage {
  protected readonly page: Page;
  protected readonly logger: Logger;

  constructor(page: Page) {
    this.page = page;
    this.logger = createLogger(this.constructor.name);
  }

  // ===== Selector Helper =====

  /**
   * Trích xuất selector thân thiện (ví dụ "#email", "button[type='submit']")
   * từ chuỗi đại diện của Playwright Locator
   */
  protected getSelectorString(locator: Locator): string {
    const locStr = locator.toString();
    // Playwright format: "Locator@#email" hoặc "Locator@css=#email"
    const matchAt = locStr.match(/Locator@(?:css=)?(.*)/);
    if (matchAt && matchAt[1]) {
      return matchAt[1];
    }
    // Fallback nếu dùng locator('...')
    const matchQuotes = locStr.match(/locator\(['"](.*?)['"]\)/);
    if (matchQuotes && matchQuotes[1]) {
      return matchQuotes[1];
    }
    return locStr;
  }

  // ===== Navigation =====

  /**
   * Navigate đến URL (relative hoặc absolute)
   * Creates an Allure child step with the destination URL.
   */
  async navigate(url: string = '/'): Promise<void> {
    await allure.step(`Navigate to: "${url}"`, async () => {
      this.logger.step(`Navigate to: ${url}`);
      await this.page.goto(url);
      await this.waitForPageLoad();
    });
  }

  /**
   * Chờ page load hoàn toàn (internal — not wrapped in allure step)
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
   * Click vào element — tự động chờ element visible + enabled.
   */
  async click(locator: Locator, description: string = ''): Promise<void> {
    const locStr = locator.toString();
    const selector = this.getSelectorString(locator);
    const label = description || 'element';
    const stepName = `Click ${label} on selector "${selector}"`;

    await allure.step(stepName, async () => {
      this.logger.action('click', description || locStr);
      await expect(locator).toBeVisible({ timeout: 15_000 });
      await expect(locator).toBeEnabled({ timeout: 5_000 });
      await locator.click();
    });
  }

  /**
   * Double click vào element.
   */
  async doubleClick(locator: Locator, description: string = ''): Promise<void> {
    const locStr = locator.toString();
    const selector = this.getSelectorString(locator);
    const label = description || 'element';
    const stepName = `Double-click ${label} on selector "${selector}"`;

    await allure.step(stepName, async () => {
      this.logger.action('dblclick', description || locStr);
      await expect(locator).toBeVisible({ timeout: 15_000 });
      await locator.dblclick();
    });
  }

  /**
   * Right click vào element.
   */
  async rightClick(locator: Locator, description: string = ''): Promise<void> {
    const locStr = locator.toString();
    const selector = this.getSelectorString(locator);
    const label = description || 'element';
    const stepName = `Right-click ${label} on selector "${selector}"`;

    await allure.step(stepName, async () => {
      this.logger.action('rightclick', description || locStr);
      await expect(locator).toBeVisible({ timeout: 15_000 });
      await locator.click({ button: 'right' });
    });
  }

  // ===== Input Actions =====

  /**
   * Fill text vào input — xóa nội dung cũ trước khi điền.
   */
  async fill(locator: Locator, value: string, description: string = ''): Promise<void> {
    const locStr = locator.toString();
    const selector = this.getSelectorString(locator);
    const isPasswordField = description.toLowerCase().includes('password') || selector.includes('password') || locStr.includes('password');
    const displayValue = isPasswordField ? '"••••••••"' : `"${value}"`;

    let stepName = `Enter value: ${displayValue} into input "${selector}"`;
    if (isPasswordField) {
      stepName = `Enter password: ${displayValue} into input "${selector}"`;
    } else if (description.toLowerCase().includes('email') || selector.includes('email') || locStr.includes('email')) {
      stepName = `Enter email address: ${displayValue} into input "${selector}"`;
    } else if (description) {
      stepName = `Enter ${description.toLowerCase()}: ${displayValue} into input "${selector}"`;
    }

    await allure.step(stepName, async () => {
      this.logger.action(`fill "${isPasswordField ? '[MASKED]' : value}"`, description || locStr);
      await expect(locator).toBeVisible({ timeout: 15_000 });
      await expect(locator).toBeEnabled({ timeout: 5_000 });
      await locator.clear();
      await locator.fill(value);
    });
  }

  /**
   * Type từng ký tự (dùng khi fill() không hoạt động với autocomplete).
   */
  async typeSlowly(locator: Locator, value: string, delay: number = 50): Promise<void> {
    const locStr = locator.toString();
    const selector = this.getSelectorString(locator);
    const stepName = `Type "${value}" slowly into "${selector}"`;

    await allure.step(stepName, async () => {
      this.logger.action(`type "${value}"`, locStr);
      await expect(locator).toBeVisible({ timeout: 15_000 });
      await locator.clear();
      await locator.pressSequentially(value, { delay });
    });
  }

  /**
   * Clear nội dung của input (internal helper — not wrapped in allure step)
   */
  async clearInput(locator: Locator): Promise<void> {
    this.logger.action('clear', locator.toString());
    await expect(locator).toBeVisible({ timeout: 15_000 });
    await locator.clear();
  }

  /**
   * Chọn option trong dropdown <select>.
   */
  async selectOption(locator: Locator, value: string): Promise<void> {
    const locStr = locator.toString();
    const selector = this.getSelectorString(locator);
    const stepName = `Select option "${value}" from dropdown "${selector}"`;

    await allure.step(stepName, async () => {
      this.logger.action(`selectOption "${value}"`, locStr);
      await expect(locator).toBeVisible({ timeout: 15_000 });
      await locator.selectOption({ label: value });
    });
  }

  // ===== Checkbox / Radio =====

  /**
   * Check checkbox nếu chưa được check.
   */
  async check(locator: Locator): Promise<void> {
    const locStr = locator.toString();
    const selector = this.getSelectorString(locator);
    const stepName = `Check checkbox on selector "${selector}"`;

    await allure.step(stepName, async () => {
      this.logger.action('check', locStr);
      await expect(locator).toBeVisible({ timeout: 15_000 });
      await locator.check();
    });
  }

  /**
   * Uncheck checkbox nếu đang được check.
   */
  async uncheck(locator: Locator): Promise<void> {
    const locStr = locator.toString();
    const selector = this.getSelectorString(locator);
    const stepName = `Uncheck checkbox on selector "${selector}"`;

    await allure.step(stepName, async () => {
      this.logger.action('uncheck', locStr);
      await expect(locator).toBeVisible({ timeout: 15_000 });
      await locator.uncheck();
    });
  }

  // ===== Assertions helpers (internal — not wrapped in allure step) =====

  async waitForVisible(locator: Locator, timeout: number = 15_000): Promise<void> {
    this.logger.debug(`Waiting for visible: ${locator.toString()}`);
    await expect(locator).toBeVisible({ timeout });
  }

  async waitForHidden(locator: Locator, timeout: number = 15_000): Promise<void> {
    this.logger.debug(`Waiting for hidden: ${locator.toString()}`);
    await expect(locator).toBeHidden({ timeout });
  }

  async waitForText(text: string, timeout: number = 15_000): Promise<void> {
    this.logger.debug(`Waiting for text: "${text}"`);
    await expect(this.page.getByText(text)).toBeVisible({ timeout });
  }

  // ===== Getters (not wrapped — query methods) =====

  async getText(locator: Locator): Promise<string> {
    await expect(locator).toBeVisible({ timeout: 15_000 });
    return (await locator.textContent()) ?? '';
  }

  async getInputValue(locator: Locator): Promise<string> {
    await expect(locator).toBeVisible({ timeout: 15_000 });
    return locator.inputValue();
  }

  async isVisible(locator: Locator): Promise<boolean> {
    return locator.isVisible();
  }

  async isEnabled(locator: Locator): Promise<boolean> {
    return locator.isEnabled();
  }

  // ===== Scroll (not wrapped — utility methods) =====

  async scrollIntoView(locator: Locator): Promise<void> {
    this.logger.debug(`Scrolling into view: ${locator.toString()}`);
    await locator.scrollIntoViewIfNeeded();
  }

  async scrollToBottom(): Promise<void> {
    this.logger.debug('Scrolling to bottom of page');
    await this.page.keyboard.press('End');
    await this.page.waitForTimeout(300);
  }

  // ===== Screenshot =====

  /**
   * Chụp screenshot và attach vào Allure report.
   * Child allure step: includes screenshot name.
   */
  async screenshot(name: string): Promise<Buffer> {
    this.logger.debug(`Taking screenshot: ${name}`);
    return this.page.screenshot({ fullPage: false, path: `test-results/screenshots/${name}.png` });
  }

  // ===== Keyboard (not wrapped — simple key presses) =====

  async pressEnter(): Promise<void> {
    await this.page.keyboard.press('Enter');
  }

  async pressTab(): Promise<void> {
    await this.page.keyboard.press('Tab');
  }

  async pressEscape(): Promise<void> {
    await this.page.keyboard.press('Escape');
  }

  // ===== Abstract methods — subclass PHẢI override =====

  /**
   * Verify page đã load đúng (check URL hoặc heading).
   * Mỗi Page class phải implement method này, và PHẢI wrap với allure.step().
   */
  abstract verifyPageLoaded(): Promise<void>;
}
