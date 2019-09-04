package ru.shaplov.service;

import ru.shaplov.models.RedirectUrl;

import java.util.List;

/**
 * @author shaplov
 * @since 02.09.2019
 */
public interface RedirectService {

    /**
     * Registry new URL in service.
     * @param redirectUrlObj redirect obj contains full url.
     * @param accountId current accountId.
     * @return RedirectURL obj with filled shortUrl field.
     */
    RedirectUrl registryUrl(RedirectUrl redirectUrlObj, String accountId);

    /**
     * Get Redirect Url for specified user and short url.
     * @param shortUrl short url.
     * @param accountId current account id.
     * @return Redirect url obj.
     */
    RedirectUrl getRedirectAndIncrement(String shortUrl, String accountId);

    /**
     * Find all redirect urls for current account id.
     * @param accountId current account id.
     * @return List of RedirectUrl.
     */
    List<RedirectUrl> findUrlsForAccountId(String accountId);
}
