package com.pahappa.hospital.services.initializeAdmin;


import com.pahappa.hospital.daos.RoleDao;
import com.pahappa.hospital.daos.UserDao;
import com.pahappa.hospital.models.Role;
import com.pahappa.hospital.models.User;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class InitializeAdminService {

    @Inject
    RoleDao roleDao;

    @Inject
    UserDao userDao;

    @PostConstruct
    public void init() {
        //default roles
        // Check and create "System_Administrator" role
        Role systemAdmin = roleDao.findByName("System_Administrator");
        if (systemAdmin == null) {
            systemAdmin = new Role();
            systemAdmin.setRoleName("System_Administrator");
            roleDao.save(systemAdmin);
        }

        // Check and create "Doctor" role
        if (roleDao.findByName("Doctor") == null) {
            Role doctor = new Role();
            doctor.setRoleName("Doctor");
            roleDao.save(doctor);
        }

        // Check and create "Receptionist" role
        if (roleDao.findByName("Receptionist") == null) {
            Role receptionist = new Role();
            receptionist.setRoleName("Receptionist");
            roleDao.save(receptionist);
        }


        // Create admin user if none exists
        if (userDao.getUserByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(BCrypt.hashpw("adminpass", BCrypt.gensalt()));
            admin.setRole(systemAdmin);
            userDao.createUser(admin);
        }

        // Add doctor
        if (userDao.getUserByUsername("doctor1").isEmpty()) {
            User doctor = new User();
            doctor.setUsername("doctor1");
            doctor.setPassword(BCrypt.hashpw("doctorpass", BCrypt.gensalt()));
            doctor.setRole(roleDao.findByName("Doctor"));
            userDao.createUser(doctor);
        }

        // Add receptionist
        if (userDao.getUserByUsername("reception1").isEmpty()) {
            User receptionist = new User();
            receptionist.setUsername("reception1");
            receptionist.setPassword(BCrypt.hashpw("receptionpass", BCrypt.gensalt()));
            receptionist.setRole(roleDao.findByName("Receptionist"));
            userDao.createUser(receptionist);
        }
    }

}





















//package com.pahappa.hospital.services.initializeAdmin;
//
//import com.pahappa.hospital.models.User;
//import com.pahappa.hospital.services.user.UserService;
//import com.pahappa.hospital.services.user.impl.UserServiceImpl;
//import jakarta.annotation.PostConstruct;
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.inject.Inject;
//import org.mindrot.jbcrypt.BCrypt;
//
//import java.io.Serializable;
//
//@ApplicationScoped
//public class InitializeAdminService implements Serializable {
//    private static final long serialVersionUID = 1L;
//
//    @Inject6
//    private UserServiceImpl userServiceImpl;
//
//    @PostConstruct
//    public void init() {
//        // Check if admin user already exists
//        if (userServiceImpl.findByUsername("admin") == null) {
//            try {
//                User adminUser = new User();
//                adminUser.setUsername("admin");
//
//                // Hash the password using BCrypt
//                String hashedPassword = BCrypt.hashpw("password", BCrypt.gensalt());
//                adminUser.setPassword(hashedPassword);
//
//                adminUser.setAdmin(true);
//
//                userServiceImpl.saveUser(adminUser);
//
//                System.out.println("Admin user created successfully with username: admin");
//            } catch (Exception e) {
//                System.err.println("Error creating admin user: " + e.getMessage());
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("Admin user already exists.");
//        }
//    }
//}