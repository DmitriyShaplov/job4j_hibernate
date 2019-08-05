package ru.shaplov.persistence;

import org.springframework.data.repository.CrudRepository;
import ru.shaplov.models.DriveType;

/**
 * @author shaplov
 * @since 06.08.2019
 */
public interface DriveTypeRepository extends CrudRepository<DriveType, Integer> {
}
