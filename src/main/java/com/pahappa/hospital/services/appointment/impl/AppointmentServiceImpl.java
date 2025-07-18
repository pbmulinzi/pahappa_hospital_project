package com.pahappa.hospital.services.appointment.impl;

import com.pahappa.hospital.daos.AppointmentDao;
import com.pahappa.hospital.enums.AppointmentStatus;
import com.pahappa.hospital.models.Appointment;
import com.pahappa.hospital.models.Doctor;
import com.pahappa.hospital.models.Patient;
import com.pahappa.hospital.services.appointment.AppointmentService;
import com.pahappa.hospital.services.doctor.impl.DoctorServiceImpl;
import com.pahappa.hospital.services.patient.impl.PatientServiceImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class AppointmentServiceImpl implements AppointmentService, Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private AppointmentDao appointmentDao;

    @Inject
    private PatientServiceImpl patientServiceImpl;

    @Inject
    private DoctorServiceImpl doctorServiceImpl;

    // Create new appointment
    public void createAppointment(Appointment appointment) {
        if (hasConflictingAppointment(appointment.getDoctor(), appointment.getAppointmentDate())) {
            throw new IllegalStateException("Doctor has a conflicting appointment at this time");
        }
        appointmentDao.createAppointment(appointment);
    }

    // Update existing appointment
    public void updateAppointment(Appointment appointment) {
        appointmentDao.updateAppointment(appointment);
    }

    // Cancel appointment (soft delete)
    public void cancelAppointment(Long appointmentId) {
        appointmentDao.softDeleteAppointment(appointmentId);
    }

    // Get appointment by ID (excluding deleted)
    public Appointment getAppointmentById(Long appointmentId) {
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
                patientServiceImpl.searchPatientsByName(name).getFirst()
        );
    }

    // Search appointments by doctor name
    public List<Appointment> getAppointmentsByDoctorName(String name) {
        return appointmentDao.getAppointmentsByDoctor(
                doctorServiceImpl.searchDoctorsByName(name).getFirst()
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
    public List<Appointment> getAppointmentsByDateRange(LocalDate start, LocalDate end) {
        return appointmentDao.getAppointmentsByDateRange(start, end);
    }

    // Restore cancelled appointment
    public void restoreAppointment(int appointmentId) {
        appointmentDao.restoreAppointment(appointmentId);
    }

    // Check for scheduling conflicts
    public boolean hasConflictingAppointment(Doctor doctor, LocalDate date) {
        return appointmentDao.hasConflictingAppointment(doctor, date);
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
    public void updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        Appointment appointment = getAppointmentById(appointmentId);
        if (appointment != null) {
            if (appointment.getStatus() == AppointmentStatus.COMPLETED || appointment.getStatus() == AppointmentStatus.CANCELLED) {
                throw new IllegalStateException("Cannot update status of appointment after completion or cancellation");
            }
            appointment.setStatus(status);
            updateAppointment(appointment);
        }
    }
}
