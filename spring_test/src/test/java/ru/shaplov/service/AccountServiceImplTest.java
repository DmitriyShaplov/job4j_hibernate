package ru.shaplov.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.shaplov.models.Account;
import ru.shaplov.repository.AccountRepository;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    public void whenOpenAccountThenGeneratedPassword() {
        Account account = new Account();
        account.setAccountId("test-account");
        assertNull(account.getPassword());
        assertNull(account.getDecodedPass());
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(passwordEncoder.encode(any(String.class))).thenReturn("password");
        accountService.openAccount(account);
        assertNotNull(account.getPassword());
        assertNotNull(account.getDecodedPass());
        assertThat(account.getPassword(), is("password"));
        assertThat(account.getDecodedPass().length(), is(8));
    }
}