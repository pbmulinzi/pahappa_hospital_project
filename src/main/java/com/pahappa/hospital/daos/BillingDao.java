package com.pahappa.hospital.daos;

import com.pahappa.hospital.enums.PaymentMethod;
import com.pahappa.hospital.enums.PaymentStatus;
import com.pahappa.hospital.models.Billing;
import com.pahappa.hospital.models.Patient;
import com.pahappa.hospital.utils.HibernateUtil;
import jakarta.enterprise.context.ApplicationScoped;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class BillingDao {


    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    // Create new bill
    public void createBill(Billing bill) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(bill);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Update existing bill
    public void updateBill(Billing bill) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(bill);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Soft delete bill
    public void softDeleteBill(Long billId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Billing bill = session.get(Billing.class, billId);
            if (bill != null) {
                bill.setDeleted(true);
                session.merge(bill);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Get bill by ID (excluding deleted)
    public Billing getBillById(Long billId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Billing b WHERE b.id = :billId";
            return session.createQuery(hql, Billing.class)
                    .setParameter("billId", billId)
                    .uniqueResult();
        }
    }
//    public Billing getBillById(Long billId) {
//        try (Session session = sessionFactory.openSession()) {
//            session.enableFilter("deletedBillFilter")
//                    .setParameter("isDeleted", false);
//            return session.get(Billing.class, billId);
//        }
//    }

    // Get all non-deleted bills
    public List<Billing> getAllActiveBills() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM com.pahappa.hospital.models.Billing b WHERE b.deleted = false";
            return session.createQuery(hql, Billing.class).list();
        }
    }
//    public List<Billing> getAllActiveBills() {
//        try (Session session = sessionFactory.openSession()) {
//            session.enableFilter("deletedBillFilter")
//                    .setParameter("isDeleted", false);
//            return session.createQuery("FROM com.pahappa.hospital.models.Billing", Billing.class).list();
//        }
//    }

    // Get all bills including soft-deleted
    public List<Billing> getAllBills() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM com.pahappa.hospital.models.Billing", Billing.class).list();
        }
    }

    // Restore soft-deleted bill
    public void restoreBill(Long billId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Billing bill = session.get(Billing.class, billId);
            if (bill != null && bill.isDeleted()) {
                bill.setDeleted(false);
                session.merge(bill);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Get bills by patient
    public List<Billing> getBillsByPatient(Patient patient) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM com.pahappa.hospital.models.Billing b " +
                    "WHERE b.patient = :patient AND b.deleted = false";
            Query<Billing> query = session.createQuery(hql, Billing.class);
            query.setParameter("patient", patient);
            return query.list();
        }
    }
//    public List<Billing> getBillsByPatient(Patient patient) {
//        try (Session session = sessionFactory.openSession()) {
//            session.enableFilter("deletedBillFilter")
//                    .setParameter("isDeleted", false);
//            Query<Billing> query = session.createQuery(
//                    "FROM com.pahappa.hospital.models.Billing WHERE patient = :patient",
//                    Billing.class
//            );
//            query.setParameter("patient", patient);
//            return query.list();
//        }
//    }


    // Get bills by payment status
    public List<Billing> getBillsByStatus(PaymentStatus status) {
        try (Session session = sessionFactory.openSession()) {
            Query<Billing> query = session.createQuery(
                    "FROM Billing b WHERE b.paymentStatus = :status",
                    Billing.class
            );
            query.setParameter("status", status);
            return query.list();
        }
    }

    // Get bills by payment method
    public List<Billing> getBillsByPaymentMethod(PaymentMethod method) {
        try (Session session = sessionFactory.openSession()) {
            Query<Billing> query = session.createQuery(
                    "FROM Billing b WHERE b.paymentMethod = :method",
                    Billing.class
            );
            query.setParameter("method", method);
            return query.list();
        }
    }

    // Get bills by date range
    public List<Billing> getBillsByDateRange(LocalDate start, LocalDate end) {
        try (Session session = sessionFactory.openSession()) {
            Query<Billing> query = session.createQuery(
                    "FROM Billing b WHERE b.issueDate BETWEEN :start AND :end",
                    Billing.class
            );
            query.setParameter("start", start);
            query.setParameter("end", end);
            return query.list();
        }
    }

    // Get overdue bills (past due date)
    public List<Billing> getOverdueBills() {
        try (Session session = sessionFactory.openSession()) {
            Query<Billing> query = session.createQuery(
                    "FROM Billing b WHERE b.dueDate < CURRENT_DATE AND b.paymentStatus != :paidStatus",
                    Billing.class
            );
            query.setParameter("paidStatus", PaymentStatus.PAID);
            return query.list();
        }
    }

    // Get bills with outstanding balance
    public List<Billing> getBillsWithBalance() {
        try (Session session = sessionFactory.openSession()) {
            Query<Billing> query = session.createQuery(
                    "FROM Billing b WHERE b.amountPaid < b.totalAmount",
                    Billing.class
            );
            return query.list();
        }
    }



