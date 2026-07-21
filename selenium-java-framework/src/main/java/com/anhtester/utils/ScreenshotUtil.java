package com.anhtester.utils;

import com.anhtester.config.ConfigReader;
import com.anhtester.driver.DriverFactory;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotUtil — Chụp và lưu screenshot
 *
 * Tính năng:
 * - Chụp screenshot và lưu vào thư mục target/screenshots
 * - Đính kèm vào Allure Report tự động
 * - Tên file bao gồm timestamp để dễ truy vết
 */
public final class ScreenshotUtil {

    private static final Logger log = LogManager.getLogger(ScreenshotUtil.class);
    private static final DateTimeFormatter TIMESTAMP_FMT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtil() { }

    /**
     * Chụp screenshot và đính kèm vào Allure report.
     * Gọi trong @AfterMethod khi test FAIL.
     *
     * @param testName Tên test case — dùng làm tên file
     */
    public static void captureAndAttach(String testName) {
        try {
            byte[] screenshotBytes = captureAsBytes();
            // Đính kèm vào Allure
            Allure.addAttachment(
                "Screenshot — " + testName,
                "image/png",
                new ByteArrayInputStream(screenshotBytes),
                ".png"
            );
            // Lưu ra file
            saveToFile(testName, screenshotBytes);
        } catch (Exception e) {
            log.error("Không thể chụp screenshot cho '{}': {}", testName, e.getMessage());
        }
    }

    /**
     * Chụp screenshot và trả về dạng byte array.
     */
    public static byte[] captureAsBytes() {
        WebDriver driver = DriverFactory.getDriver();
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Chụp screenshot và lưu ra file.
     * @return Đường dẫn tuyệt đối của file đã lưu
     */
    public static String saveToFile(String testName) {
        try {
            byte[] bytes = captureAsBytes();
            return saveToFile(testName, bytes);
        } catch (Exception e) {
            log.error("Lỗi lưu screenshot: {}", e.getMessage());
            return "";
        }
    }

    private static String saveToFile(String testName, byte[] screenshotBytes) throws IOException {
        String screenshotDir = ConfigReader.getScreenshotDir();
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FMT);
        // Làm sạch tên file
        String safeName = testName.replaceAll("[^a-zA-Z0-9_\\-]", "_");
        String fileName = safeName + "_" + timestamp + ".png";

        Path dirPath = Paths.get(screenshotDir);
        Files.createDirectories(dirPath);
        Path filePath = dirPath.resolve(fileName);
        Files.write(filePath, screenshotBytes);

        log.info("Screenshot đã lưu: {}", filePath.toAbsolutePath());
        return filePath.toAbsolutePath().toString();
    }

    /**
     * Đính kèm URL hiện tại vào Allure report
     */
    public static void attachCurrentUrl(String label) {
        try {
            String url = DriverFactory.getDriver().getCurrentUrl();
            Allure.addAttachment(label, "text/plain",
                new ByteArrayInputStream(url.getBytes()), ".txt");
        } catch (Exception e) {
            log.warn("Không thể lấy URL hiện tại: {}", e.getMessage());
        }
    }

    /**
     * Đính kèm text vào Allure report
     */
    public static void attachText(String name, String content) {
        try {
            Allure.addAttachment(name, "text/plain",
                new ByteArrayInputStream(content.getBytes()), ".txt");
        } catch (Exception e) {
            log.warn("Lỗi đính kèm text vào Allure: {}", e.getMessage());
        }
    }
}
