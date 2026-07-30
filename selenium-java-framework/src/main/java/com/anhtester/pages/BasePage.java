package com.anhtester.pages;

import com.anhtester.driver.DriverFactory;
import com.anhtester.utils.WaitHelper;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

/**
 * BasePage — Lớp cha cho tất cả Page Objects
 *
 * Cung cấp:
 * - Common UI interactions: click, type, getText, isVisible...
 * - Smart waits tích hợp (qua WaitHelper)
 * - Dynamic WebDriver resolution từ DriverFactory (Chống lỗi NoSuchSessionException)
 * - Logging mỗi action bằng Log4j2
 */
public abstract class BasePage {

    protected final Logger log = LogManager.getLogger(getClass());

    protected BasePage() {
        PageFactory.initElements(DriverFactory.getDriver(), this);
        log.debug("Khởi tạo Page: {}", getClass().getSimpleName());
    }

    /**
     * Luôn lấy instance WebDriver mới nhất từ ThreadLocal của DriverFactory
     */
    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    // ================================================================
    // Navigation
    // ================================================================

    @Step("Mở URL: {url}")
    public void openUrl(String url) {
        log.info("Mở URL: {}", url);
        getDriver().get(url);
        WaitHelper.waitForPageLoad();
    }

    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    public String getPageTitle() {
        return getDriver().getTitle();
    }

    // ================================================================
    // Element Interactions
    // ================================================================

    @Step("Click element")
    protected void click(WebElement element) {
        log.debug("Click: {}", describeElement(element));
        WaitHelper.waitForClickable(element);
        element.click();
    }

    @Step("Click element (JavaScript)")
    protected void jsClick(WebElement element) {
        log.debug("JS Click: {}", describeElement(element));
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", element);
    }

    @Step("Hover chuột vào element")
    protected void hover(WebElement element) {
        log.debug("Hover: {}", describeElement(element));
        WaitHelper.waitForVisible(element);
        new org.openqa.selenium.interactions.Actions(getDriver()).moveToElement(element).perform();
    }

    @Step("Xóa và nhập text: [{text}]")
    protected void clearAndType(WebElement element, String text) {
        log.debug("clearAndType: '{}' → element: {}", text, describeElement(element));
        WaitHelper.waitForVisible(element);
        element.sendKeys(Keys.chord(Keys.COMMAND, "a"));
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.BACK_SPACE);
        try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        element.sendKeys(text);
    }

    @Step("Nhập thêm text: [{text}]")
    protected void type(WebElement element, String text) {
        WaitHelper.waitForVisible(element);
        element.sendKeys(text);
    }

    @Step("Nhấn phím: {key}")
    protected void pressKey(WebElement element, Keys key) {
        log.debug("PressKey: {} trên {}", key, describeElement(element));
        WaitHelper.waitForVisible(element);
        element.sendKeys(key);
    }

    @Step("Chọn dropdown theo text: [{text}]")
    protected void selectByVisibleText(WebElement element, String text) {
        try {
            if (isDisplayed(element)) {
                new Select(element).selectByVisibleText(text);
                return;
            }
        } catch (Exception ignored) {}

        try {
            executeScript(
                "var $sel = $(arguments[0]);" +
                "var val = $sel.find('option').filter(function() { return $(this).text().trim() === arguments[1] || $(this).val() === arguments[1]; }).val();" +
                "if(val !== undefined) { $sel.val(val).trigger('change'); }" +
                "if($.fn.selectpicker) { $sel.selectpicker('refresh'); }",
                element, text
            );
        } catch (Exception e) {
            log.warn("selectByVisibleText JS fallback error: " + e.getMessage());
            new Select(element).selectByVisibleText(text);
        }
    }

    // ================================================================
    // Text & State
    // ================================================================

    protected String getText(WebElement element) {
        WaitHelper.waitForVisible(element);
        return element.getText().trim();
    }

    protected String getAttribute(WebElement element, String attribute) {
        WaitHelper.waitForVisible(element);
        return element.getAttribute(attribute);
    }

    /**
     * Kiểm tra element có hiển thị không — KHÔNG throw exception nếu không tìm thấy.
     * @return true nếu visible, false nếu không tìm thấy hoặc ẩn
     */
    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Chờ element visible trong timeout và trả về true/false — không throw exception.
     */
    protected boolean isVisibleWithin(WebElement element, int timeoutSeconds) {
        return WaitHelper.isVisibleWithin(element, timeoutSeconds);
    }

    // ================================================================
    // Wait shortcuts (delegate to WaitHelper)
    // ================================================================

    protected boolean waitForUrlContains(String partialUrl) {
        return WaitHelper.waitForUrlContains(partialUrl);
    }

    protected boolean waitForUrlContains(String partialUrl, int timeoutSeconds) {
        return WaitHelper.waitForUrlContains(partialUrl, timeoutSeconds);
    }

    // ================================================================
    // JavaScript helpers
    // ================================================================

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    protected Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) getDriver()).executeScript(script, args);
    }

    // ================================================================
    // Private helpers
    // ================================================================

    private String describeElement(WebElement element) {
        try {
            return element.toString().replaceAll(".*->", "").replace("]", "").trim();
        } catch (Exception e) {
            return "element";
        }
    }
}
