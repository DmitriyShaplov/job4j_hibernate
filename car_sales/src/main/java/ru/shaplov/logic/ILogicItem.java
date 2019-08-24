package ru.shaplov.logic;

import ru.shaplov.models.*;

import javax.servlet.http.Part;
import java.time.LocalDate;
import java.util.List;

/**
 * @author shaplov
 * @since 14.07.2019
 */
public interface ILogicItem {
    Item save(Item entity, Part file);
    Item update(Item entity);
    void delete(Item entity);
    Item get(Item entity);
    List<Item> getItems();
    List<Item> getItemsForDate(LocalDate date);
    List<Item> getItemsForBrand(int brandId);
    List<Item> getItemsWithImg();
    PictureLob getImg(PictureLob entity);
}
