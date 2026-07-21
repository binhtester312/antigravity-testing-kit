package com.anhtester.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * LoginPage — Page Object cho trang đăng nhập Perfex CRM
 * URL: https://crm.anhtester.com/admin/authentication
 */
public class LoginPage extends BasePage {

    // ============ Locators ============

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(css = "input[name='remember_me']")
    private WebElement rememberMeCheckbox;

    @FindBy(css = "input[type='submit'], button[type='submit']")
    private WebElement loginButton;

    @FindBy(css = "a[href*='forgot_password']")
    private WebElement forgotPasswordLink;

    // Error messages — server-side (hiển thị trong alert div)
    @FindBy(css = ".alert-danger, .alert.alert-danger")
    private WebElement serverErrorAlert;

    // Validation messages — field-level (dưới input)
    @FindBy(css = "#email ~ .help-block, #email + .help-block, .form-group:has(#email) .help-block")
    private WebElement emailErrorMessage;

    @FindBy(css = "#password ~ .help-block, #password + .help-block, .form-group:has(#password) .help-block")
    private WebElement passwordErrorMessage;

    // ============ Actions ============

    @Step("Nhập email: {email}")
    public LoginPage enterEmail(String email) {
        clearAndType(emailInput, email);
        return this;
    }

    @Step("Nhập password")
    public LoginPage enterPassword(String password) {
        clearAndType(passwordInput, password);
        return this;
    }

    @Step("Tick checkbox 'Remember me'")
    public LoginPage checkRememberMe() {
        if (!rememberMeCheckbox.isSelected()) {
            click(rememberMeCheckbox);
        }
        return this;
    }

    @Step("Click nút Login")
    public void clickLoginButton() {
        click(loginButton);
    }

    @Step("Click link 'Forgot Password?'")
    public void clickForgotPasswordLink() {
        click(forgotPasswordLink);
    }

    @Step("Nhấn phím Enter để submit form")
    public void pressEnterToSubmit() {
        emailInput.sendKeys(org.openqa.selenium.Keys.ENTER);
    }

    // ============ Full Login Flow ============

    @Step("Đăng nhập với email: {email}")
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    @Step("Đăng nhập với 'Remember me': email={email}")
    public void loginWithRememberMe(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        checkRememberMe();
        clickLoginButton();
    }

    @Step("Đăng nhập bằng phím Enter: email={email}")
    public void loginWithEnterKey(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        pressEnterToSubmit();
    }

    // ============ Assertions / Getters ============

    @Step("Lấy thông báo lỗi server-side")
    public String getServerErrorMessage() {
        try {
            return getText(serverErrorAlert);
        } catch (Exception e) {
            return "";
        }
    }

    @Step("Lấy lỗi validation của trường Email")
    public String getEmailErrorMessage() {
        try {
            return getText(emailErrorMessage);
        } catch (Exception e) {
            return "";
        }
    }

    @Step("Lấy lỗi validation của trường Password")
    public String getPasswordErrorMessage() {
        try {
            return getText(passwordErrorMessage);
        } catch (Exception e) {
            return "";
        }
    }

    @Step("Kiểm tra thông báo lỗi server có hiển thị không")
    public boolean isServerErrorDisplayed() {
        return isDisplayed(serverErrorAlert);
    }

    @Step("Kiểm tra lỗi email có hiển thị không")
    public boolean isEmailErrorDisplayed() {
        return isDisplayed(emailErrorMessage);
    }

    @Step("Kiểm tra lỗi password có hiển thị không")
    public boolean isPasswordErrorDisplayed() {
        return isDisplayed(passwordErrorMessage);
    }

    @Step("Kiểm tra checkbox 'Remember me' có được chọn không")
    public boolean isRememberMeChecked() {
        return rememberMeCheckbox.isSelected();
    }

    public boolean isOnLoginPage() {
        return getCurrentUrl().contains("authentication");
    }
}
