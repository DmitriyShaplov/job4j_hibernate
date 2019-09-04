package ru.shaplov.service;

import ru.shaplov.models.Account;

/**
 * @author shaplov
 * @since 02.09.2019
 */
public interface AccountService {

    /**
     * Open new account with specified accountId.
     * @param account with username.
     * @return created account.
     */
    Account openAccount(Account account);
}
