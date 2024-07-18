package com.ewallet.ewallet.partner;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Data
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/partner", produces = "application/json; charset=UTF-8")
public class PartnerController {

    PartnerRepository partnerRepository;

    @GetMapping("/{id}")
    public Mono<Partner> getPartner(Authentication authentication) {
        Partner partner;


        return Mono.just(new Partner("1", "partner1", "description", "serviceId", "apiKey", "balance", "created"));
    }

}
