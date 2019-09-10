package ru.shaplov.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import ru.shaplov.models.Account;
import ru.shaplov.models.RedirectUrl;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RedirectUrlRepositoryTest {

    @Autowired
    private RedirectUrlRepository urlRepository;

    @Autowired
    private AccountRepository accountRepository;

    private Account testAccount;
    private RedirectUrl testRedirect;

    @Before
    public void createAccount() {
        Account account = new Account();
        account.setAccountId("test-account");
        account.setPassword("test-password");
        testAccount = accountRepository.save(account);
        testRedirect = new RedirectUrl();
        testRedirect.setUrl("https://www.google.com");
        testRedirect.setShortURL("http://localhost:8080/short");
        testRedirect.setAccount(testAccount);
        urlRepository.save(testRedirect);
    }

    @After
    public void deleteAccount() {
        accountRepository.delete(testAccount);
        urlRepository.delete(testRedirect);
    }

    @Test
    public void whenCreateNewUrl() {
        assertThat(urlRepository.findById(testRedirect.getId()).orElse(null), is(testRedirect));
    }

    @Test
    public void whenFindByShortURLAndAccountAccountId() {
        assertThat(urlRepository.findByShortURLAndAccountAccountId(
                testRedirect.getShortURL(), testRedirect.getAccount().getAccountId()),
                is(testRedirect));
    }

    @Test
    public void whenFindByAccountIdThenListOfAccounts() {
        assertTrue(urlRepository.findByAccountAccountId(testRedirect.getAccount().getAccountId())
                .contains(testRedirect));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void whenTwoSameUrlOnOneAccountThenException() {
        RedirectUrl redirectUrl = new RedirectUrl();
        redirectUrl.setUrl("https://www.google.com");
        redirectUrl.setShortURL("http://localhost:8080/short1");
        redirectUrl.setAccount(testAccount);
        urlRepository.save(redirectUrl);
        urlRepository.delete(redirectUrl);
    }

    @Test
    public void whenTwoSameUrlOnDifferentAccountsThenOk() {
        Account account = new Account();
        account.setAccountId("test-account-2");
        account.setPassword("password");
        accountRepository.save(account);
        RedirectUrl redirectUrl = new RedirectUrl();
        redirectUrl.setUrl("https://www.google.com");
        redirectUrl.setShortURL("http://localhost:8080/short");
        redirectUrl.setAccount(account);
        urlRepository.save(redirectUrl);
        assertThat(
                urlRepository.findByShortURLAndAccountAccountId(redirectUrl.getShortURL(),
                        account.getAccountId()).getUrl(),
                is(urlRepository.findByShortURLAndAccountAccountId(testRedirect.getShortURL(),
                        testAccount.getAccountId()).getUrl()));
    }

    @Test
    public void whenIncrementCountInUrl() {
        assertThat(testRedirect.getCount(), is(0L));
        urlRepository.incrementCountInUrl(testRedirect.getShortURL(), testAccount.getAccountId());
        RedirectUrl url = urlRepository.findByShortURLAndAccountAccountId(testRedirect.getShortURL(), testAccount.getAccountId());
        assertThat(url.getCount(), is(1L));
    }
}