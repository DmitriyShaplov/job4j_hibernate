package ru.shaplov.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shaplov.models.CarUser;

/**
 * @author shaplov
 * @since 05.08.2019
 */
public interface UserRepository extends JpaRepository<CarUser, Integer> {

    /**
     * Auth user.
     * @param login User's login.
     * @param password User's password.
     * @return User or null.
     */
    CarUser findByLoginAndPassword(String login, String password);

    CarUser findByLogin(String username);
}
