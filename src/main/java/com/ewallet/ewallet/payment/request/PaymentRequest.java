package com.ewallet.ewallet.payment.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Data
@Table("\"payment_request\"")
public class PaymentRequest {
    @Id
    String id;
    String partnerId;
    double money;

}



