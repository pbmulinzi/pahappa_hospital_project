package com.pahappa.hospital.menus;

import com.pahappa.hospital.enums.PaymentMethod;
import com.pahappa.hospital.enums.PaymentStatus;
import com.pahappa.hospital.models.Billing;
import com.pahappa.hospital.models.Patient;
import com.pahappa.hospital.services.BillingService;
import com.pahappa.hospital.services.PatientService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class BillingMenu {

    private final BillingService billingService = new BillingService();
    private final PatientService patientService = new PatientService();
    private final Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void manageBilling() {
        int choice;

        do {
            System.out.println("\n--- Billing Management ---\n");
            System.out.println("1. Create New Bill");
            System.out.println("2. Delete Bill");
            System.out.println("3. View Bill Details");
            System.out.println("4. View All Bills");
            System.out.println("5. Update Bill");
            System.out.println("6. Record Payment");
            System.out.println("7. Search Bills by Patient");
            System.out.println("8. Search Bills by Status");
            System.out.println("9. View Overdue Bills");
            System.out.println("10. View Outstanding Balances");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");

            choice = readInt("");

            switch (choice) {
                case 1:
                    createBill();
                    break;
                case 2:
                    deleteBill();
                    break;
                case 3:
                    viewBillDetails();
                    break;
                case 4:
                    viewAllBills();
                    break;
                case 5:
                    updateBill();
                    break;
                case 6:
                    recordPayment();
                    break;
                case 7:
                    searchBillsByPatient();
                    break;
                case 8:
                    searchBillsByStatus();
                    break;
                case 9:
                    viewOverdueBills();
                    break;
                case 10:
                    viewOutstandingBalances();
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

    private BigDecimal readDecimal(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a valid number.");
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

    private void createBill() {
        System.out.println("\n--- Create New Bill ---\n");

        // Get patient
        int patientId = readInt("Patient ID: ");
        Patient patient = patientService.getPatientById(patientId);
        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        LocalDate issueDate = readDate("Issue Date (yyyy-MM-dd): ");
        LocalDate dueDate = readDate("Due Date (yyyy-MM-dd): ");
        BigDecimal totalAmount = readDecimal("Total Amount: ");

        System.out.print("Description: ");
        String description = scanner.nextLine();

        Billing bill = new Billing();
        bill.setPatient(patient);
        bill.setIssueDate(issueDate);
        bill.setDueDate(dueDate);
        bill.setTotalAmount(totalAmount);
        bill.setDescription(description);
        bill.setPaymentStatus(PaymentStatus.PENDING);

        billingService.createBill(bill);
        System.out.println("\n✅ Bill created successfully! Bill ID: " + bill.getBillId());
    }

    private void deleteBill() {
        int billId = readInt("\nEnter Bill ID to delete: ");
        billingService.softDeleteBill(billId);
        System.out.println("Bill marked as deleted.");
    }

    private void viewBillDetails() {
        int billId = readInt("\nEnter Bill ID: ");
        Billing bill = billingService.getBillById(billId);

        if (bill != null) {
            System.out.println("\n--- Bill Details ---");
            System.out.println("Bill ID: " + bill.getBillId());
            System.out.println("Patient: " + bill.getPatient().getPatientFirstName() +
                    " " + bill.getPatient().getPatientLastName());
            System.out.println("Issue Date: " + bill.getIssueDate());
            System.out.println("Due Date: " + bill.getDueDate());
            System.out.println("Total Amount: " + bill.getTotalAmount());
            System.out.println("Amount Paid: " + bill.getAmountPaid());
            System.out.println("Balance Due: " + bill.getBalanceDue());
            System.out.println("Status: " + bill.getPaymentStatus());
            System.out.println("Description: " + bill.getDescription());
        } else {
            System.out.println("Bill not found.");
        }
    }

    private void viewAllBills() {
        System.out.println("\n--- All Bills ---");
        List<Billing> bills = billingService.getAllBills();

        if (bills.isEmpty()) {
            System.out.println("No bills found.");
            return;
        }

        for (Billing bill : bills) {
            System.out.printf("[%d] %s | %s | %s/%s | %s%n",
                    bill.getBillId(),
                    bill.getPatient().getPatientFirstName() + " " + bill.getPatient().getPatientLastName(),
                    bill.getTotalAmount(),
                    bill.getAmountPaid(),
                    bill.getBalanceDue(),
                    bill.getPaymentStatus());
        }
    }

    private void updateBill() {
        int billId = readInt("\nEnter Bill ID to update: ");
        Billing bill = billingService.getBillById(billId);

        if (bill == null) {
            System.out.println("Bill not found.");
            return;
        }

        System.out.println("\nLeave blank to keep current value");

        System.out.print("Issue Date [" + bill.getIssueDate() + "]: ");
        String issueInput = scanner.nextLine();
        if (!issueInput.isBlank()) {
            try {
                bill.setIssueDate(LocalDate.parse(issueInput, dateFormatter));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Keeping existing value.");
            }
        }

        System.out.print("Due Date [" + bill.getDueDate() + "]: ");
        String dueInput = scanner.nextLine();
        if (!dueInput.isBlank()) {
            try {
                bill.setDueDate(LocalDate.parse(dueInput, dateFormatter));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Keeping existing value.");
            }
        }

        System.out.print("Total Amount [" + bill.getTotalAmount() + "]: ");
        String amountInput = scanner.nextLine();
        if (!amountInput.isBlank()) {
            try {
                bill.setTotalAmount(new BigDecimal(amountInput));
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Keeping existing value.");
            }
        }

        System.out.print("Description [" + bill.getDescription() + "]: ");
        String description = scanner.nextLine();
        if (!description.isBlank()) bill.setDescription(description);

        billingService.updateBill(bill);
        System.out.println("✅ Bill updated successfully.");
    }

    private void recordPayment() {
        int billId = readInt("\nEnter Bill ID: ");
        Billing bill = billingService.getBillById(billId);

        if (bill == null) {
            System.out.println("Bill not found.");
            return;
        }

        System.out.println("Current Balance Due: " + bill.getBalanceDue());
        BigDecimal amount = readDecimal("Payment Amount: ");

        System.out.print("Payment Method (" + getEnumValues(PaymentMethod.values()) + "): ");
        String methodInput = scanner.nextLine();
        PaymentMethod method;
        try {
            method = PaymentMethod.valueOf(methodInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid payment method. Defaulting to CASH.");
            method = PaymentMethod.CASH;
        }

        billingService.recordPayment(billId, amount, method);
        System.out.println("✅ Payment recorded successfully.");
    }

    private void searchBillsByPatient() {
        System.out.print("\nEnter patient name to search: ");
        String name = scanner.nextLine();
        List<Billing> results = billingService.getBillsByPatientName(name);
        displaySearchResults("Patient", name, results);
    }

    private void searchBillsByStatus() {
        System.out.print("\nEnter status (" + getEnumValues(PaymentStatus.values()) + "): ");
        String statusInput = scanner.nextLine();
        try {
            PaymentStatus status = PaymentStatus.valueOf(statusInput.toUpperCase());
            List<Billing> results = billingService.getBillsByStatus(status);
            displaySearchResults("Status", statusInput, results);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status entered.");
        }
    }

    private void viewOverdueBills() {
        System.out.println("\n--- Overdue Bills ---");
        List<Billing> results = billingService.getOverdueBills();
        displaySearchResults("Overdue", "", results);
    }

    private void viewOutstandingBalances() {
        System.out.println("\n--- Bills with Outstanding Balances ---");
        List<Billing> results = billingService.getBillsWithBalance();
        displaySearchResults("Outstanding Balances", "", results);
    }

    private String getEnumValues(Enum<?>[] values) {
        StringBuilder sb = new StringBuilder();
        for (Enum<?> value : values) {
            sb.append(value.name()).append("/");
        }
        return sb.substring(0, sb.length() - 1);
    }

    private void displaySearchResults(String criteria, String value, List<Billing> results) {
        if (results.isEmpty()) {
            System.out.println("No bills found with " + criteria + ": " + value);
        } else {
            System.out.println("\n--- Search Results (" + results.size() + " found) ---");
            results.forEach(bill -> System.out.printf("%d: %s | %s/%s | %s | Due: %s%n",
                    bill.getBillId(),
                    bill.getPatient().getPatientFirstName() + " " + bill.getPatient().getPatientLastName(),
                    bill.getAmountPaid(),
                    bill.getTotalAmount(),
                    bill.getPaymentStatus(),
                    bill.getDueDate()));
        }
    }
}
