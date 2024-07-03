package com.ewallet.ewallet.otp;

import com.ewallet.ewallet.service.OTPGenerator;
import com.ewallet.ewallet.service.SmsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OTPManager {

    OTPGenerator otpGenerator;

    /**
     * Gửi mã OTP cho người dùng, thông tin người nhận sẽ được lấy từ {@link OTPData#getSendTo()}
     * hàm này sẽ gửi OTP bất đồng bộ
     * {@link OTPSender} là service gửi OTP (ví dụ: gửi qua email, sms) {@link com.ewallet.ewallet.service.EmailService},
     * {@link SmsService}
     *
     * @param otpSender đối tượng gửi OTP
     * @param otpData   thông tin người nhận và loại OTP
     */
    public void send(OTPSender otpSender, OTPData otpData) {

        otpGenerator.generateOTP().thenAccept(otp -> otpSender.sendOTP(otpData.getSendTo(), otp));
    }

    /**
     * Xác thực mã OTP có đúng của người dùng hiện tại hay không
     *
     * @param otpData
     */
    public void verify(OTPData otpData) {
    }
}
