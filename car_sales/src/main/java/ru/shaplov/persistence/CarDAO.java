package ru.shaplov.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.shaplov.models.*;
import ru.shaplov.util.HibernateUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

/**
 * @author shaplov
 * @since 13.07.2019
 */
@Repository
public class CarDAO implements IDaoCrud {

    private final SessionFactory factory = HibernateUtil.getInstance().getSessionFactory();

    private CarDAO() {
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
     * Get items for specified date.
     * @param date LocalDate.
     * @return List of Items.
     */
    @Override
    public List<IEntity> getItemsForDate(LocalDate date) {
        try (Session session = factory.openSession()) {
            return session.createQuery("from Item where year(created) = :year and "
                    + "month(created) = :month and day(created) = :day order by id desc", IEntity.class)
                    .setParameter("year", date.getYear())
                    .setParameter("month", date.getMonth().getValue())
                    .setParameter("day", date.getDayOfMonth())
                    .getResultList();
        }
    }

    /**
     * Get items only with stored image.
     * @return List of Items.
     */
    @Override
    public List<IEntity> getItemsWithImg() {
        try (Session session = factory.openSession()) {
            return session.createQuery("from Item where picture is not null order by id desc", IEntity.class)
                    .getResultList();
        }
    }

    /**
     * Get items for specified brand.
     * @param brandId int brandId.
     * @return List of items.
     */
    @Override
    public List<IEntity> getItemsForBrand(int brandId) {
        try (Session session = factory.openSession()) {
            return session.createQuery("from Item where brand.id = :brandId order by id desc", IEntity.class)
                    .setParameter("brandId", brandId)
                    .getResultList();
        }
    }

    public static void main(String[] args) {
        CarDAO dao = new CarDAO();
        System.out.println(dao.getList("Item"));
    }
}
