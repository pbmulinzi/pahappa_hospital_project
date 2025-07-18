package com.pahappa.hospital.beans;

import com.pahappa.hospital.services.*;

import com.pahappa.hospital.services.doctor.DoctorService;
import com.pahappa.hospital.services.patient.PatientService;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;

import java.io.Serializable;


@Named("dashboardBean")
@SessionScoped
public class DashboardBean implements Serializable{

    DoctorService doctorService = new DoctorService();
    PatientService patientService = new PatientService();

    private static final long serialVersionUID = 1L;

    @Inject
    private DoctorService doctorService;

    @Inject
    private PatientService patientService;
>>>>>>> Stashed changes
>>>>>>> Stashed changes

    private int totalDoctors;
    private int totalPatients;

    @PostConstruct
    public void init() {
        //called once when session starts
         totalDoctors = doctorService.getTotalDoctors();
         totalPatients = patientService.getTotalPatients();
    }

    public String navigateToDoctor() {
        return "doctor.xhtml?faces-redirect=true";
    }

    public String navigateToPatient() {
        return "patient.xhtml?faces-redirect=true";
    }

    public String navigateToAppointments() {
        return "appointments.xhtml?faces-redirect=true";
    }

    public String navigateToStaff() {
        return "staff.xhtml?faces-redirect=true";
    }

    public String navigateToMedicalRecord() {
        return "medicalRecord.xhtml?faces-redirect=true";
    }

    public String navigateToBilling() {
        return "billing.xhtml?faces-redirect=true";
    }

    public int getTotalDoctors() {
        return totalDoctors;
    }


    public String navigateWithPatientId(Long patientId) {
        return "patient-profile.xhtml?patientId="+ patientId + "&faces-redirect=true";
    }


    //Getters for total doctors and patients
    public int getTotalDoctors() {
        return totalDoctors;
    }

    public int getTotalPatients() {
        return totalPatients;
    }

}
