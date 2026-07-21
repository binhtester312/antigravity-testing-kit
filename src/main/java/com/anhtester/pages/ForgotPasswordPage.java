package com.anhtester.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * ForgotPasswordPage — Page Object cho trang quên mật khẩu Perfex CRM
 * URL: https://crm.anhtester.com/admin/authentication/forgot_password
 */
public class ForgotPasswordPage extends BasePage {

    // ============ Locators ============

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(css = "input[type='submit'], button[type='submit']")
    private WebElement confirmButton;

    @FindBy(css = ".alert-success, .alert.alert-success")
    private WebElement successAlert;

    @FindBy(css = ".alert-danger, .alert.alert-danger")
    private WebElement errorAlert;

    // ============ Actions ============

    @Step("Nhập email vào form Forgot Password: {email}")
    public ForgotPasswordPage enterEmail(String email) {
        clearAndType(emailInput, email);
        return this;
    }

    @Step("Click nút Confirm")
    public void clickConfirm() {
        click(confirmButton);
    }

    @Step("Submit Forgot Password với email: {email}")
    public void submitForgotPassword(String email) {
        enterEmail(email);
        clickConfirm();
    }

    // ============ Assertions / Getters ============

    @Step("Kiểm tra thông báo thành công có hiển thị không")
    public boolean isSuccessMessageDisplayed() {
        return isDisplayed(successAlert);
    }

    @Step("Lấy nội dung thông báo thành công")
    public String getSuccessMessage() {
        try {
            return getText(successAlert);
        } catch (Exception e) {
            return "";
        }
    }

    @Step("Lấy nội dung thông báo lỗi")
    public String getErrorMessage() {
        try {
            return getText(errorAlert);
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isOnForgotPasswordPage() {
        return getCurrentUrl().contains("forgot_password");
    }
}
