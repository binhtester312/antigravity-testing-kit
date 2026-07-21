package com.anhtester.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * ForgotPasswordPage — Page Object cho trang Quên mật khẩu
 * URL: https://crm.anhtester.com/admin/authentication/forgot_password
 */
public class ForgotPasswordPage extends BasePage {

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(css = "input[type='submit'], button[type='submit']")
    private WebElement confirmButton;

    @FindBy(css = ".alert-success, .alert.alert-success")
    private WebElement successAlert;

    @FindBy(css = ".alert-danger, .alert.alert-danger")
    private WebElement errorAlert;

    // ================================================================
    // Actions
    // ================================================================

    @Step("Nhập email Forgot Password: [{email}]")
    public ForgotPasswordPage enterEmail(String email) {
        log.info("Forgot Password — nhập email: {}", email);
        clearAndType(emailInput, email);
        return this;
    }

    @Step("Click nút Confirm")
    public void clickConfirm() {
        log.info("Click Confirm button");
        click(confirmButton);
    }

    @Step("Submit Forgot Password: email=[{email}]")
    public void submitForgotPassword(String email) {
        enterEmail(email);
        clickConfirm();
    }

    // ================================================================
    // Queries
    // ================================================================

    public boolean isOnForgotPasswordPage() {
        return getCurrentUrl().contains("forgot_password");
    }

    public boolean isSuccessMessageDisplayed() {
        return isVisibleWithin(successAlert, 5);
    }

    public String getSuccessMessage() {
        try { return getText(successAlert); }
        catch (Exception e) { return ""; }
    }

    public String getErrorMessage() {
        try { return getText(errorAlert); }
        catch (Exception e) { return ""; }
    }
}
