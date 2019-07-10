package ru.shaplov.logic;

import ru.shaplov.models.*;
import ru.shaplov.persistence.CarDAO;
import ru.shaplov.persistence.IDao;

import java.util.List;

/**
 * @author shaplov
 * @since 14.07.2019
 */
public class LogicDB implements ILogic {

    private final static LogicDB INSTANCE = new LogicDB();

    private final IDao dao = CarDAO.getInstance();

    private LogicDB() {
    }

    public static LogicDB getInstance() {
        return INSTANCE;
    }

    @Override
    public IEntity save(IEntity entity) {
        return dao.save(entity);
    }

    @Override
    public IEntity update(IEntity entity) {
        return dao.update(entity);
    }

    @Override
    public void delete(IEntity entity) {
        dao.delete(entity);
    }

    @Override
    public IEntity get(IEntity entity) {
        return dao.get(entity);
    }

    @Override
    public List<IEntity> getList(String entityName) {
        return dao.getList(entityName);
    }

    @Override
    public List<ITitledEntity> getBodyTypes(int modelId) {
        return dao.getBodyTypes(modelId);
    }

    @Override
    public List<ITitledEntity> getEngineTypes(int modelId, int bodyId) {
        return dao.getEngineTypes(modelId, bodyId);
    }

    @Override
    public List<ITitledEntity> getDriveTypes(int modelId, int bodyId, int engineId) {
        return dao.getDriveTypes(modelId, bodyId, engineId);
    }

    @Override
    public List<ITitledEntity> getTransTypes(int modelId, int bodyId, int engineId, int driveId) {
        return dao.getTransTypes(modelId, bodyId, engineId, driveId);
    }

    @Override
    public CarUser authUser(String login, String password) {
        return dao.authUser(login, password);
    }

    @Override
    public int getItemCount() {
        return dao.getItemCount();
    }

    @Override
    public int getLastItemId() {
        return dao.getLastItemId();
    }

    @Override
    public boolean isSold(int id) {
        return dao.isSold(id);
    }
}
