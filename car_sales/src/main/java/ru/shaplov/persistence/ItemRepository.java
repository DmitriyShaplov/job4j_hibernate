package ru.shaplov.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shaplov.models.Item;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author shaplov
 * @since 05.08.2019
 */
public interface ItemRepository extends JpaRepository<Item, Integer> {

    /**
     * Find all items Ordered by id desc.
     * @return List of items.
     */
    List<Item> findAllByOrderByIdDesc();

    /**
     * Returns List of Items with specified brand id.
     * @param brandId Brand id.
     * @return List of Items.
     */
    List<Item> findByBrandIdOrderByIdDesc(int brandId);

    /**
     * Returns List of items with picture.
     * @return List of Items.
     */
    List<Item> findByPictureNotNullOrderByIdDesc();

    /**
     * Return List of Items between two LocalDateTime.
     * @param start inclusive start.
     * @param end exclusive end.
     * @return List of Items.
     */
    List<Item> findByCreatedGreaterThanEqualAndCreatedLessThanOrderByIdDesc(LocalDateTime start, LocalDateTime end);

    /**
     * Returns List of Items on specified date.
     * @param date LocalDate date.
     * @return List of Items.
     */
    default List<Item> findByCreatedGreaterThanEqualAndCreatedLessThanOrderByIdDesc(LocalDate date) {
        return findByCreatedGreaterThanEqualAndCreatedLessThanOrderByIdDesc(
                date.atStartOfDay(), date.plusDays(1).atStartOfDay()
        );
    }

    /**
     * Returns last item.
     */
    Item findFirstByOrderByIdDesc();

    /**
     * Returns Item count between two LocalDateTime inclusive at start and exclusive at end.
     */
    int countByCreatedGreaterThanEqualAndCreatedLessThan(LocalDateTime start, LocalDateTime end);

    /**
     * Returns Item count on specified date.
     */
    default int countByCreatedGreaterThanEqualAndCreatedLessThan(LocalDate date) {
        return countByCreatedGreaterThanEqualAndCreatedLessThan(
                date.atStartOfDay(), date.plusDays(1).atStartOfDay()
        );
    }

    /**
     * Returns last Item filtered by date. (between start and end of the day)
     */
    Item findFirstByCreatedGreaterThanEqualAndCreatedLessThanOrderByIdDesc(LocalDateTime start, LocalDateTime end);

    /**
     * Returns last Item filtered by date.
     */
    default Item findFirstByCreatedGreaterThanEqualAndCreatedLessThanOrderByIdDesc(LocalDate date) {
        return findFirstByCreatedGreaterThanEqualAndCreatedLessThanOrderByIdDesc(
                date.atStartOfDay(), date.plusDays(1).atStartOfDay()
        );
    }

    /**
     * Returns count by brand id.
     */
    int countByBrandId(int brandId);

    /**
     * Return last item filtered by brand id.
     */
    Item findFirstByBrandIdOrderByIdDesc(int brandId);

    /**
     * Returns count with picture.
     */
    int countByPictureNotNull();

    /**
     * Returns last Item with picture.
     */
    Item findFirstByPictureNotNullOrderByIdDesc();
}
