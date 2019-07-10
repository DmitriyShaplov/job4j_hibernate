package ru.shaplov.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.criterion.Projections;
import ru.shaplov.models.*;

import javax.persistence.NoResultException;
import java.util.Calendar;
import java.util.List;
import java.util.function.Function;

/**
 * @author shaplov
 * @since 13.07.2019
 */
public class CarDAO implements IDao {

    private final static CarDAO INSTANCE = new CarDAO();

    private final SessionFactory factory;

    /**
     * private Constructor.
     * Constructs SessionFactory object.
     */
    private CarDAO() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            factory = new MetadataSources(registry)
                    .buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw e;
        }
    }

    public static CarDAO getInstance() {
        return INSTANCE;
    }

    private <T> T transaction(Function<Session, T> command) {
        final Session session = factory.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T result = command.apply(session);
            tx.commit();
            return result;
        } catch (final Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Saves entity.
     */
    public IEntity save(IEntity entity) {
        return transaction(session -> {
            session.save(entity);
            return entity;
        });
    }

    /**
     * Updates entity.
     */
    public IEntity update(IEntity entity) {
        return transaction(session -> {
            session.update(entity);
            return entity;
        });
    }

    /**
     * Deletes entity.
     */
    public void delete(IEntity entity) {
        transaction(session -> {
            session.delete(entity);
            return null;
        });
    }

    /**
     * Gets entity.
     */
    public IEntity get(IEntity entity) {
        try (Session session = factory.openSession()) {
            return session.get(entity.getClass(), entity.getId());
        }
    }

    /**
     * Gets List of entities.
     */
    public List<IEntity> getList(String entityName) {
        try (Session session = factory.openSession()) {
            return session.createQuery("from " + entityName + " entity order by entity.id desc", IEntity.class).list();
        }
    }

    /**
     * Get List of body types.
     * @param modelId model id.
     */
    public List<ITitledEntity> getBodyTypes(int modelId) {
        try (Session session = factory.openSession()) {
            return session.createQuery("select distinct body from Unifying u where u.model.id = :modelId", ITitledEntity.class)
                    .setParameter("modelId", modelId)
                    .getResultList();
        }
    }

    /**
     * Gets List of engine types.
     * @param modelId model id.
     * @param bodyId body id.
     */
    public List<ITitledEntity> getEngineTypes(int modelId, int bodyId) {
        try (Session session = factory.openSession()) {
            return session.createQuery("select distinct engine from Unifying u where u.model.id = :modelId"
                    + " and u.body.id = :bodyId", ITitledEntity.class)
                    .setParameter("modelId", modelId)
                    .setParameter("bodyId", bodyId)
                    .getResultList();
        }
    }

    /**
     * Get List of drive types.
     * @param modelId model id.
     * @param bodyId body id.
     * @param engineId engine id.
     */
    public List<ITitledEntity> getDriveTypes(int modelId, int bodyId, int engineId) {
        try (Session session = factory.openSession()) {
            return session.createQuery("select distinct drive from Unifying u where u.model.id = :modelId"
                    + " and u.body.id = :bodyId and u.engine.id = :engineId", ITitledEntity.class)
                    .setParameter("modelId", modelId)
                    .setParameter("bodyId", bodyId)
                    .setParameter("engineId", engineId)
                    .getResultList();
        }
    }

    /**
     * Get List of transmission types.
     * @param modelId model id.
     * @param bodyId body id.
     * @param engineId engine id.
     * @param driveId drive id.
     */
    public List<ITitledEntity> getTransTypes(int modelId, int bodyId, int engineId, int driveId) {
        try (Session session = factory.openSession()) {
            return session.createQuery("select distinct trans from Unifying u where u.model.id = :modelId"
                    + " and u.body.id = :bodyId and u.engine.id = :engineId and u.drive.id = :driveId", ITitledEntity.class)
                    .setParameter("modelId", modelId)
                    .setParameter("bodyId", bodyId)
                    .setParameter("engineId", engineId)
                    .setParameter("driveId", driveId)
                    .getResultList();
        }
    }

    @Override
    public CarUser authUser(String login, String password) {
        try (Session session = factory.openSession()) {
            return session.createQuery("from CarUser u where u.login = :login and u.password = :password", CarUser.class)
                    .setParameter("login", login)
                    .setParameter("password", password).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        try (Session session = factory.openSession()) {
           return session.createQuery("select count(*) from Item", Number.class).getSingleResult().intValue();
        }
    }

    @Override
    public int getLastItemId() {
        try (Session session = factory.openSession()) {
            return session.createQuery("select id From Item order by id desc", Number.class).setMaxResults(1)
                    .getSingleResult().intValue();
        }
    }

    @Override
    public boolean isSold(int id) {
        try (Session session = factory.openSession()) {
            return session.createQuery("select sold From Item where id = :id", Boolean.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }
    }
}
