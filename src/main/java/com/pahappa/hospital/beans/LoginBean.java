package com.pahappa.hospital.beans;

import java.io.Serializable;

import com.pahappa.hospital.models.User;
import com.pahappa.hospital.services.InitializeAdminService;
import com.pahappa.hospital.services.UserService;
import com.pahappa.hospital.services.auditLog.AuditLogService;
import com.pahappa.hospital.services.initializeAdmin.InitializeAdminService;
import com.pahappa.hospital.services.user.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

@Named
@SessionScoped
public class LoginBean implements Serializable {


    public static final long serialVersionUID = 1L; // Unique identifier for serialization

    public static final String ROLE_SYSTEM_ADMIN = "System_Administrator";
    public static final String ROLE_DOCTOR = "Doctor";
    public static final String ROLE_RECEPTIONIST = "Receptionist";

    private static final String LOGIN_ERROR = "Invalid username or password";

    private boolean closed;
    private String username;
    private String password;
    private boolean loggedIn = false;
    private User currentUser;

    @Inject
    private transient UserService userService; //mark as transient to avoid serialization issues because of CD

    @Inject
    private InitializeAdminService initializeAdminService;

    @Inject
    private AuditLogService auditLogService;


    @PostConstruct
    public void init() {

    }

    public void info() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Message Content"));
    }

    public void warn() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Message Content"));
    }

    public void error() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Message Content."));
    }

    public void fatal() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal", "Message Content."));
    }

    public void onClose() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Message is closed", null));
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String login() {
        // Input validation
        if (username == null || username.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "Username is required."));
            return null;
        }

        if (password == null || password.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "Password is required."));
            return null;
        }

        try {
            // Find user by username
            User user =     userService.findByUsername(username.trim());

            if (user != null) {
                // Check if password matches
                if (validatePassword(password, user.getPassword())) {
                    // Successful login
                    this.currentUser = user;
                    this.loggedIn = true;

                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Login Successful", "Welcome, " + user.getUsername() + "!"));

                    // Clear password from memory for security
                    this.password = null;

                    return "dashboard?faces-redirect=true";
                } else {
                    // Invalid password
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "Invalid username or password."));
                    return null;
                }
            } else {
                // User not found
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "Invalid username or password."));
                return null;
            }
        } catch (Exception e) {
            // Log the error (in production, use proper logging)
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "An error occurred during login. Please try again."));
            return null;
        }
    }



    public String login() {
        User user = userService.findByUsername(username);
        if (user != null && validatePassword(password, user.getPassword())) {
            this.currentUser = user;
            this.loggedIn = true;

            //redirect based on rolee
            switch(user.getRole().getRoleName()) {
                case ROLE_SYSTEM_ADMIN:
                    auditLogService.logAction("LOGIN", "User " + user.getUsername() + " logged in successfully.");
                    return "/dashboardTemplates/systemAdminDashboard.xhtml?faces-redirect=true";
                case ROLE_DOCTOR:
                    auditLogService.logAction("LOGIN", "User " + user.getUsername() + " logged in successfully.");
                    return "/dashboardTemplates/doctorDashboard.xhtml?faces-redirect=true";
                case ROLE_RECEPTIONIST:
                    auditLogService.logAction("LOGIN", "User " + user.getUsername() + " logged in successfully.");
                    return "/dashboardTemplates/receptionistDashboard.xhtml?faces-redirect=true";
                default:
                    return "/dashboardTemplates/dashboard.xhtml?faces-redirect=true"; //Fallback to current dashboard
            }
        } else {
            //proper error handling and return
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "Invalid username or password."));
            return null;
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
        if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$")) {
            // Use BCrypt to verify the password
            return BCrypt.checkpw(providedPassword, storedPassword);
        } else {
            // For plain text passwords (backward compatibility)
            return providedPassword.equals(storedPassword);
        }
    }

    public String logout() {
        try {
            // Clear session data
            this.currentUser = null;
            this.loggedIn = false;
            this.username = null;
            this.password = null;

            // Invalidate the HTTP session
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
            if (session != null) {
                session.invalidate();
            }

            // Add logout message
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Logout Successful", "You have been logged out successfully."));

            // Redirect to login page
            return "login?faces-redirect=true";

            // Redirect to login page
            return "login?faces-redirect=true";
            // Redirect to login page
            String contextPath = facesContext.getExternalContext().getRequestContextPath();
            return "/login.xhtml?faces-redirect=true";

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Logout Error", "An error occurred during logout."));
            return null;
        }
    }

    public void checkLogin() {
        if (!loggedIn || currentUser == null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String currentPage = facesContext.getViewRoot().getViewId();

            // Allow access to login page
            if (!currentPage.contains("login.xhtml")) {
                try {
                    facesContext.getExternalContext().redirect("login.xhtml");
                } catch (Exception e) {
                    // Handle redirect error
                    e.printStackTrace();
                }
            }
        }
    }

    public void initializeAdmin() {
        InitializeAdminService adminService = new InitializeAdminService();
        adminService.init();

    public void initializeAdmin() {
        InitializeAdminService adminService = new InitializeAdminService();
        adminService.init();
    public void checkSystemAdminAccess() {
        if (currentUser == null || !ROLE_SYSTEM_ADMIN.equals(currentUser.getRole().getRoleName())) {
            try {
                String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/login.xhtml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void checkDoctorAccess() {
        if (currentUser == null || !ROLE_DOCTOR.equals(currentUser.getRole().getRoleName())) {
            try {
                String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/login.xhtml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void checkReceptionistAccess() {
        if (currentUser == null || !ROLE_RECEPTIONIST.equals(currentUser.getRole().getRoleName())) {
            try {
                String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/login.xhtml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void preventBackdoorAccess() {
        FacesContext context = FacesContext.getCurrentInstance() ;
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

        //set headers to prevent caching
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0); // Proxies

        checkLogin(); //redirect to login if not logged in
    }

    public void initializeAdmin() {
//        InitializeAdminService adminService = new InitializeAdminService();
        initializeAdminService.init();

    }
}
















//package com.pahappa.hospital.beans;
//
//import java.io.Serializable;
//
//import com.pahappa.hospital.models.User;
//import com.pahappa.hospital.services.InitializeAdminService;
//import com.pahappa.hospital.services.UserService;
//import jakarta.annotation.PostConstruct;
//import jakarta.enterprise.context.SessionScoped;
//import jakarta.inject.Inject;
//import jakarta.faces.application.FacesMessage;
//import jakarta.faces.context.FacesContext;
//import jakarta.inject.Named;
//import jakarta.servlet.http.HttpSession;
//import org.mindrot.jbcrypt.BCrypt;
//
//@Named
//@SessionScoped
//public class LoginBean implements Serializable {
//
//    private boolean closed;
//    private String username;
//    private String password;
//    private boolean loggedIn = false;
//    private User currentUser;
//
//    private UserService userService;
//
//    @PostConstruct
//    public void init() {
//        this.userService = new UserService();
//    }
//
//    public void info() {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Message Content"));
//    }
//
//    public void warn() {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Message Content"));
//    }
//
//    public void error() {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Message Content."));
//    }
//
//    public void fatal() {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal", "Message Content."));
//    }
//
//    public void onClose() {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Message is closed", null));
//        closed = true;
//    }
//
//    public boolean isClosed() {
//        return closed;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public boolean isLoggedIn() {
//        return loggedIn;
//    }
//
//    public void setLoggedIn(boolean loggedIn) {
//        this.loggedIn = loggedIn;
//    }
//
//    public User getCurrentUser() {
//        return currentUser;
//    }
//
//    public void setCurrentUser(User currentUser) {
//        this.currentUser = currentUser;
//    }
//
//    public String login() {
//        User user = userService.findByUsername(username);
//        if (user != null) {
//            // Set session variables
//            this.currentUser = user;
//            this.loggedIn = true;
//
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Login Successful", "Welcome, " + username + "!"));
//            return "dashboard?faces-redirect=true";
//        } else {
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "Invalid username or password."));
//            return null;
//        }
//    }
//
//    public String logout() {
//        // Clear session data
//        this.currentUser = null;
//        this.loggedIn = false;
//        this.username = null;
//        this.password = null;
//
//        // Invalidate the HTTP session
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
//        if (session != null) {
//            session.invalidate();
//        }
//
//        // Add logout message
//        FacesContext.getCurrentInstance().addMessage(null,
//                new FacesMessage(FacesMessage.SEVERITY_INFO, "Logout Successful", "You have been logged out successfully."));
//
//        // Redirect to login page
//        return "login?faces-redirect=true";
//    }
//
//    public void initializeAdmin() {
//        InitializeAdminService adminService = new InitializeAdminService();
//        adminService.init();
//    }
//}
















//package com.pahappa.hospital.beans;
//
////package org.primefaces.showcase.view.message;
//
//import java.io.Serializable;
//
//import com.pahappa.hospital.models.User;
//import com.pahappa.hospital.services.InitializeAdminService;
//import com.pahappa.hospital.services.UserService;
//import jakarta.annotation.PostConstruct;
//import jakarta.enterprise.context.SessionScoped;
//import jakarta.inject.Inject;
//import jakarta.faces.application.FacesMessage;
//import jakarta.faces.context.FacesContext;
//import jakarta.inject.Named;
//import org.mindrot.jbcrypt.BCrypt;
//
//@Named
//@SessionScoped
//public class LoginBean implements Serializable {
//
//    private boolean closed;
//
//    private String username;
//    private String password;
//
//    private UserService userService;
//
//    @PostConstruct
//    public void init() {
//        this.userService = new UserService();
//    }
//
//    public void info() {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Message Content"));
//    }
//
//    public void warn() {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Message Content"));
//    }
//
//    public void error() {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Message Content."));
//    }
//
//    public void fatal() {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal", "Message Content."));
//    }
//
//    public void onClose() {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Message is closed", null));
//        closed = true;
//    }
//
//    public boolean isClosed() {
//        return closed;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String login() {
//        User user = userService.findByUsername(username);
//        if (user != null) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Login Successful", "Welcome, " + username + "!"));
//            return "dashboard?faces-redirect=true";
//        } else {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "Invalid username or password."));
//            return null;
//        }
//    }
//
//    public void initializeAdmin() {
//        InitializeAdminService adminService = new InitializeAdminService();
//        adminService.init();
//    }
//}
