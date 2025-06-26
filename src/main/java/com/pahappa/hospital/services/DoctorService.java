package com.pahappa.hospital.services;

import com.pahappa.hospital.daos.DoctorDao;
import com.pahappa.hospital.enums.Speciality;
import com.pahappa.hospital.models.Doctor;

import java.util.List;

public class DoctorService {

    private final DoctorDao doctorDao = new DoctorDao();

    // Create a new doctor (assumes ID = 0 or unassigned)
    public void createDoctor(Doctor doctor) {
        doctorDao.createDoctor(doctor); // Still uses merge in DAO, but this method is separate for clarity
    }

    // Update an existing doctor (ID must be valid and from DB)
    public void updateDoctor(Doctor doctor) {
        doctorDao.updateDoctor(doctor);
    }

    // Soft delete a doctor by ID
    public void softDeleteDoctor(int doctorId) {
        doctorDao.softDeleteDoctor(doctorId);
    }

    // Get a doctor by ID (ignores soft-deleted doctors)
    public Doctor getDoctorById(int doctorId) {
        return doctorDao.getDoctorById(doctorId);
    }

    // Get all doctors (including soft-deleted ones)
    public List<Doctor> getAllDoctors() {
        return doctorDao.getAllDoctors();
    }

    // Search for doctors by name (only non-deleted ones)
    public List<Doctor> searchDoctorsByName(String name) {
        return doctorDao.searchDoctorsByName(name);
    }

    // Search for doctors by speciality (only non-deleted ones)
    public List<Doctor> findDoctorsBySpeciality(Speciality speciality) {
        return doctorDao.findDoctorsBySpeciality(speciality);
    }
}


