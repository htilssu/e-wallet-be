package com.ewallet.ewallet.otp;

import com.ewallet.ewallet.service.otp.OTPGenerator;
import com.ewallet.ewallet.service.otp.SmsService;
import com.ewallet.ewallet.util.DateTimeUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OTPManager {

    OTPGenerator otpGenerator;
    ClaimOTPRepository claimOTPRepository;

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

        ClaimOTPModel claim = new ClaimOTPModel(otpData.getOtp(), otpData.getSendTo(), DateTimeUtil.convertToString(
                Instant.now()));

        claimOTPRepository.save(claim); //save
    }

    /**
     * Xác thực mã OTP có đúng của người dùng hiện tại hay không
     *
     * @param otpData
     *
     * @return
     */
    public boolean verify(String userId, OTPData otpData) {
        var userClaim = claimOTPRepository.findOtpByUserId(userId).join();
        if (userClaim.isExpired()) return false;
        return Objects.equals(otpData.getOtp(), userClaim.getOtp());
    }
}
