package com.pahappa.hospital.services;

import com.pahappa.hospital.daos.PatientDao;
import com.pahappa.hospital.models.Patient;

import java.util.List;

public class PatientService {

    private final PatientDao patientDao = new PatientDao();

    public void createPatient(Patient patient) {
        patientDao.createPatient(patient);
    }

    public void updatePatient(Patient patient) {
        patientDao.updatePatient(patient);
    }

    public void softDeletePatient(int patientId) {
        patientDao.softDeletePatient(patientId);
    }

    public Patient getPatientById(int patientId) {
        return patientDao.getPatientById(patientId);
    }

    public List<Patient> getAllPatients() {
        return patientDao.getAllPatients();
    }

    public List<Patient> getActivePatients() {
        return patientDao.getAllActivePatients();
    }

    public List<Patient> searchPatientsByName(String name) {
        return patientDao.searchPatientsByProperty("name", name);
    }

    public Patient searchPatientById(String id) {
        return patientDao.searchPatientByProperty("patientId", id);
    }

    public void restorePatient(int patientId) {
        patientDao.restorePatient(patientId);
    }
}
