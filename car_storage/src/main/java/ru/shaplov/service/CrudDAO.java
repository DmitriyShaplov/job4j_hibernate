package ru.shaplov.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.shaplov.models.EngineANN;
import ru.shaplov.models.IEntity;

import java.util.function.Function;

/**
 * @author shaplov
 * @since 08.07.2019
 */
public class CrudDAO {

    private static final CrudDAO INSTANCE = new CrudDAO();

    private final SessionFactory factory;

    private CrudDAO() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw e;
        }
    }

    public static CrudDAO getInstance() {
        return INSTANCE;
    }

    private <T> T tx(final Function<Session, T> command) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            T result = command.apply(session);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }

    public IEntity add(IEntity entity) {
        return tx(session -> {
           session.save(entity);
           return entity;
        });
    }

    public IEntity get(IEntity entity) {
        return tx(session -> session.get(entity.getClass(), entity.getId()));
    }

    public IEntity update(IEntity entity) {
        return tx(session -> {
            session.update(entity);
            return entity;
        });
    }

    public void delete(IEntity entity) {
        tx(session -> {
            session.delete(entity);
            return null;
        });
    }

    public static void main(String[] args) {
        CrudDAO dao = new CrudDAO();
        EngineANN engineANN = new EngineANN();
        engineANN.setName("engineeee");
        engineANN = (EngineANN) dao.add(engineANN);
        Object object = dao.get(engineANN);
        System.out.println("test");
    }
}
