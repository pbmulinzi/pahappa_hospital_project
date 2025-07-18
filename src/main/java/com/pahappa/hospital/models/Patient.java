package com.pahappa.hospital.models;

import com.pahappa.hospital.enums.Gender;
import com.pahappa.hospital.enums.MaritalStatus;
import com.pahappa.hospital.enums.BloodGroup;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "first_name", length = 50)
    private String patientFirstName;

    @Column(name = "other_names", length = 50)
    private String otherNames;

    @Column(name = "last_name", length = 50)
    private String patientLastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "nin", length = 20)
    private String nin;

    @Email
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "phone_number", length = 15)
    private String patientPhoneNumber;

    @Column(name = "next_of_kin_name", length = 100)
    private String nextOfKinName;

    @Column(name = "next_of_kin_phone_number", length = 15)
    private String nextOfKinPhoneNumber;

    @Column(name = "relationship_with_next_of_kin", length = 50)
    private String relationshipWithNextOfKin;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group")
    private BloodGroup bloodGroup;

    @Column(name = "known_allergies", length = 255)
    private String knownAllergies;

    @Column(name = "existing_conditions", length = 255)
    private String existingConditions;

    @Column(name = "medical_history", length = 1000)
    private String medicalHistory;

    @ManyToOne
    @JoinColumn(name = "assigned_doctor_id")
    private Doctor assignedDoctor;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    @Column(name = "occupation", length = 100)
    private String occupation;

    @Column(nullable = false)
    private boolean deleted = false;


    // Constructors
    public Patient() {}

    public Patient(Long patientId, String patientFirstName, String otherNames, String patientLastName,
                   LocalDate dateOfBirth, Gender gender, String nin, String email, String address,
                   String patientPhoneNumber, String nextOfKinName, String nextOfKinPhoneNumber,
                   String relationshipWithNextOfKin, BloodGroup bloodGroup, String knownAllergies,
                   String existingConditions, String medicalHistory, Doctor assignedDoctor, MaritalStatus maritalStatus,
                   String occupation) {
        this.patientId = patientId;
        this.patientFirstName = patientFirstName;
        this.otherNames = otherNames;
        this.patientLastName = patientLastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.nin = nin;
        this.email = email;
        this.address = address;
        this.patientPhoneNumber = patientPhoneNumber;
        this.nextOfKinName = nextOfKinName;
        this.nextOfKinPhoneNumber = nextOfKinPhoneNumber;
        this.relationshipWithNextOfKin = relationshipWithNextOfKin;
        this.bloodGroup = bloodGroup;
        this.knownAllergies = knownAllergies;
        this.existingConditions = existingConditions;
        this.medicalHistory = medicalHistory;
        this.assignedDoctor = assignedDoctor;
        this.maritalStatus = maritalStatus;
        this.occupation = occupation;
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

    public String getOtherNames() {
        return otherNames;
    }
    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getNin() {
        return nin;
    }
    public void setNin(String nin) {
        this.nin = nin;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getPatientPhoneNumber() {
        return patientPhoneNumber;
    }
    public void setPatientPhoneNumber(String patientPhoneNumber) {
        this.patientPhoneNumber = patientPhoneNumber;
    }

    public String getNextOfKinName() {
        return nextOfKinName;
    }
    public void setNextOfKinName(String nextOfKinName) {
        this.nextOfKinName = nextOfKinName;
    }

    public String getNextOfKinPhoneNumber() {
        return nextOfKinPhoneNumber;
    }
    public void setNextOfKinPhoneNumber(String nextOfKinPhoneNumber) {
        this.nextOfKinPhoneNumber = nextOfKinPhoneNumber;
    }

    public String getRelationshipWithNextOfKin() {
        return relationshipWithNextOfKin;
    }
    public void setRelationshipWithNextOfKin(String relationshipWithNextOfKin) {
        this.relationshipWithNextOfKin = relationshipWithNextOfKin;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }
    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getKnownAllergies() {
        return knownAllergies;
    }
    public void setKnownAllergies(String knownAllergies) {
        this.knownAllergies = knownAllergies;
    }

    public String getExistingConditions() {
        return existingConditions;
    }
    public void setExistingConditions(String existingConditions) {
        this.existingConditions = existingConditions;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }
    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public Doctor getAssignedDoctor() {
        return assignedDoctor;
    }
    public void setAssignedDoctor(Doctor assignedDoctor) {
        this.assignedDoctor = assignedDoctor;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }
    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getOccupation() {
        return occupation;
    }
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    // Utility Method
    public String getFullPatientName() {
        return patientFirstName + " " + patientLastName + " " + otherNames;
    }



    // toString
    @Override
    public String toString() {
        return "Patient [ID=" + patientId +
                ", Name=" + getFullPatientName() +
                ", Gender=" + gender +
                ", Email=" + email +
                ", Phone=" + patientPhoneNumber +
                ", NIN=" + nin +
                ", Address=" + address +
                ", DOB=" + dateOfBirth +
                ", Blood Group=" + bloodGroup +
                ", Known Allergies=" + knownAllergies +
                ", Existing Conditions=" + existingConditions +
                ", Medical History=" + medicalHistory +
                ", Assigned Doctor=" + (assignedDoctor != null ? assignedDoctor.getFullDoctorName() : "None") +
                ", Marital Status=" + maritalStatus +
                ", Occupation=" + occupation +
                ", Next of Kin=" + nextOfKinName +
                ", Relationship with Next of Kin=" + relationshipWithNextOfKin + "]";
    }
}

