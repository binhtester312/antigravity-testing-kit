package com.anhtester.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * LoginPage — Page Object cho trang đăng nhập Perfex CRM
 * URL: https://crm.anhtester.com/admin/authentication
 *
 * Locators được khai báo tại đây, KHÔNG inline trong test.
 */
public class LoginPage extends BasePage {

    // ================================================================
    // Locators — @FindBy (không dùng XPath tuyệt đối)
    // ================================================================

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(css = "#remember, input[name='remember'], input[name='remember_me']")
    private WebElement rememberMeCheckbox;

    @FindBy(css = "input[type='submit'], button[type='submit']")
    private WebElement loginButton;

    @FindBy(css = "a[href*='forgot_password']")
    private WebElement forgotPasswordLink;

    // Server-side error (sau khi submit)
    @FindBy(css = ".alert-danger, .alert.alert-danger, div[class*='alert-danger']")
    private WebElement serverErrorAlert;

    // Field-level validation errors
    @FindBy(css = ".form-group:has(#email) .help-block, #email ~ .help-block")
    private WebElement emailFieldError;

    @FindBy(css = ".form-group:has(#password) .help-block, #password ~ .help-block")
    private WebElement passwordFieldError;

    // ================================================================
    // Actions — mỗi method mô tả 1 hành vi của người dùng
    // ================================================================

    @Step("Nhập email: [{email}]")
    public LoginPage enterEmail(String email) {
        log.info("Nhập email: {}", email);
        clearAndType(emailInput, email);
        return this;
    }

    @Step("Nhập password")
    public LoginPage enterPassword(String password) {
        log.info("Nhập password (ẩn)");
        clearAndType(passwordInput, password);
        return this;
    }

    @Step("Tick checkbox 'Remember me'")
    public LoginPage checkRememberMe() {
        log.info("Tick Remember me");
        if (!rememberMeCheckbox.isSelected()) {
            click(rememberMeCheckbox);
        }
        return this;
    }

    @Step("Click nút Login")
    public void clickLoginButton() {
        log.info("Click Login button");
        click(loginButton);
    }

    @Step("Click link 'Forgot Password?'")
    public void clickForgotPasswordLink() {
        log.info("Click Forgot Password link");
        click(forgotPasswordLink);
    }

    @Step("Nhấn phím Enter để submit")
    public void pressEnterOnEmailField() {
        log.info("Nhấn Enter trên email field");
        pressKey(emailInput, Keys.ENTER);
    }

    // ================================================================
    // Composite Actions — full user flows
    // ================================================================

    @Step("Đăng nhập: email=[{email}]")
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    @Step("Đăng nhập với Remember me: email=[{email}]")
    public void loginWithRememberMe(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        checkRememberMe();
        clickLoginButton();
    }

    @Step("Đăng nhập bằng phím Enter: email=[{email}]")
    public void loginWithEnterKey(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        pressEnterOnEmailField();
    }

    // ================================================================
    // Queries / Assertions helpers
    // ================================================================

    public boolean isOnLoginPage() {
        return getCurrentUrl().contains("authentication");
    }

    @Step("Lấy thông báo lỗi server-side")
    public String getServerErrorMessage() {
        try { return getText(serverErrorAlert); }
        catch (Exception e) { return ""; }
    }

    public boolean isServerErrorDisplayed() {
        return isVisibleWithin(serverErrorAlert, 3);
    }

    public boolean isEmailFieldErrorDisplayed() {
        return isVisibleWithin(emailFieldError, 2);
    }

    public boolean isPasswordFieldErrorDisplayed() {
        return isVisibleWithin(passwordFieldError, 2);
    }

    @Step("Lấy lỗi validation email field")
    public String getEmailFieldError() {
        try { return getText(emailFieldError); }
        catch (Exception e) { return ""; }
    }

    @Step("Lấy lỗi validation password field")
    public String getPasswordFieldError() {
        try { return getText(passwordFieldError); }
        catch (Exception e) { return ""; }
    }

    public boolean isRememberMeChecked() {
        return rememberMeCheckbox.isSelected();
    }
}
