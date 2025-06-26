package com.pahappa.hospital.services;

import com.pahappa.hospital.daos.AppointmentDao;
import com.pahappa.hospital.enums.AppointmentStatus;
import com.pahappa.hospital.models.Appointment;
import com.pahappa.hospital.models.Doctor;
import com.pahappa.hospital.models.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AppointmentService {

    private final AppointmentDao appointmentDao = new AppointmentDao();
    private final PatientService patientService = new PatientService();
    private final DoctorService doctorService = new DoctorService();

    // Create new appointment
    public void createAppointment(Appointment appointment) {
        if (hasConflictingAppointment(appointment.getDoctor(), appointment.getAppointmentDateTime())) {
            throw new IllegalStateException("Doctor has a conflicting appointment at this time");
        }
        appointmentDao.createAppointment(appointment);
    }

    // Update existing appointment
    public void updateAppointment(Appointment appointment) {
        appointmentDao.updateAppointment(appointment);
    }

    // Cancel appointment (soft delete)
    public void cancelAppointment(int appointmentId) {
        appointmentDao.softDeleteAppointment(appointmentId);
    }

    // Get appointment by ID (excluding deleted)
    public Appointment getAppointmentById(int appointmentId) {
        return appointmentDao.getAppointmentById(appointmentId);
    }

    // Get all appointments including soft-deleted
    public List<Appointment> getAllAppointments() {
        return appointmentDao.getAllAppointments();
    }

    // Get only active appointments
    public List<Appointment> getActiveAppointments() {
        return appointmentDao.getAllActiveAppointments();
    }

    // Search appointments by patient name
    public List<Appointment> getAppointmentsByPatientName(String name) {
        return appointmentDao.getAppointmentsByPatient(
                patientService.searchPatientsByName(name).get(0)
        );
    }

    // Search appointments by doctor name
    public List<Appointment> getAppointmentsByDoctorName(String name) {
        return appointmentDao.getAppointmentsByDoctor(
                doctorService.searchDoctorsByName(name).get(0)
        );
    }

    // Get appointments by status
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentDao.getAppointmentsByStatus(status);
    }

    // Get appointments by specific date
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointmentDao.getAppointmentsByDate(date);
    }

    // Get appointments by date range
    public List<Appointment> getAppointmentsByDateRange(LocalDateTime start, LocalDateTime end) {
        return appointmentDao.getAppointmentsByDateRange(start, end);
    }

    // Restore cancelled appointment
    public void restoreAppointment(int appointmentId) {
        appointmentDao.restoreAppointment(appointmentId);
    }

    // Check for scheduling conflicts
    public boolean hasConflictingAppointment(Doctor doctor, LocalDateTime dateTime) {
        return appointmentDao.hasConflictingAppointment(doctor, dateTime);
    }

    // Get appointments for a specific patient
    public List<Appointment> getAppointmentsByPatient(Patient patient) {
        return appointmentDao.getAppointmentsByPatient(patient);
    }

    // Get appointments for a specific doctor
    public List<Appointment> getAppointmentsByDoctor(Doctor doctor) {
        return appointmentDao.getAppointmentsByDoctor(doctor);
    }

    // Get today's appointments
    public List<Appointment> getTodaysAppointments() {
        return getAppointmentsByDate(LocalDate.now());
    }

    // Update appointment status
    public void updateAppointmentStatus(int appointmentId, AppointmentStatus status) {
        Appointment appointment = getAppointmentById(appointmentId);
        if (appointment != null) {
            appointment.setStatus(status);
            updateAppointment(appointment);
        }
    }
}
