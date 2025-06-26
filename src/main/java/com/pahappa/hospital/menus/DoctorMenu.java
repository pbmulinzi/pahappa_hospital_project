package com.pahappa.hospital.menus;

import com.pahappa.hospital.models.Doctor;
import com.pahappa.hospital.enums.Shift;
import com.pahappa.hospital.enums.Speciality;
import com.pahappa.hospital.services.DoctorService;

import java.util.List;
import java.util.Scanner;

public class DoctorMenu {

    private final DoctorService doctorService = new DoctorService();
    private final Scanner scanner = new Scanner(System.in);

    public void manageDoctors() {
        int choice;

        do {
            System.out.println("\n--- Doctor Menu ---\n");
            System.out.println("1. Create Doctor");
            System.out.println("2. Delete Doctor");
            System.out.println("3. Get Doctor by ID");
            System.out.println("4. View all Doctors");
            System.out.println("5. Update Doctor");
            System.out.println("6. Search Doctor(s) by Name");
            System.out.println("7. Search Doctor(s) by Speciality");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");

            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    createDoctor();
                    break;
                case 2:
                    deleteDoctor();
                    break;
                case 3:
                    getDoctorById();
                    break;
                case 4:
                    viewAllDoctors();
                    break;
                case 5:
                    updateDoctor();
                    break;
                case 6:
                    findDoctorByName();
                    break;
                case 7:
                    findDoctorBySpeciality();
                    break;
                case 0:
                    System.out.println("Returning to main menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 0);
    }

    private void createDoctor() {
        System.out.println("\n--- Create New Doctor ---\n");

        // For new doctor creation, id can be 0 or ignored (assuming DAO handles new entity)
        int id = 0;

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();

        System.out.print("Shift (NIGHT/MORNING/AFTERNOON): ");
        Shift shift;
        try {
            shift = Shift.valueOf(scanner.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid shift entered, defaulting to MORNING.");
            shift = Shift.MORNING;
        }

        System.out.print("Speciality: ");
        Speciality speciality;
        try {
            speciality = Speciality.valueOf(scanner.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid speciality entered, defaulting to CARDIOLOGY.");
            speciality = Speciality.ENT;
        }

        Doctor doctor = new Doctor(id, firstName, lastName, phone, shift, speciality);
        doctorService.createDoctor(doctor);
        System.out.println("Doctor saved successfully.");
    }

    private void deleteDoctor() {
        System.out.print("Enter Doctor ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        doctorService.softDeleteDoctor(id);
        System.out.println("Doctor marked as deleted.");
    }

    private void getDoctorById() {
        System.out.print("Enter Doctor ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        Doctor doctor = doctorService.getDoctorById(id);
        if (doctor != null) {
            System.out.println("\nDoctor Details:");
            System.out.println(doctor);
        } else {
            System.out.println("Doctor not found or is deleted.");
        }
    }

    private void viewAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        System.out.println("\n--- All Doctors (including deleted) ---");
        doctors.forEach(System.out::println);
    }

    private void updateDoctor() {
        System.out.print("Enter Doctor ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        Doctor existingDoctor = doctorService.getDoctorById(id);
        if (existingDoctor == null) {
            System.out.println("Doctor not found or is deleted. Cannot update.");
            return;
        }

        System.out.print("First Name [" + existingDoctor.getDoctorFirstName() + "]: ");
        String firstName = scanner.nextLine();
        if (!firstName.isBlank()) {
            existingDoctor.setDoctorFirstName(firstName);
        }

        System.out.print("Last Name [" + existingDoctor.getDoctorLastName() + "]: ");
        String lastName = scanner.nextLine();
        if (!lastName.isBlank()) {
            existingDoctor.setDoctorLastName(lastName);
        }

        System.out.print("Phone Number [" + existingDoctor.getDoctorPhoneNumber() + "]: ");
        String phone = scanner.nextLine();
        if (!phone.isBlank()) {
            existingDoctor.setDoctorPhoneNumber(phone);
        }

        System.out.print("Shift (NIGHT/MORNING/AFTERNOON) [" + existingDoctor.getShift() + "]: ");
        String shiftInput = scanner.nextLine();
        if (!shiftInput.isBlank()) {
            try {
                Shift shift = Shift.valueOf(shiftInput.toUpperCase());
                existingDoctor.setShift(shift);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid shift entered, keeping existing value.");
            }
        }

        System.out.print("Speciality [" + existingDoctor.getSpeciality() + "]: ");
        String specialityInput = scanner.nextLine();
        if (!specialityInput.isBlank()) {
            try {
                Speciality speciality = Speciality.valueOf(specialityInput.toUpperCase());
                existingDoctor.setSpeciality(speciality);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid speciality entered, keeping existing value.");
            }
        }

        doctorService.updateDoctor(existingDoctor);
        System.out.println("Doctor updated successfully.");
    }

    private void findDoctorByName() {
        System.out.print("Enter name to search: ");
        String name = scanner.nextLine();
        List<Doctor> results = doctorService.searchDoctorsByName(name);
        if (results.isEmpty()) {
            System.out.println("No doctors found matching that name.");
        } else {
            System.out.println("\n--- Search Results ---");
            results.forEach(System.out::println);
        }
    }

    private void findDoctorBySpeciality() {
        System.out.print("Enter speciality to search: ");
        String specialityInput = scanner.nextLine();
        Speciality speciality;
        try {
            speciality = Speciality.valueOf(specialityInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid speciality entered.");
            return;
        }

        // You would need to implement this method in your DoctorService and DAO
        List<Doctor> results = doctorService.findDoctorsBySpeciality(speciality);
        if (results.isEmpty()) {
            System.out.println("No doctors found with speciality: " + speciality);
        } else {
            System.out.println("\n--- Search Results ---");
            results.forEach(System.out::println);
        }
    }
}



