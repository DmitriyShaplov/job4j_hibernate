package ru.shaplov.models;

import java.util.Objects;
import java.util.Set;

/**
 * @author shaplov
 * @since 18.10.2019
 */
public class EventMessage {

    private String username;
    private Set<String> users;
    private EnumState state;

    public String getUsername() {
        return username;
    }

    public Set<String> getUsers() {
        return users;
    }

    public EnumState getState() {
        return state;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }

    public void setState(EnumState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventMessage message = (EventMessage) o;
        return Objects.equals(username, message.username)
                && Objects.equals(users, message.users)
                && state == message.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, users, state);
    }
}
