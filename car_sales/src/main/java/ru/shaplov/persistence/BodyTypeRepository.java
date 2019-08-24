package ru.shaplov.persistence;

import org.springframework.data.repository.CrudRepository;
import ru.shaplov.models.BodyType;

/**
 * @author shaplov
 * @since 06.08.2019
 */
public interface BodyTypeRepository extends CrudRepository<BodyType, Integer> {
}
