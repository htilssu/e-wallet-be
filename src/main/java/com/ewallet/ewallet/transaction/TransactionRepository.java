package com.ewallet.ewallet.transaction;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionRepository extends ReactiveCrudRepository<Transaction, String> {


    @Query("SELECT * FROM transaction WHERE id = :id" +
            " LEFT Join wallet_transaction ON transaction.id = wallet_transaction.id")
    Mono<TransactionDetail> findDetailById(String id);


    @Query("SELECT * FROM transaction where " )
    Flux<WalletTransaction> getAllByUser(String id);
    
}
