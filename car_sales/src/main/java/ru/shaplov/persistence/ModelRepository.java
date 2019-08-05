package ru.shaplov.persistence;

import org.springframework.data.repository.CrudRepository;
import ru.shaplov.models.Model;

/**
 * @author shaplov
 * @since 06.08.2019
 */
public interface ModelRepository extends CrudRepository<Model, Integer> {
}
