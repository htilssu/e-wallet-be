package com.ewallet.ewallet.partner;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PartnerRepository extends ReactiveCrudRepository<Partner, String> {

    Mono<Partner> getPartnerById(String id);
    Mono<Partner> getPartnerByApiKey(String apiKey);
}
