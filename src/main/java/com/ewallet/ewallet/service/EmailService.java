package com.ewallet.ewallet.service;

import com.ewallet.ewallet.otp.OTPSender;
import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Service
public class EmailService implements OTPSender {

    public final JavaMailSender javaMailSender;
    public String otpTemplate = null;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
        try (InputStream ip = EmailService.class.getResourceAsStream("/mail-template.html")) {
            if (ip != null) {
                otpTemplate = new String(ip.readAllBytes());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    public void sendEmail(String toEmail, String subject, String body) {
        var msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject(subject);
        msg.setText(body);
        javaMailSender.send(msg);
    }

    @Override
    public void sendOTP(String sendTo, String otp) {
        var newMail = javaMailSender.createMimeMessage();
        // set the email recipient

        String htmlText = otpTemplate.replace("{{otp}}", otp);

        try {
            newMail.setSubject("Mã xác thực OTP", "utf-8");
            newMail.addRecipients(MimeMessage.RecipientType.TO, sendTo);
            newMail.setContent(htmlText, "text/html; charset=utf-8");
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }

        javaMailSender.send(newMail);
    }

    @Override
    @Async
    public CompletableFuture<Void> sendOTPAsync(String sendTo, String otp) {
        var newMail = javaMailSender.createMimeMessage();
        // set the email recipient

        String htmlText = otpTemplate.replace("{{otp}}", otp);

        try {
            newMail.setSubject("Mã xác thực OTP", "utf-8");
            newMail.addRecipients(MimeMessage.RecipientType.TO, sendTo);
            newMail.setContent(htmlText, "text/html; charset=utf-8");
        } catch (MessagingException e) {
            CompletableFuture.failedFuture(e);
        }

        javaMailSender.send(newMail);

        return CompletableFuture.completedFuture(null);
    }
}
