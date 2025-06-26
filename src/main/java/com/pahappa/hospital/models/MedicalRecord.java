package com.pahappa.hospital.models;

import com.pahappa.hospital.enums.RecordType;
import jakarta.persistence.*;
import org.hibernate.annotations.Filter;

import java.time.LocalDate;

@Entity
@Table(name = "medical_records")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private int recordId;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "record_type", nullable = false)
    private RecordType recordType;

    @Column(name = "diagnosis", nullable = false, length = 1000)
    private String diagnosis;

    @Column(name = "treatment", length = 2000)
    private String treatment;

    @Column(name = "prescription", length = 1000)
    private String prescription;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    @Filter(name = "deletedRecordFilter", condition = "deleted = :isDeleted")
    private boolean deleted = false;

    // Constructors
    public MedicalRecord() {}

    public MedicalRecord(Patient patient, Doctor doctor, LocalDate recordDate,
                         RecordType recordType, String diagnosis) {
        this.patient = patient;
        this.doctor = doctor;
        this.recordDate = recordDate;
        this.recordType = recordType;
        this.diagnosis = diagnosis;
    }

    // Getters and setters
    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public RecordType getRecordType() {
        return recordType;
    }

    public void setRecordType(RecordType recordType) {
        this.recordType = recordType;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    // Utility method
    public String getSummary() {
        return diagnosis.length() > 50 ? diagnosis.substring(0, 50) + "..." : diagnosis;
    }

    // toString
    @Override
    public String toString() {
        return "MedicalRecord [ID=" + recordId +
                ", Patient=" + patient.getPatientFirstName() + " " + patient.getPatientLastName() +
                ", Doctor=" + doctor.getDoctorFirstName() + " " + doctor.getDoctorLastName() +
                ", Date=" + recordDate +
                ", Type=" + recordType +
                ", Diagnosis=" + getSummary() + "]";
    }
}
