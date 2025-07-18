package com.pahappa.hospital.daos;

import com.pahappa.hospital.enums.AppointmentStatus;
import com.pahappa.hospital.models.Appointment;
import com.pahappa.hospital.models.Doctor;
import com.pahappa.hospital.models.Patient;
import com.pahappa.hospital.utils.HibernateUtil;
<<<<<<< Updated upstream
=======
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
>>>>>>> Stashed changes
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

<<<<<<< Updated upstream
public class AppointmentDao {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
=======
@ApplicationScoped
public class AppointmentDao {

    @Inject
    private SessionFactory sessionFactory;
>>>>>>> Stashed changes

    // Create new appointment
    public void createAppointment(Appointment appointment) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(appointment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Update existing appointment
    public void updateAppointment(Appointment appointment) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(appointment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }





    // Soft delete appointment
    public void softDeleteAppointment(Long appointmentId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Appointment appointment = session.get(Appointment.class, appointmentId);
            if (appointment != null) {
                appointment.setDeleted(true);
                session.merge(appointment);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }




    // Get appointment by ID (excluding deleted)
<<<<<<< Updated upstream
    public Appointment getAppointmentById(int appointmentId) {
=======
    public Appointment getAppointmentById(Long appointmentId) {
>>>>>>> Stashed changes
        try (Session session = sessionFactory.openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.id = :id AND a.deleted = false",
                    Appointment.class
            );
            query.setParameter("id", appointmentId);
            return query.uniqueResult(); // use getSingleResult() if you're on JPA
        }
    }


<<<<<<< Updated upstream
//    public Appointment getAppointmentById(int appointmentId) {
//        try (Session session = sessionFactory.openSession()) {
//            session.enableFilter("deletedAppointmentFilter")
//                    .setParameter("isDeleted", false);
//            return session.get(Appointment.class, appointmentId);
//        }
//    }

=======
>>>>>>> Stashed changes
    // Get all non-deleted appointments
    public List<Appointment> getAllActiveAppointments() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Appointment a WHERE a.deleted = false";
            return session.createQuery(hql, Appointment.class).list();
        }
    }


    // Get all appointments including soft-deleted
    public List<Appointment> getAllAppointments() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM com.pahappa.hospital.models.Appointment", Appointment.class).list();
        }
    }

    // Restore soft-deleted appointment
    public void restoreAppointment(int appointmentId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Appointment appointment = session.get(Appointment.class, appointmentId);
            if (appointment != null && appointment.isDeleted()) {
                appointment.setDeleted(false);
                session.merge(appointment);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Get appointments by patient
    public List<Appointment> getAppointmentsByPatient(Patient patient) {
        try (Session session = sessionFactory.openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.patient = :patient AND a.deleted = false",
                    Appointment.class
            );
            query.setParameter("patient", patient);
            return query.list();
        }
    }



<<<<<<< Updated upstream
//    public List<Appointment> getAppointmentsByPatient(Patient patient) {
//        try (Session session = sessionFactory.openSession()) {
//            session.enableFilter("deletedAppointmentFilter")
//                    .setParameter("isDeleted", false);
//            Query<Appointment> query = session.createQuery(
//                    "FROM com.pahappa.hospital.models.Appointment WHERE patient = :patient",
//                    Appointment.class
//            );
//            query.setParameter("patient", patient);
//            return query.list();
//        }
//    }
=======
>>>>>>> Stashed changes

    // Get appointments by doctor
    public List<Appointment> getAppointmentsByDoctor(Doctor doctor) {
        try (Session session = sessionFactory.openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.doctor = :doctor AND a.deleted = false",
                    Appointment.class
            );
            query.setParameter("doctor", doctor);
            return query.list();
        }
    }


<<<<<<< Updated upstream
//    public List<Appointment> getAppointmentsByDoctor(Doctor doctor) {
//        try (Session session = sessionFactory.openSession()) {
//            session.enableFilter("deletedAppointmentFilter")
//                    .setParameter("isDeleted", false);
//            Query<Appointment> query = session.createQuery(
//                    "FROM com.pahappa.hospital.models.Appointment WHERE doctor = :doctor",
//                    Appointment.class
//            );
//            query.setParameter("doctor", doctor);
//            return query.list();
//        }
//    }

=======
>>>>>>> Stashed changes
    // Get appointments by status
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        try (Session session = sessionFactory.openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.status = :status AND a.deleted = false",
                    Appointment.class
            );
            query.setParameter("status", status);
            return query.list();
        }
    }


