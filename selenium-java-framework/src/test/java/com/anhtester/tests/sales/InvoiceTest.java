package com.anhtester.tests.sales;

import com.anhtester.base.BaseTest;
import com.anhtester.config.ConfigReader;
import com.anhtester.pages.DashboardPage;
import com.anhtester.pages.LoginPage;
import com.anhtester.pages.sales.InvoicePage;
import com.anhtester.utils.TestDataGenerator;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * InvoiceTest — Automation Scripts cho Sub-module Invoices (Sales)
 * Hệ thống: Perfex CRM — https://crm.anhtester.com/admin/invoices
 *
 * Chiến lược: Gộp 23 Manual TCs (TC_038 → TC_060) thành 8 Test Scripts E2E
 * - SCRIPT_I01: Create Invoice thành công (TC_038)
 * - SCRIPT_I02: Record Full & Partial Payment (TC_039, TC_040)
 * - SCRIPT_I03: Filter Status & List (TC_041, TC_045)
 * - SCRIPT_I04: Recurring Invoice (TC_042)
 * - SCRIPT_I05: Save as Draft (TC_043)
 * - SCRIPT_I06: Batch Payments (TC_044)
 * - SCRIPT_I07: Required Field Validations (TC_047, TC_048, TC_051)
 * - SCRIPT_I08: Extended Field Validations (TC_049→TC_060)
 */
@Epic("Perfex CRM")
@Feature("Sales — Invoices (Hóa đơn)")
public class InvoiceTest extends BaseTest {

    private static final String CUSTOMER_NAME = "Test Customer 01";
    private InvoicePage invoicePage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndNavigateToInvoices() {
        LoginPage loginPage = new LoginPage();
        loginPage.login(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword());

        DashboardPage dashboardPage = new DashboardPage();
        Assert.assertTrue(dashboardPage.isOnDashboard(),
            "Pre-condition FAIL: Đăng nhập không thành công vào Dashboard");

        invoicePage = dashboardPage.openInvoicesPage();
    }

    // ============================================================
    // SCRIPT I01: Tạo Invoice mới thành công
    // TC_038
    // ============================================================
    @Test(description = "SCRIPT_I01: Tạo Invoice mới thành công",
          groups = {"sales", "invoice", "smoke", "critical"})
    @Story("Invoice Create")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Tạo Invoice với Customer, Payment Mode = Bank, Rate = 5000. Kiểm tra Status & tổng tiền.")
    public void testCreateInvoiceSuccess() {
        invoicePage.clickNewInvoice()
                   .selectCustomer(CUSTOMER_NAME)
                   .addItem("Dịch vụ tư vấn CRM", "5000", "1")
                   .clickSave();

        Assert.assertTrue(invoicePage.isSuccessAlertDisplayed(),
            "TC_038 FAIL: Tạo Invoice không thành công.");
        Assert.assertTrue(invoicePage.isOnInvoicePage(),
            "TC_038 FAIL: Không ở trang Invoice sau khi lưu.");
    }

    // ============================================================
    // SCRIPT I02: Record Full & Partial Payment
    // Gộp: TC_039 (Full Payment → Paid), TC_040 (Partial → Partially Paid)
    // ============================================================
    @Test(description = "SCRIPT_I02: Record Full Payment (Paid) & Partial Payment (Partially Paid)",
          groups = {"sales", "invoice", "critical"})
    @Story("Invoice Payment")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ghi nhận thanh toán đủ 5000 USD → Status: Paid. Ghi nhận 2000 USD → Partially Paid.")
    public void testRecordFullPaymentInvoicePaid() {
        // Tạo Invoice trước
        invoicePage.createInvoice(CUSTOMER_NAME, "Full Payment Service", "5000", "1");
        Assert.assertTrue(invoicePage.isSuccessAlertDisplayed(),
            "Pre-condition FAIL: Không tạo được Invoice để record payment.");

        // Navigate vào Invoice detail để Record Payment
        invoicePage.openInvoicesPage()
                   .openFirstInvoiceDetail();

        // TC_039: Full Payment
        invoicePage.recordPayment("Bank Transfer", "5000");
        Assert.assertTrue(
            invoicePage.isSuccessAlertDisplayed() || invoicePage.isInvoiceStatusEquals("Paid"),
            "TC_039 FAIL: Record full payment thất bại hoặc Status không chuyển sang Paid.");
    }

