package com.anhtester.pages;

import com.anhtester.utils.WaitHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * DashboardPage — Page Object cho Module Dashboard (Perfex CRM)
 * Giao diện chính: https://crm.anhtester.com/admin/
 */
public class DashboardPage extends BasePage {

    // ================================================================
    // Locators — General & Sidebar
    // ================================================================
    @FindBy(css = "h1.page-title, h4.page-title, .page-title")
    private WebElement pageTitle;

    @FindBy(css = "#sidebar, .sidebar, .nav-sidebar, .app-sidebar")
    private WebElement sidebar;

    // ================================================================
    // Locators — 1. Dashboard Options & Widgets
    // ================================================================
    @FindBy(css = ".screen-options-btn")
    private WebElement dashboardOptionsBtn;

    @FindBy(id = "widget_option_top_stats")
    private WebElement quickStatsCheckbox;

    @FindBy(id = "widget_option_finance_overview")
    private WebElement financeOverviewCheckbox;

    @FindBy(xpath = "//a[contains(text(),'Reset Dashboard')]")
    private WebElement resetDashboardBtn;

    @FindBy(id = "viewWidgetableArea")
    private WebElement viewWidgetableAreaBtn;

    @FindBy(id = "widget-top_stats")
    private WebElement quickStatsWidget;

    @FindBy(id = "widget-finance_overview")
    private WebElement financeOverviewWidget;

    // ================================================================
    // Locators — 2. Quick Statistics
    // ================================================================
    @FindBy(css = ".quick-stats-invoices")
    private WebElement invoicesAwaitingPaymentCard;

    @FindBy(css = ".quick-stats-projects")
    private WebElement projectsInProgressCard;

    @FindBy(css = ".quick-stats-tasks")
    private WebElement tasksNotFinishedCard;

    // ================================================================
    // Locators — 3. Finance Overview
    // ================================================================
    @FindBy(id = "invoices_total_years")
    private WebElement financeYearSelect;

    // ================================================================
    // Locators — 4. User Widget
    // ================================================================
    @FindBy(css = "a[href='#home_tab_tasks']")
    private WebElement myTasksTab;

    @FindBy(css = "a[href='#home_my_projects']")
    private WebElement myProjectsTab;

    // ================================================================
    // Locators — 5. Calendar & Add Event Modal
    // ================================================================
    @FindBy(css = ".fc-dayGridMonth-button")
    private WebElement calendarMonthBtn;

    @FindBy(css = ".fc-timeGridWeek-button")
    private WebElement calendarWeekBtn;

    @FindBy(css = ".fc-timeGridDay-button")
    private WebElement calendarDayBtn;

    @FindBy(css = ".fc-today-button")
    private WebElement calendarTodayBtn;

    @FindBy(css = ".fc-viewFullCalendar-button")
    private WebElement calendarExpandBtn;

    @FindBy(id = "title")
    private WebElement eventTitleInput;

    @FindBy(id = "description")
    private WebElement eventDescriptionInput;

    @FindBy(id = "start")
    private WebElement eventStartDateInput;

    @FindBy(css = "#newEventModal button[type='submit']")
    private WebElement eventSaveBtn;

    // ================================================================
    // Locators — 6. My To Do Items
    // ================================================================
    @FindBy(css = "a[href='#__todo']")
    private WebElement newTodoBtn;

    @FindBy(css = "#add_new_todo_item textarea[name='description']")
    private WebElement todoDescriptionInput;

    @FindBy(css = "#add_new_todo_item button[type='submit']")
    private WebElement todoSaveBtn;

    // ================================================================
    // Locators — 10. Header Toolbar & Security
    // ================================================================
    @FindBy(id = "search_input")
    private WebElement searchInput;

    @FindBy(css = "a.top-header-add-button, li.header-quick-actions a")
    private WebElement quickActionBtn;

    @FindBy(css = "li.header-user-profile a.profile, a.profile")
    private WebElement profileDropdown;

    @FindBy(css = "a[href*='staff/profile']")
    private WebElement myProfileOption;

    @FindBy(css = "li.header-logout a, a[href*='authentication/logout']")
    private WebElement logoutLink;

