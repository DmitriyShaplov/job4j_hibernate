package ru.shaplov.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.shaplov.models.CarUser;
import ru.shaplov.util.HibernateUtil;

/**
 * @author shaplov
 * @since 30.07.2019
 */
public class DaoUser implements IDaoUser {

    private static final DaoUser INSTANCE = new DaoUser();

    private final SessionFactory factory = HibernateUtil.getInstance().getSessionFactory();

    private DaoUser() {
    }

    public static DaoUser getInstance() {
        return INSTANCE;
    }

    @Override
    public CarUser save(CarUser user) {
        final Session session = factory.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            session.save(user);
            tx.commit();
            return user;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
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
}
