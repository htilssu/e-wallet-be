package com.ewallet.ewallet;

import com.ewallet.ewallet.link_service.ServiceRepository;
import com.ewallet.ewallet.security.filter.ApiServiceFilter;
import com.ewallet.ewallet.security.filter.TokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ApiServiceFilter apiServiceFilter) throws Exception {
        http.anonymous(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                                           session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        ;


        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/api/v?/auth/**")
                    .permitAll()
//                    .requestMatchers("/user/**")
//                    .hasRole("USER")
//                    .requestMatchers("/admin/**")
//                    .hasRole("ADMIN")
                    .anyRequest()
                    .fullyAuthenticated()
            ;
        });


        //add token filter to security filter chain
        http.addFilterBefore(tokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(apiServiceFilter, TokenFilter.class);




        http.headers(httpz -> {
            httpz.httpStrictTransportSecurity(HeadersConfigurer.HstsConfig::disable);
        });

        http.exceptionHandling(e -> {
            e.authenticationEntryPoint((request, response, authException) -> {
                response.setStatus(401);
            });
        });

        return http.build();
    }

    @Bean
    public TokenFilter tokenFilter() {
        return new TokenFilter();
    }

    @Bean
    public ApiServiceFilter apiServiceFilter(ServiceRepository serviceRepository) {
        return new ApiServiceFilter(serviceRepository);
    }
}
