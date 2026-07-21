package com.anhtester.pages;

import com.anhtester.driver.DriverManager;
import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.anhtester.config.ConfigReader.getExplicitWait;

/**
 * BasePage — Lớp cha cho tất cả Page Objects
 * Cung cấp các method dùng chung: wait, click, type, getTitle...
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(getExplicitWait()));
        PageFactory.initElements(driver, this);
    }

    // ============ Navigation ============

    @Step("Mở URL: {url}")
    public void openUrl(String url) {
        driver.get(url);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    // ============ Element Interactions ============

    @Step("Click vào element")
    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    @Step("Xóa và nhập text: {text}")
    protected void clearAndType(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    @Step("Nhập text: {text}")
    protected void type(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.sendKeys(text);
    }

    // ============ Wait Helpers ============

    protected WebElement waitForVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected boolean waitForUrlContains(String partialUrl) {
        return wait.until(ExpectedConditions.urlContains(partialUrl));
    }

    protected boolean waitForUrlToBe(String exactUrl) {
        return wait.until(ExpectedConditions.urlToBe(exactUrl));
    }

    // ============ Text & Attribute ============

    protected String getText(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.getText().trim();
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ============ JavaScript Helpers ============

    protected void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
}
