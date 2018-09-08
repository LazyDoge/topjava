package ru.javawebinar.topjava.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

@Component
public class UserDuplicateValidator implements Validator {


    @Autowired
    UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        UserTo user = (UserTo) target;

        User byEmail = null;
        try {
            byEmail = userService.getByEmail(user.getEmail());
        } catch (NotFoundException e) {
            return;
        }
        if (byEmail != null && SecurityUtil.authUserId() != byEmail.getId()) {
            errors.rejectValue("email", "user.duplicate");
        }

    }
}
