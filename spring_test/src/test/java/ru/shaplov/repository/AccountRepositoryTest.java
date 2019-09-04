package ru.shaplov.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import ru.shaplov.models.Account;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void whenCreateAccount() {
        Account account = new Account();
        account.setAccountId("test-account");
        account.setPassword("test-password");
        account = accountRepository.save(account);
        assertThat(accountRepository.findById(account.getId()).orElse(null), is(account));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void whenCreateTwoSameAccountsThenException() {
        Account account = new Account();
        account.setAccountId("test-account-same");
        account.setPassword("test-password");
        accountRepository.save(account);
        account = new Account();
        account.setAccountId("test-account-same");
        account.setPassword("123456");
        accountRepository.save(account);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void whenCreateAccountWOPassword() {
        Account account = new Account();
        account.setAccountId("test-account-wo-passsword");
        accountRepository.save(account);
    }
}