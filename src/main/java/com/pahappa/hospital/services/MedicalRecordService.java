package com.pahappa.hospital.services;

import com.pahappa.hospital.daos.MedicalRecordDao;
import com.pahappa.hospital.enums.RecordType;
import com.pahappa.hospital.models.Doctor;
import com.pahappa.hospital.models.MedicalRecord;
import com.pahappa.hospital.models.Patient;

import java.time.LocalDate;
import java.util.List;

public class MedicalRecordService {

    private final MedicalRecordDao medicalRecordDao = new MedicalRecordDao();

    // Create new medical record
    public void createMedicalRecord(MedicalRecord record) {
        medicalRecordDao.createMedicalRecord(record);
    }

    // Update existing medical record
    public void updateMedicalRecord(MedicalRecord record) {
        medicalRecordDao.updateMedicalRecord(record);
    }

    // Soft delete medical record
    public void softDeleteMedicalRecord(int recordId) {
        medicalRecordDao.softDeleteMedicalRecord(recordId);
    }

    // Restore soft-deleted medical record
    public void restoreMedicalRecord(int recordId) {
        medicalRecordDao.restoreMedicalRecord(recordId);
    }

    // Get non-deleted medical record by ID
    public MedicalRecord getMedicalRecordById(int recordId) {
        return medicalRecordDao.getMedicalRecordById(recordId);
    }

    // Get all medical records (including soft-deleted)
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordDao.getAllMedicalRecords();
    }

    // Get only active (non-deleted) medical records
    public List<MedicalRecord> getAllActiveMedicalRecords() {
        return medicalRecordDao.getAllActiveMedicalRecords();
    }

    // Get records by patient (non-deleted)
    public List<MedicalRecord> getRecordsByPatient(Patient patient) {
        return medicalRecordDao.getRecordsByPatient(patient);
    }

    // Get records by doctor (non-deleted)
    public List<MedicalRecord> getRecordsByDoctor(Doctor doctor) {
        return medicalRecordDao.getRecordsByDoctor(doctor);
    }

    // Get records by type (non-deleted)
    public List<MedicalRecord> getRecordsByType(RecordType recordType) {
        return medicalRecordDao.getRecordsByType(recordType);
    }

    // Get records by date range (non-deleted)
    public List<MedicalRecord> getRecordsByDateRange(LocalDate start, LocalDate end) {
        return medicalRecordDao.getRecordsByDateRange(start, end);
    }

    // Search records by diagnosis keyword (non-deleted)
    public List<MedicalRecord> searchRecordsByDiagnosis(String keyword) {
        return medicalRecordDao.searchRecordsByDiagnosis(keyword);
    }

    // Get patient's full medical history (non-deleted)
    public List<MedicalRecord> getPatientMedicalHistory(int patientId) {
        return medicalRecordDao.getPatientMedicalHistory(patientId);
    }
}
