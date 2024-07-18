package com.ewallet.ewallet;

import com.ewallet.ewallet.security.filter.ApiServiceFilter;
import com.ewallet.ewallet.security.filter.TokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableAsync(proxyTargetClass = true)
@SpringBootApplication
@EnableCaching(proxyTargetClass = true)
public class EWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(EWalletApplication.class, args);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   ApiServiceFilter apiServiceFilter) throws
                                                                                      Exception {
        http
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        http.authorizeHttpRequests(auth -> auth.requestMatchers("/api/v?/auth/**",
                                                                "/api/v?/user/register"
                )
                .permitAll()
                .requestMatchers("/api/v?/user/**")
                .hasRole("USER")
                .requestMatchers("/api/v?/partner/**")
                .hasRole("PARTNER")
                .requestMatchers("/api/v?/admin/**")
                .hasRole("ADMIN")
                .anyRequest()
                .fullyAuthenticated());


        //add token filter to security filter chain
        http.addFilterBefore(tokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(apiServiceFilter, TokenFilter.class);


        http.exceptionHandling(e -> {
            e.authenticationEntryPoint((request, response, authException) ->
                                               response.setStatus(401));
        });

        return http.build();
    }

    @Bean
    public TokenFilter tokenFilter() {
        return new TokenFilter();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedOriginPattern("http://*:[*]");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }

}
