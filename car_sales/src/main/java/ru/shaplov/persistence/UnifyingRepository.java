package ru.shaplov.persistence;

import org.springframework.data.repository.CrudRepository;
import ru.shaplov.models.Unifying;

/**
 * @author shaplov
 * @since 06.08.2019
 */
public interface UnifyingRepository extends CrudRepository<Unifying, Integer> {
}
