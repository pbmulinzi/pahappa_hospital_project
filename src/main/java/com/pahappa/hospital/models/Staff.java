package com.pahappa.hospital.models;

import com.pahappa.hospital.enums.Department;
import com.pahappa.hospital.enums.Role;
import com.pahappa.hospital.enums.Shift;
import jakarta.persistence.*;
import org.hibernate.annotations.Filter;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private int staffId;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "phone_number", unique = true, nullable = false, length = 15)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Shift shift;

    @Column(nullable = false)
    @Filter(name = "deletedStaffFilter", condition = "deleted = :isDeleted")
    private boolean deleted = false;

    // Constructors
    public Staff() {}

    public Staff(String firstName, String lastName, String phoneNumber,
                 Role role, Department department, Shift shift) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.department = department;
        this.shift = shift;
        this.deleted = false;
    }

    // Getters and Setters
    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    // Utility method
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // toString
    @Override
    public String toString() {
        return "Staff [ID=" + staffId +
                ", Name=" + getFullName() +
                ", Phone=" + phoneNumber +
                ", Role=" + role +
                ", Department=" + department +
                ", Shift=" + shift + "]";
    }
}
