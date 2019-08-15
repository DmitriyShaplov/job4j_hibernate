package ru.shaplov.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import ru.shaplov.models.*;

import java.util.List;

/**
 * @author shaplov
 * @since 06.08.2019
 */
public interface CarPartsRepository extends Repository<Unifying, Integer> {

    @Query("select distinct body from Unifying u where u.model.id = :modelId")
    List<ITitledEntity> getBodyTypes(@Param("modelId") int modelId);

    @Query("select distinct engine from Unifying u where u.model.id = :modelId and u.body.id = :bodyId")
    List<ITitledEntity> getEngineTypes(@Param("modelId") int modelId, @Param("bodyId") int bodyId);

    @Query("select distinct drive from Unifying u where u.model.id = :modelId and u.body.id = :bodyId and u.engine.id = :engineId")
    List<ITitledEntity> getDriveTypes(@Param("modelId") int modelId, @Param("bodyId") int bodyId, @Param("engineId") int engineId);

    @Query("select distinct trans from Unifying u where u.model.id = :modelId"
            + " and u.body.id = :bodyId and u.engine.id = :engineId and u.drive.id = :driveId")
    List<ITitledEntity> getTransTypes(@Param("modelId") int modelId, @Param("bodyId") int bodyId,
                                      @Param("engineId") int engineId, @Param("driveId") int driveId);
}
