package com.ewallet.ewallet.transfer;

import com.ewallet.ewallet.dto.mapper.TransactionMapper;
import com.ewallet.ewallet.dto.request.TransactionRequest;
import com.ewallet.ewallet.dto.response.TransactionResponse;
import com.ewallet.ewallet.model.response.ResponseMessage;
import com.ewallet.ewallet.models.Transaction;
import com.ewallet.ewallet.models.User;
import com.ewallet.ewallet.models.WalletTransaction;
import com.ewallet.ewallet.repository.TransactionRepository;
import com.ewallet.ewallet.repository.WalletTransactionRepository;
import com.ewallet.ewallet.service.TransactionService;
import com.ewallet.ewallet.transfer.exceptions.InsufficientBalanceException;
import com.ewallet.ewallet.transfer.exceptions.ReceiverNotFoundException;
import com.ewallet.ewallet.user.UserRepository;
import com.ewallet.ewallet.wallet.Wallet;
import com.ewallet.ewallet.wallet.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v?/transfer", produces = "application/json; charset=UTF-8")
public class TransferController {

    private final TransactionService transactionService;
    TransactionRepository transactionRepository;
    UserRepository userRepository;
    WalletRepository walletRepository;
    WalletTransactionRepository walletTransactionRepository;
    TransactionMapper transactionMapper;

    @PostMapping
    public ResponseEntity<?> transfer(@RequestBody TransactionRequest data,
            Authentication authentication) {
        if (data.getMoney() <= 0) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Số tiền phải lớn hơn 0"));
        }

        if (data.getSendTo().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Người nhận không được để trống"));
        }

        String senderId = authentication.getName();
        Optional<Wallet> optionalSenderWallet = walletRepository.findByOwnerIdAndOwnerType(senderId, "user");

        if (optionalSenderWallet.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ReceiverNotFoundException("Không tìm thấy ví của bạn"));
        }

        Wallet senderWallet = optionalSenderWallet.get();

        if (senderWallet.getBalance() < data.getMoney() || senderWallet.getBalance() <= 0) {
            return ResponseEntity.badRequest()
                    .body(new InsufficientBalanceException("Số dư của bạn không đủ!"));
        }

        Optional<User> optionalReceiver = userRepository.findByEmail(data.getSendTo());

        if (optionalReceiver.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ReceiverNotFoundException("Không tìm thấy người nhận"));
        }

        User receiver = optionalReceiver.get();
        Optional<Wallet> optionalReceiverWallet = walletRepository.findByOwnerIdAndOwnerType(receiver.getId(), "user");

        if (optionalReceiverWallet.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ReceiverNotFoundException("Không tìm thấy ví người nhận"));
        }

        Wallet receiverWallet = optionalReceiverWallet.get();
        senderWallet.sendMoneyTo(receiverWallet, data.getMoney());

        Transaction transaction = transactionMapper.toEntity(data);
        transactionRepository.save(transaction);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .id(transaction.getId())
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .build();

        walletTransactionRepository.save(walletTransaction);

        walletRepository.saveAll(List.of(senderWallet, receiverWallet));

        TransactionResponse transactionResponse = transactionService.getTransactionDetail(transaction.getId());

        return ResponseEntity.ok(transactionResponse);
    }
}
