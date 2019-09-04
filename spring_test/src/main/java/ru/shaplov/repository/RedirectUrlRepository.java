package ru.shaplov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shaplov.models.RedirectUrl;

import java.util.List;

/**
 * @author shaplov
 * @since 02.09.2019
 */
public interface RedirectUrlRepository extends JpaRepository<RedirectUrl, Integer> {
    RedirectUrl findByShortURLAndAccountAccountId(String shortUrl, String accountId);
    List<RedirectUrl> findByAccountAccountId(String accountId);
}
