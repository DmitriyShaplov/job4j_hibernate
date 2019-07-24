package ru.shaplov.persistence;

import org.junit.Before;
import org.junit.Test;
import ru.shaplov.models.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class IDaoPartsTest {

    private final IDaoCrud dao = CarDAO.getInstance();
    private final IDaoParts daoParts = DaoParts.getInstance();

    private int modelId;
    private int bodyId;
    private int engineId;
    private int driveId;

    @Before
    public void init() {
        Brand brand = new Brand();
        brand.setTitle("brand");
        brand = (Brand) dao.save(brand);
        Model model = new Model();
        model.setTitle("model");
        model.setBrand(brand);
        model = (Model) dao.save(model);
        modelId = model.getId();
        BodyType body = new BodyType();
        body.setTitle("body");
        body = (BodyType) dao.save(body);
        bodyId = body.getId();
        EngineType engine = new EngineType();
        engine.setTitle("engine");
        engine = (EngineType) dao.save(engine);
        engineId = engine.getId();
        DriveType drive = new DriveType();
        drive.setTitle("drive");
        drive = (DriveType) dao.save(drive);
        driveId = drive.getId();
        TransType trans = new TransType();
        trans.setTitle("trans");
        trans = (TransType) dao.save(trans);
        Unifying unifying = new Unifying();
        unifying.setModel(model);
        unifying.setBody(body);
        unifying.setEngine(engine);
        unifying.setDrive(drive);
        unifying.setTrans(trans);
        dao.save(unifying);
    }

    @Test
    public void whenGetBodyTypes() {
        assertThat(daoParts.getBodyTypes(modelId).iterator().next().getTitle(), is("body"));
    }

    @Test
    public void whenGetEngineTypes() {
        assertThat(daoParts.getEngineTypes(modelId, bodyId).iterator().next().getTitle(), is("engine"));
    }

    @Test
    public void whenGetDriveTypes() {
        assertThat(daoParts.getDriveTypes(modelId, bodyId, engineId).iterator().next().getTitle(), is("drive"));
    }

    @Test
    public void whenGetTransTypes() {
        assertThat(daoParts.getTransTypes(modelId, bodyId, engineId, driveId).iterator().next().getTitle(), is("trans"));
    }
}