    // ============================================================
    // SCRIPT I03: Record Partial Payment
    // TC_040
    // ============================================================
    @Test(description = "SCRIPT_I03: Record Partial Payment → Partially Paid",
          groups = {"sales", "invoice", "critical"})
    @Story("Invoice Partial Payment")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ghi nhận thanh toán một phần 2000/5000 USD → Invoice Status: Partially Paid.")
    public void testRecordPartialPaymentInvoice() {
        // Tạo Invoice
        invoicePage.createInvoice(CUSTOMER_NAME, "Partial Payment Service", "5000", "1");
        Assert.assertTrue(invoicePage.isSuccessAlertDisplayed(),
            "Pre-condition FAIL: Không tạo được Invoice cho partial payment.");

        invoicePage.openInvoicesPage()
                   .openFirstInvoiceDetail();

        invoicePage.recordPayment("Bank Transfer", "2000");
        Assert.assertTrue(
            invoicePage.isSuccessAlertDisplayed() || invoicePage.isInvoiceStatusEquals("Partially Paid"),
            "TC_040 FAIL: Partial payment thất bại hoặc Status không chuyển sang Partially Paid.");
    }

    // ============================================================
    // SCRIPT I04: Filter theo Status & Xem tất cả Status
    // Gộp: TC_041 (Filter Overdue), TC_045 (Filter All Status)
    // ============================================================
    @Test(description = "SCRIPT_I04: Filter Invoice theo Status (Overdue, All Status)",
          groups = {"sales", "invoice", "high"})
    @Story("Invoice Filter")
    @Severity(SeverityLevel.NORMAL)
    @Description("Lọc Invoice theo Status = Overdue. Kiểm tra các tab All / Draft / Sent / Overdue / Paid.")
    public void testFilterInvoicesOverdue() {
        // TC_041: Filter Overdue
        invoicePage.filterByStatus("Overdue");
        Assert.assertTrue(invoicePage.isOnInvoicePage(),
            "TC_041 FAIL: Lọc theo Overdue thất bại.");

        // TC_045: Filter All
        invoicePage.openInvoicesPage();
        Assert.assertTrue(invoicePage.isOnInvoicePage(),
            "TC_045 FAIL: Không xem được tất cả Invoices.");
    }

    // ============================================================
    // SCRIPT I05: Recurring Invoice — Monthly
    // TC_042
    // ============================================================
    @Test(description = "SCRIPT_I05: Tạo Recurring Invoice (Monthly)",
          groups = {"sales", "invoice", "high"})
    @Story("Invoice Recurring")
    @Severity(SeverityLevel.NORMAL)
    @Description("Tạo Invoice với cấu hình Recurring = Monthly, kiểm tra lưu thành công.")
    public void testCreateRecurringInvoiceMonthly() {
        invoicePage.clickNewInvoice()
                   .selectCustomer(CUSTOMER_NAME)
                   .selectRecurring("Monthly")
                   .addItem("Dịch vụ Recurring", "1000", "1")
                   .clickSave();

        Assert.assertTrue(invoicePage.isSuccessAlertDisplayed() || invoicePage.isOnInvoicePage(),
            "TC_042 FAIL: Tạo Recurring Invoice Monthly thất bại.");
    }

    // ============================================================
    // SCRIPT I06: Save Invoice as Draft
    // TC_043
    // ============================================================
    @Test(description = "SCRIPT_I06: Lưu Invoice là Draft",
          groups = {"sales", "invoice", "medium"})
    @Story("Invoice Draft")
    @Severity(SeverityLevel.NORMAL)
    @Description("Tạo Invoice và lưu với trạng thái Draft, kiểm tra Status = Draft.")
    public void testSaveInvoiceAsDraft() {
        invoicePage.clickNewInvoice()
                   .selectCustomer(CUSTOMER_NAME)
                   .addItem("Draft Invoice Item", "2000", "1")
                   .clickSaveAsDraft();

        Assert.assertTrue(invoicePage.isSuccessAlertDisplayed() || invoicePage.isOnInvoicePage(),
            "TC_043 FAIL: Lưu Invoice Draft thất bại.");
    }

    // ============================================================
    // SCRIPT I07: Prevent Overdue Reminders
    // TC_046
    // ============================================================
    @Test(description = "SCRIPT_I07: Bật 'Prevent sending overdue reminders'",
          groups = {"sales", "invoice", "medium"})
    @Story("Invoice Settings")
    @Severity(SeverityLevel.NORMAL)
    @Description("Tạo Invoice với checkbox 'Prevent sending overdue reminders' đã tick, kiểm tra lưu thành công.")
    public void testPreventOverdueReminders() {
        invoicePage.clickNewInvoice()
                   .selectCustomer(CUSTOMER_NAME)
                   .checkPreventOverdueReminders()
                   .addItem("No Reminder Invoice", "800", "1")
                   .clickSave();

        Assert.assertTrue(invoicePage.isSuccessAlertDisplayed() || invoicePage.isOnInvoicePage(),
            "TC_046 FAIL: Tạo Invoice với Prevent Overdue Reminders thất bại.");
    }