<<<<<<< Updated upstream
//    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
//        try (Session session = sessionFactory.openSession()) {
//            session.enableFilter("deletedAppointmentFilter")
//                    .setParameter("isDeleted", false);
//            Query<Appointment> query = session.createQuery(
//                    "FROM com.pahappa.hospital.models.Appointment WHERE status = :status",
//                    Appointment.class
//            );
//            query.setParameter("status", status);
//            return query.list();
//        }
//    }

    // Get appointments by date range
    public List<Appointment> getAppointmentsByDateRange(LocalDateTime start, LocalDateTime end) {
=======
    // Get appointments by date range
    public List<Appointment> getAppointmentsByDateRange(LocalDate start, LocalDate end) {
>>>>>>> Stashed changes
        try (Session session = sessionFactory.openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.appointmentDate BETWEEN :start AND :end AND a.deleted = false",
                    Appointment.class
            );
            query.setParameter("start", start);
            query.setParameter("end", end);
            return query.list();
        }
    }


<<<<<<< Updated upstream
//    public List<Appointment> getAppointmentsByDateRange(LocalDateTime start, LocalDateTime end) {
//        try (Session session = sessionFactory.openSession()) {
//            session.enableFilter("deletedAppointmentFilter")
//                    .setParameter("isDeleted", false);
//            Query<Appointment> query = session.createQuery(
//                    "FROM com.pahappa.hospital.models.Appointment WHERE appointmentDate BETWEEN :start AND :end",
//                    Appointment.class
//            );
//            query.setParameter("start", start);
//            query.setParameter("end", end);
//            return query.list();
//        }
//    }
=======
>>>>>>> Stashed changes

    // Get appointments for a specific date
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        try (Session session = sessionFactory.openSession()) {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.appointmentDate = :date AND a.deleted = false",
                    Appointment.class
            );
            query.setParameter("date", date);
            return query.list();
        }
<<<<<<< Updated upstream
//        LocalDateTime start = date.atStartOfDay();
//        LocalDateTime end = date.plusDays(1).atStartOfDay();
//        return getAppointmentsByDateRange(start, end);
=======
>>>>>>> Stashed changes
    }

    // Check for conflicting appointments
    public boolean hasConflictingAppointment(Doctor doctor, LocalDate date) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(a) FROM Appointment a " +
                    "WHERE a.doctor = :doctor " +
                    "AND a.appointmentDate = :date " +
                    "AND a.status NOT IN (:cancelledStatuses) " +
                    "AND a.deleted = false";

            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("doctor", doctor);
            query.setParameter("date", date);
            query.setParameterList("cancelledStatuses", getCancelledStatuses());

            Long count = query.uniqueResult();
            return count != null && count > 0;
        }
    }


<<<<<<< Updated upstream
//    public boolean hasConflictingAppointment(Doctor doctor, LocalDateTime dateTime) {
//        try (Session session = sessionFactory.openSession()) {
//            session.enableFilter("deleteAppointmentFilter")
//                    .setParameter("isDelete", false);
//
//            String hql = "SELECT COUNT(a) FROM Appointment a " +
//                    "WHERE a.doctor = :doctor " +
//                    "AND a.appointmentDate = :dateTime " +
//                    "AND a.status NOT IN (:cancelledStatuses)";
//
//            Query<Long> query = session.createQuery(hql, Long.class);
//            query.setParameter("doctor", doctor);
//            query.setParameter("dateTime", dateTime);
//            query.setParameterList("cancelledStatuses", getCancelledStatuses());
//
//            Long count = query.uniqueResult();
//            return count != null && count > 0;
//        }
//    }
=======
>>>>>>> Stashed changes

    private List<AppointmentStatus> getCancelledStatuses() {
        return Arrays.asList(
                AppointmentStatus.CANCELLED,
                AppointmentStatus.NO_SHOW
        );
    }
<<<<<<< Updated upstream


//    public boolean hasConflictingAppointment(Doctor doctor, LocalDateTime dateTime) {
//        try (Session session = sessionFactory.openSession()) {
//            session.enableFilter("deletedAppointmentFilter")
//                    .setParameter("isDeleted", false);
//            Query<Long> query = session.createQuery(
//                    "SELECT COUNT(a) FROM com.pahappa.hospital.models.Appointment a " +
//                            "WHERE a.doctor = :doctor " +
//                            "AND a.appointmentDate = :dateTime " +
//                            "AND a.status NOT IN (:cancelledStatus)",
//                    Long.class
//            );
//            query.setParameter("doctor", doctor);
//            query.setParameter("dateTime", dateTime);
//            query.setParameter("cancelledStatus", AppointmentStatus.CANCELLED);
//            return query.uniqueResult() > 0;
//        }
//    }
=======
>>>>>>> Stashed changes
}
