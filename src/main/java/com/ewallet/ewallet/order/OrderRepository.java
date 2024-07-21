package com.ewallet.ewallet.order;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface OrderRepository extends ReactiveCrudRepository<Order,String> {

    @Override
    Mono<Order> findById(@NotNull String s);
}
