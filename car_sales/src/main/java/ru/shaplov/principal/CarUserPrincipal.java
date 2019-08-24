package ru.shaplov.principal;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author shaplov
 * @since 14.08.2019
 */
public class CarUserPrincipal extends User {
    private final int id;

    public int getId() {
        return id;
    }

    public CarUserPrincipal(int id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }
}