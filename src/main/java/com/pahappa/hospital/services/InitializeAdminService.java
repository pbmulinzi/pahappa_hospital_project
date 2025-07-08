package com.pahappa.hospital.services;

import com.pahappa.hospital.models.User;
import jakarta.annotation.PostConstruct;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;

public class InitializeAdminService implements Serializable {
    private static final long serialVersionUID = 1L;

    @PostConstruct
    public void init() {
        UserService userService = new UserService();

        // Check if admin user already exists
        if (userService.findByUsername("admin") == null) {
            try {
                User adminUser = new User();
                adminUser.setUsername("admin");

                // Hash the password using BCrypt
                String hashedPassword = BCrypt.hashpw("password", BCrypt.gensalt());
                adminUser.setPassword(hashedPassword);

                adminUser.setAdmin(true);

                userService.saveUser(adminUser);

                System.out.println("Admin user created successfully with username: admin");
            } catch (Exception e) {
                System.err.println("Error creating admin user: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}





















//package com.pahappa.hospital.services;
//
//import com.pahappa.hospital.models.User;
//import jakarta.annotation.PostConstruct;
//
//public class InitializeAdminService {
//
//    @PostConstruct
//    public void init() {
//        UserService userService = new UserService();
//        if(userService.findByUsername("admin") == null) {
//            User adminUser = new User();
//            adminUser.setUsername("admin");
//            adminUser.setPassword("password");
//            adminUser.setAdmin(true);
//            userService.saveUser(adminUser);
//        }
//    }
//}
