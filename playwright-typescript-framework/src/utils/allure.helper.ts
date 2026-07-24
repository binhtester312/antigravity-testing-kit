/**
 * allure.helper.ts — Allure Report helper utilities
 *
 * Cung cấp các helper methods để:
 * - Đính kèm screenshots vào Allure report
 * - Thêm metadata (epic, feature, story, severity, tags)
 * - Tạo allure steps với cú pháp rõ ràng
 * - Đính kèm video execution
 *
 * Chỉ gọi trong phạm vi một test đang chạy (test context).
 */

import { allure } from 'allure-playwright';
import { type Page, type TestInfo } from '@playwright/test';

export type AlluSeverity = 'blocker' | 'critical' | 'normal' | 'minor' | 'trivial';

export class AllureHelper {

  /**
   * Tạo một allure step — dùng khi cần wrap thủ công ngoài page classes
   */
  static async step(name: string, fn: () => Promise<void>): Promise<void> {
    return allure.step(name, fn);
  }

  // ===== Screenshot Attachments =====

  /**
   * Chụp và attach screenshot hiện tại vào Allure step hiện tại
   */
  static async attachScreenshot(page: Page, name: string = '📸 Screenshot'): Promise<void> {
    const screenshot = await page.screenshot({ fullPage: false });
    await allure.attachment(name, screenshot, 'image/png');
  }

  /**
   * Chụp full-page screenshot và attach vào Allure
   */
  static async attachFullPageScreenshot(page: Page, name: string = '📸 Full Page Screenshot'): Promise<void> {
    const screenshot = await page.screenshot({ fullPage: true });
    await allure.attachment(name, screenshot, 'image/png');
  }

  /**
   * Attach screenshot vào testInfo (dùng trong fixture teardown)
   * Đây là cách duy nhất để attach sau khi test hoàn thành
   */
  static async attachScreenshotToTestInfo(
    page: Page,
    testInfo: TestInfo,
    name: string = '📸 Final State (PASS)',
  ): Promise<void> {
    const screenshot = await page.screenshot({ fullPage: false }).catch(() => null);
    if (screenshot) {
      await testInfo.attach(name, { body: screenshot, contentType: 'image/png' });
    }
  }

  // ===== Test Metadata =====

  static async setEpic(epic: string): Promise<void> {
    await allure.epic(epic);
  }

  static async setFeature(feature: string): Promise<void> {
    await allure.feature(feature);
  }

  static async setStory(story: string): Promise<void> {
    await allure.story(story);
  }

  static async setSeverity(severity: AlluSeverity): Promise<void> {
    await allure.severity(severity);
  }

  static async addTag(...tags: string[]): Promise<void> {
    await allure.tags(...tags);
  }

  static async addOwner(owner: string): Promise<void> {
    await allure.owner(owner);
  }

  static async addDescription(description: string): Promise<void> {
    await allure.description(description);
  }

  static async addParameter(
    name: string,
    value: string,
    options?: { excluded?: boolean; mode?: 'hidden' | 'masked' | 'default' },
  ): Promise<void> {
    await allure.parameter(name, value, options);
  }

  static async addLink(url: string, name?: string, type: string = 'custom'): Promise<void> {
    if (name !== undefined) {
      await allure.link(type, url, name);
    } else {
      await allure.link(type, url);
    }
  }
}
