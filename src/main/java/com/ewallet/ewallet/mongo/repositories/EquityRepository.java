package com.ewallet.ewallet.mongo.repositories;

import com.ewallet.ewallet.mongo.documents.Equity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface EquityRepository extends MongoRepository<Equity, String> {

    Optional<Equity> findByUser(String user);
    @Async
    CompletableFuture<Equity> findByUserAndMonthAndYear(String user,
            Integer month,
            Integer year);
}
