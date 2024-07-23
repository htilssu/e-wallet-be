package com.ewallet.ewallet.transaction;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface WalletTransactionRepository
        extends ReactiveCrudRepository<WalletTransaction, String> {

    @Query("INSERT INTO wallet_transaction (id, sender_wallet, receiver_wallet) VALUES " +
            "(:#{#walletTransaction.getId()}, :#{#walletTransaction.getSenderWallet()}, " +
            ":#{#walletTransaction.getReceiverWallet()})" )
    Mono<WalletTransaction> insert(WalletTransaction walletTransaction);

}
