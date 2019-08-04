package ru.shaplov.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class stores session factory instance.
 *
 * @author shaplov
 * @since 23.07.2019
 */
public class HibernateUtil {

    private final Logger logger = LogManager.getLogger(HibernateUtil.class);

    private static final HibernateUtil INSTANCE = new HibernateUtil();

    private final SessionFactory sf;

    private HibernateUtil() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sf = new MetadataSources(registry)
                    .buildMetadata().buildSessionFactory();
            logger.info("Session factory initialized.");
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            logger.error("Session factory couldn't initialize.", e);
            throw e;
        }
    }

    public SessionFactory getSessionFactory() {
        return sf;
    }

    public static HibernateUtil getInstance() {
        return INSTANCE;
    }
}
