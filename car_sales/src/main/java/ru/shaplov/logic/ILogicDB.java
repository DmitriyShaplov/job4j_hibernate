package ru.shaplov.logic;

import ru.shaplov.models.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author shaplov
 * @since 14.07.2019
 */
public interface ILogicDB {
    IEntity save(IEntity entity);
    IEntity update(IEntity entity);
    void delete(IEntity entity);
    IEntity get(IEntity entity);
    List<IEntity> getList(String entityName);
    List<IEntity> getItemsForDate(LocalDate date);
    List<IEntity> getItemsForBrand(int brandId);
    List<IEntity> getItemsWithImg();
    CarUser authUser(String login, String password);
}
