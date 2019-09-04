package ru.shaplov.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.shaplov.models.Account;
import ru.shaplov.repository.AccountRepository;

/**
 * @author shaplov
 * @since 03.09.2019
 */
@Service
public class AccountValidator implements Validator {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountValidator(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Account.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Account account = (Account) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "accountId", "NotEmpty");
        if (accountRepository.findByAccountId(account.getAccountId()) != null) {
            errors.rejectValue("accountId", "Duplicate.accountId");
        }
        if (account.getAccountId().length() < 5 || account.getAccountId().length() > 32) {
            errors.rejectValue("accountId", "Size.accountId");
        }
    }
}
