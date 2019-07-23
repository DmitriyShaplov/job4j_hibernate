package ru.shaplov.persistence;

import java.time.LocalDate;

/**
 * @author shaplov
 * @since 23.07.2019
 */
public interface IDaoStatus {
    int getItemCount();
    int getLastItemId();
    int getItemCountForDate(LocalDate date);
    int getLastItemIdForDate(LocalDate date);
    int getItemCountForBrand(int brandId);
    int getLastItemIdForBrand(int brandId);
    int getItemCountWithImg();
    int getLstItemIdWithImg();
    boolean isSold(int id);
}
