package com.pahappa.hospital.beans;

import com.pahappa.hospital.enums.PaymentMethod;
import com.pahappa.hospital.enums.PaymentStatus;
import com.pahappa.hospital.models.Billing;
import com.pahappa.hospital.models.Patient;
import com.pahappa.hospital.services.BillingService;
import com.pahappa.hospital.services.PatientService;
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

    public List<PaymentStatus> getAllPaymentStatuses() {
        return Arrays.asList(PaymentStatus.values());
    }

    public List<PaymentMethod> getAllPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }


    public void recordPayment() {
        billingService.recordPayment(
                billing.getBillId(),
                billing.getTotalAmount(),
                billing.getPaymentMethod()
        );
        init();
    }
}





















//package com.pahappa.hospital.beans;
//
//import com.pahappa.hospital.enums.PaymentMethod;
//import com.pahappa.hospital.enums.PaymentStatus;
//import com.pahappa.hospital.models.Billing;
//import com.pahappa.hospital.models.Patient;
//import com.pahappa.hospital.services.BillingService;
//import com.pahappa.hospital.services.PatientService;
//import jakarta.annotation.PostConstruct;
//import jakarta.faces.view.ViewScoped;
//import jakarta.inject.Inject;
//import jakarta.inject.Named;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//
//@Named
//@ViewScoped
//public class BillingBean implements Serializable {
//
//    private Billing billing = new Billing();
//    private List<Billing> billings;
//    private List<Patient> patients;
//
//    @Inject
//    private BillingService billingService;
//    @Inject
//    private PatientService patientService;
//
//    @PostConstruct
//    public void init() {
//        billings = billingService.getAllBills();
//        patients = patientService.getAllPatients();
//    }
//
//    public void saveBill() {
//        if (billing.getBillId() == null) {
//            billing.setPaymentStatus(PaymentStatus.PENDING);
//            billingService.createBill(billing);
//        } else {
//            billingService.updateBill(billing);
//        }
//        billing = new Billing();
//        init();
//    }
//
//    public void editBill(Billing b) {
//        this.billing = b;
//    }
//
//    public void deleteBill(Long id) {
//        billingService.softDeleteBill(id);
//        init();
//    }
//
//    public void recordPayment(Long id, BigDecimal amount, PaymentMethod method) {
//        billingService.recordPayment(id, amount, method);
//        init();
//    }
//
//    public List<PaymentMethod> getPaymentMethods() { return Arrays.asList(PaymentMethod.values()); }
//    public List<PaymentStatus> getPaymentStatuses() { return Arrays.asList(PaymentStatus.values()); }
//
//    public Billing getBilling() { return billing; }
//    public void setBilling(Billing billing) { this.billing = billing; }
//
//    public List<Billing> getBillings() { return billings; }
//    public void setBillings(List<Billing> billings) { this.billings = billings; }
//
//    public List<Patient> getPatients() { return patients; }
//    public void setPatients(List<Patient> patients) { this.patients = patients; }
//}
//
