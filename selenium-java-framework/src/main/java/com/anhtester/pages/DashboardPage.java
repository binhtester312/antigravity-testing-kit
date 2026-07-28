package com.anhtester.pages;

import com.anhtester.utils.WaitHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * DashboardPage — Page Object cho trang Dashboard sau login
 * URL: https://crm.anhtester.com/admin/
 */
public class DashboardPage extends BasePage {

    @FindBy(css = "h1.page-title, h4.page-title, .page-title")
    private WebElement pageTitle;

    @FindBy(css = "li.header-user-profile a.profile, a.profile, .header-user-profile a")
    private WebElement profileDropdown;

    @FindBy(css = "li.header-logout a, .header-logout a")
    private WebElement logoutLink;

    @FindBy(css = "#sidebar, .sidebar, .nav-sidebar, .app-sidebar")
    private WebElement sidebar;

    @FindBy(css = "li.menu-item-clients > a, li.menu-item-customers > a, #sidebar a[href*='clients'], a[href*='admin/clients']")
    private WebElement customersMenu;

    // ================================================================
    // Actions
    // ================================================================

    @Step("Chuyển đến trang Customers")
    public CustomerPage openCustomersPage() {
        log.info("Chuyển đến trang Customers");
        try {
            if (isVisibleWithin(customersMenu, 3)) {
                click(customersMenu);
            } else {
                openUrl("https://crm.anhtester.com/admin/clients");
            }
        } catch (Exception e) {
            log.warn("Click menu Customers bị lỗi, tự động dùng direct URL navigation fallback");
            openUrl("https://crm.anhtester.com/admin/clients");
        }
        return new CustomerPage();
    }

    @Step("Click avatar / profile dropdown")
    public DashboardPage clickProfileDropdown() {
        log.info("Click profile dropdown");
        click(profileDropdown);
        return this;
    }

    @Step("Logout khỏi hệ thống")
    public void logout() {
        log.info("Thực hiện logout");
        try {
            clickProfileDropdown();
            click(logoutLink);
        } catch (Exception e) {
            log.warn("Click menu Logout bị vướng giao diện, tự động chuyển hướng sang URL Logout");
            openUrl("https://crm.anhtester.com/admin/authentication/logout");
        }
    }

    // ================================================================
    // Queries
    // ================================================================

    @Step("Kiểm tra đang ở Dashboard")
    public boolean isOnDashboard() {
        WaitHelper.waitForPageLoad();
        for (int i = 0; i < 15; i++) {
            String url = getCurrentUrl();
            if (url.contains("/admin") && !url.contains("authentication")) {
                log.info("isOnDashboard PASS: URL={}", url);
                return true;
            }
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        }
        String finalUrl = getCurrentUrl();
        boolean result = finalUrl.contains("/admin") && !finalUrl.contains("authentication");
        log.info("isOnDashboard Result: {} (URL={})", result, finalUrl);
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
