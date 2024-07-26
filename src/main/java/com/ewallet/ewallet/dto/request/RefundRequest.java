package com.ewallet.ewallet.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RefundRequest {
    String transactionId;
    String orderId;
}
