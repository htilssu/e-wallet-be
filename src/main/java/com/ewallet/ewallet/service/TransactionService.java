package com.ewallet.ewallet.service;

import com.ewallet.ewallet.dto.mapper.TransactionMapper;
import com.ewallet.ewallet.dto.mapper.WalletTransactionMapperImpl;
import com.ewallet.ewallet.dto.response.TransactionResponse;
import com.ewallet.ewallet.dto.response.WalletTransactionDto;
import com.ewallet.ewallet.repository.TransactionRepository;
import com.ewallet.ewallet.repository.WalletTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final WalletTransactionService walletTransactionService;
    private final WalletTransactionRepository walletTransactionRepository;
    private final WalletTransactionMapperImpl walletTransactionMapperImpl;

    @Transactional(readOnly = true)
    public WalletTransactionDto getTransactionDetail(String id) {
        var transaction = transactionRepository.findById(id).orElse(null);

        if (transaction == null) {
            throw new RuntimeException("Transaction not found");
        }

        var transactionResponse = transactionMapper.toResponse(transaction);

        if ("wallet".equals(transaction.getTransactionTarget())) {
            var walletTransaction = walletTransactionRepository.findById(transaction.getId()).get();

            return walletTransactionMapperImpl.toDto(walletTransaction);
        }
        // TODO: Xử lý các loại giao dịch khác nếu cần

        return null;
    }
}
