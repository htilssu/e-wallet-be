package com.ewallet.ewallet.util;

import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

@NoArgsConstructor
public final class PasswordUtil {
    public static String hashPassword(String password) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10,
                new SecureRandom(System.getenv("SALT")
                        .getBytes()));

        return encoder.encode(password);
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10,
                new SecureRandom(System.getenv("SALT")
                        .getBytes()));

        return encoder.matches(password, hashedPassword);
    }
}
