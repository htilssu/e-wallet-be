package com.ewallet.ewallet.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {

    public static Object getPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
