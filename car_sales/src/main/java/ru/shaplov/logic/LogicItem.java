package ru.shaplov.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.models.*;
import ru.shaplov.persistence.ItemRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author shaplov
 * @since 14.07.2019
 */
@Service
@Transactional
public class LogicItem implements ILogicItem {

    private final ItemRepository itemRepository;

    @Autowired
    private LogicItem(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item save(Item entity) {
        return itemRepository.save(entity);
    }

    @Override
    public Item update(Item entity) {
        return itemRepository.save(entity);
    }

    @Override
    public void delete(Item entity) {
        itemRepository.delete(entity);
    }

    @Override
    public Item get(Item entity) {
        return itemRepository.findById(entity.getId()).orElse(null);
    }

    @Override
    public List<Item> getItems() {
        return itemRepository.findAllByOrderByIdDesc();
    }

    @Override
    public List<Item> getItemsForDate(LocalDate date) {
        return itemRepository.findByCreatedGreaterThanEqualAndCreatedLessThanOrderByIdDesc(date);
    }

    @Override
    public List<Item> getItemsForBrand(int brandId) {
        return itemRepository.findByBrandIdOrderByIdDesc(brandId);
    }

    @Override
    public List<Item> getItemsWithImg() {
        return itemRepository.findByPictureNotNullOrderByIdDesc();
    }
}
