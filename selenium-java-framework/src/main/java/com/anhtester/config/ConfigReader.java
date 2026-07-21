package com.anhtester.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader — Quản lý cấu hình tập trung
 *
 * Ưu tiên đọc theo thứ tự:
 * 1. System property / Environment variable (cho CI/CD override)
 * 2. config.properties trong classpath
 * 3. Giá trị default được hardcode
 *
 * Pattern: Singleton + Static methods
 */
public final class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties;

    private ConfigReader() { }

    private static synchronized Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
                if (is == null) {
                    log.warn("Không tìm thấy '{}' trong classpath. Sẽ dùng env variables / defaults.", CONFIG_FILE);
                } else {
                    properties.load(is);
                    log.debug("Đã load config từ '{}'", CONFIG_FILE);
                }
            } catch (IOException e) {
                throw new RuntimeException("Lỗi đọc " + CONFIG_FILE + ": " + e.getMessage(), e);
            }
        }
        return properties;
    }

    /**
     * Lấy giá trị config — ưu tiên System property / env variable
     * Throw exception nếu key không tồn tại và không có default.
     */
    public static String get(String key) {
        // 1. Check System property (mvn -Dkey=value)
        String value = System.getProperty(key);
        if (value != null && !value.isBlank()) return value.trim();

        // 2. Check Environment variable (CI/CD secrets)
        value = System.getenv(key.replace(".", "_").toUpperCase());
        if (value != null && !value.isBlank()) return value.trim();

        // 3. Check config.properties
        value = getProperties().getProperty(key);
        if (value != null && !value.isBlank()) return value.trim();

        throw new RuntimeException(
            "Config key '" + key + "' không tồn tại trong config.properties, " +
            "System properties, hoặc Environment variables."
        );
    }

    /** Lấy giá trị với fallback default nếu không tìm thấy */
    public static String get(String key, String defaultValue) {
        try {
            return get(key);
        } catch (RuntimeException e) {
            log.debug("Key '{}' không tồn tại, dùng default: '{}'", key, defaultValue);
            return defaultValue;
        }
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
    }

    public static int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            log.warn("Không parse được '{}' thành int, dùng default: {}", key, defaultValue);
            return defaultValue;
        }
    }

    // ================================================================
    // Convenience accessors — mỗi key chỉ khai báo 1 chỗ
    // ================================================================

    public static String getBaseUrl()           { return get("base.url"); }
    public static String getAdminEmail()         { return get("admin.email"); }
    public static String getAdminPassword()      { return get("admin.password"); }
    public static String getBrowser()            { return get("browser", "chrome"); }
    public static boolean isHeadless()           { return getBoolean("headless", false); }
    public static int getImplicitWait()          { return getInt("implicit.wait.seconds", 10); }
    public static int getExplicitWait()          { return getInt("explicit.wait.seconds", 15); }
    public static int getPageLoadTimeout()       { return getInt("page.load.timeout.seconds", 30); }
    public static String getScreenshotDir()      { return get("screenshot.dir", "target/screenshots"); }
}
