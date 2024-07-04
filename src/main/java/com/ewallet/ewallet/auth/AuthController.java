package com.ewallet.ewallet.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v?/auth")
public class AuthController {
    @PostMapping("/login")
    public Mono<String> login() {
        return Mono.just("Login success");
    }

    @GetMapping("/logout")
    public Mono<String> logout() {
        return Mono.just("Logout success");
    }

    @PostMapping("/register")
    public Mono<String> register() {
        return Mono.just("Register success");
    }
}
