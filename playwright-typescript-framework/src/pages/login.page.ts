/**
 * login.page.ts — Login Page Object for Perfex CRM
 * URL: https://crm.anhtester.com/admin/authentication
 */

import { type Page, expect } from '@playwright/test';
import { BasePage } from './base.page';
import { EnvConfig } from '../utils/env.config';

export class LoginPage extends BasePage {
  // Locators
  readonly emailInput = this.page.locator('#email');
  readonly passwordInput = this.page.locator('#password');
  readonly rememberMeCheckbox = this.page.locator('#remember, input[name="remember"], input[name="remember_me"]');
  readonly loginButton = this.page.locator('button[type="submit"], input[type="submit"]');
  readonly forgotPasswordLink = this.page.locator('a[href*="forgot_password"]');
  readonly serverErrorAlert = this.page.locator('.alert-danger, .alert.alert-danger');
  readonly emailFieldError = this.page.locator('.form-group:has(#email) .help-block, #email ~ .help-block');
  readonly passwordFieldError = this.page.locator('.form-group:has(#password) .help-block, #password ~ .help-block');
  readonly csrfTokenInput = this.page.locator('input[name="csrf_token_name"]');

  constructor(page: Page) {
    super(page);
  }

  override async navigate(): Promise<void> {
    this.logger.step('Navigate to Login page');
    await super.navigate(EnvConfig.baseUrl || 'https://crm.anhtester.com/admin/authentication');
    await this.verifyPageLoaded();
  }

  async enterEmail(email: string): Promise<void> {
    this.logger.step(`Enter email: ${email}`);
    await this.fill(this.emailInput, email, 'Email input');
  }

  async enterPassword(password: string): Promise<void> {
    this.logger.step('Enter password: [MASKED]');
    await this.fill(this.passwordInput, password, 'Password input');
  }

  async checkRememberMe(): Promise<void> {
    this.logger.step('Check Remember Me');
    const isChecked = await this.rememberMeCheckbox.isChecked().catch(() => false);
    if (!isChecked) {
      await this.check(this.rememberMeCheckbox);
    }
  }

  async clickLoginButton(): Promise<void> {
    this.logger.step('Click Login button');
    await this.click(this.loginButton, 'Login button');
  }

  async pressEnterOnEmailField(): Promise<void> {
    this.logger.step('Press Enter on Email field');
    await this.emailInput.press('Enter');
  }

  async login(email: string, password: string): Promise<void> {
    this.logger.step(`Login with email: ${email}`);
    await this.enterEmail(email);
    await this.enterPassword(password);
    await this.clickLoginButton();
  }

  async loginWithRememberMe(email: string, password: string): Promise<void> {
    this.logger.step(`Login with Remember Me: ${email}`);
    await this.enterEmail(email);
    await this.enterPassword(password);
    await this.checkRememberMe();
    await this.clickLoginButton();
  }

  async loginWithEnterKey(email: string, password: string): Promise<void> {
    this.logger.step(`Login with Enter key: ${email}`);
    await this.enterEmail(email);
    await this.enterPassword(password);
    await this.pressEnterOnEmailField();
  }

  async loginWithDefaultCredentials(): Promise<void> {
    this.logger.step('Login with default credentials from env');
    await this.login(EnvConfig.defaultUsername || 'admin@example.com', EnvConfig.defaultPassword || '123456');
  }

  async clickForgotPasswordLink(): Promise<void> {
    this.logger.step('Click Forgot Password link');
    await this.click(this.forgotPasswordLink, 'Forgot Password link');
  }

  isOnLoginPage(): boolean {
    return this.getCurrentUrl().includes('authentication');
  }

  async isServerErrorDisplayed(): Promise<boolean> {
    return this.serverErrorAlert.isVisible({ timeout: 4_000 }).catch(() => false);
  }

  async getServerErrorMessage(): Promise<string> {
    return (await this.serverErrorAlert.textContent())?.trim() ?? '';
  }

  async isEmailFieldErrorDisplayed(): Promise<boolean> {
    return this.emailFieldError.isVisible({ timeout: 3_000 }).catch(() => false);
  }

  async isPasswordFieldErrorDisplayed(): Promise<boolean> {
    return this.passwordFieldError.isVisible({ timeout: 3_000 }).catch(() => false);
  }

  async getEmailFieldError(): Promise<string> {
    return (await this.emailFieldError.textContent())?.trim() ?? '';
  }

  async getPasswordFieldError(): Promise<string> {
    return (await this.passwordFieldError.textContent())?.trim() ?? '';
  }

  async getCsrfTokenValue(): Promise<string | null> {
    return this.csrfTokenInput.getAttribute('value');
  }

  async verifyPageLoaded(): Promise<void> {
    this.logger.step('Verify Login page loaded');
    await expect(this.loginButton, 'Login button should be visible when Login page is loaded')
      .toBeVisible({ timeout: 15_000 });
  }

  async verifyOnLoginPage(): Promise<void> {
    this.logger.step('Verify currently on Login page');
    await expect(this.page, 'Should be on Login page').toHaveURL(/.*authentication.*/i);
  }
}
