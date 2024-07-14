package com.ewallet.ewallet.util;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.Encoder;
import java.security.SecureRandom;

@NoArgsConstructor
@AllArgsConstructor
@Service
@Configuration
public class PasswordUtil {

    @Value("${password.salt}")
    private String salt;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom(salt.getBytes()));
    }

}
