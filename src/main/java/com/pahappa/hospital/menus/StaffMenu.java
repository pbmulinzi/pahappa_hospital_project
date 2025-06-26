package com.pahappa.hospital.menus;

import com.pahappa.hospital.enums.Department;
import com.pahappa.hospital.enums.Role;
import com.pahappa.hospital.enums.Shift;
import com.pahappa.hospital.models.Staff;
import com.pahappa.hospital.services.StaffService;

import java.util.List;
import java.util.Scanner;

public class StaffMenu {

    private final StaffService staffService = new StaffService();
    private final Scanner scanner = new Scanner(System.in);

    public void manageStaff() {
        int choice;

        do {
            System.out.println("\n--- Staff Management ---");
            System.out.println("1. Add New Staff Member");
            System.out.println("2. Remove Staff Member (Soft Delete)");
            System.out.println("3. View Staff Details");
            System.out.println("4. View All Staff Members");
            System.out.println("5. Update Staff Information");
            System.out.println("6. Search Staff by Name");
            System.out.println("7. Search Staff by Role");
            System.out.println("8. Search Staff by Department");
            System.out.println("9. Search Staff by Shift");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");

            choice = readInt("");

            switch (choice) {
                case 1:
                    createStaff();
                    break;
                case 2:
                    deleteStaff();
                    break;
                case 3:
                    getStaffById();
                    break;
                case 4:
                    viewAllStaff();
                    break;
                case 5:
                    updateStaff();
                    break;
                case 6:
                    findStaffByName();
                    break;
                case 7:
                    findStaffByRole();
                    break;
                case 8:
                    findStaffByDepartment();
                    break;
                case 9:
                    findStaffByShift();
                    break;
                case 0:
                    System.out.println("Returning to main menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid integer.");
            }
        }
    }

    private void createStaff() {
        System.out.println("\n--- Add New Staff Member ---");

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();

        // Role input with validation
        Role role = null;
        while (role == null) {
            System.out.print("Role (" + getEnumValues(Role.values()) + "): ");
            try {
                role = Role.valueOf(scanner.nextLine().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid role. Please choose from: " + getEnumValues(Role.values()));
            }
        }

        // Department input with validation
        Department department = null;
        while (department == null) {
            System.out.print("Department (" + getEnumValues(Department.values()) + "): ");
            try {
                department = Department.valueOf(scanner.nextLine().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid department. Please choose from: " + getEnumValues(Department.values()));
            }
        }

        // Shift input with validation
        Shift shift = null;
        while (shift == null) {
            System.out.print("Shift (" + getEnumValues(Shift.values()) + "): ");
            try {
                shift = Shift.valueOf(scanner.nextLine().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid shift. Please choose from: " + getEnumValues(Shift.values()));
            }
        }

        Staff staff = new Staff(firstName, lastName, phone, role, department, shift);
        staffService.createStaff(staff);
        System.out.println("\n✅ Staff member added successfully!");
    }

    private String getEnumValues(Enum<?>[] values) {
        StringBuilder sb = new StringBuilder();
        for (Enum<?> value : values) {
            sb.append(value.name()).append("/");
        }
        return sb.substring(0, sb.length() - 1); // Remove trailing slash
    }

    private void deleteStaff() {
        int id = readInt("Enter Staff ID to remove: ");
        staffService.softDeleteStaff(id);
        System.out.println("Staff member marked as removed.");
    }

    private void getStaffById() {
        int id = readInt("Enter Staff ID: ");
        Staff staff = staffService.getStaffById(id);
        if (staff != null) {
            System.out.println("\n--- Staff Details ---");
            System.out.println(staff);
        } else {
            System.out.println("Staff member not found or is removed.");
        }
    }

    private void viewAllStaff() {
        List<Staff> staffList = staffService.getAllStaff();
        System.out.println("\n--- All Staff Members ---");
        if (staffList.isEmpty()) {
            System.out.println("No staff members found.");
            return;
        }
        staffList.forEach(System.out::println);
    }

    private void updateStaff() {
        int id = readInt("Enter Staff ID to update: ");
        Staff existingStaff = staffService.getStaffById(id);

        if (existingStaff == null) {
            System.out.println("Staff member not found or is removed. Cannot update.");
            return;
        }

        System.out.println("\nLeave blank to keep current value");

        System.out.print("First Name [" + existingStaff.getFirstName() + "]: ");
        String firstName = scanner.nextLine();
        if (!firstName.isBlank()) existingStaff.setFirstName(firstName);

        System.out.print("Last Name [" + existingStaff.getLastName() + "]: ");
        String lastName = scanner.nextLine();
        if (!lastName.isBlank()) existingStaff.setLastName(lastName);

        System.out.print("Phone Number [" + existingStaff.getPhoneNumber() + "]: ");
        String phone = scanner.nextLine();
        if (!phone.isBlank()) existingStaff.setPhoneNumber(phone);

        System.out.print("Role [" + existingStaff.getRole() + "]: ");
        String roleInput = scanner.nextLine();
        if (!roleInput.isBlank()) {
            try {
                Role role = Role.valueOf(roleInput.toUpperCase());
                existingStaff.setRole(role);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid role entered, keeping existing value.");
            }
        }

        System.out.print("Department [" + existingStaff.getDepartment() + "]: ");
        String deptInput = scanner.nextLine();
        if (!deptInput.isBlank()) {
            try {
                Department department = Department.valueOf(deptInput.toUpperCase());
                existingStaff.setDepartment(department);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid department entered, keeping existing value.");
            }
        }

        System.out.print("Shift [" + existingStaff.getShift() + "]: ");
        String shiftInput = scanner.nextLine();
        if (!shiftInput.isBlank()) {
            try {
                Shift shift = Shift.valueOf(shiftInput.toUpperCase());
                existingStaff.setShift(shift);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid shift entered, keeping existing value.");
            }
        }

        staffService.updateStaff(existingStaff);
        System.out.println("Staff information updated successfully.");
    }

    private void findStaffByName() {
        System.out.print("Enter name to search: ");
        String name = scanner.nextLine();
        List<Staff> results = staffService.searchStaffByName(name);
        displaySearchResults("Name", name, results);
    }

    private void findStaffByRole() {
        System.out.print("Enter role to search: ");
        String roleInput = scanner.nextLine();
        try {
            Role role = Role.valueOf(roleInput.toUpperCase());
            List<Staff> results = staffService.findStaffByRole(role);
            displaySearchResults("Role", roleInput, results);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid role entered.");
        }
    }

    private void findStaffByDepartment() {
        System.out.print("Enter department to search: ");
        String deptInput = scanner.nextLine();
        try {
            Department department = Department.valueOf(deptInput.toUpperCase());
            List<Staff> results = staffService.findStaffByDepartment(department);
            displaySearchResults("Department", deptInput, results);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid department entered.");
        }
    }

    private void findStaffByShift() {
        System.out.print("Enter shift to search: ");
        String shiftInput = scanner.nextLine();
        try {
            Shift shift = Shift.valueOf(shiftInput.toUpperCase());
            List<Staff> results = staffService.findStaffByShift(shift);
            displaySearchResults("Shift", shiftInput, results);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid shift entered.");
        }
    }

    private void displaySearchResults(String criteria, String value, List<Staff> results) {
        if (results.isEmpty()) {
            System.out.println("No staff members found with " + criteria + ": " + value);
        } else {
            System.out.println("\n--- Search Results (" + results.size() + " found) ---");
            results.forEach(System.out::println);
        }
    }
}
