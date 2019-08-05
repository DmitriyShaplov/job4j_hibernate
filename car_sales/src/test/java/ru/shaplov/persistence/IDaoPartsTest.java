package ru.shaplov.persistence;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.shaplov.config.DataJpaConfig;
import ru.shaplov.models.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class IDaoPartsTest {

    private final ApplicationContext ctx = new AnnotationConfigApplicationContext(DataJpaConfig.class);
    private final CarPartsRepository daoParts = ctx.getBean(CarPartsRepository.class);
    private final BrandRepository daoBrand = ctx.getBean(BrandRepository.class);
    private final ModelRepository daoModel = ctx.getBean(ModelRepository.class);
    private final BodyTypeRepository daoBody = ctx.getBean(BodyTypeRepository.class);
    private final EngineTypeRepository daoEngine = ctx.getBean(EngineTypeRepository.class);
    private final DriveTypeRepository daoDrive = ctx.getBean(DriveTypeRepository.class);
    private final TransTypeRepository daoTrans = ctx.getBean(TransTypeRepository.class);
    private final UnifyingRepository daoUni = ctx.getBean(UnifyingRepository.class);

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