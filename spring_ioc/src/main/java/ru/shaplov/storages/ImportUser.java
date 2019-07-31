package ru.shaplov.storages;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author shaplov
 * @since 30.07.2019
 */
public class ImportUser {

    private final Storage storage;

    public ImportUser(final Storage storage) {
        this.storage = storage;
    }

    public User add(User user) {
        return storage.add(user);
    }

    public User findById(int id) {
        return storage.findById(id);
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-context-jdbc.xml");
        ImportUser importUser = context.getBean(ImportUser.class);
        User user = new User();
        user.setName("Admin");
        user = importUser.add(user);
        user = importUser.findById(user.getId());
        System.out.println(user);
    }
}
