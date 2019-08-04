package ru.shaplov.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shaplov.models.*;
import ru.shaplov.persistence.IDaoCrud;

import java.time.LocalDate;
import java.util.List;

/**
 * @author shaplov
 * @since 14.07.2019
 */
@Service
public class LogicDB implements ILogicDB {

    private final IDaoCrud dao;

    @Autowired
    private LogicDB(IDaoCrud dao) {
        this.dao = dao;
    }

    @Override
    public IEntity save(IEntity entity) {
        return dao.save(entity);
    }

    @Override
    public IEntity update(IEntity entity) {
        return dao.update(entity);
    }

    @Override
    public void delete(IEntity entity) {
        dao.delete(entity);
    }

    @Override
    public IEntity get(IEntity entity) {
        return dao.get(entity);
    }

    @Override
    public List<IEntity> getList(String entityName) {
        return dao.getList(entityName);
    }

    @Override
    public List<IEntity> getItemsForDate(LocalDate date) {
        return dao.getItemsForDate(date);
    }

    @Override
    public List<IEntity> getItemsForBrand(int brandId) {
        return dao.getItemsForBrand(brandId);
    }

    @Override
    public List<IEntity> getItemsWithImg() {
        return dao.getItemsWithImg();
    }
}
