package com.anhtester.base;

import com.anhtester.config.ConfigReader;
import com.anhtester.driver.DriverManager;
import com.anhtester.utils.AllureUtils;
import io.qameta.allure.Step;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

/**
 * BaseTest — Lớp cha cho tất cả Test classes
 * Xử lý: khởi tạo WebDriver, dọn dẹp sau test, và chụp screenshot khi FAIL
 */
public class BaseTest {

    /**
     * Mở browser và navigate đến base URL trước mỗi test method
     */
    @BeforeMethod(alwaysRun = true)
    @Step("Khởi tạo browser và mở trang Login")
    public void setUp(Method method) {
        System.out.println("\n▶ Bắt đầu test: " + method.getName());
        DriverManager.initDriver();
        DriverManager.getDriver().get(ConfigReader.getBaseUrl());
    }

    /**
     * Đóng browser sau mỗi test method.
     * Nếu test FAIL → chụp screenshot đính kèm vào Allure.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            System.out.println("✗ FAIL: " + result.getName());
            // Đính kèm screenshot vào Allure report
            AllureUtils.attachScreenshot("FAIL — " + result.getName());
            AllureUtils.attachCurrentUrl();
            // Đính kèm thông tin lỗi
            if (result.getThrowable() != null) {
                AllureUtils.attachText(
                    "Error Message",
                    result.getThrowable().getMessage()
                );
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            System.out.println("✔ PASS: " + result.getName());
        } else {
            System.out.println("⊘ SKIP: " + result.getName());
        }
        DriverManager.quitDriver();
    }

    // ============ Protected Helper — dùng trong test methods ============

    /**
     * Thực hiện đăng nhập thành công trước khi test một số TC
     * (dùng cho TC_014, TC_020, TC_021)
     */
    @Step("Pre-condition: Đăng nhập thành công với tài khoản admin")
    protected void performValidLogin() {
        com.anhtester.pages.LoginPage loginPage = new com.anhtester.pages.LoginPage();
        loginPage.login(
            ConfigReader.getAdminEmail(),
            ConfigReader.getAdminPassword()
        );
        // Chờ redirect về Dashboard
        new org.openqa.selenium.support.ui.WebDriverWait(
            DriverManager.getDriver(),
            java.time.Duration.ofSeconds(ConfigReader.getExplicitWait())
        ).until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/admin/"));
    }
}
