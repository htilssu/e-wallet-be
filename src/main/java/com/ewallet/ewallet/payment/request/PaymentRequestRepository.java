package com.ewallet.ewallet.payment.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PaymentRequestRepository extends ReactiveCrudRepository<PaymentRequest,String> {

}
