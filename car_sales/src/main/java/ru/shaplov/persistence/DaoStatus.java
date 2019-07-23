package ru.shaplov.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.shaplov.util.HibernateUtil;

import java.time.LocalDate;

/**
 * @author shaplov
 * @since 23.07.2019
 */
public class DaoStatus implements IDaoStatus {

    private static final DaoStatus INSTANCE = new DaoStatus();

    private final SessionFactory factory = HibernateUtil.getInstance().getSessionFactory();

    private DaoStatus() {
    }

    public static DaoStatus getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isSold(int id) {
        try (Session session = factory.openSession()) {
            return session.createQuery("select sold From Item where id = :id", Boolean.class)
                    .setParameter("id", id)
                    .getSingleResult();
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
            return session.createQuery("select id From Item order by id desc", Number.class)
                    .setMaxResults(1)
                    .getSingleResult().intValue();
        }
    }

    @Override
    public int getItemCountForDate(LocalDate date) {
        try (Session session = factory.openSession()) {
            return session.createQuery("select count(*) from Item where year(created) = :year and "
                    + "month(created) = :month and day(created) = :day", Number.class)
                    .setParameter("year", date.getYear())
                    .setParameter("month", date.getMonthValue())
                    .setParameter("day", date.getDayOfMonth())
                    .getSingleResult().intValue();
        }
    }

    @Override
    public int getLastItemIdForDate(LocalDate date) {
        try (Session session = factory.openSession()) {
            return session.createQuery("select id From Item where year(created) = :year and "
                    + "month(created) = :month and day(created) = :day order by id desc", Number.class).setMaxResults(1)
                    .setParameter("year", date.getYear())
                    .setParameter("month", date.getMonthValue())
                    .setParameter("day", date.getDayOfMonth())
                    .setMaxResults(1)
                    .getSingleResult().intValue();
        }
    }

    @Override
    public int getItemCountForBrand(int brandId) {
        try (Session session = factory.openSession()) {
            return session.createQuery("select count(*) from Item where brand.id = :brandId", Number.class)
                    .setParameter("brandId", brandId)
                    .getSingleResult().intValue();
        }
    }

    @Override
    public int getLastItemIdForBrand(int brandId) {
        try (Session session = factory.openSession()) {
            return session.createQuery("select id From Item where brand.id = :brandId order by id desc", Number.class)
                    .setParameter("brandId", brandId)
                    .setMaxResults(1)
                    .getSingleResult().intValue();
        }
    }

    @Override
    public int getItemCountWithImg() {
        try (Session session = factory.openSession()) {
            return session.createQuery("select count(*) from Item where picture is not null", Number.class)
                    .getSingleResult().intValue();
        }
    }

    @Override
    public int getLstItemIdWithImg() {
        try (Session session = factory.openSession()) {
            return session.createQuery("select id from Item where picture is not null", Number.class)
                    .getSingleResult().intValue();
        }
    }
}
