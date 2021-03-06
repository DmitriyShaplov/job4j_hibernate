package ru.shaplov.persistence;

import ru.shaplov.models.Item;

import java.util.List;

/**
 * @author shaplov
 * @since 05.07.2019
 */
public interface IStore {
    Item add(Item item);
    Item update(Item item);
    void delete(Item item);
    Item get(Item item);
    List<Item> getAll();
}
