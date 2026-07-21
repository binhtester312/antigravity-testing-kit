package com.anhtester.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader — Đọc cấu hình từ config.properties
 * Sử dụng Singleton pattern để chỉ load file một lần
 */
public class ConfigReader {

    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties;

    private ConfigReader() {
        // Utility class — không khởi tạo trực tiếp
    }

    private static Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            try (InputStream inputStream = ConfigReader.class
                    .getClassLoader()
                    .getResourceAsStream(CONFIG_FILE)) {
                if (inputStream == null) {
                    throw new RuntimeException(
                        "Không tìm thấy file config: " + CONFIG_FILE +
                        ". Hãy tạo file src/test/resources/config.properties"
                    );
                }
                properties.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi đọc file config.properties: " + e.getMessage(), e);
            }
        }
        return properties;
    }

    /**
     * Lấy giá trị từ config, throw exception nếu key không tồn tại
     */
    public static String get(String key) {
        String value = getProperties().getProperty(key);
        if (value == null || value.isBlank()) {
            throw new RuntimeException(
                "Config key '" + key + "' không tồn tại hoặc rỗng trong config.properties"
            );
        }
        return value.trim();
    }

    /**
     * Lấy giá trị với default nếu key không tồn tại
     */
    public static String get(String key, String defaultValue) {
        String value = getProperties().getProperty(key);
        return (value == null || value.isBlank()) ? defaultValue : value.trim();
    }

    /**
     * Lấy giá trị boolean
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = getProperties().getProperty(key);
        if (value == null || value.isBlank()) return defaultValue;
        return Boolean.parseBoolean(value.trim());
    }

    /**
     * Lấy giá trị int
     */
    public static int getInt(String key, int defaultValue) {
        String value = getProperties().getProperty(key);
        if (value == null || value.isBlank()) return defaultValue;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // ============ Convenience methods ============

    public static String getBaseUrl() {
        return get("base.url");
    }

    public static String getAdminEmail() {
        return get("admin.email");
    }

    public static String getAdminPassword() {
        return get("admin.password");
    }

    public static String getBrowser() {
        return get("browser", "chrome");
    }

    public static boolean isHeadless() {
        return getBoolean("headless", false);
    }

    public static int getImplicitWait() {
        return getInt("implicit.wait.seconds", 10);
    }

    public static int getExplicitWait() {
        return getInt("explicit.wait.seconds", 15);
    }
}
