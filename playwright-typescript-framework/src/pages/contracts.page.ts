/**
 * contracts.page.ts — Contracts Page Object for Perfex CRM
 * URL: https://crm.anhtester.com/admin/contracts
 */

import { type Page, type Locator, expect } from '@playwright/test';
import { allure } from 'allure-playwright';
import { BasePage } from './base.page';

export class ContractsPage extends BasePage {
  // ===== Locators - List Page =====
  readonly newContractBtn: Locator = this.page.locator('a[href*="contracts/contract"]').filter({ hasText: 'New Contract' }).first();
  readonly searchInput: Locator = this.page.locator('input[type="search"]').first();
  readonly contractsTable: Locator = this.page.locator('table.table-contracts, table.dataTable').first();
  readonly tableRows: Locator = this.page.locator('table.table-contracts tbody tr, table.dataTable tbody tr');
  readonly filterCustomerDropdown: Locator = this.page.locator('select[name="customer_id"], #customer_id').first();
  readonly trashFilterCheckbox: Locator = this.page.locator('input[name="trash"], #trash').first();
  readonly summaryTotal: Locator = this.page.locator('.contract-summary, .summary-total').first();
  readonly noDataText: Locator = this.page.locator('td.dataTables_empty, .no-margin').first();

  // ===== Locators - Contract Form =====
  readonly customerDropdownBtn: Locator = this.page.locator('button[data-id="clientid"]').first();
  readonly customerSearchBox: Locator = this.page.locator('.bs-searchbox input').first();
  readonly customerSelectOption = (name: string) => this.page.locator('.dropdown-menu.open li a').filter({ hasText: name }).first();
  readonly subjectInput: Locator = this.page.locator('input#subject').first();
  readonly contractValueInput: Locator = this.page.locator('input#contract_value').first();
  readonly contractTypeBtn: Locator = this.page.locator('button[data-id="contract_type"]').first();
  readonly contractTypeOption = (type: string) => this.page.locator('.dropdown-menu.open li a').filter({ hasText: type }).first();
  readonly startDateInput: Locator = this.page.locator('input#datestart').first();
  readonly endDateInput: Locator = this.page.locator('input#dateend').first();
  readonly descriptionInput: Locator = this.page.locator('textarea#description').first();
  readonly trashCheckbox: Locator = this.page.locator('input#trash').first();
  readonly hideFromCustomerSwitch: Locator = this.page.locator('input#hide_from_customer').first();
  readonly saveBtn: Locator = this.page.locator('form#contract-form button[type="submit"], button[type="submit"].btn-primary').first();

  // ===== Locators - Form Validation Messages =====
  readonly customerErrorMsg: Locator = this.page.locator('#clientid-error, #clientid-error-el, .text-danger:has-text("Customer"), label#clientid-error').first();
  readonly subjectErrorMsg: Locator = this.page.locator('#subject-error, p#subject-error').first();
  readonly dateErrorMsg: Locator = this.page.locator('#dateend-error, .alert-danger, .text-danger:has-text("Date")').first();
  readonly valueErrorMsg: Locator = this.page.locator('#contract_value-error, .text-danger:has-text("Value")').first();

  // ===== Locators - Detail & Actions =====
  readonly signContractBtn: Locator = this.page.locator('a:has-text("Sign Contract"), button:has-text("Sign Contract")').first();
  readonly signerNameInput: Locator = this.page.locator('input#name, input[name="name"]').first();
  readonly signatureCanvas: Locator = this.page.locator('canvas, #signature').first();
  readonly submitSignBtn: Locator = this.page.locator('button[type="submit"]:has-text("Sign"), button.btn-success:has-text("Sign")').first();
  readonly statusBadge: Locator = this.page.locator('.label-contract-status, .contract-status, .label-info, .label-success').first();
  readonly attachmentsTab: Locator = this.page.locator('a[href*="tab_attachments"], a:has-text("Attachments")').first();
  readonly fileUploadInput: Locator = this.page.locator('input[type="file"]').first();
  readonly fileUploadDropzone: Locator = this.page.locator('.dropzone, #contract-attachments-upload').first();
  readonly fileErrorAlert: Locator = this.page.locator('.dz-error-message, .alert-danger').first();
  readonly renewBtn: Locator = this.page.locator('a:has-text("Renew"), button:has-text("Renew")').first();
  readonly pdfBtn: Locator = this.page.locator('a[href*="pdf"], a:has-text("PDF")').first();
  readonly printBtn: Locator = this.page.locator('a[href*="print"], a:has-text("Print")').first();
  readonly editBtn: Locator = this.page.locator('a:has-text("Edit"), a[href*="contract/"]').first();
  readonly deleteBtn: Locator = this.page.locator('a.text-danger:has-text("Delete"), a[href*="delete"]').first();

