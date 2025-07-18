package com.pahappa.hospital.beans;

import com.pahappa.hospital.enums.PaymentMethod;
import com.pahappa.hospital.enums.PaymentStatus;
import com.pahappa.hospital.models.Billing;
import com.pahappa.hospital.models.Patient;
import com.pahappa.hospital.services.BillingService;
import com.pahappa.hospital.services.PatientService;

import com.pahappa.hospital.models.Appointment;
import com.pahappa.hospital.models.Billing;
import com.pahappa.hospital.models.Patient;
import com.pahappa.hospital.services.appointment.AppointmentService;
import com.pahappa.hospital.services.auditLog.AuditLogService;
import com.pahappa.hospital.services.billing.BillingService;
import com.pahappa.hospital.services.patient.PatientService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Named("billingBean")
@ViewScoped
public class BillingBean implements Serializable {

    @Inject
    private BillingService billingService;

    @Inject
    private PatientService patientService;


    @Inject
    private AppointmentService appointmentService;

    @Inject
    private AuditLogService auditLogService;


    private Billing billing = new Billing();
    private List<Billing> billingList;
    private String searchQuery;
    private PaymentStatus searchStatus;
    private Long selectedBillingId;

    private List<PaymentStatus> allPaymentStatuses;
    private List<PaymentMethod> allPaymentMethods;



    @PostConstruct
    public void init() {
        billingList = billingService.getActiveBills();
    }

    public void saveBilling() {
        if (billing.getBillId() == null) {
            billingService.createBill(billing);
        } else {
            billingService.updateBill(billing);
        }
        billing = new Billing();

    private Long selectedPatientId;
    private List<PaymentStatus> allPaymentStatuses;
    private List<PaymentMethod> allPaymentMethods;
    private List<Patient> patientList;
    private List<Appointment> appointmentList;


    private LocalDate searchDateFrom;
    private LocalDate searchDateTo;
    private Billing selectedBill;
    private BigDecimal paymentAmount;
    private PaymentMethod paymentMethod;


    @PostConstruct
    public void init() {
        billingList = billingService.getActiveBills();
        patientList = patientService.getActivePatients();
        appointmentList = appointmentService.getAllAppointments();

    }

    public void saveBilling() {
        if (selectedPatientId != null) {
            // Find the patient by ID
            Patient patient = patientService.getPatientById(selectedPatientId);
            billing.setPatient(patient);
        }

        if (billing.getBillId() == null) {
            billingService.createBill(billing);
            auditLogService.logAction("BILLING_CREATE", "Created billing for patient: " + billing.getPatient().getPatientFirstName() + " " + billing.getPatient().getPatientLastName());
        } else {
            billingService.updateBill(billing);
            auditLogService.logAction("BILLING_UPDATE", "Updated billing for patient: " + billing.getPatient().getPatientFirstName() + " " + billing.getPatient().getPatientLastName());
        }
        billing = new Billing();
        selectedPatientId = null;

        init();
    }

    public void editBilling(Billing bill) {
        this.billing = bill;
    }

    public void deleteBilling(Long id) {
        billingService.softDeleteBill(id);
        init();
    }

    public void restoreBilling() {
        if (selectedBillingId != null) {
            billingService.restoreBill(selectedBillingId);
            init();
        }
    }

    public void searchByPatientName() {
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            billingList = billingService.getBillsByPatientName(searchQuery);
        } else {
            init();
        }
    }

    public void searchByStatus() {
        if (searchStatus != null) {
            billingList = billingService.getBillsByStatus(searchStatus);
        } else {
            init();
        }
    }



    public void searchBills() {
        if (searchDateFrom != null && searchDateTo != null) {
            billingList = billingService.getBillsByDateRange(searchDateFrom, searchDateTo);
        } else if (searchStatus != null) {
            billingList = billingService.getBillsByStatus(searchStatus);
        } else if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            billingList = billingService.getBillsByPatientName(searchQuery);
        } else {
            init();
        }
    }

    public void clearSearch() {
        searchQuery = null;
        searchStatus = null;
        searchDateFrom = null;
        searchDateTo = null;
        init();
    }


>>>>>>> Stashed changes
>>>>>>> Stashed changes
    // Getters and Setters
    public Billing getBilling() { return billing; }
    public void setBilling(Billing billing) { this.billing = billing; }

    public List<Billing> getBillingList() { return billingList; }
    public void setBillingList(List<Billing> billingList) {
        this.billingList = billingList;
    }

    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public PaymentStatus getSearchStatus() { return searchStatus; }
    public void setSearchStatus(PaymentStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    public Long getSelectedBillingId() { return selectedBillingId; }
    public void setSelectedBillingId(Long selectedBillingId) {
        this.selectedBillingId = selectedBillingId;
    }



    public Long getSelectedPatientId() {
        return selectedPatientId;
    }

    public void setSelectedPatientId(Long selectedPatientId) {
        this.selectedPatientId = selectedPatientId;
    }



    public List<PaymentStatus> getAllPaymentStatuses() {
        return Arrays.asList(PaymentStatus.values());
    }

    public List<PaymentMethod> getAllPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    public List<Patient> getPatientList() {
        return patientService.getAllPatients();
    }
    public void setPatientList(List<Patient> patientList) {
        this.patientList = patientList;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentService.getAllAppointments();
    }
    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }


    public List<Appointment> getAppointmentListByPatient(Patient patient) {
        return appointmentService.getAppointmentsByPatient(patient);
    }
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }



    public LocalDate getSearchDateFrom() {
        return searchDateFrom;
    }
    public void setSearchDateFrom(LocalDate searchDateFrom) {
        this.searchDateFrom = searchDateFrom;
    }

    public LocalDate getSearchDateTo() {
        return searchDateTo;
    }
    public void setSearchDateTo(LocalDate searchDateTo) {
        this.searchDateTo = searchDateTo;
    }

    public Billing getSelectedBill() { return selectedBill; }
    public void setSelectedBill(Billing selectedBill) { this.selectedBill = selectedBill; }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }
    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


>>>>>>> Stashed changes
>>>>>>> Stashed changes

    public void recordPayment() {
        billingService.recordPayment(
                billing.getBillId(),
                billing.getTotalAmount(),
                billing.getPaymentMethod()
        );
        init();
    }
}




















