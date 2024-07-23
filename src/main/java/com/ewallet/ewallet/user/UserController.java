package com.ewallet.ewallet.user;

import com.ewallet.ewallet.model.response.ResponseMessage;
import com.ewallet.ewallet.validator.UserValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/user", produces = "application/json; charset=UTF-8")
public class UserController {

    UserRepository userRepository;

    @PostMapping("/register")
    public Mono<?> register(@RequestBody User user,
                            BCryptPasswordEncoder bCryptPasswordEncoder) {

        if (!UserValidator.isValidateUser(user)) {
            return Mono.just(ResponseEntity.badRequest()
                                     .body(new ResponseMessage(
                                             "Vui lòng kiểm tra lại thông tin")));
        }


        final Mono<User> foundedUser = userRepository.findByEmail(user.getEmail());


        return foundedUser.map(u -> Mono.just(ResponseEntity.ok()
                                                      .body(new ResponseMessage(
                                                              "Người dùng đã tồn tại"))))
                .switchIfEmpty(Mono.fromSupplier(() -> {
                    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));


                    return userRepository.save(user)
                            .flatMap(savedEntity -> Mono.just(ResponseEntity.ok(new ResponseMessage(
                                    "Đăng ký thành công"))))
                            .onErrorResume(error -> Mono.just(ResponseEntity.ok()
                                                                      .body(new ResponseMessage(
                                                                              error.getCause()
                                                                                      .getMessage()))));
                }));

    }

    @GetMapping()
    public Mono<User> getUser(Authentication authentication) {
        final Mono<User> userMono = userRepository.findById((String) authentication.getPrincipal());

        return userMono.map(user -> {
            user.setPassword(null);
            return user;
        });
    }
}
