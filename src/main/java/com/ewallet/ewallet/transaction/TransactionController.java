package com.ewallet.ewallet.transaction;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v?/transaction", produces = "application/json; charset=UTF-8")
public class TransactionController {

    TransactionService transactionService;
    TransactionRepository transactionRepository;

    @GetMapping("/{id}")
    public Mono<?> getTransaction(@PathVariable String id) {
        return transactionService.getTransactionDetail(id)
                .flatMap(transactionDetail ->
                        Mono.just(ResponseEntity.ok((Object) transactionDetail.flatten())))
                .onErrorResume(
                        this::handleException);
    }

    private Mono<? extends ResponseEntity<Object>> handleException(Throwable throwable) {
        if (throwable instanceof TransactionNotFoundException) {
            return Mono.just(ResponseEntity.notFound().build());
        }
        return Mono.just(ResponseEntity.badRequest().body(throwable.getMessage()));
    }

//    @GetMapping("/history")
//    public Flux<?> getTransactionHistory() {
//        return transactionRepository.
//    }

}
