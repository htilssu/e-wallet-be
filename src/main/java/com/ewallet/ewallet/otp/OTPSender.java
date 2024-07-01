package com.ewallet.ewallet.otp;

import java.util.concurrent.CompletableFuture;

public interface OTPSender {

    void sendOTP(String sendTo, String otp);
    CompletableFuture<Void> sendOTPAsync(String sendTo, String otp);

}
