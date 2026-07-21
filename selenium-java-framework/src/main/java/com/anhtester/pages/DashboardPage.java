package com.anhtester.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * DashboardPage — Page Object cho trang Dashboard sau login
 * URL: https://crm.anhtester.com/admin/
 */
public class DashboardPage extends BasePage {

    @FindBy(css = "h1.page-title, h4.page-title, .page-title")
    private WebElement pageTitle;

    @FindBy(css = ".header-right .dropdown-toggle, .topnav-user, img.rounded-circle, a[data-toggle='dropdown']")
    private WebElement profileDropdown;

    @FindBy(css = "a[href*='logout'], .dropdown-menu a[href*='logout']")
    private WebElement logoutLink;

    @FindBy(css = "#sidebar, .sidebar, .nav-sidebar, .app-sidebar")
    private WebElement sidebar;

    // ================================================================
    // Actions
    // ================================================================

    @Step("Click avatar / profile dropdown")
    public DashboardPage clickProfileDropdown() {
        log.info("Click profile dropdown");
        click(profileDropdown);
        return this;
    }

    @Step("Logout khỏi hệ thống")
    public void logout() {
        log.info("Thực hiện logout");
        clickProfileDropdown();
        click(logoutLink);
    }

    // ================================================================
    // Queries
    // ================================================================

    @Step("Kiểm tra đang ở Dashboard")
    public boolean isOnDashboard() {
        String url = getCurrentUrl();
        boolean result = url.contains("/admin/") && !url.contains("authentication");
        log.debug("isOnDashboard: {} (URL: {})", result, url);
        return result;
    }

    @Step("Lấy tiêu đề trang")
    public String getPageHeading() {
        try { return getText(pageTitle); }
        catch (Exception e) { return ""; }
    }

    public boolean isSidebarVisible() {
        return isVisibleWithin(sidebar, 5);
    }
}
