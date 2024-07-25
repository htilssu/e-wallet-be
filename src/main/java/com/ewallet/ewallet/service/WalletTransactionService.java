package com.ewallet.ewallet.service;

import com.ewallet.ewallet.dto.mapper.WalletTransactionMapper;
import com.ewallet.ewallet.repository.WalletTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WalletTransactionService {

    private final WalletTransactionMapper walletTransactionMapper;
    private final WalletService walletService;
    private final WalletTransactionRepository walletTransactionRepository;

}
