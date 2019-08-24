package ru.shaplov.logic;

import ru.shaplov.models.CarUser;

/**
 * @author shaplov
 * @since 30.07.2019
 */
public interface ILogicUser {
    CarUser save(CarUser user);
    CarUser authUser(String login, String password);
    CarUser findByLogin(String login);
}
