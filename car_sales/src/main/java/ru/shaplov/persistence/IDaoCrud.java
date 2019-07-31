package ru.shaplov.persistence;

import ru.shaplov.models.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author shaplov
 * @since 14.07.2019
 */
public interface IDaoCrud {
    IEntity save(IEntity entity);
    IEntity update(IEntity entity);
    void delete(IEntity entity);
    IEntity get(IEntity entity);
    List<IEntity> getList(String entityName);
    List<IEntity> getItemsForDate(LocalDate date);
    List<IEntity> getItemsForBrand(int brandId);
    List<IEntity> getItemsWithImg();
}
