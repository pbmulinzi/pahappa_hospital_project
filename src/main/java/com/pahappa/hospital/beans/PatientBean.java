package com.pahappa.hospital.beans;

import com.pahappa.hospital.models.Patient;
import com.pahappa.hospital.services.PatientService;
import com.pahappa.hospital.enums.BloodGroup;
import com.pahappa.hospital.models.Patient;
import com.pahappa.hospital.services.auditLog.AuditLogService;
import com.pahappa.hospital.services.doctor.DoctorService;
import com.pahappa.hospital.services.patient.PatientService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;
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

    private List<BloodGroup> bloodGroupList;
    private String searchQuery;
    private Long selectedPatientId;

    private Long selectedDoctorId;

    @Inject
    private PatientService patientService;

    @Inject
    private AuditLogService auditLogService;

    @Inject
    private DoctorService doctorService;


    @PostConstruct
    public void init() {
        this.patient = new Patient();
        this.patientList = patientService.getActivePatients();



        this.bloodGroupList = List.of(BloodGroup.values());

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

        boolean hasError = false;

        if (patient.getPatientFirstName() == null || patient.getPatientFirstName().trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Patient First Name cannot be empty."));
            hasError = true;
        } else {
            patient.setPatientFirstName(patient.getPatientFirstName().trim());
        }

        if (patient.getPatientLastName() == null || patient.getPatientLastName().trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Patient Last Name cannot be empty."));
            hasError = true;
        } else {
            patient.setPatientLastName(patient.getPatientLastName().trim());
        }


        //only check for duplicates when creating a new patient
        if (patient.getPatientPhoneNumber() != null && !patient.getPatientPhoneNumber().isEmpty()) {
            String trimmedPhone = patient.getPatientPhoneNumber().trim();
            boolean phoneExists = patientService.existsByPhoneNumber(trimmedPhone);
            if (phoneExists && (patient.getPatientId() == null || !trimmedPhone.equals(patientService.getPatientById(patient.getPatientId()).getPatientPhoneNumber()))) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Phone number already exists."));
                hasError = true;
            }
        }

        if (patient.getEmail() != null && !patient.getEmail().isEmpty()) {
            String trimmedEmail = patient.getEmail().trim();
            boolean emailExists = patientService.existsByEmail(trimmedEmail);
            if (emailExists && (patient.getPatientId() == null || !trimmedEmail.equals(patientService.getPatientById(patient.getPatientId()).getEmail()))) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email already exists."));
                hasError = true;
            }
        }

        if (patient.getNin() != null && !patient.getNin().isEmpty()) {
            String trimmedNin = patient.getNin().trim();
            boolean ninExists = patientService.existsByNin(trimmedNin);
            if (ninExists && (patient.getPatientId() == null || !trimmedNin.equals(patientService.getPatientById(patient.getPatientId()).getNin()))) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "NIN already exists."));
                hasError = true;
            }
        }

        if (selectedDoctorId != null) {
            var doctor = doctorService.getDoctorById(selectedDoctorId);
            if (doctor != null) {
                patient.setAssignedDoctor(doctor);
            }
        } else {
            patient.setAssignedDoctor(null);
        }


        if (hasError) {
            return; // Stop processing if there are validation errors
        }




        try {
            if (patient.getPatientId() == null) {
                patientService.createPatient(patient);
                auditLogService.logAction("PATIENT_CREATE", "Created patient: " + patient.getPatientFirstName() + " " + patient.getPatientLastName());
                FacesMessage successMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Success", "Patient created successfully");
                FacesContext.getCurrentInstance().addMessage(null, successMessage);
            } else {
                patientService.updatePatient(patient);
                auditLogService.logAction("PATIENT_UPDATE", "Updated patient: " + patient.getPatientFirstName() + " " + patient.getPatientLastName());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Success", "Patient updated successfully"));
            }
            patient = new Patient(); // Reset the patient object after saving
            selectedDoctorId = null; // Reset after save
            refreshList(); // Refresh the patient list
        } catch (Exception e) {
            FacesMessage errorMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Failed to save patient: " + e.getCause());
            FacesContext.getCurrentInstance().addMessage(null, errorMessage);
        }
    }

    public void viewPatient(Patient patient) {
        this.patient = patientService.getPatientById(patient.getPatientId());
    }


    public void editPatient(Patient patient) {
        Patient freshPatient = patientService.getPatientById(patient.getPatientId());
        if (freshPatient != null) {
            this.patient = freshPatient;
        }
    }


        }
    }


            if (freshPatient != null) {
                this.patient = freshPatient;
                if (freshPatient.getAssignedDoctor() != null) {
                    this.selectedDoctorId = freshPatient.getAssignedDoctor().getDoctorId();
                } else {
                    this.selectedDoctorId = null;
                }
            }
        }
    }

    // PatientBean.java
    public LocalDate getToday() {
        return java.time.LocalDate.now();
    }

    public void deletePatient(Long patientId) {
        patientService.softDeletePatient(patientId);
        refreshList();
    }


    public void setPatientForDeletion(Long patientId) {
        this.selectedPatientId = patientId;
    }

    public void confirmDeletePatient() {
        if (selectedPatientId != null) {
            patientService.softDeletePatient(selectedPatientId);
            auditLogService.logAction("PATIENT_DELETE", "Soft deleted patient with ID: " + selectedPatientId);
            refreshList();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Patient deleted"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No patient selected"));
        }
        selectedPatientId = null;
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


    public String getAssignedDoctorName() {
        if (patient != null && patient.getAssignedDoctor() != null && patient.getAssignedDoctor().getDoctorId() != null) {
            var doctor = doctorService.getDoctorById(patient.getAssignedDoctor().getDoctorId());
            if (doctor!=null) {
                return doctor.getFullDoctorName();
            }
        }
        return "";
    }

    private void refreshList() {
        patientList = patientService.getAllPatients();
        patientList.sort(Comparator.comparing(Patient::getPatientId).reversed());
    }

    // Getters and Setters
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Long getSelectedDoctorId() {
        return selectedDoctorId;
    }
    public void setSelectedDoctorId(Long selectedDoctorId) {
        this.selectedDoctorId = selectedDoctorId;
    }



    public List<Patient> getPatientList() {
        return patientList;
    }

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

    public List<BloodGroup> getBloodGroupList() {
        return bloodGroupList;
    }
    public void setBloodGroupList(List<BloodGroup> bloodGroupList) {
        this.bloodGroupList = bloodGroupList;
    }
}


















