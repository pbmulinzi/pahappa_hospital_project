package com.pahappa.hospital.menus;

import com.pahappa.hospital.enums.AppointmentStatus;
import com.pahappa.hospital.models.Appointment;
import com.pahappa.hospital.models.Doctor;
import com.pahappa.hospital.models.Patient;
import com.pahappa.hospital.services.AppointmentService;
import com.pahappa.hospital.services.DoctorService;
import com.pahappa.hospital.services.PatientService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class AppointmentMenu {

    private final AppointmentService appointmentService = new AppointmentService();
    private final PatientService patientService = new PatientService();
    private final DoctorService doctorService = new DoctorService();
    private final Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void manageAppointments() {
        int choice;

        do {
            System.out.println("\n--- Appointment Management ---\n");
            System.out.println("1. Schedule New Appointment");
            System.out.println("2. Cancel Appointment");
            System.out.println("3. View Appointment Details");
            System.out.println("4. View All Appointments");
            System.out.println("5. Update Appointment");
            System.out.println("6. Search by Patient Name");
            System.out.println("7. Search by Doctor Name");
            System.out.println("8. Search by Date");
            System.out.println("9. Search by Status");
            System.out.println("10. Restore Cancelled Appointment");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");

            choice = readInt("");

            switch (choice) {
                case 1:
                    scheduleAppointment();
                    break;
                case 2:
                    cancelAppointment();
                    break;
                case 3:
                    viewAppointmentDetails();
                    break;
                case 4:
                    viewAllAppointments();
                    break;
                case 5:
                    updateAppointment();
                    break;
                case 6:
                    searchByPatientName();
                    break;
                case 7:
                    searchByDoctorName();
                    break;
                case 8:
                    searchByDate();
                    break;
                case 9:
                    searchByStatus();
                    break;
                case 10:
                    restoreAppointment();
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

    private LocalDateTime readDateTime(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return LocalDateTime.parse(input, dateTimeFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Please use yyyy-MM-dd HH:mm format.");
            }
        }
    }

    private void scheduleAppointment() {
        System.out.println("\n--- Schedule New Appointment ---\n");

        // Get patient
        System.out.print("Patient ID: ");
        int patientId = readInt("");
        Patient patient = patientService.getPatientById(patientId);
        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        // Get doctor
        System.out.print("Doctor ID: ");
        int doctorId = readInt("");
        Doctor doctor = doctorService.getDoctorById(doctorId);
        if (doctor == null) {
            System.out.println("Doctor not found.");
            return;
        }

        // Get date/time
        LocalDateTime dateTime = readDateTime("Appointment Date/Time (yyyy-MM-dd HH:mm): ");

        System.out.print("Reason: ");
        String reason = scanner.nextLine();

        // Create appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setReason(reason);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        appointmentService.createAppointment(appointment);
        System.out.println("\n Appointment scheduled successfully!");
    }

    private void cancelAppointment() {
        int id = readInt("\nEnter Appointment ID to cancel: ");
        appointmentService.cancelAppointment(id);
        System.out.println("Appointment cancelled successfully.");
    }

    private void viewAppointmentDetails() {
        int id = readInt("\nEnter Appointment ID: ");
        Appointment appointment = appointmentService.getAppointmentById(id);

        if (appointment != null) {
            System.out.println("\n--- Appointment Details ---");
            System.out.println("ID: " + appointment.getAppointmentId());
            System.out.println("Patient: " + appointment.getPatient().getPatientFirstName() +
                    " " + appointment.getPatient().getPatientLastName());
            System.out.println("Doctor: " + appointment.getDoctor().getDoctorFirstName() +
                    " " + appointment.getDoctor().getDoctorLastName());
            System.out.println("Date/Time: " + formatDateTime(appointment.getAppointmentDateTime()));
            System.out.println("Reason: " + appointment.getReason());
            System.out.println("Status: " + appointment.getStatus());
            System.out.println("Notes: " + (appointment.getNotes() != null ? appointment.getNotes() : "N/A"));
        } else {
            System.out.println("Appointment not found.");
        }
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a"));
    }

    private void viewAllAppointments() {
        System.out.println("\n--- All Appointments ---");
        List<Appointment> appointments = appointmentService.getAllAppointments();

        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }

        for (Appointment appt : appointments) {
            System.out.printf("[%d] %s | %s %s -> %s %s | %s | %s%n",
                    appt.getAppointmentId(),
                    formatDateTime(appt.getAppointmentDateTime()),
                    appt.getPatient().getPatientFirstName(),
                    appt.getPatient().getPatientLastName(),
                    appt.getDoctor().getDoctorFirstName(),
                    appt.getDoctor().getDoctorLastName(),
                    appt.getStatus(),
                    appt.isDeleted() ? "CANCELLED" : "ACTIVE");
        }
    }

    private void updateAppointment() {
        int id = readInt("\nEnter Appointment ID to update: ");
        Appointment appointment = appointmentService.getAppointmentById(id);

        if (appointment == null) {
            System.out.println("Appointment not found.");
            return;
        }

        System.out.println("\nLeave blank to keep current value");

        // Update date/time
        System.out.print("Date/Time [" + formatDateTime(appointment.getAppointmentDateTime()) + "]: ");
        String dateInput = scanner.nextLine();
        if (!dateInput.isBlank()) {
            try {
                LocalDateTime newDateTime = LocalDateTime.parse(dateInput, dateTimeFormatter);
                appointment.setAppointmentDateTime(newDateTime);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Keeping existing date/time.");
            }
        }

        // Update status
        System.out.print("Status [" + appointment.getStatus() + "]: ");
        String statusInput = scanner.nextLine();
        if (!statusInput.isBlank()) {
            try {
                AppointmentStatus status = AppointmentStatus.valueOf(statusInput.toUpperCase());
                appointment.setStatus(status);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status. Keeping existing value.");
            }
        }

        // Update reason
        System.out.print("Reason [" + appointment.getReason() + "]: ");
        String reason = scanner.nextLine();
        if (!reason.isBlank()) appointment.setReason(reason);

        // Update notes
        System.out.print("Notes [" + (appointment.getNotes() != null ? appointment.getNotes() : "") + "]: ");
        String notes = scanner.nextLine();
        if (!notes.isBlank()) appointment.setNotes(notes);

        appointmentService.updateAppointment(appointment);
        System.out.println("Appointment updated successfully.");
    }

    private void searchByPatientName() {
        System.out.print("\nEnter patient name to search: ");
        String name = scanner.nextLine();
        List<Appointment> results = appointmentService.getAppointmentsByPatientName(name);
        displaySearchResults("Patient Name", name, results);
    }

    private void searchByDoctorName() {
        System.out.print("\nEnter doctor name to search: ");
        String name = scanner.nextLine();
        List<Appointment> results = appointmentService.getAppointmentsByDoctorName(name);
        displaySearchResults("Doctor Name", name, results);
    }

    private void searchByDate() {
        LocalDate date = readDate("Enter date (yyyy-MM-dd): ");
        List<Appointment> results = appointmentService.getAppointmentsByDate(date);
        displaySearchResults("Date", date.toString(), results);
    }

    private void searchByStatus() {
        System.out.print("\nEnter status (" + getEnumValues(AppointmentStatus.values()) + "): ");
        String statusInput = scanner.nextLine();
        try {
            AppointmentStatus status = AppointmentStatus.valueOf(statusInput.toUpperCase());
            List<Appointment> results = appointmentService.getAppointmentsByStatus(status);
            displaySearchResults("Status", statusInput, results);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status entered.");
        }
    }

    private void restoreAppointment() {
        int id = readInt("\nEnter Appointment ID to restore: ");
        appointmentService.restoreAppointment(id);
        System.out.println("Appointment restored successfully.");
    }

    private String getEnumValues(Enum<?>[] values) {
        StringBuilder sb = new StringBuilder();
        for (Enum<?> value : values) {
            sb.append(value.name()).append("/");
        }
        return sb.substring(0, sb.length() - 1);
    }

    private void displaySearchResults(String criteria, String value, List<Appointment> results) {
        if (results.isEmpty()) {
            System.out.println("No appointments found with " + criteria + ": " + value);
        } else {
            System.out.println("\n--- Search Results (" + results.size() + " found) ---");
            results.forEach(appt -> System.out.printf("%d: %s | %s -> %s | %s%n",
                    appt.getAppointmentId(),
                    formatDateTime(appt.getAppointmentDateTime()),
                    appt.getPatient().getPatientFirstName() + " " + appt.getPatient().getPatientLastName(),
                    appt.getDoctor().getDoctorFirstName() + " " + appt.getDoctor().getDoctorLastName(),
                    appt.getStatus()));
        }
    }
}
