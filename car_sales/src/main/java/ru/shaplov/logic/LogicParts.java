package ru.shaplov.logic;

import ru.shaplov.models.ITitledEntity;
import ru.shaplov.persistence.DaoParts;

import java.util.List;

/**
 * @author shaplov
 * @since 23.07.2019
 */
public class LogicParts implements ILogicParts {

    private final static LogicParts INSTANCE = new LogicParts();

    private final DaoParts daoParts = DaoParts.getInstance();

    private LogicParts() {
    }

    public static LogicParts getInstance() {
        return INSTANCE;
    }

    @Override
    public List<ITitledEntity> getBodyTypes(int modelId) {
        return daoParts.getBodyTypes(modelId);
    }

    @Override
    public List<ITitledEntity> getEngineTypes(int modelId, int bodyId) {
        return daoParts.getEngineTypes(modelId, bodyId);
    }

    @Override
    public List<ITitledEntity> getDriveTypes(int modelId, int bodyId, int engineId) {
        return daoParts.getDriveTypes(modelId, bodyId, engineId);
    }

    @Override
    public List<ITitledEntity> getTransTypes(int modelId, int bodyId, int engineId, int driveId) {
        return daoParts.getTransTypes(modelId, bodyId, engineId, driveId);
    }
}
