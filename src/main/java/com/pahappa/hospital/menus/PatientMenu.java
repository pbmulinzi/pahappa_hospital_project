package com.pahappa.hospital.menus;

import com.pahappa.hospital.enums.Gender;
import com.pahappa.hospital.models.Patient;
import com.pahappa.hospital.services.PatientService;

import java.util.List;
import java.util.Scanner;

public class PatientMenu {

    private final PatientService patientService = new PatientService();
    private final Scanner scanner = new Scanner(System.in);

    public void managePatients() {
        int choice;

        do {
            System.out.println("\n--- Patient Management ---");
            System.out.println("1. Register New Patient");
            System.out.println("2. Delete Patient"); //soft delete
            System.out.println("3. View Patient Details");
            System.out.println("4. View All Patients");
            System.out.println("5. Update Patient");
            System.out.println("6. Search Patient(s) by Name");
            System.out.println("7. Restore Discharged Patient");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");

            choice = readInt("");  // Use new input method

            switch (choice) {
                case 1:
                    registerPatient();
                    break;
                case 2:
                    dischargePatient();
                    break;
                case 3:
                    viewPatientDetails();
                    break;
                case 4:
                    viewAllPatients();
                    break;
                case 5:
                    updatePatient();
                    break;
                case 6:
                    searchPatientsByName();
                    break;
                case 7:
                    restorePatient();
                    break;
                case 0:
                    System.out.println("Returning to main menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }

    // Helper methods for input validation
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

    private String readPhone(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            String digits = input.replaceAll("[^0-9]", "");

            if (digits.length() >= 10 && digits.length() <= 15) {
                return digits;
            }
            System.out.println("Phone must have 10-15 digits. Try again.");
        }
    }

    private void registerPatient() {
        System.out.println("\n--- Patient Registration ---");

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        String phone = readPhone("Phone Number: ");

        System.out.print("Gender: ");
        Gender gender = Gender.valueOf(scanner.nextLine().toUpperCase());


        // Create patient using no-arg constructor and setters
        Patient patient = new Patient();
        patient.setPatientFirstName(firstName);
        patient.setPatientLastName(lastName);
        patient.setPatientPhoneNumber(phone);
        patient.setGender(gender);
        patient.setDeleted(false);

        patientService.createPatient(patient);
        System.out.println("\n Patient registered successfully!");
    }

    private void dischargePatient() {
        int id = readInt("\nEnter Patient ID to discharge: ");
        patientService.softDeletePatient(id);
        System.out.println("Patient discharged successfully.");
    }

    private void viewPatientDetails() {
        int id = readInt("\nEnter Patient ID: ");
        Patient patient = patientService.getPatientById(id);

        if (patient != null) {
            System.out.println("\n--- Patient Details ---");
            System.out.println("ID: " + patient.getPatientId());
            System.out.println("Name: " + patient.getPatientFirstName() + " " + patient.getPatientLastName());
            System.out.println("Phone: " + patient.getPatientPhoneNumber());
            System.out.println("Status: " + (patient.isDeleted() ? "DISCHARGED" : "ACTIVE"));
        } else {
            System.out.println("Patient not found.");
        }
    }

    private void viewAllPatients() {
        System.out.println("\n--- All Patients ---");
        List<Patient> patients = patientService.getAllPatients();

        if (patients.isEmpty()) {
            System.out.println("No patients found.");
            return;
        }

        for (Patient p : patients) {
            System.out.printf("[%d] %s %s | %s | %s%n",
                    p.getPatientId(),
                    p.getPatientFirstName(),
                    p.getPatientLastName(),
                    p.getPatientPhoneNumber(),
                    p.isDeleted() ? "DISCHARGED" : "ACTIVE");
        }
    }

    private void updatePatient() {
        int id = readInt("\nEnter Patient ID to update: ");
        Patient patient = patientService.getPatientById(id);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.println("\nLeave blank to keep current value");

        System.out.print("First Name [" + patient.getPatientFirstName() + "]: ");
        String firstName = scanner.nextLine();
        if (!firstName.isBlank()) patient.setPatientFirstName(firstName);

        System.out.print("Last Name [" + patient.getPatientLastName() + "]: ");
        String lastName = scanner.nextLine();
        if (!lastName.isBlank()) patient.setPatientLastName(lastName);

        System.out.print("Phone [" + patient.getPatientPhoneNumber() + "]: ");
        String phone = scanner.nextLine();
        if (!phone.isBlank()) {
            String cleanedPhone = phone.replaceAll("[^0-9]", "");
            if (cleanedPhone.length() >= 10 && cleanedPhone.length() <= 15) {
                patient.setPatientPhoneNumber(cleanedPhone);
            } else {
                System.out.println("Invalid phone number. Keeping existing value.");
            }
        }

        patientService.updatePatient(patient);
        System.out.println("Patient information updated successfully.");
    }

    private void searchPatientsByName() {
        System.out.print("\nEnter patient name to search: ");
        String name = scanner.nextLine();
        List<Patient> results = patientService.searchPatientsByName(name);

        if (results.isEmpty()) {
            System.out.println("No matching patients found.");
            return;
        }

        System.out.println("\n--- Search Results ---");
        results.forEach(p -> System.out.printf("%d: %s %s (%s)%n",
                p.getPatientId(),
                p.getPatientFirstName(),
                p.getPatientLastName(),
                p.getPatientPhoneNumber()));
    }

    private void restorePatient() {
        int id = readInt("\nEnter Patient ID to restore: ");
        patientService.restorePatient(id);
        System.out.println("Patient restored successfully.");
    }
}