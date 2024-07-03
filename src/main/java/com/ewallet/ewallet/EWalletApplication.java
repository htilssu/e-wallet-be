package com.ewallet.ewallet;

import com.ewallet.ewallet.security.filter.TokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableAsync(proxyTargetClass = true)
@SpringBootApplication
@EnableCaching(proxyTargetClass = true)
public class EWalletApplication {

    public static void main(String[] args) {
        SpringApplication
                .run(EWalletApplication.class, args);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(auth -> {
//            auth.requestMatchers("/", "/home")
//                    .permitAll()
//                    .requestMatchers("/user/**")
//                    .hasRole("USER")
//                    .anyRequest()
//                    .authenticated();
//        });

        http.authorizeHttpRequests(auth -> {
            auth.anyRequest().permitAll();
        });

        http.csrf(AbstractHttpConfigurer::disable);

        //add token filter to security filter chain
        http.addFilterBefore(new TokenFilter(), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }


}
