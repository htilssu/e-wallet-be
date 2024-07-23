package com.ewallet.ewallet.wallet;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Mono;

public interface WalletRepository extends ReactiveCrudRepository<Wallet, String> {

    Mono<Wallet> findByOwnerIdAndOwnerType(String ownerId, String ownerType);
}
