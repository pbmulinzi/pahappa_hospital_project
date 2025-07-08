package com.pahappa.hospital.beans;

import com.pahappa.hospital.enums.RecordType;
import com.pahappa.hospital.models.MedicalRecord;
import com.pahappa.hospital.models.Patient;
import com.pahappa.hospital.models.Doctor;
import com.pahappa.hospital.services.MedicalRecordService;
import com.pahappa.hospital.services.PatientService;
import com.pahappa.hospital.services.DoctorService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Named("medicalRecordBean")
@ViewScoped
public class MedicalRecordBean implements Serializable {

    private MedicalRecord record = new MedicalRecord();
    private List<MedicalRecord> recordList;
    private String diagnosisKeyword;
    private RecordType recordTypeSearch;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long selectedRecordId;

    // For form selections
    private List<RecordType> allRecordTypes;
    private List<Patient> allPatients;
    private List<Doctor> allDoctors;

    // Services
    private MedicalRecordService medicalRecordService;
    private PatientService patientService;
    private DoctorService doctorService;

    @PostConstruct
    public void init() {
        this.medicalRecordService = new MedicalRecordService();
        this.patientService = new PatientService();
        this.doctorService = new DoctorService();

        refreshRecords();
        allRecordTypes = Arrays.asList(RecordType.values());
        allPatients = patientService.getActivePatients();
        allDoctors = doctorService.getAllDoctors();
    }

    public void saveRecord() {
        if (record.getRecordId() == null) {
            medicalRecordService.createMedicalRecord(record);
        } else {
            medicalRecordService.updateMedicalRecord(record);
        }
        record = new MedicalRecord();
        refreshRecords();
    }

    public void editRecord(MedicalRecord rec) {
        this.record = rec;
    }

    public void deleteRecord() {
        if (selectedRecordId != null) {
            medicalRecordService.softDeleteMedicalRecord(selectedRecordId.intValue());
            refreshRecords();
        }
    }

    public void searchByDiagnosis() {
        if (diagnosisKeyword != null && !diagnosisKeyword.isEmpty()) {
            recordList = medicalRecordService.searchRecordsByDiagnosis(diagnosisKeyword);
        } else {
            refreshRecords();
        }
    }

    public void searchByRecordType() {
        if (recordTypeSearch != null) {
            recordList = medicalRecordService.getRecordsByType(recordTypeSearch);
        } else {
            refreshRecords();
        }
    }

    public void searchByDateRange() {
        if (startDate != null && endDate != null) {
            recordList = medicalRecordService.getRecordsByDateRange(startDate, endDate);
        } else {
            refreshRecords();
        }
    }

    private void refreshRecords() {
        recordList = medicalRecordService.getAllActiveMedicalRecords();
    }

    // Getters and Setters
    public MedicalRecord getRecord() { return record; }
    public void setRecord(MedicalRecord record) { this.record = record; }

    public List<MedicalRecord> getRecordList() { return recordList; }
    public void setRecordList(List<MedicalRecord> recordList) { this.recordList = recordList; }

    public String getDiagnosisKeyword() { return diagnosisKeyword; }
    public void setDiagnosisKeyword(String diagnosisKeyword) { this.diagnosisKeyword = diagnosisKeyword; }

    public RecordType getRecordTypeSearch() { return recordTypeSearch; }
    public void setRecordTypeSearch(RecordType recordTypeSearch) { this.recordTypeSearch = recordTypeSearch; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Long getSelectedRecordId() { return selectedRecordId; }
    public void setSelectedRecordId(Long selectedRecordId) { this.selectedRecordId = selectedRecordId; }

    public List<RecordType> getAllRecordTypes() { return allRecordTypes; }
    public List<Patient> getAllPatients() { return allPatients; }
    public List<Doctor> getAllDoctors() { return allDoctors; }
}





















//package com.pahappa.hospital.beans;
//
//import com.pahappa.hospital.enums.RecordType;
//import com.pahappa.hospital.models.Doctor;
//import com.pahappa.hospital.models.MedicalRecord;
//import com.pahappa.hospital.models.Patient;
//import com.pahappa.hospital.services.DoctorService;
//import com.pahappa.hospital.services.MedicalRecordService;
//import com.pahappa.hospital.services.PatientService;
//import jakarta.annotation.PostConstruct;
//import jakarta.faces.view.ViewScoped;
//import jakarta.inject.Inject;
//import jakarta.inject.Named;
//
//import java.io.Serializable;
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//
//@Named
//@ViewScoped
//public class MedicalRecordBean implements Serializable {
//
//    private MedicalRecord record = new MedicalRecord();
//    private List<MedicalRecord> records;
//
//    @Inject
//    private MedicalRecordService recordService;
//
//    @Inject
//    private PatientService patientService;
//
//    @Inject
//    private DoctorService doctorService;
//
//    private List<Patient> patients;
//    private List<Doctor> doctors;
//
//    @PostConstruct
//    public void init() {
//        records = recordService.getAllMedicalRecords();
//        patients = patientService.getAllPatients();
//        doctors = doctorService.getAllDoctors();
//    }
//
//    public void saveRecord() {
//        if (record.getRecordId() == null) {
//            recordService.createMedicalRecord(record);
//        } else {
//            recordService.updateMedicalRecord(record);
//        }
//        record = new MedicalRecord();
//        init();
//    }
//
//    public void editRecord(MedicalRecord r) {
//        this.record = r;
//    }
//
//    public void deleteRecord(int id) {
//        recordService.softDeleteMedicalRecord(id);
//        init();
//    }
//
//    public List<RecordType> getRecordTypes() {
//        return Arrays.asList(RecordType.values());
//    }
//
//    // Getters and Setters
//    public MedicalRecord getRecord() { return record; }
//    public void setRecord(MedicalRecord record) { this.record = record; }
//
//    public List<MedicalRecord> getRecords() { return records; }
//    public void setRecords(List<MedicalRecord> records) { this.records = records; }
//
//    public List<Patient> getPatients() { return patients; }
//    public void setPatients(List<Patient> patients) { this.patients = patients; }
//
//    public List<Doctor> getDoctors() { return doctors; }
//    public void setDoctors(List<Doctor> doctors) { this.doctors = doctors; }
//}
