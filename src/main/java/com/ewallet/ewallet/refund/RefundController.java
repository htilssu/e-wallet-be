package com.ewallet.ewallet.refund;

import com.ewallet.ewallet.dto.mapper.TransactionMapperImpl;
import com.ewallet.ewallet.dto.request.RefundRequest;
import com.ewallet.ewallet.dto.response.RefundResponse;
import com.ewallet.ewallet.models.Transaction;
import com.ewallet.ewallet.payment.request.PaymentRequestRepository;
import com.ewallet.ewallet.repository.TransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v?/refund", produces = "application/json;charset=UTF-8")
public class RefundController {

    private final TransactionRepository transactionRepository;
    private final TransactionMapperImpl transactionMapperImpl;
    private final PaymentRequestRepository paymentRequestRepository;

    public RefundController(TransactionRepository transactionRepository,
            TransactionMapperImpl transactionMapperImpl,
            PaymentRequestRepository paymentRequestRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionMapperImpl = transactionMapperImpl;
        this.paymentRequestRepository = paymentRequestRepository;
    }

    @PostMapping
    public ResponseEntity<?> refund(@RequestBody RefundRequest transactionRequest) {
        if (transactionRequest.getTransactionId() == null && transactionRequest.getOrderId() == null) {
            return ResponseEntity.badRequest().build();
        }


        Transaction transaction;
        if (transactionRequest.getTransactionId() != null) {
            var transactionOptional = transactionRepository.findById(
                    transactionRequest.getTransactionId());
            if (transactionOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            transaction = transactionOptional.get();
        }

        if (transactionRequest.getOrderId() != null) {
            var paymentRequestOptional = paymentRequestRepository.findById(
                    transactionRequest.getOrderId());
            if (paymentRequestOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var paymentRequest = paymentRequestOptional.get();
            transaction = paymentRequest.getTransaction();

        }
        else {
            return ResponseEntity.badRequest().build();
        }


        switch (transaction.getStatus()) {
            case "REFUNDED":
                return ResponseEntity.badRequest().body(
                        new RefundResponse(transactionMapperImpl.toResponse(transaction),
                                "Giao dịch đã được hoàn tiền trước đó"));
            case "PENDING":
                return ResponseEntity.badRequest().body(
                        new RefundResponse(transactionMapperImpl.toResponse(transaction),
                                "Giao dịch đang chờ xử lý"));
            case "FAILED":
                return ResponseEntity.badRequest().body(
                        new RefundResponse(transactionMapperImpl.toResponse(transaction),
                                "Giao dịch đã thất bại"));
            case "SUCCESS":
                break;
            default:
                return ResponseEntity.badRequest().body(
                        new RefundResponse(transactionMapperImpl.toResponse(transaction),
                                "Giao dịch không hợp lệ"));
        }


        transaction.setStatus("REFUNDED");
        transactionRepository.save(transaction);

        return ResponseEntity.ok(new RefundResponse(transactionMapperImpl.toResponse(transaction),
                "Hoàn tiền thành công"));
    }

}
