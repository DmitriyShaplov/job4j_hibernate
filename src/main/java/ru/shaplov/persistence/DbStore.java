package ru.shaplov.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.shaplov.models.Item;

import java.util.List;
import java.util.function.Function;

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

    private <T> T tran(final Function<Session, T> command) {
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

    /**
     * Adds item to DB.
     * @param item new item w/o id.
     * @return item with generated id.
     */
    @Override
    public Item add(Item item) {
        return tran(session -> {
            session.save(item);
            return item;
        });
    }

    /**
     * Updates item in db.
     * @param item item with all filled fields.
     * @return this item.
     */
    @Override
    public Item update(Item item) {
        return tran(session -> {
            session.update(item);
            return item;
        });
    }

    /**
     * Deletes item from db.
     * @param item item with id.
     */
    @Override
    public void delete(Item item) {
        tran(session -> {
            session.delete(item);
            return null;
        });
    }

    /**
     * Gets item from db.
     * @param item item with id.
     * @return item.
     */
    @Override
    public Item get(Item item) {
        return tran(session -> session.get(Item.class, item.getId()));
    }

    /**
     * Gets all items from db table.
     * @return list of items.
     */
    @Override
    public List<Item> getAll() {
        return tran(session -> session.createQuery("from Item", Item.class).list());
    }
}
