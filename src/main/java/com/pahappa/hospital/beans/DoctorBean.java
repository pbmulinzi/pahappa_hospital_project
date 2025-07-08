package com.pahappa.hospital.beans;

import com.pahappa.hospital.enums.Shift;
import com.pahappa.hospital.enums.Speciality;
import com.pahappa.hospital.models.Doctor;
import com.pahappa.hospital.services.DoctorService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named("doctorBean")
@ViewScoped
public class DoctorBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Doctor doctor;
    private List<Doctor> doctorList;
    private String searchQuery;
    private Speciality searchSpeciality;
    private Long selectedDoctorId;
    private List<Shift> allShifts;
    private List<Speciality> allSpecialities;

    @Inject
    private DoctorService doctorService;

    @PostConstruct
    public void init() {
        this.doctor = new Doctor();
        this.doctorList = doctorService.getAllDoctors();
        this.allShifts = Arrays.asList(Shift.values());
        this.allSpecialities = Arrays.asList(Speciality.values());
    }

    public void saveDoctor() {
        try {
            if (doctor.getDoctorId() == null) {
                doctorService.createDoctor(doctor);
            } else {
                doctorService.updateDoctor(doctor);
            }
            doctor = new Doctor();
            init();
        } catch (IllegalStateException e) {
            System.err.println("Scheduling conflict: " + e.getMessage());
        }
    }

    public void editDoctor(Doctor doctor) {
        try {
            Doctor freshDoctor = doctorService.getDoctorById(doctor.getDoctorId());
            if (freshDoctor != null) {
                this.doctor = new Doctor();
                this.doctor.setDoctorId(freshDoctor.getDoctorId());
                this.doctor.setDoctorFirstName(freshDoctor.getDoctorFirstName());
                this.doctor.setDoctorLastName(freshDoctor.getDoctorLastName());
                this.doctor.setDoctorPhoneNumber(freshDoctor.getDoctorPhoneNumber());
                this.doctor.setSpeciality(freshDoctor.getSpeciality());
                this.doctor.setShift(freshDoctor.getShift());
                this.doctor.setDeleted(freshDoctor.isDeleted());
            }
        } catch (Exception e) {
            System.err.println("Error fetching doctor: " + e.getMessage());
            this.doctor = doctor; //fallback to the original doctor; incase an error occurs.
        }
    }

    public void deleteDoctor(Long doctorId) {
        doctorService.softDeleteDoctor(doctorId);
        init();
    }

    public void searchDoctorByName() {
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            doctorList = doctorService.searchDoctorsByName(searchQuery);
        } else {
            init();
        }
    }

    public void searchDoctorBySpeciality() {
        if (searchSpeciality != null) {
            doctorList = doctorService.findDoctorsBySpeciality(searchSpeciality);
        } else {
            init();
        }
    }

    public void searchDoctors() {
        if (searchSpeciality != null) {
            doctorList = doctorService.findDoctorsBySpeciality(searchSpeciality);
        } else if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            doctorList = doctorService.searchDoctorsByName(searchQuery);
        } else {
            init();
        }
    }

    // Getters and Setters
    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public List<Doctor> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Speciality getSearchSpeciality() {
        return searchSpeciality;
    }

    public void setSearchSpeciality(Speciality searchSpeciality) {
        this.searchSpeciality = searchSpeciality;
    }

    public Long getSelectedDoctorId() {
        return selectedDoctorId;
    }

    public void setSelectedDoctorId(Long selectedDoctorId) {
        this.selectedDoctorId = selectedDoctorId;
    }

    public List<Speciality> getAllSpecialities() {
        return Arrays.asList(Speciality.values());
    }

    public void setAllSpecialities(List<Speciality> allSpecialities) {
        this.allSpecialities = allSpecialities;
    }

    public List<Shift> getAllShifts() {
        return Arrays.asList(Shift.values());
    }

    public void setShifts(List<Shift> shifts) {
        this.allShifts = shifts;
    }
}








