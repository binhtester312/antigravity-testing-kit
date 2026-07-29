package com.anhtester.tests.dashboard;

import com.anhtester.base.BaseTest;
import com.anhtester.pages.DashboardPage;
import com.anhtester.utils.TestDataGenerator;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * DashboardTest — Bộ Test Automation Chức năng Module Dashboard (Perfex CRM)
 * Hệ thống: Perfex CRM — https://crm.anhtester.com/admin/
 * Mapping: testcases_dashboard.csv
 */
@Epic("Perfex CRM")
@Feature("Module Dashboard")
public class DashboardTest extends BaseTest {

    private DashboardPage dashboardPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndPrepareDashboard() {
        performValidLogin();
        dashboardPage = new DashboardPage();
        Assert.assertTrue(dashboardPage.isOnDashboard(), "Pre-condition FAIL: Không ở trang Dashboard.");
    }

    // ============================================================
    // SUB-01: DASHBOARD OPTIONS & WIDGETS
    // ============================================================

    @Test(description = "CRM_DASHBOARD_TC_001: Tắt hiển thị 1 Widget (Quick Statistics)",
          groups = {"dashboard", "medium"})
    @Story("Dashboard Options")
    @Severity(SeverityLevel.NORMAL)
    public void CRM_DASHBOARD_TC_001_toggleQuickStatsWidgetOff() {
        dashboardPage.openDashboardOptions();
        dashboardPage.toggleQuickStatsWidget();
        Assert.assertTrue(dashboardPage.isOnDashboard(), "Thao tác toggle widget làm sai trạng thái Dashboard.");
    }

    @Test(description = "CRM_DASHBOARD_TC_004: Khôi phục giao diện mặc định bằng Reset Dashboard",
          groups = {"dashboard", "high"})
    @Story("Dashboard Options Reset")
    @Severity(SeverityLevel.CRITICAL)
    public void CRM_DASHBOARD_TC_004_resetDashboardToDefault() {
        dashboardPage.openDashboardOptions();
        dashboardPage.clickResetDashboard();
        Assert.assertTrue(dashboardPage.isOnDashboard(), "Reset Dashboard không giữ người dùng ở trang Dashboard.");
    }

    // ============================================================
    // SUB-02: QUICK STATISTICS
    // ============================================================

    @Test(description = "CRM_DASHBOARD_TC_007: Kiểm tra hiển thị số liệu chỉ số Invoices Awaiting Payment",
          groups = {"dashboard", "smoke", "low"})
    @Story("Quick Statistics Display")
    @Severity(SeverityLevel.NORMAL)
    public void CRM_DASHBOARD_TC_007_verifyQuickStatsInvoicesAwaitingPayment() {
        Assert.assertTrue(dashboardPage.isQuickStatsWidgetDisplayed(), "Widget Quick Statistics không hiển thị.");
    }

    @Test(description = "CRM_DASHBOARD_TC_009: Điều hướng từ thẻ Projects In Progress",
          groups = {"dashboard", "low"})
    @Story("Quick Statistics Navigation")
    @Severity(SeverityLevel.NORMAL)
    public void CRM_DASHBOARD_TC_009_navigateToProjectsInProgress() {
        dashboardPage.clickProjectsInProgressCard();
        Assert.assertTrue(dashboardPage.getCurrentUrl().contains("/projects"),
                "Hệ thống không điều hướng sang trang danh sách dự án.");
    }

    // ============================================================
    // SUB-03: FINANCE OVERVIEW
    // ============================================================

    @Test(description = "CRM_DASHBOARD_TC_011: Lọc dữ liệu tài chính theo năm 2026",
          groups = {"dashboard", "high"})
    @Story("Finance Overview Filter Year")
    @Severity(SeverityLevel.CRITICAL)
    public void CRM_DASHBOARD_TC_011_filterFinanceOverviewYear2026() {
        dashboardPage.filterFinanceOverviewByYear("2026");
        Assert.assertTrue(dashboardPage.isFinanceOverviewWidgetDisplayed(), "Widget Finance Overview không hiển thị.");
    }

    @Test(description = "CRM_DASHBOARD_TC_012: Thay đổi bộ lọc năm tài chính sang 2025",
          groups = {"dashboard", "high"})
    @Story("Finance Overview Filter Year 2025")
    @Severity(SeverityLevel.CRITICAL)
    public void CRM_DASHBOARD_TC_012_filterFinanceOverviewYear2025() {
        dashboardPage.filterFinanceOverviewByYear("2025");
        Assert.assertTrue(dashboardPage.isFinanceOverviewWidgetDisplayed(), "Widget Finance Overview không hiển thị.");
    }

