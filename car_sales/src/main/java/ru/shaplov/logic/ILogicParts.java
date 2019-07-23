package ru.shaplov.logic;

import ru.shaplov.models.ITitledEntity;

import java.util.List;

/**
 * @author shaplov
 * @since 23.07.2019
 */
public interface ILogicParts {
    List<ITitledEntity> getBodyTypes(int modelId);
    List<ITitledEntity> getEngineTypes(int modelId, int bodyId);
    List<ITitledEntity> getDriveTypes(int modelId, int bodyId, int engineId);
    List<ITitledEntity> getTransTypes(int modelId, int bodyId, int engineId, int driveId);
}
