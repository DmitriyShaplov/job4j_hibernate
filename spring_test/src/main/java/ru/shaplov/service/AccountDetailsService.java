package ru.shaplov.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.shaplov.models.Account;
import ru.shaplov.repository.AccountRepository;

import java.util.ArrayList;

/**
 * @author shaplov
 * @since 02.09.2019
 */
@Service
public class AccountDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public AccountDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByAccountId(username);
        if (account == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(account.getAccountId(), account.getPassword(), new ArrayList<>());
    }
}
