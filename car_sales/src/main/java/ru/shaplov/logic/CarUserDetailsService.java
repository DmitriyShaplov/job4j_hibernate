package ru.shaplov.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.models.CarUser;
import ru.shaplov.persistence.UserRepository;
import ru.shaplov.principal.CarUserPrincipal;

import java.util.ArrayList;

/**
 * @author shaplov
 * @since 14.08.2019
 */
@Service
public class CarUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CarUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CarUser user = userRepository.findByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CarUserPrincipal(user.getId(), user.getLogin(), "{noop}" + user.getPassword(), new ArrayList<>());
    }
}