package com.ewallet.ewallet.mail;

import com.ewallet.ewallet.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v{version}/mail")
public class MailController {

    EmailService mailService;

    @PostMapping()
    public Mono<Void> sendMail() {
        return Mono.empty();
    }
}
