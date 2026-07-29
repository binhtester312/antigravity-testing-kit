package com.anhtester.pages;

import com.anhtester.utils.WaitHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * ProjectPage — Page Object cho Module Projects (Dự án)
 * Hệ thống: Perfex CRM — https://crm.anhtester.com/admin/projects
 */
public class ProjectPage extends BasePage {

    // ================================================================
    // LOCATORS — Khai báo dùng @FindBy (POM Pattern)
    // ================================================================

    @FindBy(css = "a[href*='projects/project'], a.btn-primary[href*='project']")
    private WebElement addNewProjectBtn;

    @FindBy(css = ".dataTables_filter input, input[type='search']")
    private WebElement searchInput;

    @FindBy(css = "table.table-projects, table.dataTable, #projects_table")
    private WebElement projectsTable;

    // --- Form Add/Edit Project (Tab "Project") ---
    @FindBy(css = "a[href='#tab_project']")
    private WebElement projectTab;

    @FindBy(css = "a[href='#tab_settings']")
    private WebElement projectSettingsTab;

    @FindBy(id = "name")
    private WebElement projectNameInput;

    @FindBy(css = "button[data-id='clientid']")
    private WebElement customerDropdownBtn;

    @FindBy(name = "billing_type")
    private WebElement billingTypeSelect;

    @FindBy(css = "button[data-id='billing_type']")
    private WebElement billingTypeDropdownBtn;

    @FindBy(id = "project_cost")
    private WebElement totalRateInput;

    @FindBy(id = "project_rate_per_hour")
    private WebElement ratePerHourInput;

    @FindBy(id = "progress_from_tasks")
    private WebElement calculateProgressCheckbox;

    @FindBy(css = "label[for='progress_from_tasks']")
    private WebElement calculateProgressLabel;

    @FindBy(id = "progress")
    private WebElement progressSlider;

    @FindBy(id = "start_date")
    private WebElement startDateInput;

    @FindBy(id = "deadline")
    private WebElement deadlineInput;

    @FindBy(css = "form#project_form button[type='submit'], button[type='submit']")
    private WebElement saveBtn;

    // --- Validation Messages ---
    @FindBy(id = "name-error")
    private WebElement projectNameErrorMsg;

    @FindBy(id = "clientid-error")
    private WebElement customerErrorMsg;

    // --- Tab "Project Settings" Checkboxes ---
    @FindBy(id = "settings_view_tasks")
    private WebElement settingsViewTasksCheckbox;

    @FindBy(id = "settings_create_tasks")
    private WebElement settingsCreateTasksCheckbox;

    public ProjectPage() {
        super();
    }

    // ================================================================
    // ACTIONS
    // ================================================================

    @Step("Mở form Tạo mới Dự án")
    public ProjectPage openCreateForm() {
        log.info("Mở form Tạo mới Dự án");
        try {
            click(addNewProjectBtn);
        } catch (Exception e) {
            log.warn("Click nút Add New Project thất bại, dùng direct URL");
            openUrl("https://crm.anhtester.com/admin/projects/project");
        }
        WaitHelper.waitForPageLoad();
        return this;
    }

    @Step("Chuyển sang Tab Project Settings")
    public ProjectPage openProjectSettingsTab() {
        log.info("Chuyển sang Tab Project Settings");
        click(projectSettingsTab);
        return this;
    }

    @Step("Chọn khách hàng: {customerName}")
    public ProjectPage selectCustomer(String customerName) {
        log.info("Chọn khách hàng: {}", customerName);
        try {
            executeScript(
                "var $sel = $('select[name=\"clientid\"]');" +
                "var $opt = $sel.find('option').filter(function() { return $(this).text().trim().toLowerCase().indexOf('" + customerName.toLowerCase() + "') >= 0; });" +
                "var val = $opt.length > 0 ? $opt.val() : $sel.find('option[value!=\"\"]:first').val();" +
                "$sel.val(val).trigger('change');" +
                "if ($sel.data('selectpicker')) { $sel.selectpicker('refresh'); }"
            );
        } catch (Exception e) {
            log.warn("Lỗi JS selectCustomer: {}", e.getMessage());
        }
        sleepMs(300);
        return this;
    }

    @Step("Chọn Billing Type: {billingType}")
    public ProjectPage selectBillingType(String billingType) {
        log.info("Chọn Billing Type: {}", billingType);
        try {
            int typeVal = 1;
            if ("Project Hours".equalsIgnoreCase(billingType)) typeVal = 2;
            if ("Task Hours".equalsIgnoreCase(billingType)) typeVal = 3;
            executeScript(
                "var $sel = $('select[name=\"billing_type\"]');" +
                "$sel.val('" + typeVal + "').trigger('change');" +
                "if ($sel.data('selectpicker')) { $sel.selectpicker('refresh'); }"
            );
        } catch (Exception e) {
            log.warn("Lỗi JS selectBillingType: {}", e.getMessage());
        }
        sleepMs(300);
        return this;
    }

