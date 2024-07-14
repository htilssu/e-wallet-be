package com.ewallet.ewallet.auth;

import com.ewallet.ewallet.model.ResponseMessage;
import com.ewallet.ewallet.user.User;
import com.ewallet.ewallet.user.UserRepository;
import com.ewallet.ewallet.util.JwtUtil;
import com.ewallet.ewallet.util.PasswordUtil;
import com.ewallet.ewallet.validator.EmailValidator;
import com.ewallet.ewallet.validator.UserValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Mono<ResponseEntity<ResponseMessage>> login(@RequestBody AuthData user,
                                                       BCryptPasswordEncoder bCryptPasswordEncoder) {

        if (!EmailValidator.isValid(user.getUsername())) {
            return Mono.just(ResponseEntity.badRequest()
                                     .body(new ResponseMessage(
                                             "Đăng nhập thất bại, vui lòng kiểm tra lại thông tin")));
        }

        final Mono<User> foundedUser = userRepository.findByEmail(user.getUsername()); //login by email
        //check if not exist any user


        return foundedUser.map(u -> {
            if (u == null) {
                return ResponseEntity.status(200).body(new ResponseMessage("Người dùng không tồn tại"));
            }

            if (bCryptPasswordEncoder.matches(user.getPassword(), u.getPassword())) {
                return ResponseEntity.status(200)
                        .header("Set-Cookie",
                                "token=" + JwtUtil.generateToken(u) + "; Path=/; HttpOnly"
                        )
                        .body(new ResponseMessage(
                                "Đăng nhập thành công"));
            }
            else {
                return ResponseEntity.badRequest()
                        .body(new ResponseMessage(
                                "Mật khẩu không đúng"));
            }
        }).switchIfEmpty(Mono.defer(() -> Mono.just(ResponseEntity.ok(new ResponseMessage("Tài khoản không tồn tại trong hệ thống")))));

    }

    @GetMapping("/logout")
    public Mono<String> logout() {
        return Mono.just("Logout success");
    }

    @PostMapping("/register")
    public Mono<Object> register(@RequestBody User user,
                                 BCryptPasswordEncoder bCryptPasswordEncoder) {

        if (!UserValidator.isValidateUser(user)) {
            return Mono.just(ResponseEntity.badRequest()
                                     .body(new ResponseMessage(
                                             "Đăng ký thất bại, vui lòng kiểm tra lại thông tin")));
        }


        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            final Mono<User> map = userRepository.save(user).map(savedUser -> savedUser);
            return map.map(ResponseEntity::ok);
        } catch (Exception e) {
            return Mono.just(ResponseEntity.badRequest()
                                     .body(new ResponseMessage(
                                             "Đăng ký thất bại, vui lòng kiểm tra lại thông tin")));
        }

    }

}
