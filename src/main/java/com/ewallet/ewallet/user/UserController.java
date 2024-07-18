package com.ewallet.ewallet.user;

import com.ewallet.ewallet.model.ResponseMessage;
import com.ewallet.ewallet.validator.UserValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
                    user.setPassword(""); //remove password from response
                    final Mono<User> saved = userRepository.save(user);
                    return saved.map(userSaved -> ResponseEntity.ok(new ResponseMessage(
                            "Đăng ký thành công")));
                }));

    }
}
