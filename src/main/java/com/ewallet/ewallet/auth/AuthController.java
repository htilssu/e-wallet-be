package com.ewallet.ewallet.auth;

import com.ewallet.ewallet.model.ResponseMessage;
import com.ewallet.ewallet.user.User;
import com.ewallet.ewallet.user.UserRepository;
import com.ewallet.ewallet.util.JwtUtil;
import com.ewallet.ewallet.util.ObjectUtil;
import com.ewallet.ewallet.validator.EmailValidator;
import com.ewallet.ewallet.validator.UserValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v?/auth", produces = "application/json; charset=UTF-8")
public class AuthController {

    UserRepository userRepository;

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestBody AuthData user,
                                         BCryptPasswordEncoder bCryptPasswordEncoder) {

        if (user.getUsername() == null || user.getPassword() == null) {
            return Mono.just(ResponseEntity.ok()
                                     .body(new ResponseMessage(
                                             "Đăng nhập thất bại, vui lòng kiểm tra lại thông tin")));
        }

        if (!EmailValidator.isValid(user.getUsername())) {
            return Mono.just(ResponseEntity.ok()
                                     .body(new ResponseMessage(
                                             "Đăng nhập thất bại, vui lòng kiểm tra lại thông tin")));
        }

        final Mono<User> foundedUser = userRepository.findByEmail(user.getUsername()); //login by email


        return foundedUser.map(u -> {
                    if (u == null) {
                        return ResponseEntity.status(200)
                                .body(new ResponseMessage("Người dùng không tồn tại"));
                    }

                    if (bCryptPasswordEncoder.matches(user.getPassword(), u.getPassword())) {
                        u.setPassword(null);
                        return ResponseEntity.status(200)
                                .header("Set-Cookie",
                                        "token=" + JwtUtil.generateToken(u) + "; Path=/; SameSite=None; Secure; Partitioned; Max-Age=99999;"
                                )
                                .body(ObjectUtil.mergeObjects(ObjectUtil.wrapObject("user", u),
                                                              new ResponseMessage("Đăng nhập thành công")
                                      )
                                );
                    }
                    else {
                        return ResponseEntity.ok()
                                .body(new ResponseMessage(
                                        "Mật khẩu không đúng"));
                    }
                })
                .switchIfEmpty(Mono.defer(() -> Mono.just(ResponseEntity.ok(new ResponseMessage(
                        "Tài khoản không tồn tại trong hệ thống"))))); // if not exist any user

    }

    @GetMapping("/logout")
    public Mono<String> logout() {
        return Mono.just("Đăng xuất thành công!");
    }

}