//    // Get bills by payment status
//    public List<Billing> getBillsByStatus(PaymentStatus status) {
//        try (Session session = sessionFactory.openSession()) {
//            session.enableFilter("deletedBillFilter")
//                    .setParameter("isDeleted", false);
//            Query<Billing> query = session.createQuery(
//                    "FROM com.pahappa.hospital.models.Billing WHERE paymentStatus = :status",
//                    Billing.class
//            );
//            query.setParameter("status", status);
//            return query.list();
//        }
//    }
//
//    // Get bills by payment method
//    public List<Billing> getBillsByPaymentMethod(PaymentMethod method) {
//        try (Session session = sessionFactory.openSession()) {
//            session.enableFilter("deletedBillFilter")
//                    .setParameter("isDeleted", false);
//            Query<Billing> query = session.createQuery(
//                    "FROM com.pahappa.hospital.models.Billing WHERE paymentMethod = :method",
//                    Billing.class
//            );
//            query.setParameter("method", method);
//            return query.list();
//        }
//    }
//
//    // Get bills by date range
//    public List<Billing> getBillsByDateRange(LocalDate start, LocalDate end) {
//        try (Session session = sessionFactory.openSession()) {
//            session.enableFilter("deletedBillFilter")
//                    .setParameter("isDeleted", false);
//            Query<Billing> query = session.createQuery(
//                    "FROM com.pahappa.hospital.models.Billing WHERE issueDate BETWEEN :start AND :end",
//                    Billing.class
//            );
//            query.setParameter("start", start);
//            query.setParameter("end", end);
//            return query.list();
//        }
//    }
//
//    // Get overdue bills (past due date)
//    public List<Billing> getOverdueBills() {
//        try (Session session = sessionFactory.openSession()) {
//            session.enableFilter("deletedBillFilter")
//                    .setParameter("isDeleted", false);
//            Query<Billing> query = session.createQuery(
//                    "FROM com.pahappa.hospital.models.Billing WHERE dueDate < CURRENT_DATE AND paymentStatus != :paidStatus",
//                    Billing.class
//            );
//            query.setParameter("paidStatus", PaymentStatus.PAID);
//            return query.list();
//        }
//    }
//
//    // Get bills with outstanding balance
//    public List<Billing> getBillsWithBalance() {
//        try (Session session = sessionFactory.openSession()) {
//            session.enableFilter("deletedBillFilter")
//                    .setParameter("isDeleted", false);
//            Query<Billing> query = session.createQuery(
//                    "FROM com.pahappa.hospital.models.Billing WHERE amountPaid < totalAmount",
//                    Billing.class
//            );
//            return query.list();
//        }
//    }

    // Record payment
    public void recordPayment(Long billId, BigDecimal amount, PaymentMethod method) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Billing bill = session.get(Billing.class, billId);
            if (bill != null) {
                BigDecimal newAmountPaid = bill.getAmountPaid().add(amount);
                bill.setAmountPaid(newAmountPaid);
                bill.setPaymentMethod(method);

                // Update payment status
                if (newAmountPaid.compareTo(bill.getTotalAmount()) >= 0) {
                    bill.setPaymentStatus(PaymentStatus.PAID);
                } else if (newAmountPaid.compareTo(BigDecimal.ZERO) > 0) {
                    bill.setPaymentStatus(PaymentStatus.PARTIAL);
                }

                session.merge(bill);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}