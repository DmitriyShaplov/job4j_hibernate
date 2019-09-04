package ru.shaplov.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.models.Account;
import ru.shaplov.models.RedirectUrl;
import ru.shaplov.repository.AccountRepository;
import ru.shaplov.repository.RedirectUrlRepository;

import java.util.List;

/**
 * @author shaplov
 * @since 02.09.2019
 */
@Service
@Transactional
public class RedirectServiceImpl implements RedirectService {

    private final RedirectUrlRepository redirectUrlRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public RedirectServiceImpl(RedirectUrlRepository redirectUrlRepository, AccountRepository accountRepository) {
        this.redirectUrlRepository = redirectUrlRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Registry new URL in service.
     * @param redirectUrlObj redirect obj contains full url.
     * @param accountId current accountId.
     * @return RedirectURL obj with filled shortUrl field.
     */
    @Override
    public RedirectUrl registryUrl(RedirectUrl redirectUrlObj, String accountId) {
        String generatedUrl = RandomStringUtils.randomAlphanumeric(6);
        redirectUrlObj.setShortURL(generatedUrl);
        Account account = accountRepository.findByAccountId(accountId);
        if (account == null) {
            throw new IllegalStateException("There is no account with such id.");
        }
        redirectUrlObj.setAccount(account);
        return redirectUrlRepository.save(redirectUrlObj);
    }

    /**
     * Get Redirect Url for specified user and short url.
     * @param shortUrl short url.
     * @param accountId current account id.
     * @return Redirect url obj.
     */
    @Override
    public RedirectUrl getRedirectAndIncrement(String shortUrl, String accountId) {
        RedirectUrl result = redirectUrlRepository.findByShortURLAndAccountAccountId(shortUrl, accountId);
        result.setCount(result.getCount() + 1);
        result = redirectUrlRepository.save(result);
        return result;
    }

    /**
     * Find all redirect urls for current account id.
     * @param accountId current account id.
     * @return List of RedirectUrl.
     */
    @Override
    public List<RedirectUrl> findUrlsForAccountId(String accountId) {
        return redirectUrlRepository.findByAccountAccountId(accountId);
    }
}
