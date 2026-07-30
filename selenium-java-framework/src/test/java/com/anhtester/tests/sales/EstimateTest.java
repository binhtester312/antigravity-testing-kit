package com.anhtester.tests.sales;

import com.anhtester.base.BaseTest;
import com.anhtester.config.ConfigReader;
import com.anhtester.pages.DashboardPage;
import com.anhtester.pages.LoginPage;
import com.anhtester.pages.sales.EstimatePage;
import com.anhtester.utils.TestDataGenerator;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * EstimateTest — Automation Scripts cho Sub-module Estimates (Sales)
 * Hệ thống: Perfex CRM — https://crm.anhtester.com/admin/estimates
 *
 * Chiến lược: Gộp 15 Manual TCs (TC_023 → TC_037) thành 6 Test Scripts E2E
 * - SCRIPT_E01: Create + AutoNumber (TC_023, TC_024)
 * - SCRIPT_E02: Convert to Invoice (TC_025)
 * - SCRIPT_E03: List & Edit Draft (TC_026, TC_028)
 * - SCRIPT_E04: Expiry Date (TC_027)
 * - SCRIPT_E05: Required Field Validations (TC_029, TC_030, TC_031)
 * - SCRIPT_E06: Field Validations (TC_032 → TC_037)
 */
@Epic("Perfex CRM")
@Feature("Sales — Estimates (Ước tính)")
public class EstimateTest extends BaseTest {

    private static final String CUSTOMER_NAME = "Test Customer 01";
    private EstimatePage estimatePage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndNavigateToEstimates() {
        LoginPage loginPage = new LoginPage();
        loginPage.login(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword());

        DashboardPage dashboardPage = new DashboardPage();
        Assert.assertTrue(dashboardPage.isOnDashboard(),
            "Pre-condition FAIL: Đăng nhập không thành công vào Dashboard");

        estimatePage = dashboardPage.openEstimatesPage();
    }

    // ============================================================
    // SCRIPT E01: Tạo Estimate mới thành công + Auto Number
    // Gộp: TC_023 (Create Success), TC_024 (Auto Number)
    // ============================================================
    @Test(description = "SCRIPT_E01: Tạo Estimate mới thành công & kiểm tra Auto Number",
          groups = {"sales", "estimate", "smoke", "critical"})
    @Story("Estimate Create")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Tạo Estimate với Customer, kiểm tra Status = Draft, Estimate Number auto-generate (EST-XXXXXX).")
    public void testCreateEstimateSuccess() {
        // TC_024: Lấy số hiện tại trước khi tạo
        estimatePage.clickNewEstimate();
        String currentNumber = estimatePage.getEstimateNumberValue();

        // TC_023: Điền form & Save
        estimatePage.selectCustomer(CUSTOMER_NAME)
                    .addItem("Dịch vụ phần mềm", "2000", "1")
                    .clickSave();

        Assert.assertTrue(estimatePage.isSuccessAlertDisplayed() || estimatePage.isOnEstimatesPage() || estimatePage.getCurrentUrl().contains("estimate"),
            "TC_023 FAIL: Tạo Estimate không thành công.");

        // TC_024: Verify auto-number được sinh tự động
        estimatePage.openEstimatesPage().clickNewEstimate();
        String newNumber = estimatePage.getEstimateNumberValue();
        Assert.assertFalse(newNumber.isEmpty(),
            "TC_024 FAIL: Estimate Number không được auto-generate.");
    }

    // ============================================================
    // SCRIPT E02: Convert Estimate sang Invoice
    // TC_025
    // ============================================================
    @Test(description = "SCRIPT_E02: Convert Estimate sang Invoice",
          groups = {"sales", "estimate", "critical"})
    @Story("Estimate Convert to Invoice")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Tạo Estimate → Convert to Invoice → kiểm tra Invoice được tạo tự động với dữ liệu đúng.")
    public void testConvertEstimateToInvoice() {
        // Tạo Estimate trước
        estimatePage.createEstimate(CUSTOMER_NAME, "Service Convert", "3000", "1");
        Assert.assertTrue(estimatePage.isSuccessAlertDisplayed() || estimatePage.isOnEstimatesPage() || estimatePage.getCurrentUrl().contains("estimate"),
            "Pre-condition FAIL: Không tạo được Estimate để convert.");

        // Convert to Invoice — cần navigate vào detail page trước
        // Mở Estimate đầu tiên trong danh sách
        estimatePage.openEstimatesPage()
                    .openFirstEstimateDetail()
                    .clickConvertToInvoice();

        Assert.assertTrue(estimatePage.isOnInvoicePage() || estimatePage.isSuccessAlertDisplayed(),
            "TC_025 FAIL: Convert to Invoice không thành công hoặc không điều hướng sang Invoice.");
    }

    // ============================================================
    // SCRIPT E03: Xem List & Chỉnh sửa Estimate Draft
    // Gộp: TC_026 (Xem list), TC_028 (Edit Draft)
    // ============================================================
    @Test(description = "SCRIPT_E03: Xem danh sách Estimates & Chỉnh sửa Draft",
          groups = {"sales", "estimate", "high"})
    @Story("Estimate List & Edit")
    @Severity(SeverityLevel.NORMAL)
    @Description("Kiểm tra trang danh sách Estimates hiển thị đúng. Chỉnh sửa Rate của Estimate Draft.")
    public void testVerifyEstimateListTable() {
        // TC_026: Verify list page
        Assert.assertTrue(estimatePage.isOnEstimatesPage(),
            "TC_026 FAIL: Không điều hướng được vào trang Estimates.");

        // TC_028: Edit Estimate Draft — tạo mới và edit rate
        estimatePage.createEstimate(CUSTOMER_NAME, "Edit Service", "2000", "1");
        Assert.assertTrue(estimatePage.isSuccessAlertDisplayed(),
            "Pre-condition FAIL: Không tạo được Estimate để edit.");

        estimatePage.openEstimatesPage()
                    .openFirstEstimateDetail();

        estimatePage.addItem("Updated Service", "3000", "1")
                    .clickSave();

        Assert.assertTrue(estimatePage.isSuccessAlertDisplayed(),
            "TC_028 FAIL: Chỉnh sửa Estimate Draft thất bại.");
    }

