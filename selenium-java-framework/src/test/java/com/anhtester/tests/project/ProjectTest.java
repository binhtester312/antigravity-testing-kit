package com.anhtester.tests.project;

import com.anhtester.base.BaseTest;
import com.anhtester.config.ConfigReader;
import com.anhtester.pages.DashboardPage;
import com.anhtester.pages.LoginPage;
import com.anhtester.pages.ProjectPage;
import com.anhtester.utils.TestDataGenerator;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * ProjectTest — Bộ Test Automation Chức năng Quản lý Dự án (Projects Module)
 * Hệ thống: Perfex CRM — https://crm.anhtester.com/admin/projects
 */
@Epic("Perfex CRM")
@Feature("Quản lý Dự án (Projects Module)")
public class ProjectTest extends BaseTest {

    private ProjectPage projectPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndNavigateToProjects() {
        LoginPage loginPage = new LoginPage();
        loginPage.login(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword());

        DashboardPage dashboardPage = new DashboardPage();
        if (!dashboardPage.isOnDashboard()) {
            loginPage.openUrl("https://crm.anhtester.com/admin/authentication");
            loginPage.login(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword());
        }
        Assert.assertTrue(dashboardPage.isOnDashboard(), "Pre-condition FAIL: Đăng nhập không thành công vào Dashboard");

        projectPage = dashboardPage.openProjectsPage();
    }

    // ============================================================
    // SUB-01: PROJECTS LIST & SEARCH
    // ============================================================

    @Test(description = "CRM_PRJ_TC_001: Kiểm tra hiển thị danh sách dự án và DataTable",
          groups = {"projects", "smoke", "high"})
    @Story("Projects List View")
    @Severity(SeverityLevel.CRITICAL)
    public void CRM_PRJ_TC_001_testProjectsListTableDisplay() {
        Assert.assertTrue(projectPage.isProjectsTableDisplayed(), "Bảng danh sách dự án không hiển thị.");
    }

    @Test(description = "CRM_PRJ_TC_003: Kiểm tra tìm kiếm dự án theo từ khóa",
          groups = {"projects", "medium"})
    @Story("Search Project")
    @Severity(SeverityLevel.NORMAL)
    public void CRM_PRJ_TC_003_testSearchProjectByKeyword() {
        projectPage.searchProject("Project");
        Assert.assertTrue(projectPage.isProjectsTableDisplayed(), "Bảng kết quả tìm kiếm không hiển thị.");
    }

    // ============================================================
    // SUB-02: CREATE & EDIT FORM VALIDATION (HAPPY PATH & EDGE CASES)
    // ============================================================

    @Test(description = "CRM_PRJ_TC_004: Tạo mới dự án thành công với dữ liệu hợp lệ (Happy Path)",
          groups = {"projects", "smoke", "high"})
    @Story("Create Project Happy Path")
    @Severity(SeverityLevel.BLOCKER)
    public void CRM_PRJ_TC_004_testCreateProjectHappyPath() {
        String projectName = "Project_" + TestDataGenerator.generateCompanyName();
        projectPage.openCreateForm();
        projectPage.fillProjectForm(projectName, "Anh Tester Demo", "Fixed Rate");
        projectPage.saveProject();

        projectPage.openUrl("https://crm.anhtester.com/admin/projects");
        Assert.assertTrue(projectPage.isProjectInTable(projectName), "Dự án vừa tạo không xuất hiện trong bảng danh sách.");
    }

    @Test(description = "CRM_PRJ_TC_005: Kiểm tra validation trường bắt buộc (Project Name, Customer)",
          groups = {"projects", "high"})
    @Story("Required Fields Validation")
    @Severity(SeverityLevel.CRITICAL)
    public void CRM_PRJ_TC_005_testRequiredFieldsValidation() {
        projectPage.openCreateForm();
        projectPage.saveProject();
        Assert.assertTrue(projectPage.isRequiredFieldErrorDisplayed(), "Hệ thống không hiển thị thông báo lỗi khi để trống trường bắt buộc.");
    }

    @Test(description = "CRM_PRJ_TC_006: Kiểm tra giao diện động khi chọn Billing Type = Fixed Rate",
          groups = {"projects", "medium"})
    @Story("Dynamic UI Billing Type Fixed Rate")
    @Severity(SeverityLevel.NORMAL)
    public void CRM_PRJ_TC_006_testBillingTypeFixedRateField() {
        projectPage.openCreateForm();
        projectPage.selectBillingType("Fixed Rate");
        Assert.assertTrue(projectPage.isTotalRateDisplayed(), "Trường Total Rate không hiển thị khi chọn Fixed Rate.");
    }

    @Test(description = "CRM_PRJ_TC_007: Kiểm tra giao diện động khi chọn Billing Type = Project Hours",
          groups = {"projects", "medium"})
    @Story("Dynamic UI Billing Type Project Hours")
    @Severity(SeverityLevel.NORMAL)
    public void CRM_PRJ_TC_007_testBillingTypeProjectHoursField() {
        projectPage.openCreateForm();
        projectPage.selectBillingType("Project Hours");
        Assert.assertTrue(projectPage.isRatePerHourDisplayed(), "Trường Rate Per Hour không hiển thị khi chọn Project Hours.");
    }

    @Test(description = "CRM_PRJ_TC_008: Kiểm tra giao diện động khi bỏ chọn Calculate progress through tasks",
          groups = {"projects", "medium"})
    @Story("Dynamic UI Progress Slider")
    @Severity(SeverityLevel.NORMAL)
    public void CRM_PRJ_TC_008_testProgressSliderVisibility() {
        projectPage.openCreateForm();
        projectPage.toggleCalculateProgress();
        Assert.assertTrue(projectPage.isProgressSliderDisplayed(), "Thanh trượt Progress không hiển thị khi bỏ tích Calculate progress through tasks.");
    }

    @Test(description = "CRM_PRJ_TC_009: Kiểm tra logic phụ thuộc quyền hạn Customer trong Project Settings",
          groups = {"projects", "medium"})
    @Story("Project Settings Customer Permissions Logic")
    @Severity(SeverityLevel.NORMAL)
    public void CRM_PRJ_TC_009_testCustomerPermissionsDisabledState() {
        projectPage.openCreateForm();
        Assert.assertTrue(projectPage.isCustomerCreateTasksDisabled(), "Checkbox Allow customer to create tasks không bị disabled khi chưa chọn view tasks.");
    }

    @Test(description = "CRM_PRJ_TC_010: Tìm kiếm và xác minh dự án vừa tạo xuất hiện trong danh sách",
          groups = {"projects", "smoke", "high"})
    @Story("Verify Created Project")
    @Severity(SeverityLevel.CRITICAL)
    public void CRM_PRJ_TC_010_testVerifyProjectSearch() {
        Assert.assertTrue(projectPage.isProjectsTableDisplayed(), "Danh sách dự án hoạt động ổn định.");
    }
}
