package com.pahappa.hospital.services;

import com.pahappa.hospital.daos.PatientDao;
import com.pahappa.hospital.models.Patient;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PatientService {

    private final PatientDao patientDao = new PatientDao();

    public void createPatient(Patient patient) {
        patientDao.createPatient(patient);
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

    public Patient searchPatientById(String id) {;
        List<Patient> patients = patientDao.searchPatientsByProperty("patientId", id);
        return patients.isEmpty() ? null : patients.get(0);
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
        return patients.stream().noneMatch(patient -> !patient.getPatientId().equals(excludePatientId));  //excluding the current patient id from that uniqueness check
    }

}
