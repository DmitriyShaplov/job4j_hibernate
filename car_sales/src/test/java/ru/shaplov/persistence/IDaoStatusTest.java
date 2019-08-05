package ru.shaplov.persistence;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.shaplov.config.DataJpaConfig;
import ru.shaplov.models.Brand;
import ru.shaplov.models.Item;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class IDaoStatusTest {

    private final ApplicationContext ctx = new AnnotationConfigApplicationContext(DataJpaConfig.class);
    private final ItemRepository dao = ctx.getBean(ItemRepository.class);
    private final BrandRepository daoBrand = ctx.getBean(BrandRepository.class);

    @Before
    public void clearItems() {
        dao.findAll().forEach(dao::delete);
    }

    @Test
    public void whenGetItemCountAndIdThen2andLastId() {
        Item item1 = new Item();
        Item item2 = new Item();
        dao.save(item1);
        dao.save(item2);
        assertThat((int) dao.count(), is(2));
        assertThat(dao.findFirstByOrderByIdDesc().getId(), is(item2.getId()));
    }

    @Test
    public void whenGetItemCountAndIdTodayThen1AndTodayId() {
        Item item1 = new Item();
        item1.setCreated(LocalDateTime.now());
        Item item2 = new Item();
        item2.setCreated(LocalDateTime.now().minusDays(1));
        dao.save(item1);
        dao.save(item2);
        assertThat(dao.countByCreatedGreaterThanEqualAndCreatedLessThan(LocalDate.now()), is(1));
        assertThat(dao.findFirstByCreatedGreaterThanEqualAndCreatedLessThanOrderByIdDesc(LocalDate.now()).getId(), is(item1.getId()));
    }

    @Test
    public void whenGetItemCountAndIdForBrandThen1AndId() {
        Brand brand = new Brand();
        brand = daoBrand.save(brand);
        Item item1 = new Item();
        item1.setBrand(brand);
        Item item2 = new Item();
        dao.save(item1);
        dao.save(item2);
        assertThat(dao.countByBrandId(brand.getId()), is(1));
        assertThat(dao.findFirstByBrandIdOrderByIdDesc(brand.getId()).getId(), is(item1.getId()));
    }

    @Test
    public void whenGetItemCountAndIdWithImgThen1AndId() {
        Item item1 = new Item();
        item1.setPicture("picture");
        Item item2 = new Item();
        dao.save(item1);
        dao.save(item2);
        assertThat(dao.countByPictureNotNull(), is(1));
        assertThat(dao.findFirstByPictureNotNullOrderByIdDesc().getId(), is(item1.getId()));
    }

    @Test
    public void whenAddSoldItemThenCheckIsSoldIsTrue() {
        Item item = new Item();
        item.setSold(true);
        item = dao.save(item);
        assertTrue(dao.findById(item.getId()).orElseThrow().isSold());
    }

    @Test
    public void whenAddNotSoldItemThenCheckIsSoldIsFalse() {
        Item item = new Item();
        item.setSold(false);
        item = dao.save(item);
        assertFalse(dao.findById(item.getId()).orElseThrow().isSold());
    }
}