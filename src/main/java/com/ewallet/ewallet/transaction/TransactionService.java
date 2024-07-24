package com.ewallet.ewallet.transaction;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    /**
     * Lấy thông tin chi tiết giao dịch dựa vào {@code id} của giao dịch,
     * sẽ join các bảng giao dịch có liên quanh để lấy thông tin chi tiết.
     * Nếu không tìm thấy thông tin giao dịch sẽ throw ra {@link TransactionNotFoundException}
     * sử dụng {@link Mono#onErrorResume(Function)} để bắt và xử lý lỗi. Nếu tìm thấy thông tin giao dịch sẽ trả về
     * {@link Mono<TransactionDetail>} chứa thông tin chi tiết giao dịch.
     * @param id id của giao dịch
     *
     * @return trả về {@link Mono<TransactionDetail>} chứa thông tin chi tiết giao dịch
     * @exception TransactionNotFoundException nếu không tìm thấy thông tin giao dịch
     */
    public Mono<TransactionDetail> getTransactionDetail(String id) {
        return transactionRepository.findById(id)
                .flatMap(transaction -> {
                    final Mono<WalletTransaction> walletTransactionMono =
                            walletTransactionRepository.findById(
                                    transaction.getId());

                    return walletTransactionMono.map(
                            walletTransaction -> new TransactionDetail(transaction,
                                    walletTransaction)).switchIfEmpty(Mono.error(
                            new TransactionNotFoundException("Không tìm thấy giao dịch")));
                }).switchIfEmpty(
                        Mono.error(new TransactionNotFoundException("Không tìm thấy giao dịch")));

    }
}
