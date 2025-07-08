package com.pahappa.hospital.models;

import com.pahappa.hospital.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.Filter;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "first_name", nullable = false, length = 50)
    private String patientFirstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String patientLastName;

    @Column(name = "phone_number", unique = true, nullable = false, length = 15)
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone format") //validationssss
    private String patientPhoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private boolean deleted = false;

    // Constructors
    public Patient() {}

    public Patient(Long patientId, String patientFirstName, String patientLastName, String patientPhoneNumber, Gender gender) {
        this.patientId = patientId;
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;
        this.patientPhoneNumber = patientPhoneNumber;
        this.gender = gender;
        this.deleted = false;
    }

    // Getters and Setters
    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public String getPatientPhoneNumber() {
        return patientPhoneNumber;
    }

    public void setPatientPhoneNumber(String patientPhoneNumber) {
        this.patientPhoneNumber = patientPhoneNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    // Utility Method
    public String getFullPatientName() {
        return patientFirstName + " " + patientLastName;
    }

    // toString
    @Override
    public String toString() {
        return "Patient [ID=" + patientId +
                ", Name=" + getFullPatientName() +
                ", Phone=" + patientPhoneNumber +
                ", Gender=" + gender + "]";
    }
}

