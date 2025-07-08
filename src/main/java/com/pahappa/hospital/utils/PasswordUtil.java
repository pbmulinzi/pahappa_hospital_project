package com.pahappa.hospital.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Default number of rounds for BCrypt hashing
    private static final int DEFAULT_ROUNDS = 12;

    /**
     * Hashes a plain text password using BCrypt
     * @param plainTextPassword The plain text password to hash
     * @return The hashed password
     */
    public static String hashPassword(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(DEFAULT_ROUNDS));
    }

    /**
     * Verifies a plain text password against a hashed password
     * @param plainTextPassword The plain text password to verify
     * @param hashedPassword The stored hashed password
     * @return true if the passwords match, false otherwise
     */
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        if (plainTextPassword == null || hashedPassword == null) {
            return false;
        }

        try {
            // Check if the stored password is BCrypt hashed
            if (isPasswordHashed(hashedPassword)) {
                return BCrypt.checkpw(plainTextPassword, hashedPassword);
            } else {
                // For plain text passwords (backward compatibility)
                return plainTextPassword.equals(hashedPassword);
            }
        } catch (Exception e) {
            // If there's an error in verification, assume passwords don't match
            return false;
        }
    }

    /**
     * Checks if a password is already hashed with BCrypt
     * @param password The password to check
     * @return true if the password is hashed, false otherwise
     */
    public static boolean isPasswordHashed(String password) {
        return password != null &&
                (password.startsWith("$2a$") ||
                        password.startsWith("$2b$") ||
                        password.startsWith("$2y$"));
    }

    /**
     * Validates password strength (optional - you can customize this)
     * @param password The password to validate
     * @return true if the password meets minimum requirements
     */
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        // Check for at least one uppercase letter, one lowercase letter, and one digit
        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasLowercase = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");

        return hasUppercase && hasLowercase && hasDigit;
    }

    /**
     * Generates a random salt for BCrypt (usually not needed as BCrypt.gensalt() is used)
     * @return A random salt string
     *
     * A salt is a random value added to a password before hashing
     *
     * Salting ensures each hash is unique, even for identical passwords
     *
     * This protects against precomputed attacks like rainbow tables
     *
     */
    public static String generateSalt() {
        return BCrypt.gensalt(DEFAULT_ROUNDS);
    }
}