package com.pahappa.hospital.daos;

import com.pahappa.hospital.models.Patient;
import com.pahappa.hospital.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class PatientDao {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    // Create a new patient
    public void createPatient(Patient patient) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(patient);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Update an existing patient
    public void updatePatient(Patient patient) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(patient);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Soft delete a patient
    public void softDeletePatient(int patientId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Patient patient = session.get(Patient.class, patientId);
            if (patient != null) {
                patient.setDeleted(true);
                session.merge(patient);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Restore a soft-deleted patient
    public void restorePatient(int patientId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Patient patient = session.get(Patient.class, patientId);
            if (patient != null && patient.isDeleted()) {
                patient.setDeleted(false);
                session.merge(patient);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Get patient by ID (excluding deleted)
    public Patient getPatientById(int patientId) {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedPatientFilter")
                    .setParameter("isDeleted", false);
            return session.get(Patient.class, patientId);
        }
    }

    // Get all active patients (non-deleted)
    public List<Patient> getAllActivePatients() {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedPatientFilter")
                    .setParameter("isDeleted", false);
            return session.createQuery("FROM com.pahappa.hospital.models.Patient", Patient.class).list();
        }
    }

    // Get all patients including deleted
    public List<Patient> getAllPatients() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM com.pahappa.hospital.models.Patient", Patient.class).list();
        }
    }

    // Search patients by name (first + last)
    public List<Patient> searchPatientsByProperty(String columnName, String value) {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedPatientFilter")
                    .setParameter("isDeleted", false);

            Query<Patient> query = session.createQuery(
                    "FROM com.pahappa.hospital.models.Patient WHERE LOWER(CONCAT(patientFirstName, ' ', patientLastName)) LIKE :name",
                    Patient.class
            );
            query.setParameter(columnName, "%" + value.toLowerCase() + "%");
            return query.list();
        }
    }
    public Patient searchPatientByProperty(String columnName, String value) {
        {
            try (Session session = sessionFactory.openSession()) {
                session.enableFilter("deletedPatientFilter")
                        .setParameter("isDeleted", false);

                Query<Patient> query = session.createQuery(
                        "FROM com.pahappa.hospital.models.Patient WHERE LOWER(CONCAT(patientFirstName, ' ', patientLastName)) LIKE :name",
                        Patient.class
                );
                query.setParameter(columnName, "%" + value.toLowerCase() + "%");
                return query.uniqueResult();
            }
        }
    }

//    // Check if phone number exists (excluding deleted)
//    public boolean phoneNumberExists(String phoneNumber) {
//        try (Session session = sessionFactory.openSession()) {
//            session.enableFilter("deletedPatientFilter")
//                    .setParameter("isDeleted", false);
//
//            Query<Long> query = session.createQuery(
//                    "SELECT COUNT(p) FROM com.pahappa.hospital.models.Patient p WHERE p.patientPhoneNumber = :phone",
//                    Long.class
//            );
//            query.setParameter("phone", phoneNumber);
//            return query.uniqueResult() > 0;
//        }
//    }
}

