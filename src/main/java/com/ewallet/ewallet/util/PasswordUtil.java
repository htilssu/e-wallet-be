package com.ewallet.ewallet.util;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.Encoder;
import java.security.SecureRandom;

@NoArgsConstructor
@AllArgsConstructor
@Service
public final class PasswordUtil {

    @Value("${password.salt}")
    private String salt;
    private BCryptPasswordEncoder encoder;

    public static String hashPassword(String password) {
        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder(10,
                                          new SecureRandom(System.getenv("SALT").getBytes())
                );


        return encoder.encode(password);
    }

    public boolean verifyPassword(String password, String hashedPassword) {
        return encoder.matches(password, hashedPassword);
    }

    @PostConstruct
    public void init() {
        encoder = new BCryptPasswordEncoder(10, new SecureRandom(salt.getBytes()));
    }
}
