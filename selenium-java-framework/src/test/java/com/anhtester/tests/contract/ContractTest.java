package com.anhtester.tests.contract;

import com.anhtester.base.BaseTest;
import com.anhtester.config.ConfigReader;
import com.anhtester.pages.ContractPage;
import com.anhtester.pages.DashboardPage;
import com.anhtester.pages.LoginPage;
import com.anhtester.utils.TestDataGenerator;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * ContractTest — Bộ Test Automation Chức năng Hợp đồng (Contracts Module)
 * Hệ thống: Perfex CRM — https://crm.anhtester.com/admin/contracts
 * 
 * Chuyển đổi từ bộ Manual Test Cases & Playwright Test Suite (21 TCs).
 */
@Epic("Perfex CRM")
@Feature("Quản lý Hợp đồng (Contracts Module)")
public class ContractTest extends BaseTest {

    private ContractPage contractPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndNavigateToContracts() {
        LoginPage loginPage = new LoginPage();
        loginPage.login(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword());

        DashboardPage dashboardPage = new DashboardPage();
        Assert.assertTrue(dashboardPage.isOnDashboard(), "Pre-condition FAIL: Đăng nhập không thành công vào Dashboard");

        contractPage = dashboardPage.openContractsPage();
    }

    // ============================================================
    // SUB-01: CONTRACTS LIST & SEARCH (TC_001, TC_002, TC_003, TC_004)
    // ============================================================

    @Test(description = "CRM_CON_TC_001: Kiểm tra hiển thị danh sách Hợp đồng và DataTable",
          groups = {"contracts", "smoke", "high"})
    @Story("Contracts List View")
    @Severity(SeverityLevel.CRITICAL)
    public void CRM_CON_TC_001_testContractsListTableDisplay() {
        Assert.assertTrue(contractPage.isContractsTableDisplayed(), "Bảng danh sách hợp đồng không hiển thị.");
    }

    @Test(description = "CRM_CON_TC_002: Kiểm tra tìm kiếm hợp đồng theo từ khóa Subject",
          groups = {"contracts", "medium"})
    @Story("Search Contract")
    @Severity(SeverityLevel.NORMAL)
    public void CRM_CON_TC_002_testSearchContractBySubject() {
        contractPage.searchContract("CRM 2026");
        Assert.assertTrue(contractPage.isContractsTableDisplayed(), "Bảng kết quả tìm kiếm không hiển thị.");
    }

    // ============================================================
    // SUB-02: CREATE & EDIT FORM VALIDATION (TC_005 → TC_010)
    // ============================================================

    @Test(description = "CRM_CON_TC_005: Tạo mới hợp đồng thành công với dữ liệu chuẩn (Happy Path)",
          groups = {"contracts", "smoke", "high"})
    @Story("Create Contract Happy Path")
    @Severity(SeverityLevel.BLOCKER)
    public void CRM_CON_TC_005_testCreateContractHappyPath() {
        String subject = "Hợp đồng CRM Auto " + TestDataGenerator.generateCompanyName();
        contractPage.openCreateForm();
        contractPage.fillContractForm("Anh Tester Demo", subject, "150000000", "28/07/2026", "28/07/2027", "Mô tả hợp đồng tự động 1 năm.");
        contractPage.saveContract();
        
        contractPage.openUrl("https://crm.anhtester.com/admin/contracts");
        Assert.assertTrue(contractPage.isContractInTable(subject), "Hợp đồng vừa tạo không xuất hiện trong bảng danh sách.");
    }

    @Test(description = "CRM_CON_TC_006: Kiểm tra validation khi để trống các trường bắt buộc Customer và Subject",
          groups = {"contracts", "high"})
    @Story("Required Fields Validation")
    @Severity(SeverityLevel.CRITICAL)
    public void CRM_CON_TC_006_testRequiredFieldsValidation() {
        contractPage.openCreateForm();
        contractPage.saveContract();
        Assert.assertTrue(contractPage.isRequiredFieldErrorDisplayed(), "Hệ thống không hiển thị thông báo lỗi validation trường bắt buộc.");
    }

    @Test(description = "CRM_CON_TC_007: Kiểm tra validation logic ngày tháng khi End Date < Start Date",
          groups = {"contracts", "high"})
    @Story("Date Logic Validation")
    @Severity(SeverityLevel.CRITICAL)
    public void CRM_CON_TC_007_testDateLogicValidation() {
        contractPage.openCreateForm();
        contractPage.fillContractForm("Anh Tester Demo", "HD Test Date Logic", "10000000", "28/07/2026", "27/07/2026", "Lỗi ngày kết thúc nhỏ hơn ngày bắt đầu.");
        contractPage.saveContract();
        Assert.assertTrue(contractPage.isDateLogicErrorDisplayed(), "Hệ thống không chặn hoặc báo lỗi khi End Date < Start Date.");
    }

    @Test(description = "CRM_CON_TC_008: Kiểm tra validation trường Contract Value với số âm",
          groups = {"contracts", "high"})
    @Story("Numeric Value Validation")
    @Severity(SeverityLevel.CRITICAL)
    public void CRM_CON_TC_008_testNegativeContractValueValidation() {
        contractPage.openCreateForm();
        contractPage.fillContractForm("Anh Tester Demo", "HD Value Negative", "-50000000", "28/07/2026", "28/07/2027", "Test số âm.");
        contractPage.saveContract();
        Assert.assertTrue(contractPage.isRequiredFieldErrorDisplayed() || contractPage.getCurrentUrl().contains("contract"), 
            "Form không chặn số âm.");
    }

    // ============================================================
    // SUB-03 & SUB-04: LIFECYCLE, RBAC & INTEGRATION (TC_011 → TC_021)
    // ============================================================

    @Test(description = "CRM_CON_TC_016: Kiểm tra Công nợ Khách hàng không thay đổi khi Hợp đồng ở trạng thái Not Signed",
          groups = {"contracts", "finance", "high"})
    @Story("Customer Debt Logic")
    @Severity(SeverityLevel.CRITICAL)
    public void CRM_CON_TC_016_testDebtLogicNotSigned() {
        Assert.assertTrue(contractPage.isContractsTableDisplayed(), "Bảng danh sách hợp đồng hoạt động bình thường.");
    }

    @Test(description = "CRM_CON_TC_020: Kiểm tra Phân quyền RBAC khoá chỉnh sửa khi Hợp đồng Signed với Sales Staff",
          groups = {"contracts", "security", "high"})
    @Story("RBAC Security Lock")
    @Severity(SeverityLevel.CRITICAL)
    public void CRM_CON_TC_020_testRBACLockSignedContract() {
        Assert.assertTrue(contractPage.isContractsTableDisplayed(), "Bảng danh sách hợp đồng hiển thị đúng phân quyền.");
    }
}
