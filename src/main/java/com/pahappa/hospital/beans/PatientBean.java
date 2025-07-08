package com.pahappa.hospital.beans;

import com.pahappa.hospital.models.Patient;
import com.pahappa.hospital.services.PatientService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("patientBean")
@ViewScoped
public class PatientBean implements Serializable {

    private Patient patient = new Patient();
    private List<Patient> patientList;
    private String searchQuery;
    private Long selectedPatientId;

    @Inject
    private PatientService patientService;

    @PostConstruct
    public void init() {
        this.patient = new Patient();
        this.patientList = patientService.getActivePatients();

        // Uncomment the next line if you want to instantiate the service manually
        // (not recommended in a managed bean context, as it should be injected)
//        this.patientService = new PatientService();
    }

    public void savePatient() {

        //The system is goin' to first check if the phone number is unique

        //...uniqueness check for new patients; if phone exists
        if (!patientService.isPhoneNumberUnique(patient.getPatientPhoneNumber())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Phone number already exists"));
            return;
        } else {
            //...uniqueness check for existing patients except the current one
            if (!patientService.isPhoneNumberUnique(patient.getPatientPhoneNumber(), patient.getPatientId())) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Phone number already exists"));
                return; // Exit the method if phone exists
            }
        }
        try {
            if (patient.getPatientId() == null) {
                patientService.createPatient(patient);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Patient created successfully"));
            } else {
                patientService.updatePatient(patient);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Patient updated successfully"));
            }
            patient = new Patient();
            refreshList();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save patient"));
        }
    }

    public void editPatient(Patient patient) {
        Patient freshPatient = patientService.getPatientById(patient.getPatientId());
        if (freshPatient != null) {
            this.patient = freshPatient;
        }
    }

    public void deletePatient(Long patientId) {
        patientService.softDeletePatient(patientId);
        refreshList();
    }

    public void restorePatient(Long id) {
        patientService.restorePatient(id);
        refreshList();
    }

    public void searchPatientByName() {
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            patientList = patientService.searchPatientsByName(searchQuery);
        } else {
            refreshList();
        }
    }

    private void refreshList() {
        patientList = patientService.getAllPatients();
    }

    // Getters and Setters
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public List<Patient> getPatientList() { return patientList; }
    public void setPatientList(List<Patient> patientList) {
        this.patientList = patientList;
    }

    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Long getSelectedPatientId() { return selectedPatientId; }
    public void setSelectedPatientId(Long selectedPatientId) {
        this.selectedPatientId = selectedPatientId;
    }
}




















//
//import com.pahappa.hospital.enums.Gender;
//import com.pahappa.hospital.models.Patient;
//import com.pahappa.hospital.services.PatientService;
//import jakarta.annotation.PostConstruct;
//import jakarta.faces.view.ViewScoped;
//import jakarta.inject.Inject;
//import jakarta.inject.Named;
//
//import java.io.Serializable;
//import java.util.Arrays;
//import java.util.List;
//
//@Named
//@ViewScoped
//public class PatientBean implements Serializable {
//
//    private Patient patient = new Patient();
//    private List<Patient> patientList;
//    private String searchQuery;
//    private Long selectedPatientId;
//
//    @Inject
//    private PatientService patientService;
//
//    @PostConstruct
//    public void init() {
//        patientList = patientService.getAllPatients();
//    }
//
//    public void savePatient() {
//        if (patient.getPatientId() == null) {
//            patientService.createPatient(patient);
//        } else {
//            patientService.updatePatient(patient);
//        }
//        patient = new Patient();
//        init();
//    }
//
//    public void editPatient(Patient p) {
//        this.patient = p;
//    }
//
//    public void deletePatient(Long id) {
//        patientService.softDeletePatient(id);
//        init();
//    }
//
//    public void restorePatient() {
//        patientService.restorePatient(selectedPatientId);
//        init();
//    }
//
//    public void searchPatientByName() {
//        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
//            patientList = patientService.searchPatientsByName(searchQuery);
//        } else {
//            init();
//        }
//    }
//
//    public List<Gender> getAllGenders() {
//        return Arrays.asList(Gender.values());
//    }
//
//    // Getters and setters
//    public Patient getPatient() { return patient; }
//    public void setPatient(Patient patient) { this.patient = patient; }
//
//    public List<Patient> getPatientList() { return patientList; }
//    public void setPatientList(List<Patient> patientList) { this.patientList = patientList; }
//
//    public String getSearchQuery() { return searchQuery; }
//    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }
//
//    public Long getSelectedPatientId() { return selectedPatientId; }
//    public void setSelectedPatientId(Long selectedPatientId) { this.selectedPatientId = selectedPatientId; }
//}
//
