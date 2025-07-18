package com.pahappa.hospital.services.appointment;

import com.pahappa.hospital.daos.AppointmentDao;
import com.pahappa.hospital.enums.AppointmentStatus;
import com.pahappa.hospital.models.Appointment;
import com.pahappa.hospital.models.Doctor;
import com.pahappa.hospital.models.Patient;
import com.pahappa.hospital.services.doctor.impl.DoctorServiceImpl;
import com.pahappa.hospital.services.patient.impl.PatientServiceImpl;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;



public interface AppointmentService {

    public void createAppointment(Appointment appointment);
    public void updateAppointment(Appointment appointment);
    public void cancelAppointment(Long appointmentId);
    public Appointment getAppointmentById(Long appointmentId);
    public List<Appointment> getAllAppointments();
    public List<Appointment> getActiveAppointments();
    public List<Appointment> getAppointmentsByPatientName(String name);
    public List<Appointment> getAppointmentsByDoctorName(String name);
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status);
    public List<Appointment> getAppointmentsByDate(LocalDate date);
    public List<Appointment> getAppointmentsByDateRange(LocalDate start, LocalDate end);
    public void restoreAppointment(int appointmentId);
    public boolean hasConflictingAppointment(Doctor doctor, LocalDate date);
    public List<Appointment> getAppointmentsByPatient(Patient patient);
    public List<Appointment> getAppointmentsByDoctor(Doctor doctor);
    public List<Appointment> getTodaysAppointments();
    public void updateAppointmentStatus(Long appointmentId, AppointmentStatus status);
}
