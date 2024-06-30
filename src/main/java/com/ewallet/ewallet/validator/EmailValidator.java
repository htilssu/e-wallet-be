package com.ewallet.ewallet.validator;

import java.util.regex.Pattern;

public class EmailValidator {

    private static final String regex = "^([a-zA-Z0-9.+]+)@([a-zA-Z0-9]+)\\.([a-zA-Z]{2,5})$";
    private static final Pattern pattern = Pattern.compile(regex);

    public static boolean isValid(String email) {
        return pattern.matcher(email).matches();
    }

}