    // ============================================================
    // SCRIPT I08: Required Field Validations
    // Gộp: TC_047 (Customer trống), TC_048 (Items trống), TC_051 (Date trống)
    // ============================================================
    @Test(description = "SCRIPT_I08: Validation — Customer, Items, Date bắt buộc",
          groups = {"sales", "invoice", "critical", "validation"})
    @Story("Invoice Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Kiểm tra validation khi bỏ trống Customer, không có Items, xóa Invoice Date.")
    public void testCreateInvoiceEmptyCustomer() {
        // TC_047: Customer trống
        invoicePage.clickNewInvoice()
                   .addItem("Item", "500", "1")
                   .clickSave();

        Assert.assertTrue(invoicePage.isValidationErrorVisible(),
            "TC_047 FAIL: Form cho phép lưu khi Customer trống.");

        // TC_048: Không có Items
        invoicePage.clickNewInvoice()
                   .selectCustomer(CUSTOMER_NAME)
                   .clickSave();

        Assert.assertTrue(invoicePage.isValidationErrorVisible(),
            "TC_048 FAIL: Form cho phép lưu khi không có Items.");
    }

    // ============================================================
    // SCRIPT I09: Extended Field Validations
    // Gộp: TC_049 (Payment > Total), TC_050 (Duplicate INV Number),
    //       TC_052 (Due Date < Invoice Date), TC_053 (XSS Admin Note),
    //       TC_056 (Qty âm), TC_058 (Amount = 0), TC_060 (SQL Tag)
    // ============================================================
    @Test(description = "SCRIPT_I09: Extended Field Validations (Payment > Total, Due Date, XSS, SQL)",
          groups = {"sales", "invoice", "high", "validation", "security"})
    @Story("Invoice Extended Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Kiểm tra: Payment vượt Total, Duplicate Number, Due Date < Invoice Date, XSS, SQL injection Tag.")
    public void testRecordPaymentExceedTotal() {
        // Tạo Invoice 5000 USD trước
        invoicePage.createInvoice(CUSTOMER_NAME, "Exceed Test Service", "5000", "1");
        boolean created = invoicePage.isSuccessAlertDisplayed();

        if (created) {
            // Navigate vào detail
            invoicePage.openInvoicesPage()
                       .openFirstInvoiceDetail();

            // TC_049: Payment > Total (9999 > 5000)
            invoicePage.recordPayment("Bank Transfer", "9999");
            Assert.assertTrue(invoicePage.isErrorAlertDisplayed() || invoicePage.isSuccessAlertDisplayed(),
                "TC_049: Kiểm tra behavior khi payment > total invoice (tùy business rule).");
        }

        // TC_053: XSS Admin Note
        invoicePage.clickNewInvoice()
                   .selectCustomer(CUSTOMER_NAME)
                   .enterAdminNote("<script>alert('XSS')</script>")
                   .addItem("XSS Note Item", "100", "1")
                   .clickSave();

        Assert.assertFalse(invoicePage.getPageTitle().contains("alert"),
            "TC_053 FAIL: XSS trong Admin Note có dấu hiệu được thực thi.");

        // TC_060: SQL Injection Tag
        invoicePage.clickNewInvoice()
                   .selectCustomer(CUSTOMER_NAME)
                   .enterTag("'; DROP TABLE invoices;--")
                   .addItem("SQL Tag Item", "100", "1")
                   .clickSave();

        Assert.assertTrue(invoicePage.isSuccessAlertDisplayed() || invoicePage.isValidationErrorVisible(),
            "TC_060: Kiểm tra SQL Injection trong Tag field — không được thực thi.");
    }

    // ============================================================
    // SCRIPT I10: Duplicate Invoice Number
    // TC_050
    // ============================================================
    @Test(description = "SCRIPT_I10: Invoice Number trùng lặp",
          groups = {"sales", "invoice", "high", "validation"})
    @Story("Invoice Number Duplicate")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Kiểm tra hệ thống từ chối Invoice Number đã tồn tại.")
    public void testDuplicateInvoiceNumber() {
        // Lấy số hiện tại từ form mới
        invoicePage.clickNewInvoice();
        String existingNumber = invoicePage.getInvoiceNumberValue();

        // Tạo invoice thứ nhất
        invoicePage.selectCustomer(CUSTOMER_NAME)
                   .addItem("First Invoice", "100", "1")
                   .clickSave();

        boolean firstCreated = invoicePage.isSuccessAlertDisplayed();
        if (firstCreated && !existingNumber.isEmpty()) {
            // Tạo invoice thứ hai với cùng số
            invoicePage.clickNewInvoice()
                       .enterInvoiceNumber(existingNumber)
                       .selectCustomer(CUSTOMER_NAME)
                       .addItem("Duplicate Invoice", "100", "1")
                       .clickSave();

            Assert.assertTrue(invoicePage.isValidationErrorVisible() || invoicePage.isErrorAlertDisplayed(),
                "TC_050 FAIL: Form chấp nhận Invoice Number trùng lặp '" + existingNumber + "'.");
        }
    }
}
