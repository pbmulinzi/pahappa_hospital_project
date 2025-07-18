package com.pahappa.hospital.services.patient.impl;

import com.pahappa.hospital.daos.PatientDao;
import com.pahappa.hospital.models.Patient;
import com.pahappa.hospital.services.patient.PatientService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class PatientServiceImpl implements PatientService, Serializable {
    @Inject
    private PatientDao patientDao;

    @Override
    public void createPatient(Patient patient) {
        try {
            patientDao.createPatient(patient);
        } catch (Exception e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    public void updatePatient(Patient patient) {
        patientDao.updatePatient(patient);
    }

    public void softDeletePatient(Long patientId) {
        patientDao.softDeletePatient(patientId);
    }

    public Patient getPatientById(Long patientId) {
        return patientDao.getPatientById(patientId);
    }

    public List<Patient> getAllPatients() {
        return patientDao.getAllPatients();
    }

    public List<Patient> getActivePatients() {
        return patientDao.getAllActivePatients();
    }

    public List<Patient> searchPatientsByName(String name) {
        return patientDao.searchPatientsByName(name);
    }


    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        // Implement logic to check if a patient exists with the given phone number
        return !patientDao.searchPatientsByProperty("patientPhoneNumber", phoneNumber).isEmpty();
    }

    @Override
    public boolean existsByEmail(String email) {
        // Implement logic to check if a patient exists with the given email
        return !patientDao.searchPatientsByProperty("email", email).isEmpty();
    }

    @Override
    public boolean existsByNin(String nin) {
        // Implement logic to check if a patient exists with the given NIN
        return !patientDao.searchPatientsByProperty("nin", nin).isEmpty();
    }



    public Patient searchPatientById(String id) {
        List<Patient> patients = patientDao.searchPatientsByProperty("patientId", id);
        return patients.isEmpty() ? null : patients.getFirst();
    }

    public void restorePatient(Long patientId) {
        patientDao.restorePatient(patientId);
    }

    public int getTotalPatients() {
        return patientDao.getAllActivePatients().size();
    }


    //for new patient; with no id to be excluded
    public boolean isPhoneNumberUnique(String phoneNumber) {
        return patientDao.searchPatientsByProperty("patientPhoneNumber", phoneNumber).isEmpty();
    }


    //for existing patients
    public boolean isPhoneNumberUnique(String phoneNumber, Long excludePatientId) {
        List<Patient> patients = patientDao.searchPatientsByProperty("patientPhoneNumber", phoneNumber);
        for (Patient patient : patients) {
            if (!patient.getPatientId().equals(excludePatientId)) {
                return false; // Phone number exists for another patient
            }
        }
        return true; // Phone number is unique or belongs to the same patient
    }
}
