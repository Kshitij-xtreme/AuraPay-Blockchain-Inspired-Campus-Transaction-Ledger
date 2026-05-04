package com.aurapy.security;

import org.apache.commons.codec.digest.DigestUtils;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Cryptographic utilities for hashing and encryption operations
 */
public class CryptoUtil {

    private static final String SHA_256 = "SHA-256";
    private static final String PBKDF2 = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 100000;
    private static final int KEY_LENGTH = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generate SHA-256 hash of input string
     * @param input String to hash
     * @return Hex string of hash
     */
    public static String sha256Hash(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        return DigestUtils.sha256Hex(input);
    }

    /**
     * Generate SHA-256 hash of bytes
     * @param input Bytes to hash
     * @return Hex string of hash
     */
    public static String sha256Hash(byte[] input) {
        if (input == null || input.length == 0) {
            return "";
        }
        return DigestUtils.sha256Hex(input);
    }

    /**
     * Generate HMAC-SHA256 signature
     * @param message Message to sign
     * @param secretKey Secret key
     * @return Base64 encoded signature
     */
    public static String hmacSha256(String message, String secretKey) {
        try {
            java.security.Mac mac = java.security.Mac.getInstance("HmacSHA256");
            javax.crypto.spec.SecretKeySpec keySpec = new javax.crypto.spec.SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8),
                0,
                secretKey.getBytes(StandardCharsets.UTF_8).length,
                "HmacSHA256"
            );
            mac.init(keySpec);
            byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC-SHA256", e);
        }
    }

    /**
     * Hash password with salt
     * @param password Password to hash
     * @param salt Salt for hashing
     * @return Hashed password
     */
    public static String hashPassword(String password, byte[] salt) {
        try {
            javax.crypto.SecretKeyFactory factory = javax.crypto.SecretKeyFactory.getInstance(PBKDF2);
            javax.crypto.spec.PBEKeySpec spec = new javax.crypto.spec.PBEKeySpec(
                password.toCharArray(), salt, ITERATIONS, KEY_LENGTH
            );
            javax.crypto.SecretKey secret = factory.generateSecret(spec);
            return Base64.getEncoder().encodeToString(secret.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Generate random salt
     * @return Random salt bytes
     */
    public static byte[] generateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }

    /**
     * Generate random token
     * @param length Length of token
     * @return Random token string
     */
    public static String generateToken(int length) {
        byte[] randomBytes = new byte[length];
        RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    /**
     * Verify password against hash
     * @param password Password to verify
     * @param salt Salt used for hashing
     * @param hash Hash to verify against
     * @return True if password matches hash
     */
    public static boolean verifyPassword(String password, byte[] salt, String hash) {
        String passwordHash = hashPassword(password, salt);
        return passwordHash.equals(hash);
    }

    /**
     * Convert hex string to bytes
     * @param hex Hex string
     * @return Byte array
     */
    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                                 + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Convert bytes to hex string
     * @param bytes Bytes to convert
     * @return Hex string
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
