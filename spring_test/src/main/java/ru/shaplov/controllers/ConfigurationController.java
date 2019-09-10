package ru.shaplov.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.shaplov.models.Account;
import ru.shaplov.models.RedirectUrl;
import ru.shaplov.service.AccountService;
import ru.shaplov.service.RedirectService;
import ru.shaplov.validator.AccountValidator;
import ru.shaplov.validator.RedirectUrlValidator;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shaplov
 * @since 29.08.2019
 */
@RestController
public class ConfigurationController {

    private static final Logger LOG = LogManager.getLogger(ConfigurationController.class);

    private final AccountService accountService;
    private final RedirectService redirectService;
    private final AccountValidator accountValidator;
    private final RedirectUrlValidator urlValidator;
    private final MessageSource messageSource;

    @Autowired
    public ConfigurationController(AccountService accountService, RedirectService redirectService,
                                   AccountValidator accountValidator, RedirectUrlValidator urlValidator,
                                   MessageSource messageSource) {
        this.accountService = accountService;
        this.redirectService = redirectService;
        this.accountValidator = accountValidator;
        this.urlValidator = urlValidator;
        this.messageSource = messageSource;
    }

    @PostMapping(value = "/account", consumes = "application/json")
    public OpenAccountObj openAccount(@RequestBody Account account,
                                      BindingResult bindingResult, Locale locale) {
        String description;
        accountValidator.validate(account, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errMsg = new StringBuilder();
            StringBuilder descMsg = new StringBuilder();
            for (var error : bindingResult.getAllErrors()) {
                String message = messageSource.getMessage(error.getCode(), null, locale);
                errMsg.append(error.getObjectName())
                .append(": ").append(message)
                        .append(System.lineSeparator());
                descMsg.append(message).append(". ");
            }
            description = descMsg.toString();
            LOG.info(errMsg.toString());
        } else {
            try {
                account = accountService.openAccount(account);
                description = "Your account is opened";
                LOG.info("Account opened. AccountId: " + account.getAccountId());
                return new OpenAccountObj(true, description, account.getDecodedPass());
            } catch (Exception e) {
                description = "Account opening failed";
                LOG.error(description + ". accountId: " + account.getAccountId() + System.lineSeparator() + e.getMessage(), e);
            }
        }
        return new OpenAccountObj(false, description, null);
    }

    @PostMapping(value = "/register", consumes = "application/json")
    public ObjectNode registerURL(@RequestBody RedirectUrl redirectUrlObj,
                                  HttpServletRequest req, Principal principal,
                                  BindingResult bindingResult, Locale locale) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        urlValidator.validate(redirectUrlObj, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errMsg = new StringBuilder();
            for (var error : bindingResult.getAllErrors()) {
                String message = messageSource.getMessage(error.getCode(), null, locale);
                errMsg.append(error.getObjectName())
                        .append(": ").append(message)
                        .append(System.lineSeparator());
            }
            LOG.info(errMsg.toString());
            throw new IllegalStateException(errMsg.toString());
        } else {
            try {
                String accountId = principal.getName();
                redirectUrlObj = redirectService.registryUrl(redirectUrlObj, accountId);
                String baseUrl = req.getRequestURL().toString().replace(req.getRequestURI(), req.getContextPath());
                String url = baseUrl + "/" + redirectUrlObj.getShortURL();
                node.put("shortUrl", url);
                LOG.info("Url registered. Full: " + redirectUrlObj.getUrl() + " Short: " + redirectUrlObj.getShortURL());
                return node;
            } catch (Exception e) {
                LOG.error("Failed to registry URL: " + redirectUrlObj.getUrl() + System.lineSeparator() + e.getMessage(), e);
                throw e;
            }
        }
    }

    @GetMapping("/statistic/{accountId}")
    public Map<String, Long> showStatistics(@PathVariable String accountId) {
        try {
            List<RedirectUrl> redirectUrls = redirectService.findUrlsForAccountId(accountId);
            return redirectUrls.stream().collect(Collectors.toMap(RedirectUrl::getUrl, RedirectUrl::getCount));
        } catch (Exception e) {
            LOG.error("Could not retrieve statistics for " + accountId + ". " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Class for mapping opening account response.
     */
    private static class OpenAccountObj {
        private boolean success;
        private String description;
        private String password;

        private OpenAccountObj(boolean success, String description, String password) {
            this.success = success;
            this.description = description;
            this.password = password;
        }

        public OpenAccountObj() {
        }

        public boolean isSuccess() {
            return success;
        }

        public String getDescription() {
            return description;
        }

        public String getPassword() {
            return password;
        }
    }
}
