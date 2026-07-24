/**
 * forgot-password.page.ts — Forgot Password Page Object
 * URL: https://crm.anhtester.com/admin/authentication/forgot_password
 *
 * Allure step hierarchy:
 *   submitForgotPassword() / clickConfirm() → parent steps
 *     └── base.page fill() / click() → child steps (with locator details)
 */

import { type Page, expect } from '@playwright/test';
import { allure } from 'allure-playwright';
import { BasePage } from './base.page';

export class ForgotPasswordPage extends BasePage {

  // ===== Locators =====
  readonly emailInput = this.page.locator('#email');
  readonly confirmButton = this.page.locator('button[type="submit"], input[type="submit"]');
  readonly successAlert = this.page.locator('.alert-success, .alert.alert-success');
  readonly errorAlert = this.page.locator('.alert-danger, .alert.alert-danger');

  constructor(page: Page) {
    super(page);
  }

  // ===== Navigation =====

  override async navigate(): Promise<void> {
    await allure.step('Navigate to CRM Forgot Password page', async () => {
      this.logger.step('Navigate to Forgot Password page');
      await this.page.goto('https://crm.anhtester.com/admin/authentication/forgot_password');
      await this.waitForPageLoad();
    });
  }

  // ===== Form Actions =====

  async enterEmail(email: string): Promise<void> {
    this.logger.step(`Enter email in Forgot Password: ${email}`);
    await this.fill(this.emailInput, email, 'Forgot Password Email Input');
  }

  async clickConfirm(): Promise<void> {
    this.logger.step('Click Confirm button');
    await this.click(this.confirmButton, 'Confirm Button');
  }

  async submitForgotPassword(email: string): Promise<void> {
    await allure.step(`Submit Forgot Password request for email: "${email}"`, async () => {
      this.logger.step(`Submit Forgot Password for email: ${email}`);
      await this.enterEmail(email);
      await this.clickConfirm();
    });
  }

  // ===== State Checks (not wrapped — query methods) =====

  isOnForgotPasswordPage(): boolean {
    return this.getCurrentUrl().includes('forgot_password');
  }

  async isSuccessMessageDisplayed(): Promise<boolean> {
    return this.successAlert.isVisible({ timeout: 5_000 }).catch(() => false);
  }

  async getSuccessMessage(): Promise<string> {
    return (await this.successAlert.textContent())?.trim() ?? '';
  }

  async getErrorMessage(): Promise<string> {
    return (await this.errorAlert.textContent())?.trim() ?? '';
  }

  // ===== Verifications =====

  async verifyPageLoaded(): Promise<void> {
    this.logger.step('Verify Forgot Password page loaded');
    await expect(this.confirmButton, 'Confirm button should be visible').toBeVisible({ timeout: 15_000 });
  }
}
