package com.ewallet.ewallet.otp;

import com.ewallet.ewallet.model.response.ResponseMessage;
import com.ewallet.ewallet.service.EmailService;
import com.ewallet.ewallet.service.otp.OTPUtil;
import com.ewallet.ewallet.service.otp.SmsService;
import com.ewallet.ewallet.user.UserRepository;
import com.ewallet.ewallet.util.ObjectUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/api/v?/otp", produces = "application/json; charset=utf-8")
public class OTPController {

    OTPManager otpManager;
    EmailService emailService;
    UserRepository userRepository;
    SmsService smsService;

    @PostMapping
    public Mono<?> sendOtp(@RequestBody @Nullable OTPData otpdata,
            Authentication authentication) {

        if (otpdata == null) {
            return Mono.just(ResponseEntity.badRequest()
                    .body(new ResponseMessage("Data is invalid")));
        }

        switch (otpdata.getOtpType()) {
            case "email" -> {
                otpManager.send(emailService, otpdata, authentication);
                return userRepository
                        .findById((String) authentication.getPrincipal())
                        .flatMap(user -> {
                            otpManager.send(emailService, otpdata, authentication);
                            return Mono.just(ResponseEntity.ok(ObjectUtil.mergeObjects(
                                    new ResponseMessage("OTP đã được gửi đến email của bạn!"),
                                    ObjectUtil.wrapObject("email", user.getEmail()),ObjectUtil.wrapObject("expire",
                                            OTPUtil.getExpiryTime()))));
                        }).switchIfEmpty(Mono.defer(() -> Mono.just(ResponseEntity.badRequest()
                                .body(ObjectUtil.mergeObjects(
                                        new ResponseMessage("Không tìm thấy người dùng"))))));
            }

            case "phone" -> {

                otpManager.send(smsService, otpdata, authentication);
                return Mono.just(ResponseEntity.ok()
                        .body(new ResponseMessage(
                                "OTP đã được gửi đến số điện thoại của bạn!")));
            }
            default -> {
                return Mono.just(ResponseEntity.badRequest().body(new ResponseMessage(
                        "OTP type is invalid")));
            }
        }

    }

    @PostMapping("/verify")
    public Mono<ResponseEntity<ResponseMessage>> verifyOtp(@RequestBody OTPData otpData,
            Authentication authentication) {
        if (authentication != null) {
            var userId = (String) authentication.getPrincipal();
            boolean verifyStatus = otpManager.verify(userId, otpData);

            if (verifyStatus)
                return Mono.just(ResponseEntity.ok()
                        .body(new ResponseMessage("Mã OTP hợp lệ")));

            return Mono.just(ResponseEntity.status(401)
                    .body(new ResponseMessage("Mã OTP không hợp lệ")));
        }


        return Mono.just(ResponseEntity.badRequest()
                .body(new ResponseMessage("Người dùng không hợp lệ")));
    }
}
