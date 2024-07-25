package com.ewallet.ewallet.transaction;

import com.ewallet.ewallet.dto.mapper.TransactionMapper;
import com.ewallet.ewallet.dto.response.WalletTransactionDto;
import com.ewallet.ewallet.models.Transaction;
import com.ewallet.ewallet.repository.TransactionRepository;
import com.ewallet.ewallet.service.TransactionService;
import com.ewallet.ewallet.transaction.exception.TransactionNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/transaction", produces = "application/json; charset=UTF-8")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @GetMapping("/{id}")
    public WalletTransactionDto getTransaction(@PathVariable String id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Không tìm thấy giao dịch"));

        return transactionService.getTransactionDetail(id);

    }

}
