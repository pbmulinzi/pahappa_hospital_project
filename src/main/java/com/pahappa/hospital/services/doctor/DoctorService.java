package com.pahappa.hospital.services.doctor;

import com.pahappa.hospital.daos.DoctorDao;
import com.pahappa.hospital.enums.Speciality;
import com.pahappa.hospital.models.Doctor;

import java.io.Serializable;
import java.util.List;

public interface DoctorService {

    public void createDoctor(Doctor doctor);
    public void updateDoctor(Doctor doctor);
    public void softDeleteDoctor(Long doctorId);
    public Doctor getDoctorById(Long doctorId);
    public List<Doctor> getAllDoctors();
    public List<Doctor> getAllActiveDoctors();
    public int getTotalDoctors();
    public List<Doctor> searchDoctorsByName(String name);
    public List<Doctor> findDoctorsBySpeciality(Speciality speciality);

}


