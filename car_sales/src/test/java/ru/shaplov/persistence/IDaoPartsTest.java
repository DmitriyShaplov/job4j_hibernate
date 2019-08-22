package ru.shaplov.persistence;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.shaplov.models.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("/application-test.properties")
public class IDaoPartsTest {

    @Autowired
    private CarPartsRepository daoParts;
    @Autowired
    private BrandRepository daoBrand;
    @Autowired
    private ModelRepository daoModel;
    @Autowired
    private BodyTypeRepository daoBody;
    @Autowired
    private EngineTypeRepository daoEngine;
    @Autowired
    private DriveTypeRepository daoDrive;
    @Autowired
    private TransTypeRepository daoTrans;
    @Autowired
    private UnifyingRepository daoUni;

    private int modelId;
    private int bodyId;
    private int engineId;
    private int driveId;

    @Before
    public void init() {
        Brand brand = new Brand();
        brand.setTitle("brand");
        brand = daoBrand.save(brand);
        Model model = new Model();
        model.setTitle("model");
        model.setBrand(brand);
        model = daoModel.save(model);
        modelId = model.getId();
        BodyType body = new BodyType();
        body.setTitle("body");
        body = daoBody.save(body);
        bodyId = body.getId();
        EngineType engine = new EngineType();
        engine.setTitle("engine");
        engine = daoEngine.save(engine);
        engineId = engine.getId();
        DriveType drive = new DriveType();
        drive.setTitle("drive");
        drive = daoDrive.save(drive);
        driveId = drive.getId();
        TransType trans = new TransType();
        trans.setTitle("trans");
        trans = daoTrans.save(trans);
        Unifying unifying = new Unifying();
        unifying.setModel(model);
        unifying.setBody(body);
        unifying.setEngine(engine);
        unifying.setDrive(drive);
        unifying.setTrans(trans);
        daoUni.save(unifying);
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