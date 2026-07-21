package com.anhtester.utils;

import com.anhtester.driver.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.anhtester.config.ConfigReader.getExplicitWait;

/**
 * WaitHelper — Tập trung tất cả smart wait strategies
 *
 * Nguyên tắc:
 * - KHÔNG dùng Thread.sleep() / hard waits
 * - Chỉ dùng ExpectedConditions + WebDriverWait
 * - Timeout mặc định lấy từ ConfigReader
 */
public final class WaitHelper {

    private static final Logger log = LogManager.getLogger(WaitHelper.class);

    private WaitHelper() { }

    private static WebDriverWait getWait() {
        return new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(getExplicitWait()));
    }

    private static WebDriverWait getWait(int timeoutSeconds) {
        return new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(timeoutSeconds));
    }

    // ================================================================
    // Element Visibility
    // ================================================================

    /** Chờ element visible trên DOM */
    public static WebElement waitForVisible(WebElement element) {
        log.debug("Chờ element visible: {}", element);
        return getWait().until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForVisible(WebElement element, int timeoutSeconds) {
        return getWait(timeoutSeconds).until(ExpectedConditions.visibilityOf(element));
    }

    /** Chờ element clickable */
    public static WebElement waitForClickable(WebElement element) {
        log.debug("Chờ element clickable: {}", element);
        return getWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    /** Chờ element invisible / ẩn đi */
    public static boolean waitForInvisible(WebElement element) {
        return getWait().until(ExpectedConditions.invisibilityOf(element));
    }

    // ================================================================
    // Text & Attribute
    // ================================================================

    /** Chờ element có text cụ thể */
    public static boolean waitForText(WebElement element, String text) {
        return getWait().until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    // ================================================================
    // URL
    // ================================================================

    public static boolean waitForUrlContains(String partialUrl) {
        log.debug("Chờ URL chứa: '{}'", partialUrl);
        return getWait().until(ExpectedConditions.urlContains(partialUrl));
    }

    public static boolean waitForUrlContains(String partialUrl, int timeoutSeconds) {
        return getWait(timeoutSeconds).until(ExpectedConditions.urlContains(partialUrl));
    }

    public static boolean waitForUrlToBe(String exactUrl) {
        return getWait().until(ExpectedConditions.urlToBe(exactUrl));
    }

    // ================================================================
    // Page Load
    // ================================================================

    /** Chờ trang load hoàn toàn (document.readyState = 'complete') */
    public static void waitForPageLoad() {
        log.debug("Chờ page load hoàn tất...");
        getWait().until((ExpectedCondition<Boolean>) driver -> {
            assert driver != null;
            return ((JavascriptExecutor) driver)
                .executeScript("return document.readyState")
                .equals("complete");
        });
    }

    // ================================================================
    // Safe check (không throw exception)
    // ================================================================

    /**
     * Kiểm tra element có visible trong timeout không.
     * @return true nếu visible, false nếu timeout
     */
    public static boolean isVisibleWithin(WebElement element, int timeoutSeconds) {
        try {
            waitForVisible(element, timeoutSeconds);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
