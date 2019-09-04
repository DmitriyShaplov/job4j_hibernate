package ru.shaplov.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.models.Account;
import ru.shaplov.repository.AccountRepository;

/**
 * Service for accounts management.
 *
 * @author shaplov
 * @since 02.09.2019
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Open new account with specified accountId.
     * @param account with username.
     * @return created account.
     */
    @Override
    public Account openAccount(Account account) {
        String generatedPassword = RandomStringUtils.randomAlphanumeric(8);
        account.setPassword(passwordEncoder.encode(generatedPassword));
        accountRepository.save(account);
        account.setDecodedPass(generatedPassword);
        return account;

    }
}
