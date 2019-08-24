package ru.shaplov.persistence;

import org.springframework.data.repository.CrudRepository;
import ru.shaplov.models.TransType;

/**
 * @author shaplov
 * @since 06.08.2019
 */
public interface TransTypeRepository extends CrudRepository<TransType, Integer> {
}
