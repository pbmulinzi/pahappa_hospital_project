package com.pahappa.hospital.services.patient;

import com.pahappa.hospital.daos.PatientDao;
import com.pahappa.hospital.models.Patient;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

public interface PatientService {

    public void createPatient(Patient patient);
    public void updatePatient(Patient patient);
    public void softDeletePatient(Long patientId);
    public Patient getPatientById(Long patientId);
    public List<Patient> getAllPatients();
    public List<Patient> getActivePatients();
    public List<Patient> searchPatientsByName(String name);
    public Patient searchPatientById(String id);
    public void restorePatient(Long patientId);
    public int getTotalPatients();

    public boolean existsByPhoneNumber(String phoneNumber);

    public boolean existsByEmail(String email);

    public boolean existsByNin(String nin);

    //for new patient; with no id to be excluded
    public boolean isPhoneNumberUnique(String phoneNumber);

    //for existing patients
    public boolean isPhoneNumberUnique(String phoneNumber, Long excludePatientId);

}
