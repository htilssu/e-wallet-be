package com.ewallet.ewallet.otp;

import com.ewallet.ewallet.service.EmailService;
import com.ewallet.ewallet.service.SmsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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
    SmsService smsService;

    @PostMapping
    public Mono<ResponseEntity<String>> sendOtp(@RequestBody @Nullable OTPData otpdata) {

        if (otpdata == null) {
            return Mono.just(ResponseEntity.badRequest().body("Data is invalid"));
        }

        switch (otpdata.getOtpType()) {
            case "email" -> {
                otpManager.send(emailService, otpdata);
                return Mono.just(ResponseEntity.ok().body("OTP đã được gửi đến email của bạn!"));
            }

            case "phone" -> {

                otpManager.send(smsService, otpdata);
                return Mono.just(ResponseEntity.ok().body("OTP đã được gửi đến số điện thoại của bạn!"));
            }
            default -> {
                return Mono.just(ResponseEntity.badRequest().body("Data is invalid"));
            }
        }

    }

    @PostMapping("/verify")
    public void verifyOtp(@RequestBody OTPData otpData) {
        otpManager.verify(otpData);
    }
}
