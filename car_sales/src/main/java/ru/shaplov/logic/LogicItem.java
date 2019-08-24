package ru.shaplov.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.models.*;
import ru.shaplov.persistence.ItemRepository;
import ru.shaplov.persistence.PictureLobRepository;

import javax.servlet.http.Part;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * @author shaplov
 * @since 14.07.2019
 */
@Service
@Transactional
public class LogicItem implements ILogicItem {

    private static final Logger LOG = LogManager.getLogger(LogicItem.class);

    private final ItemRepository itemRepository;
    private final PictureLobRepository pictureLobRepository;

    @Autowired
    public LogicItem(ItemRepository itemRepository, PictureLobRepository pictureLobRepository) {
        this.itemRepository = itemRepository;
        this.pictureLobRepository = pictureLobRepository;
    }

    @Override
    public Item save(Item entity, Part file) {
        try {
            if (file.getSize() > 0) {
                PictureLob img = new PictureLob();
                img.setImg(file.getInputStream().readAllBytes());
                img.setMimeType(file.getContentType());
                img = pictureLobRepository.save(img);
                entity.setPicture(String.valueOf(img.getId()));
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
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

    @Override
    public PictureLob getImg(PictureLob entity) {
        return pictureLobRepository.findById(entity.getId()).orElse(null);
    }
}
