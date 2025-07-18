package com.pahappa.hospital.daos;

import com.pahappa.hospital.daos.AuditLogDao;
import com.pahappa.hospital.models.AuditLog;
import com.pahappa.hospital.utils.HibernateUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class AuditLogDao {

    @Inject
    private SessionFactory sessionFactory;

    public AuditLog saveAuditLog(AuditLog auditLog) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(auditLog);
            transaction.commit();
            return auditLog;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    public List<AuditLog> getAllAuditLogs() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM AuditLog a ORDER BY a.date DESC";
            return session.createQuery(hql, AuditLog.class).list();
        }
    }

    public List<AuditLog> getAuditLogsByDate(LocalDate date) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM AuditLog a WHERE a.date = :date ORDER BY a.date DESC";
            Query<AuditLog> query = session.createQuery(hql, AuditLog.class);
            query.setParameter("date", date);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<AuditLog> getAuditLogsByAction(String action) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM AuditLog a WHERE a.action = :action ORDER BY a.date DESC";
            Query<AuditLog> query = session.createQuery(hql, AuditLog.class);
            query.setParameter("action", action);
            return query.getResultList();
        }
    }
}