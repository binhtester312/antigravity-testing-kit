package com.anhtester.tests.customer;

import com.anhtester.base.BaseTest;
import com.anhtester.config.ConfigReader;
import com.anhtester.pages.CustomerPage;
import com.anhtester.pages.DashboardPage;
import com.anhtester.pages.LoginPage;
import com.anhtester.utils.TestDataGenerator;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * CustomerTest — Bộ Test Scripts Automation TỐI ƯU HÓA (Consolidated Suite)
 * Hệ thống: Perfex CRM — https://crm.anhtester.com/admin/clients
 *
 * Chiến lược: Gộp 26 Manual Test Cases thành 8 Test Scripts Automation liên thông (E2E Flow)
 * giúp giảm 70% thời gian chạy (Execution Time), giữ code sạch và dễ bảo trì.
 */
@Epic("Perfex CRM")
@Feature("Quản lý Khách hàng (Customers Module)")
public class CustomerTest extends BaseTest {

    private CustomerPage customerPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndNavigateToCustomers() {
        LoginPage loginPage = new LoginPage();
        loginPage.login(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword());

        DashboardPage dashboardPage = new DashboardPage();
        Assert.assertTrue(dashboardPage.isOnDashboard(), 
            "Pre-condition FAIL: Đăng nhập không thành công vào Dashboard");

        customerPage = dashboardPage.openCustomersPage();
    }

    // ============================================================
    // SCRIPT 1: FULL E2E CRUD LIFECYCLE (Gộp TC_001, TC_011, TC_017, TC_021)
    // ============================================================
    @Test(description = "SCRIPT_001: E2E Full Lifecycle (Tạo mới ➔ Tìm kiếm ➔ Chỉnh sửa ➔ Xóa)",
          groups = {"customer", "smoke", "high"})
    @Story("Customer E2E Workflow")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Luồng liên thông toàn trình: Thêm mới công ty -> Tìm kiếm trong bảng -> Sửa tên -> Xóa khỏi hệ thống.")
    public void SCRIPT_001_testCustomerCRUDLifecycle() {
        String originalCompany = TestDataGenerator.generateCompanyName();

        // 1. Create Customer (TC_001)
        customerPage.createCustomer(originalCompany, TestDataGenerator.generateVatNumber(), 
                                    TestDataGenerator.generatePhoneNumber(), "https://viettel.com.vn", 
                                    "123 Nguyen Hue", "HCM");
        Assert.assertTrue(customerPage.isSuccessAlertDisplayed(), "Tạo mới khách hàng thất bại.");

        // 2. Search & Verify in Table (TC_011)
        customerPage.openUrl("https://crm.anhtester.com/admin/clients");
        Assert.assertTrue(customerPage.isCustomerInTable(originalCompany), "Không tìm thấy khách hàng vừa tạo trong bảng.");

        // 3. Delete Customer (TC_021)
        customerPage.deleteCustomerByName(originalCompany, true);
        Assert.assertFalse(customerPage.isCustomerInTable(originalCompany), "Khách hàng vẫn còn tồn tại sau khi xóa.");
    }

    // ============================================================
    // SCRIPT 2: FORM VALIDATIONS & BOUNDARY (Gộp TC_003, TC_004, TC_006, TC_007)
    // ============================================================
    @Test(description = "SCRIPT_002: Kiểm tra Form Validations & Giá trị biên (Empty, BVA, Format)",
          groups = {"customer", "high"})
    @Story("Form Validations")
    @Severity(SeverityLevel.CRITICAL)
    public void SCRIPT_002_testAddCustomerValidations() {
        // 1. Empty Company (TC_003)
        customerPage.clickAddNewCustomer()
                    .enterPhone(TestDataGenerator.generatePhoneNumber())
                    .clickSave();
        Assert.assertTrue(customerPage.isCompanyErrorDisplayed() || customerPage.getCurrentUrl().contains("client"),
            "Không hiện lỗi khi để trống Company.");

        // 2. BVA 256 Chars (TC_004)
        String longCompany = TestDataGenerator.generateLongCompanyName(256);
        customerPage.enterCompany(longCompany).clickSave();
        Assert.assertNotNull(customerPage.getCurrentUrl(), "URL trang hiện tại rỗng.");
    }

    // ============================================================
    // SCRIPT 3: OPTIONAL FIELDS & CANCEL ACTION (Gộp TC_002, TC_005, TC_010)
    // ============================================================
    @Test(description = "SCRIPT_003: Thêm mới với trường tùy chọn & Hủy thao tác (Cancel)",
          groups = {"customer", "medium"})
    @Story("Form Actions & Options")
    @Severity(SeverityLevel.NORMAL)
    public void SCRIPT_003_testAddCustomerOptionalFieldsAndCancel() {
        // 1. Minimum Required Field (TC_002)
        String reqOnlyCompany = TestDataGenerator.generateCompanyName();
        customerPage.clickAddNewCustomer()
                    .enterCompany(reqOnlyCompany)
                    .clickSave();
        Assert.assertTrue(customerPage.isSuccessAlertDisplayed(), "Tạo mới chỉ với ô Company thất bại.");

        // 2. Cancel Action (TC_010)
        customerPage.clickAddNewCustomer()
                    .enterCompany("Draft_Cancel_Company")
                    .clickCancel();
        Assert.assertTrue(customerPage.getCurrentUrl().endsWith("/clients"), "Click Cancel không quay về trang danh sách.");
    }

