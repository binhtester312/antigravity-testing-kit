package com.anhtester.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * DashboardPage — Page Object cho trang Dashboard sau khi đăng nhập thành công
 * URL: https://crm.anhtester.com/admin/
 */
public class DashboardPage extends BasePage {

    // ============ Locators ============

    // Tiêu đề trang Dashboard
    @FindBy(css = "h1.page-title, h4.page-title, .page-title")
    private WebElement pageTitle;

    // Avatar / Profile dropdown góc trên phải
    @FindBy(css = ".header-right .dropdown-toggle, .topnav-user, img.rounded-circle")
    private WebElement profileDropdown;

    // Nút Logout trong dropdown
    @FindBy(css = "a[href*='logout'], .dropdown-menu a[href*='logout']")
    private WebElement logoutLink;

    // Sidebar navigation (để xác nhận đã vào dashboard)
    @FindBy(css = "#sidebar, .sidebar, .nav-sidebar")
    private WebElement sidebar;

    // ============ Actions ============

    @Step("Click vào avatar/profile dropdown")
    public DashboardPage clickProfileDropdown() {
        click(profileDropdown);
        return this;
    }

    @Step("Click Logout")
    public void clickLogout() {
        // Click profile dropdown trước, sau đó click logout
        clickProfileDropdown();
        click(logoutLink);
    }

    // ============ Assertions / Getters ============

    @Step("Lấy tiêu đề trang Dashboard")
    public String getPageHeading() {
        try {
            return getText(pageTitle);
        } catch (Exception e) {
            return "";
        }
    }

    @Step("Kiểm tra đang ở trang Dashboard")
    public boolean isOnDashboard() {
        return getCurrentUrl().contains("/admin/") &&
               !getCurrentUrl().contains("authentication");
    }

    @Step("Kiểm tra Dashboard title hiển thị")
    public boolean isDashboardTitleVisible() {
        try {
            return pageTitle.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Kiểm tra sidebar hiển thị (xác nhận đã đăng nhập)")
    public boolean isSidebarVisible() {
        return isDisplayed(sidebar);
    }
}
