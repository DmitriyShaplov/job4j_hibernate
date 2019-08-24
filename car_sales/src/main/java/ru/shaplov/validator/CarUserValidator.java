package ru.shaplov.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.shaplov.logic.ILogicUser;
import ru.shaplov.models.CarUser;

/**
 * @author shaplov
 * @since 14.08.2019
 */
@Service
public class CarUserValidator implements Validator {

    private final ILogicUser logicUser;

    @Autowired
    public CarUserValidator(ILogicUser logicUser) {
        this.logicUser = logicUser;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CarUser.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CarUser user = (CarUser) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "login", "NotEmpty");
        if (logicUser.findByLogin(user.getLogin()) != null) {
            errors.rejectValue("login", "Duplicate.user", "Duplicate user");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tel", "NotEmpty");
    }
}
