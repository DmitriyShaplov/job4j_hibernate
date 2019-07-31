package ru.shaplov.logic;

import java.time.LocalDate;

/**
 * @author shaplov
 * @since 23.07.2019
 */
public interface ILogicStatus {
    int getItemCount();
    int getLastItemId();
    int getItemCountForDate(LocalDate date);
    int getLastItemIdForDate(LocalDate date);
    int getItemCountForBrand(int brandId);
    int getLastItemIdForBrand(int brandId);
    int getItemCountWithImg();
    int getLastItemIdWithImg();
    boolean isSold(int id);
}