  constructor(page: Page) {
    super(page);
  }

  // ===== Navigation =====

  override async navigate(): Promise<void> {
    await allure.step('Navigate to CRM Contracts page', async () => {
      this.logger.step('Navigate to Contracts list page');
      await super.navigate('https://crm.anhtester.com/admin/contracts');
      await this.waitForPageLoad();
    });
  }

  async openCreateContractForm(): Promise<void> {
    await allure.step('Open Create Contract Form', async () => {
      this.logger.step('Open Create Contract Form');
      await super.navigate('https://crm.anhtester.com/admin/contracts/contract');
      await this.waitForPageLoad();
    });
  }

  // ===== Actions =====

  async searchContract(keyword: string): Promise<void> {
    await allure.step(`Search contract with keyword: "${keyword}"`, async () => {
      this.logger.step(`Search contract: ${keyword}`);
      await this.searchInput.fill(keyword);
      await this.page.keyboard.press('Enter');
      await this.page.waitForTimeout(500);
    });
  }

  async clickNewContractButton(): Promise<void> {
    await allure.step('Click "+ New Contract" button', async () => {
      this.logger.step('Click + New Contract button');
      await this.newContractBtn.click();
      await this.waitForPageLoad();
    });
  }

  async fillContractForm(data: {
    customerName: string;
    subject: string;
    value?: string;
    contractType?: string;
    startDate?: string;
    endDate?: string;
    description?: string;
  }): Promise<void> {
    await allure.step(`Fill contract form for subject: "${data.subject}"`, async () => {
      this.logger.step(`Filling contract form: ${data.subject}`);
      
      // Select Customer
      if (data.customerName) {
        await this.customerDropdownBtn.click({ force: true }).catch(() => {});
        await this.page.waitForTimeout(300);
        if (await this.customerSearchBox.isVisible().catch(() => false)) {
          await this.customerSearchBox.fill(data.customerName);
          await this.page.waitForTimeout(300);
        }
        const option = this.customerSelectOption(data.customerName);
        if (await option.isVisible().catch(() => false)) {
          await option.click();
        } else {
          // Fallback direct select
          await this.page.selectOption('select#clientid', { label: data.customerName }).catch(() => {});
        }
      }

      // Input Subject
      if (data.subject !== undefined) {
        await this.subjectInput.fill(data.subject);
      }

      // Input Contract Value
      if (data.value !== undefined) {
        await this.contractValueInput.fill(data.value);
      }

      // Select Contract Type
      if (data.contractType) {
        await this.contractTypeBtn.click({ force: true }).catch(() => {});
        await this.page.waitForTimeout(300);
        const typeOpt = this.contractTypeOption(data.contractType);
        if (await typeOpt.isVisible().catch(() => false)) {
          await typeOpt.click();
        } else {
          await this.page.selectOption('select#contract_type', { label: data.contractType }).catch(() => {});
        }
      }

      // Input Start Date
      if (data.startDate !== undefined) {
        await this.startDateInput.fill(data.startDate);
      }

      // Input End Date
      if (data.endDate !== undefined) {
        await this.endDateInput.fill(data.endDate);
      }

      // Input Description
      if (data.description !== undefined) {
        await this.descriptionInput.fill(data.description);
      }
    });
  }

