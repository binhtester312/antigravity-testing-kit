package com.anhtester.utils;

import com.anhtester.driver.DriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * AllureUtils — Tiện ích đính kèm screenshot và dữ liệu vào Allure Report
 */
public class AllureUtils {

    private AllureUtils() {
        // Utility class
    }

    /**
     * Chụp screenshot và đính kèm vào Allure report (sử dụng khi test FAIL)
     *
     * @param screenshotName Tên hiển thị trong Allure
     */
    @Attachment(value = "{screenshotName}", type = "image/png")
    public static byte[] takeScreenshot(String screenshotName) {
        WebDriver driver = DriverManager.getDriver();
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Đính kèm screenshot vào Allure với tên mặc định "Screenshot"
     */
    public static void attachScreenshot() {
        attachScreenshot("Screenshot");
    }

    /**
     * Đính kèm screenshot vào Allure với tên tùy chỉnh — dùng cho on-failure
     *
     * @param name Tên của attachment trong report
     */
    public static void attachScreenshot(String name) {
        try {
            WebDriver driver = DriverManager.getDriver();
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshotBytes), ".png");
        } catch (Exception e) {
            Allure.addAttachment("Screenshot Error", "text/plain",
                new ByteArrayInputStream(("Không thể chụp screenshot: " + e.getMessage()).getBytes()),
                ".txt");
        }
    }

    /**
     * Đính kèm văn bản vào Allure report
     *
     * @param name    Tên attachment
     * @param content Nội dung text
     */
    public static void attachText(String name, String content) {
        try (InputStream is = new ByteArrayInputStream(content.getBytes())) {
            Allure.addAttachment(name, "text/plain", is, ".txt");
        } catch (Exception e) {
            // Suppress
        }
    }

    /**
     * Ghi URL hiện tại vào Allure report
     */
    public static void attachCurrentUrl() {
        try {
            String url = DriverManager.getDriver().getCurrentUrl();
            attachText("Current URL", url);
        } catch (Exception e) {
            // Suppress
        }
    }
}
