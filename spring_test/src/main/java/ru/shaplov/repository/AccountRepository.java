package ru.shaplov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shaplov.models.Account;

/**
 * @author shaplov
 * @since 02.09.2019
 */
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByAccountId(String accountId);
}
