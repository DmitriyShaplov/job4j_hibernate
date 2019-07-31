package ru.shaplov.logic;

import ru.shaplov.models.CarUser;
import ru.shaplov.persistence.DaoUser;
import ru.shaplov.persistence.IDaoUser;

/**
 * @author shaplov
 * @since 30.07.2019
 */
public class LogicUser implements ILogicUser {

    private final IDaoUser daoUser = DaoUser.getInstance();

    private static final LogicUser INSTANCE = new LogicUser();

    private LogicUser() {
    }

    public static LogicUser getInstance() {
        return INSTANCE;
    }

    @Override
    public CarUser save(CarUser user) {
        return daoUser.save(user);
    }

    @Override
    public CarUser authUser(String login, String password) {
        return daoUser.authUser(login, password);
    }
}
