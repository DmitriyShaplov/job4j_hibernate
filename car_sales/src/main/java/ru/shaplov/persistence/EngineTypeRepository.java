package ru.shaplov.persistence;

import org.springframework.data.repository.CrudRepository;
import ru.shaplov.models.EngineType;

/**
 * @author shaplov
 * @since 06.08.2019
 */
public interface EngineTypeRepository extends CrudRepository<EngineType, Integer> {
}
