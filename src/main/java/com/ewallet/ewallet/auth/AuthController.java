package com.ewallet.ewallet.auth;

import com.ewallet.ewallet.model.response.ResponseMessage;
import com.ewallet.ewallet.models.User;
import com.ewallet.ewallet.user.UserRepository;
import com.ewallet.ewallet.util.JwtUtil;
import com.ewallet.ewallet.util.ObjectUtil;
import com.ewallet.ewallet.validator.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/auth", produces = "application/json; charset=UTF-8")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthData authData) {

        if (authData.getUsername() == null || authData.getPassword() == null) {
            return ResponseEntity.status(401)
                    .body(new ResponseMessage("Đăng nhập thất bại, vui lòng kiểm tra lại thông tin"));
        }

        if (!EmailValidator.isValid(authData.getUsername())) {
            return ResponseEntity.status(401)
                    .body(new ResponseMessage("Đăng nhập thất bại, vui lòng kiểm tra lại thông tin"));
        }

        Optional<User> user = userRepository.findByEmail(authData.getUsername());

        if (user.isEmpty()) {
            return ResponseEntity.status(401)
                    .body(new ResponseMessage("Tài khoản không tồn tại trong hệ thống"));
        }

        if (bCryptPasswordEncoder.matches(authData.getPassword(), user.get().getPassword())) {
            user.get().setPassword(null);
            String token = JwtUtil.generateToken(user.orElse(null));
            return ResponseEntity.ok()
                    .header("Set-Cookie", "token=" + token + "; Path=/; SameSite=None; Secure; Max-Age=99999;")
                    .body(ObjectUtil.mergeObjects(
                            ObjectUtil.wrapObject("user", user.get()),
                            new ResponseMessage("Đăng nhập thành công"),
                            ObjectUtil.wrapObject("token", token)));
        } else {
            return ResponseEntity.status(401)
                    .body(new ResponseMessage("Mật khẩu không đúng"));
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Đăng xuất thành công!");
    }
}
