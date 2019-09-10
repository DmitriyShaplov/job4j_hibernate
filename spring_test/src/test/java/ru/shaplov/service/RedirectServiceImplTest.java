package ru.shaplov.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.shaplov.models.Account;
import ru.shaplov.models.RedirectUrl;
import ru.shaplov.repository.AccountRepository;
import ru.shaplov.repository.RedirectUrlRepository;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RedirectServiceImplTest {

    @InjectMocks
    private RedirectServiceImpl redirectService;

    @Mock
    private RedirectUrlRepository redirectUrlRepository;

    @Mock
    private AccountRepository accountRepository;

    @Test
    public void whenRegistryUrlThenUrlWithAccountAndShortUrl() {
        Account account = new Account();
        account.setAccountId("test-account");
        RedirectUrl redirectUrl = new RedirectUrl();
        redirectUrl.setUrl("https://www.google.com");
        when(redirectUrlRepository.save(any(RedirectUrl.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        when(accountRepository.findByAccountId("username"))
                .thenReturn(account);
        redirectUrl = redirectService.registryUrl(redirectUrl, "username");
        assertThat(redirectUrl.getShortURL().length(), is(6));
        assertThat(redirectUrl.getAccount().getAccountId(), is("test-account"));
    }
}
