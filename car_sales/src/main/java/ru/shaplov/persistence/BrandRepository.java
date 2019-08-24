package ru.shaplov.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shaplov.models.Brand;

/**
 * @author shaplov
 * @since 06.08.2019
 */
public interface BrandRepository extends JpaRepository<Brand, Integer> {
}
