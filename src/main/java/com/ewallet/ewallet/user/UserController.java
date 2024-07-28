package com.ewallet.ewallet.user;

import com.ewallet.ewallet.dto.mapper.UserMapperImpl;
import com.ewallet.ewallet.model.response.ResponseMessage;
import com.ewallet.ewallet.models.User;
import com.ewallet.ewallet.dto.response.UserDto;
import com.ewallet.ewallet.models.Wallet;
import com.ewallet.ewallet.wallet.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/user", produces = "application/json; charset=UTF-8")
public class UserController {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapperImpl userMapperImpl;


    @GetMapping()
    public ResponseEntity<UserDto> getUser(Authentication authentication) {
        Optional<User> user = userRepository.findById((String) authentication.getPrincipal());
        if (user.isPresent()) {
            user.get().setPassword(null);
            return ResponseEntity.ok(userMapperImpl.toDto(user.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/wallet")
    public ResponseEntity<Wallet> getWallet(Authentication authentication) {
        Optional<Wallet> wallet = walletRepository.findByOwnerIdAndOwnerType((String) authentication.getPrincipal(), "user");
        return wallet.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/password")
    public ResponseEntity<ResponseMessage> changePassword(@RequestBody ChangePasswordData passwordData,
            Authentication authentication) {
        Optional<User> user = userRepository.findById((String) authentication.getPrincipal());

        if (user.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Người dùng không hợp lệ"));
        }

        if (passwordData.getOldPassword().equals(passwordData.getNewPassword())) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Mật khẩu mới không được trùng với mật khẩu cũ"));
        }

        if (!bCryptPasswordEncoder.matches(passwordData.getOldPassword(), user.get().getPassword())) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Mật khẩu cũ không đúng"));
        }

        user.get().setPassword(bCryptPasswordEncoder.encode(passwordData.getNewPassword()));
        try {
            userRepository.save(user.get());
            return ResponseEntity.ok(new ResponseMessage("Đổi mật khẩu thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Đổi mật khẩu thất bại"));
        }
    }
}
