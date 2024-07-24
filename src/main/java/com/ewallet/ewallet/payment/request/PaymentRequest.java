package com.ewallet.ewallet.payment.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
@Table("\"payment_request\"")
public class PaymentRequest {
    @Id
    String id;
    String partnerId;
    double money;
    String voucherId;
    String voucherName;
    String voucherCode;
    double voucherDiscount;
    String status;
    String transactionId;
    LocalDateTime created;
    LocalDateTime updated;
    String externalTransactionId;
}



