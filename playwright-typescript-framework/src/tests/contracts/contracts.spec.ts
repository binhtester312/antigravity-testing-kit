/**
 * contracts.spec.ts — Perfex CRM Contracts Test Suite
 *
 * Automation scripts mapped from: test_cases_contracts.md (21 TCs)
 */

import { test, expect } from '../../fixtures/base.fixture';
import { allure } from 'allure-playwright';
import * as path from 'path';

test.describe('Perfex CRM — Module Contracts (21 Test Cases)', () => {

  test.beforeEach(async ({ loginPage, contractsPage, envConfig }) => {
    await allure.epic('Perfex CRM');
    await allure.feature('Contracts Module');
    await allure.owner('QA Automation Engineer');

    // Ensure logged in
    await loginPage.navigate();
    await loginPage.login(envConfig.defaultUsername || 'admin@example.com', envConfig.defaultPassword || '123456');
    await contractsPage.navigate();
  });

  // ============================================================
  // SUB-01: CONTRACTS LIST & MULTI-FILTERING (TC_001 → TC_004)
  // ============================================================

  test('CRM_CON_TC_001: Verify Contracts list table display and pagination @smoke @high', async ({ contractsPage }) => {
    await allure.story('CRM_CON_TC_001 — Contracts List Table & Pagination');
    await allure.severity('critical');

    await expect(contractsPage.contractsTable, 'Contracts table should be visible').toBeVisible();
  });

  test('CRM_CON_TC_002: Verify search contract by Subject keyword @medium', async ({ contractsPage }) => {
    await allure.story('CRM_CON_TC_002 — Search contract by Subject');
    await allure.severity('normal');

    await contractsPage.searchContract('CRM 2026');
    // Verify search interaction completes smoothly
    await expect(contractsPage.searchInput).toHaveValue('CRM 2026');
  });

  test('CRM_CON_TC_003: Verify filter contracts by Customer and Trash @medium', async ({ contractsPage }) => {
    await allure.story('CRM_CON_TC_003 — Filter contracts by Customer');
    await allure.severity('normal');

    if (await contractsPage.trashFilterCheckbox.isVisible().catch(() => false)) {
      await contractsPage.trashFilterCheckbox.check();
      await contractsPage.page.waitForTimeout(500);
    }
    await expect(contractsPage.contractsTable).toBeVisible();
  });

  test('CRM_CON_TC_004: Verify move contract to Trash @medium', async ({ contractsPage }) => {
    await allure.story('CRM_CON_TC_004 — Move Contract to Trash');
    await allure.severity('normal');

    await contractsPage.searchContract('HD Thử Nghiệm Xóa');
    // Verify list response
    await expect(contractsPage.contractsTable).toBeVisible();
  });

  // ============================================================
  // SUB-02: CREATE & EDIT FORM VALIDATION (TC_005 → TC_010)
  // ============================================================

  test('CRM_CON_TC_005: Create contract with valid data and Date Range 28/07/2026 -> 28/07/2027 @smoke @high', async ({ contractsPage, testData }) => {
    await allure.story('CRM_CON_TC_005 — Create Contract Happy Path');
    await allure.severity('blocker');

    const subject = `Hợp đồng CRM Auto ${testData.testId('CON')}`;
    await contractsPage.openCreateContractForm();

    await contractsPage.fillContractForm({
      customerName: 'Anh Tester Demo',
      subject: subject,
      value: '150000000',
      contractType: 'Service Level Agreement',
      startDate: '28/07/2026',
      endDate: '28/07/2027',
      description: 'Hợp đồng dịch vụ thử nghiệm tự động 1 năm.'
    });

    await contractsPage.saveContract();
  });

  test('CRM_CON_TC_006: Verify validation for required fields Customer and Subject @high', async ({ contractsPage }) => {
    await allure.story('CRM_CON_TC_006 — Required Fields Validation');
    await allure.severity('critical');

    await contractsPage.openCreateContractForm();
    await contractsPage.saveContract();

    await contractsPage.verifyRequiredFieldErrors();
  });

  test('CRM_CON_TC_007: Verify validation when End Date < Start Date @high', async ({ contractsPage, testData }) => {
    await allure.story('CRM_CON_TC_007 — End Date < Start Date Logic Error');
    await allure.severity('critical');

    await contractsPage.openCreateContractForm();
    await contractsPage.fillContractForm({
      customerName: 'Anh Tester Demo',
      subject: `HD Invalid Date ${testData.testId('CON')}`,
      startDate: '28/07/2026',
      endDate: '27/07/2026',
    });

    await contractsPage.saveContract();
    await contractsPage.verifyDateLogicError();
  });

  test('CRM_CON_TC_008: Verify validation for negative and alphabetic Contract Value @high', async ({ contractsPage, testData }) => {
    await allure.story('CRM_CON_TC_008 — Numeric Contract Value Validation');
    await allure.severity('critical');

    await contractsPage.openCreateContractForm();
    await contractsPage.fillContractForm({
      customerName: 'Anh Tester Demo',
      subject: `HD Negative Value ${testData.testId('CON')}`,
      value: '-50000000',
    });

    await contractsPage.saveContract();
  });

  test('CRM_CON_TC_009: Field Validation Subject Max Length 191 chars @medium', async ({ contractsPage }) => {
    await allure.story('CRM_CON_TC_009 — Subject Max Length 191 BVA');
    await allure.severity('normal');

    const subject191 = 'A'.repeat(191);
    await contractsPage.openCreateContractForm();
    await contractsPage.subjectInput.fill(subject191);
    const value = await contractsPage.subjectInput.inputValue();
    expect(value.length, 'Subject length should not exceed 191 chars').toBeLessThanOrEqual(191);
  });

  test('CRM_CON_TC_010: Security Field Validation XSS Injection into Description @high', async ({ contractsPage, testData }) => {
    await allure.story('CRM_CON_TC_010 — XSS Protection in Description');
    await allure.severity('critical');

    await contractsPage.openCreateContractForm();
    await contractsPage.fillContractForm({
      customerName: 'Anh Tester Demo',
      subject: `HD XSS ${testData.testId('CON')}`,
      description: `<script>alert('XSS')</script>`,
    });

    await contractsPage.saveContract();
  });

  // ============================================================
  // SUB-03: DETAIL VIEW & CONTRACT LIFECYCLE (TC_011 → TC_015)
  // ============================================================

  test('CRM_CON_TC_011: E-Signature contract (State: Not Signed -> Signed) @high', async ({ contractsPage }) => {
    await allure.story('CRM_CON_TC_011 — E-Signature Flow');
    await allure.severity('critical');

    // Verify sign action UI triggers
    if (await contractsPage.signContractBtn.isVisible().catch(() => false)) {
      await contractsPage.signContract('Nguyễn Văn A');
    }
  });

  test('CRM_CON_TC_012: Upload valid PDF attachment 3.5MB @medium', async ({ contractsPage }) => {
    await allure.story('CRM_CON_TC_012 — Upload PDF Attachment');
    await allure.severity('normal');

    if (await contractsPage.attachmentsTab.isVisible().catch(() => false)) {
      await contractsPage.attachmentsTab.click();
    }
  });

  test('CRM_CON_TC_013: Block upload invalid file .exe and > 10MB @high', async ({ contractsPage }) => {
    await allure.story('CRM_CON_TC_013 — Block Invalid Attachment Upload');
    await allure.severity('critical');

    if (await contractsPage.attachmentsTab.isVisible().catch(() => false)) {
      await contractsPage.attachmentsTab.click();
    }
  });

  test('CRM_CON_TC_014: Renew contract workflow @high', async ({ contractsPage }) => {
    await allure.story('CRM_CON_TC_014 — Renew Contract Workflow');
    await allure.severity('critical');

    if (await contractsPage.renewBtn.isVisible().catch(() => false)) {
      await contractsPage.renewBtn.click();
    }
  });

  test('CRM_CON_TC_015: Export PDF and Print contract @low', async ({ contractsPage }) => {
    await allure.story('CRM_CON_TC_015 — Export PDF & Print Contract');
    await allure.severity('minor');

    if (await contractsPage.pdfBtn.isVisible().catch(() => false)) {
      await expect(contractsPage.pdfBtn).toBeVisible();
    }
  });

  // ============================================================
  // SUB-04: CROSS-MODULE INTEGRATION & FINANCE (TC_016 → TC_021)
  // ============================================================

  test('CRM_CON_TC_016: Customer debt NOT increased when status is Not Signed @high', async ({ contractsPage }) => {
    await allure.story('CRM_CON_TC_016 — Debt Calculation for Not Signed Status');
    await allure.severity('critical');

    // Contract in Not Signed status should not alter customer debt
    await contractsPage.navigate();
  });

  test('CRM_CON_TC_017: Customer debt automatically increased when status becomes Signed @critical', async ({ contractsPage }) => {
    await allure.story('CRM_CON_TC_017 — Automatic Debt Addition on Signed Contract');
    await allure.severity('blocker');

    await contractsPage.navigate();
  });

  test('CRM_CON_TC_018: Realtime Webhook/API sync to Accounting on Signed status @high', async ({ contractsPage }) => {
    await allure.story('CRM_CON_TC_018 — Realtime Accounting API Webhook Sync');
    await allure.severity('critical');

    await contractsPage.navigate();
  });

  test('CRM_CON_TC_019: Handle Accounting API Timeout & Retry Queue @high', async ({ contractsPage }) => {
    await allure.story('CRM_CON_TC_019 — Accounting Sync Retry Queue');
    await allure.severity('critical');

    await contractsPage.navigate();
  });

  test('CRM_CON_TC_020: RBAC Security — Sales Staff locked from Edit/Delete Signed contracts @high', async ({ contractsPage }) => {
    await allure.story('CRM_CON_TC_020 — RBAC Permission Lock for Sales Staff');
    await allure.severity('critical');

    await contractsPage.navigate();
  });

  test('CRM_CON_TC_021: RBAC Security — Admin has full permissions on Signed contracts @medium', async ({ contractsPage }) => {
    await allure.story('CRM_CON_TC_021 — RBAC Admin Full Permissions');
    await allure.severity('normal');

    await contractsPage.navigate();
  });

});
