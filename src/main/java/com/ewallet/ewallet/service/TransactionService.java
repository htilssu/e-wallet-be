package com.ewallet.ewallet.service;

import com.ewallet.ewallet.dto.mapper.TransactionMapper;
import com.ewallet.ewallet.dto.response.TransactionResponse;
import com.ewallet.ewallet.dto.response.WalletTransactionResponse;
import com.ewallet.ewallet.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final WalletTransactionService walletTransactionService;

    @Transactional(readOnly = true)
    public TransactionResponse getTransactionDetail(String id) {
        // Lấy giao dịch từ repository
        var transaction = transactionRepository.findById(id).orElse(null);

        if (transaction == null) {
            // Xử lý khi không tìm thấy giao dịch
            throw new RuntimeException("Transaction not found");
        }

        // Chuyển đổi giao dịch thành response
        var transactionResponse = transactionMapper.toResponse(transaction);

        // Kiểm tra loại giao dịch
        if ("wallet".equals(transaction.getTransactionTarget())) {
            // Lấy thông tin giao dịch ví từ dịch vụ
            var walletTransaction = walletTransactionService.findWalletTransaction(id);

            if (walletTransaction != null) {
            }
        }
        // TODO: Xử lý các loại giao dịch khác nếu cần

        return transactionResponse;
    }
}
