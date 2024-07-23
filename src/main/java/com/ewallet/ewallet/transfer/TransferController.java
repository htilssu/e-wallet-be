package com.ewallet.ewallet.transfer;

import com.ewallet.ewallet.model.response.ResponseMessage;
import com.ewallet.ewallet.transaction.Transaction;
import com.ewallet.ewallet.transaction.TransactionRepository;
import com.ewallet.ewallet.transaction.WalletTransaction;
import com.ewallet.ewallet.transaction.WalletTransactionRepository;
import com.ewallet.ewallet.user.UserRepository;
import com.ewallet.ewallet.util.ObjectUtil;
import com.ewallet.ewallet.wallet.Wallet;
import com.ewallet.ewallet.wallet.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v?/transfer", produces = "application/json; charset=UTF-8")
public class TransferController {

    TransactionRepository transactionRepository;
    UserRepository userRepository;
    WalletRepository walletRepository;
    WalletTransactionRepository walletTransactionRepository;

    @PostMapping
    public Mono<ResponseEntity<?>> transfer(@RequestBody TransferData data,
            Authentication authentication) {
        if (data.getMoney() <= 0) {
            return Mono.just(ResponseEntity.badRequest()
                                           .body(new ResponseMessage("Số tiền phải lớn hơn 0")));
        }

        if (data.getSendTo()
                .isEmpty()) {
            return Mono.just(ResponseEntity.badRequest()
                                           .body(new ResponseMessage(
                                                   "Người nhận không được để trống")));
        }

        String senderId = authentication.getName();

        return walletRepository
                .findByOwnerIdAndOwnerType(senderId, "user")
                .flatMap(wallet -> checkWalletBalance(wallet, data.getMoney()))
                .switchIfEmpty(
                        Mono.error(new ReceiverNotFoundException("Không tìm thấy ví của bạn")))
                .flatMap(wallet -> userRepository
                        .findByEmail(data.sendTo)
                        .switchIfEmpty(Mono.error(
                                new ReceiverNotFoundException("Không tìm thấy người nhận")))
                        .flatMap(user -> walletRepository
                                .findByOwnerIdAndOwnerType(
                                        user.getId(),
                                        "user"))
                        .switchIfEmpty(Mono.error(
                                new ReceiverNotFoundException("Không tìm thấy ví người nhận")))
                        .flatMap(receiverWallet -> {
                            wallet.sendMoneyTo(
                                    receiverWallet,
                                    data.getMoney());


                            final Transaction
                                    transaction =
                                    createTransaction(
                                            data.getMoney()
                                    );

                            final Mono<ResponseEntity<?>>
                                    responseEntityMono =
                                    saveTransaction(
                                            transaction,
                                            wallet.getId(),
                                            receiverWallet.getId()
                                    );
                            return walletRepository.saveAll(
                                                           List.of(wallet,
                                                                   receiverWallet))
                                                   .then(responseEntityMono);

                        })
                        .onErrorResume(throwable -> Mono.just(
                                        ResponseEntity.badRequest()
                                                      .body(new ResponseMessage(
                                                              throwable.getMessage())
                                                      )
                                )
                        )
                );
    }

    private Mono<Wallet> checkWalletBalance(Wallet wallet, double money) {
        if (wallet.getBalance() < money || wallet.getBalance() <= 0) {
            return Mono.error(new InsufficientBalanceException("Số dư của bạn không đủ!"));
        }
        return Mono.just(wallet);
    }

    private Transaction createTransaction(double money) {
        return new Transaction(money, null, "transfer", "wallet", "completed", null, null, null);
    }

    private Mono<ResponseEntity<?>> saveTransaction(Transaction transaction,
            int id,
            int receiverWalletId) {
        return transactionRepository
                .save(transaction)
                .flatMap(trans -> transactionRepository.findById(trans.getId()))
                .flatMap(saved -> {
                    WalletTransaction walletTransaction =
                            new WalletTransaction(saved.getId(), id,
                                    receiverWalletId
                            );

                    return walletTransactionRepository
                            .insert(walletTransaction)
                            .then(walletTransactionRepository.findById(saved.getId()))
                            .flatMap(walletTransaction1 -> Mono.just(
                                    ResponseEntity.ok(ObjectUtil.mergeObjects(
                                            ObjectUtil.wrapObject(
                                                    "transaction",
                                                    ObjectUtil.mergeObjects(
                                                            walletTransaction1,
                                                            saved
                                                    )
                                            ),
                                            new ResponseMessage(
                                                    "Chuyển tiền thành " + "c" +
                                                            "ông")))));
                });
    }

}
