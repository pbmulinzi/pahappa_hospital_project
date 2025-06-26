package com.pahappa.hospital.models;

import com.pahappa.hospital.enums.AppointmentStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.Filter;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private int appointmentId;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDateTime;

    @Column(name = "reason", nullable = false, length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    @Filter(name = "deletedAppointmentFilter", condition = "deleted = :isDeleted")
    private boolean deleted = false;

    // Constructors
    public Appointment() {}

    public Appointment(Patient patient, Doctor doctor, LocalDateTime appointmentDateTime,
                       String reason, AppointmentStatus status) {
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDateTime = appointmentDateTime;
        this.reason = reason;
        this.status = status;
        this.deleted = false;
    }

    // Getters and Setters
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
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

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
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
    public String getFormattedDateTime() {
        return appointmentDateTime.toString(); // Format as needed
    }

    // toString
    @Override
    public String toString() {
        return "Appointment [ID=" + appointmentId +
                ", Patient=" + patient.getPatientFirstName() + " " + patient.getPatientLastName() +
                ", Doctor=" + doctor.getDoctorFirstName() + " " + doctor.getDoctorLastName() +
                ", Date/Time=" + getFormattedDateTime() +
                ", Status=" + status + "]";
    }
}