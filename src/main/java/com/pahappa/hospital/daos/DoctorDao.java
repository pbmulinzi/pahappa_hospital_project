package com.pahappa.hospital.daos;

import com.pahappa.hospital.models.Doctor;
<<<<<<< Updated upstream
import com.pahappa.hospital.utils.HibernateUtil;
=======
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
>>>>>>> Stashed changes
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.List;

<<<<<<< Updated upstream
public class DoctorDao implements Serializable {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
=======

@ApplicationScoped
public class DoctorDao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private SessionFactory sessionFactory;

>>>>>>> Stashed changes

    // Create a new doctor
    public void createDoctor(Doctor doctor) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(doctor);
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
    public void softDeleteDoctor(Long doctorId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Doctor doctor = session.get(Doctor.class, doctorId);
            if (doctor != null) {
                if(doctor.isDeleted()){
                    doctor.setDeleted(false);
                }else {
                    doctor.setDeleted(true);
                }

                session.merge(doctor);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Get a doctor by ID (including soft-deleted)
    public Doctor getDoctorById(Long doctorId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Doctor.class, doctorId);
//            String hql = "FROM Doctor d WHERE d.id = :id AND d.deleted = :isDeleted";
//            return session.createQuery(hql, Doctor.class)
//                    .setParameter("id", doctorId)
//                    .setParameter("isDeleted", false)
//                    .uniqueResult();
        }
    }

    // Get all non-deleted doctors
    public List<Doctor> getAllActiveDoctors() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Doctor d WHERE d.deleted = :isDeleted";
            return session.createQuery(hql, Doctor.class)
                    .setParameter("isDeleted", false)
                    .list();
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
            String hql = "FROM Doctor d WHERE LOWER(CONCAT(d.doctorFirstName, ' ', d.doctorLastName)) LIKE :name " +
                    "AND d.deleted = false";
            return session.createQuery(hql, Doctor.class)
                    .setParameter("name", "%" + name.toLowerCase() + "%")
                    .list();
        }
    }

    // Search doctors by speciality
    public List<Doctor> findDoctorsBySpeciality(Enum<?> speciality) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Doctor d WHERE d.speciality = :speciality " +
                    "AND d.deleted = false";
            return session.createQuery(hql, Doctor.class)
                    .setParameter("speciality", speciality)
                    .list();
        }
    }

    // Check if a phone number exists (not deleted)
    public boolean phoneNumberExists(String phoneNumber) {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(d) FROM Doctor d WHERE d.doctorPhoneNumber = :phone AND d.deleted = false",
                    Long.class
            );
            query.setParameter("phone", phoneNumber);
            return query.uniqueResult() > 0;
        }
    }
}