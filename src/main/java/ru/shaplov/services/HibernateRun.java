package ru.shaplov.services;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.shaplov.models.User;

import java.util.Calendar;
import java.util.List;

/**
 * Test class for hibernate first launch.
 * @author shaplov
 * @since 04.07.2019
 */
public class HibernateRun {

    private SessionFactory factory;

    /**
     * Set up Session Factory.
     */
    public void setUp() {
        factory = new Configuration()
                .configure()
                .buildSessionFactory();
    }

    /**
     * Save or update user.
     * @param user for save - w/o id, for update - with id.
     * @return user id.
     */
    public int saveOrUpdate(User user) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.saveOrUpdate(user);
            tx.commit();
            return user.getId();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
                throw e;
            }
        }
        throw new IllegalStateException("Something wrong with user creation");
    }

    /**
     * Find user by id in db.
     * @param id User's id
     * @return user.
     */
    public User findUser(int id) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            tx.commit();
            return user;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
                throw e;
            }
        }
        throw new IllegalStateException("Something wrong with finding user");
    }

    /**
     * Deletes specified user from db.
     * @param user user with id field.
     */
    public void deleteUser(User user) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.delete(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
                throw e;
            }
        }
    }

    /**
     * Get list of users from db.
     * @return List of users.
     */
    public List<User> findAll() {
        List<User> res = null;
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            res = session.createQuery("from User", User.class).list();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
                throw e;
            }
        }
        return res;
    }

    /**
     * Main.
     * @param args args.
     */
    public static void main(String[] args) {
        HibernateRun hiberTest = new HibernateRun();
        hiberTest.setUp();
        User user = new User();
        user.setName("Test");
        user.setExpired(Calendar.getInstance());
        int userId = hiberTest.saveOrUpdate(user);
        System.out.println(userId);
        user = hiberTest.findUser(userId);
        System.out.println(user);
        user.setName("Test updated");
        hiberTest.saveOrUpdate(user);
        user = hiberTest.findUser(userId);
        System.out.println(user);
        hiberTest.deleteUser(user);
        List<User> users = hiberTest.findAll();
        System.out.println(users);
    }
}
