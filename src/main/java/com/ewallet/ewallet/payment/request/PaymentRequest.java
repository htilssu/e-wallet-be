package com.ewallet.ewallet.payment.request;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
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



