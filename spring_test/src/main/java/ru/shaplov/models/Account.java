package ru.shaplov.models;

import javax.persistence.*;
import java.util.Objects;

/**
 * Entity for account management.
 *
 * @author shaplov
 * @since 02.09.2019
 */
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String accountId;

    @Column(nullable = false)
    private String password;

    @Transient
    private transient String decodedPass;

    public Account() {
    }

    public String getDecodedPass() {
        return decodedPass;
    }

    public void setDecodedPass(String decodedPass) {
        this.decodedPass = decodedPass;
    }

    public int getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account account = (Account) o;
        return id == account.id
                && Objects.equals(accountId, account.accountId)
                && Objects.equals(password, account.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, password);
    }
}
