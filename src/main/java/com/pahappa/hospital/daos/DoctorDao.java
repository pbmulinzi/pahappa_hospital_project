package com.pahappa.hospital.daos;

import com.pahappa.hospital.models.Doctor;
import com.pahappa.hospital.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class DoctorDao {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    // Create a new doctor
    public void createDoctor(Doctor doctor) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(doctor); // Use persist for new entity
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Update an existing doctor
    public void updateDoctor(Doctor doctor) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(doctor); // Merge for updating existing
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Soft delete a doctor
    public void softDeleteDoctor(int doctorId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Doctor doctor = session.get(Doctor.class, doctorId);
            if (doctor != null) {
                doctor.setDeleted(true);
                session.merge(doctor);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Get doctor by ID (excluding deleted)
    public Doctor getDoctorById(int doctorId) {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedDoctorFilter")
                    .setParameter("isDeleted", false);
            return session.get(Doctor.class, doctorId);
        }
    }

    // Get all non-deleted doctors
    public List<Doctor> getAllActiveDoctors() {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedDoctorFilter")
                    .setParameter("isDeleted", false);
            return session.createQuery("FROM Doctor", Doctor.class).list();
        }
    }

    // Get all doctors including soft-deleted
    public List<Doctor> getAllDoctors() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Doctor", Doctor.class).list();
        }
    }

    // Restore a soft-deleted doctor
    public void restoreDoctor(int doctorId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Doctor doctor = session.get(Doctor.class, doctorId);
            if (doctor != null && doctor.isDeleted()) {
                doctor.setDeleted(false);
                session.merge(doctor);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Search doctors by name
    public List<Doctor> searchDoctorsByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedDoctorFilter")
                    .setParameter("isDeleted", false);
            Query<Doctor> query = session.createQuery(
                    "FROM Doctor WHERE LOWER(CONCAT(doctorFirstName, ' ', doctorLastName)) LIKE :name",
                    Doctor.class
            );
            query.setParameter("name", "%" + name.toLowerCase() + "%");
            return query.list();
        }
    }

    // Search doctors by speciality
    public List<Doctor> findDoctorsBySpeciality(Enum<?> speciality) {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedDoctorFilter")
                    .setParameter("isDeleted", false);
            Query<Doctor> query = session.createQuery(
                    "FROM Doctor WHERE speciality = :speciality",
                    Doctor.class
            );
            query.setParameter("speciality", speciality);
            return query.list();
        }
    }

    // Check if a phone number exists (not deleted)
    public boolean phoneNumberExists(String phoneNumber) {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedDoctorFilter")
                    .setParameter("isDeleted", false);
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(d) FROM Doctor d WHERE doctorPhoneNumber = :phone",
                    Long.class
            );
            query.setParameter("phone", phoneNumber);
            return query.uniqueResult() > 0;
        }
    }
}