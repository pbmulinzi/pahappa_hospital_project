package com.pahappa.hospital.menus;

import com.pahappa.hospital.enums.RecordType;
import com.pahappa.hospital.models.MedicalRecord;
import com.pahappa.hospital.models.Patient;
import com.pahappa.hospital.models.Doctor;
import com.pahappa.hospital.services.MedicalRecordService;
import com.pahappa.hospital.services.PatientService;
import com.pahappa.hospital.services.DoctorService;
import com.pahappa.hospital.daos.MedicalRecordDao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MedicalRecordMenu {

    private final MedicalRecordService recordService = new MedicalRecordService();
    private final PatientService patientService = new PatientService();
    private final DoctorService doctorService = new DoctorService();
    private final Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void manageMedicalRecords() {
        int choice;

        do {
            System.out.println("\n--- Medical Records Management ---\n");
            System.out.println("1. Create New Medical Record");
            System.out.println("2. Delete Medical Record");
            System.out.println("3. View Medical Record Details");
            System.out.println("4. View All Medical Records");
            System.out.println("5. Update Medical Record");
            System.out.println("6. Search Records by Patient");
            System.out.println("7. Search Records by Diagnosis");
            System.out.println("8. View Patient Medical History");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");

            choice = readInt("");

            switch (choice) {
                case 1:
                    createMedicalRecord();
                    break;
                case 2:
                    deleteMedicalRecord();
                    break;
                case 3:
                    viewRecordDetails();
                    break;
                case 4:
                    viewAllRecords();
                    break;
                case 5:
                    updateMedicalRecord();
                    break;
                case 6:
                    searchRecordsByPatient();
                    break;
                case 7:
                    searchRecordsByDiagnosis();
                    break;
                case 8:
                    viewPatientHistory();
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

    private LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return LocalDate.parse(input, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd format.");
            }
        }
    }

    private void createMedicalRecord() {
        System.out.println("\n--- Create New Medical Record ---\n");

        // Get patient
        int patientId = readInt("Patient ID: ");
        Patient patient = patientService.getPatientById(patientId);
        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        // Get doctor
        int doctorId = readInt("Doctor ID: ");
        Doctor doctor = doctorService.getDoctorById(doctorId);
        if (doctor == null) {
            System.out.println("Doctor not found.");
            return;
        }

        LocalDate recordDate = readDate("Record Date (yyyy-MM-dd): ");

        // Record type
        System.out.print("Record Type (CONSULTATION/LAB_RESULT/IMAGING_REPORT/etc.): ");
        RecordType recordType;
        try {
            recordType = RecordType.valueOf(scanner.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid record type. Defaulting to CONSULTATION.");
            recordType = RecordType.CONSULTATION;
        }

        System.out.print("Diagnosis: ");
        String diagnosis = scanner.nextLine();

        System.out.print("Treatment (optional): ");
        String treatment = scanner.nextLine();

        System.out.print("Prescription (optional): ");
        String prescription = scanner.nextLine();

        System.out.print("Notes (optional): ");
        String notes = scanner.nextLine();

        MedicalRecord record = new MedicalRecord();
        record.setPatient(patient);
        record.setDoctor(doctor);
        record.setRecordDate(recordDate);
        record.setRecordType(recordType);
        record.setDiagnosis(diagnosis);
        record.setTreatment(treatment);
        record.setPrescription(prescription);
        record.setNotes(notes);

        recordService.createMedicalRecord(record);
        System.out.println("\nMedical record created successfully!");
    }

    private void deleteMedicalRecord() {
        int recordId = readInt("\nEnter Medical Record ID to delete: ");
        recordService.softDeleteMedicalRecord(recordId);
        System.out.println("Medical record marked as deleted.");
    }

    private void viewRecordDetails() {
        int recordId = readInt("\nEnter Medical Record ID: ");
        MedicalRecord record = recordService.getMedicalRecordById(recordId);

        if (record != null) {
            System.out.println("\n--- Medical Record Details ---");
            System.out.println("Record ID: " + record.getRecordId());
            System.out.println("Patient: " + record.getPatient().getPatientFirstName() +
                    " " + record.getPatient().getPatientLastName());
            System.out.println("Doctor: " + record.getDoctor().getDoctorFirstName() +
                    " " + record.getDoctor().getDoctorLastName());
            System.out.println("Date: " + record.getRecordDate());
            System.out.println("Type: " + record.getRecordType());
            System.out.println("Diagnosis: " + record.getDiagnosis());
            System.out.println("Treatment: " +
                    (record.getTreatment() != null ? record.getTreatment() : "N/A"));
            System.out.println("Prescription: " +
                    (record.getPrescription() != null ? record.getPrescription() : "N/A"));
            System.out.println("Notes: " +
                    (record.getNotes() != null ? record.getNotes() : "N/A"));
        } else {
            System.out.println("Medical record not found.");
        }
    }

    private void viewAllRecords() {
        System.out.println("\n--- All Medical Records ---");
        List<MedicalRecord> records = recordService.getAllMedicalRecords();

        if (records.isEmpty()) {
            System.out.println("No medical records found.");
            return;
        }

        for (MedicalRecord record : records) {
            System.out.printf("[%d] %s | %s | %s | %s%n",
                    record.getRecordId(),
                    record.getRecordDate(),
                    record.getPatient().getPatientFirstName() + " " + record.getPatient().getPatientLastName(),
                    record.getRecordType(),
                    record.getDiagnosis().length() > 50 ?
                            record.getDiagnosis().substring(0, 50) + "..." : record.getDiagnosis());
        }
    }

    private void updateMedicalRecord() {
        int recordId = readInt("\nEnter Medical Record ID to update: ");
        MedicalRecord record = recordService.getMedicalRecordById(recordId);

        if (record == null) {
            System.out.println("Medical record not found.");
            return;
        }

        System.out.println("\nLeave blank to keep current value");

        System.out.print("Date [" + record.getRecordDate() + "]: ");
        String dateInput = scanner.nextLine();
        if (!dateInput.isBlank()) {
            try {
                record.setRecordDate(LocalDate.parse(dateInput, dateFormatter));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Keeping existing value.");
            }
        }

        System.out.print("Type [" + record.getRecordType() + "]: ");
        String typeInput = scanner.nextLine();
        if (!typeInput.isBlank()) {
            try {
                record.setRecordType(RecordType.valueOf(typeInput.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid record type. Keeping existing value.");
            }
        }

        System.out.print("Diagnosis [" + record.getDiagnosis() + "]: ");
        String diagnosis = scanner.nextLine();
        if (!diagnosis.isBlank()) record.setDiagnosis(diagnosis);

        System.out.print("Treatment [" + (record.getTreatment() != null ? record.getTreatment() : "") + "]: ");
        String treatment = scanner.nextLine();
        if (!treatment.isBlank()) record.setTreatment(treatment);

        System.out.print("Prescription [" + (record.getPrescription() != null ? record.getPrescription() : "") + "]: ");
        String prescription = scanner.nextLine();
        if (!prescription.isBlank()) record.setPrescription(prescription);

        System.out.print("Notes [" + (record.getNotes() != null ? record.getNotes() : "") + "]: ");
        String notes = scanner.nextLine();
        if (!notes.isBlank()) record.setNotes(notes);

        recordService.updateMedicalRecord(record);
        System.out.println("Medical record updated successfully.");
    }

    private void searchRecordsByPatient() {
        System.out.print("\nEnter patient Id to search: ");
        String id = scanner.nextLine();
        Patient patient = patientService.searchPatientById(id);
        List<MedicalRecord> results = recordService.getRecordsByPatient(patient);
        displaySearchResults("Patient", patient.getFullPatientName(), results);
    }

    private void searchRecordsByDiagnosis() {
        System.out.print("\nEnter diagnosis keyword to search: ");
        String keyword = scanner.nextLine();
        List<MedicalRecord> results = recordService.searchRecordsByDiagnosis(keyword);
        displaySearchResults("Diagnosis", keyword, results);
    }

    private void viewPatientHistory() {
        int patientId = readInt("\nEnter Patient ID: ");
        List<MedicalRecord> history = recordService.getPatientMedicalHistory(patientId);

        if (history.isEmpty()) {
            System.out.println("No medical history found for this patient.");
            return;
        }

        System.out.println("\n--- Medical History for Patient ID: " + patientId + " ---");
        history.forEach(record -> System.out.printf("%s | %s | %s%n",
                record.getRecordDate(),
                record.getRecordType(),
                record.getDiagnosis()));
    }

    private void displaySearchResults(String criteria, String value, List<MedicalRecord> results) {
        if (results.isEmpty()) {
            System.out.println("No records found with " + criteria + ": " + value);
        } else {
            System.out.println("\n--- Search Results (" + results.size() + " found) ---");
            results.forEach(record -> System.out.printf("%d: %s | %s | %s%n",
                    record.getRecordId(),
                    record.getRecordDate(),
                    record.getPatient().getPatientFirstName() + " " + record.getPatient().getPatientLastName(),
                    record.getDiagnosis()));
        }
    }
}
