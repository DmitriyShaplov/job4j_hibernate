package ru.shaplov.logic;

import ru.shaplov.models.*;

import java.util.List;

/**
 * @author shaplov
 * @since 14.07.2019
 */
public interface ILogic {
    IEntity save(IEntity entity);
    IEntity update(IEntity entity);
    void delete(IEntity entity);
    IEntity get(IEntity entity);
    List<IEntity> getList(String entityName);
    List<ITitledEntity> getBodyTypes(int modelId);
    List<ITitledEntity> getEngineTypes(int modelId, int bodyId);
    List<ITitledEntity> getDriveTypes(int modelId, int bodyId, int engineId);
    List<ITitledEntity> getTransTypes(int modelId, int bodyId, int engineId, int driveId);
    CarUser authUser(String login, String password);
    int getItemCount();
    int getLastItemId();
    boolean isSold(int id);
}
