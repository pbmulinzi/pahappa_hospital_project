package com.pahappa.hospital.daos;

import com.pahappa.hospital.models.User;
<<<<<<< Updated upstream
import com.pahappa.hospital.utils.HibernateUtil;
import jakarta.enterprise.context.ApplicationScoped;
=======
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
>>>>>>> Stashed changes
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
<<<<<<< Updated upstream
=======
import java.util.Collections;
>>>>>>> Stashed changes
import java.util.List;

@ApplicationScoped
public class UserDao implements Serializable {
    private static final long serialVersionUID = 1L;

<<<<<<< Updated upstream
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
=======
    @Inject
    private SessionFactory sessionFactory;
>>>>>>> Stashed changes

    public void createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to create user", e);
        }
    }

    public void updateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to update user", e);
        }
    }

    public List<User> getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
<<<<<<< Updated upstream
            return null;
=======
            return Collections.emptyList();
>>>>>>> Stashed changes
        }

        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE LOWER(username) = :username", User.class);
            query.setParameter("username", username.toLowerCase().trim());
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
<<<<<<< Updated upstream
            return null;
=======
            return Collections.emptyList();
>>>>>>> Stashed changes
        }
    }

    public User getUserById(Long id) {
        if (id == null) {
            return null;
        }

        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("FROM User", User.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to delete user", e);
        }
    }
}

















//package com.pahappa.hospital.daos;
//
//import com.pahappa.hospital.models.User;
//import com.pahappa.hospital.utils.HibernateUtil;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.hibernate.query.Query;
//
//import java.util.List;
//
//public class UserDao {
//    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
//
//    public void createUser(User user) {
//        Transaction transaction = null;
//
//        try(Session session = sessionFactory.openSession()){
//            transaction = session.beginTransaction();
//            session.persist(user);
//            transaction.commit();
//        } catch (Exception e) {
//            if (transaction != null) transaction.rollback();
//            e.printStackTrace();
//        }
//    }
//
//    public List<User> getUserByUsername(String username) {
//        try (Session session = sessionFactory.openSession()) {
//            Query<User> query = session.createQuery("FROM User WHERE LOWER(username) = :username", User.class);
//            query.setParameter("username", username.toLowerCase());
//            return query.list();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//}