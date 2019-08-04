package ru.shaplov.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shaplov.models.ITitledEntity;
import ru.shaplov.persistence.DaoParts;

import java.util.List;

/**
 * @author shaplov
 * @since 23.07.2019
 */
@Service
public class LogicParts implements ILogicParts {

    private final DaoParts daoParts;

    @Autowired
    private LogicParts(DaoParts daoParts) {
        this.daoParts = daoParts;
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
