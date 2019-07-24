package ru.shaplov.models;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Objects;

/**
 * @author shaplov
 * @since 13.07.2019
 */
@Entity
@Table(name = "car_users")
public class CarUser implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    private String tel;

    private Calendar created;

    public CarUser() {
    }

    public CarUser(int id) {
        this.id = id;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getTel() {
        return tel;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CarUser carUser = (CarUser) o;
        return id == carUser.id && Objects.equals(login, carUser.login)
                && Objects.equals(password, carUser.password)
                && Objects.equals(tel, carUser.tel)
                && Objects.equals(created, carUser.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, tel, created);
    }
}
