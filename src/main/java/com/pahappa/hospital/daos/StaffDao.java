package com.pahappa.hospital.daos;

import com.pahappa.hospital.enums.Department;
import com.pahappa.hospital.enums.Role;
import com.pahappa.hospital.enums.Shift;
import com.pahappa.hospital.models.Staff;
import com.pahappa.hospital.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class StaffDao {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    // Create new staff member
    public void createStaff(Staff staff) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(staff);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Update existing staff member
    public void updateStaff(Staff staff) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(staff);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Soft delete staff member
    public void softDeleteStaff(Long staffId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Staff staff = session.get(Staff.class, staffId);
            if (staff != null) {
                staff.setDeleted(true);
                session.merge(staff);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Get staff by ID (excluding deleted)
    public Staff getStaffById(Long staffId) {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedStaffFilter")
                    .setParameter("isDeleted", false);
            return session.get(Staff.class, staffId);
        }
    }

    // Get all active staff members
    public List<Staff> getAllActiveStaff() {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedStaffFilter")
                    .setParameter("isDeleted", false);
            return session.createQuery("FROM com.pahappa.hospital.models.Staff", Staff.class).list();
        }
    }

    // Get all staff including soft-deleted
    public List<Staff> getAllStaff() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM com.pahappa.hospital.models.Staff", Staff.class).list();
        }
    }

    // Restore a soft-deleted staff member
    public void restoreStaff(Long staffId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Staff staff = session.get(Staff.class, staffId);

            if (staff != null && staff.isDeleted()) {
                staff.setDeleted(false);
                session.merge(staff);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Failed to restore staff", e);
        }
    }

    // Search staff by name
    public List<Staff> searchStaffByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedStaffFilter")
                    .setParameter("isDeleted", false);
            Query<Staff> query = session.createQuery(
                    "FROM com.pahappa.hospital.models.Staff WHERE LOWER(CONCAT(firstName, ' ', lastName)) LIKE :name",
                    Staff.class
            );
            query.setParameter("name", "%" + name.toLowerCase() + "%");
            return query.list();
        }
    }

    // Search staff by role
    public List<Staff> findStaffByRole(Role role) {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedStaffFilter")
                    .setParameter("isDeleted", false);
            Query<Staff> query = session.createQuery(
                    "FROM com.pahappa.hospital.models.Staff WHERE role = :role",
                    Staff.class
            );
            query.setParameter("role", role);
            return query.list();
        }
    }

    // Search staff by department
    public List<Staff> findStaffByDepartment(Department department) {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedStaffFilter")
                    .setParameter("isDeleted", false);
            Query<Staff> query = session.createQuery(
                    "FROM com.pahappa.hospital.models.Staff WHERE department = :department",
                    Staff.class
            );
            query.setParameter("department", department);
            return query.list();
        }
    }

    // Search staff by shift
    public List<Staff> findStaffByShift(Shift shift) {
        try (Session session = sessionFactory.openSession()) {
            session.enableFilter("deletedStaffFilter")
                    .setParameter("isDeleted", false);
            Query<Staff> query = session.createQuery(
                    "FROM com.pahappa.hospital.models.Staff WHERE shift = :shift",
                    Staff.class
            );
            query.setParameter("shift", shift);
            return query.list();
        }
    }

//    // Check if phone number exists (not deleted)
//    public boolean phoneNumberExists(String phoneNumber) {
//        try (Session session = sessionFactory.openSession()) {
//            session.enableFilter("deletedStaffFilter")
//                    .setParameter("isDeleted", false);
//            Query<Long> query = session.createQuery(
//                    "SELECT COUNT(s) FROM Staff s WHERE phoneNumber = :phone",
//                    Long.class
//            );
//            query.setParameter("phone", phoneNumber);
//            return query.uniqueResult() > 0;
//        }
//    }
}