/**
 * dashboard.page.ts — Dashboard Page Object for Perfex CRM
 * URL: https://crm.anhtester.com/admin/
 */

import { type Page, expect } from '@playwright/test';
import { BasePage } from './base.page';

export class DashboardPage extends BasePage {
  readonly pageTitle = this.page.locator('h1.page-title, h4.page-title, .page-title');
  readonly profileDropdown = this.page.locator('li.icon.header-user-profile, a.dropdown-toggle.profile').first();
  readonly sidebar = this.page.locator('#sidebar, .sidebar, .nav-sidebar, .app-sidebar');

  constructor(page: Page) {
    super(page);
  }

  override async navigate(): Promise<void> {
    this.logger.step('Navigate to Dashboard page');
    await super.navigate('https://crm.anhtester.com/admin/');
    await this.verifyPageLoaded();
  }

  async clickProfileDropdown(): Promise<void> {
    this.logger.step('Click profile dropdown');
    await this.profileDropdown.click({ force: true }).catch(() => {});
    await this.page.waitForTimeout(500);
  }

  async logout(): Promise<void> {
    this.logger.step('Perform logout');
    await this.page.waitForLoadState('domcontentloaded');
    await this.clickProfileDropdown();
    const logoutBtn = this.page.locator('a[href*="logout"]').last();
    if (await logoutBtn.isVisible().catch(() => false)) {
      await logoutBtn.click({ force: true });
    } else {
      await this.page.goto('https://crm.anhtester.com/admin/authentication/logout');
    }
  }

  isOnDashboard(): boolean {
    const url = this.getCurrentUrl();
    return url.includes('/admin/') && !url.includes('authentication');
  }

  async getPageHeading(): Promise<string> {
    return (await this.pageTitle.textContent())?.trim() ?? '';
  }

  async isSidebarVisible(): Promise<boolean> {
    return this.sidebar.isVisible({ timeout: 5_000 }).catch(() => false);
  }

  async verifyPageLoaded(): Promise<void> {
    this.logger.step('Verify Dashboard page loaded');
    await expect(this.page, 'User should be on Dashboard URL').toHaveURL(/.*\/admin\/(?!authentication).*/);
  }

  async verifyLoggedIn(): Promise<void> {
    this.logger.step('Verify user is logged in');
    await expect(this.page, 'User should be redirected away from Login page after successful login')
      .not.toHaveURL(/.*authentication.*/i);
  }
}
