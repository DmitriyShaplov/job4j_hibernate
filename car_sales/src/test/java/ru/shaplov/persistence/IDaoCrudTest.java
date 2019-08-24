package ru.shaplov.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.shaplov.models.Brand;
import ru.shaplov.models.CarUser;
import ru.shaplov.models.Item;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("/application-test.properties")
public class IDaoCrudTest {

    @Autowired
    private ItemRepository dao;

    @Autowired
    private UserRepository daoUser;

    @Autowired
    private BrandRepository daoBrand;

    @Test
    public void whenAddUserThenNewUser() {
        CarUser user = new CarUser();
        user.setLogin("test login");
        user.setPassword("test password");
        user = daoUser.save(user);
        assertThat(daoUser.findById(user.getId()).orElse(null), is(user));
    }

    @Test
    public void whenUpdateUserThenNewLogin() {
        CarUser user = new CarUser();
        user.setLogin("test login 1");
        user.setPassword("test password");
        user = daoUser.save(user);
        user.setLogin("test login updated");
        daoUser.save(user);
        assertThat(daoUser.findById(user.getId()).orElseThrow().getLogin(), is("test login updated"));
    }

    @Test
    public void whenDeleteUserThenNull() {
        CarUser user = new CarUser();
        user.setLogin("test login delete");
        user.setPassword("test password");
        user = daoUser.save(user);
        daoUser.delete(user);
        assertTrue(daoUser.findById(user.getId()).isEmpty());
    }

    @Test
    public void whenGetUserThereTestLogin() {
        CarUser user = new CarUser();
        user.setLogin("test login get");
        user.setPassword("test password");
        user = daoUser.save(user);
        assertThat(daoUser.findById(user.getId()).orElseThrow().getLogin(), is("test login get"));
    }

    @Test
    public void whenAddItemThenGetUsersLogin() {
        CarUser user = new CarUser();
        user.setLogin("test login in item");
        user.setPassword("test password");
        user = daoUser.save(user);
        Item item = new Item();
        item.setUser(user);
        item = dao.save(item);
        assertThat(dao.findById(item.getId()).orElseThrow().getUser().getLogin(), is("test login in item"));
    }

    @Test
    public void whenGetListThenContainsItem() {
        Item item = new Item();
        item.setTitle("test item in list");
        dao.save(item);
        assertTrue(dao.findAll().contains(item));
    }

    @Test
    public void whenGetListOnTodayItContainsOnlyForToday() {
        Item itemToday = new Item();
        itemToday.setTitle("test item today");
        itemToday.setCreated(LocalDateTime.now());
        dao.save(itemToday);
        Item itemYesterday = new Item();
        itemYesterday.setTitle("test item today");
        itemYesterday.setCreated(LocalDateTime.now().minusDays(1));
        dao.save(itemYesterday);
        assertTrue(dao.findByCreatedGreaterThanEqualAndCreatedLessThanOrderByIdDesc(LocalDate.now())
                .contains(itemToday));
        assertFalse(dao.findByCreatedGreaterThanEqualAndCreatedLessThanOrderByIdDesc(LocalDate.now())
                .contains(itemYesterday));
    }

    @Test
    public void whenGetListForBrandItContainsBrand() {
        Brand brandAudi = new Brand();
        brandAudi.setTitle("audi");
        brandAudi = daoBrand.save(brandAudi);
        Item itemAudi = new Item();
        itemAudi.setBrand(brandAudi);
        dao.save(itemAudi);
        Brand brandBMW = new Brand();
        brandBMW.setTitle("BMW");
        brandBMW = daoBrand.save(brandBMW);
        Item itemBMW = new Item();
        itemBMW.setBrand(brandBMW);
        dao.save(itemBMW);
        assertTrue(dao.findByBrandIdOrderByIdDesc(brandAudi.getId()).contains(itemAudi));
        assertFalse(dao.findByBrandIdOrderByIdDesc(brandAudi.getId()).contains(itemBMW));
    }

    @Test
    public void whenGetListWithImgItContainsOnlyWithImg() {
        Item itemImg = new Item();
        itemImg.setPicture("picture...");
        dao.save(itemImg);
        Item itemWOImg = new Item();
        dao.save(itemWOImg);
        assertTrue(dao.findByPictureNotNullOrderByIdDesc().contains(itemImg));
        assertFalse(dao.findByPictureNotNullOrderByIdDesc().contains(itemWOImg));
    }

    @Test
    public void whenAuthValidUserThenTrue() {
        CarUser user = new CarUser();
        user.setLogin("test auth");
        user.setPassword("test password");
        daoUser.save(user);
        assertThat(daoUser.findByLoginAndPassword("test auth", "test password"), is(user));
    }

    @Test
    public void whenAuthInvalidUserThenFalse() {
        CarUser user = new CarUser();
        user.setLogin("test auth false");
        user.setPassword("test password");
        daoUser.save(user);
        assertNull(daoUser.findByLoginAndPassword("test auth false", "wrong password"));
    }
}