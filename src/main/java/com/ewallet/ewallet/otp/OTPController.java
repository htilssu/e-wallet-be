package com.ewallet.ewallet.otp;

import com.ewallet.ewallet.model.ResponseMessage;
import com.ewallet.ewallet.service.EmailService;
import com.ewallet.ewallet.service.otp.SmsService;
import com.ewallet.ewallet.util.AuthUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public Mono<ResponseEntity<ResponseMessage>> sendOtp(@RequestBody @Nullable OTPData otpdata,
                                                         Authentication authentication) {

        if (otpdata == null) {
            return Mono.just(ResponseEntity.badRequest()
                                     .body(new ResponseMessage("Data is invalid")));
        }

        switch (otpdata.getOtpType()) {
            case "email" -> {
                otpManager.send(emailService, otpdata, authentication);
                return Mono.just(ResponseEntity.ok()
                                         .body(new ResponseMessage(
                                                 "OTP đã được gửi đến email của bạn!")));
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
    public void verifyOtp(@RequestBody OTPData otpData) {
        var principal = AuthUtil.getPrincipal();
        var context = SecurityContextHolder.getContext();
        if (principal instanceof Authentication) {
            var userId = ((Authentication) principal).getName();
        }
    }
}
