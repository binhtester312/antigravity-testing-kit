package com.anhtester.driver;

import com.anhtester.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * DriverFactory — Tạo và quản lý WebDriver instances
 *
 * Design Pattern: Factory + ThreadLocal (thread-safe cho parallel test)
 * - Mỗi thread có 1 WebDriver riêng biệt
 * - Browser type và headless mode đọc từ ConfigReader
 * - Hỗ trợ: Chrome | Firefox | Edge
 */
public final class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverHolder = new ThreadLocal<>();

    private DriverFactory() { }

    /**
     * Khởi tạo WebDriver cho thread hiện tại.
     * Phải gọi trước khi dùng getDriver().
     */
    public static void createDriver() {
        String browser = ConfigReader.getBrowser().toLowerCase().trim();
        boolean headless = ConfigReader.isHeadless();

        log.info("Khởi tạo WebDriver — browser: '{}', headless: {}", browser, headless);

        WebDriver driver = switch (browser) {
            case "firefox" -> createFirefoxDriver(headless);
            case "edge"    -> createEdgeDriver(headless);
            default        -> {
                if (!browser.equals("chrome")) {
                    log.warn("Browser '{}' không được hỗ trợ, fallback sang Chrome.", browser);
                }
                yield createChromeDriver(headless);
            }
        };

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));
        driver.manage().window().maximize();

        driverHolder.set(driver);
        log.debug("WebDriver khởi tạo thành công — Thread: {}", Thread.currentThread().getName());
    }

    /**
     * Lấy WebDriver của thread hiện tại.
     *
     * @throws IllegalStateException nếu chưa gọi createDriver()
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverHolder.get();
        if (driver == null) {
            throw new IllegalStateException(
                "WebDriver chưa được khởi tạo trên thread '" + Thread.currentThread().getName() + "'. " +
                "Hãy gọi DriverFactory.createDriver() trong @BeforeMethod."
            );
        }
        return driver;
    }

    /**
     * Đóng browser và giải phóng WebDriver khỏi ThreadLocal.
     * PHẢI gọi trong @AfterMethod để tránh memory leak.
     */
    public static void quitDriver() {
        WebDriver driver = driverHolder.get();
        if (driver != null) {
            try {
                driver.quit();
                log.debug("WebDriver đã đóng — Thread: {}", Thread.currentThread().getName());
            } catch (Exception e) {
                log.warn("Lỗi khi đóng WebDriver: {}", e.getMessage());
            } finally {
                driverHolder.remove();
            }
        }
    }

    /** Kiểm tra WebDriver có đang active không */
    public static boolean isDriverActive() {
        return driverHolder.get() != null;
    }

    // ================================================================
    // Private factory methods
    // ================================================================

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (headless) options.addArguments("--headless=new");
        options.addArguments(
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--disable-gpu",
            "--window-size=1920,1080",
            "--disable-extensions",
            "--disable-blink-features=AutomationControlled",
            "--remote-allow-origins=*"
        );
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        if (headless) options.addArguments("--headless");
        options.addArguments("--width=1920", "--height=1080");
        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        if (headless) options.addArguments("--headless=new");
        options.addArguments("--no-sandbox", "--window-size=1920,1080");
        return new EdgeDriver(options);
    }
}
