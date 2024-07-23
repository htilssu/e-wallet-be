package com.ewallet.ewallet.service.otp;

import lombok.Getter;

public class OTPUtil {

    /**
     * Thời gian hết hạn của mã OTP
     */
    @Getter
    static long expiryTime = 60 * 5; // 5 minutes
}
