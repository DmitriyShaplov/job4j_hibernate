package ru.shaplov.models;

import javax.persistence.*;
import java.util.Objects;

/**
 * Entity for storing registered urls.
 *
 * @author shaplov
 * @since 02.09.2019
 */
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "shorturl",
                                "account_id"
                        }
                ),
                @UniqueConstraint(
                        columnNames = {
                                "url",
                                "account_id"
                        }
                )
        })
public class RedirectUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String shortURL;

    private int redirectType = 302;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private long count;

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getShortURL() {
        return shortURL;
    }

    public int getRedirectType() {
        return redirectType;
    }

    public Account getAccount() {
        return account;
    }

    public long getCount() {
        return count;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setShortURL(String shortURL) {
        this.shortURL = shortURL;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setRedirectType(int redirectType) {
        this.redirectType = redirectType;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RedirectUrl that = (RedirectUrl) o;
        return id == that.id && redirectType == that.redirectType
                && Objects.equals(url, that.url)
                && Objects.equals(shortURL, that.shortURL)
                && Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, redirectType, url, shortURL, account);
    }
}