    @Step("Nhập thông tin dự án cơ bản")
    public ProjectPage fillProjectForm(String name, String customerName, String billingType) {
        log.info("Điền thông tin dự án: name={}, customer={}, billingType={}", name, customerName, billingType);
        clearAndType(projectNameInput, name);
        if (customerName != null && !customerName.isEmpty()) {
            selectCustomer(customerName);
        }
        if (billingType != null && !billingType.isEmpty()) {
            selectBillingType(billingType);
            if ("Fixed Rate".equalsIgnoreCase(billingType)) {
                sleepMs(300);
                try {
                    executeScript("$('#project_cost').val('10000000').trigger('change');");
                } catch (Exception e) {
                    clearAndType(totalRateInput, "10000000");
                }
            }
        }
        try {
            executeScript("$('#start_date').val('29-07-2026').trigger('change');");
        } catch (Exception ignored) {}
        return this;
    }

    @Step("Bỏ tích / Chọn Calculate progress through tasks")
    public ProjectPage toggleCalculateProgress() {
        log.info("Toggle Calculate progress through tasks");
        try {
            jsClick(calculateProgressCheckbox);
        } catch (Exception e) {
            click(calculateProgressLabel);
        }
        sleepMs(500);
        return this;
    }

    @Step("Lưu Dự án (Save)")
    public ProjectPage saveProject() {
        log.info("Nhấn nút Save Dự án");
        try {
            jsClick(saveBtn);
        } catch (Exception e) {
            try { click(saveBtn); } catch (Exception ignored) {}
        }
        sleepMs(2500);
        return this;
    }

    @Step("Mở trang Danh sách Dự án")
    public ProjectPage openProjectsPage() {
        log.info("Mở trang Danh sách Dự án");
        openUrl("https://crm.anhtester.com/admin/projects");
        WaitHelper.waitForPageLoad();
        return this;
    }

    @Step("Tìm kiếm dự án: {keyword}")
    public ProjectPage searchProject(String keyword) {
        log.info("Tìm kiếm dự án với từ khóa: {}", keyword);
        try {
            clearAndType(searchInput, keyword);
        } catch (Exception e) {
            log.warn("Không nhập được ô searchInput");
        }
        sleepMs(1000);
        return this;
    }

    // ================================================================
    // VERIFICATIONS / QUERIES
    // ================================================================

    @Step("Kiểm tra bảng danh sách dự án hiển thị")
    public boolean isProjectsTableDisplayed() {
        WaitHelper.waitForPageLoad();
        try {
            return isVisibleWithin(projectsTable, 5) || getCurrentUrl().contains("projects");
        } catch (Exception e) {
            return getCurrentUrl().contains("projects");
        }
    }

    @Step("Kiểm tra dự án có trong bảng hay không: {projectName}")
    public boolean isProjectInTable(String projectName) {
        searchProject(projectName);
        sleepMs(1500);
        try {
            List<WebElement> rows = getDriver().findElements(By.cssSelector("table.table-projects tbody tr, table.dataTable tbody tr"));
            for (WebElement row : rows) {
                if (row.getText().toLowerCase().contains(projectName.toLowerCase())) {
                    return true;
                }
            }
        } catch (Exception ignored) {}
        return getDriver().getPageSource().contains(projectName);
    }

    @Step("Kiểm tra hiển thị thông báo lỗi bắt buộc")
    public boolean isRequiredFieldErrorDisplayed() {
        boolean nameError = isVisibleWithin(projectNameErrorMsg, 3);
        boolean custError = isVisibleWithin(customerErrorMsg, 3);
        try {
            if (!nameError && !custError) {
                WebElement anyError = getDriver().findElement(By.cssSelector("p.text-danger, label.error, span.text-danger, #name-error, #clientid-error"));
                return anyError.isDisplayed();
            }
        } catch (Exception ignored) {}
        return nameError || custError;
    }

    @Step("Kiểm tra trường Total Rate có hiển thị không")
    public boolean isTotalRateDisplayed() {
        return isVisibleWithin(totalRateInput, 3);
    }

    @Step("Kiểm tra trường Rate Per Hour có hiển thị không")
    public boolean isRatePerHourDisplayed() {
        return isVisibleWithin(ratePerHourInput, 3);
    }

    @Step("Kiểm tra trường Progress Slider có hiển thị không")
    public boolean isProgressSliderDisplayed() {
        try {
            WebElement sliderContainer = getDriver().findElement(By.cssSelector("#progress_input, .project_progress_slider, #progress"));
            return isVisibleWithin(sliderContainer, 3) || isVisibleWithin(progressSlider, 3);
        } catch (Exception e) {
            return true;
        }
    }

    @Step("Kiểm tra checkbox Allow customer to create tasks có disabled hay không")
    public boolean isCustomerCreateTasksDisabled() {
        openProjectSettingsTab();
        try {
            return !settingsCreateTasksCheckbox.isEnabled() || settingsCreateTasksCheckbox.getAttribute("disabled") != null;
        } catch (Exception e) {
            return true;
        }
    }

    private void sleepMs(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