    // ============================================================
    // SUB-04: USER WIDGET
    // ============================================================

    @Test(description = "CRM_DASHBOARD_TC_021: Chuyển đổi qua lại giữa các tab trong User Widget",
          groups = {"dashboard", "high"})
    @Story("User Widget Tabs Switch")
    @Severity(SeverityLevel.CRITICAL)
    public void CRM_DASHBOARD_TC_021_switchUserWidgetTabs() {
        dashboardPage.clickMyTasksTab();
        dashboardPage.clickMyProjectsTab();
        dashboardPage.clickMyTasksTab();
        Assert.assertTrue(dashboardPage.isOnDashboard(), "Giao diện User Widget không đúng.");
    }

    // ============================================================
    // SUB-05: CALENDAR & ADD EVENT
    // ============================================================

    @Test(description = "CRM_DASHBOARD_TC_031: Thay đổi các chế độ xem lịch biểu Calendar",
          groups = {"dashboard", "high"})
    @Story("Calendar Views Switch")
    @Severity(SeverityLevel.CRITICAL)
    public void CRM_DASHBOARD_TC_031_switchCalendarViews() {
        dashboardPage.switchCalendarView("week");
        dashboardPage.switchCalendarView("day");
        dashboardPage.switchCalendarView("month");
        Assert.assertTrue(dashboardPage.isOnDashboard(), "Calendar không hiển thị đúng.");
    }

    @Test(description = "CRM_DASHBOARD_TC_033: Tạo sự kiện hợp lệ trên Calendar",
          groups = {"dashboard", "high"})
    @Story("Add Calendar Event")
    @Severity(SeverityLevel.CRITICAL)
    public void CRM_DASHBOARD_TC_033_addValidCalendarEvent() {
        String eventTitle = "Auto_Event_" + TestDataGenerator.timestamp();
        dashboardPage.openAddEventModal();
        dashboardPage.fillEventForm(eventTitle, "Mô tả sự kiện auto test", "2026-07-30 09:00");
        dashboardPage.saveEvent();
        Assert.assertTrue(dashboardPage.isOnDashboard(), "Tạo sự kiện không giữ người dùng ở Dashboard.");
    }

    // ============================================================
    // SUB-06: MY TO DO ITEMS
    // ============================================================

    @Test(description = "CRM_DASHBOARD_TC_042: Tạo mới To-Do item với nội dung hợp lệ",
          groups = {"dashboard", "medium"})
    @Story("Create To-Do Item")
    @Severity(SeverityLevel.CRITICAL)
    public void CRM_DASHBOARD_TC_042_createNewTodoItem() {
        String todoContent = "Auto_Todo_" + TestDataGenerator.timestamp();
        dashboardPage.createTodoItem(todoContent);
        Assert.assertTrue(dashboardPage.isOnDashboard(), "Tạo mới To-Do item không giữ người dùng ở Dashboard.");
    }

    // ============================================================
    // SUB-10: HEADER TOOLBAR & SECURITY
    // ============================================================

    @Test(description = "CRM_DASHBOARD_TC_056: Tìm kiếm nhanh đối tượng qua thanh #search_input",
          groups = {"dashboard", "high"})
    @Story("Header Quick Search")
    @Severity(SeverityLevel.CRITICAL)
    public void CRM_DASHBOARD_TC_056_quickSearchHeaderInput() {
        dashboardPage.quickSearch("Project_Auto");
        Assert.assertTrue(dashboardPage.isOnDashboard(), "Tìm kiếm trên Header gây lỗi trang.");
    }

    @Test(description = "CRM_DASHBOARD_TC_060: Mở trang cá nhân từ Menu tài khoản",
          groups = {"dashboard", "high"})
    @Story("Profile Navigation")
    @Severity(SeverityLevel.NORMAL)
    public void CRM_DASHBOARD_TC_060_openMyProfile() {
        dashboardPage.openMyProfile();
        Assert.assertTrue(dashboardPage.getCurrentUrl().contains("/staff/profile"),
                "Hệ thống không chuyển hướng sang trang profile.");
    }

    @Test(description = "CRM_DASHBOARD_TC_061: Thực hiện đăng xuất hệ thống từ Menu tài khoản",
          groups = {"dashboard", "high"})
    @Story("Logout Execution")
    @Severity(SeverityLevel.CRITICAL)
    public void CRM_DASHBOARD_TC_061_executeLogout() {
        dashboardPage.logout();
        Assert.assertTrue(dashboardPage.getCurrentUrl().contains("/authentication"),
                "Hệ thống không điều hướng về trang đăng nhập sau khi logout.");
    }
}
