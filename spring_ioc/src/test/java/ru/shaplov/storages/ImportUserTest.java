package ru.shaplov.storages;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ImportUserTest {

    private ApplicationContext context = new ClassPathXmlApplicationContext("spring-context-memory.xml");

    @Test
    public void whenAddUserThenFindUserByIdNameTestName() {
        ImportUser importUser = context.getBean(ImportUser.class);
        User user = new User();
        user.setName("Test Name");
        user = importUser.add(user);
        assertThat(importUser.findById(user.getId()).getName(), is("Test Name"));
    }
}