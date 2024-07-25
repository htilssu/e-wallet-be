package com.ewallet.ewallet.service;

import com.ewallet.ewallet.dto.mapper.WalletTransactionMapper;
import com.ewallet.ewallet.dto.response.WalletTransactionResponse;
import com.ewallet.ewallet.repository.WalletTransactionRepository;
import com.ewallet.ewallet.transaction.exception.TransactionNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WalletTransactionService {

    private final WalletTransactionMapper walletTransactionMapper;
    private final WalletService walletService;
    private final WalletTransactionRepository walletTransactionRepository;

    public WalletTransactionResponse findWalletTransaction(String id) {
        var walletTransaction = walletTransactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Không tìm thấy giao dịch"));

        var walletTransactionResponse = walletTransactionMapper.toResponse(walletTransaction);
//        var senderWallet = walletService.getWallet(walletTransaction.getSenderWallet());
//        var receiverWallet = walletService.getWallet(walletTransaction.getReceiverWallet());
//
//        walletTransactionResponse.setSenderWallet(senderWallet);
//        walletTransactionResponse.setReceiverWallet(receiverWallet);

        return walletTransactionResponse;
    }
}
