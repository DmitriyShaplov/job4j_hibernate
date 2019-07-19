package ru.shaplov.models;

import java.util.Calendar;
import java.util.Objects;

/**
 * @author shaplov
 * @since 04.07.2019
 */
public class User {

    private int id;
    private String name;
    private Calendar expired;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Calendar getExpired() {
        return expired;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExpired(Calendar expired) {
        this.expired = expired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id == user.id && Objects.equals(name, user.name)
                && Objects.equals(expired, user.expired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, expired);
    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", expired=" + expired
                + '}';
    }
}
