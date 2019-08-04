package ru.shaplov.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shaplov.models.CarUser;
import ru.shaplov.persistence.IDaoUser;

/**
 * @author shaplov
 * @since 30.07.2019
 */
@Service
public class LogicUser implements ILogicUser {

    private final IDaoUser daoUser;

    @Autowired
    private LogicUser(IDaoUser daoUser) {
        this.daoUser = daoUser;
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