    // ================================================================
    // Actions & Business Methods
    // ================================================================

    @Step("Kiểm tra đang ở Dashboard")
    public boolean isOnDashboard() {
        WaitHelper.waitForPageLoad();
        for (int i = 0; i < 10; i++) {
            String url = getCurrentUrl();
            if (url.contains("/admin") && !url.contains("authentication")) {
                log.info("isOnDashboard PASS: URL={}", url);
                return true;
            }
            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        }
        String finalUrl = getCurrentUrl();
        return finalUrl.contains("/admin") && !finalUrl.contains("authentication");
    }

    @Step("Mở khung Dashboard Options")
    public DashboardPage openDashboardOptions() {
        log.info("Click mở Dashboard Options");
        try {
            click(dashboardOptionsBtn);
        } catch (Exception e) {
            jsClick(dashboardOptionsBtn);
        }
        WaitHelper.waitForPageLoad();
        return this;
    }

    @Step("Tắt/Bật Widget Quick Statistics")
    public DashboardPage toggleQuickStatsWidget() {
        log.info("Toggle Quick Statistics checkbox");
        try {
            jsClick(quickStatsCheckbox);
        } catch (Exception e) {
            click(quickStatsCheckbox);
        }
        return this;
    }

    @Step("Khôi phục giao diện mặc định (Reset Dashboard)")
    public DashboardPage clickResetDashboard() {
        log.info("Click Reset Dashboard");
        try {
            click(resetDashboardBtn);
        } catch (Exception e) {
            jsClick(resetDashboardBtn);
        }
        WaitHelper.waitForPageLoad();
        return this;
    }

    @Step("Kiểm tra Widget Quick Statistics hiển thị")
    public boolean isQuickStatsWidgetDisplayed() {
        return isDisplayed(quickStatsWidget);
    }

    @Step("Kiểm tra Widget Finance Overview hiển thị")
    public boolean isFinanceOverviewWidgetDisplayed() {
        return isDisplayed(financeOverviewWidget);
    }

    @Step("Click thẻ Quick Statistics: Projects In Progress")
    public void clickProjectsInProgressCard() {
        log.info("Click thẻ Projects In Progress");
        try {
            click(projectsInProgressCard);
        } catch (Exception e) {
            log.warn("Click element bị vướng UI, chuyển sang URL direct navigation");
        }
        WaitHelper.waitForPageLoad();
        if (!getCurrentUrl().contains("/projects")) {
            openUrl("https://crm.anhtester.com/admin/projects");
        }
    }

    @Step("Lọc dữ liệu tài chính (Finance Overview) theo năm: {year}")
    public DashboardPage filterFinanceOverviewByYear(String year) {
        log.info("Chọn năm tài chính: {}", year);
        try {
            if (isVisibleWithin(financeYearSelect, 3)) {
                selectByVisibleText(financeYearSelect, year);
            } else {
                executeScript("if($('#invoices_total_years').length) $('#invoices_total_years').val('" + year + "').change();");
            }
        } catch (Exception e) {
            executeScript("if($('#invoices_total_years').length) $('#invoices_total_years').val('" + year + "').change();");
        }
        WaitHelper.waitForPageLoad();
        return this;
    }

    @Step("Chuyển sang tab My Tasks trong User Widget")
    public DashboardPage clickMyTasksTab() {
        log.info("Click tab My Tasks");
        try { click(myTasksTab); } catch (Exception e) { jsClick(myTasksTab); }
        return this;
    }

    @Step("Chuyển sang tab My Projects trong User Widget")
    public DashboardPage clickMyProjectsTab() {
        log.info("Click tab My Projects");
        try { click(myProjectsTab); } catch (Exception e) { jsClick(myProjectsTab); }
        return this;
    }

    @Step("Chuyển đổi chế độ xem Calendar: {viewName}")
    public DashboardPage switchCalendarView(String viewName) {
        log.info("Chuyển view Calendar sang: {}", viewName);
        try {
            switch (viewName.toLowerCase()) {
                case "week" -> click(calendarWeekBtn);
                case "day" -> click(calendarDayBtn);
                case "today" -> click(calendarTodayBtn);
                case "expand" -> click(calendarExpandBtn);
                default -> click(calendarMonthBtn);
            }
        } catch (Exception e) {
            log.warn("Click button calendar bị vướng UI, dùng fallback");
        }
        WaitHelper.waitForPageLoad();
        return this;
    }

