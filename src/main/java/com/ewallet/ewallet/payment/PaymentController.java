package com.ewallet.ewallet.payment;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping("api/v?/payment")
public class PaymentController {

    @GetMapping("/{id}")
    public Mono<String> getPayment(@PathVariable String id) {
        return Mono.justOrEmpty("Payment made for id: " + id);
    }

    @PostMapping()
    public Mono<String> createPayment(@RequestBody PaymentModel paymentModel) {
        return Mono.justOrEmpty(
                "Payment made for id: " + paymentModel.id + " with amount: " + paymentModel.amount);
    }

}
