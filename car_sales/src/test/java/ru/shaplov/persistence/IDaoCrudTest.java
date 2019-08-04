package ru.shaplov.persistence;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.shaplov.config.TestConfig;
import ru.shaplov.config.WebConfig;
import ru.shaplov.models.Brand;
import ru.shaplov.models.CarUser;
import ru.shaplov.models.IEntity;
import ru.shaplov.models.Item;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class IDaoCrudTest {

    private final ApplicationContext ctx = new AnnotationConfigApplicationContext(TestConfig.class);
    private final IDaoCrud dao = ctx.getBean(IDaoCrud.class);
    private final IDaoUser daoUser = ctx.getBean(IDaoUser.class);

    @Test
    public void whenAddUserThenNewUser() {
        CarUser user = new CarUser();
        user.setLogin("test login");
        user.setPassword("test password");
        user = (CarUser) dao.save(user);
        assertThat(dao.get(new CarUser(user.getId())), is(user));
    }

    @Test
    public void whenUpdateUserThenNewLogin() {
        CarUser user = new CarUser();
        user.setLogin("test login 1");
        user.setPassword("test password");
        user = (CarUser) dao.save(user);
        user.setLogin("test login updated");
        dao.update(user);
        assertThat(((CarUser) dao.get(new CarUser(user.getId()))).getLogin(), is("test login updated"));
    }

    @Test
    public void whenDeleteUserThenNull() {
        CarUser user = new CarUser();
        user.setLogin("test login delete");
        user.setPassword("test password");
        user = (CarUser) dao.save(user);
        dao.delete(user);
        assertNull(dao.get(user));
    }

    @Test
    public void whenGetUserThereTestLogin() {
        CarUser user = new CarUser();
        user.setLogin("test login get");
        user.setPassword("test password");
        user = (CarUser) dao.save(user);
        assertThat(((CarUser) dao.get(new CarUser(user.getId()))).getLogin(), is("test login get"));
    }

    @Test
    public void whenAddItemThenGetUsersLogin() {
        CarUser user = new CarUser();
        user.setLogin("test login in item");
        user.setPassword("test password");
        user = (CarUser) dao.save(user);
        Item item = new Item();
        item.setUser(user);
        item = (Item) dao.save(item);
        assertThat(((Item) dao.get(new Item(item.getId()))).getUser().getLogin(), is("test login in item"));
    }

    @Test
    public void whenGetListThenContainsItem() {
        Item item = new Item();
        item.setTitle("test item in list");
        dao.save(item);
        assertTrue(dao.getList("Item").contains(item));
    }

    @Test
    public void whenGetListOnTodayItContainsOnlyForToday() {
        Item itemToday = new Item();
        itemToday.setTitle("test item today");
        itemToday.setCreated(Calendar.getInstance());
        dao.save(itemToday);
        Item itemYesterday = new Item();
        itemYesterday.setTitle("test item today");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        itemYesterday.setCreated(calendar);
        dao.save(itemYesterday);
        assertTrue(dao.getItemsForDate(LocalDate.now()).contains(itemToday));
        assertFalse(dao.getItemsForDate(LocalDate.now()).contains(itemYesterday));
    }

    @Test
    public void whenGetListForBrandItContainsBrand() {
        Brand brandAudi = new Brand();
        brandAudi.setTitle("audi");
        brandAudi = (Brand) dao.save(brandAudi);
        Item itemAudi = new Item();
        itemAudi.setBrand(brandAudi);
        dao.save(itemAudi);
        Brand brandBMW = new Brand();
        brandBMW.setTitle("BMW");
        brandBMW = (Brand) dao.save(brandBMW);
        Item itemBMW = new Item();
        itemBMW.setBrand(brandBMW);
        dao.save(itemBMW);
        List<IEntity> list = dao.getItemsForBrand(brandAudi.getId());
        assertTrue(dao.getItemsForBrand(brandAudi.getId()).contains(itemAudi));
        assertFalse(dao.getItemsForBrand(brandAudi.getId()).contains(itemBMW));
    }

    @Test
    public void whenGetListWithImgItContainsOnlyWithImg() {
        Item itemImg = new Item();
        itemImg.setPicture("picture...");
        dao.save(itemImg);
        Item itemWOImg = new Item();
        dao.save(itemWOImg);
        assertTrue(dao.getItemsWithImg().contains(itemImg));
        assertFalse(dao.getItemsWithImg().contains(itemWOImg));
    }

    @Test
    public void whenAuthValidUserThenTrue() {
        CarUser user = new CarUser();
        user.setLogin("test auth");
        user.setPassword("test password");
        daoUser.save(user);
        assertThat(daoUser.authUser("test auth", "test password"), is(user));
    }

    @Test
    public void whenAuthInvalidUserThenFalse() {
        CarUser user = new CarUser();
        user.setLogin("test auth false");
        user.setPassword("test password");
        daoUser.save(user);
        assertNull(daoUser.authUser("test auth false", "wrong password"));
    }
}