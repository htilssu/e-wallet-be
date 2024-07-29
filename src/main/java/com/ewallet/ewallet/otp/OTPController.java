package com.ewallet.ewallet.otp;

import com.ewallet.ewallet.dto.response.ResponseMessage;
import com.ewallet.ewallet.models.User;
import com.ewallet.ewallet.service.EmailService;
import com.ewallet.ewallet.util.OTPUtil;
import com.ewallet.ewallet.service.otp.SmsService;
import com.ewallet.ewallet.user.UserRepository;
import com.ewallet.ewallet.util.ObjectUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v?/otp", produces = "application/json; charset=utf-8")
public class OTPController {

    private final OTPManager otpManager;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final SmsService smsService;

    @PostMapping
    public ResponseEntity<?> sendOtp(@RequestBody @Nullable OTPData otpData,
            Authentication authentication) {

        if (otpData == null) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Data is invalid"));
        }

        switch (otpData.getOtpType()) {
            case "email":
                otpManager.send(emailService, otpData, authentication);
                Optional<User> user = userRepository.findById((String) authentication.getPrincipal());

                if (user.isPresent()) {
                    return ResponseEntity.ok(ObjectUtil.mergeObjects(
                            new ResponseMessage("OTP đã được gửi đến email của bạn!"),
                            ObjectUtil.wrapObject("email", user.get().getEmail()),
                            ObjectUtil.wrapObject("expire", OTPUtil.getExpiryTime())));
                } else {
                    return ResponseEntity.badRequest()
                            .body(new ResponseMessage("Không tìm thấy người dùng"));
                }

            case "phone":
                otpManager.send(smsService, otpData, authentication);
                return ResponseEntity.ok(new ResponseMessage("OTP đã được gửi đến số điện thoại của bạn!"));

            default:
                return ResponseEntity.badRequest()
                        .body(new ResponseMessage("OTP type is invalid"));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<ResponseMessage> verifyOtp(@RequestBody OTPData otpData,
            Authentication authentication) {
        if (authentication != null) {
            String userId = (String) authentication.getPrincipal();
            boolean verifyStatus = otpManager.verify(userId, otpData);

            if (verifyStatus) {
                return ResponseEntity.ok(new ResponseMessage("Mã OTP hợp lệ"));
            } else {
                return ResponseEntity.status(401)
                        .body(new ResponseMessage("Mã OTP không hợp lệ"));
            }
        }

        return ResponseEntity.badRequest()
                .body(new ResponseMessage("Người dùng không hợp lệ"));
    }
}
