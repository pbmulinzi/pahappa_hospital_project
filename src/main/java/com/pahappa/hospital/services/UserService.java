package com.pahappa.hospital.services;

import com.pahappa.hospital.daos.UserDao;
import com.pahappa.hospital.models.User;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class UserService implements Serializable {


    private static final long serialVersionUID = 1L;
    //declares a unique identifier for the UserService class, which implements Serializable.

    @Inject
    private UserDao userDao;

    @PostConstruct
    public void init() {
        // No default admin creation logic
    }

    public UserService() {
    }

    public User findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        try {
            List<User> users = userDao.getUserByUsername(username.trim());
            return (users != null && !users.isEmpty()) ? users.get(0) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        try {
            // Hash password if it's not already hashed
            if (user.getPassword() != null && !isPasswordHashed(user.getPassword())) {
                String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
                user.setPassword(hashedPassword);
            }

            userDao.createUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save user", e);
        }
    }

    public boolean validateLogin(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }

        try {
            User user = findByUsername(username.trim());
            if (user != null) {
                return validatePassword(password, user.getPassword());
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validates the provided password against the stored password.
     * Supports both plain text (for backward compatibility) and BCrypt hashed passwords.
     */
    private boolean validatePassword(String providedPassword, String storedPassword) {
        if (providedPassword == null || storedPassword == null) {
            return false;
        }

        // Check if the stored password is BCrypt hashed
        if (isPasswordHashed(storedPassword)) {
            // Use BCrypt to verify the password
            return BCrypt.checkpw(providedPassword, storedPassword);
        } else {
            // For plain text passwords (backward compatibility)
            return providedPassword.equals(storedPassword);
        }
    }

    /**
     * Checks if a password is already hashed with BCrypt
     */
    private boolean isPasswordHashed(String password) {
        return password != null && (password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$"));
    }

    public void updateUserPassword(String username, String newPassword) {
        if (username == null || username.trim().isEmpty() || newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be null or empty");
        }

        try {
            User user = findByUsername(username.trim());
            if (user != null) {
                String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                user.setPassword(hashedPassword);
                userDao.updateUser(user);
            } else {
                throw new RuntimeException("User not found: " + username);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update password", e);
        }
    }
}















//package com.pahappa.hospital.services;
//
//import com.pahappa.hospital.daos.UserDao;
//import com.pahappa.hospital.models.User;
//import jakarta.annotation.PostConstruct;
//import jakarta.enterprise.inject.Any;
//
//import java.util.List;
//
//
//
//public class UserService {
//
//    private final UserDao userDao;
//
//    @PostConstruct
//    public void init() {
//        // No default admin creation logic
//    }
//
//    public UserService() {
//        userDao = new UserDao();
//    }
//
//
//    public User findByUsername(String username) {
//        List<User> users = userDao.getUserByUsername(username);
//        return (users != null && !users.isEmpty()) ? users.get(0) : null;
//        //this will return the first user found in the list; with the given username, or null, if nothing is found.
//    }
//
//    public void saveUser(User user){
//        //save using the UserDao function.
//        userDao.createUser(user);
//    }
//}