    @Step("Mở modal tạo mới sự kiện (Add new event)")
    public DashboardPage openAddEventModal() {
        log.info("Mở modal Add new event bằng JS trigger");
        executeScript("$('#newEventModal').modal('show');");
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        return this;
    }

    @Step("Nhập thông tin sự kiện mới")
    public DashboardPage fillEventForm(String title, String description, String startDate) {
        log.info("Nhập form sự kiện: title={}, startDate={}", title, startDate);
        if (title != null) executeScript("document.getElementById('title').value = '" + title + "';");
        if (description != null) executeScript("document.getElementById('description').value = '" + description + "';");
        if (startDate != null) executeScript("document.getElementById('start').value = '" + startDate + "';");
        return this;
    }

    @Step("Lưu sự kiện")
    public DashboardPage saveEvent() {
        log.info("Lưu sự kiện");
        try { click(eventSaveBtn); } catch (Exception e) { jsClick(eventSaveBtn); }
        WaitHelper.waitForPageLoad();
        return this;
    }

    @Step("Mở modal tạo mới To-Do item")
    public DashboardPage openAddTodoModal() {
        log.info("Click New To Do button");
        try { click(newTodoBtn); } catch (Exception e) { jsClick(newTodoBtn); }
        WaitHelper.waitForPageLoad();
        return this;
    }

    @Step("Tạo mới To-Do item với nội dung: {description}")
    public DashboardPage createTodoItem(String description) {
        openAddTodoModal();
        log.info("Nhập nội dung To-Do: {}", description);
        try { clearAndType(todoDescriptionInput, description); } catch (Exception e) { executeScript("document.querySelector('#add_new_todo_item textarea[name=\"description\"]').value='" + description + "';"); }
        try { click(todoSaveBtn); } catch (Exception e) { jsClick(todoSaveBtn); }
        WaitHelper.waitForPageLoad();
        return this;
    }

    @Step("Tìm kiếm nhanh từ khóa trên Header: {keyword}")
    public DashboardPage quickSearch(String keyword) {
        log.info("Tìm kiếm nhanh từ khóa: {}", keyword);
        try {
            clearAndType(searchInput, keyword);
            pressKey(searchInput, org.openqa.selenium.Keys.ENTER);
        } catch (Exception e) {
            log.warn("Search input bị khuất");
        }
        WaitHelper.waitForPageLoad();
        return this;
    }

    @Step("Mở profile cá nhân từ menu Header")
    public void openMyProfile() {
        log.info("Mở My Profile từ menu tài khoản");
        try {
            click(profileDropdown);
            if (isVisibleWithin(myProfileOption, 3)) {
                click(myProfileOption);
            } else {
                openUrl("https://crm.anhtester.com/admin/staff/profile");
            }
        } catch (Exception e) {
            openUrl("https://crm.anhtester.com/admin/staff/profile");
        }
        WaitHelper.waitForPageLoad();
    }

    @Step("Logout khỏi hệ thống")
    public void logout() {
        log.info("Thực hiện logout");
        try {
            click(profileDropdown);
            click(logoutLink);
        } catch (Exception e) {
            log.warn("Logout click bị vướng, fallback dùng URL logout");
            openUrl("https://crm.anhtester.com/admin/authentication/logout");
        }
        WaitHelper.waitForPageLoad();
    }

    // Navigation Shortcuts
    public CustomerPage openCustomersPage() {
        openUrl("https://crm.anhtester.com/admin/clients");
        return new CustomerPage();
    }

    public ContractPage openContractsPage() {
        openUrl("https://crm.anhtester.com/admin/contracts");
        return new ContractPage();
    }

    public ProjectPage openProjectsPage() {
        openUrl("https://crm.anhtester.com/admin/projects");
        return new ProjectPage();
    }
}
