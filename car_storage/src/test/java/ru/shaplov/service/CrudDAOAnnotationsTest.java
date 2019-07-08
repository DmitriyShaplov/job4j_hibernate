package ru.shaplov.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.shaplov.models.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CrudDAOAnnotationsTest {

    private static final SessionFactory FACTORY;

    private CarBodyANN carBody;
    private EngineANN engine;
    private TransmissionANN transmission;
    private CarANN car;

    static {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            FACTORY = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw e;
        }
    }

    @Before
    public void setUp() {
        carBody = new CarBodyANN();
        carBody.setName("test car body");
        engine = new EngineANN();
        engine.setName("test engine");
        transmission = new TransmissionANN();
        transmission.setName("test transmission");
        car = new CarANN();
        car.setName("test car");
        car.setCarBody(carBody);
        car.setEngine(engine);
        car.setTransmission(transmission);
    }

    @After
    public void clear() {
        try (Session session = FACTORY.openSession()) {
            session.beginTransaction();
            session.delete(car);
            session.delete(carBody);
            session.delete(engine);
            session.delete(transmission);
            session.getTransaction().commit();
        }
    }

    @Test
    public void whenAddCarThenEngineNameTestEngine() {
        CrudDAO dao = CrudDAO.getInstance();
        dao.add(car);
        EngineANN result = (EngineANN) dao.get(engine);
        assertThat(result.getName(), is("test engine"));
    }

    @Test
    public void whenUpdateCarThenNewName() {
        CrudDAO dao = CrudDAO.getInstance();
        dao.add(car);
        engine.setName("updated engine name");
        dao.update(engine);
        EngineANN result = (EngineANN) dao.get(engine);
        assertThat(result.getName(), is("updated engine name"));
    }

    @Test
    public void whenDeleteThenNull() {
        CrudDAO dao = CrudDAO.getInstance();
        TransmissionANN tran = new TransmissionANN();
        tran.setName("deleted transmission");
        dao.add(tran);
        dao.delete(tran);
        IEntity result = dao.get(tran);
        assertNull(result);
    }
}