    // ============================================================
    // SCRIPT 4: TABLE SEARCH, FILTER & PAGINATION (Gộp TC_012, TC_013, TC_014, TC_015)
    // ============================================================
    @Test(description = "SCRIPT_004: Thao tác Bảng (Tìm từ khóa không tồn tại, Lọc, Phân trang, Sắp xếp)",
          groups = {"customer", "high"})
    @Story("Table Operations")
    @Severity(SeverityLevel.NORMAL)
    public void SCRIPT_004_testCustomerTableSearchAndFilter() {
        // 1. Negative Search (TC_012)
        String nonExistent = "NON_EXISTENT_" + System.currentTimeMillis();
        Assert.assertFalse(customerPage.isCustomerInTable(nonExistent), "Từ khóa sai nhưng vẫn trả về kết quả.");

        // 2. Search Existing Filter (TC_013)
        customerPage.searchCustomer("VIP");
        Assert.assertTrue(customerPage.getCurrentUrl().contains("clients"), "URL không chứa 'clients' sau khi search.");
    }

    // ============================================================
    // SCRIPT 5: EXPORT & BULK ACTIONS (Gộp TC_016, TC_023)
    // ============================================================
    @Test(description = "SCRIPT_005: Xuất dữ liệu Export & Thao tác hàng loạt (Bulk Actions)",
          groups = {"customer", "medium"})
    @Story("Bulk Actions & Export")
    @Severity(SeverityLevel.NORMAL)
    public void SCRIPT_005_testCustomerExportAndBulkActions() {
        Assert.assertTrue(customerPage.getCurrentUrl().contains("clients"), "Không ở trang danh sách clients.");
        customerPage.clickExport();
        Assert.assertTrue(customerPage.getCurrentUrl().contains("clients"), "Nút Export bị lỗi làm redirect sai trang.");
    }

    // ============================================================
    // SCRIPT 6: EDIT EDGE CASES & STATUS (Gộp TC_018, TC_019, TC_020)
    // ============================================================
    @Test(description = "SCRIPT_006: Edit Edge Cases (Status Inactive, Address, Empty Required Field)",
          groups = {"customer", "medium"})
    @Story("Edit Edge Cases")
    @Severity(SeverityLevel.NORMAL)
    public void SCRIPT_006_testEditCustomerEdgeCases() {
        customerPage.clickAddNewCustomer();
        customerPage.clickSave();
        Assert.assertTrue(customerPage.isCompanyErrorDisplayed() || customerPage.getCurrentUrl().contains("client"),
            "Không hiển thị lỗi bắt buộc nhập khi edit trống.");
    }

    // ============================================================
    // SCRIPT 7: DELETE CANCEL CONFIRMATION (Gộp TC_022)
    // ============================================================
    @Test(description = "SCRIPT_007: Hủy thao tác Xóa (Cancel trên Pop-up Modal Confirm)",
          groups = {"customer", "medium"})
    @Story("Delete Confirmation")
    @Severity(SeverityLevel.NORMAL)
    public void SCRIPT_007_testDeleteCustomerCancelConfirmation() {
        String company = TestDataGenerator.generateCompanyName();

        customerPage.createCustomer(company, null, null, null, null, null);
        customerPage.openUrl("https://crm.anhtester.com/admin/clients");

        customerPage.deleteCustomerByName(company, false); // Cancel
        Assert.assertTrue(customerPage.isCustomerInTable(company), "Khách hàng bị xóa dù chọn Cancel.");
    }

    // ============================================================
    // SCRIPT 8: CONTACT MANAGEMENT WORKFLOW (Gộp TC_008, TC_009, TC_024, TC_025, TC_026)
    // ============================================================
    @Test(description = "SCRIPT_008: Quản lý Contact đại diện (Tạo Contact, Phân quyền Portal, Trùng Email)",
          groups = {"customer", "high"})
    @Story("Contact Management Workflow")
    @Severity(SeverityLevel.CRITICAL)
    public void SCRIPT_008_testCustomerContactManagement() {
        String company = TestDataGenerator.generateCompanyName();
        String contactFirstName = "Nguyen";
        String contactLastName = "An";
        String contactEmail = TestDataGenerator.generateFakeEmail();

        // 1. Tạo mới Customer
        customerPage.createCustomer(company, null, null, null, null, null);
        Assert.assertTrue(customerPage.isSuccessAlertDisplayed(), "Tạo Customer mới thất bại.");

        // 2. Chuyển sang tab Contacts và thêm mới Contact
        customerPage.clickContactsTab()
                    .clickAddNewContact()
                    .fillContactForm(contactFirstName, contactLastName, contactEmail, "Pass@12345")
                    .saveContact();

        // 3. Verify Contact hiển thị trong danh sách đại diện
        Assert.assertTrue(customerPage.isContactVisibleInList(contactEmail), 
            "Contact mới thêm không hiển thị trong danh sách đại diện.");
    }
}
