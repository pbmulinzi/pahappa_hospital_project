package com.pahappa.hospital.beans;

import com.pahappa.hospital.enums.AppointmentStatus;
import com.pahappa.hospital.models.Appointment;
import com.pahappa.hospital.models.Doctor;
import com.pahappa.hospital.models.Patient;
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
>>>>>>> Stashed changes
>>>>>>> Stashed changes
import com.pahappa.hospital.services.AppointmentService;
import com.pahappa.hospital.services.DoctorService;
import com.pahappa.hospital.services.PatientService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
=======
=======
import com.pahappa.hospital.services.appointment.impl.AppointmentServiceImpl;
import com.pahappa.hospital.services.auditLog.AuditLogService;
import com.pahappa.hospital.services.doctor.impl.DoctorServiceImpl;
import com.pahappa.hospital.services.patient.impl.PatientServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
>>>>>>> Stashed changes
>>>>>>> Stashed changes
>>>>>>> Stashed changes
import jakarta.inject.Named;

import java.io.Serializable;
import java.time.LocalDate;
<<<<<<< Updated upstream
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
=======
<<<<<<< Updated upstream
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
=======
<<<<<<< Updated upstream
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
=======
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
>>>>>>> Stashed changes
>>>>>>> Stashed changes
>>>>>>> Stashed changes
import java.util.List;

@Named("appointmentBean")
@ViewScoped
public class AppointmentBean implements Serializable {

    private Appointment appointment = new Appointment();
    private List<Appointment> appointmentList;
    private List<Doctor> doctorList;
    private List<Patient> patientList;

<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
>>>>>>> Stashed changes
>>>>>>> Stashed changes
    // Search and filter properties
    private String searchQuery;
    private AppointmentStatus searchStatus;
    private LocalDate searchDate;
    private Long selectedDoctorId;
    private Long selectedPatientId;
    private Long selectedAppointmentId;
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
>>>>>>> Stashed changes
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
=======
=======
    private List<AppointmentStatus> allAppointmentStatuses;


    // Services
    @Inject
    private AppointmentServiceImpl appointmentServiceImpl;

    @Inject
    private DoctorServiceImpl doctorServiceImpl;

    @Inject
    private PatientServiceImpl patientServiceImpl;

    @Inject
    private AuditLogService auditLogService;

    @PostConstruct
    public void init() {
        appointmentList = appointmentServiceImpl.getActiveAppointments();
        doctorList = doctorServiceImpl.getAllDoctors();
        patientList = patientServiceImpl.getAllPatients();
    }

    // CRUD Operations


    public void saveAppointment() {
        System.out.println("DEBUG: saveAppointment called. Appointment ID: " + appointment.getAppointmentId());

        if (selectedDoctorId != null) {
            Doctor doctor = doctorServiceImpl.getDoctorById(selectedDoctorId);
            appointment.setDoctor(doctor);
        }

        try {
            if (appointment.getAppointmentId() == null) {
                appointmentServiceImpl.createAppointment(appointment);
                auditLogService.logAction("APPOINTMENT_CREATE", "Created appointment for patient: " + appointment.getPatient().getPatientFirstName() + " " + appointment.getPatient().getPatientLastName());
            } else {
                System.out.println("DEBUG: Updating appointment - " + appointment);
                appointmentServiceImpl.updateAppointment(appointment);
                auditLogService.logAction("APPOINTMENT_UPDATE", "Updated appointment for patient: " + appointment.getPatient().getPatientFirstName() + " " + appointment.getPatient().getPatientLastName());
>>>>>>> Stashed changes
>>>>>>> Stashed changes
>>>>>>> Stashed changes
            }
            appointment = new Appointment();
            init();
        } catch (IllegalStateException e) {
<<<<<<< Updated upstream
            // Handle scheduling conflict error
            // You might want to add FacesMessage here for user feedback
=======
<<<<<<< Updated upstream
            // Handle scheduling conflict error
            // You might want to add FacesMessage here for user feedback
=======
<<<<<<< Updated upstream
            // Handle scheduling conflict error
            // You might want to add FacesMessage here for user feedback
=======
>>>>>>> Stashed changes
>>>>>>> Stashed changes
>>>>>>> Stashed changes
            System.err.println("Scheduling conflict: " + e.getMessage());
        }
    }

<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
>>>>>>> Stashed changes
>>>>>>> Stashed changes
    public void editAppointment(Appointment apt) {
        this.appointment = apt;
    }

