package com.ewallet.ewallet.order;

import com.ewallet.ewallet.model.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v?/order", produces = "application/json; charset=UTF-8")
public class OrderController {

    OrderRepository orderRepository;

    @GetMapping("/{id}")
    public Mono<?> getOrderById(@PathVariable String id) {
        final Mono<Order> orderMono = orderRepository.findById(id);
        return orderMono.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Order>> createOrder(@RequestBody Order order) {
        return orderRepository.save(order)
                .map(ResponseEntity::ok);
    }
}
