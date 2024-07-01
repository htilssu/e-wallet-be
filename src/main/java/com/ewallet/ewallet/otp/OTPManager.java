package com.ewallet.ewallet.otp;

import com.ewallet.ewallet.service.OTPGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OTPManager {

    OTPGenerator otpGenerator;

    public void send(OTPSender otpSender, OTPData otpData) {

        otpGenerator.generateOTP().thenAccept(otp -> {
            otpSender.sendOTP(otpData.getSendTo(), otp);

        });
    }

}
