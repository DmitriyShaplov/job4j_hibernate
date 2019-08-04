package ru.shaplov.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shaplov.persistence.DaoStatus;

import java.time.LocalDate;

/**
 * @author shaplov
 * @since 23.07.2019
 */
@Service
public class LogicStatus implements ILogicStatus {

    private final DaoStatus dao;

    @Autowired
    private LogicStatus(DaoStatus dao) {
        this.dao = dao;
    }

    @Override
    public int getItemCount() {
        return dao.getItemCount();
    }

    @Override
    public int getLastItemId() {
        return dao.getLastItemId();
    }

    @Override
    public boolean isSold(int id) {
        return dao.isSold(id);
    }

    @Override
    public int getItemCountForDate(LocalDate date) {
        return dao.getItemCountForDate(date);
    }

    @Override
    public int getLastItemIdForDate(LocalDate date) {
        return dao.getLastItemIdForDate(date);
    }

    @Override
    public int getItemCountForBrand(int brandId) {
        return dao.getItemCountForBrand(brandId);
    }

    @Override
    public int getLastItemIdForBrand(int brandId) {
        return dao.getLastItemIdForBrand(brandId);
    }

    @Override
    public int getItemCountWithImg() {
        return dao.getItemCountWithImg();
    }

    @Override
    public int getLastItemIdWithImg() {
        return dao.getLastItemIdWithImg();
    }
}
