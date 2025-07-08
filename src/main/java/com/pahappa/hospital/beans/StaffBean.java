package com.pahappa.hospital.beans;

import com.pahappa.hospital.enums.Department;
import com.pahappa.hospital.enums.Role;
import com.pahappa.hospital.enums.Shift;
import com.pahappa.hospital.models.Staff;
import com.pahappa.hospital.services.StaffService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named("staffBean")
@ViewScoped
public class StaffBean implements Serializable {

    private Staff staff = new Staff();
    private List<Staff> staffList;
    private String searchQuery;
    private Role searchRole;
    private Department searchDepartment;
    private Shift searchShift;
    private Long selectedStaffId;

    private List<Role> allRoles;
    private List<Department> allDepartments;
    private List<Shift> allShifts;

    @Inject
    private StaffService staffService;

    @PostConstruct
    public void init() {
        staffList = staffService.getAllStaff();
        allRoles = Arrays.asList(Role.values());
        allDepartments = Arrays.asList(Department.values());
        allShifts = Arrays.asList(Shift.values());
    }

    public void saveStaff() {
        if (staff.getStaffId() == null) {
            staffService.createStaff(staff);
        } else {
            staffService.updateStaff(staff);
        }
        staff = new Staff();
        init();
    }

    public void editStaff(Staff staff) {
        this.staff = staff;
    }

    public void deleteStaff(Long id) {
        staffService.softDeleteStaff(id);
        init();
    }

    public void restoreStaff() {
        if (selectedStaffId != null) {
            staffService.restoreStaff(selectedStaffId);
            init();
        }
    }

    public void searchStaffByName() {
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            staffList = staffService.searchStaffByName(searchQuery);
        } else {
            init();
        }
    }

    public void searchStaffByRole() {
        if (searchRole != null) {
            staffList = staffService.findStaffByRole(searchRole);
        } else {
            init();
        }
    }

    public void searchStaffByDepartment() {
        if (searchDepartment != null) {
            staffList = staffService.findStaffByDepartment(searchDepartment);
        } else {
            init();
        }
    }

    public void searchStaffByShift() {
        if (searchShift != null) {
            staffList = staffService.findStaffByShift(searchShift);
        } else {
            init();
        }
    }

    // Getters and Setters
    public Staff getStaff() { return staff; }
    public void setStaff(Staff staff) { this.staff = staff; }

    public List<Staff> getStaffList() { return staffList; }
    public void setStaffList(List<Staff> staffList) { this.staffList = staffList; }

    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }

    public Role getSearchRole() { return searchRole; }
    public void setSearchRole(Role searchRole) { this.searchRole = searchRole; }

    public Department getSearchDepartment() { return searchDepartment; }
    public void setSearchDepartment(Department searchDepartment) { this.searchDepartment = searchDepartment; }

    public Shift getSearchShift() { return searchShift; }
    public void setSearchShift(Shift searchShift) { this.searchShift = searchShift; }

    public Long getSelectedStaffId() { return selectedStaffId; }
    public void setSelectedStaffId(Long selectedStaffId) { this.selectedStaffId = selectedStaffId; }

    public List<Role> getAllRoles() { return allRoles; }
    public List<Department> getAllDepartments() { return allDepartments; }
    public List<Shift> getAllShifts() { return allShifts; }
}














//package com.pahappa.hospital.beans;
//
//import com.pahappa.hospital.enums.Department;
//import com.pahappa.hospital.enums.Role;
//import com.pahappa.hospital.enums.Shift;
//import com.pahappa.hospital.models.Staff;
//import com.pahappa.hospital.services.StaffService;
//import jakarta.annotation.PostConstruct;
//import jakarta.faces.view.ViewScoped;
//import jakarta.inject.Inject;
//import jakarta.inject.Named;
//
//import java.io.Serializable;
//import java.util.Arrays;
//import java.util.List;
//
//@Named
//@ViewScoped
//public class StaffBean implements Serializable {
//
//    private Staff staff = new Staff();
//    private List<Staff> staffList;
//    private String searchQuery;
//
//    @Inject
//    private StaffService staffService;
//
//    @PostConstruct
//    public void init() {
//        staffList = staffService.getAllStaff();
//    }
//
//    public void saveStaff() {
//        if (staff.getStaffId() == null) {
//            staffService.createStaff(staff);
//        } else {
//            staffService.updateStaff(staff);
//        }
//        staff = new Staff();
//        init();
//    }
//
//    public void editStaff(Staff s) {
//        this.staff = s;
//    }
//
//    public void deleteStaff(Long id) {
//        staffService.softDeleteStaff(id);
//        init();
//    }
//
//    public void searchStaffByName() {
//        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
//            staffList = staffService.searchStaffByName(searchQuery);
//        } else {
//            init();
//        }
//    }
//
//    // Optionally add more search methods here...
//
//    public List<Department> getDepartments() { return Arrays.asList(Department.values()); }
//    public List<Role> getRoles() { return Arrays.asList(Role.values()); }
//    public List<Shift> getShifts() { return Arrays.asList(Shift.values()); }
//
//    // Getters and setters
//    public Staff getStaff() { return staff; }
//    public void setStaff(Staff staff) { this.staff = staff; }
//
//    public List<Staff> getStaffList() { return staffList; }
//    public void setStaffList(List<Staff> staffList) { this.staffList = staffList; }
//
//    public String getSearchQuery() { return searchQuery; }
//    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////package com.pahappa.hospital.beans;
////
////import com.pahappa.hospital.models.Staff;
////import com.pahappa.hospital.services.StaffService;
////import jakarta.faces.view.ViewScoped;
////import jakarta.inject.Inject;
////import jakarta.inject.Named;
////import java.io.Serializable;
////import java.util.List;
////
////@Named // Makes it accessible in JSF pages
////@ViewScoped // Maintains state during page interactions
////public class StaffBean implements Serializable {
////
////    @Inject
////    private StaffService staffService;
////
////    private List<Staff> staffList;
////    private Staff newStaff = new Staff(); // For form binding
////
////    public void loadStaff() {
////        staffList = staffService.getAllStaff();
////    }
////
////    // Getters and setters
////    public List<Staff> getStaffList() {
////        return staffList;
////    }
////
////    public Staff getNewStaff() {
////        return newStaff;
////    }
////
////    public void setNewStaff(Staff newStaff) {
////        this.newStaff = newStaff;
////    }
////
////    // Action method
////    public String saveStaff() {
////        staffService.createStaff(newStaff);
////        newStaff = new Staff(); // Reset form
////        return "staff?faces-redirect=true"; // Reload page
////    }
////}