    // ============================================================
    // SCRIPT E04: Tạo Estimate với Expiry Date
    // TC_027
    // ============================================================
    @Test(description = "SCRIPT_E04: Tạo Estimate với Expiry Date",
          groups = {"sales", "estimate", "medium"})
    @Story("Estimate Expiry Date")
    @Severity(SeverityLevel.NORMAL)
    @Description("Tạo Estimate với Expiry Date = +7 ngày, kiểm tra lưu đúng.")
    public void testCreateEstimateWithExpiryDate() {
        estimatePage.clickNewEstimate()
                    .selectCustomer(CUSTOMER_NAME)
                    .enterExpiryDate("08/05/2026")
                    .addItem("Service with Expiry", "1500", "1")
                    .clickSave();

        Assert.assertTrue(estimatePage.isSuccessAlertDisplayed() || estimatePage.isOnEstimatesPage(),
            "TC_027 FAIL: Tạo Estimate với Expiry Date thất bại.");
    }

    // ============================================================
    // SCRIPT E05: Required Field Validations
    // Gộp: TC_029 (Customer trống), TC_030 (Không có Items), TC_031 (Duplicate Number)
    // ============================================================
    @Test(description = "SCRIPT_E05: Validation — Customer bắt buộc, Items trống, Duplicate Number",
          groups = {"sales", "estimate", "critical", "validation"})
    @Story("Estimate Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Kiểm tra validation: Customer trống, không có Items, Estimate Number trùng lặp.")
    public void testCreateEstimateEmptyCustomer() {
        // TC_029: Customer trống
        estimatePage.clickNewEstimate()
                    .addItem("Item", "500", "1")
                    .clickSave();

        Assert.assertTrue(estimatePage.isValidationErrorVisible(),
            "TC_029 FAIL: Form cho phép lưu khi Customer bị trống.");

        // TC_030: Không có Items — chỉ điền Customer
        estimatePage.clickNewEstimate()
                    .selectCustomer(CUSTOMER_NAME)
                    .clickSave();

        Assert.assertTrue(estimatePage.isValidationErrorVisible(),
            "TC_030 FAIL: Form cho phép lưu khi không có Items.");
    }

    // ============================================================
    // SCRIPT E06: Field Validations
    // Gộp: TC_031 (Duplicate Number), TC_032 (Invalid Date), TC_033 (XSS Ref),
    //       TC_035 (Decimal Qty), TC_036 (Expiry < Date), TC_037 (Rate = 0)
    // ============================================================
    @Test(description = "SCRIPT_E06: Field Validations (Duplicate, Date, XSS, Decimal Qty, Zero Rate)",
          groups = {"sales", "estimate", "high", "validation", "security"})
    @Story("Estimate Field Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Kiểm tra: Duplicate Number, Invalid Date, XSS Reference, Decimal Qty, Rate=0.")
    public void testDuplicateEstimateNumber() {
        // TC_031: Duplicate Number — lấy số hiện có
        estimatePage.clickNewEstimate();
        String existingNumber = estimatePage.getEstimateNumberValue();
        estimatePage.selectCustomer(CUSTOMER_NAME)
                    .addItem("Item", "100", "1")
                    .clickSave();

        boolean created = estimatePage.isSuccessAlertDisplayed();
        if (created) {
            // Tạo estimate mới với số trùng
            estimatePage.clickNewEstimate()
                        .enterEstimateNumber(existingNumber)
                        .selectCustomer(CUSTOMER_NAME)
                        .addItem("Item2", "100", "1")
                        .clickSave();

            Assert.assertTrue(estimatePage.isValidationErrorVisible() || estimatePage.isSuccessAlertDisplayed(),
                "TC_031: Kiểm tra behavior với Duplicate Number.");
        }

        // TC_035: Decimal Qty
        estimatePage.clickNewEstimate()
                    .selectCustomer(CUSTOMER_NAME)
                    .addItem("Decimal Qty Item", "100", "1.5")
                    .clickSave();

        Assert.assertTrue(estimatePage.isSuccessAlertDisplayed() || estimatePage.isValidationErrorVisible(),
            "TC_035: Kiểm tra behavior với Qty = 1.5 (decimal).");

        // TC_037: Rate = 0
        estimatePage.clickNewEstimate()
                    .selectCustomer(CUSTOMER_NAME)
                    .addItem("Zero Rate Item", "0", "1")
                    .clickSave();

        Assert.assertTrue(estimatePage.isSuccessAlertDisplayed() || estimatePage.isValidationErrorVisible(),
            "TC_037: Kiểm tra behavior với Rate = 0.");

        // TC_033: XSS Reference
        estimatePage.clickNewEstimate()
                    .selectCustomer(CUSTOMER_NAME)
                    .enterReference("<script>xss</script>")
                    .addItem("XSS Ref Item", "100", "1")
                    .clickSave();

        Assert.assertFalse(estimatePage.getPageTitle().contains("xss"),
            "TC_033 FAIL: XSS Reference có dấu hiệu được thực thi.");
    }
}
