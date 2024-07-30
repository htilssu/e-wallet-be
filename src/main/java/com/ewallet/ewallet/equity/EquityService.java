package com.ewallet.ewallet.equity;

import com.ewallet.ewallet.models.Transaction;
import com.ewallet.ewallet.mongo.documents.Equity;
import com.ewallet.ewallet.mongo.repositories.EquityRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class EquityService {

    private final EquityRepository equityRepository;

    @Async
    public CompletableFuture<Void> updateEquity(Transaction transaction) {
        var senderId = transaction.getSenderId();
        var receiverId = transaction.getReceiverId();

        var today = LocalDate.now();
        var month = today.getMonthValue();
        var year = today.getYear();

        CompletableFuture<Equity> senderEquity = equityRepository.findByUserAndMonthAndYear(
                senderId, month, year);
        CompletableFuture<Equity> receiverEquity = equityRepository.findByUserAndMonthAndYear(
                receiverId, month, year);

        senderEquity.thenAccept(equity -> {
            if (equity == null) {
                equity = Equity.builder().year(year).month(month).user(senderId).build();
            }
            equityRepository.save(equity);
        });
        return null;
    }

}
