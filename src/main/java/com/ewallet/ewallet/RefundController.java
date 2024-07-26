package com.ewallet.ewallet;

import com.ewallet.ewallet.dto.mapper.TransactionMapperImpl;
import com.ewallet.ewallet.dto.request.RefundRequest;
import com.ewallet.ewallet.dto.response.RefundResponse;
import com.ewallet.ewallet.models.Transaction;
import com.ewallet.ewallet.repository.TransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v?/refund", produces = "application/json;charset=UTF-8")
public class RefundController {

    private final TransactionRepository transactionRepository;
    private final TransactionMapperImpl transactionMapperImpl;

    public RefundController(TransactionRepository transactionRepository,
            TransactionMapperImpl transactionMapperImpl) {
        this.transactionRepository = transactionRepository;
        this.transactionMapperImpl = transactionMapperImpl;
    }

    @PostMapping
    public ResponseEntity<?> refund(@RequestBody RefundRequest transactionRequest) {
        if (transactionRequest.getTransactionId() == null) {
            return ResponseEntity.badRequest().build();
        }

        //TODO: check orderId

        final Optional<Transaction> transactionOptional = transactionRepository.findById(
                transactionRequest.getTransactionId());

        if (transactionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final Transaction transaction = transactionOptional.get();

        switch (transaction.getStatus()) {
            case "REFUNDED":
                return ResponseEntity.badRequest().body(new RefundResponse(transactionMapperImpl.toResponse(transaction),
                        "Giao dịch đã được hoàn tiền trước đó"));
            case "PENDING":
                return ResponseEntity.badRequest().body(new RefundResponse(transactionMapperImpl.toResponse(transaction),
                        "Giao dịch đang chờ xử lý"));
            case "FAILED":
                return ResponseEntity.badRequest().body(new RefundResponse(transactionMapperImpl.toResponse(transaction),
                        "Giao dịch đã thất bại"));
            case "SUCCESS":
                break;
            default:
                return ResponseEntity.badRequest().body(new RefundResponse(transactionMapperImpl.toResponse(transaction),
                        "Giao dịch không hợp lệ"));
        }


        transaction.setStatus("refunded");
        transactionRepository.save(transaction);

        return ResponseEntity.ok(new RefundResponse(transactionMapperImpl.toResponse(transaction),
                "Hoàn tiền thành công"));
    }

}
