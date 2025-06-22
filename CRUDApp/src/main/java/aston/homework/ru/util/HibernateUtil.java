package aston.homework.ru.util;

import aston.homework.ru.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final Configuration CONFIGURATION = new Configuration().addAnnotatedClass(User.class);
    private static SessionFactory sessionFactory = null;

    public static Session getSession() {
        try {
            sessionFactory = CONFIGURATION.buildSessionFactory();
            return sessionFactory.getCurrentSession();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ExceptionInInitializerError();
        }
    }

    public static void close() {
        if(sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
