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
    BCryptPasswordEncoder bCryptPasswordEncoder;

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
                            .flatMap(savedEntity -> Mono.just(
                                    ResponseEntity.ok(new ResponseMessage(
                                            "Đăng ký thành công"))))
                            .onErrorResume(
                                    error -> Mono.just(ResponseEntity.ok()
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

    @PostMapping("/password")
    public Mono<ResponseEntity<ResponseMessage>> changePassword(@RequestBody ChangePasswordData passwordData,
            Authentication authentication) {
        final Mono<User> userMono = userRepository.findById((String) authentication.getPrincipal());

        return userMono.flatMap(user -> {
            if (passwordData.getOldPassword()
                    .equals(passwordData.getNewPassword())) {
                return Mono.just(ResponseEntity.badRequest()
                        .body(new ResponseMessage(
                                "Mật khẩu mới không được trùng với mật " +
                                        "khẩu cũ")));
            }

            if (!bCryptPasswordEncoder.matches(passwordData.getOldPassword(), user.getPassword())) {
                return Mono.just(ResponseEntity.badRequest()
                        .body(new ResponseMessage(
                                "Mật khẩu cũ không đúng")));
            }

            user.setPassword(bCryptPasswordEncoder.encode(passwordData.getNewPassword()));

            return userRepository.save(user)
                    .flatMap(savedEntity -> Mono
                            .just(ResponseEntity
                                    .ok()
                                    .body(new ResponseMessage(
                                            "Đổi mật khẩu thành công"))))
                    .onErrorResume(error -> Mono
                            .just(ResponseEntity
                                    .badRequest()
                                    .body(new ResponseMessage(
                                            "Đổi mật khẩu thất bại"))));
        });
    }
}
