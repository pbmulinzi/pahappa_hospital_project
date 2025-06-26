package com.pahappa.hospital.services;

import com.pahappa.hospital.daos.BillingDao;
import com.pahappa.hospital.enums.PaymentMethod;
import com.pahappa.hospital.enums.PaymentStatus;
import com.pahappa.hospital.models.Billing;
import com.pahappa.hospital.models.Patient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BillingService {

    private final BillingDao billingDao = new BillingDao();
    private final PatientService patientService = new PatientService();

    // Create new bill
    public void createBill(Billing bill) {
        billingDao.createBill(bill);
    }

    // Update existing bill
    public void updateBill(Billing bill) {
        billingDao.updateBill(bill);
    }

    // Soft delete bill
    public void softDeleteBill(int billId) {
        billingDao.softDeleteBill(billId);
    }

    // Get bill by ID (excluding deleted)
    public Billing getBillById(int billId) {
        return billingDao.getBillById(billId);
    }

    // Get all bills including soft-deleted
    public List<Billing> getAllBills() {
        return billingDao.getAllBills();
    }

    // Get only active bills
    public List<Billing> getActiveBills() {
        return billingDao.getAllActiveBills();
    }

    // Get bills by patient
    public List<Billing> getBillsByPatient(Patient patient) {
        return billingDao.getBillsByPatient(patient);
    }

    // Get bills by patient name
    public List<Billing> getBillsByPatientName(String name) {
        List<Patient> patients = patientService.searchPatientsByName(name);
        if (patients.isEmpty()) return List.of();
        return billingDao.getBillsByPatient(patients.get(0));
    }

    // Get bills by payment status
    public List<Billing> getBillsByStatus(PaymentStatus status) {
        return billingDao.getBillsByStatus(status);
    }

    // Get bills by payment method
    public List<Billing> getBillsByPaymentMethod(PaymentMethod method) {
        return billingDao.getBillsByPaymentMethod(method);
    }

    // Get bills by date range
    public List<Billing> getBillsByDateRange(LocalDate start, LocalDate end) {
        return billingDao.getBillsByDateRange(start, end);
    }

    // Get bills for a specific date
    public List<Billing> getBillsByDate(LocalDate date) {
        return billingDao.getBillsByDateRange(date, date);
    }

    // Get overdue bills
    public List<Billing> getOverdueBills() {
        return billingDao.getOverdueBills();
    }

    // Get bills with outstanding balance
    public List<Billing> getBillsWithBalance() {
        return billingDao.getBillsWithBalance();
    }

    // Record payment
    public void recordPayment(int billId, BigDecimal amount, PaymentMethod method) {
        billingDao.recordPayment(billId, amount, method);
    }

    // Restore soft-deleted bill
    public void restoreBill(int billId) {
        billingDao.restoreBill(billId);
    }

    // Calculate total outstanding balance for a patient
    public BigDecimal getPatientBalance(Patient patient) {
        List<Billing> bills = billingDao.getBillsByPatient(patient);
        return bills.stream()
                .filter(bill -> !bill.isDeleted())
                .map(Billing::getBalanceDue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Generate financial report
    public FinancialReport generateFinancialReport(LocalDate start, LocalDate end) {
        List<Billing> bills = billingDao.getBillsByDateRange(start, end);
        BigDecimal totalRevenue = bills.stream()
                .map(Billing::getAmountPaid)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal outstanding = bills.stream()
                .map(Billing::getBalanceDue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new FinancialReport(totalRevenue, outstanding, bills);
    }

    // Nested class for financial reports
    public static class FinancialReport {
        private final BigDecimal totalRevenue;
        private final BigDecimal outstandingBalance;
        private final List<Billing> bills;

        public FinancialReport(BigDecimal totalRevenue, BigDecimal outstandingBalance, List<Billing> bills) {
            this.totalRevenue = totalRevenue;
            this.outstandingBalance = outstandingBalance;
            this.bills = bills;
        }

        // Getters
        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public BigDecimal getOutstandingBalance() { return outstandingBalance; }
        public List<Billing> getBills() { return bills; }
    }
}