    public void deleteAppointment(Long id) {
        appointmentService.cancelAppointment(id);
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
=======
=======

    public void prepareNewAppointment() {
        this.appointment = new Appointment();
    }

    public void editAppointment(Long appointmentId) {
        this.appointment = appointmentServiceImpl.getAppointmentById(appointmentId);
    }

    public void deleteAppointment(Long id) {
        appointmentServiceImpl.cancelAppointment(id);
>>>>>>> Stashed changes
>>>>>>> Stashed changes
>>>>>>> Stashed changes
        init();
    }

    public void restoreAppointment() {
        if (selectedAppointmentId != null) {
<<<<<<< Updated upstream
            appointmentService.restoreAppointment(selectedAppointmentId.intValue());
=======
<<<<<<< Updated upstream
            appointmentService.restoreAppointment(selectedAppointmentId.intValue());
=======
<<<<<<< Updated upstream
            appointmentService.restoreAppointment(selectedAppointmentId.intValue());
=======
            appointmentServiceImpl.restoreAppointment(selectedAppointmentId.intValue());
            init();
        }
    }

    public LocalDate getToday() {
        return LocalDate.now();
    }

    public void searchAppointments() {
        filterQuery();
    }

    public void filterQuery() {
        if (selectedPatientId != null) {
            if (selectedDoctorId != null) selectedDoctorId = null;
            if (searchStatus != null) searchStatus = null;
            if (searchDate != null) searchDate = null;
            if (searchQuery != null) searchQuery = null;

            Patient patient = patientServiceImpl.getPatientById(selectedPatientId);
            if (patient != null) {
                appointmentList = appointmentServiceImpl.getAppointmentsByPatient(patient);
            }
        } else if (selectedDoctorId != null) {
            if (selectedPatientId != null) selectedPatientId = null;
            if (searchStatus != null) searchStatus = null;
            if (searchDate != null) searchDate = null;
            if (searchQuery != null) searchQuery = null;

            Doctor doctor = doctorServiceImpl.getDoctorById(selectedDoctorId);
            if (doctor != null) {
                appointmentList = appointmentServiceImpl.getAppointmentsByDoctor(doctor);
            }
        } else if (searchStatus != null) {
            if (selectedPatientId != null) selectedPatientId = null;
            if (selectedDoctorId != null) selectedDoctorId = null;
            if (searchDate != null) searchDate = null;
            if (searchQuery != null) searchQuery = null;

            appointmentList = appointmentServiceImpl.getAppointmentsByStatus(searchStatus);
        } else if (searchDate != null) {
            if (selectedPatientId != null) selectedPatientId = null;
            if (selectedDoctorId != null) selectedDoctorId = null;
            if (searchStatus != null) searchStatus = null;
            if (searchQuery != null) searchQuery = null;

            appointmentList = appointmentServiceImpl.getAppointmentsByDate(searchDate);
        } else if (searchQuery != null && !searchQuery.isEmpty()) {
            if (selectedPatientId != null) selectedPatientId = null;
            if (selectedDoctorId != null) selectedDoctorId = null;
            if (searchStatus != null) searchStatus = null;
            if (searchDate != null) searchDate = null;

        } else {
>>>>>>> Stashed changes
>>>>>>> Stashed changes
>>>>>>> Stashed changes
            init();
        }
    }

    // Search and Filter Methods
    public void searchAppointmentsByStatus() {
        if (searchStatus != null) {
<<<<<<< Updated upstream
            appointmentList = appointmentService.getAppointmentsByStatus(searchStatus);
=======
<<<<<<< Updated upstream
            appointmentList = appointmentService.getAppointmentsByStatus(searchStatus);
=======
<<<<<<< Updated upstream
            appointmentList = appointmentService.getAppointmentsByStatus(searchStatus);
=======
            appointmentList = appointmentServiceImpl.getAppointmentsByStatus(searchStatus);
>>>>>>> Stashed changes
>>>>>>> Stashed changes
>>>>>>> Stashed changes
        } else {
            init();
        }
    }

<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
>>>>>>> Stashed changes
>>>>>>> Stashed changes
    public void searchAppointmentsByDate() {
        if (searchDate != null) {
            appointmentList = appointmentService.getAppointmentsByDate(searchDate);
        } else {
            appointmentList = appointmentService.getActiveAppointments();
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
=======
=======
    public void cancelAppointment(Long appointmentId) {
        Appointment appt = appointmentServiceImpl.getAppointmentById(appointmentId);
        if (appt != null) {
            appt.setStatus(AppointmentStatus.CANCELLED);
            appointmentServiceImpl.updateAppointment(appt);
            auditLogService.logAction("APPOINTMENT_CANCEL", "Cancelled appointment for patient: "
                    + appt.getPatient().getPatientFirstName() + " "
                    + appt.getPatient().getPatientLastName());
            init();
        }
    }


    public void searchAppointmentsByDate() {
        if (searchDate != null) {
            appointmentList = appointmentServiceImpl.getAppointmentsByDate(searchDate);
        } else {
            appointmentList = appointmentServiceImpl.getActiveAppointments();
>>>>>>> Stashed changes
>>>>>>> Stashed changes
>>>>>>> Stashed changes

//            init();
        }
    }

    public void searchAppointmentsByDoctor() {
        if (selectedDoctorId != null) {
<<<<<<< Updated upstream
            Doctor doctor = doctorService.getDoctorById(selectedDoctorId.longValue());
            if (doctor != null) {
                appointmentList = appointmentService.getAppointmentsByDoctor(doctor);
=======
<<<<<<< Updated upstream
            Doctor doctor = doctorService.getDoctorById(selectedDoctorId.longValue());
            if (doctor != null) {
                appointmentList = appointmentService.getAppointmentsByDoctor(doctor);
=======
<<<<<<< Updated upstream
            Doctor doctor = doctorService.getDoctorById(selectedDoctorId.longValue());
            if (doctor != null) {
                appointmentList = appointmentService.getAppointmentsByDoctor(doctor);
=======
            Doctor doctor = doctorServiceImpl.getDoctorById(selectedDoctorId);
            if (doctor != null) {
                appointmentList = appointmentServiceImpl.getAppointmentsByDoctor(doctor);
>>>>>>> Stashed changes
>>>>>>> Stashed changes
>>>>>>> Stashed changes
            }
        } else {
            init();
        }
    }

    public void searchAppointmentsByPatient() {
        if (selectedPatientId != null) {
<<<<<<< Updated upstream
            Patient patient = patientService.getPatientById(selectedPatientId);
            if (patient != null) {
                appointmentList = appointmentService.getAppointmentsByPatient(patient);
=======
<<<<<<< Updated upstream
            Patient patient = patientService.getPatientById(selectedPatientId);
            if (patient != null) {
                appointmentList = appointmentService.getAppointmentsByPatient(patient);
=======
<<<<<<< Updated upstream
            Patient patient = patientService.getPatientById(selectedPatientId);
            if (patient != null) {
                appointmentList = appointmentService.getAppointmentsByPatient(patient);
=======
            Patient patient = patientServiceImpl.getPatientById(selectedPatientId);
            if (patient != null) {
                appointmentList = appointmentServiceImpl.getAppointmentsByPatient(patient);
>>>>>>> Stashed changes
>>>>>>> Stashed changes
>>>>>>> Stashed changes
            }
        } else {
            init();
        }
    }

    public void getTodaysAppointments() {
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
>>>>>>> Stashed changes
>>>>>>> Stashed changes
        appointmentList = appointmentService.getTodaysAppointments();
    }

    public void getAllAppointments() {
        appointmentList = appointmentService.getAllAppointments();
    }

    public void getActiveAppointments() {
        appointmentList = appointmentService.getActiveAppointments();
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
=======
=======
        appointmentList = appointmentServiceImpl.getTodaysAppointments();
    }

    public void getAllAppointments() {
        appointmentList = appointmentServiceImpl.getAllAppointments();
    }

    public void getActiveAppointments() {
        appointmentList = appointmentServiceImpl.getActiveAppointments();
>>>>>>> Stashed changes
>>>>>>> Stashed changes
>>>>>>> Stashed changes
    }

    // Status update method
    public void updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {
<<<<<<< Updated upstream
        appointmentService.updateAppointmentStatus(appointmentId.intValue(), status);
=======
<<<<<<< Updated upstream
        appointmentService.updateAppointmentStatus(appointmentId.intValue(), status);
=======
<<<<<<< Updated upstream
        appointmentService.updateAppointmentStatus(appointmentId.intValue(), status);
=======
        appointmentServiceImpl.updateAppointmentStatus(appointmentId.longValue(), status);
>>>>>>> Stashed changes
>>>>>>> Stashed changes
>>>>>>> Stashed changes
        init();
    }

    // Utility methods
    public boolean hasConflictingAppointment(Doctor doctor, LocalDate date) {
<<<<<<< Updated upstream
        return appointmentService.hasConflictingAppointment(doctor, date);
=======
<<<<<<< Updated upstream
        return appointmentService.hasConflictingAppointment(doctor, date);
=======
<<<<<<< Updated upstream
        return appointmentService.hasConflictingAppointment(doctor, date);
=======
        return appointmentServiceImpl.hasConflictingAppointment(doctor, date);
>>>>>>> Stashed changes
>>>>>>> Stashed changes
>>>>>>> Stashed changes
    }

    // Getters and Setters
    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public List<Appointment> getAppointmentList() {
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
=======
        appointmentList.sort(Comparator.comparing(Appointment::getAppointmentId).reversed());
>>>>>>> Stashed changes
>>>>>>> Stashed changes
>>>>>>> Stashed changes
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






















<<<<<<< Updated upstream



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
=======
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
>>>>>>> Stashed changes


<<<<<<< Updated upstream
=======
=======
>>>>>>> Stashed changes
>>>>>>> Stashed changes
>>>>>>> Stashed changes
