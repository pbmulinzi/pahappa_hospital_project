package com.pahappa.hospital.services;

import com.pahappa.hospital.daos.StaffDao;
import com.pahappa.hospital.enums.Department;
import com.pahappa.hospital.enums.Role;
import com.pahappa.hospital.enums.Shift;
import com.pahappa.hospital.models.Staff;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class StaffService {

    @Inject
    private StaffDao staffDao;
//    private StaffDao staffDao = new StaffDao();

    // Create new staff member
    public void createStaff(Staff staff) {
        staffDao.createStaff(staff);
    }

    // Update existing staff member
    public void updateStaff(Staff staff) {
        staffDao.updateStaff(staff);
    }

    // Soft delete staff member
    public void softDeleteStaff(Long staffId) {
        staffDao.softDeleteStaff(staffId);
    }

    // Get staff by ID (excluding deleted)
    public Staff getStaffById(Long staffId) {
        return staffDao.getStaffById(staffId);
    }

    // Get all staff (including soft-deleted)
    public List<Staff> getAllStaff() {
        return staffDao.getAllStaff();
    }

    // Get only active staff members
    public List<Staff> getActiveStaff() {
        return staffDao.getAllActiveStaff();
    }

    // Search staff by name (non-deleted only)
    public List<Staff> searchStaffByName(String name) {
        return staffDao.searchStaffByName(name);
    }

    // Search staff by role (non-deleted only)
    public List<Staff> findStaffByRole(Role role) {
        return staffDao.findStaffByRole(role);
    }

    // Search staff by department (non-deleted only)
    public List<Staff> findStaffByDepartment(Department department) {
        return staffDao.findStaffByDepartment(department);
    }

    // Search staff by shift (non-deleted only)
    public List<Staff> findStaffByShift(Shift shift) {
        return staffDao.findStaffByShift(shift);
    }

    // Restore soft-deleted staff member
    public void restoreStaff(Long staffId) {
        staffDao.restoreStaff(staffId);
    }

}
