package com.ewallet.ewallet.payment.request;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v?/payment", produces = "application/json; charset=UTF-8")
public class PaymentRequestController {

    PaymentRequestRepository paymentRequestRepository;

    @GetMapping("/{id}")
    public Mono<?> getOrderById(@PathVariable String id, Authentication authentication) {
        final Mono<PaymentRequest> orderMono = paymentRequestRepository.findById(id);
        return orderMono.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<PaymentRequest>> createPayment(@RequestBody PaymentRequest paymentRequest) {
        return paymentRequestRepository.save(paymentRequest)
                .map(ResponseEntity::ok);
    }
}
