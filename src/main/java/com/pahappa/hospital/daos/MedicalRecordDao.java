package com.pahappa.hospital.daos;

import com.pahappa.hospital.enums.RecordType;
import com.pahappa.hospital.models.MedicalRecord;
import com.pahappa.hospital.models.Patient;
import com.pahappa.hospital.models.Doctor;
import com.pahappa.hospital.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class MedicalRecordDao {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    // Create new medical record
    public void createMedicalRecord(MedicalRecord record) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(record);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Update existing medical record
    public void updateMedicalRecord(MedicalRecord record) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(record);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Soft delete medical record
    public void softDeleteMedicalRecord(int recordId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            MedicalRecord record = session.get(MedicalRecord.class, recordId);
            if (record != null) {
                record.setDeleted(true);
                session.merge(record);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Get medical record by ID (excluding deleted)
    public MedicalRecord getMedicalRecordById(int recordId) {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedRecordFilter")
                    .setParameter("isDeleted", false);
            return session.get(MedicalRecord.class, recordId);
        }
    }

    // Get all non-deleted medical records
    public List<MedicalRecord> getAllActiveMedicalRecords() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "FROM MedicalRecord mr WHERE mr.deleted = false",
                    MedicalRecord.class
            ).list();
        }
    }

    // Get all medical records including soft-deleted
    public List<MedicalRecord> getAllMedicalRecords() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM MedicalRecord", MedicalRecord.class).list();
        }
    }

    // Restore soft-deleted medical record
    public void restoreMedicalRecord(int recordId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            MedicalRecord record = session.get(MedicalRecord.class, recordId);
            if (record != null && record.isDeleted()) {
                record.setDeleted(false);
                session.merge(record);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Get records by patient
    public List<MedicalRecord> getRecordsByPatient(Patient patient) {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedRecordFilter")
                    .setParameter("isDeleted", false);
            Query<MedicalRecord> query = session.createQuery(
                    "FROM MedicalRecord WHERE patient = :patient",
                    MedicalRecord.class
            );
            query.setParameter("patient", patient);
            return query.list();
        }
    }

    // Get records by doctor
    public List<MedicalRecord> getRecordsByDoctor(Doctor doctor) {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedRecordFilter")
                    .setParameter("isDeleted", false);
            Query<MedicalRecord> query = session.createQuery(
                    "FROM MedicalRecord WHERE doctor = :doctor",
                    MedicalRecord.class
            );
            query.setParameter("doctor", doctor);
            return query.list();
        }
    }

    // Get records by record type
    public List<MedicalRecord> getRecordsByType(RecordType recordType) {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedRecordFilter")
                    .setParameter("isDeleted", false);
            Query<MedicalRecord> query = session.createQuery(
                    "FROM com.pahappa.hospital.models.MedicalRecord WHERE recordType = :recordType",
                    MedicalRecord.class
            );
            query.setParameter("recordType", recordType);
            return query.list();
        }
    }

    // Get records by date range
    public List<MedicalRecord> getRecordsByDateRange(LocalDate start, LocalDate end) {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedRecordFilter")
                    .setParameter("isDeleted", false);
            Query<MedicalRecord> query = session.createQuery(
                    "FROM MedicalRecord WHERE recordDate BETWEEN :start AND :end",
                    MedicalRecord.class
            );
            query.setParameter("start", start);
            query.setParameter("end", end);
            return query.list();
        }
    }

    // Search records by diagnosis keyword
    public List<MedicalRecord> searchRecordsByDiagnosis(String keyword) {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedRecordFilter")
                    .setParameter("isDeleted", false);
            Query<MedicalRecord> query = session.createQuery(
                    "FROM MedicalRecord WHERE LOWER(diagnosis) LIKE :keyword",
                    MedicalRecord.class
            );
            query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
            return query.list();
        }
    }

    // Get patient's full medical history
    public List<MedicalRecord> getPatientMedicalHistory(Long patientId) {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedRecordFilter")
                    .setParameter("isDeleted", false);
            Query<MedicalRecord> query = session.createQuery(
            "FROM MedicalRecord WHERE patient.id = :patientId ORDER BY recordDate DESC",
                MedicalRecord.class
            );
            query.setParameter("patientId", patientId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
