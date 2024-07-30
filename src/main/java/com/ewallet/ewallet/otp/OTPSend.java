package com.ewallet.ewallet.otp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class OTPSend extends OTPData {

    private String otpType;
    @Setter
    private String sendTo;
}
