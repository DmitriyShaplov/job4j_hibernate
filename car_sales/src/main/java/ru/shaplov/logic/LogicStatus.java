package ru.shaplov.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.persistence.ItemRepository;

import java.time.LocalDate;

/**
 * @author shaplov
 * @since 23.07.2019
 */
@Service
@Transactional
public class LogicStatus implements ILogicStatus {

    private final ItemRepository itemRepository;

    @Autowired
    private LogicStatus(ItemRepository dao) {
        this.itemRepository = dao;
    }

    @Override
    public int getItemCount() {
        return (int) itemRepository.count();
    }

    @Override
    public int getLastItemId() {
        return itemRepository.findFirstByOrderByIdDesc().getId();
    }

    @Override
    public int getItemCountForDate(LocalDate date) {
        return itemRepository.countByCreatedGreaterThanEqualAndCreatedLessThan(date);
    }

    @Override
    public int getLastItemIdForDate(LocalDate date) {
        return itemRepository.findFirstByCreatedGreaterThanEqualAndCreatedLessThanOrderByIdDesc(date).getId();
    }

    @Override
    public int getItemCountForBrand(int brandId) {
        return itemRepository.countByBrandId(brandId);
    }

    @Override
    public int getLastItemIdForBrand(int brandId) {
        return itemRepository.findFirstByBrandIdOrderByIdDesc(brandId).getId();
    }

    @Override
    public int getItemCountWithImg() {
        return itemRepository.countByPictureNotNull();
    }

    @Override
    public int getLastItemIdWithImg() {
        return itemRepository.findFirstByPictureNotNullOrderByIdDesc().getId();
    }

    @Override
    public boolean isSold(int id) {
        return itemRepository.getOne(id).isSold();
    }
}
