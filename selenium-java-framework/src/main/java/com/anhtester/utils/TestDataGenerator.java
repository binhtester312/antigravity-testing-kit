package com.anhtester.utils;

import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.Locale;

/**
 * TestDataGenerator — Sinh test data unique, traceable
 *
 * Nguyên tắc:
 * - Tất cả dữ liệu unique cần có timestamp để tránh conflict
 * - Dữ liệu phải traceable — từ data có thể truy ra test nào sinh ra
 * - Hỗ trợ Faker cho dữ liệu thực tế (tên, địa chỉ...)
 *
 * Format: {prefix}_{timestamp}
 */
public final class TestDataGenerator {

    private static final Logger log = LogManager.getLogger(TestDataGenerator.class);
    private static final Faker faker = new Faker(new Locale("en-US"));

    private TestDataGenerator() { }

    /**
     * Lấy Unix timestamp hiện tại (milliseconds).
     * Dùng để tạo suffix unique cho test data.
     */
    public static long timestamp() {
        return Instant.now().toEpochMilli();
    }

    // ================================================================
    // Email
    // ================================================================

    /**
     * Sinh email unique + traceable.
     * Format: auto_{testPrefix}_{timestamp}@test.local
     */
    public static String generateEmail(String testPrefix) {
        String email = "auto_" + sanitize(testPrefix) + "_" + timestamp() + "@test.local";
        log.debug("Email generated: {}", email);
        return email;
    }

    /** Email ngẫu nhiên theo kiểu thực tế (dùng Faker) */
    public static String generateFakeEmail() {
        String email = "auto_" + faker.name().firstName().toLowerCase() + "_"
            + timestamp() + "@" + faker.internet().domainName();
        log.debug("Fake email generated: {}", email);
        return email;
    }

    // ================================================================
    // Username / Display Name
    // ================================================================

    public static String generateUsername(String prefix) {
        return "user_" + sanitize(prefix) + "_" + timestamp();
    }

    public static String generateFullName() {
        return faker.name().fullName();
    }

    // ================================================================
    // Phone
    // ================================================================

    public static String generatePhone() {
        // Format: 09xx-xxx-xxx (Việt Nam style)
        return "09" + faker.numerify("########");
    }

    // ================================================================
    // Password
    // ================================================================

    /**
     * Sinh password đáp ứng yêu cầu thông thường:
     * ít nhất 1 uppercase, 1 lowercase, 1 digit, 1 special char, 8-16 chars
     */
    public static String generateStrongPassword() {
        return "Auto@" + faker.number().digits(4) + "Aa";
    }

    /** Password cố định dùng cho happy path test */
    public static String getValidPassword() {
        return "Test@12345";
    }

    // ================================================================
    // String utilities
    // ================================================================

    /** Sinh chuỗi ngẫu nhiên độ dài tùy chỉnh */
    public static String generateRandomString(int length) {
        return faker.lorem().characters(length, true, false);
    }

    /** Sinh chuỗi chỉ số có độ dài nhất định */
    public static String generateRandomDigits(int length) {
        return faker.numerify("#".repeat(length));
    }

    /** Sinh chuỗi dài 255 ký tự — dùng cho BVA upper bound test */
    public static String generate255CharString() {
        // 255 = 63 * 4 + 3
        return "Aa1!".repeat(63) + "Aa1";
    }

    // ================================================================
    // Private helpers
    // ================================================================

    private static String sanitize(String input) {
        if (input == null || input.isBlank()) return "test";
        return input.toLowerCase()
            .replaceAll("[^a-z0-9]", "_")
            .replaceAll("_+", "_")
            .replaceAll("^_|_$", "");
    }
}
