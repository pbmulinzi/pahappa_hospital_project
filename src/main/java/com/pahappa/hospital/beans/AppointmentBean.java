package com.pahappa.hospital.beans;

import com.pahappa.hospital.enums.AppointmentStatus;
import com.pahappa.hospital.models.Appointment;
import com.pahappa.hospital.models.Doctor;
import com.pahappa.hospital.models.Patient;
import com.pahappa.hospital.services.AppointmentService;
import com.pahappa.hospital.services.DoctorService;
import com.pahappa.hospital.services.PatientService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Named("appointmentBean")
@ViewScoped
public class AppointmentBean implements Serializable {

    private Appointment appointment = new Appointment();
    private List<Appointment> appointmentList;
    private List<Doctor> doctorList;
    private List<Patient> patientList;

    // Search and filter properties
    private String searchQuery;
    private AppointmentStatus searchStatus;
    private LocalDate searchDate;
    private Long selectedDoctorId;
    private Long selectedPatientId;
    private Long selectedAppointmentId;

    // Services
    private AppointmentService appointmentService;
    private DoctorService doctorService;
    private PatientService patientService;

    @PostConstruct
    public void init() {
        this.appointmentService = new AppointmentService();
        this.doctorService = new DoctorService();
        this.patientService = new PatientService();

        appointmentList = appointmentService.getActiveAppointments();
        doctorList = doctorService.getAllDoctors();
        patientList = patientService.getAllPatients();
    }

    // CRUD Operations
    public void saveAppointment() {
        try {
            if (appointment.getAppointmentId() == null) {
                appointmentService.createAppointment(appointment);
            } else {
                appointmentService.updateAppointment(appointment);
            }
            appointment = new Appointment();
            init();
        } catch (IllegalStateException e) {
            // Handle scheduling conflict error
            // You might want to add FacesMessage here for user feedback
            System.err.println("Scheduling conflict: " + e.getMessage());
        }
    }

    public void editAppointment(Appointment apt) {
        this.appointment = apt;
    }

    public void deleteAppointment(Long id) {
        appointmentService.cancelAppointment(id);
        init();
    }

    public void restoreAppointment() {
        if (selectedAppointmentId != null) {
            appointmentService.restoreAppointment(selectedAppointmentId.intValue());
            init();
        }
    }

    // Search and Filter Methods
    public void searchAppointmentsByStatus() {
        if (searchStatus != null) {
            appointmentList = appointmentService.getAppointmentsByStatus(searchStatus);
        } else {
            init();
        }
    }

    public void searchAppointmentsByDate() {
        if (searchDate != null) {
            appointmentList = appointmentService.getAppointmentsByDate(searchDate);
        } else {
            appointmentList = appointmentService.getActiveAppointments();

//            init();
        }
    }

    public void searchAppointmentsByDoctor() {
        if (selectedDoctorId != null) {
            Doctor doctor = doctorService.getDoctorById(selectedDoctorId.longValue());
            if (doctor != null) {
                appointmentList = appointmentService.getAppointmentsByDoctor(doctor);
            }
        } else {
            init();
        }
    }

    public void searchAppointmentsByPatient() {
        if (selectedPatientId != null) {
            Patient patient = patientService.getPatientById(selectedPatientId);
            if (patient != null) {
                appointmentList = appointmentService.getAppointmentsByPatient(patient);
            }
        } else {
            init();
        }
    }

    public void getTodaysAppointments() {
        appointmentList = appointmentService.getTodaysAppointments();
    }

    public void getAllAppointments() {
        appointmentList = appointmentService.getAllAppointments();
    }

    public void getActiveAppointments() {
        appointmentList = appointmentService.getActiveAppointments();
    }

    // Status update method
    public void updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        appointmentService.updateAppointmentStatus(appointmentId.intValue(), status);
        init();
    }

    // Utility methods
    public boolean hasConflictingAppointment(Doctor doctor, LocalDate date) {
        return appointmentService.hasConflictingAppointment(doctor, date);
    }

    // Getters and Setters
    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    public List<Doctor> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    public List<Patient> getPatientList() {
        return patientList;
    }

    public void setPatientList(List<Patient> patientList) {
        this.patientList = patientList;
    }


    public String formatDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }



    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public AppointmentStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(AppointmentStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    public LocalDate getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(LocalDate searchDate) {
        this.searchDate = searchDate;
    }

    public Long getSelectedDoctorId() {
        return selectedDoctorId;
    }

    public void setSelectedDoctorId(Long selectedDoctorId) {
        this.selectedDoctorId = selectedDoctorId;
    }

    public Long getSelectedPatientId() {
        return selectedPatientId;
    }

    public void setSelectedPatientId(Long selectedPatientId) {
        this.selectedPatientId = selectedPatientId;
    }

    public Long getSelectedAppointmentId() {
        return selectedAppointmentId;
    }

    public void setSelectedAppointmentId(Long selectedAppointmentId) {
        this.selectedAppointmentId = selectedAppointmentId;
    }

    // Enum helper methods for JSF dropdowns
    public List<AppointmentStatus> getAllAppointmentStatuses() {
        return Arrays.asList(AppointmentStatus.values());
    }
}

























//package com.pahappa.hospital.beans;
//
//import com.pahappa.hospital.enums.AppointmentStatus;
//import com.pahappa.hospital.models.Appointment;
//import com.pahappa.hospital.models.Doctor;
//import com.pahappa.hospital.models.Patient;
//import com.pahappa.hospital.services.AppointmentService;
//import com.pahappa.hospital.services.DoctorService;
//import com.pahappa.hospital.services.PatientService;
//import jakarta.annotation.PostConstruct;
//import jakarta.faces.view.ViewScoped;
//import jakarta.inject.Inject;
//import jakarta.inject.Named;
//
//import java.io.Serializable;
//import java.util.Arrays;
//import java.util.List;
//
//@Named
//@ViewScoped
//public class AppointmentBean implements Serializable {
//
//    private Appointment appointment = new Appointment();
//    private List<Appointment> appointments;
//    private String searchQuery;
//
//    @Inject
//    private AppointmentService appointmentService;
//
//    @Inject
//    private DoctorService doctorService;
//
//    @Inject
//    private PatientService patientService;
//
//    @PostConstruct
//    public void init() {
//        appointments = appointmentService.getAllAppointments();
//    }
//
//    public void saveAppointment() {
//        if (appointment.getAppointmentId() == null) {
//            appointment.setStatus(AppointmentStatus.SCHEDULED);
//            appointmentService.createAppointment(appointment);
//        } else {
//            appointmentService.updateAppointment(appointment);
//        }
//        appointment = new Appointment();
//        init();
//    }
//
//    public void editAppointment(Appointment a) {
//        this.appointment = a;
//    }
//
//    public void cancelAppointment(Long id) {
//        appointmentService.cancelAppointment(id);
//        init();
//    }
//
//    public List<Doctor> getDoctors() {
//        return doctorService.getAllDoctors();
//    }
//
//    public List<Patient> getPatients() {
//        return patientService.getAllPatients();
//    }
//
//    public List<AppointmentStatus> getStatuses() {
//        return Arrays.asList(AppointmentStatus.values());
//    }
//
//    // Getters and Setters
//
//    public Appointment getAppointment() { return appointment; }
//    public void setAppointment(Appointment appointment) { this.appointment = appointment; }
//
//    public List<Appointment> getAppointments() { return appointments; }
//    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }
//
//    public String getSearchQuery() { return searchQuery; }
//    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }
//}
//
