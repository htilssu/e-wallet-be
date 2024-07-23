package com.ewallet.ewallet.otp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OTPData {

    private String otpType;
    private String sendTo;
    private String otp;
}
