package com.pahappa.hospital.daos;

import com.pahappa.hospital.models.Role;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@ApplicationScoped
public class RoleDao {
    @Inject
    private SessionFactory sessionFactory;

    public void save(Role role) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(role);
            transaction.commit();
        }
    }

    public Role findByName(String roleName) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Role r WHERE r.roleName = :roleName", Role.class)
                          .setParameter("roleName", roleName)
                          .uniqueResult();
        }
    }
}
