package ru.shaplov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.shaplov.models.RedirectUrl;

import java.util.List;

/**
 * @author shaplov
 * @since 02.09.2019
 */
public interface RedirectUrlRepository extends JpaRepository<RedirectUrl, Integer> {
    RedirectUrl findByShortURLAndAccountAccountId(String shortUrl, String accountId);
    List<RedirectUrl> findByAccountAccountId(String accountId);

    @Modifying(clearAutomatically = true)
    @Query("update RedirectUrl r set r.count = r.count + 1 where r.id in (select r2.id from RedirectUrl r2 where r.shortURL = ?1 and r.account.accountId = ?2)")
    int incrementCountInUrl(String shortURL, String accountId);
}
