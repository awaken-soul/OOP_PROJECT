  package com.logistics.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * Utility class to securely hash and verify passwords using SHA-256.
 */
public class PasswordHasher {

    /**
     * Hashes a plain text password.
     * @param password The plain text password.
     * @return The hex string representation of the hashed password, or null if hashing fails.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                password.getBytes(StandardCharsets.UTF_8));
            
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error: SHA-256 algorithm not found.");
            return null; 
        }
    }

    /**
     * Converts a byte array to a hex string.
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Verifies a plain text password against a stored hashed password.
     * @param plainPassword The password entered by the user.
     * @param hashedPassword The hash stored in the database.
     * @return true if the hashes match, false otherwise.
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (hashedPassword == null) return false;
        String newHash = hashPassword(plainPassword);
        return newHash != null && newHash.equals(hashedPassword);
    }
}
