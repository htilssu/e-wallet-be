package com.ewallet.ewallet.service;

import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.logging.Logger;

@Service
public class EmailService {

    public final JavaMailSender javaMailSender;
    public  String otpTemplate= null;


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

    public void sendOtp(String toEmail, String subject, String otpCode) {
        var newMail = javaMailSender.createMimeMessage();
        // set the email recipient

        String htmlText = otpTemplate.replace("{{otp}}", otpCode);


        try {
            newMail.setSubject(subject, "utf-8");
            newMail.addRecipients(MimeMessage.RecipientType.TO, toEmail);
            newMail.setContent(htmlText, "text/html; charset=utf-8");
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }

        javaMailSender.send(newMail);
    }
}