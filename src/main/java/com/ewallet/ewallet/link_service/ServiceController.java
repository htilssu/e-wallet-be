package com.ewallet.ewallet.link_service;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/service", produces = "application/json; charset=UTF-8")
public class ServiceController {

    @PostMapping("/create")
    public Mono<Service> createService(@RequestBody Service service) {

        return Mono.just(service);
    }

    @PutMapping()
    public Mono<Service> updateService(@RequestBody Service service) {


        return Mono.just(service);
    }
}
