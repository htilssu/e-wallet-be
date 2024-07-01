package com.ewallet.ewallet.service;


import com.ewallet.ewallet.otp.OTPSender;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@ConfigurationProperties(prefix = "twilio.config")
@Setter
public class SmsService implements OTPSender {


    private String ACCOUNT_SID;
    private String AUTH_TOKEN;
    private String SERVICE_ID;

    @PostConstruct
    public void init() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Twilio.setRegion("us1");
        Twilio.setEdge("singapore");
    }


    @Override
    public void sendOTP(String sendTo, String otp) {
        try {
            Verification verification = Verification.creator(SERVICE_ID, sendTo, "sms")
                    .create();
            System.out.println(verification.getSid());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    @Async
    public CompletableFuture<Void> sendOTPAsync(String sendTo, String otp) {
        try {
            Verification verification = Verification.creator(SERVICE_ID, sendTo, "sms")
                    .create();
            System.out.println(verification.getSid());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return CompletableFuture.completedFuture(null);

    }
}
