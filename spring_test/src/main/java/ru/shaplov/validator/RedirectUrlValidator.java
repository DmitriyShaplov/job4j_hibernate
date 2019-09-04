package ru.shaplov.validator;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.shaplov.models.RedirectUrl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shaplov
 * @since 03.09.2019
 */
@Service
public class RedirectUrlValidator implements Validator {

    private final List<Integer> redirectTypes = new ArrayList<>(List.of(301, 302));

    @Override
    public boolean supports(Class<?> aClass) {
        return RedirectUrl.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RedirectUrl redirectUrl = (RedirectUrl) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "url", "NotEmpty");
        if (!redirectTypes.contains(redirectUrl.getRedirectType())) {
            errors.rejectValue("redirectUrl", "Wrong.redirectType");
        }
        try {
            new URL(redirectUrl.getUrl());
        } catch (MalformedURLException e) {
            errors.rejectValue("url", "Wrong.url");
        }
    }
}
