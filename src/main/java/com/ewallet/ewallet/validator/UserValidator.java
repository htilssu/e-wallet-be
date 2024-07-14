package com.ewallet.ewallet.validator;

import com.ewallet.ewallet.user.User;
import com.ewallet.ewallet.util.DateTimeUtil;

import java.time.Instant;
import java.time.ZoneId;

public class UserValidator {

    public static boolean isValidateUser(User user) {
        if (user.getEmail() == null || user.getEmail()
                .isEmpty() || !EmailValidator.isValid(user.getEmail())) return false; //check email
        if (user.getFirstName() == null
                || user.getFirstName().isEmpty()
                || user.getLastName() == null
                || user.getLastName()
                .isEmpty()) return false; //check firstname and lastname

        if (user.getDob() == null || user.getDob()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .isAfter(Instant.now())) return false; //dob greater than current date


        if (user.getPassword().length() < 6) return false;
        if (user.getPhoneNumber().length() < 10) return false;
        return true;
    }
}
