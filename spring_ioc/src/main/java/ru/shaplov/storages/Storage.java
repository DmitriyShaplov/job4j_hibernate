package ru.shaplov.storages;

/**
 * @author shaplov
 * @since 30.07.2019
 */
public interface Storage {
    User add(User user);
    User findById(int id);
}
