package com.pahappa.hospital.services.user;

import com.pahappa.hospital.models.User;

public interface UserService {


    //don't need to mention that they are public, as all methods in an interface are public by default
    User findByUsername(String username);

    void saveUser(User user);

    boolean validateLogin(String username, String password);

    /**
     * Validates the provided password against the stored password.
     * Supports both plain text (for backward compatibility) and BCrypt hashed passwords.
     */
    boolean validatePassword(String providedPassword, String storedPassword);

    /**
     * Checks if a password is already hashed with BCrypt
     */
    boolean isPasswordHashed(String password);

    void updateUserPassword(String username, String newPassword);
}