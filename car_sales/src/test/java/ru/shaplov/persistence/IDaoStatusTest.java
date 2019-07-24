package ru.shaplov.persistence;

import org.junit.Before;
import org.junit.Test;
import ru.shaplov.models.Brand;
import ru.shaplov.models.Item;

import java.time.LocalDate;
import java.util.Calendar;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class IDaoStatusTest {

    private final IDaoCrud dao = CarDAO.getInstance();
    private final IDaoStatus daoStatus = DaoStatus.getInstance();

    @Before
    public void clearItems() {
        dao.getList("Item").forEach(dao::delete);
    }

    @Test
    public void whenGetItemCountAndIdThen2andLastId() {
        Item item1 = new Item();
        Item item2 = new Item();
        dao.save(item1);
        dao.save(item2);
        assertThat(daoStatus.getItemCount(), is(2));
        assertThat(daoStatus.getLastItemId(), is(item2.getId()));
    }

    @Test
    public void whenGetItemCountAndIdTodayThen1AndTodayId() {
        Item item1 = new Item();
        item1.setCreated(Calendar.getInstance());
        Item item2 = new Item();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_MONTH, -1);
        item2.setCreated(yesterday);
        dao.save(item1);
        dao.save(item2);
        assertThat(daoStatus.getItemCountForDate(LocalDate.now()), is(1));
        assertThat(daoStatus.getLastItemIdForDate(LocalDate.now()), is(item1.getId()));
    }

    @Test
    public void whenGetItemCountAndIdForBrandThen1AndId() {
        Brand brand = new Brand();
        brand = (Brand) dao.save(brand);
        Item item1 = new Item();
        item1.setBrand(brand);
        Item item2 = new Item();
        dao.save(item1);
        dao.save(item2);
        assertThat(daoStatus.getItemCountForBrand(brand.getId()), is(1));
        assertThat(daoStatus.getLastItemIdForBrand(brand.getId()), is(item1.getId()));
    }

    @Test
    public void whenGetItemCountAndIdWithImgThen1AndId() {
        Item item1 = new Item();
        item1.setPicture("picture");
        Item item2 = new Item();
        dao.save(item1);
        dao.save(item2);
        assertThat(daoStatus.getItemCountWithImg(), is(1));
        assertThat(daoStatus.getLastItemIdWithImg(), is(item1.getId()));
    }

    @Test
    public void whenAddSoldItemThenCheckIsSoldIsTrue() {
        Item item = new Item();
        item.setSold(true);
        item = (Item) dao.save(item);
        assertTrue(daoStatus.isSold(item.getId()));
    }

    @Test
    public void whenAddNotSoldItemThenCheckIsSoldIsFalse() {
        Item item = new Item();
        item.setSold(false);
        item = (Item) dao.save(item);
        assertFalse(daoStatus.isSold(item.getId()));
    }
}