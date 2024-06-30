package com.ewallet.ewallet.otp;

import com.ewallet.ewallet.service.EmailService;
import com.ewallet.ewallet.service.OTPService;
import com.ewallet.ewallet.service.PhoneService;
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

    EmailService emailService;
    OTPService otpService;
    PhoneService phoneService;

    @PostMapping
    public Mono<ResponseEntity<String>> sendOtp(@RequestBody @Nullable OTPData otpdata) {

        if (otpdata == null) {
            return Mono.just(ResponseEntity.badRequest().body("Data is invalid"));
        }



        switch (otpdata.getOtpType()) {
            case "email" -> {
                otpService.generateOTP().thenAccept(s -> emailService.sendOtp(otpdata.getEmail(), "OTP", s));
                return Mono.just(ResponseEntity.ok().body("OTP đã được gửi đến email của bạn!"));
            }

            case "phone" -> {
                phoneService.sendOtp(otpdata.getPhoneNumber()).thenAccept(s -> System.out.println("OTP đã được gửi đến số điện thoại " + otpdata.getPhoneNumber()));
                return Mono.just(ResponseEntity.ok().body("OTP đã được gửi đến số điện thoại của bạn!"));
            }
            default -> {
                return Mono.just(ResponseEntity.badRequest().body("Data is invalid"));
            }
        }

    }
}
