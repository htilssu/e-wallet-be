package com.ewallet.ewallet.link_service;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ServiceRepository extends ReactiveCrudRepository<Service, String> {

    Mono<Service> findServiceById(String id);
    Mono<Service> findServiceByApiKey(String apiKey);
}
