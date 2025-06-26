package com.pahappa.hospital.models;

import com.pahappa.hospital.enums.*;
import jakarta.persistence.*;
import org.hibernate.annotations.Filter;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private int doctorId;

    @Column(name = "first_name", nullable = false, length = 50)
    private String doctorFirstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String doctorLastName;

    @Column(name = "phone_number", unique = true, nullable = false, length = 15)
    private String doctorPhoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Shift shift;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Speciality speciality;

    @Column(nullable = false)
    @Filter(name = "deletedDoctorFilter", condition = "deleted = :isDeleted")
    private boolean deleted = false;


    //constructors
    public Doctor() {}
    public Doctor(int doctorId, String doctorFirstName, String doctorLastName, String doctorPhoneNumber, Shift shift, Speciality speciality) {
        this.doctorId = doctorId;
        this.doctorFirstName = doctorFirstName;
        this.doctorLastName = doctorLastName;
        this.doctorPhoneNumber = doctorPhoneNumber;
        this.shift = shift;
        this.speciality = speciality;
        this.deleted = false;
    }

    //getters and setters
    public int getDoctorId() {
        return doctorId;
    }
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorFirstName() {
        return doctorFirstName;
    }
    public void setDoctorFirstName(String doctorFirstName) {
        this.doctorFirstName = doctorFirstName;
    }

    public String getDoctorLastName() {
        return doctorLastName;
    }
    public void setDoctorLastName(String doctorLastName) {
        this.doctorLastName = doctorLastName;
    }

    public String getDoctorPhoneNumber() {
        return doctorPhoneNumber;
    }
    public void setDoctorPhoneNumber(String doctorPhoneNumber) {
        this.doctorPhoneNumber = doctorPhoneNumber;
    }

    public Shift getShift() {
        return shift;
    }
    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Speciality getSpeciality() {
        return speciality;
    }
    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    //utility method
    public String getFullDoctorName() {
        return doctorFirstName + " " + doctorLastName;
    }


    //toString
    @Override
    public String toString() {
        return "Doctor [ID=" + doctorId +
                ", Name=" + getFullDoctorName() +
                ", Phone=" + doctorPhoneNumber +
                ", Shift=" + shift +
                ", Speciality=" + speciality + "]";
    }

}
