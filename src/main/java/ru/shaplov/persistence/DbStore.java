package ru.shaplov.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.shaplov.models.Item;

import java.util.List;

/**
 * Singleton dao class.
 * @author shaplov
 * @since 05.07.2019
 */
public class DbStore implements IStore {

    private static final DbStore INSTANCE = new DbStore();

    private final SessionFactory factory;

    private DbStore() {
        factory = new Configuration()
                .configure()
                .buildSessionFactory();
    }

    public static DbStore getInstance() {
        return INSTANCE;
    }

    /**
     * Adds item to DB.
     * @param item new item w/o id.
     * @return item with generated id.
     */
    @Override
    public Item add(Item item) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.save(item);
            tx.commit();
            return item;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        }
        throw new IllegalStateException("Something wrong with item addition");
    }

    /**
     * Updates item in db.
     * @param item item with all filled fields.
     * @return this item.
     */
    @Override
    public Item update(Item item) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.update(item);
            tx.commit();
            return item;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        }
        throw new IllegalStateException("Something wrong with item addition");
    }

    /**
     * Deletes item from db.
     * @param item item with id.
     */
    @Override
    public void delete(Item item) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.delete(item);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
                throw new IllegalStateException("Something wrong with delete");
            }
        }

    }

    /**
     * Gets item from db.
     * @param item item with id.
     * @return item.
     */
    @Override
    public Item get(Item item) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            Item result = session.get(Item.class, item.getId());
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        }
        throw new IllegalStateException("Something wrong with get");
    }

    /**
     * Gets all items from db table.
     * @return list of items.
     */
    @Override
    public List<Item> getAll() {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            List<Item> result = session.createQuery("from Item", Item.class).list();
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        }
        throw new IllegalStateException("Something wrong with getAll");
    }
}