  async saveContract(): Promise<void> {
    await allure.step('Click Save contract button', async () => {
      this.logger.step('Click Save contract');
      await this.saveBtn.click();
      await this.page.waitForTimeout(1000);
    });
  }

  async signContract(signerName: string): Promise<void> {
    await allure.step(`Sign contract as: "${signerName}"`, async () => {
      this.logger.step(`Signing contract: ${signerName}`);
      if (await this.signContractBtn.isVisible().catch(() => false)) {
        await this.signContractBtn.click();
        await this.page.waitForTimeout(500);
      }
      if (await this.signerNameInput.isVisible().catch(() => false)) {
        await this.signerNameInput.fill(signerName);
      }
      if (await this.signatureCanvas.isVisible().catch(() => false)) {
        const box = await this.signatureCanvas.boundingBox();
        if (box) {
          await this.page.mouse.move(box.x + 10, box.y + 10);
          await this.page.mouse.down();
          await this.page.mouse.move(box.x + 50, box.y + 30);
          await this.page.mouse.up();
        }
      }
      if (await this.submitSignBtn.isVisible().catch(() => false)) {
        await this.submitSignBtn.click();
        await this.page.waitForTimeout(1000);
      }
    });
  }

  async uploadAttachment(filePath: string): Promise<void> {
    await allure.step(`Upload attachment file: "${filePath}"`, async () => {
      this.logger.step(`Uploading attachment: ${filePath}`);
      if (await this.attachmentsTab.isVisible().catch(() => false)) {
        await this.attachmentsTab.click();
        await this.page.waitForTimeout(500);
      }
      await this.fileUploadInput.setInputFiles(filePath);
      await this.page.waitForTimeout(1500);
    });
  }

  // ===== Verifications =====

  async verifyContractInList(subject: string): Promise<void> {
    await allure.step(`Verify contract "${subject}" is displayed in Contracts list`, async () => {
      this.logger.step(`Verify contract in list: ${subject}`);
      await this.searchContract(subject);
      const row = this.page.locator('table tbody tr').filter({ hasText: subject }).first();
      await expect(row, `Contract "${subject}" should be visible in table`).toBeVisible({ timeout: 5000 });
    });
  }

  async verifyNoContractInList(subject: string): Promise<void> {
    await allure.step(`Verify contract "${subject}" is NOT displayed in list`, async () => {
      this.logger.step(`Verify no contract in list: ${subject}`);
      await this.searchContract(subject);
      await expect(this.noDataText, 'Table should display empty / no matching records').toBeVisible({ timeout: 5000 });
    });
  }

  async verifyRequiredFieldErrors(): Promise<void> {
    await allure.step('Verify required field validation errors for empty Subject & Customer', async () => {
      this.logger.step('Verify required field error messages');
      const hasError = (await this.subjectErrorMsg.isVisible().catch(() => false)) ||
                       (await this.customerErrorMsg.isVisible().catch(() => false)) ||
                       (await this.page.locator('.text-danger, .has-error').count() > 0);
      expect(hasError, 'Form should show validation error messages for required fields').toBeTruthy();
    });
  }

  async verifyDateLogicError(): Promise<void> {
    await allure.step('Verify End Date < Start Date error message', async () => {
      this.logger.step('Verify Date Logic Error');
      const hasDateErr = (await this.dateErrorMsg.isVisible().catch(() => false)) ||
                         (await this.page.locator('.alert-danger, .text-danger').count() > 0);
      expect(hasDateErr, 'Form should show date logic validation error when End Date < Start Date').toBeTruthy();
    });
  }

  async verifyContractSignedStatus(): Promise<void> {
    await allure.step('Verify contract status is Signed', async () => {
      this.logger.step('Verify contract status Signed');
      const statusText = await this.statusBadge.textContent().catch(() => '');
      expect(statusText?.toLowerCase(), 'Contract status should be Signed').toContain('signed');
    });
  }
}
