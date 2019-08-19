package ru.shaplov.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.models.CarUser;
import ru.shaplov.persistence.UserRepository;

/**
 * @author shaplov
 * @since 30.07.2019
 */
@Service
@Transactional
public class LogicUser implements ILogicUser {

    private final UserRepository userRepository;

    @Autowired
    public LogicUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CarUser save(CarUser user) {
        return userRepository.save(user);
    }

    @Override
    public CarUser authUser(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }

    @Override
    public CarUser findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
