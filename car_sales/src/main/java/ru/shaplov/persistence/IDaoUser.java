package ru.shaplov.persistence;

import ru.shaplov.models.CarUser;

/**
 * @author shaplov
 * @since 30.07.2019
 */
public interface IDaoUser {
    CarUser save(CarUser user);
    CarUser authUser(String login, String password);
}
