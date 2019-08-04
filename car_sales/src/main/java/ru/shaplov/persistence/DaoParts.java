package ru.shaplov.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.shaplov.models.ITitledEntity;
import ru.shaplov.util.HibernateUtil;

import java.util.List;

/**
 * @author shaplov
 * @since 23.07.2019
 */
@Repository
public class DaoParts implements IDaoParts {

    private final SessionFactory factory = HibernateUtil.getInstance().getSessionFactory();

    private DaoParts() {
    }

    /**
     * Get List of body types.
     * @param modelId model id.
     */
    @Override
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
    @Override
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
    @Override
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
    @Override
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
}
