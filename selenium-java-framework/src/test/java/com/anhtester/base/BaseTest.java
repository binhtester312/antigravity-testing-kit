package com.anhtester.base;

import com.anhtester.config.ConfigReader;
import com.anhtester.driver.DriverFactory;
import com.anhtester.utils.ScreenshotUtil;
import com.anhtester.utils.WaitHelper;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * BaseTest — Lớp cha cho tất cả Test classes
 *
 * Trách nhiệm:
 * - @BeforeMethod: Khởi tạo WebDriver + mở URL
 * - @AfterMethod: Chụp screenshot khi FAIL + đóng driver
 * - Pre-condition helper: performValidLogin()
 * - Log mọi bước bằng Log4j2
 */
public class BaseTest {

    private static final Logger log = LogManager.getLogger(BaseTest.class);

    @BeforeMethod(alwaysRun = true)
    @Step("Khởi tạo browser và mở trang Login")
    public void setUp(Method method) {
        log.info("▶ BẮT ĐẦU: {} :: {}", getClass().getSimpleName(), method.getName());
        DriverFactory.createDriver();
        DriverFactory.getDriver().get(ConfigReader.getBaseUrl());
        WaitHelper.waitForPageLoad();
        log.debug("Browser đã mở: {}", ConfigReader.getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        switch (result.getStatus()) {
            case ITestResult.FAILURE -> {
                log.error("✗ FAIL: {} — Lý do: {}", testName,
                    result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown");
                // Chụp screenshot và đính kèm vào Allure
                ScreenshotUtil.captureAndAttach("FAIL_" + testName);
                ScreenshotUtil.attachCurrentUrl("URL khi FAIL");
                if (result.getThrowable() != null) {
                    ScreenshotUtil.attachText("Error Detail", result.getThrowable().getMessage());
                }
            }
            case ITestResult.SUCCESS ->
                log.info("✔ PASS: {}", testName);
            case ITestResult.SKIP ->
                log.warn("⊘ SKIP: {} — Lý do: {}", testName,
                    result.getThrowable() != null ? result.getThrowable().getMessage() : "Skipped");
        }

        DriverFactory.quitDriver();
        log.info("◀ KẾT THÚC: {}\n", testName);
    }

    // ================================================================
    // Pre-condition helper — dùng cho TC yêu cầu đã đăng nhập trước
    // ================================================================

    @Step("Pre-condition: Đăng nhập thành công với tài khoản admin")
    protected void performValidLogin() {
        log.info("Pre-condition: thực hiện login với admin account");
        com.anhtester.pages.LoginPage loginPage = new com.anhtester.pages.LoginPage();
        loginPage.login(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword());

        // Chờ redirect về Dashboard
        new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(ConfigReader.getExplicitWait()))
            .until(ExpectedConditions.urlContains("/admin/"));

        log.info("Pre-condition: login thành công, URL: {}", DriverFactory.getDriver().getCurrentUrl());
    }
}
