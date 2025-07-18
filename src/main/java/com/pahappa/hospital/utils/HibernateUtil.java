package com.pahappa.hospital.utils;


import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ApplicationScoped  //making it a CDI managed bean
public class HibernateUtil {

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

    private final SessionFactory sessionFactory;

    public HibernateUtil() {  //constructor initialization
        this.sessionFactory = buildSessionFactory();
    }

    private SessionFactory buildSessionFactory() {
        try {
            // Create registry using hibernate.cfg.xml
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build();

            // Create MetadataSources
            Metadata metadata = new MetadataSources(registry)
                    .getMetadataBuilder()
                    .build();

            // Create SessionFactory
            return metadata.getSessionFactoryBuilder().build();

        } catch (Exception ex) {
            System.err.println("SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    @Produces //to produce the SessionFactory for injection
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }


    @PreDestroy  // Called when application shuts down
    public void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            logger.info("Closing Hibernate SessionFactory");
            sessionFactory.close();
        }
    }
}